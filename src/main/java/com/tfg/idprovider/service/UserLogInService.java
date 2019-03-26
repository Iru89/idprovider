package com.tfg.idprovider.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.tfg.idprovider.jwt.JSONWebToken;
import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserLogInService {


    private PasswordEncoder passwordEncoder;
    private MongoUserDetailsService mongoUserDetailsService;
    private UserRepository userRepository;

    public UserLogInService(PasswordEncoder passwordEncoder, UserRepository userRepository, MongoUserDetailsService mongoUserDetailsService) throws NoSuchAlgorithmException {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mongoUserDetailsService = mongoUserDetailsService;
        }



    public ResponseEntity logIn(UserLogInDto user) {
        try {
            MyUserDetails myUserDetails = (MyUserDetails) mongoUserDetailsService.loadUserByUsername(user.getUsername());
            MyUser myUser = userRepository.findByUserDetailsId(myUserDetails.get_id());
            KeyPair keyPair = myUser.getKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Algorithm algorithm = getAlgorithm(publicKey, privateKey);
            if (myUserDetails.getPassword().equals(user.getPassword())) {
                Map<String, Object> headers = getHeaderClaims();
                Map<String, Object> payload = getPayloadClaims(myUser);
                String token = JSONWebToken.createToken(algorithm, headers, payload);
                return ResponseEntity.ok().body(token);
            }
        }catch (JWTCreationException e) {
            e.printStackTrace();
            //Si falla la creacio del JWT retornem un HttpStatus 503
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
        //Si el username no coincideix amb el password retornem un HttpStatus 401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    private Date dateExpires() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }

    private Date dateNotBefore() {
        return Date.from(Instant.now());
    }

    private Date dateNow() {
        return Date.from(Instant.now());
    }

    private Algorithm getAlgorithm(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        return Algorithm.RSA512(publicKey, privateKey);
    }

    private Map<String, Object> getHeaderClaims() {
        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("alg", "RS512");
        headerClaims.put("typ", "JWT");
        return headerClaims;
    }

    private Map<String, Object> getPayloadClaims(MyUser myUser){
        Map<String, Object> payloadClaims = new HashMap();
        payloadClaims.put("iss", "auth0");
        payloadClaims.put("sub", myUser.getPersonalData().getEmail());
        //payloadClaims.put("aud", );   //Per indicar a on dona acces
        payloadClaims.put("exp", dateExpires());
        payloadClaims.put("nbf", dateNotBefore());
        payloadClaims.put("iat", dateNow());

        //Claims propies
        payloadClaims.put("username", myUser.getMyUserDetails().getUsername());
        payloadClaims.put("userId", myUser.get_id().toString());
        payloadClaims.put("authorities", myUser.getMyUserDetails().getAuthorities());

        return payloadClaims;
    }
}

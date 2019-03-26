package com.tfg.idprovider.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.tfg.idprovider.jwt.GenerateKeys;
import com.tfg.idprovider.jwt.JSONWebToken;
import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserLogInService {

    private static final int KEY_LENGTH = 1024;

    private PasswordEncoder passwordEncoder;
    private MongoUserDetailsService mongoUserDetailsService;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;


    public UserLogInService(PasswordEncoder passwordEncoder, UserRepository userRepository, MongoUserDetailsService mongoUserDetailsService) throws Exception {
        this.passwordEncoder = passwordEncoder;
        this.mongoUserDetailsService = mongoUserDetailsService;
        GenerateKeys generateKeys = new GenerateKeys(KEY_LENGTH);
        generateKeys.createKeys();
        this.publicKey = (RSAPublicKey) generateKeys.getPublicKey();
        this.privateKey = (RSAPrivateKey) generateKeys.getPrivateKey();
    }



    public ResponseEntity logIn(UserLogInDto user) {
        try {
            MyUser myUserDetails = (MyUser) mongoUserDetailsService.loadUserByUsername(user.getUsername());
            Algorithm algorithm = getAlgorithm();
            if (myUserDetails.getPassword().equals(user.getPassword())) {
                Map<String, Object> headers = getHeaderClaims();
                Map<String, Object> payload = getPayloadClaims(myUserDetails);
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

    private Algorithm getAlgorithm() {
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
        payloadClaims.put("sub", myUser.getEmail());
        //payloadClaims.put("aud", );   //Per indicar a on dona acces
        payloadClaims.put("exp", dateExpires());
        payloadClaims.put("nbf", dateNotBefore());
        payloadClaims.put("iat", dateNow());

        //Claims propies
        payloadClaims.put("username", myUser.getUsername());
        payloadClaims.put("userId", myUser.get_id().toString());
        payloadClaims.put("authorities", myUser.getAuthorities());

        return payloadClaims;
    }
}

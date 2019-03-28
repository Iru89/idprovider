package com.tfg.idprovider.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.tfg.idprovider.jwt.GenerateKeys;
import com.tfg.idprovider.jwt.JSONWebToken;
import com.tfg.idprovider.model.MyUser;
import com.tfg.idprovider.model.dto.UserLogInDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private static final int KEY_LENGTH = 1024;
//    private PasswordEncoder passwordEncoder;
    private MongoUserDetailsService mongoUserDetailsService;
//    private UserRepository userRepository;

    public UserLogInService(MongoUserDetailsService mongoUserDetailsService) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
        this.mongoUserDetailsService = mongoUserDetailsService;

        }



    public ResponseEntity logIn(UserLogInDto user) {
        try {
            MyUser myUser = (MyUser) mongoUserDetailsService.loadUserByUsername(user.getUsername());
            GenerateKeys generateKeys = null;
            try {
                generateKeys = new GenerateKeys(KEY_LENGTH);
            } catch (NoSuchAlgorithmException e) {
                return ResponseEntity.badRequest().body(e.toString());
            }
            final KeyPair keyPair = generateKeys.createKeys();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Algorithm algorithm = getAlgorithm(publicKey, privateKey);
            if (myUser.getPassword().equals(user.getPassword())) {
                Map<String, Object> headers = getHeaderClaims();
                Map<String, Object> payload = getPayloadClaims(myUser);
                String token = JSONWebToken.createToken(algorithm, headers, payload);
                return ResponseEntity.ok().body(token);
            }
        }catch (JWTCreationException e) {
            e.printStackTrace();
            //Si falla la creacion del JWT retornamos un HttpStatus 503
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.toString());
        } catch (UsernameNotFoundException e) {
            //Si el username no coincide con el password retornamos un HttpStatus 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.toString());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username and password do not match");
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
        payloadClaims.put("sub", myUser.getEmail());
        //payloadClaims.put("aud", );   //Per indicar a on dona acces
        payloadClaims.put("exp", dateExpires());
        payloadClaims.put("nbf", dateNotBefore());
        payloadClaims.put("iat", dateNow());

        //Claims propies
        payloadClaims.put("username", myUser.getUsername());
        payloadClaims.put("userId", myUser.getId().toString());
        payloadClaims.put("authorities", myUser.getAuthorities());

        return payloadClaims;
    }
}

package com.tfg.idprovider.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tfg.idprovider.jwt.JSONWebToken;
import com.tfg.idprovider.model.MyUserDetails;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.tfg.idprovider.jwt.JSONWebToken.ISSUER;

@Component
public class JwtTokenProvider {

    private KeyPair keyPair;

    public JwtTokenProvider(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String generateToken(Authentication authentication) throws NoSuchAlgorithmException, JWTCreationException {

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        Algorithm algorithm = getAlgorithm();
        Map<String, Object> headers = getHeaderClaims();
        Map<String, Object> payload = getPayloadClaims(myUserDetails);

        return JSONWebToken.createToken(algorithm, headers, payload);
    }

    public ObjectId getUserIdFromJWT(String token) {
        DecodedJWT decodedJWT = JSONWebToken.decodeToken(token);

        Claim claim = decodedJWT.getClaim("userId");
        if(!claim.isNull()){
            return new ObjectId(claim.asString());
        }
        return null;
    }

    public boolean validateToken(String authToken) throws NoSuchAlgorithmException {
        Algorithm algorithm = getAlgorithm();
        DecodedJWT decodedJWT = JSONWebToken.verifyToken(authToken, algorithm);
        return true;
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

    private Algorithm getAlgorithm() throws NoSuchAlgorithmException {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return Algorithm.RSA512(publicKey, privateKey);
    }

    private Map<String, Object> getHeaderClaims() {
        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put("alg", "RS512");
        headerClaims.put("typ", "JWT");
        return headerClaims;
    }

    private Map<String, Object> getPayloadClaims(MyUserDetails myUserDetails){
        Map<String, Object> payloadClaims = new HashMap();
        payloadClaims.put("iss", ISSUER);
        payloadClaims.put("sub", myUserDetails.getEmail());
        //payloadClaims.put("aud", );   //Per indicar a on dona acces
        payloadClaims.put("exp", dateExpires());
        payloadClaims.put("nbf", dateNotBefore());
        payloadClaims.put("iat", dateNow());

        //Claims propies
        payloadClaims.put("username", myUserDetails.getUsername());
        payloadClaims.put("userId", myUserDetails.getId().toString());
        payloadClaims.put("authorities", myUserDetails.getAuthorities());

        return payloadClaims;
    }
}

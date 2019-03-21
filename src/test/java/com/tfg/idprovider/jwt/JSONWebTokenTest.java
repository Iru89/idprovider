package com.tfg.idprovider.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JSONWebTokenTest {

    private GenerateKeys generateKeys;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public JSONWebTokenTest() throws NoSuchAlgorithmException, IOException {
        this.generateKeys = new GenerateKeys(1024);
        generateKeys.createKeys();
        try {
            this.publicKey = (RSAPublicKey) generateKeys.getPublicKey();
            this.privateKey = (RSAPrivateKey) generateKeys.getPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createToken() {
        Date date = dateExpires();
        Algorithm algorithm = getAlgorithm();
        Map<String, Object> headers = getHeaderClaims();
        Map<String, Object> payload = getPayloadClaims();
        String token = JSONWebToken.createToken(algorithm, headers, payload);
        DecodedJWT decodedJWT = verifyAndDecodeToken(token);

        Assert.assertEquals(headers.get("typ"), decodedJWT.getHeaderClaim("typ").asString());

    }

    private DecodedJWT verifyAndDecodeToken(String token) {
        Algorithm algorithm = getAlgorithm();
        DecodedJWT decodedJWT = JSONWebToken.verifyToken(token, algorithm);
        return JSONWebToken.decodeToken(token);
    }

    private Date dateExpires() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }

    private Date dateNow() {
        return Date.from(Instant.now());
    }

    private Algorithm getAlgorithm() {
        return Algorithm.RSA512(publicKey, privateKey);
    }

    private Map<String, Object> getHeaderClaims() {
        Map<String, Object> headerClaims = new HashMap<String, Object>();
        headerClaims.put("alg", "RS512");
        headerClaims.put("typ", "JWT");
        return headerClaims;
    }

    private Map<String, Object> getPayloadClaims(){
        Map<String, Object> payloadClaims = new HashMap();
        payloadClaims.put("iss", "auth0");
        payloadClaims.put("sub", "o.rave@gmail.com");
        //payloadClaims.put("aud", );   //Per indicar a on dona acces
        payloadClaims.put("exp", dateExpires());
        payloadClaims.put("nbf", dateNow());
        payloadClaims.put("iat", dateNow());

        //Claims propies
        payloadClaims.put("username", "iru89");
        payloadClaims.put("userId", "123456789");
        //payloadClaims.put("authorities", "ROLE_USER");

        return payloadClaims;
    }

}
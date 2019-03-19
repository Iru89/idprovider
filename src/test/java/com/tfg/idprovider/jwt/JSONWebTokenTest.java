package com.tfg.idprovider.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tfg.idprovider.service.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

    public JSONWebTokenTest() throws NoSuchAlgorithmException {
        this.generateKeys = new GenerateKeys(512);
        generateKeys.createKeys();
        this.publicKey = (RSAPublicKey) generateKeys.getPublicKey();
        this.privateKey = (RSAPrivateKey) generateKeys.getPrivateKey();
    }

    @Test
    public void createToken() {
        Date date = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        Algorithm algorithm = getAlgorithm();
        Map<String, Object> headers = getHeaderClaims();
        String token = JSONWebToken.createToken(date, algorithm, headers);

    }

    @Test
    public void verifyToken() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCIsImV4cCI6MTU1MzAxOTM5OH0.VG9PQzXUGk-F9u47I2-rbU1BTrfe5sDANXpeukvge3xibcP2CJmp96zshKj74aXnmcAd-Cwj4aUpWlwBCBe7gw";
        Algorithm algorithm = getAlgorithm();
        DecodedJWT decodedJWT = JSONWebToken.verifyToken(token, algorithm);
        decodedJWT = JSONWebToken.decodeToken(token);
    }

    private Date dateExpires() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
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
}
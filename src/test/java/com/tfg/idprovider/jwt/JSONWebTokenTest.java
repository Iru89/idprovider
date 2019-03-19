package com.tfg.idprovider.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tfg.idprovider.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
        this.generateKeys = new GenerateKeys(1024);
        generateKeys.createKeys();
        this.publicKey = (RSAPublicKey) generateKeys.getPublicKey();
        this.privateKey = (RSAPrivateKey) generateKeys.getPrivateKey();
    }

    @Test
    public void createToken() {
        Date date = dateExpires();
        Algorithm algorithm = getAlgorithm();
        Map<String, Object> headers = getHeaderClaims();
        String token = JSONWebToken.createToken(date, algorithm, headers);
        DecodedJWT decodedJWT = verifyToken(token);

        Assert.assertEquals(headers.get("typ"), decodedJWT.getHeaderClaim("typ").asString());

    }

    private DecodedJWT verifyToken(String token) {
        Algorithm algorithm = getAlgorithm();
        DecodedJWT decodedJWT = JSONWebToken.verifyToken(token, algorithm);
        return JSONWebToken.decodeToken(token);
    }

    private Date dateExpires() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
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

}
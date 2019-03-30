package com.tfg.idprovider.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JSONWebToken {

    public static final String ISSUER = "authIru";

    public static String createToken(Algorithm algorithm, Map<String, Object> headers, Map<String, Object> payload) throws JWTCreationException {
        return JWT.create()
                .withHeader(headers)
                .withIssuer(ISSUER)
                .withSubject(payload.get("sub").toString())
                .withAudience()
                .withExpiresAt((Date) payload.get("exp"))
                .withNotBefore((Date) payload.get("nbf"))
                .withIssuedAt((Date) payload.get("iat"))
                //.withJWTId()
                .withClaim("username", payload.get("username").toString())
                .withClaim("userId", payload.get("userId").toString())
                //.withArrayClaim("authorities", (String[]) payload.get("authorities"))
                .sign(algorithm);
    }

    public static DecodedJWT verifyToken(String token, Algorithm algorithm) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .acceptLeeway(5)            //Aceptem 5 seg de marge en exp nbf i iat
                .build();                   //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }

    public static DecodedJWT decodeToken(String token) throws JWTDecodeException{
        DecodedJWT jwt = JWT.decode(token);
        return jwt;
    }

}

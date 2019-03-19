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


    public JSONWebToken() {

    }

    public static String createToken(Date dateExpires, Algorithm algorithm, Map<String, Object> headers) throws JWTCreationException {
        return JWT.create()
                .withHeader(headers)
                .withIssuer("auth0")
                .withExpiresAt(dateExpires)
                .sign(algorithm);
    }

    public static DecodedJWT verifyToken(String token, Algorithm algorithm) throws JWTVerificationException{
//        DecodedJWT jwt = null;
//        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();                   //Reusable verifier instance
             DecodedJWT jwt = verifier.verify(token);
//        } catch (JWTVerificationException exception){
//            //Invalid signature/claims
//        }
        return jwt;
    }

    public static DecodedJWT decodeToken(String token) throws JWTDecodeException{
//        DecodedJWT jwt = null;
//        try {
            DecodedJWT jwt = JWT.decode(token);
//        } catch (JWTDecodeException exception){
//            //Invalid token
//        }
        return jwt;
    }

}

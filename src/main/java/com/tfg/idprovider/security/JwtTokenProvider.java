package com.tfg.idprovider.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tfg.idprovider.model.MyUserDetails;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenProvider {

    private static final String ISSUER = "authIru";
    private KeyPair keyPair;

    public JwtTokenProvider(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String generateToken(Authentication authentication) throws JWTCreationException {

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        Algorithm algorithm = getAlgorithm(keyPair);
        Map<String, Object> headers = getHeaderClaims();

        return JWT.create()
                .withHeader(headers)
                .withIssuer(ISSUER)
                .withSubject(myUserDetails.getEmail())
                .withAudience()
                .withExpiresAt(dateExpires())
                .withNotBefore(dateNotBefore())
                .withIssuedAt(dateIssuedAt())
                .withClaim("username", myUserDetails.getUsername())
                .withClaim("userId", myUserDetails.getId().toString())
                .withArrayClaim("authorities", getRoles(myUserDetails))
                .sign(algorithm);

    }

    public ObjectId getUserIdFromJWT(String token) {

        try{
            DecodedJWT decodedJWT = JWT.decode(token);

            Claim claim = decodedJWT.getClaim("userId");
            if(!claim.isNull()){
                return new ObjectId(claim.asString());
            }
            return null;
        }catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateToken(String authToken) {
        try{
            Algorithm algorithm = getAlgorithm(keyPair);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .acceptLeeway(5)            //Aceptem 5 seg de marge en exp nbf i iat
                    .build()
                    .verify(authToken);
            return true;
        }catch (JWTVerificationException e ){
            return false;
        }
    }


    private Date dateExpires() {
        return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    }

    private Date dateNotBefore() {
        return Date.from(Instant.now());
    }

    private Date dateIssuedAt() {
        return Date.from(Instant.now());
    }

    private Algorithm getAlgorithm(KeyPair keyPair) {
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

    private String[] getRoles(MyUserDetails myUserDetails) {
        return myUserDetails.getAuthorities().stream().map(a->a.getAuthority()).toArray(String[]::new);
    }

}

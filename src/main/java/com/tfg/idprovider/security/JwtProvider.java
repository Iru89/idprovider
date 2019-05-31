package com.tfg.idprovider.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.model.dto.JwtAuthenticationDto;
import com.tfg.idprovider.repository.UserRepository;
import org.bson.types.ObjectId;
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
public class JwtProvider {

    private static final String ISSUER = "authIru";
    private static final String ID_PROVIDER = "idProvider";
    private static final String GEOMETRIC_RESOURCES = "GeometricResources";

    private KeyPair keyPair;

    public JwtProvider(KeyPair keyPair, UserRepository userRepository) {
        this.keyPair = keyPair;
    }

    public JwtAuthenticationDto generateTokens(MyUserDetails myUserDetails) {

        String accessToken = generateAccessToken(myUserDetails);
        String refreshToken = generateRefreshToken(myUserDetails);

        JwtAuthenticationDto jwt = JwtAuthenticationDto.JwtAuthenticationDtoBuilder
                .builder()
                .withAccessToken(accessToken)
                .withRefreshToken(refreshToken)
                .build();

        return jwt;
    }

    private String generateAccessToken(MyUserDetails myUserDetails) throws JWTCreationException {

        Algorithm algorithm = getAlgorithm(keyPair);
        Map<String, Object> headers = getHeaderClaims();

        return JWT.create()
                .withHeader(headers)
                .withIssuer(ISSUER)
                .withSubject(myUserDetails.getEmail())
                .withAudience(ID_PROVIDER, GEOMETRIC_RESOURCES)
                .withExpiresAt(dateExpiresAccessToken())
                .withNotBefore(dateNotBefore())
                .withIssuedAt(dateIssuedAt())
                .withClaim("username", myUserDetails.getUsername())
                .withClaim("userId", myUserDetails.getId().toString())
                .withArrayClaim("authorities", getRoles(myUserDetails))
                .sign(algorithm);

    }

    private String generateRefreshToken(MyUserDetails myUserDetails){

        Algorithm algorithm = getAlgorithm(keyPair);
        Map<String, Object> headers = getHeaderClaims();

        return JWT.create()
                .withHeader(headers)
                .withIssuer(ISSUER)
                .withJWTId(myUserDetails.getJwtRefreshId().toHexString())
                .withSubject(myUserDetails.getId().toHexString())
                .withAudience(ID_PROVIDER)
                .withExpiresAt(dateExpiresRefreshToken())
                .withNotBefore(dateExpiresAccessToken())
                .withIssuedAt(dateIssuedAt())
                .withClaim("userId", myUserDetails.getId().toString())
                .withArrayClaim("authorities", getRoles(myUserDetails))
                .sign(algorithm);

    }

    public ObjectId getUserIdFromJWT(String token) {

        try{
            DecodedJWT decodedJWT = JWT.decode(token);

            Claim claimUserId = decodedJWT.getClaim("userId");
            if(!claimUserId.isNull()){
                return new ObjectId(claimUserId.asString());
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
                    .withAudience(ID_PROVIDER)
                    .acceptLeeway(5)            //Aceptem 5 seg de marge en exp nbf i iat
                    .build()
                    .verify(authToken);
            return true;
        }catch (JWTVerificationException e ){
            return false;
        }
    }

    public boolean validateTokenRefresh(String tokenRefresh, ObjectId jwtRefreshId) {
        try{
            Algorithm algorithm = getAlgorithm(keyPair);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withJWTId(jwtRefreshId.toHexString())
                    .acceptLeeway(5)            //Aceptem 5 seg de marge en exp nbf i iat
                    .build()
                    .verify(tokenRefresh);
            return true;
        }catch (JWTVerificationException e ){
            return false;
        }
    }

    private Date dateExpiresAccessToken() {
        return Date.from(Instant.now().plus(1, ChronoUnit.MINUTES));
    }

    private Date dateExpiresRefreshToken() {
        return Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
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

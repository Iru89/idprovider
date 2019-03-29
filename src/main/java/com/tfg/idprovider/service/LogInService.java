package com.tfg.idprovider.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class LogInService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    public LogInService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity logIn(UserLogInDto user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsernameOrEmail(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(jwt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //Si falla la creacion del algorithm retornamos un HttpStatus 400
            return ResponseEntity.badRequest().body(e.toString());
        }catch (JWTCreationException e) {
            e.printStackTrace();
            //Si falla la creacion del JWT retornamos un HttpStatus 503
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.toString());
        }
    }
}

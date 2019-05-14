package com.tfg.idprovider.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.model.dto.JwtAuthenticationDto;
import com.tfg.idprovider.model.dto.JwtAuthenticationDto.JwtAuthenticationDtoBuilder;
import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.repository.UserRepository;
import com.tfg.idprovider.security.JwtProvider;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogInService {

    private AuthenticationManager authenticationManager;
    private JwtProvider tokenProvider;
    private MongoUserDetailsService userDetailsService;

    public LogInService(AuthenticationManager authenticationManager, JwtProvider jwtProvider, MongoUserDetailsService userDetailsService ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    public ResponseEntity logIn(UserLogInDto user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsernameOrEmail(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        return getTokens(myUserDetails);
    }

    public ResponseEntity refreshTokens(ObjectId id) {

        MyUserDetails myUserDetails = (MyUserDetails) userDetailsService.loadUserById(id);
        return getTokens(myUserDetails);

    }

    private ResponseEntity getTokens(MyUserDetails myUserDetails) {
        try {

            JwtAuthenticationDto jwt = tokenProvider.generateTokens(myUserDetails);
            return ResponseEntity.ok(jwt);

        }catch (JWTCreationException e) {
            e.printStackTrace();
            //Si falla la creacion del JWT retornamos un HttpStatus 503
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.toString());
        }
    }
}

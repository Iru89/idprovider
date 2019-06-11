package com.tfg.idprovider.service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.model.Role;
import com.tfg.idprovider.model.User;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogInService {

    private AuthenticationManager authenticationManager;
    private JwtProvider tokenProvider;
    private MongoUserDetailsService userDetailsService;
    private UserRepository userRepository;

    public LogInService(AuthenticationManager authenticationManager, JwtProvider tokenProvider, MongoUserDetailsService userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    public ResponseEntity login(UserLogInDto user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsernameOrEmail(),
                            user.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

            return getTokens(myUserDetails);
        }catch (AuthenticationException e){
            return (ResponseEntity.badRequest().body("Bad Credentials"));
        }
    }

    public ResponseEntity refreshTokens(ObjectId id) {

        MyUserDetails myUserDetails = (MyUserDetails) userDetailsService.loadUserById(id);
        return getTokens(myUserDetails);

    }

    private ResponseEntity getTokens(MyUserDetails myUserDetails) {
        try {


            MyUserDetails newMyUserDetails = updateJwtRefreshId(myUserDetails);
            JwtAuthenticationDto jwt = tokenProvider.generateTokens(newMyUserDetails);
            return ResponseEntity.ok(jwt);

        }catch (JWTCreationException e) {
            e.printStackTrace();
            //Si falla la creacion del JWT retornamos un HttpStatus 503
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.toString());
        }
    }

    private MyUserDetails updateJwtRefreshId(MyUserDetails myUserDetails) {
        ObjectId jwtId = new ObjectId();
        List<Role> roles = myUserDetails.getAuthorities().stream()
                .map(role -> {
                    if(role.getAuthority().equals(Role.ROLE_USER.name())){
                        return Role.ROLE_USER;
                    }else if(role.getAuthority().equals(Role.ROLE_ADMIN.name())){
                        return Role.ROLE_ADMIN;
                    }else return null;
                })
                .collect(Collectors.toList());

        User newUser = User.UserBuilder
                .builder()
                .withId(myUserDetails.getId())
                .withjwtRefreshId(jwtId)
                .withUsername(myUserDetails.getUsername())
                .withPassword(myUserDetails.getPassword())
                .withEmail(myUserDetails.getEmail())
                .withAuthorities(roles)
                .withPersonalData(myUserDetails.getPersonalData())
                .build();

        User saveUser = userRepository.save(newUser);

        return MyUserDetails.create(saveUser);
    }
}

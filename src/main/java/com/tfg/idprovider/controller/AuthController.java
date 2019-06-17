package com.tfg.idprovider.controller;

import com.tfg.idprovider.model.MyUserDetails;
import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.model.dto.UserSignUpDto;
import com.tfg.idprovider.service.LogInService;
import com.tfg.idprovider.service.SignUpService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private LogInService logInService;
    private SignUpService signUpService;

    public AuthController(LogInService logInService, SignUpService signUpService) {
        this.logInService = logInService;
        this.signUpService = signUpService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@Valid @RequestBody UserLogInDto user){
        return logInService.login(user);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity createUser(@Valid @RequestBody UserSignUpDto user){
        return signUpService.registerNewUserAccount(user);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity refreshTokens(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        ObjectId userId = myUserDetails.getId();
        return logInService.refreshTokens(userId);
    }
}
package com.tfg.idprovider.controller;

import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.model.dto.UserSignUpDto;
import com.tfg.idprovider.service.LogInService;
import com.tfg.idprovider.service.SignUpService;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/logIn", method = RequestMethod.POST)
    public ResponseEntity getJWT(@Valid @RequestBody UserLogInDto user){
        return logInService.logIn(user);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity createUser(@Valid @RequestBody UserSignUpDto user){
        return signUpService.registerNewUserAccount(user);
    }
}

package com.tfg.idprovider.controller;

import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.model.dto.UserRegistrationDto;
import com.tfg.idprovider.service.UserLogInService;
import com.tfg.idprovider.service.UserRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {


    private UserLogInService userLogInService;
    private UserRegistrationService userRegistrationService;

    public UserController(UserLogInService userLogInService, UserRegistrationService userRegistrationService) {
        this.userLogInService = userLogInService;
        this.userRegistrationService = userRegistrationService;
    }

    @RequestMapping(value = "/logIn", method = RequestMethod.POST)
    public ResponseEntity getJWT(@Valid @RequestBody UserLogInDto user){
        return userLogInService.logIn(user);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity createUser(@Valid @RequestBody UserRegistrationDto user){
        return userRegistrationService.registerNewUserAccount(user);
    }
}

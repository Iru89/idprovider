package com.tfg.idprovider.controller;

import com.tfg.idprovider.exception.UserAlreadyExistException;
import com.tfg.idprovider.model.dto.UserLogInDto;
import com.tfg.idprovider.model.dto.UserRegistrationDto;
import com.tfg.idprovider.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {


    public UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/logIn", method = RequestMethod.POST)
    public ResponseEntity getJWT(UserLogInDto user){
        return userService.logIn(user);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity createUser(UserRegistrationDto user){
        try{
            return ResponseEntity.ok().body(userService.registerNewUserAccount(user));
        }catch (UserAlreadyExistException e){
            return ResponseEntity.badRequest().body(false);
        }
    }
}

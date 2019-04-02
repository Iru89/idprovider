package com.tfg.idprovider.controller;

import com.tfg.idprovider.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity users(){
        return adminService.listUsers();
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity myUser(@PathVariable("username") String username){
        return adminService.getMyUser(username);
    }

}

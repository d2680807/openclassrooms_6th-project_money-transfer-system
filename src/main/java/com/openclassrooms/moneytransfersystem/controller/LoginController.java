package com.openclassrooms.moneytransfersystem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class LoginController {

    @RequestMapping("/*")
    @RolesAllowed("CLIENT")
    public String getClient() {

        return "Bienvenue !";
    }
}

package com.openclassrooms.moneytransfersystem.controller;

import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class UserController {

    @Autowired
    private UserCreationService userCreationService;

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {

        return userCreationService.createUser(user);
    }

    @PostMapping("/createUsers")
    public Collection<User> createUsers(@RequestBody Collection<User> users) {

        return userCreationService.createUsers(users);
    }
}

package com.openclassrooms.moneytransfersystem.controller;

import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class UserController {

    @Autowired
    private UserCreationService userCreationService;

    @Autowired
    private UserReadService userReadService;

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {

        return userCreationService.createUser(user);
    }

    @PostMapping("/createUsers")
    public Collection<User> createUsers(@RequestBody Collection<User> users) {

        return userCreationService.createUsers(users);
    }

    @GetMapping("/users/{id}")
    public User readUserById(@PathVariable Long id) {

        return userReadService.readUserById(id);
    }

    @GetMapping("/users")
    public User readUserByEmail(@RequestParam String email) {

        return userReadService.readUserByEmail(email);
    }

    @GetMapping("/users")
    public Collection<User> readUsers() {

        return userReadService.readUsers();
    }
}

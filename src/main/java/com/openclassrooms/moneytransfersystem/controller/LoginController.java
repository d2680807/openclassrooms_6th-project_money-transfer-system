package com.openclassrooms.moneytransfersystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.model.*;
import com.openclassrooms.moneytransfersystem.model.utility.ListElement;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import com.openclassrooms.moneytransfersystem.service.login.LoginService;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserReadService;
import com.openclassrooms.moneytransfersystem.service.user.UserUpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class LoginController {

    @Autowired
    private UserCreationService userCreationService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserUpdateService userUpdateService;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private LoginService loginService;

    Logger logger = LogManager.getLogger(LoginController.class);

    @GetMapping("")
    public String viewHomePage() {

        logger.debug("[viewHomePage] display: home page");

        return "index";
    }

    @GetMapping("/register")
    public String viewRegistrationForm(Model model) {

        model.addAttribute("user", new User());
        logger.debug("[viewRegistrationForm] display: registration form");

        return "signup";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {

        userCreationService.createUser(user);
        logger.debug("[processRegister] user: " + user);

        return "success";
    }

    @GetMapping("/app")
    public String viewApplication(Model model) throws JsonProcessingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticationName = authentication.getName();
        logger.debug("[viewApplication] authentication: " + authenticationName);

        String balance = loginService.getBalance(authenticationName);
        model.addAttribute("balance", balance);
        logger.debug("[viewApplication] balance: " + balance);

        Requirement requirement = loginService.getRequirement(authenticationName);
        model.addAttribute("requirement", requirement);
        logger.debug("[viewApplication] user id: " + requirement.getUserId());

        Set<String> friendsList = loginService.getFriendsList(authenticationName);
        model.addAttribute("friendsList", friendsList);
        logger.debug("[viewApplication] friendsList: " + friendsList);

        List<ListElement> transfersList = loginService.getTransfersList(authentication.getName());
        model.addAttribute("transfersList", transfersList);
        logger.debug("[viewApplication] transfersList: " + friendsList);

        logger.debug("[viewHomePage] display: app page");

        return "app";
    }

    @PostMapping("/process_topup")
    public String processTopup(Requirement requirement) {

        logger.debug("[processTopup] user id: " + requirement.getUserId());
        logger.debug("[processTopup] amount: " + requirement.getAmount());
        loginService.updateBalance(requirement, true);

        return "index";
    }

    @PostMapping("/process_balance_back")
    public String processBalanceBack(Requirement requirement) {

        logger.debug("[processBalanceBack] user id: " + requirement.getUserId());
        logger.debug("[processBalanceBack] amount: " + requirement.getAmount());
        loginService.updateBalance(requirement, false);

        return "index";
    }

    @PostMapping("/process_friendship")
    public String processFriendship(Requirement requirement) throws JsonProcessingException {

        logger.debug("[processFriendship] user id: " + requirement.getUserId());
        logger.debug("[processFriendship] recipient: " + requirement.getRecipient());
        loginService.addFriend(requirement);

        return "index";
    }

    @PostMapping("/process_transfer")
    public String processTransfer(Requirement requirement) {

        logger.debug("[processTransfer] user id: " + requirement.getUserId());
        logger.debug("[processTransfer] recipient: " + requirement.getRecipient());
        logger.debug("[processTransfer] amount: " + requirement.getAmount());
        logger.debug("[processTransfer] description: " + requirement.getDescription());
        loginService.transferToFriend(requirement);

        return "index";
    }
}

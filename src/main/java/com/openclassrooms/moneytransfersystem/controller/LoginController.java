package com.openclassrooms.moneytransfersystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.model.*;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserReadService;
import com.openclassrooms.moneytransfersystem.service.user.UserUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;
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

    Logger logger = LoggerFactory.getLogger(LoginController.class);

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

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setBalance(0);
        user.setFriendsList("[]");
        logger.debug("[processRegister] user: " + user);

        userCreationService.createUser(user);

        return "success";
    }

    @GetMapping("/app")
    public String viewApplication(Model model) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("[app] User Email: " + authentication.getName());
        User user = userReadService.readUserByEmail(authentication.getName());

        String balance = String.format("%.2f", user.getBalance());
        model.addAttribute("balance", balance);

        List<ListElement> listTransfers = new ArrayList<>();
        user.getTransfers().stream()
                .forEach( t -> {
                    ListElement transfer = new ListElement();
                    String prefix;
                    if (t.getType().equals("OUT")) {
                        prefix = "-";
                        transfer.setRelation(
                                transferRepository.findById(t.getId() + 1).get().getUser().getFirstName()
                        );
                    } else {
                        prefix = "+";
                        transfer.setRelation(
                                transferRepository.findById(t.getId() - 1).get().getUser().getFirstName()
                        );
                    }
                    transfer.setDate(t.getDate().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.FRENCH)));

                    transfer.setDescription(t.getDescription());
                    transfer.setAmount(String.valueOf(prefix + t.getAmount()));
                    listTransfers.add(transfer);
                });

        Collections.sort(listTransfers, Comparator.comparing(ListElement::getDate));
        model.addAttribute("listTransfers", listTransfers);

        Requirement requirement = new Requirement();
        requirement.setUserId(user.getId());
        logger.debug("[app-transfer-back] User ID: " + requirement.getUserId());
        logger.debug("[app-transfer-back] Amount: " + requirement.getAmount());
        model.addAttribute("transferBack", requirement);

        ObjectMapper mapper = new ObjectMapper();
        List<String> friends = mapper.readValue(user.getFriendsList(), List.class);
        model.addAttribute("friends", friends);

        logger.debug("[viewHomePage] display: app page");

        return "app";
    }

    @PostMapping("/process_topup")
    public String processTopup(Requirement requirement) {

        logger.debug("[processTopup] user id: " + requirement.getUserId());
        logger.debug("[processTopup] amount: " + requirement.getAmount());

        userUpdateService.getTopup(requirement);

        return "index";
    }

    @PostMapping("/process_balance_back")
    public String processBalanceBack(Requirement requirement) {

        logger.debug("[processBalanceBack] user id: " + requirement.getUserId());
        logger.debug("[processBalanceBack] amount: " + requirement.getAmount());

        userUpdateService.getBalanceBack(requirement);

        return "index";
    }

    @PostMapping("/process_transfer")
    public String processTransfer(Requirement requirement) {

        logger.debug("[processTransfer] user id: " + requirement.getUserId());
        logger.debug("[processTransfer] recipient: " + requirement.getRecipient());
        logger.debug("[processTransfer] amount: " + requirement.getAmount());
        logger.debug("[processTransfer] description: " + requirement.getDescription());

        userUpdateService.friendTransfer(requirement);

        return "index";
    }

    @PostMapping("/process_friendship")
    public String processFriendship(Requirement requirement) throws JsonProcessingException {

        logger.debug("[processFriendship] user id: " + requirement.getUserId());
        logger.debug("[processFriendship] recipient: " + requirement.getRecipient());

        userUpdateService.addFriend(requirement);

        return "index";
    }
}

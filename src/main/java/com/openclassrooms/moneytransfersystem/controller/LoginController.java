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

        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setBalance(0);
        user.setFriendsList("[]");

        userCreationService.createUser(user);

        return "success";
    }

    @PostMapping("/process_balance_back")
    public String processRegister(Requirements requirements) {

        logger.debug("[process_balance_back] User ID: " + requirements.getUserId());
        logger.debug("[process_balance_back] Amount: " + requirements.getAmount());

        userUpdateService.getBalanceBack(requirements);

        return "index";
    }

    @PostMapping("/process_topup")
    public String processTopup(Requirements requirements) {

        logger.debug("[process_balance_back] User ID: " + requirements.getUserId());
        logger.debug("[process_balance_back] Amount: " + requirements.getAmount());

        userUpdateService.getTopup(requirements);

        return "index";
    }

    @PostMapping("/process_transfer")
    public String processTransfer(Requirements requirements) {

        logger.debug("[process_transfer] User ID: " + requirements.getUserId());
        logger.debug("[process_transfer] Recipient: " + requirements.getRecipient());
        logger.debug("[process_transfer] Amount: " + requirements.getAmount());
        logger.debug("[process_transfer] Description: " + requirements.getDescription());

        userUpdateService.friendTransfer(requirements);

        return "index";
    }

    @PostMapping("/process_friendship")
    public String processFriendship(Requirements requirements) throws JsonProcessingException {

        logger.debug("[process_friendship] User ID: " + requirements.getUserId());
        logger.debug("[process_friendship] Recipient: " + requirements.getRecipient());

        userUpdateService.addFriend(requirements);

        return "index";
    }

    @GetMapping("/app")
    public String listTransfers(Model model) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("[app] User Email: " + authentication.getName());
        User user = userReadService.readUserByEmail(authentication.getName());
        //logger.debug("[app] User Balance: " + user.getBalance());

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

        Requirements requirements = new Requirements();
        requirements.setUserId(user.getId());
        logger.debug("[app-transfer-back] User ID: " + requirements.getUserId());
        logger.debug("[app-transfer-back] Amount: " + requirements.getAmount());
        model.addAttribute("transferBack", requirements);

        ObjectMapper mapper = new ObjectMapper();
        List<String> friends = mapper.readValue(user.getFriendsList(), List.class);
        model.addAttribute("friends", friends);

        return "app";
    }
}

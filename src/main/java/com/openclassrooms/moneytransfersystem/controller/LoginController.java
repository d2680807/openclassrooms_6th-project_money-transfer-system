package com.openclassrooms.moneytransfersystem.controller;

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

import java.time.LocalDateTime;
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

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setBalance(0);
        user.setFriendsList("[]");

        userCreationService.createUser(user);

        return "register_success";
    }

    @PostMapping("/process_balance_back")
    public String processRegister(TransferBack transferBack) {

        logger.debug("[process_balance_back] User ID: " + transferBack.getUserId());
        logger.debug("[process_balance_back] Amount: " + transferBack.getAmount());

        userUpdateService.getBalanceBack(transferBack);

        return "index";
    }

    @PostMapping("/process_topup")
    public String processTopup(TransferBack transferBack) {

        logger.debug("[process_balance_back] User ID: " + transferBack.getUserId());
        logger.debug("[process_balance_back] Amount: " + transferBack.getAmount());

        userUpdateService.getTopup(transferBack);

        return "index";
    }

    @GetMapping("/app")
    public String listTransfers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("[app] User Email: " + authentication.getName());
        User user = userReadService.readUserByEmail(authentication.getName());
        //logger.debug("[app] User Balance: " + user.getBalance());

        String balance = String.format("%.2f", user.getBalance());
        model.addAttribute("balance", balance);

        List<TransferView> listTransfers = new ArrayList<>();
        user.getTransfers().stream()
                .forEach( t -> {
                    TransferView transfer = new TransferView();
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
                    transfer.setDate(t.getDate());

                    transfer.setDescription(t.getDescription());
                    transfer.setAmount(String.valueOf(prefix + t.getAmount()));
                    listTransfers.add(transfer);
                });

        Collections.sort(listTransfers, Comparator.comparing(TransferView::getDate));
        model.addAttribute("listTransfers", listTransfers);

        TransferBack transferBack = new TransferBack();
        transferBack.setUserId(user.getId());
        logger.debug("[app-transfer-back] User ID: " + transferBack.getUserId());
        logger.debug("[app-transfer-back] Amount: " + transferBack.getAmount());
        model.addAttribute("transferBack", transferBack);

        return "app";
    }
}

package com.openclassrooms.moneytransfersystem.controller;

import com.openclassrooms.moneytransfersystem.model.*;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class LoginController {

    @Autowired
    private UserCreationService userCreationService;

    @Autowired
    private UserReadService userReadService;

    @GetMapping("")
    public String viewHomePage() {

        return "index";
    }

    @GetMapping("/app")
    public String listTransfers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userReadService.readUserByEmail(authentication.getName());

        String balance = String.format("%.2f", user.getBalance());
        model.addAttribute("balance", balance);

        List<TransferView> listTransfers = new ArrayList<>();
        user.getTransfers().stream()
                .forEach( t -> {
                    TransferView transfer = new TransferView();
                    transfer.setDate(t.getDate());
                    transfer.setRelation(t.getUser().getFirstName());
                    transfer.setDescription(t.getDescription());
                    if (t.getType() == TransferType.INGOING) {
                        transfer.setAmount("+ " + String.valueOf(t.getAmount()));
                    } else if (t.getType() == TransferType.OUTGOING) {
                        transfer.setAmount("- " + String.valueOf(t.getAmount()));
                    }
                    listTransfers.add(transfer);
                });

        Collections.sort(listTransfers, Comparator.comparing(TransferView::getDate));
        model.addAttribute("listTransfers", listTransfers);

        return "app";
    }
}

package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.ListElement;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FormService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TaxRepository taxRepository;

    Logger logger = LoggerFactory.getLogger(FormService.class);

    public String getBalance(String authenticationName) {

        User user = userRepository.findByEmail(authenticationName);
        String balance = String.format("%.2f", user.getBalance());

        return balance;
    }

    public void updateBalance(Requirement requirement, boolean isTopup) {

        if (requirement.getAmount() == 0) {

            return;
        } else {
            logger.debug("[service-balance-back] User ID: " + requirement.getUserId());
            logger.debug("[service-balance-back] Balance: " + requirement.getAmount());

            Optional<User> optionalUser = userRepository.findById(requirement.getUserId());
            User user = new User();
            if (optionalUser.isPresent()) {
                user.setId(optionalUser.get().getId());
                user.setEmail(optionalUser.get().getEmail());
                user.setPassword(optionalUser.get().getPassword());
                user.setFirstName(optionalUser.get().getFirstName());
                user.setLastName(optionalUser.get().getLastName());
                user.setIbanCode(optionalUser.get().getIbanCode());
                user.setBicCode(optionalUser.get().getBicCode());
                user.setFriendsList(optionalUser.get().getFriendsList());

                Transfer outTransfer = new Transfer();
                outTransfer.setUser(user);
                outTransfer.setDate(LocalDateTime.now());
                outTransfer.setAmount(requirement.getAmount());
                outTransfer.setTax(taxRepository.findByName("DEFAULT").getRate());

                Transfer inTransfer = new Transfer();
                inTransfer.setDate(LocalDateTime.now());
                inTransfer.setAmount(requirement.getAmount());
                inTransfer.setTax(taxRepository.findByName("DEFAULT").getRate());
                inTransfer.setUser(userRepository.findByEmail("app@test.com"));

                if (isTopup) {
                    user.setBalance(optionalUser.get().getBalance() + requirement.getAmount());
                    outTransfer.setType("IN");
                    outTransfer.setDescription("Rechargement depuis votre compte bancaire");
                    inTransfer.setType("OUT");
                    inTransfer.setDescription("Rechargement depuis votre compte bancaire");
                } else {
                    user.setBalance(optionalUser.get().getBalance() - requirement.getAmount());
                    outTransfer.setType("OUT");
                    outTransfer.setDescription("Retrait de solde vers compte bancaire");
                    inTransfer.setType("IN");
                    inTransfer.setDescription("Retrait de solde vers compte bancaire");
                }

                userRepository.save(user);
                transferRepository.save(outTransfer);
                transferRepository.save(inTransfer);
            }
        }
    }

    public Requirement getRequirement(String authenticationName) {

        Requirement requirement = new Requirement();
        requirement.setUserId(userRepository.findByEmail(authenticationName).getId());

        return requirement;
    }

    public Set<String> getFriendsList(String authenticationName) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = userRepository.findByEmail(authenticationName).getFriendsList();
        Set<String> friends = mapper.readValue(jsonString, Set.class);

        return friends;
    }

    public List<ListElement> getTransfersList(String authenticationName) {

        List<ListElement> transfersList = new ArrayList<>();
        userRepository.findByEmail(authenticationName).getTransfers().stream()
                .forEach( t -> {
                    ListElement transfer = new ListElement();
                    String prefix;
                    if (t.getType().equals("OUT")) {
                        prefix = "-";
                        transfer.setRelation(transferRepository.findById(t.getId() + 1).get().getUser().getFirstName());
                    } else {
                        prefix = "+";
                        transfer.setRelation(transferRepository.findById(t.getId() - 1).get().getUser().getFirstName());
                    }
                    transfer.setDate(t.getDate().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.FRENCH)));
                    transfer.setDescription(t.getDescription());
                    transfer.setAmount(String.valueOf(prefix + t.getAmount()));
                    transfersList.add(transfer);
                });
        Collections.sort(transfersList, Comparator.comparing(ListElement::getDate));

        return transfersList;
    }

    public void transferToFriend(Requirement requirement) {

        if (requirement.getAmount() == 0) {

            return;
        }

        Optional<User> optionalUser = userRepository.findById(requirement.getUserId());
        User userUpdated = new User();
        User recipient = userRepository.findByEmail(requirement.getRecipient());;
        if (optionalUser.isPresent() && !Objects.isNull(recipient)) {
            userUpdated.setId(optionalUser.get().getId());
            userUpdated.setEmail(optionalUser.get().getEmail());
            userUpdated.setPassword(optionalUser.get().getPassword());
            userUpdated.setFirstName(optionalUser.get().getFirstName());
            userUpdated.setLastName(optionalUser.get().getLastName());
            userUpdated.setIbanCode(optionalUser.get().getIbanCode());
            userUpdated.setBicCode(optionalUser.get().getBicCode());
            userUpdated.setFriendsList(optionalUser.get().getFriendsList());
            userUpdated.setBalance(optionalUser.get().getBalance() - requirement.getAmount());
            userRepository.save(userUpdated);

            Transfer transfer = new Transfer();
            transfer.setUser(userUpdated);
            transfer.setDate(LocalDateTime.now());
            transfer.setType("OUT");
            transfer.setAmount(requirement.getAmount());
            transfer.setTax(taxRepository.findByName("DEFAULT").getRate());
            transfer.setDescription(requirement.getDescription());
            transferRepository.save(transfer);

            recipient.setBalance(optionalUser.get().getBalance() + requirement.getAmount());
            userRepository.save(recipient);

            Transfer inTransfer = new Transfer();
            inTransfer.setDate(LocalDateTime.now());
            inTransfer.setAmount(requirement.getAmount());
            inTransfer.setTax(taxRepository.findByName("DEFAULT").getRate());
            inTransfer.setDescription(requirement.getDescription());
            inTransfer.setUser(recipient);
            inTransfer.setType("IN");
            transferRepository.save(inTransfer);
        }
    }

    public void addFriend(Requirement requirement) throws JsonProcessingException {

        Optional<User> optionalUser = userRepository.findById(requirement.getUserId());
        User userUpdated = new User();
        User recipient = userRepository.findByEmail(requirement.getRecipient());

        if (optionalUser.isPresent() && !Objects.isNull(recipient)) {
            userUpdated.setId(optionalUser.get().getId());
            userUpdated.setEmail(optionalUser.get().getEmail());
            userUpdated.setPassword(optionalUser.get().getPassword());
            userUpdated.setFirstName(optionalUser.get().getFirstName());
            userUpdated.setLastName(optionalUser.get().getLastName());
            userUpdated.setIbanCode(optionalUser.get().getIbanCode());
            userUpdated.setBicCode(optionalUser.get().getBicCode());
            userUpdated.setBalance(optionalUser.get().getBalance());

            ObjectMapper mapper = new ObjectMapper();
            Set<String> friends = mapper.readValue(optionalUser.get().getFriendsList(), Set.class);
            friends.add(requirement.getRecipient());
            userUpdated.setFriendsList(mapper.writeValueAsString(friends));

            userRepository.save(userUpdated);
        }
    }
}

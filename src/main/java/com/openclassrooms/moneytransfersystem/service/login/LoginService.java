package com.openclassrooms.moneytransfersystem.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.ListElement;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import com.openclassrooms.moneytransfersystem.model.utility.TransferType;
import com.openclassrooms.moneytransfersystem.service.JsonService;
import com.openclassrooms.moneytransfersystem.service.user.UserUpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private TaxRepository taxRepository;
    @Autowired
    private JsonService jsonService;

    private Logger logger = LogManager.getLogger(UserUpdateService.class);

    public String getBalance(String authenticationName) {

        logger.debug("[getBalance] authenticationName: " + authenticationName);
        User user = userRepository.findByEmail(authenticationName);
        String balance = String.format("%.2f", user.getBalance());

        return balance;
    }

    @Transactional
    public void updateBalance(Requirement requirement, boolean isTopup) {

        if (requirement.getAmount() == 0) {

            return;
        } else {
            Optional<User> optionalUser = userRepository.findById(requirement.getUserId());
            User user = new User();
            if (optionalUser.isPresent()) {
                if (optionalUser.get().getBalance() - requirement.getAmount() < 0 && !isTopup) {

                    return;
                } else {
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
                        outTransfer.setType(TransferType.IN);
                        outTransfer.setDescription("Rechargement depuis votre compte bancaire");
                        inTransfer.setType(TransferType.OUT);
                        inTransfer.setDescription("Rechargement depuis votre compte bancaire");
                        transferRepository.save(inTransfer);
                        transferRepository.save(outTransfer);
                    } else {
                        user.setBalance(optionalUser.get().getBalance() - requirement.getAmount());
                        outTransfer.setType(TransferType.OUT);
                        outTransfer.setDescription("Retrait de solde vers compte bancaire");
                        inTransfer.setType(TransferType.IN);
                        inTransfer.setDescription("Retrait de solde vers compte bancaire");
                        transferRepository.save(outTransfer);
                        transferRepository.save(inTransfer);
                    }

                    userRepository.save(user);
                    logger.debug("[updateBalance] updated user: " + user);
                    logger.debug("[updateBalance] outgoing: " + outTransfer);
                    logger.debug("[updateBalance] ingoing: " + inTransfer);
                }
            }
        }
    }

    public Requirement getRequirement(String authenticationName) {

        logger.debug("[getRequirement] authenticationName: " + authenticationName);
        Requirement requirement = new Requirement();
        requirement.setUserId(userRepository.findByEmail(authenticationName).getId());

        return requirement;
    }

    public Set<String> getFriendsList(String authenticationName) throws JsonProcessingException {

        logger.debug("[getFriendsList] authenticationName: " + authenticationName);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = userRepository.findByEmail(authenticationName).getFriendsList();
        Set<String> friends = mapper.readValue(jsonString, Set.class);

        return friends;
    }

    public List<ListElement> getTransfersList(String authenticationName) {

        logger.debug("[getTransfersList] authenticationName: " + authenticationName);
        List<ListElement> transfersList = new ArrayList<>();
        userRepository.findByEmail(authenticationName).getTransfers().stream()
                .forEach(t -> {
                    ListElement transfer = new ListElement();
                    String prefix;
                    String relation;
                    if (t.getType().toString().equals(TransferType.OUT.toString())) {
                        prefix = "-";
                        logger.debug("[getTransfersList] transfer id: " + t.getId());
                        relation = transferRepository.findById(t.getId() + 1).get().getUser().getFirstName();
                        logger.debug("[getTransfersList] relation: " + relation);
                        transfer.setRelation(relation);
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

    @Transactional
    public void transferToFriend(Requirement requirement) {

        logger.debug("[transferToFriend] use id: " + requirement.getUserId());
        logger.debug("[transferToFriend] amount: " + requirement.getAmount());
        if (requirement.getAmount() == 0) {return;}
        Optional<User> optionalUser = userRepository.findById(requirement.getUserId());
        User userUpdated = new User();
        User recipient = userRepository.findByEmail(requirement.getRecipient());
        logger.debug("[transferToFriend] optionalUser: " + optionalUser.get());
        logger.debug("[transferToFriend] recipient: " + recipient);
        if (optionalUser.isPresent() && !Objects.isNull(recipient)) {
            double amount = requirement.getAmount();
            if (optionalUser.get().getBalance() - (amount + (amount * 0.005))  < 0) {

                return;
            } else {
                userUpdated.setId(optionalUser.get().getId());
                userUpdated.setEmail(optionalUser.get().getEmail());
                userUpdated.setPassword(optionalUser.get().getPassword());
                userUpdated.setFirstName(optionalUser.get().getFirstName());
                userUpdated.setLastName(optionalUser.get().getLastName());
                userUpdated.setIbanCode(optionalUser.get().getIbanCode());
                userUpdated.setBicCode(optionalUser.get().getBicCode());
                userUpdated.setFriendsList(optionalUser.get().getFriendsList());
                userUpdated.setBalance(optionalUser.get().getBalance() - (amount + (amount * 0.05)));
                userRepository.save(userUpdated);
                logger.debug("[transferToFriend] userUpdated: " + userUpdated);

                Transfer outTransfer = new Transfer();
                outTransfer.setUser(userUpdated);
                outTransfer.setDate(LocalDateTime.now());
                outTransfer.setType(TransferType.OUT);
                outTransfer.setAmount(requirement.getAmount());
                outTransfer.setTax(taxRepository.findByName("DEFAULT").getRate());
                outTransfer.setDescription(requirement.getDescription());
                transferRepository.save(outTransfer);
                logger.debug("[transferToFriend] outTransfer: " + outTransfer);

                recipient.setBalance(recipient.getBalance() + amount);
                userRepository.save(recipient);
                logger.debug("[transferToFriend] recipient: " + recipient);


                Transfer inTransfer = new Transfer();
                inTransfer.setDate(LocalDateTime.now());
                inTransfer.setAmount(requirement.getAmount());
                inTransfer.setTax(taxRepository.findByName("DEFAULT").getRate());
                inTransfer.setDescription(requirement.getDescription());
                inTransfer.setUser(recipient);
                inTransfer.setType(TransferType.IN);
                transferRepository.save(inTransfer);
                logger.debug("[transferToFriend] inTransfer: " + inTransfer);
            }
        }
    }

    @Transactional
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

            String optionalJsonString = optionalUser.get().getFriendsList();
            logger.debug("[addFriend] optionalJsonString: " + optionalJsonString);
            ObjectMapper mapper = new ObjectMapper();
            Set<String> friendsList = new HashSet<>();
            friendsList.addAll(mapper.readValue(optionalJsonString, Set.class));
            logger.debug("[addFriend] friendsList: " + friendsList);
            friendsList.add(requirement.getRecipient());
            logger.debug("[addFriend] recipient: " + friendsList);
            ObjectWriter objectWriter = mapper.writer();
            userUpdated.setFriendsList(objectWriter.writeValueAsString(friendsList));
            logger.debug("[addFriend] friendsListUpdated: " + friendsList);
            userRepository.save(userUpdated);

            String recipientJsonString = recipient.getFriendsList();
            Set<String> recipientFriendsList = new HashSet<>();
            recipientFriendsList.addAll(mapper.readValue(recipientJsonString, Set.class));
            recipientFriendsList.add(userUpdated.getEmail());
            recipient.setFriendsList(objectWriter.writeValueAsString(recipientFriendsList));
            userRepository.save(recipient);
        }
    }
}

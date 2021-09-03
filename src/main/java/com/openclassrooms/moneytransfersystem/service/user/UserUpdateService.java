package com.openclassrooms.moneytransfersystem.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserUpdateService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TaxRepository taxRepository;

    Logger logger = LoggerFactory.getLogger(UserUpdateService.class);

    public User updateUser(User user) {

        User userUpdated;
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            userUpdated = optionalUser.get();
            //Todo: See if it gets the data properly... If not, set them.
            //userUpdated.setEmail(optionalUser.get().getEmail());
            userRepository.save(userUpdated);
        } else {
            logger.debug("[updateUser] id not found: " + user.getId());

            return new User();
        }
        logger.debug("[updateUser] user: " + userUpdated);

        return userUpdated;
    }

    public void getBalanceBack(Requirement requirement) {

        if (requirement.getAmount() == 0) {

            return;
        }

        logger.debug("[service-balance-back] User ID: " + requirement.getUserId());
        logger.debug("[service-balance-back] Balance: " + requirement.getAmount());

        Optional<User> optionalUser = userRepository.findById(requirement.getUserId());
        User userUpdated = new User();
        if (optionalUser.isPresent()) {
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
            transfer.setDescription("Retrait de solde vers compte bancaire");
            transferRepository.save(transfer);
            //logger.debug(transfer.getUser().toString());
            /*logger.debug("[service-balance-back] OUT: " + transfer.getUser().getId() + "|"
                    + transfer.getDate() + "|" + transfer.getType() + "|"
                    + transfer.getAmount() + "|" + transfer.getDescription());*/

            Transfer inTransfer = new Transfer();
            inTransfer.setDate(LocalDateTime.now());
            inTransfer.setAmount(requirement.getAmount());
            inTransfer.setTax(taxRepository.findByName("DEFAULT").getRate());
            inTransfer.setDescription("Retrait de solde vers compte bancaire");
            inTransfer.setUser(userRepository.findByEmail("app@test.com"));
            inTransfer.setType("IN");
            transferRepository.save(inTransfer);
            /*logger.debug("[service-balance-back] IN: " + transfer.getUser().getId() + "|"
                    + transfer.getDate() + "|" + transfer.getType() + "|"
                    + transfer.getAmount() + "|" + transfer.getDescription());*/
        }
    }

    public void getTopup(Requirement requirement) {

        if (requirement.getAmount() == 0) {

            return;
        }

        Optional<User> optionalUser = userRepository.findById(requirement.getUserId());
        User userUpdated = new User();
        if (optionalUser.isPresent()) {
            userUpdated.setId(optionalUser.get().getId());
            userUpdated.setEmail(optionalUser.get().getEmail());
            userUpdated.setPassword(optionalUser.get().getPassword());
            userUpdated.setFirstName(optionalUser.get().getFirstName());
            userUpdated.setLastName(optionalUser.get().getLastName());
            userUpdated.setIbanCode(optionalUser.get().getIbanCode());
            userUpdated.setBicCode(optionalUser.get().getBicCode());
            userUpdated.setFriendsList(optionalUser.get().getFriendsList());
            userUpdated.setBalance(optionalUser.get().getBalance() + requirement.getAmount());
            userRepository.save(userUpdated);

            Transfer transfer = new Transfer();
            transfer.setUser(userUpdated);
            transfer.setDate(LocalDateTime.now());
            transfer.setType("IN");
            transfer.setAmount(requirement.getAmount());
            transfer.setTax(taxRepository.findByName("DEFAULT").getRate());
            transfer.setDescription("Rechargement depuis votre compte bancaire");
            transferRepository.save(transfer);
            //logger.debug(transfer.getUser().toString());
            /*logger.debug("[service-balance-back] OUT: " + transfer.getUser().getId() + "|"
                    + transfer.getDate() + "|" + transfer.getType() + "|"
                    + transfer.getAmount() + "|" + transfer.getDescription());*/

            Transfer inTransfer = new Transfer();
            inTransfer.setDate(LocalDateTime.now());
            inTransfer.setAmount(requirement.getAmount());
            inTransfer.setTax(taxRepository.findByName("DEFAULT").getRate());
            inTransfer.setDescription("Rechargement depuis votre compte bancaire");
            inTransfer.setUser(userRepository.findByEmail("app@test.com"));
            inTransfer.setType("OUT");
            transferRepository.save(inTransfer);
            /*logger.debug("[service-balance-back] IN: " + transfer.getUser().getId() + "|"
                    + transfer.getDate() + "|" + transfer.getType() + "|"
                    + transfer.getAmount() + "|" + transfer.getDescription());*/
        }
    }

    public void friendTransfer(Requirement requirement) {

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

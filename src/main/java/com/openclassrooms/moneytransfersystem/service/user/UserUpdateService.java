package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.TransferBack;
import com.openclassrooms.moneytransfersystem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
            logger.debug("Tried to update single " + User.class.getName()
                    + " id: " + user.getId() + " (not found)");

            return new User();
        }
        logger.debug("Update single " + User.class.getName() + ": " + userUpdated);

        return userUpdated;
    }

    public void getBalanceBack(TransferBack transferBack) {

        User userUpdated = userRepository.findByEmail(transferBack.getEmail());
        userUpdated.setBalance(userUpdated.getBalance() - transferBack.getAmount());
        userRepository.save(userUpdated);

        // IN Transfer
        Transfer transfer = new Transfer();
        transfer.setId(userUpdated.getId());
        transfer.setDate(LocalDateTime.now());
        transfer.setType("OUT");
        transfer.setAmount(transferBack.getAmount());
        transfer.setTax(taxRepository.findByName("DEFAULT").getRate());
        transfer.setDescription("Retrait de solde vers compte bancaire");

        transferRepository.save(transfer);

        // OUT Transfer
        transfer.setId(userRepository.findByEmail("app@test.com").getId());
        transfer.setType("IN");
        transferRepository.save(transfer);
    }
}

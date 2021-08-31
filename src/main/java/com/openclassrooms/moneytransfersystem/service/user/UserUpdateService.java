package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
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

    public void getBalanceBack(Transfer transfer) {

        // Reduce Balance
        User userUpdated = transfer.getUser();
        userUpdated.setBalance(userUpdated.getBalance() - transfer.getAmount());
        userRepository.save(userUpdated);

        // IN Transfer
        transfer.setDate(LocalDateTime.now());
        transfer.setDescription("Retrait de solde vers compte bancaire");
        transfer.setType("OUT");
        transfer.setId(transfer.getUser().getId());
        transferRepository.save(transfer);

        // OUT Transfer
        transfer.setDate(LocalDateTime.now());
        transfer.setDescription("Retrait de solde vers compte bancaire");
        transfer.setType("IN");
        transfer.setId(userRepository.findByEmail("app@test.com").getId());
        transferRepository.save(transfer);
    }
}

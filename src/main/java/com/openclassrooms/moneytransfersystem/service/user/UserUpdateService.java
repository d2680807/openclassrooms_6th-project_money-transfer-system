package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserUpdateService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LogManager.getLogger(UserUpdateService.class);

    public User updateUser(User user) {

        User userUpdated;
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            userUpdated = optionalUser.get();
            userRepository.save(userUpdated);
        } else {
            logger.debug("[updateUser] id not found: " + user.getId());

            return new User();
        }
        logger.debug("[updateUser] user: " + userUpdated);

        return userUpdated;
    }
}

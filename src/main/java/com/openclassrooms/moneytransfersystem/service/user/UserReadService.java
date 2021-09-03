package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserReadService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LogManager.getLogger(UserReadService.class);

    public User readUserById(Long id) {

        logger.debug("[readUserById] id: " + id);

        return userRepository.getById(id);
    }

    public User readUserByEmail(String email) {

        logger.debug("[readUserByEmail] email: " + email);

        return userRepository.findByEmail(email);
    }

    public Collection<User> readUsers() {

        logger.debug("[readUsers] read: all");

        return userRepository.findAll();
    }
}

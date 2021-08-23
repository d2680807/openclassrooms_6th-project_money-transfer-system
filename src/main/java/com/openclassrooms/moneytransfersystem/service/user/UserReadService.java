package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserReadService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserReadService.class);

    public User readUserById(Long id) {

        logger.debug("Read " + User.class.getName() + " by id:" + id);

        return userRepository.getById(id);
    }

    public User readUserByEmail(String email) {

        logger.debug("Read " + User.class.getName() + " by id:" + email);

        return userRepository.findByEmail(email);
    }

    public Collection<User> readUsers() {

        logger.debug("Read all " + User.class.getName());

        return userRepository.findAll();
    }
}

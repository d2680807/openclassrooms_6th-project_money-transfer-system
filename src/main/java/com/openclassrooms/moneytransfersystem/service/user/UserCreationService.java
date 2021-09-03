package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserCreationService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserCreationService.class);

    public User createUser(User user) {

        logger.debug("[createUser] user: " + user);

        return userRepository.save(user);
    }

    public Collection<User> createUsers(Collection<User> users) {

        logger.debug("[createUsers] users:" + users);

        return userRepository.saveAll(users);
    }
}

package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserCreationService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LogManager.getLogger(UserCreationService.class);

    public User createUser(User user) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setBalance(0);
        user.setFriendsList("[]");
        logger.debug("[createUser] user: " + user);

        return userRepository.save(user);
    }

    public Collection<User> createUsers(Collection<User> users) {

        logger.debug("[createUsers] users:" + users);

        return userRepository.saveAll(users);
    }
}

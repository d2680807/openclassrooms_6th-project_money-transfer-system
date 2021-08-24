package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDeletionService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserDeletionService.class);

    public void deleteUserById(Long id) {

        if(userRepository.existsById(id)) {
            logger.debug("Delete " + User.class.getName() + " by id: " + id);
            userRepository.deleteById(id);
        }
    }

    public void deleteUsers() {

        logger.debug("Delete all " + User.class.getName());
        userRepository.deleteAll();
    }
}

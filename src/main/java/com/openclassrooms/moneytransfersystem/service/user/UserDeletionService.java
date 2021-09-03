package com.openclassrooms.moneytransfersystem.service.user;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDeletionService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LogManager.getLogger(UserDeletionService.class);

    public void deleteUserById(Long id) {

        if(userRepository.existsById(id)) {
            logger.debug("[deleteUserById] id: " + id);
            userRepository.deleteById(id);
        }
    }

    public void deleteUsers() {

        logger.debug("[deleteUsers] delete: all");
        userRepository.deleteAll();
    }
}

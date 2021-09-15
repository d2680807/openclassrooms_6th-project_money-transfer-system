package com.openclassrooms.moneytransfersystem.configuration;

import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitialization {


    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void postConstruct() {

        User user = new User();
        user.setEmail("app@test.com");
        user.setPassword("1234567");
        user.setFirstName("Pay My Buddy");
        user.setLastName("(Application)");
        user.setIbanCode(2641874);
        user.setBicCode(63215472);
        user.setFriendsList("[]");

        userRepository.save(user);
    }
}

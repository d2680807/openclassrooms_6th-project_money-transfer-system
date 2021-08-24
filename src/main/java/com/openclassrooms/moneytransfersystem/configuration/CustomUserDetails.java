package com.openclassrooms.moneytransfersystem.configuration;

import com.openclassrooms.moneytransfersystem.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails  implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {

        this.user = user;
    }
}

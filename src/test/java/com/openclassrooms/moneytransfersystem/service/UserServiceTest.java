package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserDeletionService;
import com.openclassrooms.moneytransfersystem.service.user.UserReadService;
import com.openclassrooms.moneytransfersystem.service.user.UserUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserCreationService userCreationService;
    @MockBean
    private UserReadService userReadService;
    @MockBean
    private UserUpdateService userUpdateService;
    @MockBean
    private UserDeletionService userDeletionService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void shouldInitialize() throws Exception {

        userRepository.deleteAll();

        User user = new User();
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(2302447);
        user.setBicCode(3021744);
        user.setFriendsList("[]");
        userRepository.save(user);
    }

    @Test
    public void shouldGetUsers() throws Exception {

        User user = new User();
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(2302447);
        user.setBicCode(3021744);
        user.setFriendsList("[]");
        user.setId(userRepository.findByEmail("harry@jkr.com").getId());

        List<User> users = new ArrayList<>();
        users.add(user);

        Mockito.when(userReadService.readUsers()).thenReturn(users);

        MvcResult mvcResult = mockMvc.perform(get("/users")).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(users);

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldGetUserById() throws Exception {

        Long userId = userRepository.findByEmail("harry@jkr.com").getId();

        User user = new User();
        user.setId(userId);
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(2302447);
        user.setBicCode(3021744);
        user.setFriendsList("[]");

        Mockito.when(userReadService.readUserById(userId)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get("/users/" + userId)).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(user);

        assertEquals(actualResponse, expectedResponse);
    }


    @Test
    public void shouldDeleteUser() throws Exception {

        Long userId = userRepository.findByEmail("harry@jkr.com").getId();

        Mockito.doNothing().when(userDeletionService).deleteUserById(userId);

        mockMvc.perform(delete("/users/" + userId)).andExpect(status().isOk());

        Mockito.verify(userDeletionService, Mockito.times(1)).deleteUserById(userId);
    }

    @Test
    public void shouldDeleteUsers() throws Exception {

        Mockito.doNothing().when(userDeletionService).deleteUsers();

        mockMvc.perform(delete("/users")).andExpect(status().isOk());

        Mockito.verify(userDeletionService, Mockito.times(1)).deleteUsers();
    }
}

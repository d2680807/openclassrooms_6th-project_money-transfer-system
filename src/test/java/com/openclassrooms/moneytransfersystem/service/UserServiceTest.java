package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.controller.UserController;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserDeletionService;
import com.openclassrooms.moneytransfersystem.service.user.UserReadService;
import com.openclassrooms.moneytransfersystem.service.user.UserUpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Test
    public void shouldCreateUser() throws Exception {

        User user = new User();
        user.setEmail("harry@test.com");
        user.setPassword("123456");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(123456);
        user.setBicCode(123456);
        user.setFriendsList("[]");

        User userSaved = new User();
        user.setEmail("harry@test.com");
        user.setPassword("123456");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(123456);
        user.setBicCode(123456);
        user.setFriendsList("[]");

        Mockito.when(userCreationService.createUser(user)).thenReturn(userSaved);

        mockMvc.perform(post("/createUser")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
                ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userSaved))).andReturn();
    }

    @Test
    public void shouldCreateUsers() throws Exception {

        User user = new User();
        user.setEmail("harry@test.com");
        user.setPassword("123456");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(123456);
        user.setBicCode(123456);
        user.setFriendsList("[]");

        List<User> users = new ArrayList<>();
        users.add(user);

        User userSaved = new User();
        user.setEmail("harry@test.com");
        user.setPassword("123456");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(123456);
        user.setBicCode(123456);
        user.setFriendsList("[]");

        List<User> usersSaved = new ArrayList<>();
        usersSaved.add(userSaved);

        Mockito.when(userCreationService.createUsers(users)).thenReturn(usersSaved);

        mockMvc.perform(post("/createUsers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(users))
                ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(usersSaved))).andReturn();
    }

    @Test
    public void shouldGetUsers() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("harry@test.com");
        user.setPassword("123456");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(123456);
        user.setBicCode(123456);
        user.setFriendsList("[]");

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

        User user = new User();
        user.setId(1L);
        user.setEmail("harry@test.com");
        user.setPassword("123456");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(123456);
        user.setBicCode(123456);
        user.setFriendsList("[]");

        Mockito.when(userReadService.readUserById(1L)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get("/users/1")).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(user);

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldUpdateUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("harry@test.com");
        user.setPassword("123456");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(123456);
        user.setBicCode(123456);
        user.setFriendsList("[]");

        User userUpdated = new User();
        userUpdated.setId(1L);
        userUpdated.setEmail("harry@test.com");
        userUpdated.setPassword("123456");
        userUpdated.setFirstName("Harry");
        userUpdated.setLastName("POTTER");
        userUpdated.setIbanCode(123456);
        userUpdated.setBicCode(123456);
        userUpdated.setFriendsList("[]");

        Mockito.when(userUpdateService.updateUser(user)).thenReturn(userUpdated);

        mockMvc.perform(put("/updateUser")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userUpdated)));
    }

    @Test
    public void shouldDeleteUser() throws Exception {

        Mockito.doNothing().when(userDeletionService).deleteUserById(1L);

        mockMvc.perform(delete("/users/1")).andExpect(status().isOk());

        Mockito.verify(userDeletionService, Mockito.times(1)).deleteUserById(1L);
    }
}

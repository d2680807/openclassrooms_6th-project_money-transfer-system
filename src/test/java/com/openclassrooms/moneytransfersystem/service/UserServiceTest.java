package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @BeforeEach
    public void shouldInitialize() throws Exception {

        userDeletionService.deleteUsers();

        User user = new User();
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(2302447);
        user.setBicCode(3021744);
        user.setFriendsList("[]");
        mockMvc.perform(post("/createUser")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)));

    }

    @Test
    public void shouldCreateUser() throws Exception {

        userDeletionService.deleteUsers();

        User user = new User();
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(2302447);
        user.setBicCode(3021744);
        user.setFriendsList("[]");

        User expectedUser = new User();
        expectedUser.setEmail("harry@jkr.com");
        expectedUser.setPassword("1234567");
        expectedUser.setFirstName("Harry");
        expectedUser.setLastName("POTTER");
        expectedUser.setIbanCode(2302447);
        expectedUser.setBicCode(3021744);
        expectedUser.setFriendsList("[]");

        Mockito.when(userCreationService.createUser(user)).thenReturn(expectedUser);

        mockMvc.perform(post("/createUser")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser))).andReturn();
    }

    @Test
    public void shouldCreateUsers() throws Exception {

        userDeletionService.deleteUsers();

        User user = new User();
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(2302447);
        user.setBicCode(3021744);
        user.setFriendsList("[]");

        List<User> usersList = new ArrayList<>();
        usersList.add(user);

        User expectedUser = new User();
        expectedUser.setEmail("harry@jkr.com");
        expectedUser.setPassword("1234567");
        expectedUser.setFirstName("Harry");
        expectedUser.setLastName("POTTER");
        expectedUser.setIbanCode(2302447);
        expectedUser.setBicCode(3021744);
        expectedUser.setFriendsList("[]");

        List<User> expectedUsersList = new ArrayList<>();
        expectedUsersList.add(expectedUser);

        Mockito.when(userCreationService.createUsers(usersList)).thenReturn(expectedUsersList);

        mockMvc.perform(post("/createUsers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(usersList))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUsersList))).andReturn();
    }

    @Test
    public void shouldGetUsers() throws Exception {

        userDeletionService.deleteUsers();

        User user = new User();
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(2302447);
        user.setBicCode(3021744);
        user.setFriendsList("[]");
        userCreationService.createUser(user);
        user.setId(userReadService.readUserByEmail("harry@jkr.com").getId());

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

        Long userId = userReadService.readUserByEmail("harry@jkr.com").getId();

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
    public void shouldUpdateUser() throws Exception {

        Long userId = userReadService.readUserByEmail("harry@jkr.com").getId();

        User user = new User();
        user.setId(userId);
        user.setEmail("harry@jkr.com");
        user.setPassword("1234567");
        user.setFirstName("Harry");
        user.setLastName("POTTER");
        user.setIbanCode(3021744);
        user.setBicCode(2302447);
        user.setFriendsList("[]");

        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setEmail("harry@jkr.com");
        expectedUser.setPassword("1234567");
        expectedUser.setFirstName("Harry");
        expectedUser.setLastName("POTTER");
        expectedUser.setIbanCode(3021744);
        expectedUser.setBicCode(2302447);
        expectedUser.setFriendsList("[]");

        Mockito.when(userUpdateService.updateUser(user)).thenReturn(expectedUser);

        mockMvc.perform(put("/updateUser")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    public void shouldDeleteUser() throws Exception {

        Long userId = userReadService.readUserByEmail("harry@jkr.com").getId();

        Mockito.doNothing().when(userDeletionService).deleteUserById(userId);

        mockMvc.perform(delete("/users/1")).andExpect(status().isOk());

        Mockito.verify(userDeletionService, Mockito.times(1)).deleteUserById(userId);
    }

    @Test
    public void shouldDeleteUsers() throws Exception {

        Mockito.doNothing().when(userDeletionService).deleteUsers();

        mockMvc.perform(delete("/users")).andExpect(status().isOk());

        Mockito.verify(userDeletionService, Mockito.times(1)).deleteUsers();
    }
}

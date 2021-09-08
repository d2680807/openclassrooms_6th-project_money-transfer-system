package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserDeletionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FormServiceTest {

    @Autowired
    private FormService formService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void shouldInitialize() throws Exception {

        mockMvc.perform(delete("/users"))
                .andExpect(status().isOk());

        User harry = new User();
        harry.setEmail("harry@test.com");
        harry.setPassword("123456");
        harry.setFirstName("Harry");
        harry.setLastName("POTTER");
        harry.setIbanCode(123456);
        harry.setBicCode(123456);
        harry.setBalance(0);
        harry.setFriendsList("[]");

        User ron = harry;
        ron.setEmail("ron@test.com");
        ron.setFirstName("Ron");
        ron.setLastName("WISLEY");

        Collection<User> users = new ArrayList<>();
        users.add(harry);
        users.add(ron);
        mockMvc.perform(post("/createUsers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(users)));
    }

    @Test
    void shouldGetBalance() {

        String expectedBalance = "0,00";
        String actualBalance = formService
                .getBalance("harry@test.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetBalanceBack() throws ParseException {

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(formService
                .getBalance("harry@test.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue() - 10) ;

        Requirement requirement = new Requirement();
        requirement.setUserId(57L);
        requirement.setAmount(10);
        formService
                .updateBalance(requirement, false);

        String actualBalance = formService
                .getBalance("harry@test.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetRequirement() {

        Requirement expectedRequirement = new Requirement();
        expectedRequirement.setUserId(57L);

        Requirement actualRequirement = formService
                .getRequirement("harry@test.com");

        assertEquals(expectedRequirement, actualRequirement);
    }

    @Test
    void shouldGetFriendsList() throws JsonProcessingException {

        Set<String> expectedFriendsList = new HashSet<>();

        Set<String> actualFriendsList = formService
                .getFriendsList("harry@test.com");

        assertEquals(expectedFriendsList, actualFriendsList);
    }

    @Test
    void shouldTransferToFriend() throws ParseException {

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(formService
                .getBalance("harry@test.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue() - 10) ;

        Requirement requirement = new Requirement();
        requirement.setUserId(57L);
        requirement.setRecipient("ron@test.com");
        requirement.setAmount(10);
        formService
                .transferToFriend(requirement);

        String actualBalance = formService
                .getBalance("harry@test.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldAddFriend() throws ParseException, JsonProcessingException {

        Set<String> expectedFriendsList = new HashSet<>();
        expectedFriendsList.add("ron@test.com");

        Requirement requirement = new Requirement();
        requirement.setUserId(57L);
        requirement.setRecipient("ron@test.com");
        formService
                .addFriend(requirement);

        Set<String> actualFriendsList = formService
                .getFriendsList("harry@test.com");

        assertEquals(expectedFriendsList, actualFriendsList);
    }
}

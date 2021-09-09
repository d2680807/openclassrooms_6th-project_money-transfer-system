package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import com.openclassrooms.moneytransfersystem.service.user.UserDeletionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FormServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FormService formService;
    @Autowired
    private UserDeletionService userDeletionService;

    @BeforeEach
    public void shouldInitialize() throws Exception {

        userDeletionService.deleteUsers();

        User harry = new User();
        harry.setEmail("harry@jkr.com");
        harry.setPassword("1234567");
        harry.setFirstName("Harry");
        harry.setLastName("POTTER");
        harry.setIbanCode(2302447);
        harry.setBicCode(3021744);
        harry.setFriendsList("[]");

        User ron = new User();
        ron.setEmail("ron@jkr.com");
        ron.setPassword("1234567");
        ron.setFirstName("Ron");
        ron.setLastName("WISLEY");
        ron.setIbanCode(4642775);
        ron.setBicCode(2662934);
        ron.setFriendsList("[]");

        List<User> users = new ArrayList<>();
        users.add(harry);
        users.add(ron);
        mockMvc.perform(post("/createUsers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(users)));

        Requirement requirement = formService
                .getRequirement("harry@jkr.com");
        requirement.setAmount(10000);
        formService
                .updateBalance(requirement, true);

        requirement = formService
                .getRequirement("ron@jkr.com");
        requirement.setAmount(1000);
        formService
                .updateBalance(requirement, true);
    }

    @Test
    void shouldGetBalance() {

        String expectedBalance = "10000,00";
        String actualBalance = formService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetBalanceBack() throws ParseException {

        double amount = 50;

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(formService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue() - amount) ;

        Requirement requirement = formService
                .getRequirement("harry@jkr.com");
        requirement.setAmount(amount);
        formService
                .updateBalance(requirement, false);

        String actualBalance = formService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldCancelBalanceBack() throws ParseException {

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(formService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue()) ;

        Requirement requirement = formService
                .getRequirement("harry@jkr.com");
        requirement.setAmount(0);
        formService
                .updateBalance(requirement, false);

        String actualBalance = formService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetFriendsList() throws JsonProcessingException {

        Set<String> expectedFriendsList = new HashSet<>();

        Set<String> actualFriendsList = formService
                .getFriendsList("harry@jkr.com");

        assertEquals(expectedFriendsList, actualFriendsList);
    }

    @Test
    void shouldTransferToFriend() throws ParseException {

        double amount = 200;

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(formService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue() - amount) ;

        Requirement requirement = formService.getRequirement("harry@jkr.com");
        requirement.setRecipient("ron@jkr.com");
        requirement.setAmount(amount);
        formService
                .transferToFriend(requirement);

        String actualBalance = formService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldNotTransferToFriend() throws ParseException {

        double amount = 0;

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(formService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue()) ;

        Requirement requirement = formService.getRequirement("harry@jkr.com");
        requirement.setRecipient("ron@jkr.com");
        requirement.setAmount(amount);
        formService
                .transferToFriend(requirement);

        String actualBalance = formService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldAddFriend() throws ParseException, JsonProcessingException {

        Set<String> expectedFriendsList = new HashSet<>();
        expectedFriendsList.add("ron@jkr.com");

        Requirement requirement = formService.getRequirement("harry@jkr.com");
        requirement.setRecipient("ron@jkr.com");
        formService.addFriend(requirement);

        Set<String> actualFriendsList = formService
                .getFriendsList("harry@jkr.com");

        assertEquals(expectedFriendsList, actualFriendsList);
    }
}

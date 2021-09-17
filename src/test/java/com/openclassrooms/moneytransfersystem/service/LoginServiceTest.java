package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.model.Tax;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import com.openclassrooms.moneytransfersystem.service.login.LoginService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxCreationService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxDeletionService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferCreationService;
import com.openclassrooms.moneytransfersystem.service.user.UserDeletionService;
import com.openclassrooms.moneytransfersystem.service.user.UserReadService;
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

@SpringBootTest
@AutoConfigureMockMvc
public class LoginServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserReadService userReadService;
    @Autowired
    private UserDeletionService userDeletionService;
    @Autowired
    private TaxCreationService taxCreationService;
    @Autowired
    private TaxDeletionService taxDeletionService;
    @Autowired
    private TransferCreationService transferCreationService;

    @BeforeEach
    public void shouldInitialize() throws Exception {

        userDeletionService.deleteUsers();
        taxDeletionService.deleteTaxes();
        Tax tax = new Tax();
        tax.setName("DEFAULT");
        tax.setRate(0.05);
        taxCreationService.createTax(tax);

        User harry = new User();
        harry.setEmail("harry@jkr.com");
        harry.setPassword("1234567");
        harry.setFirstName("Harry");
        harry.setLastName("POTTER");
        harry.setIbanCode(2302447);
        harry.setBicCode(3021744);
        harry.setFriendsList("[\"ron@jkr.com\"]");

        User ron = new User();
        ron.setEmail("ron@jkr.com");
        ron.setPassword("1234567");
        ron.setFirstName("Ron");
        ron.setLastName("WISLEY");
        ron.setIbanCode(4642775);
        ron.setBicCode(2662934);
        ron.setFriendsList("[\"harry@jkr.com\"]");

        List<User> users = new ArrayList<>();
        users.add(harry);
        users.add(ron);
        mockMvc.perform(post("/createUsers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(users)));

        Requirement requirement = loginService
                .getRequirement("harry@jkr.com");
        requirement.setAmount(10000);
        loginService
                .updateBalance(requirement, true);

        requirement = loginService
                .getRequirement("ron@jkr.com");
        requirement.setAmount(1000);
        loginService
                .updateBalance(requirement, true);
    }

    @Test
    void shouldGetBalance() {

        String expectedBalance = "10000,00";
        String actualBalance = loginService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetBalanceBack() throws ParseException {

        double amount = 50;

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(loginService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue() - amount) ;

        Requirement requirement = loginService
                .getRequirement("harry@jkr.com");
        requirement.setAmount(amount);
        loginService
                .updateBalance(requirement, false);

        String actualBalance = loginService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldCancelBalanceBack() throws ParseException {

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(loginService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue()) ;

        Requirement requirement = loginService
                .getRequirement("harry@jkr.com");
        requirement.setAmount(0);
        loginService
                .updateBalance(requirement, false);

        String actualBalance = loginService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetFriendsList() throws JsonProcessingException {

        Set<String> expectedFriendsList = new HashSet<>();
        expectedFriendsList.add("ron@jkr.com");

        Set<String> actualFriendsList = loginService
                .getFriendsList("harry@jkr.com");

        assertEquals(expectedFriendsList, actualFriendsList);
    }

    @Test
    void shouldTransferToFriend() throws ParseException {

        double amount = 200;

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(loginService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue() - amount - (amount * 0.05)) ;

        Requirement requirement = loginService.getRequirement("harry@jkr.com");
        requirement.setRecipient("ron@jkr.com");
        requirement.setAmount(amount);
        requirement.setDescription("Pour le cine.");
        loginService
                .transferToFriend(requirement);

        String actualBalance = loginService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldNotTransferToFriend() throws ParseException {

        double amount = 0;

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(loginService
                .getBalance("harry@jkr.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue()) ;

        Requirement requirement = loginService.getRequirement("harry@jkr.com");
        requirement.setRecipient("ron@jkr.com");
        requirement.setAmount(amount);
        loginService
                .transferToFriend(requirement);

        String actualBalance = loginService
                .getBalance("harry@jkr.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldAddFriend() throws ParseException, JsonProcessingException {

        Set<String> expectedFriendsList = new HashSet<>();
        expectedFriendsList.add("ron@jkr.com");

        Requirement requirement = loginService.getRequirement("harry@jkr.com");
        requirement.setRecipient("ron@jkr.com");
        loginService.addFriend(requirement);

        Set<String> actualFriendsList = loginService
                .getFriendsList("harry@jkr.com");

        assertEquals(expectedFriendsList, actualFriendsList);
    }
}

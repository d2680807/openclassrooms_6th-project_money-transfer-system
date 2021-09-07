package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openclassrooms.moneytransfersystem.model.utility.ListElement;
import com.openclassrooms.moneytransfersystem.model.utility.Requirement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FormServiceTest {

    @Autowired
    private FormService formService;

    @Test
    void shouldGetBalance() {

        String expectedBalance = "0,00";
        String actualBalance = formService
                .getBalance("test@test.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetBalanceBack() throws ParseException {

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number balance = format.parse(formService
                .getBalance("test@test.com"));
        String expectedBalance = String.format("%.2f",
                balance.doubleValue() - 10) ;

        Requirement requirement = new Requirement();
        requirement.setUserId(57L);
        requirement.setAmount(10);
        formService
                .updateBalance(requirement, false);

        String actualBalance = formService
                .getBalance("test@test.com");

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void shouldGetRequirement() {

        Requirement expectedRequirement = new Requirement();
        expectedRequirement.setUserId(57L);

        Requirement actualRequirement = formService
                .getRequirement("test@test.com");

        assertEquals(expectedRequirement, actualRequirement);
    }

    @Test
    void shouldGetFriendsList() throws JsonProcessingException {

        Set<String> expectedFriendsList = new HashSet<>();

        Set<String> actualFriendsList = formService
                .getFriendsList("test@test.com");

        assertEquals(expectedFriendsList, actualFriendsList);
    }

    @Test
    void shouldGetTransfersList() throws JsonProcessingException {

        List<ListElement> expectedTransfersList = new ArrayList<>();

        List<ListElement> actualTransfersList = formService
                .getTransfersList("test@test.com");

        assertEquals(expectedTransfersList, actualTransfersList);
    }
}

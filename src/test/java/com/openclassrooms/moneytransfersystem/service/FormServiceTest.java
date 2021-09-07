package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}

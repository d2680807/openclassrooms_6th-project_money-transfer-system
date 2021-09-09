package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JsonServiceTest {

    @Autowired
    private JsonService jsonService;

    @Test
    void shouldParseToJson() throws JsonProcessingException {

        String expectedJson = "[\"david@test.com\"]";

        Set<String> users = new HashSet<>();
        String email = "david@test.com";
        users.add(email);

        String actualJson = jsonService.toJson(users);

        assertEquals(actualJson, expectedJson);
    }

    @Test
    void shouldParseToSet() throws JsonProcessingException {

        String jsonString = "[\"david@test.com\"]";

        Set<String> expectedSet = new HashSet<>();
        String email = "david@test.com";
        expectedSet.add(email);

        Set<String> actualSet = jsonService.toSetOfString(jsonString);

        assertEquals(actualSet, expectedSet);
    }
}

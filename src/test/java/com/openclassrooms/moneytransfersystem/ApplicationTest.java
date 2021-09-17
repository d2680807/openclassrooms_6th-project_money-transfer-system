package com.openclassrooms.moneytransfersystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

    private static final Logger logger = LogManager.getLogger(ApplicationTest.class);

    @Test
    void shouldLogMyMessage() {

        String myMessage = "test";
        logger.debug(myMessage);
    }
}

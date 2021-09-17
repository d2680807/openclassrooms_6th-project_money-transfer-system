package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.dao.UserRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.TransferType;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferCreationService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferDeletionService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferReadService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferCreationService transferCreationService;
    @MockBean
    private TransferReadService transferReadService;
    @MockBean
    private TransferUpdateService transferUpdateService;
    @MockBean
    private TransferDeletionService transferDeletionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransferRepository transferRepository;

    @BeforeEach
    public void shouldInitialize() throws Exception {

        transferRepository.deleteAll();
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

        Transfer transfer = new Transfer();
        transfer.setUser(user);
        transfer.setDate(LocalDateTime.of(2021, 12, 25, 00, 00, 00));
        transfer.setType(TransferType.OUT);
        transfer.setAmount(50);
        transfer.setTax(0.005);
        transfer.setDescription("Remboursement pour le cine.");
        transferRepository.save(transfer);
    }

    @Test
    public void shouldGetTransfers() throws Exception {

        transferRepository.deleteAll();

        List<Transfer> transfers = new ArrayList<>();

        Mockito.when(transferReadService.readTransfers()).thenReturn(transfers);

        MvcResult mvcResult = mockMvc.perform(get("/transfers")).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(transfers);

        assertEquals(actualResponse, expectedResponse);
    }


    @Test
    public void shouldDeleteTransfer() throws Exception {

        Long userId = userRepository.findByEmail("harry@jkr.com").getId();

        Mockito.doNothing().when(transferDeletionService).deleteTransferById(userId);

        mockMvc.perform(delete("/transfers/" + userId)).andExpect(status().isOk());

        Mockito.verify(transferDeletionService, Mockito.times(1)).deleteTransferById(userId);
    }

    @Test
    public void shouldDeleteTransfers() throws Exception {

        Mockito.doNothing().when(transferDeletionService).deleteTransfers();

        mockMvc.perform(delete("/transfers")).andExpect(status().isOk());

        Mockito.verify(transferDeletionService, Mockito.times(1)).deleteTransfers();
    }
}

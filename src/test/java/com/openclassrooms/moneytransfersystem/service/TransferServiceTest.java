package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.TransferType;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferCreationService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferDeletionService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferReadService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferUpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @Test
    public void shouldGetTransfers() throws Exception {

        User user = new User();
        user.setId(57L);

        Transfer transfer = new Transfer();
        transfer.setUser(user);
        transfer.setDate(LocalDateTime.now());
        transfer.setType(TransferType.OUT);
        transfer.setAmount(50);
        transfer.setTax(0.05);
        transfer.setDescription("Remboursement pour le cine.");

        List<Transfer> transfers = new ArrayList<>();
        transfers.add(transfer);

        Mockito.when(transferReadService.readTransfers()).thenReturn(transfers);

        MvcResult mvcResult = mockMvc.perform(get("/transfers")).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(transfers);

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldGetTransferById() throws Exception {

        User user = new User();
        user.setId(57L);

        Transfer transfer = new Transfer();
        transfer.setId(53L);
        transfer.setUser(user);
        transfer.setDate(LocalDateTime.now());
        transfer.setType(TransferType.OUT);
        transfer.setAmount(50);
        transfer.setTax(0.05);
        transfer.setDescription("Remboursement pour le cine.");

        Mockito.when(transferReadService.readTransferById(53L)).thenReturn(transfer);

        MvcResult mvcResult = mockMvc.perform(get("/transfers/53")).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(transfer);

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldDeleteTransfer() throws Exception {

        Mockito.doNothing().when(transferDeletionService).deleteTransferById(1L);

        mockMvc.perform(delete("/transfers/1")).andExpect(status().isOk());

        Mockito.verify(transferDeletionService, Mockito.times(1)).deleteTransferById(1L);
    }

    @Test
    public void shouldDeleteTransfers() throws Exception {

        Mockito.doNothing().when(transferDeletionService).deleteTransfers();

        mockMvc.perform(delete("/transfers")).andExpect(status().isOk());

        Mockito.verify(transferDeletionService, Mockito.times(1)).deleteTransfers();
    }
}

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
    public void shouldCreateTransfer() throws Exception {

        User user = new User();
        user.setId(57L);

        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setUser(user);
        transfer.setDate(LocalDateTime.now());
        transfer.setType(TransferType.OUT);
        transfer.setAmount(50);
        transfer.setTax(0.05);
        transfer.setDescription("Remboursement pour le cine.");

        Transfer transferSaved = new Transfer();
        transferSaved.setId(1L);
        transferSaved.setUser(user);
        transferSaved.setDate(LocalDateTime.now());
        transferSaved.setType(TransferType.OUT);
        transferSaved.setAmount(50);
        transferSaved.setTax(0.05);
        transferSaved.setDescription("Remboursement pour le cine.");

        Mockito.when(transferCreationService.createTransfer(transfer)).thenReturn(transferSaved);

        mockMvc.perform(post("/createTransfer")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transfer))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(transferSaved))).andReturn();
    }

    @Test
    public void shouldCreateTransfers() throws Exception {

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

        Transfer transferSaved = new Transfer();
        transferSaved.setUser(user);
        transferSaved.setDate(LocalDateTime.now());
        transferSaved.setType(TransferType.OUT);
        transferSaved.setAmount(50);
        transferSaved.setTax(0.05);
        transferSaved.setDescription("Remboursement pour le cine.");

        List<Transfer> transfersSaved = new ArrayList<>();
        transfersSaved.add(transferSaved);

        Mockito.when(transferCreationService.createTransfers(transfers)).thenReturn(transfersSaved);

        mockMvc.perform(post("/createTransfers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transfers))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(transfersSaved))).andReturn();
    }

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
    public void shouldUpdateTransfer() throws Exception {

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

        Transfer transferUpdated = new Transfer();
        transferUpdated.setId(53L);
        transferUpdated.setUser(user);
        transferUpdated.setDate(LocalDateTime.now());
        transferUpdated.setType(TransferType.OUT);
        transferUpdated.setAmount(50);
        transferUpdated.setTax(0.05);
        transferUpdated.setDescription("Remboursement pour le cine.");

        Mockito.when(transferUpdateService.updateTransfer(transfer)).thenReturn(transferUpdated);

        mockMvc.perform(put("/updateTransfer")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(transferUpdated)));
    }

    @Test
    public void shouldDeleteTransfer() throws Exception {

        Mockito.doNothing().when(transferDeletionService).deleteTransferById(1L);

        mockMvc.perform(delete("/transfers/1")).andExpect(status().isOk());

        Mockito.verify(transferDeletionService, Mockito.times(1)).deleteTransferById(1L);
    }
}

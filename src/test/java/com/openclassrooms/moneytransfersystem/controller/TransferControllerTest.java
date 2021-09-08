package com.openclassrooms.moneytransfersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.model.utility.TransferType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateTransfer() throws Exception {

        User user = new User();
        user.setId(57L);

        Transfer transfer = new Transfer();
        transfer.setUser(user);
        transfer.setDate(LocalDateTime.now());
        transfer.setType(TransferType.OUT);
        transfer.setAmount(50);
        transfer.setTax(0.05);
        transfer.setDescription("Remboursement pour le ciné.");

        mockMvc.perform(post("/createTransfer")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk());
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
        transfer.setDescription("Remboursement pour le ciné.");

        List<Transfer> transfers = new ArrayList<>();
        transfers.add(transfer);

        mockMvc.perform(post("/createTransfers")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transfers)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetTransferById() throws Exception {

        mockMvc.perform(get("/transfers/1")).andExpect(status().is(500));
    }

    @Test
    public void shouldUpdateTransfer() throws Exception {

        User user = new User();
        user.setId(57L);
        Transfer transfer = new Transfer();
        transfer.setId(1L);
        transfer.setUser(user);
        transfer.setDate(LocalDateTime.now());
        transfer.setType(TransferType.OUT);
        transfer.setAmount(50);
        transfer.setTax(0.05);
        transfer.setDescription("Remboursement pour le restaurant.");

        mockMvc.perform(put("/updateTransfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteTransfer() throws Exception {

        mockMvc.perform(delete("/transfers/1")).andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteTransfers() throws Exception {

        mockMvc.perform(delete("/transfers")).andExpect(status().isOk());
    }
}

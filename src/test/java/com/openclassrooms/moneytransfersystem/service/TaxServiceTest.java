package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.model.Tax;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.service.tax.TaxCreationService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxDeletionService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxReadService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaxServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaxCreationService taxCreationService;
    @MockBean
    private TaxReadService taxReadService;
    @MockBean
    private TaxUpdateService taxUpdateService;
    @MockBean
    private TaxDeletionService taxDeletionService;
    @Autowired
    private TaxRepository taxRepository;

    @BeforeEach
    public void shouldInitialize() throws Exception {

        taxRepository.deleteAll();

        Tax tax = new Tax();
        tax.setName("DEFAULT");
        tax.setRate(0.05);
        taxRepository.save(tax);
    }

    @Test
    public void shouldCreateTax() throws Exception {

        taxRepository.deleteAll();

        Tax tax = new Tax();
        tax.setName("DIVERSE");
        tax.setRate(0.10);

        Tax taxSaved = new Tax();
        taxSaved.setName("DIVERSE");
        taxSaved.setRate(0.10);

        Mockito.when(taxCreationService.createTax(tax)).thenReturn(taxSaved);

        mockMvc.perform(post("/createTax")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tax))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(taxSaved))).andReturn();
    }

    @Test
    public void shouldCreateTaxes() throws Exception {

        taxRepository.deleteAll();

        Tax tax = new Tax();
        tax.setName("DIVERSE");
        tax.setRate(0.10);

        List<Tax> taxes = new ArrayList<>();
        taxes.add(tax);

        Tax taxSaved = new Tax();
        taxSaved.setName("DIVERSE");
        taxSaved.setRate(0.10);

        List<Tax> taxesSaved = new ArrayList<>();
        taxesSaved.add(taxSaved);

        Mockito.when(taxCreationService.createTaxes(taxes)).thenReturn(taxesSaved);

        mockMvc.perform(post("/createTaxes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(taxes))
        ).andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(taxesSaved))).andReturn();
    }

    @Test
    public void shouldGetTaxes() throws Exception {

        Tax tax = new Tax();
        tax.setId(taxRepository.findByName("DEFAULT").getId());
        tax.setName("DEFAULT");
        tax.setRate(0.05);

        List<Tax> taxes = new ArrayList<>();
        taxes.add(tax);

        Mockito.when(taxReadService.readTaxes()).thenReturn(taxes);

        MvcResult mvcResult = mockMvc.perform(get("/taxes")).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(taxes);

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldGetTaxById() throws Exception {

        Long taxId = taxRepository.findByName("DEFAULT").getId();
        Tax tax = new Tax();
        tax.setId(taxId);
        tax.setName("Test");
        tax.setRate(0.10);

        Mockito.when(taxReadService.readTaxById(taxId)).thenReturn(tax);

        MvcResult mvcResult = mockMvc.perform(get("/taxes/" + taxId)).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(tax);

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldUpdateTax() throws Exception {

        Long taxId = taxRepository.findByName("DEFAULT").getId();

        Tax tax = new Tax();
        tax.setId(taxId);
        tax.setName("OTHER");
        tax.setRate(0.10);

        Tax taxUpdated = new Tax();
        taxUpdated.setId(taxId);
        taxUpdated.setName("OTHER");
        taxUpdated.setRate(0.10);

        Mockito.when(taxUpdateService.updateTax(tax)).thenReturn(taxUpdated);

        mockMvc.perform(put("/updateTax")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tax)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(taxUpdated)));
    }

    @Test
    public void shouldDeleteTax() throws Exception {

        Long taxId = taxRepository.findByName("DEFAULT").getId();

        Mockito.doNothing().when(taxDeletionService).deleteTaxById(taxId);

        mockMvc.perform(delete("/taxes/" + taxId)).andExpect(status().isOk());

        Mockito.verify(taxDeletionService, Mockito.times(1)).deleteTaxById(taxId);
    }

    @Test
    public void shouldDeleteTaxes() throws Exception {

        Mockito.doNothing().when(taxDeletionService).deleteTaxes();

        mockMvc.perform(delete("/taxes")).andExpect(status().isOk());

        Mockito.verify(taxDeletionService, Mockito.times(1)).deleteTaxes();
    }
}

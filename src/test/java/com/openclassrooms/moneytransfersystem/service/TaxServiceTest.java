package com.openclassrooms.moneytransfersystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.moneytransfersystem.model.Tax;
import com.openclassrooms.moneytransfersystem.service.tax.TaxCreationService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxDeletionService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxReadService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxUpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Test
    public void shouldCreateTax() throws Exception {

        Tax tax = new Tax();
        tax.setName("Test");
        tax.setRate(0.10);

        Tax taxSaved = new Tax();
        taxSaved.setName("Test");
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

        Tax tax = new Tax();
        tax.setName("Test");
        tax.setRate(0.10);

        List<Tax> taxes = new ArrayList<>();
        taxes.add(tax);

        Tax taxSaved = new Tax();
        taxSaved.setName("Test");
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
        tax.setId(1L);
        tax.setName("Test");
        tax.setRate(0.10);

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

        Tax tax = new Tax();
        tax.setId(1L);
        tax.setName("Test");
        tax.setRate(0.10);

        Mockito.when(taxReadService.readTaxById(1L)).thenReturn(tax);

        MvcResult mvcResult = mockMvc.perform(get("/taxes/1")).andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(tax);

        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldUpdateTax() throws Exception {

        Tax tax = new Tax();
        tax.setId(1L);
        tax.setName("Test");
        tax.setRate(0.10);

        Tax taxUpdated = new Tax();
        taxUpdated.setId(1L);
        taxUpdated.setName("Test");
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

        Mockito.doNothing().when(taxDeletionService).deleteTaxById(1L);

        mockMvc.perform(delete("/taxes/1")).andExpect(status().isOk());

        Mockito.verify(taxDeletionService, Mockito.times(1)).deleteTaxById(1L);
    }
}

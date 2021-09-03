package com.openclassrooms.moneytransfersystem.controller;

import com.openclassrooms.moneytransfersystem.model.Tax;
import com.openclassrooms.moneytransfersystem.service.tax.TaxCreationService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxDeletionService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxReadService;
import com.openclassrooms.moneytransfersystem.service.tax.TaxUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class TaxController {

    @Autowired
    private TaxCreationService taxCreationService;
    @Autowired
    private TaxReadService taxReadService;
    @Autowired
    private TaxUpdateService taxUpdateService;
    @Autowired
    private TaxDeletionService taxDeletionService;

    @PostMapping("/createTax")
    public Tax createTax(@RequestBody Tax tax) {

        return taxCreationService.createTax(tax);
    }

    @GetMapping("/taxes/{id}")
    public Tax readTaxById(@PathVariable Long id) {

        return taxReadService.readTaxById(id);
    }

    @GetMapping("/taxes")
    public Collection<Tax> readTaxes() {

        return taxReadService.readTaxes();
    }

    @PutMapping("/updateTax")
    public Tax updateTax(@RequestBody Tax tax) {

        return taxUpdateService.updateTax(tax);
    }

    @DeleteMapping("/taxes/{id}")
    public void deleteTax(@PathVariable Long id) {

        taxDeletionService.deleteTaxById(id);
    }

    @DeleteMapping("/taxes")
    public void deleteTaxes() {

        taxDeletionService.deleteTaxes();
    }
}

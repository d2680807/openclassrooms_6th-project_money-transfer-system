package com.openclassrooms.moneytransfersystem.service.tax;

import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.model.Tax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TaxCreationService {

    @Autowired
    private TaxRepository taxRepository;

    Logger logger = LogManager.getLogger(TaxCreationService.class);

    public Tax createTax(Tax tax) {

        logger.debug("[createTax] tax: " + tax);

        return taxRepository.save(tax);
    }

    public Collection<Tax> createTaxes(Collection<Tax> taxes) {

        logger.debug("[createTaxes] taxes: " + taxes);

        return taxRepository.saveAll(taxes);
    }
}

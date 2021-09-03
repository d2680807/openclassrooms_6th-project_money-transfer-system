package com.openclassrooms.moneytransfersystem.service.tax;

import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.model.Tax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxCreationService {

    @Autowired
    private TaxRepository taxRepository;

    Logger logger = LoggerFactory.getLogger(TaxCreationService.class);

    public Tax createTax(Tax tax) {

        logger.debug("[createTax] tax: " + tax);

        return taxRepository.save(tax);
    }
}

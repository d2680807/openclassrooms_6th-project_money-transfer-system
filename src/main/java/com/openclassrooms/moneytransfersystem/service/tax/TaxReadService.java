package com.openclassrooms.moneytransfersystem.service.tax;

import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import com.openclassrooms.moneytransfersystem.model.Tax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TaxReadService {

    @Autowired
    private TaxRepository taxRepository;

    Logger logger = LogManager.getLogger(TaxReadService.class);

    public Tax readTaxById(Long id) {

        logger.debug("[readTaxById] id: " + id);

        return taxRepository.findById(id).get();
    }

    public Tax readTaxByName(String name) {

        logger.debug("[readTaxByName] name: " + name);

        return taxRepository.findByName(name);
    }

    public Collection<Tax> readTaxes() {

        logger.debug("[readTaxes] find: all");

        return taxRepository.findAll();
    }
}

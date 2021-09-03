package com.openclassrooms.moneytransfersystem.service.tax;

import com.openclassrooms.moneytransfersystem.dao.TaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxDeletionService {

    @Autowired
    private TaxRepository taxRepository;

    private Logger logger = LoggerFactory.getLogger(TaxDeletionService.class);

    public void deleteTaxById(Long id) {

        if(taxRepository.existsById(id)) {
            logger.debug("[deleteTaxById] id:" + id);
            taxRepository.deleteById(id);
        }
    }

    public void deleteTaxes() {

        logger.debug("[deleteTaxes] delete: all");
        taxRepository.deleteAll();
    }
}

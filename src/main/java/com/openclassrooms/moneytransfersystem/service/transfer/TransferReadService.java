package com.openclassrooms.moneytransfersystem.service.transfer;

import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransferReadService {

    @Autowired
    private TransferRepository transferRepository;

    Logger logger = LogManager.getLogger(TransferReadService.class);

    public Transfer readTransferById(Long id) {

        logger.debug("[readTransferById] id:" + id);

        return transferRepository.getById(id);
    }

    public Collection<Transfer> readTransfers() {

        logger.debug("[readTransfers] find: all");

        return transferRepository.findAll();
    }
}

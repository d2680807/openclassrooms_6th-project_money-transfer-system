package com.openclassrooms.moneytransfersystem.service.transfer;

import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransferReadService {

    @Autowired
    private TransferRepository transferRepository;

    Logger logger = LoggerFactory.getLogger(TransferReadService.class);

    public Transfer readTransferById(Long id) {

        logger.debug("[readTransferById] id:" + id);

        return transferRepository.getById(id);
    }

    public Collection<Transfer> readTransfers() {

        logger.debug("[readTransfers] find: all");

        return transferRepository.findAll();
    }
}

package com.openclassrooms.moneytransfersystem.service.transfer;

import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferDeletionService {

    @Autowired
    private TransferRepository transferRepository;

    Logger logger = LogManager.getLogger(TransferDeletionService.class);

    public void deleteTransferById(Long id) {

        if(transferRepository.existsById(id)) {
            logger.debug("[deleteTransferById] id:" + id);
            transferRepository.deleteById(id);
        }
    }

    public void deleteTransfers() {

        logger.debug("[deleteTransfers] delete: all");
        transferRepository.deleteAll();
    }
}

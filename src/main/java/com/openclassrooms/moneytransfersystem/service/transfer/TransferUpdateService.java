package com.openclassrooms.moneytransfersystem.service.transfer;

import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransferUpdateService {

    @Autowired
    private TransferRepository transferRepository;

    Logger logger = LogManager.getLogger(TransferUpdateService.class);

    public Transfer updateTransfer(Transfer transfer) {

        Transfer transferUpdated;
        Optional<Transfer> optionalTransfer = transferRepository.findById(transfer.getId());

        if (optionalTransfer.isPresent()) {
            transferUpdated = optionalTransfer.get();
            transferRepository.save(transferUpdated);
        } else {
            logger.debug("[updateTransfer] id not found: " + transfer.getId());

            return new Transfer();
        }
        logger.debug("[updateTransfer] transfer:" + transferUpdated);

        return transferUpdated;
    }
}

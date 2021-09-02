package com.openclassrooms.moneytransfersystem.service.transfer;

import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransferCreationService {

    @Autowired
    private TransferRepository transferRepository;

    Logger logger = LoggerFactory.getLogger(TransferCreationService.class);

    public Transfer createTransfer(Transfer transfer) {

        logger.debug("[createTransfer] transfer: " + transfer);

        return transferRepository.save(transfer);
    }

    public Collection<Transfer> createTransfers(Collection<Transfer> transfers) {

        logger.debug("[createTransfers] transfers: " + transfers);

        return transferRepository.saveAll(transfers);
    }
}

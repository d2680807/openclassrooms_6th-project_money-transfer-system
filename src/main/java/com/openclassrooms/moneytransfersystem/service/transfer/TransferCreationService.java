package com.openclassrooms.moneytransfersystem.service.transfer;

import com.openclassrooms.moneytransfersystem.dao.TransferRepository;
import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.model.User;
import com.openclassrooms.moneytransfersystem.service.user.UserCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransferCreationService {

    @Autowired
    private TransferRepository transferRepository;

    Logger logger = LoggerFactory.getLogger(UserCreationService.class);

    public Transfer createTransfer(Transfer transfer) {

        logger.debug("[" + User.class.getName() + " - createTransfer]: " + transfer);

        return transferRepository.save(transfer);
    }

    public Collection<Transfer> createTransfers(Collection<Transfer> transfers) {

        logger.debug("[" + User.class.getName() + " - createTransfers]: " + transfers);

        return transferRepository.saveAll(transfers);
    }
}

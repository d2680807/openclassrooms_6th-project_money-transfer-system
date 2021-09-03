package com.openclassrooms.moneytransfersystem.controller;

import com.openclassrooms.moneytransfersystem.model.Transfer;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferCreationService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferDeletionService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferReadService;
import com.openclassrooms.moneytransfersystem.service.transfer.TransferUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class TransferController {

    @Autowired
    private TransferCreationService transferCreationService;
    @Autowired
    private TransferReadService transferReadService;
    @Autowired
    private TransferUpdateService transferUpdateService;
    @Autowired
    private TransferDeletionService transferDeletionService;

    @PostMapping("/createTransfer")
    public Transfer createTransfer(@RequestBody Transfer transfer) {

        return transferCreationService.createTransfer(transfer);
    }

    @PostMapping("/createTransfers")
    public Collection<Transfer> createTransfers(@RequestBody Collection<Transfer> transfers) {

        return transferCreationService.createTransfers(transfers);
    }

    @GetMapping("/transfers/{id}")
    public Transfer readTransferById(@PathVariable Long id) {

        return transferReadService.readTransferById(id);
    }

    @GetMapping("/transfers")
    public Collection<Transfer> readTransfers() {

        return transferReadService.readTransfers();
    }

    @PutMapping("/updateTransfer")
    public Transfer updateTransfer(@RequestBody Transfer transfer) {

        return transferUpdateService.updateTransfer(transfer);
    }

    @DeleteMapping("/transfers/{id}")
    public void deleteTransfer(@PathVariable Long id) {

        transferDeletionService.deleteTransferById(id);
    }

    @DeleteMapping("/transfers")
    public void deleteTransfers() {

        transferDeletionService.deleteTransfers();
    }
}

package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

@Data
public class TransferBack {

    Long userId;
    String recipient;
    double amount;
    String description;
}

package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

@Data
public class TransferBack {

    Long userId;
    Long recipient;
    double amount;
}

package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

@Data
public class Requirement {

    Long userId;
    String recipient;
    double amount;
    String description;
}

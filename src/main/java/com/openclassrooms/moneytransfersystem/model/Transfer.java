package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transfer {

    LocalDateTime date;
    String relation;
    String description;
    String amount;
}

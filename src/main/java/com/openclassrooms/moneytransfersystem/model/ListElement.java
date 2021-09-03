package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListElement {

    String date;
    String relation;
    String description;
    String amount;
}

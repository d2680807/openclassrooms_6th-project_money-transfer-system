package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

@Data
public class ListElement {

    String date;
    String relation;
    String description;
    String amount;
}

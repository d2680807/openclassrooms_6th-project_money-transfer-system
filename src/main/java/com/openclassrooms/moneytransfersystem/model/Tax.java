package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tax")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double rate;
}

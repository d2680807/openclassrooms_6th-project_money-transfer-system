package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "type")
    private Transfer transfer;
}

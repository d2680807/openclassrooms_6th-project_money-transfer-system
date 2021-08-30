package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = true)
    private String description;

    @JoinColumn(name = "type", insertable=false, updatable=false)
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id",referencedColumnName="user_id", insertable=false, updatable=false)
    private User user;
}

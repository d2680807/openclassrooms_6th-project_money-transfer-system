package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transfer")
public class Ingoing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private double tax;
    @Column(nullable = true)
    private String description;

    //@Column(nullable = true)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sender")_
    )
    private User sender;

    //@Column(nullable = true)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver")
    )
    private User receiver;
}

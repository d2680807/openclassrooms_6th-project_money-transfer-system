package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ingoing")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingoing_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private double tax;
    @Column(nullable = true)
    private String description;

    @OneToOne(mappedBy = "ingoing")
    private Outgoing outgoing;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", insertable = false, updatable = false)
    )
    private User receiver;
}

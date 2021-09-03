package com.openclassrooms.moneytransfersystem.model;

import com.openclassrooms.moneytransfersystem.model.utility.TransferType;
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
    @Column(nullable = false)
    private double tax;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferType type;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="user_id", updatable=false)
    private User user;
}

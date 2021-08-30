package com.openclassrooms.moneytransfersystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private int ibanCode;
    @Column(nullable = false)
    private int bicCode;
    @Column(nullable = false)
    private double balance;

    private String friendsList;

    @OneToMany(mappedBy = "sender")
    private Collection<Ingoing> ingoingTransfers;

    @OneToMany(mappedBy = "receiver")
    private Collection<Outgoing> outgoingTransfers;
}

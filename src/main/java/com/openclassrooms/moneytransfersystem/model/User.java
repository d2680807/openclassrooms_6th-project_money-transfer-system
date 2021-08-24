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
    Long id;

    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    @Column(nullable = false)
    float balance;

    @OneToMany(mappedBy = "user")
    private Collection<Transfer> transfers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "friendship",
            joinColumns = @JoinColumn(name = "invited_by"),
            inverseJoinColumns = @JoinColumn(name = "recipient")
    )
    private Collection<Friendship> friendships;
}

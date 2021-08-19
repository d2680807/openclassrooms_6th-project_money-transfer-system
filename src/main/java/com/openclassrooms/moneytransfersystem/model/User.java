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
    @GeneratedValue
    @Column(name = "user_id")
    Long id;

    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    boolean active;
    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    @Column(nullable = false)
    float balance;

    /*@OneToOne(mappedBy = "user")
    private UsersDetails usersDetails;*/

    /*@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_details",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "detail_id")
    )
    private Collection<Detail> details;*/

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_links",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Collection<Link> links;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "friendship",
            joinColumns = @JoinColumn(name = "invited_by"),
            inverseJoinColumns = @JoinColumn(name = "recipient")
    )
    private Collection<Friendship> friendships;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_transfers",
            joinColumns = @JoinColumn(name = "made_by"),
            inverseJoinColumns = @JoinColumn(name = "transfer_id")
    )
    private Collection<Transfer> transfers;
}

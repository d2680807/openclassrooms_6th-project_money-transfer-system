package com.openclassrooms.moneytransfersystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long id;

    @Column(nullable = false)
    private String bankAccount;
    @Column(nullable = false)
    private String swiftCode;

    /*@OneToOne(mappedBy = "link")
    private UsersLinks usersLinks;*/

    @ManyToMany(mappedBy = "links")
    private Collection<User> users;
}

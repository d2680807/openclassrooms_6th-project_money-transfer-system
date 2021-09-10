package com.openclassrooms.moneytransfersystem.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

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

    @OneToMany(mappedBy = "user")
    private Collection<Transfer> transfers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(int ibanCode) {
        this.ibanCode = ibanCode;
    }

    public int getBicCode() {
        return bicCode;
    }

    public void setBicCode(int bicCode) {
        this.bicCode = bicCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(String friendsList) {
        this.friendsList = friendsList;
    }

    public Collection<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(Collection<Transfer> transfers) {
        this.transfers = transfers;
    }
}

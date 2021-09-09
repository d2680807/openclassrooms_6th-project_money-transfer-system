package com.openclassrooms.moneytransfersystem.model;

import com.openclassrooms.moneytransfersystem.model.utility.TransferType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransferType getType() {
        return type;
    }

    public void setType(TransferType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", date=" + date +
                ", amount=" + amount +
                ", tax=" + tax +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Double.compare(transfer.amount, amount) == 0 && Double.compare(transfer.tax, tax) == 0 && Objects.equals(id, transfer.id) && Objects.equals(date, transfer.date) && Objects.equals(description, transfer.description) && type == transfer.type && Objects.equals(user, transfer.user);
    }
}

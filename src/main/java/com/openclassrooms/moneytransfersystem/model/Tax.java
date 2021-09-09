package com.openclassrooms.moneytransfersystem.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tax")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tax_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Tax{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Double.compare(tax.rate, rate) == 0 && Objects.equals(id, tax.id) && Objects.equals(name, tax.name);
    }
}

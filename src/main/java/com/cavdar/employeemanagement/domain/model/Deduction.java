package com.cavdar.employeemanagement.domain.model;

import com.cavdar.employeemanagement.domain.enums.DeductionType;
import jakarta.persistence.*;

@Entity
@Table(name = "deduction")
public class Deduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private double amount;

    @Column(name = "type")
    private DeductionType type;

    public Deduction() {
    }

    public Deduction(Long id, String name, double amount, DeductionType type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type;
    }

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public DeductionType getType() {
        return type;
    }

    public void setType(DeductionType type) {
        this.type = type;
    }
}

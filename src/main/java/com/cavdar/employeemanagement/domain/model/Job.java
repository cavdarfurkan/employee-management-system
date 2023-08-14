package com.cavdar.employeemanagement.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "min_salary")
    private double minSalary;

    @Column(name = "max_salary")
    private double maxSalary;

    public Job() {
    }

    public Job(Long id, String name, double minSalary, double maxSalary) {
        this.id = id;
        this.name = name;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public Long getId() {
        return id;
    }

    public Job setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Job setName(String name) {
        this.name = name;
        return this;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public Job setMinSalary(double minSalary) {
        this.minSalary = minSalary;
        return this;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public Job setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
        return this;
    }
}
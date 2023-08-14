package com.cavdar.employeemanagement.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "payroll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "pay_date")
    private LocalDate payDate;

    @Column(name = "gross_pay")
    private double grossPay;

    @Column(name = "deductions")
    private double deductions;

    @Column(name = "net_pay")
    private double netPay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "payroll_deduction",
            joinColumns = {@JoinColumn(name = "payroll_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "deduction_id", referencedColumnName = "id")})
    private Deduction deduction;

    public Payroll() {
    }

    public Payroll(Long id, LocalDate payDate, double grossPay, double deductions, double netPay, Employee employee, Deduction deduction) {
        this.id = id;
        this.payDate = payDate;
        this.grossPay = grossPay;
        this.deductions = deductions;
        this.netPay = netPay;
        this.employee = employee;
        this.deduction = deduction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Deduction getDeduction() {
        return deduction;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }
}
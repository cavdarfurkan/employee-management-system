package com.cavdar.employeemanagement.domain.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "time_tracking")
public class TimeTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private Timestamp startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Timestamp endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    public TimeTracking() {
    }

    public TimeTracking(Long id, Timestamp startTime, Timestamp endTime, Employee employee) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public TimeTracking setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        return this;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public TimeTracking setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        return this;
    }

    public Employee getEmployee() {
        return employee;
    }

    public TimeTracking setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }
}
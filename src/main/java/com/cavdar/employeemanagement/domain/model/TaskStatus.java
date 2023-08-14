package com.cavdar.employeemanagement.domain.model;

import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "task_status")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private TaskStatusEnum name;

    @OneToMany(mappedBy = "status")
    private List<Task> tasks;

    public TaskStatus() {
    }

    public TaskStatus(TaskStatusEnum name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatusEnum getName() {
        return name;
    }

    public void setName(TaskStatusEnum name) {
        this.name = name;
    }
}
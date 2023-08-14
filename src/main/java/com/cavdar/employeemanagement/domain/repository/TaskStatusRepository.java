package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;
import com.cavdar.employeemanagement.domain.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    TaskStatus findByName(TaskStatusEnum name);
}
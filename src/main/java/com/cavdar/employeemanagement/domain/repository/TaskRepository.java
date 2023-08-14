package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
//    List<Task> findAllByEmployeeId(Long id);

    List<Task> findAllByEmployeeIdOrderByDueDateAsc(Long id);

    List<Task> findAllByEmployeeOrderByDueDateAsc(Employee employee);



    List<Task> findAllByEmployeeIdAndDueDate(Long id, Timestamp dueDate);

    long countByEmployee_Id(Long id);

    long countByEmployee(Employee employee);
}
package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.TimeTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface TimeTrackingRepository extends JpaRepository<TimeTracking, Long> {
    List<TimeTracking> findAllByEmployee(Employee employee);

    List<TimeTracking> findAllByEmployeeAndEndTimeNotNull(Employee employee);


    Optional<TimeTracking> findByEmployeeAndEndTime(Employee employee, Timestamp endTime);


}
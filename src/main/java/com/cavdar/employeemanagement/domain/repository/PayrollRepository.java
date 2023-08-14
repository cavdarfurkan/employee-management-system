package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
}
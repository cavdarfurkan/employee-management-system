package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeductionRepository extends JpaRepository<Deduction, Long> {
}
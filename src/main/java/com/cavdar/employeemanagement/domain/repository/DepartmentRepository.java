package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Page<Department> findByNameContainsIgnoreCase(String name, Pageable pageable);


}
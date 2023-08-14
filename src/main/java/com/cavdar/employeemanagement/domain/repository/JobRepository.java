package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByNameContainsIgnoreCaseOrMinSalaryGreaterThanEqualAndMaxSalaryLessThanEqual(String name, double minSalary, double maxSalary, Pageable pageable);

    Page<Job> findByNameContainsIgnoreCase(String name, Pageable pageable);


}
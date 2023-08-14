package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByAuthority(String authority);
}
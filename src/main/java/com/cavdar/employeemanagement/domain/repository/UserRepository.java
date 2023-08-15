package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
            select u from User u inner join u.authorities authorities 
            where authorities.authority = ?1 
            order by RAND() LIMIT 1""")
    User randomUserByAuthority(String authority);

    @Query("""
            select u from User u inner join u.authorities authorities
            where authorities.authority = ?1 and u.id not in ?2
            order by RAND() LIMIT 1""")
    User randomUserByAuthority(String authority, Collection<Long> ids);
}
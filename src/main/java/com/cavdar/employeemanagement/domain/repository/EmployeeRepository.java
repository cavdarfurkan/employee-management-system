package com.cavdar.employeemanagement.domain.repository;

import com.cavdar.employeemanagement.domain.model.Department;
import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.Job;
import com.cavdar.employeemanagement.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByManagerId(Long id);

    List<Employee> findAllByDepartment_Id(Long id);

    List<Employee> findAllByJob_Id(Long id);

    List<Employee> findByJob_NameIgnoreCase(String name);

    Optional<Employee> findByUser(User user);

    long countByManager_Id(Long id);


    @Transactional
    @Modifying
    @Query("update Employee e set e.manager = ?1 where e.manager = ?2")
    int updateManagerByManager(Employee newManager, Employee oldManager);

    @Transactional
    @Modifying
    @Query("update Employee e set e.user = ?1 where e.id = ?2")
    void updateUserById(User user, Long id);

    @Transactional
    @Modifying
    @Query("update Employee e set e.department = ?1 where e.department = ?2")
    int updateDepartmentByDepartment(Department newDepartment, Department oldDepartment);

    @Transactional
    @Modifying
    @Query("update Employee e set e.job = ?1 where e.job = ?2")
    int updateJobByJob(Job newJob, Job oldJob);

    Page<Employee> findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCaseOrJob_NameContainsIgnoreCase(
            String firstName, String lastName, String jobName, Pageable pageable);

}
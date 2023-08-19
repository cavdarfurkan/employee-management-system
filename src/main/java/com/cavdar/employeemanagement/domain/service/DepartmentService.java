package com.cavdar.employeemanagement.domain.service;

import com.cavdar.employeemanagement.domain.model.Department;
import com.cavdar.employeemanagement.domain.repository.DepartmentRepository;
import com.cavdar.employeemanagement.util.exception.DepartmentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, EmployeeService employeeService) {
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
    }

    public List<Department> getDepartments() {
        return this.departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return this.departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    public Page<Department> pageAndSearchDepartments(Pageable pageable, String search) {
        if (search == null) {
            return this.departmentRepository.findAll(pageable);
        }

        return this.departmentRepository.findByNameContainsIgnoreCase(search, pageable);
    }

    public Department saveDepartment(Department department) {
        return this.departmentRepository.save(department);
    }

    public Department updateDepartmentById(Department updatedDepartment, Long id) {
        Department department = this.getDepartmentById(id);

        department.setName(updatedDepartment.getName());

        return saveDepartment(department);
    }

    public void deleteDepartmentById(Long id) {
        Department department = getDepartmentById(id);

        this.employeeService.updateDepartmentByDepartment(null, department);

        this.departmentRepository.delete(department);
    }
}

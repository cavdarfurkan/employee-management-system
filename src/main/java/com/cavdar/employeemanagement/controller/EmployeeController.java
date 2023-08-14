package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.assembler.EmployeeModelAssembler;
import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.util.exception.EmployeeNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeModelAssembler;
    private final PagedResourcesAssembler<Employee> pagedResourcesAssembler;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeModelAssembler employeeModelAssembler, PagedResourcesAssembler<Employee> pagedResourcesAssembler) {
        this.employeeService = employeeService;
        this.employeeModelAssembler = employeeModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/all-paged")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public PagedModel<EntityModel<Employee>> getAllPaged(Pageable pageable, @RequestParam(required = false) String search) {
        Page<Employee> employeePage = this.employeeService.pageAndSearchEmployees(pageable, search);
        return pagedResourcesAssembler.toModel(employeePage, employeeModelAssembler);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Employee>> getAll() {
        List<EntityModel<Employee>> employees =
                this.employeeService.getEmployees().stream()
                        .map(employeeModelAssembler::toModel)
                        .toList();

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public EntityModel<Employee> getOne(@PathVariable Long id) {
        Employee employee = this.employeeService.getEmployeeById(id);

        return employeeModelAssembler.toModel(employee);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> getOneByUserId(@RequestParam Long id) {
        try {
            EntityModel<Employee> entityModel =
                    employeeModelAssembler.toModel(this.employeeService.getEmployeeByUserId(id));

            return ResponseEntity.ok().body(entityModel);
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> newEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = this.employeeService.saveEmployeeAndUser(employee);
            EntityModel<Employee> entityModel = employeeModelAssembler.toModel(savedEmployee);

            return ResponseEntity
                    .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                    .body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        EntityModel<Employee> entityModel =
                this.employeeModelAssembler.toModel(this.employeeService.updateEmployeeById(employee, id));

        return ResponseEntity
                .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        this.employeeService.deleteEmployeeById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public long employeeCount() {
        return this.employeeService.employeesCount();
    }

/*
##################
MANAGER CONTROLLER
##################
 */

    @GetMapping("/all/managers/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Employee>> getAllEmployeesByManager(@PathVariable Long id) {
        List<EntityModel<Employee>> arr =
                this.employeeService.getEmployeesByManagerId(id).stream()
                        .map(employeeModelAssembler::toModel)
                        .toList();

        return CollectionModel.of(
                arr,
                linkTo(methodOn(EmployeeController.class).getAllEmployeesByManager(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getOne(id)).withRel("employees"),
                linkTo(methodOn(EmployeeController.class).getAllManagers()).withRel("managers")
        );
    }

    @GetMapping("/managers/all")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Employee>> getAllManagers() {
        return CollectionModel.of(this.employeeService.getAllManagers().stream()
                        .map(employeeModelAssembler::toManagerModel)
                        .toList(),
                linkTo(methodOn(EmployeeController.class).getAllManagers()).withSelfRel()
        );
    }

    @GetMapping("/managers/all-except/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Employee>> getAllManagersExcept(@PathVariable Long id) {
        return CollectionModel.of(this.employeeService.getAllManagersExcept(id).stream()
                        .map(employeeModelAssembler::toManagerModel)
                        .toList(),
                linkTo(methodOn(EmployeeController.class).getAllManagers()).withSelfRel()
        );
    }

    @GetMapping("/count/managers/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public long employeeCountByManager(@PathVariable Long id) {
        return this.employeeService.employeesCountOfManager(id);
    }
}
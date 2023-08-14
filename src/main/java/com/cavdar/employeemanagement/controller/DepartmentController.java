package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.assembler.DepartmentModelAssembler;
import com.cavdar.employeemanagement.domain.model.Department;
import com.cavdar.employeemanagement.domain.service.DepartmentService;
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
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentModelAssembler departmentModelAssembler;
    private final PagedResourcesAssembler<Department> pagedResourcesAssembler;


    @Autowired
    public DepartmentController(DepartmentService departmentService, DepartmentModelAssembler departmentModelAssembler, PagedResourcesAssembler<Department> pagedResourcesAssembler) {
        this.departmentService = departmentService;
        this.departmentModelAssembler = departmentModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Department>> getAllDepartments() {
        List<EntityModel<Department>> departments = this.departmentService.getDepartments().stream()
                .map(departmentModelAssembler::toModel)
                .toList();

        return CollectionModel.of(
                departments,
                linkTo(methodOn(DepartmentController.class).getAllDepartments()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public EntityModel<Department> getOneDepartment(@PathVariable Long id) {
        return departmentModelAssembler.toModel(this.departmentService.getDepartmentById(id));
    }

    @GetMapping("/all-paged")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public PagedModel<EntityModel<Department>> getAllPaged(Pageable pageable, @RequestParam(required = false) String search) {
        Page<Department> departmentPage = this.departmentService.pageAndSearchDepartments(pageable, search);
        return pagedResourcesAssembler.toModel(departmentPage, departmentModelAssembler);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> newDepartment(@RequestBody Department department) {
        try {
            Department savedDepartment = this.departmentService.saveDepartment(department);
            EntityModel<Department> entityModel = departmentModelAssembler.toModel(savedDepartment);

            return ResponseEntity
                    .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                    .body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDepartment(@RequestBody Department department, @PathVariable Long id) {
        EntityModel<Department> entityModel =
                this.departmentModelAssembler.toModel(this.departmentService.updateDepartmentById(department, id));

        return ResponseEntity
                .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        this.departmentService.deleteDepartmentById(id);

        return ResponseEntity.noContent().build();
    }
}

package com.cavdar.employeemanagement.domain.assembler;

import com.cavdar.employeemanagement.controller.DepartmentController;
import com.cavdar.employeemanagement.domain.model.Department;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DepartmentModelAssembler implements RepresentationModelAssembler<Department, EntityModel<Department>> {

    @Override
    public EntityModel<Department> toModel(Department entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(DepartmentController.class).getOneDepartment(entity.getId())).withSelfRel(),
                linkTo(methodOn(DepartmentController.class).getAllPaged(Pageable.unpaged(), null)).withRel("departments"),
                linkTo(methodOn(DepartmentController.class).getAllDepartments()).withRel("departments")
        );
    }
}

package com.cavdar.employeemanagement.domain.assembler;

import com.cavdar.employeemanagement.controller.EmployeeController;
import com.cavdar.employeemanagement.controller.TaskController;
import com.cavdar.employeemanagement.domain.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(Employee entity) {
        EntityModel<Employee> entityModel = EntityModel.of(
                entity,
                linkTo(methodOn(EmployeeController.class).getOne(entity.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllPaged(Pageable.unpaged(), null)).withRel("employees"),
                linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees"),
                linkTo(methodOn(TaskController.class).getAllTasksOfEmployee(entity.getId())).withRel("tasks")
        );

        if (entity.getManager() == null) {
            return entityModel;
        }

        return entityModel.add(
                linkTo(methodOn(EmployeeController.class).getOne(entity.getManager().getId())).withRel("manager")
        );
    }

    public EntityModel<Employee> toManagerModel(Employee entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(EmployeeController.class).getOne(entity.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllPaged(Pageable.unpaged(), null)).withRel("employees"),
                linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees"),
                linkTo(methodOn(EmployeeController.class).getAllManagers()).withRel("managers"),
                linkTo(methodOn(EmployeeController.class).getAllEmployeesByManager(entity.getId())).withRel("managers")
        );
    }
}

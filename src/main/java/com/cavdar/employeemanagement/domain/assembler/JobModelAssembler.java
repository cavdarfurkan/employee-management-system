package com.cavdar.employeemanagement.domain.assembler;

import com.cavdar.employeemanagement.controller.JobController;
import com.cavdar.employeemanagement.domain.model.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class JobModelAssembler implements RepresentationModelAssembler<Job, EntityModel<Job>> {

    @Override
    public EntityModel<Job> toModel(Job entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(JobController.class).getOneJob(entity.getId())).withSelfRel(),
                linkTo(methodOn(JobController.class).getAllPaged(Pageable.unpaged(), null)).withRel("jobs"),
                linkTo(methodOn(JobController.class).getAllJobs()).withRel("jobs")
        );
    }
}

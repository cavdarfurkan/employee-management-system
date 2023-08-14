package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.assembler.JobModelAssembler;
import com.cavdar.employeemanagement.domain.model.Job;
import com.cavdar.employeemanagement.domain.service.JobService;
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
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final JobModelAssembler jobModelAssembler;
    private final PagedResourcesAssembler<Job> pagedResourcesAssembler;


    @Autowired
    public JobController(JobService jobService, JobModelAssembler jobModelAssembler, PagedResourcesAssembler<Job> pagedResourcesAssembler) {
        this.jobService = jobService;
        this.jobModelAssembler = jobModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Job>> getAllJobs() {
        List<EntityModel<Job>> jobs = this.jobService.getJobs().stream()
                .map(jobModelAssembler::toModel)
                .toList();

        return CollectionModel.of(
                jobs,
                linkTo(methodOn(JobController.class).getAllJobs()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public EntityModel<Job> getOneJob(@PathVariable Long id) {
        return jobModelAssembler.toModel(this.jobService.getJobById(id));
    }

    @GetMapping("/all-paged")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public PagedModel<EntityModel<Job>> getAllPaged(Pageable pageable, @RequestParam(required = false) String search) {
        Page<Job> jobPage = this.jobService.pageAndSearchJobs(pageable, search);
        return this.pagedResourcesAssembler.toModel(jobPage, jobModelAssembler);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> newJob(@RequestBody Job job) {
        try {
            Job savedJob = this.jobService.saveJob(job);
            EntityModel<Job> entityModel = jobModelAssembler.toModel(savedJob);

            return ResponseEntity
                    .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                    .body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateJob(@RequestBody Job job, @PathVariable Long id) {
        EntityModel<Job> entityModel =
                this.jobModelAssembler.toModel(this.jobService.updateJobById(job, id));

        return ResponseEntity
                .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        this.jobService.deleteJobById(id);

        return ResponseEntity.noContent().build();
    }
}

package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.assembler.TimeTrackingModelAssembler;
import com.cavdar.employeemanagement.domain.model.TimeTracking;
import com.cavdar.employeemanagement.domain.service.TimeTrackingService;
import com.cavdar.employeemanagement.util.exception.TimeTrackingAlreadyRunningException;
import com.cavdar.employeemanagement.util.exception.TimeTrackingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/timetracking")
public class TimeTrackingController {

    private final TimeTrackingService timeTrackingService;
    private final TimeTrackingModelAssembler timeTrackingModelAssembler;

    @Autowired
    public TimeTrackingController(TimeTrackingService timeTrackingService, TimeTrackingModelAssembler timeTrackingModelAssembler) {
        this.timeTrackingService = timeTrackingService;
        this.timeTrackingModelAssembler = timeTrackingModelAssembler;
    }

    @GetMapping("/{id}")
    public EntityModel<TimeTracking> getOneTimeTracking(@PathVariable Long id) {
        return this.timeTrackingModelAssembler.toModel(this.timeTrackingService.getTimeTrackingById(id));
    }

    @GetMapping("/all/employee")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<TimeTracking>> getAllByEmployeeId(@RequestParam Long id) {
        List<EntityModel<TimeTracking>> entityModels = this.timeTrackingService.getAllTimeTrackingByEmployee(id)
                .stream()
                .map(timeTrackingModelAssembler::toModel)
                .toList();

        return CollectionModel.of(
                entityModels,
                linkTo(methodOn(TimeTrackingController.class).getAllByEmployeeId(id)).withSelfRel()
        );
    }

    @GetMapping("/active/employee")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> getActiveTimeTracker(@RequestParam Long id) {
        try {
            EntityModel<TimeTracking> entityModel = this.timeTrackingModelAssembler.toModel(
                    this.timeTrackingService.getActiveTimeTracking(id));

            return ResponseEntity.ok().body(entityModel);
        } catch (TimeTrackingNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(e.getMessage());
        }

    }

    @PostMapping("/start/employee")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> startTimeTracking(@RequestParam Long id) {
        try {
            EntityModel<TimeTracking> entityModel =
                    timeTrackingModelAssembler.toModel(this.timeTrackingService.startTracking(id));

            return ResponseEntity
                    .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                    .body(entityModel);
        } catch (TimeTrackingAlreadyRunningException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/stop/employee")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> stopTimeTracking(@RequestParam Long id) {
        try {
            EntityModel<TimeTracking> entityModel
                    = timeTrackingModelAssembler.toModel(this.timeTrackingService.stopTracking(id));

            return ResponseEntity.ok().body(entityModel);
        } catch (TimeTrackingNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTimeTracking(@PathVariable Long id) {
        this.timeTrackingService.deleteTimeTracking(id);

        return ResponseEntity.noContent().build();
    }
}

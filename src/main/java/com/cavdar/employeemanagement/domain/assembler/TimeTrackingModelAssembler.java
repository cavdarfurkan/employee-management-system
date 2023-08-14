package com.cavdar.employeemanagement.domain.assembler;

import com.cavdar.employeemanagement.controller.TimeTrackingController;
import com.cavdar.employeemanagement.domain.model.TimeTracking;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TimeTrackingModelAssembler implements RepresentationModelAssembler<TimeTracking, EntityModel<TimeTracking>> {

    @Override
    public EntityModel<TimeTracking> toModel(TimeTracking entity) {
        Link selfLink = linkTo(methodOn(TimeTrackingController.class).getOneTimeTracking(entity.getId())).withSelfRel();
        Link timeTrackingsLink = linkTo(methodOn(TimeTrackingController.class).getAllByEmployeeId(entity.getEmployee().getId())).withRel("timetrackings");

        EntityModel<TimeTracking> entityModel = EntityModel.of(entity, selfLink, timeTrackingsLink);

        if (entity.getEndTime() == null) {
            Link stopLink = linkTo(methodOn(TimeTrackingController.class).stopTimeTracking(entity.getId())).withRel("actions");

            entityModel.add(stopLink);
        }

        return entityModel;
    }
}

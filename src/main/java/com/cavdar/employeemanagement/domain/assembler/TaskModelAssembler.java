package com.cavdar.employeemanagement.domain.assembler;

import com.cavdar.employeemanagement.controller.TaskController;
import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;
import com.cavdar.employeemanagement.domain.model.Task;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {

    @Override
    public EntityModel<Task> toModel(Task entity) {
        Link selfLink = linkTo(methodOn(TaskController.class).getOneTask(entity.getId())).withSelfRel();
        Link tasksLink = linkTo(methodOn(TaskController.class).getAllTasks()).withRel("tasks");

        EntityModel<Task> entityModel = EntityModel.of(entity, selfLink, tasksLink);

        if (entity.getStatus().getName() == TaskStatusEnum.PENDING) {
            Link startLink = linkTo(methodOn(TaskController.class).startTask(entity.getId())).withRel("actions");
            Link canceledLink = linkTo(methodOn(TaskController.class).cancelTask(entity.getId())).withRel("actions");

            entityModel.add(startLink, canceledLink);
        }

        if (entity.getStatus().getName() == TaskStatusEnum.IN_PROGRESS) {
            Link completedLink = linkTo(methodOn(TaskController.class).completeTask(entity.getId())).withRel("actions");
            Link canceledLink = linkTo(methodOn(TaskController.class).cancelTask(entity.getId())).withRel("actions");

            entityModel.add(completedLink, canceledLink);
        }

        return entityModel;
    }
}

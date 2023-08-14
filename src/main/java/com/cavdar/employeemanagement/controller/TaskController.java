package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.assembler.TaskModelAssembler;
import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;
import com.cavdar.employeemanagement.domain.model.Task;
import com.cavdar.employeemanagement.domain.model.TaskStatus;
import com.cavdar.employeemanagement.domain.service.TaskService;
import com.cavdar.employeemanagement.util.exception.TaskStatusNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskModelAssembler taskModelAssembler;

    @Autowired
    public TaskController(TaskService taskService, TaskModelAssembler taskModelAssembler) {
        this.taskService = taskService;
        this.taskModelAssembler = taskModelAssembler;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Task>> getAllTasks() {
        List<EntityModel<Task>> tasks = this.taskService.getTasks().stream()
                .map(taskModelAssembler::toModel)
                .toList();

        return CollectionModel.of(
                tasks,
                linkTo(methodOn(TaskController.class).getAllTasks()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public EntityModel<Task> getOneTask(@PathVariable Long id) {
        return taskModelAssembler.toModel(this.taskService.getTaskById(id));
    }

    @GetMapping("/all/employee")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public CollectionModel<EntityModel<Task>> getAllTasksOfEmployee(@RequestParam Long id) {
        List<EntityModel<Task>> tasks = this.taskService.getTasksByEmployee(id).stream()
                .map(taskModelAssembler::toModel)
                .toList();

        return CollectionModel.of(
                tasks,
                linkTo(methodOn(TaskController.class).getAllTasksOfEmployee(id)).withSelfRel()
        );
    }

    @GetMapping("/count/employee")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public long taskCountOfEmployee(@RequestParam Long id) {
        return this.taskService.taskCountByEmployeeId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> newTask(@RequestBody Task task) {
        try {
            Task savedTask = this.taskService.saveTask(task);
            EntityModel<Task> entityModel = this.taskModelAssembler.toModel(savedTask);

            return ResponseEntity
                    .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                    .body(entityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTask(@RequestBody Task task, @PathVariable Long id) {
        EntityModel<Task> entityModel =
                this.taskModelAssembler.toModel(this.taskService.updateTaskById(task, id));

        return ResponseEntity
                .created(entityModel.getRequiredLink(LinkRelation.of("self")).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        this.taskService.deleteTaskById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/start/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> startTask(@PathVariable Long id) {
        TaskStatus progressStatus = this.taskService.getTaskStatusByName(TaskStatusEnum.IN_PROGRESS);

        Task task = this.taskService.getTaskById(id);
        if (task.getStatus().getName() != TaskStatusEnum.PENDING) {
            throw new TaskStatusNotAllowedException(task.getStatus().getName(), progressStatus.getName());
        }
        task.setStatus(progressStatus);

        EntityModel<Task> entityModel = taskModelAssembler.toModel(this.taskService.updateTaskById(task, id));

        return ResponseEntity.ok().body(entityModel);
    }

    @PutMapping("/complete/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> completeTask(@PathVariable Long id) {
        TaskStatus completedStatus = this.taskService.getTaskStatusByName(TaskStatusEnum.COMPLETED);

        Task task = this.taskService.getTaskById(id);
        if (task.getStatus().getName() != TaskStatusEnum.IN_PROGRESS) {
            throw new TaskStatusNotAllowedException(task.getStatus().getName(), completedStatus.getName());
        }
        task.setStatus(completedStatus);

        EntityModel<Task> entityModel = taskModelAssembler.toModel(this.taskService.updateTaskById(task, id));

        return ResponseEntity.ok().body(entityModel);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelTask(@PathVariable Long id) {
        TaskStatus canceledStatus = this.taskService.getTaskStatusByName(TaskStatusEnum.CANCELED);

        Task task = this.taskService.getTaskById(id);
        if (task.getStatus().getName() != TaskStatusEnum.IN_PROGRESS && task.getStatus().getName() != TaskStatusEnum.PENDING) {
            throw new TaskStatusNotAllowedException(task.getStatus().getName(), canceledStatus.getName());
        }
        task.setStatus(canceledStatus);

        EntityModel<Task> entityModel = taskModelAssembler.toModel(this.taskService.updateTaskById(task, id));

        return ResponseEntity.ok().body(entityModel);
    }
}

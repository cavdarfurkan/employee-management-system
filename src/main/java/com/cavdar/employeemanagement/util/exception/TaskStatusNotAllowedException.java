package com.cavdar.employeemanagement.util.exception;

import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;

public class TaskStatusNotAllowedException extends RuntimeException {
    public TaskStatusNotAllowedException(TaskStatusEnum notAllowedStatus, TaskStatusEnum taskStatus) {
        super(notAllowedStatus.name() + " task status is not allowed on " + taskStatus.name() + " task status.");
    }
}

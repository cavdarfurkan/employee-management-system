package com.cavdar.employeemanagement.util.exception;

import com.cavdar.employeemanagement.domain.model.Employee;

public class ManagerLoopException extends RuntimeException {
    public ManagerLoopException(Employee manager) {
        super("Setting " + manager.getManager().getFirstName() + " " + manager.getManager().getLastName()
                + " as the manager of " + manager.getFirstName() + " " + manager.getLastName() + " causes a loop!");
    }
}

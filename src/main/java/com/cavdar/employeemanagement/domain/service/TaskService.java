package com.cavdar.employeemanagement.domain.service;

import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;
import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.Task;
import com.cavdar.employeemanagement.domain.model.TaskStatus;
import com.cavdar.employeemanagement.domain.repository.TaskRepository;
import com.cavdar.employeemanagement.domain.repository.TaskStatusRepository;
import com.cavdar.employeemanagement.util.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskStatusRepository statusRepository;
    private final EmployeeService employeeService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskStatusRepository statusRepository, EmployeeService employeeService) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.employeeService = employeeService;
    }

    public Task saveTask(Task task) {
        if (task.getEmployee() == null) {
            throw new IllegalArgumentException("An employee must be assigned to the task");
        }

        if (task.getStatus() == null) {
            TaskStatus pendingStatus = getTaskStatusByName(TaskStatusEnum.PENDING);
            task.setStatus(pendingStatus);
            task.setAssignmentDate(new Timestamp(System.currentTimeMillis()));
        }

        return this.taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> getTasks() {
        return this.taskRepository.findAll();
    }

    public List<Task> getTasksByEmployee(Long id) {
        Employee employee = this.employeeService.getEmployeeById(id);
        return this.taskRepository.findAllByEmployeeOrderByDueDateAsc(employee);
    }

    public Task updateTaskById(Task updatedTask, Long id) {
        Task task = this.getTaskById(id);

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setEmployee(updatedTask.getEmployee());
        task.setStatus(updatedTask.getStatus());

        return this.saveTask(task);
    }

    public void deleteTaskById(Long id) {
        this.taskRepository.delete(getTaskById(id));
    }

    public long taskCountByEmployeeId(Long id) {
        return this.taskRepository.countByEmployee_Id(id);
    }


    // ####TaskStatus#####

    public TaskStatus getTaskStatusByName(TaskStatusEnum name) {
        return this.statusRepository.findByName(name);
    }
}

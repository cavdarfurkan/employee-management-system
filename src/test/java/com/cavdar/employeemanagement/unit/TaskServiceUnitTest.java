package com.cavdar.employeemanagement.unit;

import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;
import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.Task;
import com.cavdar.employeemanagement.domain.model.TaskStatus;
import com.cavdar.employeemanagement.domain.repository.TaskRepository;
import com.cavdar.employeemanagement.domain.repository.TaskStatusRepository;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.domain.service.TaskService;
import com.cavdar.employeemanagement.util.exception.EmployeeNotFoundException;
import com.cavdar.employeemanagement.util.exception.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUnitTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskStatusRepository statusRepository;

    @Mock
    EmployeeService employeeService;

    @Test
    void testGetTaskStatusByName() {
        when(statusRepository.findByName(any(TaskStatusEnum.class)))
                .thenAnswer(invocation -> new TaskStatus(invocation.getArgument(0, TaskStatusEnum.class)));

        TaskStatus status = taskService.getTaskStatusByName(TaskStatusEnum.PENDING);

        assertNotNull(status);
        assertEquals(TaskStatusEnum.PENDING, status.getName());
    }

    @Test
    void testSaveTask_Return() {
        when(taskRepository.save(any(Task.class))).then(returnsFirstArg());
        when(statusRepository.findByName(any(TaskStatusEnum.class)))
                .thenAnswer(invocation -> new TaskStatus(invocation.getArgument(0, TaskStatusEnum.class)));

        Employee employee = new Employee();
        Task task = new Task(1L, "Test Task", "Test Task Description", Timestamp.from(Instant.now()), employee);

        assertDoesNotThrow(() -> taskService.saveTask(task));

        Task savedTask = taskService.saveTask(task);
        assertEquals(1L, savedTask.getId());
        assertNotNull(savedTask.getEmployee());
        assertNotNull(savedTask.getStatus());
        assertNotNull(savedTask.getAssignmentDate());
        assertEquals(TaskStatusEnum.PENDING, savedTask.getStatus().getName());
    }

    @Test
    void testSaveTask_Throw() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Task Description");
        task.setDueDate(Timestamp.from(Instant.now()));

        assertThrows(IllegalArgumentException.class, () -> taskService.saveTask(task));
    }

    @Test
    void testFindTask_Return() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(new Task(1L, "Test Task", "Test Task Description", Timestamp.from(Instant.now()), new Employee())));

        assertEquals(1L, taskService.getTaskById(1L).getId());
    }

    @Test
    void testFindTask_Throw() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testFindTasksByEmployee_Return() {
        when(employeeService.getEmployeeById(1L))
                .thenReturn(new Employee());
        when(taskRepository.findAllByEmployeeOrderByDueDateAsc(any(Employee.class)))
                .thenReturn(new ArrayList<Task>());

        assertDoesNotThrow(() -> taskService.getTasksByEmployee(1L));
    }

    @Test
    void testFindTasksByEmployee_Throws() {
        when(employeeService.getEmployeeById(1L))
                .thenThrow(EmployeeNotFoundException.class);

        assertThrows(EmployeeNotFoundException.class, () -> taskService.getTasksByEmployee(1L));
    }

    @Test
    void testUpdateTask_Return() {
        Task task = new Task(1L, "Test Task", "Test Task Description", Timestamp.from(Instant.now()), new Employee());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).then(returnsFirstArg());

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Test Task");
        updatedTask.setDescription("Updated Test Task Description");
        updatedTask.setDueDate(Timestamp.from(Instant.now()));
        updatedTask.setEmployee(new Employee());
        updatedTask.setStatus(new TaskStatus(TaskStatusEnum.COMPLETED));

        Task result = taskService.updateTaskById(updatedTask, 1L);
        assertEquals(updatedTask.getStatus().getName(), result.getStatus().getName());

        assertDoesNotThrow(() -> taskService.updateTaskById(updatedTask, 1L));
    }

    @Test
    void testUpdateTask_Throw_Employee() {
        Task task = new Task(1L, "Test Task", "Test Task Description", Timestamp.from(Instant.now()), new Employee());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Test Task");
        updatedTask.setDescription("Updated Test Task Description");
        updatedTask.setDueDate(Timestamp.from(Instant.now()));
        updatedTask.setEmployee(null);
        updatedTask.setStatus(new TaskStatus(TaskStatusEnum.COMPLETED));

        assertThrows(IllegalArgumentException.class, () -> taskService.updateTaskById(updatedTask, 1L));
    }

    @Test
    void testDeleteTask_Return() {
        Task task = new Task(1L, "Test Task", "Test Task Description", Timestamp.from(Instant.now()), new Employee());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertDoesNotThrow(() -> taskService.deleteTaskById(1L));
        verify(taskRepository).findById(1L);
    }

    @Test
    void testDeleteTask_Throw() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(1L));
    }
}

package com.cavdar.employeemanagement.unit;

import com.cavdar.employeemanagement.domain.model.Department;
import com.cavdar.employeemanagement.domain.repository.DepartmentRepository;
import com.cavdar.employeemanagement.domain.service.DepartmentService;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.util.exception.DepartmentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceUnitTest {

    @InjectMocks
    DepartmentService departmentService;

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    EmployeeService employeeService;

    @Test
    void testFindDepartment_Return() {
        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(new Department(1L, "Test Department")));

        assertEquals(1L, departmentService.getDepartmentById(1L).getId());
        assertNotNull(departmentService.getDepartmentById(1L));
    }

    @Test
    void testFindDepartment_Throw() {
        when(departmentRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.deleteDepartmentById(100L));
    }

    @Test
    void testSaveDepartment() {
        when(departmentRepository.save(any(Department.class))).then(returnsFirstArg());

        assertDoesNotThrow(() -> departmentService.saveDepartment(new Department(1L, "Test Department")));
    }

    @Test
    void testUpdateDepartment() {
        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(new Department(1L, "Test Department")));
        when(departmentRepository.save(any(Department.class))).then(returnsFirstArg());

        Department updatedDepartment = new Department()
                .setName("Updated Test Department");

        assertEquals(updatedDepartment.getName(), departmentService.updateDepartmentById(updatedDepartment, 1L).getName());
    }

    @Test
    void testDeleteDepartment_NotThrow_Verify() {
        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(new Department(1L, "Test Department")));

        assertDoesNotThrow(() -> departmentService.deleteDepartmentById(1L));
        verify(employeeService).updateDepartmentByDepartment(null, departmentService.getDepartmentById(1L));

    }

    @Test
    void testDeleteDepartment_Throw() {
        when(departmentRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.deleteDepartmentById(100L));
    }
}

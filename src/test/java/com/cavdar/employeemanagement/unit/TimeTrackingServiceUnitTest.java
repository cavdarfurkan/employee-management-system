package com.cavdar.employeemanagement.unit;

import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.TimeTracking;
import com.cavdar.employeemanagement.domain.repository.TimeTrackingRepository;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.domain.service.TimeTrackingService;
import com.cavdar.employeemanagement.util.exception.TimeTrackingAlreadyRunningException;
import com.cavdar.employeemanagement.util.exception.TimeTrackingNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeTrackingServiceUnitTest {

    @InjectMocks
    TimeTrackingService timeTrackingService;

    @Mock
    TimeTrackingRepository timeTrackingRepository;

    @Mock
    EmployeeService employeeService;

    @Test
    void testFindTimeTracking_Return() {
        TimeTracking timeTracking = new TimeTracking();
        timeTracking.setId(1L);
        when(timeTrackingRepository.findById(1L)).thenReturn(Optional.of(timeTracking));

        assertEquals(1L, timeTrackingService.getTimeTrackingById(1L).getId());
    }

    @Test
    void testFindTimeTracking_Throw() {
        when(timeTrackingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TimeTrackingNotFoundException.class, () -> timeTrackingService.getTimeTrackingById(1L));
    }

    @Test
    void testSaveTimeTracking_Return() {
        when(timeTrackingRepository.save(any(TimeTracking.class))).then(returnsFirstArg());

        TimeTracking timeTracking = new TimeTracking();
        timeTracking.setId(1L);
        timeTracking.setEndTime(Timestamp.from(Instant.now().plusSeconds(10)));
        timeTracking.setEmployee(new Employee());

        assertDoesNotThrow(() -> timeTrackingService.saveTimeTracking(timeTracking));

        TimeTracking savedTracking = timeTrackingService.saveTimeTracking(timeTracking);
        assertEquals(1L, savedTracking.getId());
        assertNotNull(savedTracking.getStartTime());
    }

    @Test
    void testSaveTimeTracking_Throw() {
        TimeTracking timeTracking = new TimeTracking();
        timeTracking.setId(1L);
        timeTracking.setEndTime(Timestamp.from(Instant.now().plusSeconds(10)));

        assertThrows(IllegalArgumentException.class, () -> timeTrackingService.saveTimeTracking(timeTracking));
    }

    @Test
    void testDeleteTimeTracking() {
        TimeTracking timeTracking = new TimeTracking();
        timeTracking.setId(1L);
        when(timeTrackingRepository.findById(1L)).thenReturn(Optional.of(timeTracking));

        assertDoesNotThrow(() -> timeTrackingService.deleteTimeTracking(1L));
        verify(timeTrackingRepository).findById(1L);
    }

    @Test
    void testStartTracking_Return() {
        when(employeeService.getEmployeeById(1L)).thenReturn(new Employee());
        when(timeTrackingRepository.findByEmployeeAndEndTime(any(Employee.class), eq(null)))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> timeTrackingService.startTracking(1L));
        verify(employeeService).getEmployeeById(1L);
        verify(timeTrackingRepository).findByEmployeeAndEndTime(any(Employee.class), eq(null));
        verify(timeTrackingRepository).save(any(TimeTracking.class));
    }

    @Test
    void testStartTracking_Throw() {
        when(employeeService.getEmployeeById(1L)).thenReturn(new Employee());
        when(timeTrackingRepository.findByEmployeeAndEndTime(any(Employee.class), eq(null)))
                .thenReturn(Optional.of(new TimeTracking()));

        assertThrows(TimeTrackingAlreadyRunningException.class, () -> timeTrackingService.startTracking(1L));
        verify(employeeService).getEmployeeById(1L);
        verify(timeTrackingRepository).findByEmployeeAndEndTime(any(Employee.class), eq(null));
        verify(timeTrackingRepository, never()).save(any(TimeTracking.class));
    }

    @Test
    void testStopTracking_Return() {
        Employee employee = new Employee();
        employee.setId(1L);

        TimeTracking timeTracking = new TimeTracking();
        timeTracking.setEmployee(new Employee());

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        when(timeTrackingRepository.findByEmployeeAndEndTime(any(Employee.class), eq(null)))
                .thenReturn(Optional.of(timeTracking));

        assertDoesNotThrow(() -> timeTrackingService.stopTracking(1L));
        verify(employeeService).getEmployeeById(1L);
        verify(timeTrackingRepository).findByEmployeeAndEndTime(any(Employee.class), eq(null));
        verify(timeTrackingRepository).save(any(TimeTracking.class));
    }

    @Test
    void testStopTracking_Throw() {
        when(employeeService.getEmployeeById(1L)).thenReturn(new Employee());
        when(timeTrackingRepository.findByEmployeeAndEndTime(any(Employee.class), eq(null)))
                .thenReturn(Optional.empty());

        assertThrows(TimeTrackingNotFoundException.class, () -> timeTrackingService.stopTracking(1L));
        verify(employeeService).getEmployeeById(1L);
        verify(timeTrackingRepository).findByEmployeeAndEndTime(any(Employee.class), eq(null));
        verify(timeTrackingRepository, never()).save(any(TimeTracking.class));
    }

    @Test
    void testFindActiveTracking_Return() {
        when(employeeService.getEmployeeById(1L)).thenReturn(new Employee());
        when(timeTrackingRepository.findByEmployeeAndEndTime(any(Employee.class), eq(null)))
                .thenReturn(Optional.of(new TimeTracking()));

        assertDoesNotThrow(() -> timeTrackingService.getActiveTimeTracking(1L));
        verify(employeeService).getEmployeeById(1L);
        verify(timeTrackingRepository).findByEmployeeAndEndTime(any(Employee.class), eq(null));
    }

    @Test
    void testFindActiveTracking_Throw() {
        when(employeeService.getEmployeeById(1L)).thenReturn(new Employee());
        when(timeTrackingRepository.findByEmployeeAndEndTime(any(Employee.class), eq(null)))
                .thenReturn(Optional.empty());

        assertThrows(TimeTrackingNotFoundException.class, () -> timeTrackingService.getActiveTimeTracking(1L));
        verify(employeeService).getEmployeeById(1L);
        verify(timeTrackingRepository).findByEmployeeAndEndTime(any(Employee.class), eq(null));
    }
}

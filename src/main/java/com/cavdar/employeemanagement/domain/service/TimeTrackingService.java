package com.cavdar.employeemanagement.domain.service;

import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.TimeTracking;
import com.cavdar.employeemanagement.domain.repository.TimeTrackingRepository;
import com.cavdar.employeemanagement.util.exception.TimeTrackingAlreadyRunningException;
import com.cavdar.employeemanagement.util.exception.TimeTrackingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TimeTrackingService {

    private final TimeTrackingRepository timeTrackingRepository;
    private final EmployeeService employeeService;

    @Autowired
    public TimeTrackingService(TimeTrackingRepository timeTrackingRepository, EmployeeService employeeService) {
        this.timeTrackingRepository = timeTrackingRepository;
        this.employeeService = employeeService;
    }

    public TimeTracking getTimeTrackingById(Long id) {
        return this.timeTrackingRepository.findById(id)
                .orElseThrow(() -> new TimeTrackingNotFoundException(id));
    }

    public List<TimeTracking> getAllTimeTrackingByEmployee(Long id) {
        Employee employee = this.employeeService.getEmployeeById(id);
        return this.timeTrackingRepository.findAllByEmployeeAndEndTimeNotNull(employee);
    }

    public TimeTracking saveTimeTracking(TimeTracking timeTracking) {
        if (timeTracking.getEmployee() == null) {
            throw new IllegalArgumentException("Employee argument can not be null");
        }
        if (timeTracking.getStartTime() == null) {
            timeTracking.setStartTime(new Timestamp(System.currentTimeMillis()));
        }

        return this.timeTrackingRepository.save(timeTracking);
    }

    public void deleteTimeTracking(Long id) {
        TimeTracking timeTracking = getTimeTrackingById(id);
        this.timeTrackingRepository.delete(timeTracking);
    }

    private Optional<TimeTracking> getNullEndTimeByEmployee(Employee employee) {
        return this.timeTrackingRepository.findByEmployeeAndEndTime(employee, null);
    }

    public TimeTracking startTracking(Long employeeId) {
        Employee employee = this.employeeService.getEmployeeById(employeeId);

        Optional<TimeTracking> optional = getNullEndTimeByEmployee(employee);
        if (optional.isPresent()) {
            throw new TimeTrackingAlreadyRunningException(optional.get().getId());
        } else {
            TimeTracking timeTracking = new TimeTracking()
                    .setStartTime(Timestamp.from(Instant.now()))
                    .setEndTime(null)
                    .setEmployee(employee);

            return saveTimeTracking(timeTracking);
        }
    }

    public TimeTracking stopTracking(Long employeeId) {
        Employee employee = this.employeeService.getEmployeeById(employeeId);

        Optional<TimeTracking> optional = getNullEndTimeByEmployee(employee);
        if (optional.isEmpty()) {
            throw new TimeTrackingNotFoundException("There is no stoppable time tracker.");
        } else {
            optional.get().setEndTime(Timestamp.from(Instant.now()));
            return saveTimeTracking(optional.get());
        }
    }

    public TimeTracking getActiveTimeTracking(Long employeeId) {
        Employee employee = this.employeeService.getEmployeeById(employeeId);

        Optional<TimeTracking> optional = getNullEndTimeByEmployee(employee);
        if (optional.isEmpty()) {
            throw new TimeTrackingNotFoundException("There is no active time tracker.");
        } else {
            return optional.get();
        }
    }
}

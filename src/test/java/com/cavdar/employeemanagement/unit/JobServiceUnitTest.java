package com.cavdar.employeemanagement.unit;

import com.cavdar.employeemanagement.domain.model.Job;
import com.cavdar.employeemanagement.domain.repository.JobRepository;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.domain.service.JobService;
import com.cavdar.employeemanagement.util.exception.JobNotFoundException;
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
public class JobServiceUnitTest {

    @InjectMocks
    JobService jobService;

    @Mock
    JobRepository jobRepository;

    @Mock
    EmployeeService employeeService;

    @Test
    void testFindJob_ReturnJob() {
        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(new Job(1L, "Test Job", 1, 2)));

        assertEquals(1L, jobService.getJobById(1L).getId());
        assertNotNull(jobService.getJobById(1L));
    }

    @Test
    void testFindJob_Throws() {
        when(jobRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(JobNotFoundException.class, () -> jobService.getJobById(100L));
    }

    @Test
    void testSaveJob() {
        when(jobRepository.save(any(Job.class))).then(returnsFirstArg());

        Job job = new Job()
                .setName("Test Job")
                .setMinSalary(1)
                .setMaxSalary(2);

        assertDoesNotThrow(() -> jobService.saveJob(job));
    }

    @Test
    void testSaveJob_MinSal_MaxSal_Constraint_Throws() {
        Job job1 = new Job(1L, "Test Job1", 2000, 1000);
        Job job2 = new Job(2L, "Test Job2", 1000, 1000);

        assertThrows(IllegalArgumentException.class, () -> jobService.saveJob(job1));
        assertThrows(IllegalArgumentException.class, () -> jobService.saveJob(job2));
    }

    @Test
    void testUpdateJob_Return() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(new Job(1L, "Test Job", 1, 2)));
        when(jobRepository.save(any(Job.class))).then(returnsFirstArg());

        Job updatedJob = new Job()
                .setName("Updated Test Job")
                .setMinSalary(10)
                .setMaxSalary(20);

        assertEquals(updatedJob.getName(), jobService.updateJobById(updatedJob, 1L).getName());
        assertNotNull(jobService.updateJobById(updatedJob, 1L));
    }

    @Test
    void testUpdateJob_Throw() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(new Job(1L, "Test Job", 1, 2)));

        Job updatedJob = new Job()
                .setName("Updated Test Job")
                .setMinSalary(20)
                .setMaxSalary(10);

        assertThrows(IllegalArgumentException.class, () -> jobService.updateJobById(updatedJob, 1L));
    }

    @Test
    void testDeleteJob_Return() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(new Job(1L, "Test Job", 1, 2)));

        assertDoesNotThrow(() -> jobService.deleteJobById(1L));
        verify(employeeService).updateJobByJob(null, jobService.getJobById(1L));
    }

    @Test
    void testDeleteJob_Throw() {
        when(jobRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class, () -> jobService.deleteJobById(100L));
    }
}

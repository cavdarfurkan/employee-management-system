package com.cavdar.employeemanagement.domain.service;

import com.cavdar.employeemanagement.domain.model.Department;
import com.cavdar.employeemanagement.domain.model.Job;
import com.cavdar.employeemanagement.domain.repository.JobRepository;
import com.cavdar.employeemanagement.util.exception.JobNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final JobRepository jobRepository;
    private final EmployeeService employeeService;

    @Autowired
    public JobService(JobRepository jobRepository, EmployeeService employeeService) {
        this.jobRepository = jobRepository;
        this.employeeService = employeeService;
    }

    public List<Job> getJobs() {
        return this.jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
    }

    public Page<Job> pageAndSearchJobs(Pageable pageable, String search) {
        if (search == null) {
            return this.jobRepository.findAll(pageable);
        }

        return this.jobRepository.findByNameContainsIgnoreCase(search, pageable);
    }

    public Job saveJob(Job job) {
        return this.jobRepository.save(job);
    }

    public Job updateJobById(Job updatedJob, Long id) {
        Job job = this.getJobById(id);

        job.setName(updatedJob.getName());
        job.setMinSalary(updatedJob.getMinSalary());
        job.setMaxSalary(updatedJob.getMaxSalary());

        return this.jobRepository.save(job);
    }

    public void deleteJobById(Long id) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));

        this.employeeService.updateJobByJob(null, job);

        this.jobRepository.delete(job);
    }
}

package com.cavdar.employeemanagement.domain.service;

import com.cavdar.employeemanagement.domain.model.*;
import com.cavdar.employeemanagement.domain.repository.AuthorityRepository;
import com.cavdar.employeemanagement.domain.repository.EmployeeRepository;
import com.cavdar.employeemanagement.domain.repository.JobRepository;
import com.cavdar.employeemanagement.util.AlphaNumericConverter;
import com.cavdar.employeemanagement.util.exception.EmployeeNotFoundException;
import com.cavdar.employeemanagement.util.exception.ManagerLoopException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final AuthorityRepository authorityRepository;
    private final UserService userService;
    private final AlphaNumericConverter alphaNumericConverter;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, JobRepository jobRepository, UserService userService, AuthorityRepository authorityRepository, AlphaNumericConverter alphaNumericConverter) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.alphaNumericConverter = alphaNumericConverter;
    }

    public Employee saveEmployee(Employee employee) {
        if (employee.getJob() != null && !isSalaryWithinConstraints(employee)) {
            throw new IllegalArgumentException("Employee salary is not within the constraints specified in the job table.");
        }

        if (employee.getJob() == null) {
            employee.setSalary(0);
        }

        if (employee.getManager() != null && Objects.equals(employee.getManager().getId(), employee.getId())) {
            throw new ManagerLoopException(employee);
        }

        if (employee.getManager() != null) {
            Employee nextManager = employee.getManager();
            while (nextManager.getManager() != null) {
                if (Objects.equals(nextManager.getManager().getId(), employee.getId())) {
                    throw new ManagerLoopException(employee);
                }
                nextManager = nextManager.getManager();
            }
        }
        return this.employeeRepository.save(employee);
    }

    public Employee saveEmployeeAndUser(Employee employee) {
        Employee savedEmployee = saveEmployee(employee);

        User generatedUser = generateUser(savedEmployee);
        savedEmployee.setUser(generatedUser);
        this.employeeRepository.updateUserById(savedEmployee.getUser(), savedEmployee.getId());

        return savedEmployee;
    }

    public Employee updateEmployeeById(Employee updatedEmployee, Long id) {
        Employee employee = this.getEmployeeById(id);

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setGender(updatedEmployee.getGender());
        employee.setDateOfBirth(updatedEmployee.getDateOfBirth());
        employee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        employee.setAddress(updatedEmployee.getAddress());
        employee.setHireDate(updatedEmployee.getHireDate());
        employee.setManager(updatedEmployee.getManager());

        User updatedUser = this.userService.updateUserById(updatedEmployee.getUser(), employee.getUser().getId());
        employee.setUser(updatedUser);

        employee.setDepartment(updatedEmployee.getDepartment());
        employee.setJob(updatedEmployee.getJob());
        employee.setSalary(updatedEmployee.getSalary());

        return this.saveEmployee(employee);
    }

    public void updateDepartmentByDepartment(Department newDepartment, Department oldDepartment) {
        this.employeeRepository.updateDepartmentByDepartment(newDepartment, oldDepartment);
    }

    public void updateJobByJob(Job newJob, Job oldJob) {
        this.employeeRepository.updateJobByJob(newJob, oldJob);
    }

    public List<Employee> getEmployees() {
        return this.employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return this.employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public List<Employee> getEmployeesByManagerId(Long id) {
        return employeeRepository.findAllByManagerId(id);
    }

    public List<Employee> getAllManagers() {
        return this.employeeRepository.findByJob_NameIgnoreCase("manager");
    }

    public List<Employee> getAllManagersExcept(Long id) {
        return getAllManagers().stream()
                // Filter the manager from the managers
                .filter((manager) -> !Objects.equals(manager.getId(), id))
                // Filter the subordinate managers of the manager
                .filter((manager) -> {
                    if (manager.getManager() != null) {
                        Employee nextManager = manager.getManager();
                        while (nextManager != null) {
                            if (Objects.equals(nextManager.getId(), id)) {
                                return false;
                            }
                            try {
                                nextManager = nextManager.getManager();
                            } catch (NullPointerException ignored) {
                                break;
                            }
                        }
                        return true;
                    }
                    return true;
                })
                .toList();
    }

    public Employee getEmployeeByUser(User user) {
        Optional<Employee> optional = this.employeeRepository.findByUser(user);
        if (optional.isEmpty()) {
            throw new EmployeeNotFoundException(user);
        } else {
            return optional.get();
        }
    }

    public Employee getEmployeeByUserId(Long userId) {
        return getEmployeeByUser(this.userService.getUserById(userId));
    }

    public Employee getEmployeeByUsername(String username) {
        return getEmployeeByUser(this.userService.getUserByUsername(username));
    }

    public void deleteEmployeeById(Long id) {
        Employee employee = getEmployeeById(id);

        this.employeeRepository.updateManagerByManager(null, employee);

        this.employeeRepository.delete(employee);
    }

    public Page<Employee> pageAndSearchEmployees(Pageable pageable, String search) {
        if (search == null) {
            return this.employeeRepository.findAll(pageable);
        }

        return this.employeeRepository
                .findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCaseOrJob_NameContainsIgnoreCase(
                        search, search, search, pageable
                );
    }

    public long employeesCount() {
        return this.employeeRepository.count();
    }

    public long employeesCountOfManager(Long id) {
        Employee manager = getEmployeeById(id);
        return this.employeeRepository.countByManager_Id(id);
    }

    private boolean isSalaryWithinConstraints(Employee employee) {
        if (employee.getSalary() < employee.getJob().getMinSalary() ||
                employee.getSalary() > employee.getJob().getMaxSalary()) {
            return false;
        }

        return true;
    }

    private User generateUser(Employee employee) {
        String username = alphaNumericConverter.convert(employee.getFirstName().toLowerCase() + employee.getLastName().toLowerCase() + employee.getId());
        String password = alphaNumericConverter.convert(employee.getFirstName().toLowerCase() + employee.getDateOfBirth().getYear());

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Authority roleEmployee = this.authorityRepository.findByAuthority("ROLE_EMPLOYEE");
        if (employee.getJob().getName().equals("Manager")) {
            Authority roleManager = this.authorityRepository.findByAuthority("ROLE_MANAGER");
            user.setAuthorities(List.of(roleManager, roleEmployee));
        } else {
            user.setAuthorities(List.of(roleEmployee));
        }

        return this.userService.saveUserAndEncryptPassword(user);
    }
}

package com.cavdar.employeemanagement.util;

import com.cavdar.employeemanagement.domain.enums.Gender;
import com.cavdar.employeemanagement.domain.enums.TaskStatusEnum;
import com.cavdar.employeemanagement.domain.model.*;
import com.cavdar.employeemanagement.domain.repository.AuthorityRepository;
import com.cavdar.employeemanagement.domain.repository.DepartmentRepository;
import com.cavdar.employeemanagement.domain.repository.JobRepository;
import com.cavdar.employeemanagement.domain.repository.TaskStatusRepository;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.domain.service.TaskService;
import com.cavdar.employeemanagement.domain.service.TimeTrackingService;
import com.cavdar.employeemanagement.domain.service.UserService;
import com.cavdar.employeemanagement.util.exception.ManagerLoopException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TimeTrackingService timeTrackingService;

    @Override
    public void run(String... args) throws Exception {
        int employeeCount = 150;

        addTaskStatuses();
        addAuthorities();
        addAdmin();
        addDepartments();
        addJobs();
        addEmployees();
        generateSampleEmployees(employeeCount);
        generateSampleTasks(employeeCount * 5);
        generateSampleTimeTrackings(employeeCount * 10);
    }

    private void addTaskStatuses() {
        TaskStatus pendingStatus = new TaskStatus(TaskStatusEnum.PENDING);
        TaskStatus inProgressStatus = new TaskStatus(TaskStatusEnum.IN_PROGRESS);
        TaskStatus completedStatus = new TaskStatus(TaskStatusEnum.COMPLETED);
        TaskStatus canceledStatus = new TaskStatus(TaskStatusEnum.CANCELED);

        this.taskStatusRepository.saveAll(List.of(pendingStatus, inProgressStatus, completedStatus, canceledStatus));
    }

    private void addAuthorities() {
        Authority adminAuthority = new Authority("ROLE_ADMIN");
        Authority managerAuthority = new Authority("ROLE_MANAGER");
        Authority employeeAuthority = new Authority("ROLE_EMPLOYEE");

        this.authorityRepository.saveAll(List.of(adminAuthority, managerAuthority, employeeAuthority));
    }

    private void addAdmin() {
        Authority adminAuthority = this.authorityRepository.findByAuthority("ROLE_ADMIN");

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@admin.com");
        admin.setPassword("123123123");
        admin.setAuthorities(List.of(adminAuthority));

        this.userService.saveUserAndEncryptPassword(admin);
    }

    private void addDepartments() {
        List<Department> deps = new ArrayList<>();

        deps.add(new Department().setName("IT Department"));
        deps.add(new Department().setName("UX/UI Department"));
        deps.add(new Department().setName("Marketing Department"));
        deps.add(new Department().setName("HR Department"));
        deps.add(new Department().setName("Business Development Department"));

        this.departmentRepository.saveAll(deps);
    }

    private void addJobs() {
        List<Job> jobs = new ArrayList<>();

        jobs.add(new Job().setName("Developer").setMinSalary(4000).setMaxSalary(15000));
        jobs.add(new Job().setName("Manager").setMinSalary(5000).setMaxSalary(20000));
        jobs.add(new Job().setName("Salesperson").setMinSalary(4000).setMaxSalary(13000));
        jobs.add(new Job().setName("Human Resources").setMinSalary(3500).setMaxSalary(10000));

        this.jobRepository.saveAll(jobs);
    }

    private void addEmployees() {
        Employee emp1 = new Employee();
        emp1.setFirstName("John");
        emp1.setLastName("Doe");
        emp1.setGender(Gender.MALE);
        emp1.setDateOfBirth(LocalDate.of(1991, 12, 25));
        emp1.setPhoneNumber("+48123123123");
        emp1.setAddress("Warsaw/Poland");
        emp1.setHireDate(LocalDate.of(2022, 3, 6));
        this.departmentRepository.findById(1L).ifPresent(emp1::setDepartment);
        this.jobRepository.findById(2L).ifPresent(emp1::setJob);
        emp1.setSalary(6000);

        Employee emp2 = new Employee();
        emp2.setFirstName("Furkan");
        emp2.setLastName("Çavdar");
        emp2.setGender(Gender.MALE);
        emp2.setDateOfBirth(LocalDate.of(1998, 2, 2));
        emp2.setPhoneNumber("+905312337098");
        emp2.setAddress("Warsaw/Poland");
        emp2.setHireDate(LocalDate.of(2023, 7, 15));
        this.departmentRepository.findById(1L).ifPresent(emp2::setDepartment);
        this.jobRepository.findById(1L).ifPresent(emp2::setJob);
        emp2.setSalary(5000);
        emp2.setManager(emp1);

        this.employeeService.saveEmployeeAndUser(emp1);
        this.employeeService.saveEmployeeAndUser(emp2);
    }

    private void generateSampleEmployees(int numberOfEmployees) {
        List<String> firstNames = List.of("Dariana", "Charlie", "Gavin", "Joey", "Toby", "Jaycie", "Abagail", "Emory", "Jillian", "Campbell", "Jarvis", "Kaia", "Hana", "Mylah", "Colston", "Haven", "Guillermo", "Chana", "Lyam", "Makai", "Gage", "Brissa", "Gustavo", "Zamir", "Emerson", "Vince", "Jaxen", "Jaycion", "Everett", "Zaylee");
        List<String> lastNames = List.of("Newton", "Masters", "Jacob", "Leary", "Esparza", "Meredith", "Starr", "Lacy", "Cornett", "Plummer", "Lowery", "Nava", "Fischer", "Brantley", "Giles", "Reeves", "Horne", "McKinley", "Hagen", "Nash", "Bernard", "Barber", "Caudill", "Hinkle", "Rutledge", "Newell", "Link", "Franks", "Huber", "Thorne");
        List<String> phonePrefixes = List.of("+48", "+90", "+1", "+44", "+49");
        List<String> streets = List.of("Oak Street", "Maple Avenue", "Cedar Lane", "Pine Road", "Elm Boulevard", "Willow Court", "Birch Drive", "Hickory Lane", "Juniper Street", "Sycamore Way", "Cypress Circle", "Poplar Lane", "Magnolia Drive", "Spruce Avenue", "Ash Street", "Cherry Lane", "Dogwood Drive", "Redwood Road", "Beech Street", "Alder Court", "Walnut Boulevard", "Chestnut Lane", "Holly Drive", "Mulberry Street", "Fir Avenue", "Locust Lane", "Linden Road", "Acacia Circle", "Hemlock Court", "Basswood Drive");
        List<String> cities = List.of("Lonejack", "Springfield", "Kansas City", "St. Louis", "Jefferson City", "Columbia", "Independence", "Lee's Summit", "O'Fallon", "St. Joseph", "St. Charles", "Joplin", "Blue Springs", "Florissant", "Chesterfield", "Cape Girardeau", "Wildwood", "University City", "Wentzville", "Liberty", "Ballwin", "Arnold", "Maryland Heights", "Nixa", "Raymore", "Ferguson", "Webster Groves", "Sedalia", "Creve Coeur", "Clayton");
        List<String> states = List.of("MO", "KS", "IL", "AR", "TN", "IA", "NE", "OK", "KY", "IN", "OH", "WI", "MN", "MI", "ND", "SD", "LA", "MS", "AL", "GA", "FL", "SC", "NC", "VA", "WV", "MD", "DE", "PA", "NJ", "NY", "CT", "RI", "MA", "NH", "VT", "ME");

        for (int i = 0; i < numberOfEmployees; i++) {
            Employee emp = new Employee();
            emp.setFirstName(randomElement(firstNames));
            emp.setLastName(randomElement(lastNames));
            emp.setGender(randomEnum(Gender.class));
            emp.setDateOfBirth(randomDate(1970, 2003));
            emp.setPhoneNumber(randomElement(phonePrefixes) + randomNumericString(9));
            emp.setAddress(String.format("%d %s %s, %s %d", randomInt(1000, 10000), randomElement(streets), randomElement(cities), randomElement(states), randomInt(10000, 99999)));
            emp.setHireDate(randomDate(2005, 2023));
            emp.setDepartment(randomElement(departmentRepository.findAll()));
            emp.setJob(randomElement(jobRepository.findAll()));
            emp.setSalary(randomDouble(emp.getJob().getMinSalary(), emp.getJob().getMaxSalary()));

            try {
                List<Employee> managers = employeeService.getAllManagers();
                managers.add(null);
                emp.setManager(randomElement(managers));
            } catch (ManagerLoopException ignored) {
                emp.setManager(null);
            }

            employeeService.saveEmployeeAndUser(emp);
        }
    }

    private void generateSampleTasks(int numberOfTasks) {
        List<String> titles = List.of("Lorem ipsum dolor sit amet", "Maecenas sed diam", "Nulla vitae elit libero", "Etiam porta sem malesuada", "Donec sed odio dui", "Blandit sit amet non magna", "A pharetra augue", "Sit amet non magna", "Consectetur adipiscing elit", "Risus varius blandit");
        List<String> descriptions = List.of("Cras mattis consectetur purus sit amet fermentum.", "Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.", "Aenean lacinia bibendum nulla sed consectetur.", "Curabitur blandit tempus porttitor.", "Nullam quis risus eget urna mollis ornare vel eu leo.", "Cras mattis consectetur purus sit amet fermentum.", "Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.", "Aenean lacinia bibendum nulla sed consectetur.", "Curabitur blandit tempus porttitor.", "Nullam quis risus eget urna mollis ornare vel eu leo.");
        List<Employee> employees = employeeService.getEmployees();
        List<TaskStatus> taskStatuses = taskStatusRepository.findAll();
        taskStatuses.add(null);

        for (int i = 0; i < numberOfTasks; i++) {
            Task task = new Task();
            task.setTitle(randomElement(titles));
            task.setDescription(randomElement(descriptions));
            task.setEmployee(randomElement(employees));

            TaskStatus status = randomElement(taskStatuses);
            if (status != null) {
                task.setStatus(status);
                task.setAssignmentDate(randomTimestamp(LocalDateTime.of(2022, 1, 1, 0, 0, 0), LocalDateTime.now()));
            }

            task.setDueDate(randomTimestamp(LocalDateTime.now(), LocalDateTime.of(2024, 12, 28, 23, 59, 59)));

            taskService.saveTask(task);
        }
    }

    private void generateSampleTimeTrackings(int numberOfTimeTrackings) {
        List<Employee> employees = employeeService.getEmployees();

        for (int i = 0; i < numberOfTimeTrackings; i++) {
            TimeTracking timeTracking = new TimeTracking();
            timeTracking.setEmployee(randomElement(employees));

            LocalDateTime dateTime = randomDateTime(2022, 2023);
            timeTracking.setStartTime(randomTimestamp(dateTime.minusHours(4), dateTime));
            timeTracking.setEndTime(randomTimestamp(dateTime, dateTime.plusHours(4)));

            timeTrackingService.saveTimeTracking(timeTracking);
        }
    }

    private <T> T randomElement(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    private <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        Random random = new Random();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private LocalDate randomDate(int minYear, int maxYear) {
        int year = randomInt(minYear, maxYear);
        int month = randomInt(1, 12);
        int day = randomInt(1, 28);
        return LocalDate.of(year, month, day);
    }

    private LocalDateTime randomDateTime(int minYear, int maxYear) {
        LocalDate date = randomDate(minYear, maxYear);
        int hour = randomInt(8, 16);
        int minute = randomInt(0, 59);
        int second = randomInt(0, 59);
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), hour, minute, second);
    }

    private Timestamp randomTimestamp(LocalDateTime start, LocalDateTime end) {
        LocalDateTime timestamp = LocalDateTime.of(
                randomInt(start.getYear(), end.getYear()),
                randomInt(start.getMonth().getValue(), end.getMonth().getValue()),
                randomInt(start.getDayOfMonth(), end.getDayOfMonth()),
                randomInt(start.getHour(), end.getHour()),
                randomInt(start.getMinute(), end.getMinute()),
                randomInt(start.getSecond(), end.getSecond())
        );
        return Timestamp.valueOf(timestamp);
    }

    private int randomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private double randomDouble(double min, double max) {
        return new Random().nextDouble(max - min + 1) + min;
    }

    private String randomNumericString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(randomInt(0, 9));
        }
        return stringBuilder.toString();
    }
}

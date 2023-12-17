package com.exam.sirma.teamemployees.config; // Use your desired package name

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.service.EmployeeService;
import com.exam.sirma.teamemployees.util.CSVReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitialization {

    private final EmployeeService employeeService;

    public DataInitialization(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            List<Employee> allEmployees = CSVReader.read("src/main/resources/datafile.csv");
            employeeService.saveAllEmployees(allEmployees);
        };
    }
}

package com.exam.sirma.teamemployees.service;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void saveAllEmployees(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }
}

package com.exam.sirma.teamemployees.service;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.repository.EmployeeRepository;
import com.exam.sirma.teamemployees.repository.ProjectParticipationRepository;
import com.exam.sirma.teamemployees.util.StringConstant;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.exam.sirma.teamemployees.util.StringConstant.*;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void saveAllEmployees(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        try {
            Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format(StringConstant.EMPLOYEE_NOT_FOUND, id)));
            return employee;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public Employee saveEmployee(Employee employee) {
        try {
            employeeRepository.save(employee);
            return employee;
        } catch (Exception e) {
            log.error(FAILED_TO_SAVE_EMPLOYEE + e.getMessage(), e);
            return null;
        }
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}

package com.exam.sirma.teamemployees.service;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.repository.EmployeeRepository;
import com.exam.sirma.teamemployees.repository.ProjectParticipationRepository;
import com.exam.sirma.teamemployees.util.StringConstant;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ProjectParticipationRepository participationRepository;


    public EmployeeService(EmployeeRepository employeeRepository, ProjectParticipationRepository participationRepository) {
        this.employeeRepository = employeeRepository;
        this.participationRepository = participationRepository;
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
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    @Transactional
    public void saveProjectParticipation(ProjectParticipation participation) {
        participationRepository.save(participation);
    }
}

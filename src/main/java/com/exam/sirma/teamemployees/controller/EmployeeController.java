package com.exam.sirma.teamemployees.controller;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.service.EmployeeService;
import com.exam.sirma.teamemployees.service.ProjectParticipationService;
import com.exam.sirma.teamemployees.util.CalculationUtil;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ProjectParticipationService participationService;

    public EmployeeController(EmployeeService employeeService, ProjectParticipationService participationService) {
        this.employeeService = employeeService;
        this.participationService = participationService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception validationException) {
            log.error("Validation error: {}", validationException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/print-result")
    public ResponseEntity<String> printResult() {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        String result = CalculationUtil.identifyLongestWorkingPair(allEmployees);
        // set System.lineSeparator() to <br> so i can get line breaks in html
        result = result.replace(System.lineSeparator(), "<br>");
        return ResponseEntity.ok(result);
    }
}

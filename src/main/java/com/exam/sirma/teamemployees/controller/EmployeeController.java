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

import static com.exam.sirma.teamemployees.util.StringConstant.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService, ProjectParticipationService participationService) {
        this.employeeService = employeeService;
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
            if (employee != null) {
                return new ResponseEntity<>(employee, HttpStatus.OK);
            }
            throw  new NoSuchElementException(String.format(EMPLOYEE_NOT_FOUND, id));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception validationException) {
            log.error(VALIDATION_ERROR, validationException.getMessage());
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
        if (allEmployees == null || allEmployees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_FETCHING_EMPLOYEES);
        }
        String result = CalculationUtil.identifyLongestWorkingPair(allEmployees);
        // set System.lineSeparator() to <br> so i can get line breaks in html
        result = result.replace(System.lineSeparator(), "<br>");
        return ResponseEntity.ok(result);
    }
}

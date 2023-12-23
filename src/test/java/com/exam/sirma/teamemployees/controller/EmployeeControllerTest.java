package com.exam.sirma.teamemployees.controller;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.service.EmployeeService;
import com.exam.sirma.teamemployees.util.CalculationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;


    // initialize annotated fields in the test class for Mockito mocks
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees() {
        List<Employee> expectedEmployees = List.of(new Employee(), new Employee());
        when(employeeService.getAllEmployees()).thenReturn(expectedEmployees);

        ResponseEntity<List<Employee>> result = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedEmployees, result.getBody());
    }

    @Test
    void getEmployeeWithExistingIdReturnsEmployee() {
        Long existingId = 1L;
        Employee expectedEmployee = new Employee();
        when(employeeService.getEmployeeById(existingId)).thenReturn(expectedEmployee);

        ResponseEntity<Employee> result = employeeController.getEmployeeById(existingId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedEmployee, result.getBody());
    }

    @Test
    void getEmployeeWIthNonExistingIdReturnsNotFound() {
        Long nonExistingId = 99L;
        when(employeeService.getEmployeeById(nonExistingId)).thenReturn(null);

        ResponseEntity<Employee> result = employeeController.getEmployeeById(nonExistingId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void saveValidEmployeeReturnsCreated() {
        Employee employeeToSave = new Employee();
        when(employeeService.saveEmployee(employeeToSave)).thenReturn(employeeToSave);

        ResponseEntity<Employee> result = employeeController.saveEmployee(employeeToSave);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(employeeToSave, result.getBody());
    }

    @Test
    void saveInvalidEmployeeReturnsBadRequest() {
        Employee invalidEmployee = new Employee();
        when(employeeService.saveEmployee(invalidEmployee)).thenThrow(new RuntimeException("Validation Error"));

        ResponseEntity<Employee> result = employeeController.saveEmployee(invalidEmployee);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void deleteEmployee() {
        Long idToDelete = 1L;

        ResponseEntity<Void> result = employeeController.deleteEmployee(idToDelete);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(employeeService).deleteEmployee(idToDelete);
    }

    @Test
    void printResultWithEmployeesReturnsOkWithResult() {
        // Mock the static method
        mockStatic(CalculationUtil.class);

        Employee employee1 = new Employee(1L);
        employee1.setProjectParticipation(new HashMap<>());

        Employee employee2 = new Employee(2L);
        employee2.setProjectParticipation(new HashMap<>());

        employee1.getProjectParticipation().put(1, new ProjectParticipation());
        employee2.getProjectParticipation().put(1, new ProjectParticipation());

        List<Employee> employees = List.of(employee1, employee2);

        when(employeeService.getAllEmployees()).thenReturn(employees);
        when(CalculationUtil.identifyLongestWorkingPair(employees)).thenReturn("Result");

        ResponseEntity<String> result = employeeController.printResult();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Result", result.getBody());
    }

    @Test
    void printResultWithEmptyEmployeesReturnsInternalServerError() {
        when(employeeService.getAllEmployees()).thenReturn(List.of());

        ResponseEntity<String> result = employeeController.printResult();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Error fetching employees", result.getBody());
    }
}


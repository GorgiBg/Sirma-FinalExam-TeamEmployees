package com.exam.sirma.teamemployees.service;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.repository.EmployeeRepository;
import com.exam.sirma.teamemployees.repository.ProjectParticipationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectParticipationRepository participationRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void saveAllEmployees() {
        List<Employee> employees = List.of(new Employee(), new Employee());
        employeeService.saveAllEmployees(employees);
        verify(employeeRepository).saveAll(employees);
    }

    @Test
    void getAllEmployees() {
        List<Employee> expectedEmployees = List.of(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);
        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(expectedEmployees, result);
    }

    @Test
    void getEmployeeWithExistingIdReturnsEmployee() {
        Long existingId = 1L;
        Employee expectedEmployee = new Employee();
        when(employeeRepository.findById(existingId)).thenReturn(Optional.of(expectedEmployee));
        Employee result = employeeService.getEmployeeById(existingId);
        assertEquals(expectedEmployee, result);
    }

    @Test
    void getEmployeeWithNonExistingIdReturnsNull() {
        Long nonExistingId = 99L;
        when(employeeRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Employee result = employeeService.getEmployeeById(nonExistingId);
        assertNull(result);
    }

    @Test
    void saveEmployee() {
        Employee employeeToSave = new Employee(1L);
        Map<Integer, ProjectParticipation> projectParticipation = employeeToSave.getProjectParticipation();
        projectParticipation.put(1, new ProjectParticipation(1, employeeToSave));

        assertDoesNotThrow(() -> {
            Employee savedEmployee = employeeService.saveEmployee(employeeToSave);
            assertNotNull(savedEmployee);
            verify(employeeRepository).save(employeeToSave);
        });
    }

    @Test
    void deleteEmployee() {
        Long idToDelete = 1L;
        employeeService.deleteEmployee(idToDelete);
        verify(employeeRepository).deleteById(idToDelete);
    }
}

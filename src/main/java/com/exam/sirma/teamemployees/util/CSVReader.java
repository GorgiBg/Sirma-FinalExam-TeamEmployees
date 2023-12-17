package com.exam.sirma.teamemployees.util;

import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.service.EmployeeService;
import com.exam.sirma.teamemployees.service.ProjectParticipationService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CSVReader {

    public static List<Employee> read(String filepath, EmployeeService employeeService, ProjectParticipationService participationService) {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Validate the number of values
                if (values.length == 4) {
                    try {
                        Long empId = Long.parseLong(values[0].trim());
                        int projectNumber = Integer.parseInt(values[1].trim());
                        LocalDate dateFrom = parseDate(values[2].trim());
                        LocalDate dateTo = parseDate(values[3].trim());

                        // Check if an Employee with the same ID already exists
                        Optional<Employee> existingEmployee = employees.stream()
                            .filter(e -> e.getEmpId().equals(empId))
                            .findFirst();

                        // Create or get the existing Employee
                        Employee employee = getEmployee(existingEmployee, empId, employees);

                        // Create ProjectParticipation object to store the data of Employee involvement in project with number(projectNumber)
                        ProjectParticipation participation = createProjectParticipation(projectNumber, dateFrom,
                            dateTo, employee);
                        employeeService.saveEmployee(employee);
                        participationService.saveProjectParticipation(participation);
                        // Set the relationship between Employee and Participation

                        employee.getProjectParticipation().put(projectNumber, participation);
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.out.println("Error parsing data: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid data in line: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // TODO - (to remove later) print Employees for testing purposes
        System.out.println(employees);
        return employees;
    }

    private static ProjectParticipation createProjectParticipation(int projectNumber, LocalDate dateFrom, LocalDate dateTo, Employee employee) {
        ProjectParticipation participation = new ProjectParticipation();
        participation.setProjectNumber(projectNumber);
        participation.setDateFrom(dateFrom);
        participation.setDateTo(dateTo);
        participation.setEmployee(employee);
        return participation;
    }

    private static Employee getEmployee(Optional<Employee> existingEmployee, Long empId, List<Employee> employees) {
        Employee employee = existingEmployee.orElseGet(() -> {
            Employee newEmployee = new Employee();
            newEmployee.setEmpId(empId);
            newEmployee.setProjectParticipation(new HashMap<>());
            employees.add(newEmployee);
            return newEmployee;
        });
        return employee;
    }


    private static LocalDate parseDate(String dateString) {
        // if we don`t have dateTo we add LocalDate.now()
        if (dateString.equalsIgnoreCase("NULL")) {
            return LocalDate.now();
        }

        // Define multiple date formats to try
        String[] possibleFormats = {
            "yyyy-MM-dd",
            "MM/dd/yyyy",
            "yyyyMMdd",
            "dd/MM/yyyy",
            "yyyy/MM/dd",
            "dd-MMM-yyyy"
        };

        for (String format : possibleFormats) {
            try {
                // By specifying Locale.ENGLISH is ensured that the month names are interpreted correctly
                DateTimeFormatter dateFormat;
                if (format.equals("dd-MMM-yyyy")) {
                    dateFormat = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
                } else {
                    dateFormat = DateTimeFormatter.ofPattern(format);
                }
                return LocalDate.parse(dateString, dateFormat);
            } catch (DateTimeParseException e) {
                // Try the next format
            }
        }

        // If none of the formats match, return LocalDate.now() or throw an exception
        return LocalDate.now();
    }
}


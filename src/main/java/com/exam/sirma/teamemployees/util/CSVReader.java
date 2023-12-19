package com.exam.sirma.teamemployees.util;

import com.exam.sirma.teamemployees.entity.DateRange;
import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.enumeration.DateString;
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

    public static List<Employee> read(String filepath, EmployeeService employeeService,
                                      ProjectParticipationService participationService) {
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
                        LocalDate dateFrom = parseDate(values[2].trim(), DateString.DATE_FROM);
                        LocalDate dateTo = parseDate(values[3].trim(), DateString.DATE_TO);

                        // Check if an Employee with the same ID already exists
                        Optional<Employee> existingEmployee = employees.stream()
                            .filter(e -> e.getEmpId().equals(empId))
                            .findFirst();

                        // Create or get the existing Employee
                        Employee employee = getEmployee(existingEmployee, empId, employees);

                        // Create or get the existing ProjectParticipation for the same project number
                        ProjectParticipation participation = getOrCreateProjectParticipation(employee, projectNumber);

                        // Add a new DateRange to the list
                        DateRange dateRange = new DateRange(dateFrom, dateTo);
                        participation.getDateRangesOnProject().add(dateRange);

                        // Save the DateRange, ProjectParticipation, and Employee
                        participationService.saveProjectParticipation(participation);
                        employeeService.saveEmployee(employee);

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

    private static ProjectParticipation getOrCreateProjectParticipation(Employee employee, int projectNumber) {
        return employee.getProjectParticipation().computeIfAbsent(projectNumber, e -> {
            ProjectParticipation newParticipation = new ProjectParticipation();
            newParticipation.setProjectNumber(projectNumber);
            return newParticipation;
        });
    }

    private static Employee getEmployee(Optional<Employee> existingEmployee, Long empId, List<Employee> employees) {
        return existingEmployee.orElseGet(() -> {
            Employee newEmployee = new Employee();
            newEmployee.setEmpId(empId);
            newEmployee.setProjectParticipation(new HashMap<>());
            employees.add(newEmployee);
            return newEmployee;
        });
    }

    private static LocalDate parseDate(String dateString, DateString dateEnum) {
        // if we don`t have dateTo we add LocalDate.now()
        if (dateString.equalsIgnoreCase("NULL") && dateEnum.equals(DateString.DATE_TO)) {
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

        throw new DateTimeParseException("Unable to parse date: " + dateString, dateString, 0);
    }
}


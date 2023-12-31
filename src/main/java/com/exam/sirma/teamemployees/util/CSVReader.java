package com.exam.sirma.teamemployees.util;

import com.exam.sirma.teamemployees.entity.DateRange;
import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.enumeration.DateString;
import com.exam.sirma.teamemployees.service.EmployeeService;
import com.exam.sirma.teamemployees.service.ProjectParticipationService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.exam.sirma.teamemployees.util.StringConstant.*;

@Slf4j
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
                        employeeService.saveEmployee(employee);
                        // Create or get the existing ProjectParticipation for the same project number
                        ProjectParticipation participation = getOrCreateProjectParticipation(employee, projectNumber);
                        participationService.saveProjectParticipation(participation);
                        // Add a new DateRange to the list
                        DateRange dateRange = new DateRange(dateFrom, dateTo, participation);
                        boolean isOverlapped = checkForOverlap(participation.getDateRangesOnProject(), dateRange);
                        if (isOverlapped) {
                            throw new IllegalArgumentException(String.format(OVERLAP_PERIODS, empId, projectNumber,
                                dateRange.getStartDate(), dateRange.getEndDate()));
                        } else {
                            participation.getDateRangesOnProject().add(dateRange);
                        }

                    } catch (DateTimeParseException | IllegalArgumentException e) {
                        System.out.println(ERROR_DATA + e.getMessage());
                    }
                } else {
                    log.error(INVALID_LINE + line);
                }
            }

        } catch (IOException e) {
            log.error(ERROR_FILE + e.getMessage());
        }
        return employees;
    }

    // check if we have common days between current DateRange and existing ones
    private static boolean checkForOverlap(List<DateRange> dateRangesOnProject, DateRange newDateRange) {
        LocalDate newStartDate = newDateRange.getStartDate();
        LocalDate newEndDate = newDateRange.getEndDate();

        for (DateRange existingDateRange : dateRangesOnProject) {
            LocalDate existingStartDate = existingDateRange.getStartDate();
            LocalDate existingEndDate = existingDateRange.getEndDate();

            // Check for non-overlap conditions
            if (newEndDate.isBefore(existingStartDate) || newStartDate.isAfter(existingEndDate)) {
                continue;
            }

            return true;
        }

        return false;
    }

    private static ProjectParticipation getOrCreateProjectParticipation(Employee employee, int projectNumber) {
        return employee.getProjectParticipation().computeIfAbsent(projectNumber, e -> {
            ProjectParticipation newParticipation = new ProjectParticipation();
            newParticipation.setProjectNumber(projectNumber);
            newParticipation.setEmployee(employee);
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
        if (dateString.equalsIgnoreCase(NULL) && dateEnum.equals(DateString.DATE_TO)) {
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

        throw new DateTimeParseException(UNABLE_PARSE_DATE + dateString, dateString, 0);
    }
}


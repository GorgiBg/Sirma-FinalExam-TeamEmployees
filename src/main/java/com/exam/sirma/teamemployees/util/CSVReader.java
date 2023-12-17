package com.exam.sirma.teamemployees.util;

import com.exam.sirma.teamemployees.entity.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CSVReader {

    public static List<Employee> read(String filepath) {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Validate the number of values
                if (values.length == 4) {
                    try {
                        Long empId = Long.parseLong(values[0].trim());
                        int projectId = Integer.parseInt(values[1].trim());
                        LocalDate dateFrom = parseDate(values[2].trim());
                        LocalDate dateTo = parseDate(values[3].trim());

                        // Create and add Employee object
                        employees.add(new Employee(empId, projectId, dateFrom, dateTo));
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    throw new IllegalArgumentException("Invalid data in line: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //TODO - (to remove later) print Employees for testing purposes
        System.out.println(employees);
        return employees;
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


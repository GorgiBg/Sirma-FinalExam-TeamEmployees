package com.exam.sirma.teamemployees.util;

import com.exam.sirma.teamemployees.entity.DateRange;
import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.exam.sirma.teamemployees.util.StringConstant.*;

@Component
public class CalculationUtil {
    public static String identifyLongestWorkingPair(List<Employee> employees) {
        StringBuilder result = new StringBuilder();
        long longestDuration = 0;
        Employee employee1WithLongestDuration = null;
        Employee employee2WithLongestDuration = null;

        if (employees == null || employees.size() < 2) {
            result.append(LESS_THAN_TWO_EMPLOYEES);
            return result.toString();
        }

        // pick any two employees and check for common projects duration
        for (int i = 0; i < employees.size(); i++) {
            Employee employee1 = employees.get(i);

            if (isNotValidEmployeeForComparison(employee1)) {
                continue;
            }

            for (int j = i + 1; j < employees.size(); j++) {
                Employee employee2 = employees.get(j);

                if (isNotValidEmployeeForComparison(employee2)) {
                    continue;
                }

                long duration = calculateTotalDuration(employee1, employee2);

                if (duration > longestDuration) {
                    longestDuration = duration;
                    employee1WithLongestDuration = employee1;
                    employee2WithLongestDuration = employee2;
                }
            }
        }

        if (employee1WithLongestDuration != null && employee2WithLongestDuration != null) {
            result.append(String.format(LONGEST_WORKING_PAIR, employee1WithLongestDuration.getEmpId(),
                employee2WithLongestDuration.getEmpId())).append(System.lineSeparator());
            result.append(String.format(TOTAL_DURATION, longestDuration)).append(System.lineSeparator());
        } else {
            result.append(NO_EMPLOYEES_TO_COMPARE);
        }

        return displayCommonProjects(employee1WithLongestDuration, employee2WithLongestDuration, result);
    }

    // check if employee and his participation are valid search for common periods
    private static boolean isNotValidEmployeeForComparison(Employee employee) {
        return employee == null || employee.getProjectParticipation() == null || employee.getProjectParticipation().isEmpty();
    }

    private static long calculateTotalDuration(Employee employee1, Employee employee2) {
        if (isNotValidEmployeeForComparison(employee1) || isNotValidEmployeeForComparison(employee2)) {
            return 0;
        }

        Map<Integer, ProjectParticipation> projectParticipation1 = employee1.getProjectParticipation();
        Map<Integer, ProjectParticipation> projectParticipation2 = employee2.getProjectParticipation();

        return projectParticipation1.entrySet().stream()
            .filter(entry -> projectParticipation2.containsKey(entry.getKey()))
            .mapToLong(entry -> calculateDuration(entry.getValue(), projectParticipation2.get(entry.getKey())))
            .sum();
    }

    private static String displayCommonProjects(Employee employee1, Employee employee2, StringBuilder result) {
        if (isNotValidEmployeeForComparison(employee1) || isNotValidEmployeeForComparison(employee2)) {
            return result.toString();
        }

        result.append(COMMON_PROJECTS).append(System.lineSeparator());
        for (Map.Entry<Integer, ProjectParticipation> entry : employee1.getProjectParticipation().entrySet()) {
            int projectNumber = entry.getKey();
            ProjectParticipation projectParticipation1 = entry.getValue();
            ProjectParticipation projectParticipation2 = employee2.getProjectParticipation().get(projectNumber);

            if (projectParticipation2 != null) {
                long duration = calculateDuration(projectParticipation1, projectParticipation2);
                result.append(String.format(PROJECT_DURATION, projectNumber, duration)).append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    private static long calculateDuration(ProjectParticipation projectParticipation1, ProjectParticipation projectParticipation2) {
        long duration = 0;

        for (DateRange dateRange1 : projectParticipation1.getDateRangesOnProject()) {
            for (DateRange dateRange2 : projectParticipation2.getDateRangesOnProject()) {
                LocalDate startDate1 = dateRange1.getStartDate();
                LocalDate endDate1 = dateRange1.getEndDate();
                LocalDate startDate2 = dateRange2.getStartDate();
                LocalDate endDate2 = dateRange2.getEndDate();

                // Check for overlap between date ranges
                LocalDate overlapStartDate = startDate1.isBefore(startDate2) ? startDate2 : startDate1;
                LocalDate overlapEndDate = endDate1.isBefore(endDate2) ? endDate1 : endDate2;

                if (!overlapStartDate.isAfter(overlapEndDate)) {
                    // Calculate the duration in days, add 1 day to be correct(Example: if Emp starts at 01-01-2020 and works for only one day)
                    long days = ChronoUnit.DAYS.between(overlapStartDate, overlapEndDate) + 1L;
                    duration += days;
                }
            }
        }

        return duration;
    }
}

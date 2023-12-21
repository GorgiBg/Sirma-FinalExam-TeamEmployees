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

        // pick any two employees and check common projects duration
        for (int i = 0; i < employees.size(); i++) {
            Employee employee1 = employees.get(i);

            for (int j = i + 1; j < employees.size(); j++) {
                Employee employee2 = employees.get(j);

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
        }

        return displayCommonProjects(employee1WithLongestDuration, employee2WithLongestDuration, result);
    }

    // calculates the duration on common projects for emp1 and emp2
    private static long calculateTotalDuration(Employee employee1, Employee employee2) {
        return employee1.getProjectParticipation().entrySet().stream()
            .filter(entry -> employee2.getProjectParticipation().containsKey(entry.getKey()))
            .mapToLong(entry -> calculateDuration(entry.getValue(), employee2.getProjectParticipation().get(entry.getKey())))
            .sum();
    }

    private static String displayCommonProjects(Employee employee1, Employee employee2, StringBuilder result) {
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
                    // Calculate the duration in days
                    long days = ChronoUnit.DAYS.between(overlapStartDate, overlapEndDate);
                    duration += days;
                }
            }
        }

        return duration;
    }
}

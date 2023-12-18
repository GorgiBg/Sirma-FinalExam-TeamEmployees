package com.exam.sirma.teamemployees.util;

import com.exam.sirma.teamemployees.entity.DateRange;
import com.exam.sirma.teamemployees.entity.Employee;
import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Component
public class CalculationUtil {
    public static void identifyLongestWorkingPair(List<Employee> employees) {
        long longestDuration = 0;
        Employee employee1WithLongestDuration = null;
        Employee employee2WithLongestDuration = null;

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

        System.out.println("Longest working pair: " + employee1WithLongestDuration.getEmpId() +
            " and " + employee2WithLongestDuration.getEmpId());
        System.out.println("Total duration: " + longestDuration + " days");

        displayCommonProjects(employee1WithLongestDuration, employee2WithLongestDuration);
    }

    private static long calculateTotalDuration(Employee employee1, Employee employee2) {
        long totalDuration = 0;

        for (Map.Entry<Integer, ProjectParticipation> entry : employee1.getProjectParticipation().entrySet()) {
            int projectNumber = entry.getKey();
            ProjectParticipation projectParticipation1 = entry.getValue();
            ProjectParticipation projectParticipation2 = employee2.getProjectParticipation().get(projectNumber);

            if (projectParticipation2 != null) {
                totalDuration += calculateDuration(projectParticipation1, projectParticipation2);
            }
        }

        return totalDuration;
    }

    private static void displayCommonProjects(Employee employee1, Employee employee2) {
        System.out.println("Common projects:");

        for (Map.Entry<Integer, ProjectParticipation> entry : employee1.getProjectParticipation().entrySet()) {
            int projectNumber = entry.getKey();
            ProjectParticipation projectParticipation1 = entry.getValue();
            ProjectParticipation projectParticipation2 = employee2.getProjectParticipation().get(projectNumber);

            if (projectParticipation2 != null) {
                long duration = calculateDuration(projectParticipation1, projectParticipation2);
                System.out.println("Project " + projectNumber + ": " + duration + " days");
            }
        }
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

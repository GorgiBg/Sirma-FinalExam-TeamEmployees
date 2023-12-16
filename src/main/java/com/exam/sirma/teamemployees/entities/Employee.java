package com.exam.sirma.teamemployees.entities;

import java.time.LocalDate;

public class Employee {
    private Long empId;
    private int projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Employee(Long empId, int projectId, LocalDate dateFrom, LocalDate dateTo) {
        this.empId = empId;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Employee() {
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "empId=" + empId +
            ", projectId=" + projectId +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            '}';
    }
}

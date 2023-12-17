package com.exam.sirma.teamemployees.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class ProjectParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int projectNumber;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @ManyToOne
    private Employee employee;

    public ProjectParticipation(int projectNumber, LocalDate dateFrom, LocalDate dateTo) {
        this.projectNumber = projectNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public ProjectParticipation() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "ProjectParticipation{" +
            "id=" + id +
            ", projectNumber=" + projectNumber +
            ", dateFrom=" + dateFrom +
            ", dateTo=" + dateTo +
            '}';
    }
}

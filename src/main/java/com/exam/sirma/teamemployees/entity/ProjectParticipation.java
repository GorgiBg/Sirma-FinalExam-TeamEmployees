package com.exam.sirma.teamemployees.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ProjectParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int projectNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateRange> dateRangesOnProject = new ArrayList<>();

    @ManyToOne
    private Employee employee;

    public ProjectParticipation() {

    }

    public ProjectParticipation(Long id, int projectNumber, Employee employee) {
        this.id = id;
        this.projectNumber = projectNumber;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(int projectNumber) {
        this.projectNumber = projectNumber;
    }

    public List<DateRange> getDateRangesOnProject() {
        return dateRangesOnProject;
    }

    public void setDateRangesOnProject(List<DateRange> dateRangesOnProject) {
        this.dateRangesOnProject = dateRangesOnProject;
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
            ", dateRangesOnProject=" + dateRangesOnProject +
            '}';
    }
}

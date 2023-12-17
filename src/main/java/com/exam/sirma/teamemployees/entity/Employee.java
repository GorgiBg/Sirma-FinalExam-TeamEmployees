package com.exam.sirma.teamemployees.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Employee {

    @Id
    private Long empId;

    @OneToMany
    private List<ProjectParticipation> projectParticipation;

    public Employee(Long empId, List<ProjectParticipation> projectParticipation) {
        this.empId = empId;
        this.projectParticipation = projectParticipation;
    }

    public Employee() {
        this.projectParticipation = new ArrayList<>();
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public List<ProjectParticipation> getProjectParticipation() {
        return projectParticipation;
    }

    public void setProjectParticipation(List<ProjectParticipation> projectParticipation) {
        this.projectParticipation = projectParticipation;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "empId=" + empId +
            ", projectParticipation=" + projectParticipation +
            '}';
    }
}

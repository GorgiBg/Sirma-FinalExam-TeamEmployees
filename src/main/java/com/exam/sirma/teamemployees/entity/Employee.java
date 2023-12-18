package com.exam.sirma.teamemployees.entity;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table
public class Employee {

    @Id
    private Long empId;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Integer, ProjectParticipation> projectParticipation;

    public Employee(Long empId, Map<Integer, ProjectParticipation> projectParticipation) {
        this.empId = empId;
        this.projectParticipation = projectParticipation;
    }

    public Employee() {
        this.projectParticipation = new HashMap<>();
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Map<Integer, ProjectParticipation> getProjectParticipation() {
        return projectParticipation;
    }

    public void setProjectParticipation(Map<Integer, ProjectParticipation> projectParticipation) {
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

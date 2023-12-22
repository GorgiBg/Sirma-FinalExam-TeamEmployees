package com.exam.sirma.teamemployees.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table
public class Employee {

    @Id
    @NotNull(message = "Employee ID must not be null!")
    private Long empId;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Map<Integer, ProjectParticipation> projectParticipation = new HashMap<>();

    public Employee(Long empId) {
        this.empId = empId;
    }

    public Employee() {
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
            '}';
    }
}

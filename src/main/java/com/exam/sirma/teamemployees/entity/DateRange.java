package com.exam.sirma.teamemployees.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class DateRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;



    @ManyToOne
    ProjectParticipation participation;

    public DateRange(LocalDate startDate, LocalDate endDate, ProjectParticipation participation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.participation = participation;
    }

    public DateRange() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DateRange{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            '}';
    }

    public ProjectParticipation getParticipation() {
        return participation;
    }

    public void setParticipation(ProjectParticipation participation) {
        this.participation = participation;
    }
}


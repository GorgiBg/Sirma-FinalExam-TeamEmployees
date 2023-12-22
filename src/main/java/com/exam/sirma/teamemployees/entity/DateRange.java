package com.exam.sirma.teamemployees.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

@Entity
@Table
public class DateRange {

    @ManyToOne
    ProjectParticipation participation;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Start date must not be null!")
    private LocalDate startDate;
    @NotNull(message = "End date must not be null!")
    private LocalDate endDate;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


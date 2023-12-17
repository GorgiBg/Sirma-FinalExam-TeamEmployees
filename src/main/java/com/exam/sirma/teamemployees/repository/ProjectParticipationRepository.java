package com.exam.sirma.teamemployees.repository;

import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectParticipationRepository extends JpaRepository<ProjectParticipation, Long> {
}

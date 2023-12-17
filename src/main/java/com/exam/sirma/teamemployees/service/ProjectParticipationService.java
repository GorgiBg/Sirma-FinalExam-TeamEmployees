package com.exam.sirma.teamemployees.service;

import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.repository.ProjectParticipationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectParticipationService {
    private final ProjectParticipationRepository projectParticipationRepository;

    public ProjectParticipationService(ProjectParticipationRepository projectParticipationRepository) {
        this.projectParticipationRepository = projectParticipationRepository;
    }

    public void saveAllProjectParticipation(List<ProjectParticipation> projectParticipation) {
        projectParticipationRepository.saveAll(projectParticipation);
    }

    public List<ProjectParticipation> getAllProjectParticipation() {
        return projectParticipationRepository.findAll();
    }

    public ProjectParticipation getProjectParticipationById(Long id) {
        try {
            return projectParticipationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No ProjectParticipation with ID: " + id + " found!"));
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ProjectParticipation saveProjectParticipation(ProjectParticipation projectParticipation) {
        return projectParticipationRepository.save(projectParticipation);
    }

    public void deleteProjectParticipation(Long id) {
        projectParticipationRepository.deleteById(id);
    }
}

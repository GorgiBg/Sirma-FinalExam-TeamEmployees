package com.exam.sirma.teamemployees.service;

import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.repository.ProjectParticipationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.exam.sirma.teamemployees.util.StringConstant.PROJECT_PARTICIPATION_NOT_FOUND;

@Service
@Slf4j
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
                .orElseThrow(() -> new NoSuchElementException(String.format(PROJECT_PARTICIPATION_NOT_FOUND, id)));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
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

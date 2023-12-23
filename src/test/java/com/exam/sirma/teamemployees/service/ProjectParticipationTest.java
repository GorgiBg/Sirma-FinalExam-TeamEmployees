package com.exam.sirma.teamemployees.service;

import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.repository.ProjectParticipationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProjectParticipationTest {

    @Mock
    private ProjectParticipationRepository participationRepository;

    @InjectMocks
    private ProjectParticipationService participationService;

    @Test
    void saveAllParticipation() {
        List<ProjectParticipation> projectParticipation = List.of(new ProjectParticipation(), new ProjectParticipation());
        participationService.saveAllProjectParticipation(projectParticipation);
        verify(participationRepository).saveAll(projectParticipation);
    }

    @Test
    void saveProjectParticipation() {
        ProjectParticipation projectParticipation = new ProjectParticipation();
        participationService.saveProjectParticipation(projectParticipation);
        verify(participationRepository).save(projectParticipation);
    }

    @Test
    void getAllProjectParticipation() {
        List<ProjectParticipation> expectedParticipation = List.of(new ProjectParticipation(), new ProjectParticipation());
        when(participationRepository.findAll()).thenReturn(expectedParticipation);
        List<ProjectParticipation> result = participationService.getAllProjectParticipation();
        assertEquals(expectedParticipation, result);
    }

    @Test
    void getPPWithExistingIdReturnsPP() {
        Long existingId = 1L;
        ProjectParticipation expectedParticipation = new ProjectParticipation();
        when(participationRepository.findById(existingId)).thenReturn(java.util.Optional.of(expectedParticipation));
        ProjectParticipation result = participationService.getProjectParticipationById(existingId);
        assertEquals(expectedParticipation, result);
    }

    @Test
    void getPPWithNonExistingIdReturnsNull() {
        Long nonExistingId = 99L;
        when(participationRepository.findById(nonExistingId)).thenReturn(java.util.Optional.empty());
        ProjectParticipation result = participationService.getProjectParticipationById(nonExistingId);
        assertNull(result);
    }

    @Test
    void deleteProjectParticipation() {
        Long idToDelete = 1L;
        participationService.deleteProjectParticipation(idToDelete);
        verify(participationRepository).deleteById(idToDelete);
    }
}


package com.exam.sirma.teamemployees.controller;

import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.service.ProjectParticipationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/projectParticipation")
public class ProjectParticipationController {

    private final ProjectParticipationService projectParticipationService;

    public ProjectParticipationController(ProjectParticipationService projectParticipationService) {
        this.projectParticipationService = projectParticipationService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectParticipation>> getAllProjectParticipation() {
        List<ProjectParticipation> projectParticipation = projectParticipationService.getAllProjectParticipation();
        return new ResponseEntity<>(projectParticipation, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectParticipation> getProjectParticipationById(@PathVariable Long id) {
        try {
            ProjectParticipation projectParticipation = projectParticipationService.getProjectParticipationById(id);
            return new ResponseEntity<>(projectParticipation, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ProjectParticipation> saveProjectParticipation(@RequestBody ProjectParticipation projectParticipation) {
        ProjectParticipation savedProjectParticipation = projectParticipationService.saveProjectParticipation(projectParticipation);
        return new ResponseEntity<>(savedProjectParticipation, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectParticipation(@PathVariable Long id) {
        projectParticipationService.deleteProjectParticipation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.exam.sirma.teamemployees.controller;

import com.exam.sirma.teamemployees.entity.ProjectParticipation;
import com.exam.sirma.teamemployees.service.ProjectParticipationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.exam.sirma.teamemployees.util.StringConstant.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
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
            log.error(FAILED_TO_PROJECT_PARTICIPATION, id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProjectParticipation> saveProjectParticipation(@Valid @RequestBody ProjectParticipation projectParticipation) {
        try {
            ProjectParticipation savedProjectParticipation = projectParticipationService.saveProjectParticipation(projectParticipation);
            return new ResponseEntity<>(savedProjectParticipation, HttpStatus.CREATED);
        } catch (Exception validationException) {
            log.error(VALIDATION_ERROR, validationException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectParticipation(@PathVariable Long id) {
        projectParticipationService.deleteProjectParticipation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

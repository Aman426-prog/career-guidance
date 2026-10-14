package com.careerguidance.app.web;

import com.careerguidance.app.service.CareerService;
import com.careerguidance.app.web.Dtos.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CareerController {
    private final CareerService career;

    public CareerController(CareerService career) { this.career = career; }

    @GetMapping("/api/analysis")
    public AnalysisDto analysis(Authentication auth) {
        return career.analyze((Long) auth.getPrincipal());
    }

    /** Missing skills in the order to learn them (high priority first). */
    @GetMapping("/api/roadmap")
    public List<GapDto> roadmap(Authentication auth) {
        return career.analyze((Long) auth.getPrincipal()).missing();
    }

    @GetMapping("/api/certifications/recommendations")
    public List<CertDto> certifications(Authentication auth) {
        return career.certifications((Long) auth.getPrincipal());
    }

    @GetMapping("/api/projects/recommendations")
    public List<ProjectDto> projects(Authentication auth) {
        return career.projects((Long) auth.getPrincipal());
    }
}

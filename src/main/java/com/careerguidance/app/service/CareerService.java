package com.careerguidance.app.service;

import com.careerguidance.app.domain.AppUser;
import com.careerguidance.app.domain.JobRole;
import com.careerguidance.app.domain.Priority;
import com.careerguidance.app.domain.RoleSkill;
import com.careerguidance.app.repo.ProjectIdeaRepository;
import com.careerguidance.app.repo.RoleSkillRepository;
import com.careerguidance.app.repo.UserRepository;
import com.careerguidance.app.repo.UserSkillRepository;
import com.careerguidance.app.web.Dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/** Skill gap, readiness score, roadmap, certification and project recommendations. */
@Service
public class CareerService {
    private static final Map<Priority, Integer> WEIGHT = Map.of(Priority.HIGH, 3, Priority.MEDIUM, 2, Priority.BONUS, 1);

    private final UserRepository users;
    private final RoleSkillRepository roleSkills;
    private final UserSkillRepository userSkills;
    private final ProjectIdeaRepository projects;

    public CareerService(UserRepository users, RoleSkillRepository roleSkills,
                         UserSkillRepository userSkills, ProjectIdeaRepository projects) {
        this.users = users;
        this.roleSkills = roleSkills;
        this.userSkills = userSkills;
        this.projects = projects;
    }

    private record Result(JobRole role, int score, List<String> covered, List<RoleSkill> missing) {}

    private Result compute(Long userId) {
        AppUser user = users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please log in again"));
        JobRole role = user.getTargetRole();
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a target role first");
        }

        Set<Long> have = userSkills.findByUserId(userId).stream()
                .map(us -> us.getSkill().getId())
                .collect(Collectors.toSet());

        int total = 0, got = 0;
        List<String> covered = new ArrayList<>();
        List<RoleSkill> missing = new ArrayList<>();
        for (RoleSkill rs : roleSkills.findByRoleIdOrderById(role.getId())) {
            int w = WEIGHT.get(rs.getPriority());
            total += w;
            if (have.contains(rs.getSkill().getId())) {
                got += w;
                covered.add(rs.getSkill().getName());
            } else {
                missing.add(rs);
            }
        }
        missing.sort(Comparator.comparing(RoleSkill::getPriority)); // stable: keeps catalogue order inside a priority
        int score = total == 0 ? 0 : Math.round(100f * got / total);
        return new Result(role, score, covered, missing);
    }

    public AnalysisDto analyze(Long userId) {
        Result r = compute(userId);
        List<GapDto> gaps = r.missing().stream()
                .map(rs -> new GapDto(rs.getSkill().getName(), rs.getPriority().name(), rs.getSkill().getBlurb()))
                .toList();
        return new AnalysisDto(r.role().getName(), r.score(), statusFor(r.score()), r.covered(), gaps);
    }

    public List<CertDto> certifications(Long userId) {
        return compute(userId).missing().stream()
                .filter(rs -> rs.getSkill().getCertification() != null)
                .map(rs -> new CertDto(rs.getSkill().getCertification(), rs.getSkill().getName()))
                .toList();
    }

    public List<ProjectDto> projects(Long userId) {
        JobRole role = compute(userId).role();
        return projects.findByRoleId(role.getId()).stream()
                .map(p -> new ProjectDto(p.getTitle(), p.getDescription()))
                .toList();
    }

    static String statusFor(int score) {
        if (score >= 85) return "Job ready";
        if (score >= 65) return "Nearly there";
        if (score >= 35) return "Building momentum";
        return "Just getting started";
    }
}

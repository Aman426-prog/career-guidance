package com.careerguidance.app.web;

import com.careerguidance.app.domain.Skill;
import com.careerguidance.app.domain.UserSkill;
import com.careerguidance.app.repo.SkillRepository;
import com.careerguidance.app.repo.UserRepository;
import com.careerguidance.app.repo.UserSkillRepository;
import com.careerguidance.app.web.Dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillRepository skills;
    private final UserSkillRepository userSkills;
    private final UserRepository users;

    public SkillController(SkillRepository skills, UserSkillRepository userSkills, UserRepository users) {
        this.skills = skills;
        this.userSkills = userSkills;
        this.users = users;
    }

    /** The full catalogue. */
    @GetMapping
    public List<SkillDto> all() {
        return skills.findAll().stream().map(SkillController::toDto).toList();
    }

    /** Skills the current user has. */
    @GetMapping("/me")
    public List<SkillDto> mine(Authentication auth) {
        return userSkills.findByUserId(uid(auth)).stream().map(us -> toDto(us.getSkill())).toList();
    }

    @PostMapping("/me")
    public List<SkillDto> add(Authentication auth, @Valid @RequestBody SkillRequest req) {
        Long uid = uid(auth);
        Skill skill = skills.findById(req.skillId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));
        if (!userSkills.existsByUserIdAndSkillId(uid, skill.getId())) {
            userSkills.save(new UserSkill(users.getReferenceById(uid), skill));
        }
        return mine(auth);
    }

    @DeleteMapping("/me/{skillId}")
    public List<SkillDto> remove(Authentication auth, @PathVariable Long skillId) {
        userSkills.deleteByUserIdAndSkillId(uid(auth), skillId);
        return mine(auth);
    }

    private static Long uid(Authentication auth) { return (Long) auth.getPrincipal(); }

    static SkillDto toDto(Skill s) {
        return new SkillDto(s.getId(), s.getName(), s.getCategory(), s.getBlurb());
    }
}

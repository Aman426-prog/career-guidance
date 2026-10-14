package com.careerguidance.app.web;

import com.careerguidance.app.domain.Skill;
import com.careerguidance.app.domain.UserSkill;
import com.careerguidance.app.repo.SkillRepository;
import com.careerguidance.app.repo.UserRepository;
import com.careerguidance.app.repo.UserSkillRepository;
import com.careerguidance.app.service.SkillExtractor;
import com.careerguidance.app.web.Dtos.ResumeResult;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final SkillRepository skills;
    private final UserSkillRepository userSkills;
    private final UserRepository users;
    private final SkillExtractor extractor;

    public ResumeController(SkillRepository skills, UserSkillRepository userSkills,
                            UserRepository users, SkillExtractor extractor) {
        this.skills = skills;
        this.userSkills = userSkills;
        this.users = users;
        this.extractor = extractor;
    }

    /** Upload a PDF resume: finds known skills in the text and adds them to the user's profile. */
    @PostMapping
    public ResumeResult upload(Authentication auth, @RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase(Locale.ROOT);
        if (file.isEmpty() || !name.endsWith(".pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upload a PDF file");
        }
        String text;
        try (PDDocument doc = Loader.loadPDF(file.getBytes())) {
            text = new PDFTextStripper().getText(doc);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read that PDF");
        }

        Long uid = (Long) auth.getPrincipal();
        List<Skill> found = extractor.extract(text, skills.findAll());
        List<String> added = new ArrayList<>();
        for (Skill s : found) {
            if (!userSkills.existsByUserIdAndSkillId(uid, s.getId())) {
                userSkills.save(new UserSkill(users.getReferenceById(uid), s));
                added.add(s.getName());
            }
        }
        return new ResumeResult(found.stream().map(Skill::getName).toList(), added);
    }
}

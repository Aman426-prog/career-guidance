package com.careerguidance.app.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public final class Dtos {
    private Dtos() {}

    public record RegisterRequest(@NotBlank String name,
                                  @NotBlank @Email String email,
                                  @NotBlank @Size(min = 6, message = "must be at least 6 characters") String password) {}
    public record LoginRequest(@NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String token, String name) {}

    public record RoleDto(Long id, String name) {}
    public record ProfileDto(String name, String email, RoleDto targetRole) {}
    public record RoleRequest(@NotNull Long roleId) {}

    public record SkillDto(Long id, String name, String category, String blurb) {}
    public record SkillRequest(@NotNull Long skillId) {}

    public record ResumeResult(List<String> found, List<String> added) {}

    public record GapDto(String skill, String priority, String blurb) {}
    public record AnalysisDto(String role, int score, String status, List<String> covered, List<GapDto> missing) {}
    public record CertDto(String certification, String skill) {}
    public record ProjectDto(String title, String description) {}
}

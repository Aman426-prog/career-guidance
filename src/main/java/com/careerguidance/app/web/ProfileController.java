package com.careerguidance.app.web;

import com.careerguidance.app.domain.AppUser;
import com.careerguidance.app.domain.JobRole;
import com.careerguidance.app.repo.JobRoleRepository;
import com.careerguidance.app.repo.UserRepository;
import com.careerguidance.app.web.Dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfileController {
    private final UserRepository users;
    private final JobRoleRepository roles;

    public ProfileController(UserRepository users, JobRoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    @GetMapping("/roles")
    public List<RoleDto> roles() {
        return roles.findAll().stream().map(r -> new RoleDto(r.getId(), r.getName())).toList();
    }

    @GetMapping("/users/me")
    public ProfileDto me(Authentication auth) {
        return toDto(currentUser(auth));
    }

    @PutMapping("/users/me/role")
    public ProfileDto setRole(Authentication auth, @Valid @RequestBody RoleRequest req) {
        AppUser user = currentUser(auth);
        JobRole role = roles.findById(req.roleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        user.setTargetRole(role);
        return toDto(users.save(user));
    }

    private AppUser currentUser(Authentication auth) {
        return users.findById((Long) auth.getPrincipal())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please log in again"));
    }

    private ProfileDto toDto(AppUser u) {
        JobRole r = u.getTargetRole();
        return new ProfileDto(u.getName(), u.getEmail(), r == null ? null : new RoleDto(r.getId(), r.getName()));
    }
}

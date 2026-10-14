package com.careerguidance.app.repo;

import com.careerguidance.app.domain.ProjectIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectIdeaRepository extends JpaRepository<ProjectIdea, Long> {
    List<ProjectIdea> findByRoleId(Long roleId);
}

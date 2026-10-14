package com.careerguidance.app.repo;

import com.careerguidance.app.domain.RoleSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleSkillRepository extends JpaRepository<RoleSkill, Long> {
    List<RoleSkill> findByRoleIdOrderById(Long roleId);
}

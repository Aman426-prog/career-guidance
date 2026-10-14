package com.careerguidance.app.repo;

import com.careerguidance.app.domain.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUserId(Long userId);
    boolean existsByUserIdAndSkillId(Long userId, Long skillId);

    @Transactional
    void deleteByUserIdAndSkillId(Long userId, Long skillId);
}

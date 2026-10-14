package com.careerguidance.app.repo;

import com.careerguidance.app.domain.JobRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRoleRepository extends JpaRepository<JobRole, Long> {
}

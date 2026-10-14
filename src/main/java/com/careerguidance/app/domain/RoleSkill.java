package com.careerguidance.app.domain;

import jakarta.persistence.*;

/** A skill a job role requires, with how important it is. */
@Entity
public class RoleSkill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private JobRole role;

    @ManyToOne(optional = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    protected RoleSkill() {}

    public RoleSkill(JobRole role, Skill skill, Priority priority) {
        this.role = role;
        this.skill = skill;
        this.priority = priority;
    }

    public JobRole getRole() { return role; }
    public Skill getSkill() { return skill; }
    public Priority getPriority() { return priority; }
}

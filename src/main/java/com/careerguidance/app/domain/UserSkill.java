package com.careerguidance.app.domain;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "skill_id"}))
public class UserSkill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AppUser user;

    @ManyToOne(optional = false)
    private Skill skill;

    protected UserSkill() {}

    public UserSkill(AppUser user, Skill skill) {
        this.user = user;
        this.skill = skill;
    }

    public Skill getSkill() { return skill; }
}

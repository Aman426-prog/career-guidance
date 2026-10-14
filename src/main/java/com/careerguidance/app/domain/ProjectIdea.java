package com.careerguidance.app.domain;

import jakarta.persistence.*;

@Entity
public class ProjectIdea {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private JobRole role;

    @Column(nullable = false)
    private String title;

    @Column(length = 400)
    private String description;

    protected ProjectIdea() {}

    public ProjectIdea(JobRole role, String title, String description) {
        this.role = role;
        this.title = title;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
}

package com.careerguidance.app.domain;

import jakarta.persistence.*;

@Entity
public class JobRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    protected JobRole() {}

    public JobRole(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
}

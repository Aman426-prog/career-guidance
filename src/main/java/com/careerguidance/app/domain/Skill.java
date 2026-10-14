package com.careerguidance.app.domain;

import jakarta.persistence.*;

@Entity
public class Skill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String category;

    @Column(length = 300)
    private String blurb;

    /** Optional certification that covers this skill. */
    private String certification;

    protected Skill() {}

    public Skill(String name, String category, String blurb, String certification) {
        this.name = name;
        this.category = category;
        this.blurb = blurb;
        this.certification = certification;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getBlurb() { return blurb; }
    public String getCertification() { return certification; }
}

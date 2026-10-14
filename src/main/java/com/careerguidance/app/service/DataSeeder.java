package com.careerguidance.app.service;

import com.careerguidance.app.domain.*;
import com.careerguidance.app.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/** Fills the catalogue (skills, roles, projects) on first start, when the database is empty. */
@Component
public class DataSeeder implements CommandLineRunner {
    private final SkillRepository skills;
    private final JobRoleRepository roles;
    private final RoleSkillRepository roleSkills;
    private final ProjectIdeaRepository projects;
    private final Map<String, Skill> byName = new HashMap<>();

    public DataSeeder(SkillRepository skills, JobRoleRepository roles,
                      RoleSkillRepository roleSkills, ProjectIdeaRepository projects) {
        this.skills = skills;
        this.roles = roles;
        this.roleSkills = roleSkills;
        this.projects = projects;
    }

    @Override
    public void run(String... args) {
        if (skills.count() > 0) return;

        skill("Languages", "Java", "Syntax, OOP, collections, streams and exceptions.", "Oracle Certified Professional: Java SE Developer");
        skill("Languages", "Python", "Core syntax, functions, files and virtual environments.", "PCEP or PCAP (Python Institute)");
        skill("Languages", "JavaScript", "Language fundamentals, async/await, the DOM and modules.", null);
        skill("Languages", "TypeScript", "Types, interfaces and generics on top of JavaScript.", null);
        skill("Languages", "SQL", "Joins, grouping, subqueries and indexes.", null);

        skill("Web", "HTML/CSS", "Semantic markup, flexbox, grid and responsive layouts.", null);
        skill("Web", "React", "Components, state, hooks and data fetching.", null);
        skill("Web", "Node.js", "Servers, npm and building an API with Express.", null);
        skill("Web", "Spring Boot", "Controllers, services, JPA repositories and configuration.", null);
        skill("Web", "REST APIs", "HTTP verbs, status codes, JSON and API design.", null);

        skill("Data", "Excel", "Formulas, pivot tables and lookup functions.", "Microsoft Office Specialist: Excel");
        skill("Data", "Pandas", "DataFrames, cleaning, grouping and merging.", null);
        skill("Data", "Statistics", "Distributions, hypothesis tests and correlation.", null);
        skill("Data", "Data visualization", "Choosing the right chart and telling a story with data.", null);
        skill("Data", "Power BI", "Data models, DAX basics and interactive dashboards.", "Microsoft PL-300: Power BI Data Analyst");
        skill("Data", "Machine learning", "Supervised learning, evaluation and scikit-learn.", null);

        skill("Cloud and tools", "Git", "Branching, merging, pull requests and resolving conflicts.", null);
        skill("Cloud and tools", "Docker", "Images, containers, volumes and Docker Compose.", null);
        skill("Cloud and tools", "Linux", "Shell, permissions, processes and basic scripting.", "LFCS (Linux Foundation)");
        skill("Cloud and tools", "AWS", "Core services: EC2, S3, IAM and networking.", "AWS Certified Cloud Practitioner");
        skill("Cloud and tools", "CI/CD", "Automated builds, tests and deploys with GitHub Actions.", null);
        skill("Cloud and tools", "Kubernetes", "Pods, deployments, services and basic troubleshooting.", "Certified Kubernetes Administrator (Linux Foundation)");

        skill("Core", "Data structures", "Arrays, maps, trees, graphs and complexity.", null);
        skill("Core", "Testing", "Unit tests, mocking and test-first habits.", null);
        skill("Core", "PostgreSQL", "Schema design, transactions and query tuning.", null);
        skill("Core", "Auth & security", "Password hashing, JWT, sessions and common attacks.", null);
        skill("Core", "System design", "Scaling, caching, queues and trade-offs.", null);

        // "Skill:H" = high priority, ":M" = medium, ":B" = bonus
        JobRole backend = role("Backend developer",
                "Java:H", "Spring Boot:H", "SQL:H", "REST APIs:H", "Git:H",
                "PostgreSQL:M", "Docker:M", "Auth & security:M", "Data structures:M", "Testing:M",
                "System design:B", "AWS:B", "CI/CD:B");
        project(backend, "Job tracker API", "Spring Boot, PostgreSQL, JWT login, and CRUD for job applications.");
        project(backend, "URL shortener", "REST APIs, caching and a simple click-analytics endpoint.");
        project(backend, "Dockerized blog service", "Containerize an API with a database and a CI pipeline.");

        JobRole frontend = role("Frontend developer",
                "HTML/CSS:H", "JavaScript:H", "React:H", "Git:H",
                "TypeScript:M", "REST APIs:M", "Testing:M",
                "Node.js:B", "Data structures:B", "System design:B");
        project(frontend, "Weather dashboard", "Fetch a public API and build a responsive UI.");
        project(frontend, "Kanban board", "React state, drag and drop and local persistence.");
        project(frontend, "Portfolio site", "Accessible layout, dark mode and deployment.");

        JobRole fullstack = role("Full-stack developer",
                "JavaScript:H", "React:H", "Node.js:H", "SQL:H", "REST APIs:H", "Git:H",
                "HTML/CSS:M", "TypeScript:M", "Docker:M", "Auth & security:M", "PostgreSQL:M",
                "AWS:B", "Testing:B", "CI/CD:B");
        project(fullstack, "Resume analyzer", "Upload a resume, store results, and show skill matches.");
        project(fullstack, "Expense tracker", "Auth, a database and a charts page.");
        project(fullstack, "Real-time chat", "Node server, React client and message history.");

        JobRole analyst = role("Data analyst",
                "SQL:H", "Excel:H", "Statistics:H", "Data visualization:H",
                "Python:M", "Pandas:M", "Power BI:M",
                "Git:B", "Machine learning:B");
        project(analyst, "Sales dashboard", "Clean a dataset and build a Power BI or Excel dashboard.");
        project(analyst, "Churn analysis", "SQL queries plus a short written insight report.");
        project(analyst, "A/B test review", "Apply hypothesis testing to a sample experiment.");

        JobRole devops = role("DevOps engineer",
                "Linux:H", "Git:H", "Docker:H", "CI/CD:H", "AWS:H",
                "Kubernetes:M", "Python:M",
                "Testing:B", "Auth & security:B");
        project(devops, "Deploy pipeline", "GitHub Actions that test, build and ship a container.");
        project(devops, "Home-lab cluster", "Run a small app on Kubernetes with health checks.");
        project(devops, "Infra monitoring", "Collect metrics and alert on failures.");
    }

    private void skill(String category, String name, String blurb, String cert) {
        byName.put(name, skills.save(new Skill(name, category, blurb, cert)));
    }

    private JobRole role(String name, String... entries) {
        JobRole role = roles.save(new JobRole(name));
        for (String e : entries) {
            int i = e.lastIndexOf(':');
            Priority p = switch (e.charAt(i + 1)) {
                case 'H' -> Priority.HIGH;
                case 'M' -> Priority.MEDIUM;
                default -> Priority.BONUS;
            };
            roleSkills.save(new RoleSkill(role, byName.get(e.substring(0, i)), p));
        }
        return role;
    }

    private void project(JobRole role, String title, String description) {
        projects.save(new ProjectIdea(role, title, description));
    }
}

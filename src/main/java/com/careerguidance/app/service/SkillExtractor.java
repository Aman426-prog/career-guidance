package com.careerguidance.app.service;

import com.careerguidance.app.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Finds catalogue skills in resume text. A skill matches when its name or one of its aliases
 * appears as a whole word (so "java" does not match "javascript"). No fuzzy matching on purpose:
 * it keeps false positives low.
 */
@Component
public class SkillExtractor {

    private static final Map<String, List<String>> ALIASES = Map.ofEntries(
            Map.entry("Java", List.of("java")),
            Map.entry("Python", List.of("python")),
            Map.entry("JavaScript", List.of("javascript", "js", "ecmascript")),
            Map.entry("TypeScript", List.of("typescript")),
            Map.entry("SQL", List.of("sql", "mysql", "t-sql", "plsql", "pl/sql")),
            Map.entry("HTML/CSS", List.of("html", "html5", "css", "css3")),
            Map.entry("React", List.of("react", "reactjs", "react.js")),
            Map.entry("Node.js", List.of("node.js", "nodejs", "node js")),
            Map.entry("Spring Boot", List.of("spring boot", "springboot")),
            Map.entry("REST APIs", List.of("rest api", "rest apis", "restful", "restful api", "restful apis")),
            Map.entry("Excel", List.of("excel", "ms excel", "microsoft excel")),
            Map.entry("Pandas", List.of("pandas")),
            Map.entry("Statistics", List.of("statistics", "statistical analysis", "hypothesis testing")),
            Map.entry("Data visualization", List.of("data visualization", "data visualisation", "matplotlib", "seaborn", "tableau")),
            Map.entry("Power BI", List.of("power bi", "powerbi")),
            Map.entry("Machine learning", List.of("machine learning", "scikit-learn", "sklearn")),
            Map.entry("Git", List.of("git", "github", "gitlab", "bitbucket")),
            Map.entry("Docker", List.of("docker", "dockerfile")),
            Map.entry("Linux", List.of("linux", "ubuntu", "bash", "shell scripting")),
            Map.entry("AWS", List.of("aws", "amazon web services", "ec2")),
            Map.entry("CI/CD", List.of("ci/cd", "cicd", "ci cd", "github actions", "jenkins")),
            Map.entry("Kubernetes", List.of("kubernetes", "k8s")),
            Map.entry("Data structures", List.of("data structures", "dsa", "algorithms")),
            Map.entry("Testing", List.of("junit", "mockito", "pytest", "jest", "unit testing", "unit tests", "test-driven")),
            Map.entry("PostgreSQL", List.of("postgresql", "postgres")),
            Map.entry("Auth & security", List.of("jwt", "oauth", "spring security", "authentication", "authorization")),
            Map.entry("System design", List.of("system design", "microservices", "scalability"))
    );

    public List<Skill> extract(String resumeText, List<Skill> catalogue) {
        String text = resumeText.toLowerCase(Locale.ROOT);
        List<Skill> found = new ArrayList<>();
        for (Skill skill : catalogue) {
            List<String> names = ALIASES.getOrDefault(skill.getName(), List.of(skill.getName().toLowerCase(Locale.ROOT)));
            for (String alias : names) {
                Pattern p = Pattern.compile("(?<![a-z0-9])" + Pattern.quote(alias) + "(?![a-z0-9])");
                if (p.matcher(text).find()) {
                    found.add(skill);
                    break;
                }
            }
        }
        return found;
    }
}

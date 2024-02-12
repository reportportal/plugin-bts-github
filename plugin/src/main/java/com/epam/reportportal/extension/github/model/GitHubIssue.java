package com.epam.reportportal.extension.github.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GitHubIssue {
    private String title;
    private String description;
    private List<String> assignees;
    private String milestone;
    private List<String> labels;
}

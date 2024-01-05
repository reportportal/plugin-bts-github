package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum GitHubIssueField {
    TITLE(PostFormField.builder()
            .id("title")
            .fieldName("Title")
            .fieldType("string")
            .isRequired(true)
            .build()
    ),
    BODY(PostFormField.builder()
            .id("body")
            .fieldName("Description")
            .fieldType("string")
            .isRequired(false)
            .build()
    ),
    ASSIGNEES(PostFormField.builder()
            .id("assignees")
            .fieldName("Assignees")
            .fieldType("array")
            .isRequired(false)
            .build()
    ),
    MILESTONE(PostFormField.builder()
            .id("milestone")
            .fieldName("Milestone")
            .fieldType("string")
            .isRequired(false)
            .build()
    ),
    LABELS(PostFormField.builder()
            .id("labels")
            .fieldName("Labels")
            .fieldType("array")
            .isRequired(false)
            .build()
    );

    @Getter
    private static final List<PostFormField> availableFields = Arrays.stream(GitHubIssueField.values())
            .map(GitHubIssueField::getFieldMeta)
            .collect(Collectors.toList());
    private final PostFormField fieldMeta;
}

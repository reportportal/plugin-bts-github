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
            .description("The title of the issue.")
            .isRequired(true)
            .build()
    ),
    BODY(PostFormField.builder()
            .id("body")
            .fieldName("Description")
            .fieldType("string")
            .description("The contents of the issue.")
            .isRequired(false)
            .build()
    ),
    ASSIGNEES(PostFormField.builder()
            .id("assignees")
            .fieldName("Assignees")
            .fieldType("array")
            .description("Logins for Users to assign to this issue. NOTE: Only users with push access can set assignees for new issues. Assignees are silently dropped otherwise.")
            .isRequired(false)
            .build()
    ),
    MILESTONE(PostFormField.builder()
            .id("milestone")
            .fieldName("Milestone")
            .fieldType("string")
            .description("The number of the milestone to associate this issue with. NOTE: Only users with push access can set the milestone for new issues. The milestone is silently dropped otherwise.")
            .isRequired(false)
            .build()
    ),
    LABELS(PostFormField.builder()
            .id("labels")
            .fieldName("Labels")
            .fieldType("array")
            .description("Labels to associate with this issue. NOTE: Only users with push access can set labels for new issues. Labels are silently dropped otherwise.")
            .isRequired(false)
            .build()
    );

    @Getter
    private static final List<PostFormField> availableFields = Arrays.stream(GitHubIssueField.values())
            .map(GitHubIssueField::getFieldMeta)
            .collect(Collectors.toList());
    private final PostFormField fieldMeta;
}

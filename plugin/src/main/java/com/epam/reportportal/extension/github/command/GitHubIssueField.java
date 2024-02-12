package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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
            .description("Logins for Users to assign to this issue. " +
                    "NOTE: Only users with push access can set assignees for new " +
                    "issues. Assignees are silently dropped otherwise.")
            .isRequired(false)
            .build()
    ),
    MILESTONE(PostFormField.builder()
            .id("milestone")
            .fieldName("Milestone")
            .fieldType("string")
            .description("The number of the milestone to associate this issue with. " +
                    "NOTE: Only users with push access can set the milestone for new issues. " +
                    "The milestone is silently dropped otherwise.")
            .isRequired(false)
            .build()
    ),
    LABELS(PostFormField.builder()
            .id("labels")
            .fieldName("Labels")
            .fieldType("array")
            .description("Labels to associate with this issue. " +
                    "NOTE: Only users with push access can set labels for new issues. " +
                    "Labels are silently dropped otherwise.")
            .isRequired(false)
            .build()
    );

    private final PostFormField fieldMeta;

    public static List<PostFormField> getAvailableFieldsMeta() {
        return Arrays.stream(GitHubIssueField.values())
                .map(GitHubIssueField::getMetaCopy)
                .collect(Collectors.toList());
    }

    private PostFormField getMetaCopy() {
        return SerializationUtils.clone(fieldMeta);
    }

    public String getId() {
        return fieldMeta.getId();
    }
}

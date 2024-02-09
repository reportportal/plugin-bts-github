package com.epam.reportportal.extension.github.service;

import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.reportportal.extension.github.provider.GitHubIssuesProvider;
import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.epam.reportportal.extension.github.command.GitHubIssueField.ASSIGNEES;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.BODY;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.LABELS;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.MILESTONE;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.TITLE;

@RequiredArgsConstructor
public class GitHubIssueService {
    private final DescriptionService descriptionService;

    public Ticket createIssue(GitHubIssuesProvider issuesRestProvider, PostTicketRQ postTicketRQ) {
        return issuesRestProvider.createIssue(buildIssue(postTicketRQ));
    }

    private GitHubIssue buildIssue(PostTicketRQ ticketRequest) {
        var issue = new GitHubIssue();
        ticketRequest.getFields().forEach(field -> {
            String fieldId = field.getId();

            if (TITLE.getId().equals(fieldId)) {
                issue.setTitle(getSingleValue(field));
            } else if (BODY.getId().equals(fieldId)) {
                issue.setDescription(getSingleValue(field));
            } else if (LABELS.getId().equals(fieldId)) {
                issue.setLabels(field.getValue());
            } else if (ASSIGNEES.getId().equals(fieldId)) {
                issue.setAssignees(field.getValue());
            } else if (MILESTONE.getId().equals(fieldId)) {
                issue.setMilestone(getSingleValue(field));
            }
        });

        if (issue.getDescription() == null) {
            issue.setDescription(getDescription(ticketRequest));
        }

        return issue;
    }

    private String getDescription(PostTicketRQ ticketRequest) {
        return descriptionService.buildDescriptionString(ticketRequest);
    }

    private static String getSingleValue(PostFormField field) {
        return Optional.ofNullable(field.getValue())
                .map(List::iterator)
                .filter(Iterator::hasNext)
                .map(Iterator::next)
                .orElse(null);
    }
}

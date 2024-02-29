/*
 * Copyright 2023 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.epam.reportportal.extension.github.service;

import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.reportportal.extension.github.provider.GitHubIssuesProvider;
import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.epam.reportportal.extension.github.command.GitHubIssueField.ASSIGNEES;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.BODY;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.LABELS;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.MILESTONE;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.TITLE;

/**
 * @author RiverSharks
 */
@RequiredArgsConstructor
public class GitHubIssueService {
    private final DescriptionService descriptionService;

    public Ticket createIssue(GitHubIssuesProvider issuesRestProvider, PostTicketRQ postTicketRQ) {
        return issuesRestProvider.createIssue(buildIssue(postTicketRQ));
    }

    public Ticket getIssue(GitHubIssuesProvider issuesRestProvider, String issueId) {
        return issuesRestProvider.getIssue(issueId);
    }

    private GitHubIssue buildIssue(PostTicketRQ ticketRequest) {
        var issue = new GitHubIssue();
        ticketRequest.getFields().forEach(field -> {
            String fieldId = field.getId();

            if (TITLE.getId().equals(fieldId)) {
                issue.setTitle(getSingleValue(field));
            } else if (BODY.getId().equals(fieldId)) {
                issue.setDescription(getDescription(ticketRequest, getSingleValue(field)));
            } else if (LABELS.getId().equals(fieldId)) {
                issue.setLabels(field.getValue());
            } else if (ASSIGNEES.getId().equals(fieldId)) {
                issue.setAssignees(field.getValue());
            } else if (MILESTONE.getId().equals(fieldId)) {
                issue.setMilestone(getSingleValue(field));
            }
        });

        if (issue.getDescription() == null) {
            issue.setDescription(getDescription(ticketRequest, StringUtils.EMPTY));
        }

        return issue;
    }

    private String getDescription(PostTicketRQ ticketRequest, String initialDescription) {
        return descriptionService.buildDescriptionString(ticketRequest, initialDescription);
    }

    private static String getSingleValue(PostFormField field) {
        return Optional.ofNullable(field.getValue())
                .map(List::iterator)
                .filter(Iterator::hasNext)
                .map(Iterator::next)
                .orElse(null);
    }
}

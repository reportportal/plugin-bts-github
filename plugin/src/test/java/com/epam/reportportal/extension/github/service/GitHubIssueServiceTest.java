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

import com.epam.reportportal.extension.github.command.GitHubIssueField;
import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.reportportal.extension.github.provider.GitHubIssuesProvider;
import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author RiverSharks
 */
@ExtendWith(MockitoExtension.class)
public class GitHubIssueServiceTest {

    @Mock
    private DescriptionService descriptionService;

    @Mock
    private GitHubIssuesProvider issuesRestProvider;

    @InjectMocks
    private GitHubIssueService gitHubIssueService;

    @Captor
    private ArgumentCaptor<GitHubIssue> issueArgumentCaptor;

    @Test
    void createIssue_shouldCreateIssue_whenEverythingProvidedInRequest() {
        var ticketRQ = new PostTicketRQ();
        ticketRQ.setFields(getFieldsWithValues());

        var ticket = new Ticket();
        when(issuesRestProvider.createIssue(any())).thenReturn(ticket);
        var testDescription = "test desc";
        when(descriptionService.buildDescriptionString(any(), any())).thenReturn(testDescription);

        Ticket createdTicket = gitHubIssueService.createIssue(issuesRestProvider, ticketRQ);

        assertThat(createdTicket).isEqualTo(ticket);

        verify(issuesRestProvider, only()).createIssue(issueArgumentCaptor.capture());
        verify(descriptionService, only()).buildDescriptionString(ticketRQ, "body sample");
        GitHubIssue gitHubIssue = issueArgumentCaptor.getValue();

        assertThat(gitHubIssue.getTitle()).isEqualTo("Bug found");
        assertThat(gitHubIssue.getDescription()).isEqualTo(testDescription);
        assertThat(gitHubIssue.getAssignees()).containsExactlyInAnyOrder("user1", "user2");
        assertThat(gitHubIssue.getLabels()).containsExactlyInAnyOrder("bug", "severe");
        assertThat(gitHubIssue.getMilestone()).isEqualTo("milestone 1");
        assertThat(gitHubIssue.getDescription()).isEqualTo("test desc");
    }

    @Test
    void createIssue_shouldCreateIssue_whenDescriptionIsNotProvided() {
        var ticketRQ = new PostTicketRQ();
        ticketRQ.setFields(List.of());

        var ticket = new Ticket();
        when(issuesRestProvider.createIssue(any())).thenReturn(ticket);
        var testDescription = "test description";
        when(descriptionService.buildDescriptionString(any(), any())).thenReturn(testDescription);

        Ticket createdTicket = gitHubIssueService.createIssue(issuesRestProvider, ticketRQ);

        assertThat(createdTicket).isEqualTo(ticket);

        verify(issuesRestProvider, only()).createIssue(issueArgumentCaptor.capture());
        verify(descriptionService, only()).buildDescriptionString(ticketRQ, StringUtils.EMPTY);
        GitHubIssue gitHubIssue = issueArgumentCaptor.getValue();

        assertThat(gitHubIssue.getDescription()).isEqualTo(testDescription);
    }

    @Test
    void getIssue_shouldReturnIssue() {
        // given
        var ticket = new Ticket();
        var issueId = "1";

        // when
        when(issuesRestProvider.getIssue(issueId)).thenReturn(ticket);
        Ticket result = gitHubIssueService.getIssue(issuesRestProvider, issueId);

        // then
        assertThat(result).isNotNull();
        verify(issuesRestProvider, only()).getIssue(issueId);
    }

    private static List<PostFormField> getFieldsWithValues() {
        Map<String, List<String>> valuesForFields = Map.of(
                GitHubIssueField.BODY.getId(), List.of("body sample"),
                GitHubIssueField.ASSIGNEES.getId(), List.of("user1", "user2"),
                GitHubIssueField.LABELS.getId(), List.of("bug", "severe"),
                GitHubIssueField.MILESTONE.getId(), List.of("milestone 1"),
                GitHubIssueField.TITLE.getId(), List.of("Bug found")
        );
        List<PostFormField> availableFieldsMeta = GitHubIssueField.getAvailableFieldsMeta();
        availableFieldsMeta.forEach(postFormField ->
                postFormField.setValue(valuesForFields.get(postFormField.getId())));
        return availableFieldsMeta;
    }


}

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

package com.epam.reportportal.extension.github.provider.rest;

import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class GitHubIssuesRestProviderTest {

    private static final String TOKEN = "token";
    private static final String PROJECT = "project";
    private static final String OWNER = "owner";

    private final IssuesApi issuesApi = mock(IssuesApi.class);
    private final IssuesMapper issueMapper = mock(IssuesMapper.class);

    private final GitHubIssuesRestProvider provider =
            new GitHubIssuesRestProvider(issuesApi, issueMapper, OWNER, PROJECT, TOKEN);


    @Test
    void shouldCreateIssue() {
        var requestDto = new IssuesCreateRequestDto();
        when(issueMapper.mapToIssueCreateRequestDto(any())).thenReturn(requestDto);
        var responseDto = new IssueDto();
        when(issuesApi.issuesCreate(any(), any(), any(), any())).thenReturn(responseDto);
        var ticketExpected = new Ticket();
        when(issueMapper.mapToTicket(any())).thenReturn(ticketExpected);

        var ticketRequest = new PostTicketRQ();
        Ticket ticket = provider.createIssue(ticketRequest);

        assertThat(ticket).isEqualTo(ticketExpected);
        verify(issueMapper).mapToIssueCreateRequestDto(ticketRequest);
        verify(issuesApi, only()).issuesCreate(OWNER, PROJECT, "Bearer " + TOKEN, requestDto);
        verify(issueMapper).mapToTicket(responseDto);

        verifyNoMoreInteractions(issueMapper);
    }
}

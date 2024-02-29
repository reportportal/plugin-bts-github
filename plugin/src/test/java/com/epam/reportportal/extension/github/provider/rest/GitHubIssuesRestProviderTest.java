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

package com.epam.reportportal.extension.github.provider.rest;

import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author RiverSharks
 */
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

        var issue = new GitHubIssue();
        Ticket ticket = provider.createIssue(issue);

        assertThat(ticket).isEqualTo(ticketExpected);
        verify(issueMapper).mapToIssueCreateRequestDto(issue);
        verify(issuesApi, only()).issuesCreate(OWNER, PROJECT, "Bearer " + TOKEN, requestDto);
        verify(issueMapper).mapToTicket(responseDto);

        verifyNoMoreInteractions(issueMapper);
    }

    @Test
    void shouldReturnIssue() {
        // given
        var issueId = "12";
        var responseDto = new IssueDto();
        when(issuesApi.issuesGet(any(), any(), any(), any())).thenReturn(responseDto);
        var ticketExpected = new Ticket();
        when(issueMapper.mapToTicket(any())).thenReturn(ticketExpected);

        // when
        Ticket ticket = provider.getIssue(issueId);

        // then
        assertThat(ticket).isEqualTo(ticketExpected);
        verify(issuesApi, only()).issuesGet(OWNER, PROJECT, 12, "Bearer " + TOKEN);
        verify(issueMapper).mapToTicket(responseDto);
        verifyNoMoreInteractions(issueMapper, issuesApi);
    }

    @Test
    void shouldThrownExceptionWhenIssueIdIsNotNumber() {
        assertThatThrownBy(() -> provider.getIssue("abc"))
                .isExactlyInstanceOf(NumberFormatException.class);
    }
}

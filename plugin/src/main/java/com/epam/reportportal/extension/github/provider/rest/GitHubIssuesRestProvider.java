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
import com.epam.reportportal.extension.github.provider.GitHubIssuesProvider;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;


/**
 * @author RiverSharks
 */
public class GitHubIssuesRestProvider implements GitHubIssuesProvider {
    private static final String AUTH_TEMPLATE = "Bearer %s";

    private final IssuesApi issuesApi;
    private final IssuesMapper issueMapper;
    private final String owner;
    private final String project;
    private final String authorization;

    public GitHubIssuesRestProvider(IssuesApi issuesApi,
                                    IssuesMapper issueMapper,
                                    String owner,
                                    String project,
                                    String apiToken) {
        this.issuesApi = issuesApi;
        this.issueMapper = issueMapper;
        this.owner = owner;
        this.project = project;
        this.authorization = String.format(AUTH_TEMPLATE, apiToken);
    }

    @Override
    public Ticket createIssue(GitHubIssue ticketRequest) {
        IssuesCreateRequestDto requestDto = issueMapper.mapToIssueCreateRequestDto(ticketRequest);
        IssueDto issueDto = issuesApi.issuesCreate(owner, project, authorization, requestDto);
        return issueMapper.mapToTicket(issueDto);
    }

    @Override
    public Ticket getIssue(String issueId) {
        IssueDto issueDto = issuesApi.issuesGet(owner, project, Integer.valueOf(issueId), authorization);
        return issueMapper.mapToTicket(issueDto);
    }
}

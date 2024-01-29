package com.epam.reportportal.extension.github.provider.rest;

import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.reportportal.extension.github.provider.GitHubIssuesProvider;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;


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
    public Ticket createIssue(PostTicketRQ ticketRequest) {
        IssuesCreateRequestDto requestDto = issueMapper.mapToIssueCreateRequestDto(ticketRequest);
        IssueDto issueDto = issuesApi.issuesCreate(owner, project, authorization, requestDto);
        return issueMapper.mapToTicket(issueDto);
    }
}

package com.epam.reportportal.extension.github.provider.mapper;

import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;

public class IssuesMapper {

    public Ticket mapToTicket(IssueDto issueDto) {
        Ticket ticket = new Ticket();

        ticket.setId(String.valueOf(issueDto.getId()));
        ticket.setTicketUrl(issueDto.getHtmlUrl().toString());
        ticket.setStatus(issueDto.getState());
        ticket.setSummary(issueDto.getTitle());
        ticket.setPluginName("GitHub Plugin");

        return ticket;
    }

    public IssuesCreateRequestDto mapToIssueCreateRequestDto(GitHubIssue issue) {
        var issuesCreateRequestDto = new IssuesCreateRequestDto();

        issuesCreateRequestDto.setBody(issue.getDescription());
        issuesCreateRequestDto.setAssignees(issue.getAssignees());
        issuesCreateRequestDto.setLabels(issue.getLabels());
        issuesCreateRequestDto.setTitle(issue.getTitle());
        issuesCreateRequestDto.setMilestone(issue.getMilestone());

        return issuesCreateRequestDto;
    }

}

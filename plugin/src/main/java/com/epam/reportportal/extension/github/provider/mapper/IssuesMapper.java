package com.epam.reportportal.extension.github.provider.mapper;

import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.epam.reportportal.extension.github.command.GitHubIssueField.ASSIGNEES;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.BODY;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.LABELS;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.MILESTONE;
import static com.epam.reportportal.extension.github.command.GitHubIssueField.TITLE;

public class IssuesMapper {

    public Ticket mapToTicket(IssueDto issueDto) {
        Ticket ticket = new Ticket();

        ticket.setId(String.valueOf(issueDto.getId()));
        ticket.setTicketUrl(issueDto.getUrl().toString());
        ticket.setStatus(issueDto.getState());
        ticket.setSummary(issueDto.getTitle());
        ticket.setPluginName("GitHub plugin");

        return ticket;
    }

    public IssuesCreateRequestDto mapToIssueCreateRequestDto(PostTicketRQ ticketRequest) {
        var requestDto = new IssuesCreateRequestDto();

        ticketRequest.getFields().forEach(field -> {

            String fieldId = field.getId();

            if (TITLE.getId().equals(fieldId)) {
                requestDto.title(getSingleValue(field));
            } else if (BODY.getId().equals(fieldId)) {
                requestDto.body(getSingleValue(field));
            } else if (LABELS.getId().equals(fieldId)) {
                requestDto.labels(field.getValue());
            } else if (ASSIGNEES.getId().equals(fieldId)) {
                requestDto.assignees(field.getValue());
            } else if (MILESTONE.getId().equals(fieldId)) {
                requestDto.milestone(getSingleValue(field));
            }
        });

        return requestDto;
    }

    private static String getSingleValue(PostFormField field) {
        return Optional.ofNullable(field.getValue())
                .map(List::iterator)
                .filter(Iterator::hasNext)
                .map(Iterator::next)
                .orElse(null);
    }

}

package com.epam.reportportal.extension.github.provider.mapper;

import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

class IssuesMapperTest {
    private final PodamFactory podamFactory = new PodamFactoryImpl();
    private final IssuesMapper mapper = new IssuesMapper();

    @Test
    void mapToTicket_shouldMapIssueDtoToTicket() {
        var issueDto = podamFactory.manufacturePojo(IssueDto.class);

        Ticket ticket = mapper.mapToTicket(issueDto);

        assertThat(ticket).hasNoNullFieldsOrProperties();
        assertThat(ticket.getId()).isEqualTo(issueDto.getId().toString());
        assertThat(ticket.getSummary()).isEqualTo(issueDto.getTitle());
        assertThat(ticket.getStatus()).isEqualTo(issueDto.getState());
        assertThat(ticket.getTicketUrl()).isEqualTo(issueDto.getUrl().toString());
        assertThat(ticket.getPluginName()).isEqualTo("GitHub Plugin");
    }

    @Test
    void mapToIssueCreateRequestDto() {
        var gitHubIssue = podamFactory.manufacturePojo(GitHubIssue.class);

        IssuesCreateRequestDto actual = mapper.mapToIssueCreateRequestDto(gitHubIssue);

        assertThat(actual.getTitle()).isEqualTo(gitHubIssue.getTitle());
        assertThat(actual.getBody()).isEqualTo(gitHubIssue.getDescription());
        assertThat(actual.getAssignee()).isNull();
        assertThat(actual.getMilestone()).isEqualTo(gitHubIssue.getMilestone());
        assertThat(actual.getLabels()).isEqualTo(gitHubIssue.getLabels());
        assertThat(actual.getAssignees()).isEqualTo(gitHubIssue.getAssignees());
    }

}

package com.epam.reportportal.extension.github.provider.mapper;

import com.epam.reportportal.extension.github.command.GitHubIssueField;
import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.Map;

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
        List<PostFormField> availableFieldsMeta = getFieldsWithValues();

        var postTicketRQ = podamFactory.manufacturePojo(PostTicketRQ.class);
        postTicketRQ.setFields(availableFieldsMeta);

        IssuesCreateRequestDto actual = mapper.mapToIssueCreateRequestDto(postTicketRQ);

        assertThat(actual.getTitle()).isEqualTo("Bug found");
        assertThat(actual.getBody()).isEqualTo("body sample");
        assertThat(actual.getAssignee()).isNull();
        assertThat(actual.getMilestone()).isEqualTo("milestone 1");
        assertThat(actual.getLabels()).containsExactly("bug", "severe");
        assertThat(actual.getAssignees()).containsExactly("user1", "user2");
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

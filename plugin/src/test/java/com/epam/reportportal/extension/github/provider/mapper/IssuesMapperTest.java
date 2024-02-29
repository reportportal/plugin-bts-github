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

package com.epam.reportportal.extension.github.provider.mapper;

import com.epam.reportportal.extension.github.generated.dto.IssueDto;
import com.epam.reportportal.extension.github.generated.dto.IssuesCreateRequestDto;
import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author RiverSharks
 */
class IssuesMapperTest {
    private final PodamFactory podamFactory = new PodamFactoryImpl();
    private final IssuesMapper mapper = new IssuesMapper();

    @Test
    void mapToTicket_shouldMapIssueDtoToTicket() {
        var issueDto = podamFactory.manufacturePojo(IssueDto.class);

        Ticket ticket = mapper.mapToTicket(issueDto);

        assertThat(ticket).hasNoNullFieldsOrProperties();
        assertThat(ticket.getId()).isEqualTo(issueDto.getNumber().toString());
        assertThat(ticket.getSummary()).isEqualTo(issueDto.getTitle());
        assertThat(ticket.getStatus()).isEqualTo(issueDto.getState());
        assertThat(ticket.getTicketUrl()).isEqualTo(issueDto.getHtmlUrl().toString());
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

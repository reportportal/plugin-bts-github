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

/**
 * @author RiverSharks
 */
public class IssuesMapper {

    public Ticket mapToTicket(IssueDto issueDto) {
        Ticket ticket = new Ticket();

        ticket.setId(String.valueOf(issueDto.getNumber()));
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

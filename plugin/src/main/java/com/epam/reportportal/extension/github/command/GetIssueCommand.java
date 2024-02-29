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

package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.reportportal.extension.github.provider.rest.GitHubIssuesProviderFactory;
import com.epam.reportportal.extension.github.service.GitHubIssueService;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.TicketRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.epam.reportportal.extension.github.command.GitHubPropertyExtractor.GitHubProperties;

/**
 * @author pigorv
 */
@Slf4j
public class GetIssueCommand implements CommonPluginCommand<Ticket> {

    private final IssuesApi issuesApi;
    private final IssuesMapper issuesMapper;
    private final GitHubPropertyExtractor propertyExtractor;
    private final GitHubIssuesProviderFactory providerFactory;
    private final GitHubIssueService issueService;
    private final IntegrationRepository integrationRepository;
    private final TicketRepository ticketRepository;


    public GetIssueCommand(IntegrationRepository integrationRepository,
                           TicketRepository ticketRepository,
                           IssuesApi issuesApi,
                           IssuesMapper issuesMapper,
                           GitHubPropertyExtractor propertyExtractor,
                           GitHubIssuesProviderFactory providerFactory,
                           GitHubIssueService issueService) {
        this.integrationRepository = integrationRepository;
        this.ticketRepository = ticketRepository;
        this.issuesApi = issuesApi;
        this.issuesMapper = issuesMapper;
        this.propertyExtractor = propertyExtractor;
        this.providerFactory = providerFactory;
        this.issueService = issueService;
    }

    @Override
    public String getName() {
        return "getIssue";
    }

    @Override
    public Ticket executeCommand(Map<String, Object> params) {
        final var projectId = (Long) propertyExtractor.getRequiredParamWithoutCasting(params, GitHubProperty.PROJECT_ID);
        final String project = propertyExtractor.getRequiredParam(params, GitHubProperty.PROJECT);
        final String ticketId = propertyExtractor.getRequiredParam(params, GitHubProperty.TICKET_ID);
        final String url = propertyExtractor.getRequiredParam(params, GitHubProperty.URL);

        final Integration integration = integrationRepository.findProjectBtsByUrlAndLinkedProject(url, project, projectId)
                .orElseGet(() -> integrationRepository.findGlobalBtsByUrlAndLinkedProject(url, project)
                        .orElseThrow(() -> new ReportPortalException(ErrorType.BAD_REQUEST_ERROR,
                                "Integration with provided url and project isn't found")));

        final com.epam.ta.reportportal.entity.bts.Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ReportPortalException(
                        ErrorType.BAD_REQUEST_ERROR, "Ticket not found with id " + ticketId));

        final GitHubProperties props = propertyExtractor.fromMap(integration.getParams().getParams());
        final var issuesProvider = providerFactory.createProvider(
                issuesApi,
                issuesMapper,
                props.getOwner(),
                props.getProject(),
                props.getApiToken());

        return issueService.getIssue(issuesProvider, ticket.getTicketId());
    }
}

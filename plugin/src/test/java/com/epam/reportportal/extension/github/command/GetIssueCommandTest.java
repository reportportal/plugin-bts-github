package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.reportportal.extension.github.provider.rest.GitHubIssuesProviderFactory;
import com.epam.reportportal.extension.github.provider.rest.GitHubIssuesRestProvider;
import com.epam.reportportal.extension.github.service.GitHubIssueService;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.TicketRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetIssueCommandTest {
    @Mock
    private IssuesApi issuesApi;
    @Mock
    private IssuesMapper issuesMapper;
    @Mock
    private GitHubPropertyExtractor propertyExtractor;
    @Mock
    private GitHubIssuesProviderFactory providerFactory;
    @Mock
    private GitHubIssueService issueService;
    @Mock
    private IntegrationRepository integrationRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private GetIssueCommand getIssueCommand;

    @Test
    void executeCommand_shouldReturnIssue() {
        // given
        var url = "https://github.com";
        var project = "sample-project";
        var projectId = 1L;
        var ticketId = "5";
        var issueId = "3";
        var params = Map.<String, Object>of();
        Integration integration = buildIntegration();
        var storedTicket = new com.epam.ta.reportportal.entity.bts.Ticket();
        storedTicket.setTicketId(issueId);
        var gitHubProperties = GitHubPropertyExtractor.GitHubProperties.builder()
                .owner("sample-owner")
                .project(project)
                .apiToken("encryptedToken")
                .build();
        GitHubIssuesRestProvider providerMock = mock(GitHubIssuesRestProvider.class);
        Ticket expectedResult = new Ticket();

        // when
        when(propertyExtractor.getRequiredParamWithoutCasting(params, GitHubProperty.PROJECT_ID))
                .thenReturn(projectId);
        when(propertyExtractor.getRequiredParam(params, GitHubProperty.PROJECT))
                .thenReturn(project);
        when(propertyExtractor.getRequiredParam(params, GitHubProperty.URL))
                .thenReturn(url);
        when(propertyExtractor.getRequiredParam(params, GitHubProperty.TICKET_ID))
                .thenReturn(ticketId);
        when(integrationRepository.findProjectBtsByUrlAndLinkedProject(url, project, projectId))
                .thenReturn(Optional.of(integration));
        when(ticketRepository.findByTicketId(ticketId))
                .thenReturn(Optional.of(storedTicket));
        when(propertyExtractor.fromMap(integration.getParams().getParams()))
                .thenReturn(gitHubProperties);
        when(providerFactory
                .createProvider(
                        issuesApi,
                        issuesMapper,
                        gitHubProperties.getOwner(),
                        gitHubProperties.getProject(),
                        gitHubProperties.getApiToken()))
                .thenReturn(providerMock);
        when(issueService.getIssue(providerMock, issueId)).thenReturn(expectedResult);

        Ticket result = getIssueCommand.executeCommand(params);

        // then
        assertThat(result).isEqualTo(expectedResult);
        verify(propertyExtractor).getRequiredParamWithoutCasting(params, GitHubProperty.PROJECT_ID);
        verify(propertyExtractor).getRequiredParam(params, GitHubProperty.URL);
        verify(propertyExtractor).getRequiredParam(params, GitHubProperty.PROJECT);
        verify(propertyExtractor).getRequiredParam(params, GitHubProperty.TICKET_ID);
        verify(integrationRepository).findProjectBtsByUrlAndLinkedProject(url, project, projectId);
        verify(ticketRepository).findByTicketId(ticketId);
        verify(propertyExtractor).fromMap(integration.getParams().getParams());
        verify(providerFactory).createProvider(
                issuesApi,
                issuesMapper,
                gitHubProperties.getOwner(),
                gitHubProperties.getProject(),
                gitHubProperties.getApiToken());
        verify(issueService).getIssue(providerMock, issueId);
        verifyNoMoreInteractions(
                issuesApi,
                issuesMapper,
                propertyExtractor,
                providerFactory,
                issueService,
                integrationRepository,
                ticketRepository);
    }

    @Test
    void getName_shouldReturnCommandName() {
        assertThat(getIssueCommand.getName()).isEqualTo("getIssue");
    }

    private static Integration buildIntegration() {
        var integration = new Integration();
        var integrationParams = new IntegrationParams();
        integrationParams.setParams(Map.of());
        integration.setParams(integrationParams);
        return integration;
    }
}

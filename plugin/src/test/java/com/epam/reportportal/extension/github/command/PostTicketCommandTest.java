package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.github.entity.validator.RequestEntityValidatorWrapper;
import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.reportportal.extension.github.provider.rest.GitHubIssuesProviderFactory;
import com.epam.reportportal.extension.github.provider.rest.GitHubIssuesRestProvider;
import com.epam.reportportal.extension.github.service.GitHubIssueService;
import com.epam.reportportal.extension.util.CommandParamUtils;
import com.epam.reportportal.extension.util.RequestEntityConverter;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostTicketCommandTest {
    @Mock
    private RequestEntityConverter requestEntityConverter;
    @Mock
    private IssuesApi issuesApi;
    @Mock
    private IssuesMapper issuesMapper;
    @Mock
    private RequestEntityValidatorWrapper validator;
    @Mock
    private GitHubPropertyExtractor propertyExtractor;
    @Mock
    private GitHubIssuesProviderFactory providerFactory;
    @Mock
    private GitHubIssueService issueService;

    @InjectMocks
    private PostTicketCommand postTicketCommand;

    @Test
    void getName_shouldReturnCommandName() {
        assertThat(postTicketCommand.getName()).isEqualTo("postTicket");
    }

    @Test
    void invokeCommand_shouldCreateTicket() {
        var integration = new Integration();
        integration.setParams(new IntegrationParams(new HashMap<>()));
        var ticketRequest = new PostTicketRQ();
        ticketRequest.setFields(List.of(new PostFormField()));

        var gitHubProperties = GitHubPropertyExtractor.GitHubProperties.builder()
                .url("https://github.com")
                .project("sample-project")
                .apiToken("encryptedToken")
                .owner("sample-owner")
                .build();

        doNothing().when(validator).validatePostTicketRQ(any());
        when(requestEntityConverter.getEntity(any(), any(), any())).thenReturn(ticketRequest);
        when(propertyExtractor.fromMap(any())).thenReturn(gitHubProperties);
        GitHubIssuesRestProvider providerMock = mock(GitHubIssuesRestProvider.class);
        when(providerFactory.createProvider(any(), any(), any(), any(), any())).thenReturn(providerMock);
        var expectedTicket = new Ticket();
        when(issueService.createIssue(any(), any())).thenReturn(expectedTicket);

        Map<String, Object> params = Map.of();
        Ticket result = postTicketCommand.invokeCommand(integration, params);

        assertThat(result).isEqualTo(expectedTicket);

        verify(validator).validatePostTicketRQ(ticketRequest);
        verify(requestEntityConverter).getEntity(CommandParamUtils.ENTITY_PARAM, params, PostTicketRQ.class);
        verify(propertyExtractor).fromMap(integration.getParams().getParams());
        verify(providerFactory).createProvider(
                issuesApi,
                issuesMapper,
                gitHubProperties.getOwner(),
                gitHubProperties.getProject(),
                gitHubProperties.getApiToken());
        verify(issueService).createIssue(providerMock, ticketRequest);

        verifyNoMoreInteractions(validator, requestEntityConverter, propertyExtractor, providerFactory);
    }

}

package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.ProjectMemberCommand;
import com.epam.reportportal.extension.github.entity.validator.RequestEntityValidatorWrapper;
import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.reportportal.extension.github.provider.rest.GitHubIssuesProviderFactory;
import com.epam.reportportal.extension.github.service.GitHubIssueService;
import com.epam.reportportal.extension.util.CommandParamUtils;
import com.epam.reportportal.extension.util.RequestEntityConverter;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.epam.reportportal.extension.github.command.GitHubPropertyExtractor.GitHubProperties;

@Slf4j
public class PostTicketCommand extends ProjectMemberCommand<Ticket> {

    private final RequestEntityConverter requestEntityConverter;
    private final IssuesApi issuesApi;
    private final IssuesMapper issuesMapper;
    private final RequestEntityValidatorWrapper validator;
    private final GitHubPropertyExtractor propertyExtractor;
    private final GitHubIssuesProviderFactory providerFactory;
    private final GitHubIssueService issueService;


    public PostTicketCommand(ProjectRepository projectRepository,
                             RequestEntityConverter requestEntityConverter,
                             IssuesApi issuesApi,
                             IssuesMapper issuesMapper,
                             RequestEntityValidatorWrapper validator,
                             GitHubPropertyExtractor propertyExtractor,
                             GitHubIssuesProviderFactory providerFactory,
                             GitHubIssueService issueService) {
        super(projectRepository);
        this.requestEntityConverter = requestEntityConverter;
        this.issuesApi = issuesApi;
        this.issuesMapper = issuesMapper;
        this.validator = validator;
        this.propertyExtractor = propertyExtractor;
        this.providerFactory = providerFactory;
        this.issueService = issueService;
    }

    @Override
    public String getName() {
        return "postTicket";
    }

    @Override
    public Ticket invokeCommand(Integration integration, Map<String, Object> params) {
        PostTicketRQ ticketRequest =
                requestEntityConverter.getEntity(CommandParamUtils.ENTITY_PARAM, params, PostTicketRQ.class);
        validate(ticketRequest);

        GitHubProperties props = propertyExtractor.fromMap(integration.getParams().getParams());

        var issuesProvider = providerFactory.createProvider(
                issuesApi,
                issuesMapper,
                props.getOwner(),
                props.getProject(),
                props.getApiToken());

        return issueService.createIssue(issuesProvider, ticketRequest);
    }

    private void validate(PostTicketRQ ticketRequest) {
        validator.validatePostTicketRQ(ticketRequest);
    }

}

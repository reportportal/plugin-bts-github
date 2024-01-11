package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.ProjectMemberCommand;
import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.reportportal.extension.github.provider.rest.GitHubIssuesRestAsyncProvider;
import com.epam.reportportal.extension.github.util.GitHubPropertyUtils;
import com.epam.reportportal.extension.util.CommandParamUtils;
import com.epam.reportportal.extension.util.RequestEntityConverter;
import com.epam.reportportal.extension.util.RequestEntityValidator;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.apache.commons.collections4.CollectionUtils;
import org.jasypt.util.text.TextEncryptor;

import java.util.Map;

import static com.epam.reportportal.extension.github.command.GitHubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GitHubProperty.OWNER;
import static com.epam.reportportal.extension.github.command.GitHubProperty.PROJECT;
import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;
import static com.epam.ta.reportportal.ws.model.ErrorType.UNABLE_INTERACT_WITH_INTEGRATION;
import static java.util.function.Predicate.not;

public class PostTicketCommand extends ProjectMemberCommand<Ticket> {

    private final RequestEntityConverter requestEntityConverter;
    private final TextEncryptor textEncryptor;
    private final IssuesApi issuesApi;
    private final IssuesMapper issuesMapper;

    public PostTicketCommand(ProjectRepository projectRepository,
                             RequestEntityConverter requestEntityConverter,
                             TextEncryptor textEncryptor,
                             IssuesApi issuesApi,
                             IssuesMapper issuesMapper) {
        super(projectRepository);
        this.requestEntityConverter = requestEntityConverter;
        this.textEncryptor = textEncryptor;
        this.issuesApi = issuesApi;
        this.issuesMapper = issuesMapper;
    }

    @Override
    public String getName() {
        return "postTicket";
    }

    @Override
    protected Ticket invokeCommand(Integration integration, Map<String, Object> params) {
        PostTicketRQ ticketRequest =
                requestEntityConverter.getEntity(CommandParamUtils.ENTITY_PARAM, params, PostTicketRQ.class);
        RequestEntityValidator.validate(ticketRequest);
        expect(ticketRequest.getFields(), not(CollectionUtils::isEmpty)).verify(UNABLE_INTERACT_WITH_INTEGRATION,
                "External System fields set is empty!");

        Map<String, Object> integrationParams = integration.getParams().getParams();

        String token = textEncryptor.decrypt(GitHubPropertyUtils.getParam(integrationParams, API_TOKEN));
        String owner = GitHubPropertyUtils.getParam(integrationParams, OWNER);
        String project = GitHubPropertyUtils.getParam(integrationParams, PROJECT);

        var issuesProvider = new GitHubIssuesRestAsyncProvider(issuesApi, issuesMapper, owner, project, token);

        return issuesProvider.createIssue(ticketRequest);
    }


}

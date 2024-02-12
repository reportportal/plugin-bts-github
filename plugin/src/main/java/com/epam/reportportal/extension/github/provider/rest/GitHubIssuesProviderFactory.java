package com.epam.reportportal.extension.github.provider.rest;

import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.provider.GitHubIssuesProvider;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;

public class GitHubIssuesProviderFactory {

    public GitHubIssuesProvider createProvider(IssuesApi issuesApi,
                                               IssuesMapper issueMapper,
                                               String owner,
                                               String project,
                                               String apiToken) {

        return new GitHubIssuesRestProvider(issuesApi, issueMapper, owner, project, apiToken);
    }
}

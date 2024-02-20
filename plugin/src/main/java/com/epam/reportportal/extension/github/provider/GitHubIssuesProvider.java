package com.epam.reportportal.extension.github.provider;

import com.epam.reportportal.extension.github.model.GitHubIssue;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;


public interface GitHubIssuesProvider {

    Ticket createIssue(GitHubIssue ticketRequest);

    Ticket getIssue(String issueId);
}

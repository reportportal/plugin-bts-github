package com.epam.reportportal.extension.github.provider;

import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;


public interface GitHubIssuesProvider {

    Ticket createIssue(PostTicketRQ ticketRequest);
}

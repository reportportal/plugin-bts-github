package com.epam.reportportal.extension.github.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum GitHubProperty {
    PROJECT("project", "BTS project"),
    PROJECT_ID("projectId", "BTS project id"),
    URL("url", "BTS url"),
    API_TOKEN("apiToken", "Access token"),
    OWNER("owner", "BTS repository owner"),
    TICKET_ID("ticketId", "Ticket id");

    private final String name;
    private final String title;
}

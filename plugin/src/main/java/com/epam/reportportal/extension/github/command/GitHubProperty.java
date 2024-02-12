package com.epam.reportportal.extension.github.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum GitHubProperty {
    PROJECT("project", "BTS project"),
    URL("url", "BTS url"),
    API_TOKEN("apiToken", "Access token"),
    OWNER("owner", "BTS repository owner");

    private final String name;
    private final String title;
}

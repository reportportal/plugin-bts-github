package com.epam.reportportal.extension.github.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum GithubProperty {
    PROJECT("project", "BTS project"),
    API_TOKEN("apiToken", "Access token"),
    URL("url", "BTS url");

    private final String name;
    private final String title;
}

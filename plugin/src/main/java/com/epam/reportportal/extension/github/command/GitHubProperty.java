/*
 * Copyright 2023 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.epam.reportportal.extension.github.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author RiverSharks
 */
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

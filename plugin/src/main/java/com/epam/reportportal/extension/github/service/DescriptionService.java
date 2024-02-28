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

package com.epam.reportportal.extension.github.service;

import com.epam.ta.reportportal.dao.TestItemRepository;
import com.epam.ta.reportportal.entity.item.TestItem;
import com.epam.ta.reportportal.entity.item.TestItemResults;
import com.epam.ta.reportportal.entity.item.issue.IssueEntity;
import com.epam.ta.reportportal.entity.log.Log;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author RiverSharks
 */
@RequiredArgsConstructor
public class DescriptionService {
    private static final String DESCRIPTION_HEADING = "## Description:\n";
    private static final String BACKLINK_HEADING = "## Back links to Report Portal:\n";
    private static final String COMMENTS_HEADING = "## Comments:\n";
    private static final String LOGS_HEADING = "## Logs:\n";
    private static final String BACK_LINK_PATTERN = "[Link to defect](%s)%n";
    private final TestItemRepository testItemRepository;


    public String buildDescriptionString(PostTicketRQ ticketRQ, String additionalDescription) {

        Optional<TestItem> testItemOptional = testItemRepository.findById(ticketRQ.getTestItemId());

        if (testItemOptional.isEmpty()) {
            return StringUtils.EMPTY;
        }

        TestItem testItem = testItemOptional.get();
        var descriptionBuilder = new StringBuilder();
        descriptionBuilder.append(BACKLINK_HEADING);
        ticketRQ.getBackLinks().entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getKey))
                .forEach(entry -> descriptionBuilder
                .append(" - ")
                .append(String.format(BACK_LINK_PATTERN, entry.getValue())));
        descriptionBuilder.append(DESCRIPTION_HEADING);
        Optional.ofNullable(additionalDescription)
                .ifPresent(v -> {
                    descriptionBuilder.append(v);
                    descriptionBuilder.append("\n");
                });
        descriptionBuilder.append(testItem.getDescription());
        descriptionBuilder.append("\n\n");

        if (ticketRQ.getIsIncludeComments()) {
            descriptionBuilder.append(COMMENTS_HEADING);
            Optional.ofNullable(testItem.getItemResults())
                    .map(TestItemResults::getIssue)
                    .map(IssueEntity::getIssueDescription)
                    .ifPresent(descriptionBuilder::append);
            descriptionBuilder.append("\n\n");
        }

        if (ticketRQ.getIsIncludeLogs()) {
            descriptionBuilder.append(LOGS_HEADING);
            String logs = testItem.getLogs()
                    .stream()
                    .map(Log::getLogMessage)
                    .collect(Collectors.joining("\n\n"));
            Optional.of(logs)
                    .filter(StringUtils::isNotBlank)
                    .ifPresent(logsString -> {
                        descriptionBuilder.append(logs);
                    });
            descriptionBuilder.append("\n\n");
        }

        return descriptionBuilder.toString();
    }
}

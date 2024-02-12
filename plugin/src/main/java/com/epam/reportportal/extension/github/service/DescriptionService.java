package com.epam.reportportal.extension.github.service;

import com.epam.ta.reportportal.dao.TestItemRepository;
import com.epam.ta.reportportal.entity.item.TestItem;
import com.epam.ta.reportportal.entity.item.TestItemResults;
import com.epam.ta.reportportal.entity.item.issue.IssueEntity;
import com.epam.ta.reportportal.entity.log.Log;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DescriptionService {
    private static final String DESCRIPTION_HEADING = "## Description:\n";
    private static final String COMMENTS_HEADING = "## Comments:\n";
    private static final String LOGS_HEADING = "## Logs:\n";
    private final TestItemRepository testItemRepository;


    public String buildDescriptionString(PostTicketRQ ticketRQ, String additionalDescription) {

        Optional<TestItem> testItemOptional = testItemRepository.findById(ticketRQ.getTestItemId());

        if (testItemOptional.isEmpty()) {
            return StringUtils.EMPTY;
        }

        TestItem testItem = testItemOptional.get();
        var descriptionBuilder = new StringBuilder();
        descriptionBuilder.append(DESCRIPTION_HEADING);
        descriptionBuilder.append(additionalDescription);
        descriptionBuilder.append("\n");
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

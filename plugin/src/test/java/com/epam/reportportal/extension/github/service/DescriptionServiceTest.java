package com.epam.reportportal.extension.github.service;

import com.epam.ta.reportportal.dao.TestItemRepository;
import com.epam.ta.reportportal.entity.item.TestItem;
import com.epam.ta.reportportal.entity.item.TestItemResults;
import com.epam.ta.reportportal.entity.item.issue.IssueEntity;
import com.epam.ta.reportportal.entity.log.Log;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DescriptionServiceTest {
    @Mock
    private TestItemRepository testItemRepository;
    @InjectMocks
    private DescriptionService descriptionService;

    @Test
    void buildDescriptionString_shouldReturnEmptyString_whenTestItemNotFound() {
        var ticketRQ = new PostTicketRQ();
        ticketRQ.setTestItemId(1L);

        when(testItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        String descriptionString = descriptionService.buildDescriptionString(ticketRQ);

        assertThat(descriptionString).isEmpty();
    }

    @Test
    void buildDescriptionString_shouldBuildDescription_whenTestItemFound() {
        var ticketRQ = new PostTicketRQ();
        ticketRQ.setTestItemId(1L);
        ticketRQ.setIsIncludeComments(true);
        ticketRQ.setIsIncludeLogs(true);

        TestItem testItem = getTestItem();

        when(testItemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));

        String descriptionString = descriptionService.buildDescriptionString(ticketRQ);

        assertThat(descriptionString)
                .isNotEmpty()
                .contains("## Description:\n" +
                        "Test description\n" +
                        "\n" +
                        "## Comments:\n" +
                        "Test issue description\n" +
                        "\n" +
                        "## Logs:\n" +
                        "Log message 2\n" +
                        "\n" +
                        "Log message 1\n" +
                        "\n"
                );
    }

    @Test
    void buildDescriptionString_shouldNotIncludeCommentsAndLogs_whenIncludeCommentsAndLogsFalse() {
        var ticketRQ = new PostTicketRQ();
        ticketRQ.setTestItemId(1L);
        ticketRQ.setIsIncludeComments(false);
        ticketRQ.setIsIncludeLogs(false);

        var testItem = new TestItem();
        testItem.setDescription("Test description");

        when(testItemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));

        String descriptionString = descriptionService.buildDescriptionString(ticketRQ);

        assertThat(descriptionString)
                .isNotEmpty()
                .contains("## Description:\n" +
                        "Test description\n\n"
                );
    }

    private static TestItem getTestItem() {
        var testItem = new TestItem();
        testItem.setDescription("Test description");

        var issueEntity = new IssueEntity();
        issueEntity.setIssueDescription("Test issue description");
        var testItemResults = new TestItemResults();
        testItemResults.setIssue(issueEntity);
        testItem.setItemResults(testItemResults);
        var log1 = new Log();
        log1.setLogMessage("Log message 1");
        var log2 = new Log();
        log2.setLogMessage("Log message 2");
        testItem.setLogs(Set.of(log1, log2));
        return testItem;
    }
}

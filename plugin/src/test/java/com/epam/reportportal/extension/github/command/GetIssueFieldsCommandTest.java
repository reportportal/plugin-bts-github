package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetIssueFieldsCommandTest {

    private final GetIssueFieldsCommand command = new GetIssueFieldsCommand(null);

    @Test
    void getName_getName_shouldReturnCommandName() {
        assertThat(command.getName()).isEqualTo("getIssueFields");
    }

    @Test
    void invokeCommand_shouldReturnIssueFieldsMetadata() {

        List<PostFormField> actual = command.invokeCommand(null, null);

        assertThat(actual).isEqualTo(GitHubIssueField.getAvailableFieldsMeta());
    }
}

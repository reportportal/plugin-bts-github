package com.epam.reportportal.extension.github.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class GetIssueTypesCommandTest {

  private final GetIssueTypesCommand command = new GetIssueTypesCommand(null);

  @Test
  void getName_shouldReturnCommandName() {
    String name = command.getName();

    assertThat(name).isEqualTo("getIssueTypes");
  }

  @Test
  void invokeCommand_shouldReturnIssueTypes() {
    List<String> issueTypes = command.invokeCommand(null, null);

    assertThat(issueTypes).containsExactly("Issue");
  }
}

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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author RiverSharks
 */
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

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

import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author RiverSharks
 */
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

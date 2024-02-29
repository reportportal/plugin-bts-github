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

import com.epam.reportportal.extension.ProjectMemberCommand;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.integration.Integration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author RiverSharks
 */
public class GetIssueTypesCommand extends ProjectMemberCommand<List<String>> {

    public GetIssueTypesCommand(ProjectRepository projectRepository) {
        super(projectRepository);
    }

    @Override
    public String getName() {
        return "getIssueTypes";
    }

    @Override
    protected List<String> invokeCommand(Integration integration, Map<String, Object> map) {
        return Arrays.stream(IssueType.values()).map(IssueType::getName).collect(Collectors.toList());
    }
}

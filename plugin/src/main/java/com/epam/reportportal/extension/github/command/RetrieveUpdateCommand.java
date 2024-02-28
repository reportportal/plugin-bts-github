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

import com.epam.reportportal.extension.CommonPluginCommand;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.reportportal.extension.github.command.GitHubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GitHubProperty.OWNER;
import static com.epam.reportportal.extension.github.command.GitHubProperty.PROJECT;
import static com.epam.reportportal.extension.github.command.GitHubProperty.URL;

/**
 * @author RiverSharks
 */
@RequiredArgsConstructor
public class RetrieveUpdateCommand implements CommonPluginCommand<Map<String, Object>> {
    private final GitHubPropertyExtractor propertyExtractor;

    @Override
    public String getName() {
        return "retrieveUpdated";
    }

    @Override
    public Map<String, Object> executeCommand(Map<String, Object> params) {
        return Stream.of(Map.entry(PROJECT.getName(), propertyExtractor.getOptionalParam(params, PROJECT)),
                        Map.entry(OWNER.getName(), propertyExtractor.getOptionalParam(params, OWNER)),
                        Map.entry(API_TOKEN.getName(), propertyExtractor.getOptionalParamEncrypted(params, API_TOKEN)),
                        Map.entry(URL.getName(), propertyExtractor.getOptionalParam(params, URL))
                )
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }
}

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
import com.epam.ta.reportportal.ws.model.ErrorType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

import static com.epam.reportportal.extension.github.command.GitHubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GitHubProperty.OWNER;
import static com.epam.reportportal.extension.github.command.GitHubProperty.PROJECT;
import static com.epam.reportportal.extension.github.command.GitHubProperty.URL;
import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;


/**
 * @author RiverSharks
 */
@RequiredArgsConstructor
public class RetrieveCreateParamsCommand implements CommonPluginCommand<Map<String, Object>> {

    private static final String NO_PARAMS_PROVIDED_MSG = "No integration params provided";
    private final GitHubPropertyExtractor propertyExtractor;

    @Override
    public String getName() {
        return "retrieveCreate";
    }

    @Override
    public Map<String, Object> executeCommand(Map<String, Object> params) {
        expect(params, MapUtils::isNotEmpty).verify(ErrorType.BAD_REQUEST_ERROR, NO_PARAMS_PROVIDED_MSG);
        return Map.of(
                PROJECT.getName(), propertyExtractor.getRequiredParam(params, PROJECT),
                OWNER.getName(), propertyExtractor.getRequiredParam(params, OWNER),
                API_TOKEN.getName(), propertyExtractor.getRequiredParamEncrypted(params, API_TOKEN),
                URL.getName(), propertyExtractor.getRequiredParam(params, URL)
        );
    }

}

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

package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.TextEncryptor;

import java.util.Map;
import java.util.Optional;

import static com.epam.reportportal.extension.github.command.GithubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GithubProperty.OWNER;
import static com.epam.reportportal.extension.github.command.GithubProperty.PROJECT;
import static com.epam.reportportal.extension.github.command.GithubProperty.URL;
import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;
import static com.epam.ta.reportportal.ws.model.ErrorType.UNABLE_INTERACT_WITH_INTEGRATION;


@RequiredArgsConstructor
public class RetrieveCreateParamsCommand implements CommonPluginCommand<Map<String, Object>> {

    private static final String NO_PARAMS_PROVIDED_MSG = "No integration params provided";
    private final TextEncryptor encryptor;

    @Override
    public String getName() {
        return "retrieveCreate";
    }

    @Override
    public Map<String, Object> executeCommand(Map<String, Object> params) {

        expect(params, MapUtils::isNotEmpty).verify(ErrorType.BAD_REQUEST_ERROR, NO_PARAMS_PROVIDED_MSG);

        String project = getParam(params, PROJECT);
        String owner = getParam(params, OWNER);
        String url = getParam(params, URL);
        String encryptedToken = encryptor.encrypt(getParam(params, API_TOKEN));

        return Map.of(
                PROJECT.getName(), project,
                OWNER.getName(), owner,
                API_TOKEN.getName(), encryptedToken,
                URL.getName(), url
        );
    }

    private static String getParam(Map<String, Object> params, GithubProperty property) {
        return Optional.ofNullable(params.get(property.getName()))
                .map(obj -> (String) obj)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> buildException(property));
    }

    private static ReportPortalException buildException(GithubProperty property) {
        return new ReportPortalException(UNABLE_INTERACT_WITH_INTEGRATION,
                String.format("%s is not specified.", property.getTitle()));
    }
}

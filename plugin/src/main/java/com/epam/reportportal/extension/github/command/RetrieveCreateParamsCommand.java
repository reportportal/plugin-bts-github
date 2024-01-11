package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.reportportal.extension.github.util.GitHubPropertyUtils;
import com.epam.ta.reportportal.ws.model.ErrorType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.jasypt.util.text.TextEncryptor;

import java.util.Map;

import static com.epam.reportportal.extension.github.command.GitHubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GitHubProperty.OWNER;
import static com.epam.reportportal.extension.github.command.GitHubProperty.PROJECT;
import static com.epam.reportportal.extension.github.command.GitHubProperty.URL;
import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;


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

        String project = GitHubPropertyUtils.getParam(params, PROJECT);
        String owner = GitHubPropertyUtils.getParam(params, OWNER);
        String url = GitHubPropertyUtils.getParam(params, URL);
        String encryptedToken = encryptor.encrypt(GitHubPropertyUtils.getParam(params, API_TOKEN));

        return Map.of(
                PROJECT.getName(), project,
                OWNER.getName(), owner,
                API_TOKEN.getName(), encryptedToken,
                URL.getName(), url
        );
    }


}

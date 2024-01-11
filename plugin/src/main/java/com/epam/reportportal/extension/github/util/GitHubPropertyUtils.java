package com.epam.reportportal.extension.github.util;

import com.epam.reportportal.extension.github.command.GitHubProperty;
import com.epam.ta.reportportal.exception.ReportPortalException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.epam.ta.reportportal.ws.model.ErrorType.UNABLE_INTERACT_WITH_INTEGRATION;

public class GitHubPropertyUtils {

    public static String getParam(Map<String, Object> params, GitHubProperty property) {
        return Optional.ofNullable(params.get(property.getName()))
                .map(obj -> (String) obj)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> buildException(property));
    }

    private static ReportPortalException buildException(GitHubProperty property) {
        return new ReportPortalException(UNABLE_INTERACT_WITH_INTEGRATION,
                String.format("%s is not specified.", property.getTitle()));
    }
}

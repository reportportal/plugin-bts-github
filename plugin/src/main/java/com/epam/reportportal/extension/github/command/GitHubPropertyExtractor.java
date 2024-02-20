package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.exception.ReportPortalException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.TextEncryptor;

import java.util.Map;
import java.util.Optional;

import static com.epam.reportportal.extension.github.command.GitHubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GitHubProperty.OWNER;
import static com.epam.reportportal.extension.github.command.GitHubProperty.PROJECT;
import static com.epam.reportportal.extension.github.command.GitHubProperty.URL;
import static com.epam.ta.reportportal.ws.model.ErrorType.UNABLE_INTERACT_WITH_INTEGRATION;

@RequiredArgsConstructor
public class GitHubPropertyExtractor {

    private final TextEncryptor textEncryptor;

    public GitHubProperties fromMap(Map<String, ?> map) {
        return GitHubProperties.builder()
                .url(getRequiredParam(map, URL))
                .project(getRequiredParam(map, PROJECT))
                .owner(getRequiredParam(map, OWNER))
                .apiToken(getRequiredParamDecrypted(map, API_TOKEN))
                .build();
    }

    public String getRequiredParam(Map<String, ?> params, GitHubProperty property) {
        return getOptionalParam(params, property)
                .orElseThrow(() -> buildException(property));
    }

    public Optional<String> getOptionalParam(Map<String, ?> params, GitHubProperty property) {
        return Optional.ofNullable(params.get(property.getName()))
                .map(obj -> (String) obj)
                .filter(StringUtils::isNotBlank);
    }

     public Object getRequiredParamWithoutCasting(Map<String, ?> params, GitHubProperty property) {
        return Optional.ofNullable(params.get(property.getName()))
                .orElseThrow(() -> buildException(property));
    }

    public String getRequiredParamDecrypted(Map<String, ?> params, GitHubProperty property) {
        return textEncryptor.decrypt(getRequiredParam(params, property));
    }

    public String getRequiredParamEncrypted(Map<String, ?> params, GitHubProperty property) {
        return textEncryptor.encrypt(getRequiredParam(params, property));
    }

    public Optional<String> getOptionalParamEncrypted(Map<String, ?> params, GitHubProperty property) {
        return getOptionalParam(params, property)
                .map(textEncryptor::encrypt);
    }

    private static ReportPortalException buildException(GitHubProperty property) {
        return new ReportPortalException(UNABLE_INTERACT_WITH_INTEGRATION,
                String.format("%s is not specified.", property.getTitle()));
    }

    @Data
    @Builder
    public static class GitHubProperties {
        private String url;
        private String project;
        private String apiToken;
        private String owner;
    }
}

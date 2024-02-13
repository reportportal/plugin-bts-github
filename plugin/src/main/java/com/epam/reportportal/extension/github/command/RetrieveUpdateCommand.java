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

@RequiredArgsConstructor
public class RetrieveUpdateCommand implements CommonPluginCommand<Map<String, Object>> {
    private final GitHubPropertyExtractor propertyExtractor;

    @Override
    public String getName() {
        return "retrieveUpdate";
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

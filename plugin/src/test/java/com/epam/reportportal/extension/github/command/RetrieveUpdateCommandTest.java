package com.epam.reportportal.extension.github.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveUpdateCommandTest {
    private static final String PROJECT = "project";
    private static final String URL = "url";
    private static final String API_TOKEN = "apiToken";
    private static final String OWNER = "owner";

    @Mock
    private GitHubPropertyExtractor propertyExtractor;
    @InjectMocks
    private RetrieveUpdateCommand command;

    @Test
    void getName_shouldReturnCommandName() {
        assertThat(command.getName()).isEqualTo("retrieveUpdate");
    }

    @Test
    void executeCommand_shouldReturnParams_whenAllParamsProvidedInInput() {
        var owner = "reportportal";
        var project = "project name";
        var url = "example.com";
        var apiToken = "3rrewdf";
        Map<String, Object> inputParams = Map.of(OWNER, owner,
                URL, url,
                PROJECT, project,
                API_TOKEN, apiToken
        );
        var encryptedToken = "encrypted";

        when(propertyExtractor.getOptionalParam(anyMap(), any())).thenAnswer(invocation -> {
            // answer with value from map
            String key = ((GitHubProperty) invocation.getArguments()[1]).getName();
            return Optional.ofNullable(inputParams.get(key));
        });
        when(propertyExtractor.getOptionalParamEncrypted(anyMap(), any())).thenReturn(Optional.of(encryptedToken));


        Map<String, Object> actual = command.executeCommand(inputParams);

        assertThat(actual)
                .hasEntrySatisfying(OWNER, obj -> assertThat(obj).isEqualTo(owner))
                .hasEntrySatisfying(URL, obj -> assertThat(obj).isEqualTo(url))
                .hasEntrySatisfying(PROJECT, obj -> assertThat(obj).isEqualTo(project))
                .hasEntrySatisfying(API_TOKEN, obj -> assertThat(obj).isEqualTo(encryptedToken));

        verify(propertyExtractor, times(3)).getOptionalParam(eq(inputParams), any());
        verify(propertyExtractor).getOptionalParamEncrypted(eq(inputParams), any());
    }

    @Test
    void executeCommand_shouldReturnOnlyPresentParams_whenNotAllParamsProvidedInInput() {
        var owner = "reportportal";
        Map<String, Object> inputParams = Map.of(OWNER, owner);

        when(propertyExtractor.getOptionalParam(anyMap(), any())).thenAnswer(invocation -> {
            // answer with value from map
            String key = ((GitHubProperty) invocation.getArguments()[1]).getName();
            return Optional.ofNullable(inputParams.get(key));
        });

        Map<String, Object> actual = command.executeCommand(inputParams);

        assertThat(actual)
                .hasSize(1)
                .hasEntrySatisfying(OWNER, obj -> assertThat(obj).isEqualTo(owner));

        verify(propertyExtractor, times(3)).getOptionalParam(eq(inputParams), any());
        verify(propertyExtractor).getOptionalParamEncrypted(eq(inputParams), any());
    }
}

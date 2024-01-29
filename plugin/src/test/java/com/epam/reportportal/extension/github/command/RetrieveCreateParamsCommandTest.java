package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.exception.ReportPortalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveCreateParamsCommandTest {
    private static final String PROJECT = "project";
    private static final String URL = "url";
    private static final String API_TOKEN = "apiToken";
    private static final String OWNER = "owner";
    @Mock
    private GitHubPropertyExtractor gitHubPropertyExtractor;
    @InjectMocks
    private RetrieveCreateParamsCommand command;

    @Test
    void getName_shouldReturnCommandName() {
        assertThat(command.getName()).isEqualTo("retrieveCreate");
    }

    @Test
    void executeCommand_shouldReturnGithubProperties() {
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

        when(gitHubPropertyExtractor.getRequiredParam(anyMap(), any())).thenAnswer(invocation -> {
            // answer with value from map
            String key = ((GitHubProperty) invocation.getArguments()[1]).getName();
            return inputParams.get(key);
        });
        when(gitHubPropertyExtractor.getRequiredParamEncrypted(anyMap(), any())).thenReturn(encryptedToken);


        Map<String, Object> githubProperties = command.executeCommand(inputParams);

        assertThat(githubProperties)
                .hasEntrySatisfying(OWNER, obj -> assertThat(obj).isEqualTo(owner))
                .hasEntrySatisfying(URL, obj -> assertThat(obj).isEqualTo(url))
                .hasEntrySatisfying(PROJECT, obj -> assertThat(obj).isEqualTo(project))
                .hasEntrySatisfying(API_TOKEN, obj -> assertThat(obj).isEqualTo(encryptedToken));

        verify(gitHubPropertyExtractor, times(3)).getRequiredParam(eq(inputParams), any());
        verify(gitHubPropertyExtractor).getRequiredParamEncrypted(eq(inputParams), any());
    }

    @Test
    void executeCommand_shouldThrowError_whenNoParametersProvided() {
        assertThatThrownBy(() -> command.executeCommand(Map.of()))
                .isExactlyInstanceOf(ReportPortalException.class)
                .hasMessage("Error in handled Request. Please, check specified parameters: " +
                        "'No integration params provided'");
    }
}

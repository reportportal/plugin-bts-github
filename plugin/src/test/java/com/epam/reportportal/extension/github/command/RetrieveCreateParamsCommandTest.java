package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.exception.ReportPortalException;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveCreateParamsCommandTest {
    public static final String PROJECT = "project";
    public static final String API_TOKEN = "apiToken";
    public static final String URL = "url";
    @Mock
    private BasicTextEncryptor encryptor;
    @InjectMocks
    private RetrieveCreateParamsCommand command;

    @Test
    void getName_shouldReturnCommandName() {
        assertThat(command.getName()).isEqualTo("retrieveCreate");
    }

    @Test
    void executeCommand_shouldReturnGithubProperties() {
        var url = "example.com";
        var project = "project name";
        var apiToken = "3rrewdf";
        Map<String, Object> inputParams = Map.of(URL, url,
                PROJECT, project,
                API_TOKEN, apiToken
        );
        var encryptedToken = "encrypted";
        when(encryptor.encrypt(anyString())).thenReturn(encryptedToken);

        Map<String, Object> githubProperties = command.executeCommand(inputParams);

        assertThat(githubProperties)
                .hasEntrySatisfying(URL, obj -> assertThat(obj).isEqualTo(url))
                .hasEntrySatisfying(PROJECT, obj -> assertThat(obj).isEqualTo(project))
                .hasEntrySatisfying(API_TOKEN, obj -> assertThat(obj).isEqualTo(encryptedToken));

        verify(encryptor, only()).encrypt(apiToken);
    }

    @ParameterizedTest
    @MethodSource("missingParamsSource")
    void executeCommand_shouldThrowError_whenNoParamProvided(Map<String, Object> params,
                                                                   String expectedErrorMsg) {
        assertThatThrownBy(() -> command.executeCommand(params))
                .isExactlyInstanceOf(ReportPortalException.class)
                .hasMessageContaining(expectedErrorMsg);
    }

    static Stream<Arguments> missingParamsSource() {
        var url = "example.com";
        var project = "project name";
        var apiToken = "3rrewdf";

        var tokenNotSpecified = "Access token is not specified.";
        var projectNotSpecified = "BTS project is not specified.";
        var urlNotSpecified = "BTS url is not specified.";

        var blankString = "  ";
        return Stream.of(
                Arguments.of(Map.of(
                        PROJECT, project,
                        API_TOKEN, apiToken
                ), urlNotSpecified),
                Arguments.of(Map.of(
                        URL, blankString,
                        PROJECT, project,
                        API_TOKEN, apiToken
                ), urlNotSpecified),
                Arguments.of(Map.of(
                        URL, url,
                        API_TOKEN, apiToken
                ), projectNotSpecified),
                Arguments.of(Map.of(
                        URL, url,
                        PROJECT, blankString,
                        API_TOKEN, apiToken
                ), projectNotSpecified),
                Arguments.of(Map.of(
                        URL, url,
                        PROJECT, project
                ), tokenNotSpecified),
                Arguments.of(Map.of(
                        URL, url,
                        PROJECT, project,
                        API_TOKEN, blankString
                ), tokenNotSpecified)
        );
    }
}

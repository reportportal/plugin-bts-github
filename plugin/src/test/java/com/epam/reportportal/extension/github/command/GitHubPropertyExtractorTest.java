package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.exception.ReportPortalException;
import org.jasypt.util.text.TextEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.epam.reportportal.extension.github.command.GitHubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GitHubProperty.PROJECT_ID;
import static com.epam.reportportal.extension.github.command.GitHubProperty.TICKET_ID;
import static com.epam.reportportal.extension.github.command.GitHubProperty.URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubPropertyExtractorTest {

    @Mock
    private TextEncryptor textEncryptor;
    @InjectMocks
    private GitHubPropertyExtractor propertyExtractor;

    @Test
    void fromMap_shouldReturnProperties_whenAllParamsProvided() {
        var url = "https://github.com";
        var project = "sample-project";
        var owner = "sample-owner";
        var encryptedToken = "encryptedToken";

        Map<String, Object> params = new HashMap<>();
        params.put("url", url);
        params.put("project", project);
        params.put("owner", owner);
        params.put("apiToken", encryptedToken);

        var decryptedToken = "decryptedToken";
        when(textEncryptor.decrypt(encryptedToken)).thenReturn(decryptedToken);

        GitHubPropertyExtractor.GitHubProperties result = propertyExtractor.fromMap(params);

        assertThat(result.getUrl()).isEqualTo(url);
        assertThat(result.getProject()).isEqualTo(project);
        assertThat(result.getOwner()).isEqualTo(owner);
        assertThat(result.getApiToken()).isEqualTo(decryptedToken);
    }

    @Test
    void getRequiredParam_shouldThrowException_whenNoRequiredParamProvided() {
        assertThatThrownBy(() -> propertyExtractor.getRequiredParam(Map.of(), URL))
                .isExactlyInstanceOf(ReportPortalException.class)
                .hasMessageContaining(URL.getTitle() + " is not specified");
    }

    @Test
    void getRequiredParamDecrypted_shouldReturnDecrypted() {
        var decryptedToken = "decryptedToken";
        when(textEncryptor.decrypt(anyString())).thenReturn(decryptedToken);

        var encryptedToken = "encryptedToken";
        String actual =
                propertyExtractor.getRequiredParamDecrypted(Map.of("apiToken", encryptedToken), API_TOKEN);

        assertThat(actual).isEqualTo(decryptedToken);
    }

    @Test
    void getRequiredParamEncrypted_shouldReturnEncrypted() {
        var encrypted = "encrypted";
        when(textEncryptor.encrypt(anyString())).thenReturn(encrypted);

        var token = "raw";
        String actual =
                propertyExtractor.getRequiredParamEncrypted(Map.of("apiToken", token), API_TOKEN);

        assertThat(actual).isEqualTo(encrypted);
    }

    @Test
    void getRequiredParamWithoutCasting_shouldThrowException_whenNoRequiredParamProvided() {
        assertThatThrownBy(() -> propertyExtractor.getRequiredParamWithoutCasting(Map.of(), URL))
                .isExactlyInstanceOf(ReportPortalException.class)
                .hasMessageContaining(URL.getTitle() + " is not specified");
    }

    @Test
    void getRequiredParamWithoutCasting_shouldReturnValueWithoutAsObject() {
        var longValue = 1L;
        var stringValue = "string";
        var params = Map.of(
                PROJECT_ID.getName(), longValue,
                TICKET_ID.getName(), stringValue
        );

        Object projectId = propertyExtractor.getRequiredParamWithoutCasting(params, PROJECT_ID);
        Object ticketId = propertyExtractor.getRequiredParamWithoutCasting(params, TICKET_ID);

        assertThat(projectId).isExactlyInstanceOf(Long.class);
        assertThat(ticketId).isExactlyInstanceOf(String.class);
    }
}

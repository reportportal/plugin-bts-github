/*
 * Copyright 2023 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.epam.reportportal.extension.github.command;

import com.epam.ta.reportportal.exception.ReportPortalException;
import org.jasypt.util.text.TextEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.epam.reportportal.extension.github.command.GitHubProperty.API_TOKEN;
import static com.epam.reportportal.extension.github.command.GitHubProperty.PROJECT_ID;
import static com.epam.reportportal.extension.github.command.GitHubProperty.TICKET_ID;
import static com.epam.reportportal.extension.github.command.GitHubProperty.URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author RiverSharks
 */
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
    void getOptionalParam_shouldReturnOptionalWithValue_whenValueIsPresentInMapByKey() {
        var url = "https://github.com";

        Optional<String> actual =
                propertyExtractor.getOptionalParam(Map.of("url", url), URL);

        assertThat(actual).isNotEmpty().get().isEqualTo(url);
    }

    @ParameterizedTest
    @MethodSource("missingParamSource")
    void getOptionalParam_shouldReturnOptionalWithValue_whenIsNotPresentInMapByKeyOrBlank(Map<String, Object> params) {
        Optional<String> actual =
                propertyExtractor.getOptionalParam(params, URL);

        assertThat(actual).isEmpty();
    }

    static Stream<Arguments> missingParamSource() {
        return Stream.of(
                Arguments.of(Map.of("url", " ")),
                Arguments.of(Map.of())
        );
    }

    @Test
    void getOptionalParamEncrypted_shouldReturnOptionalWithEncryptedValue_whenParamIsPresentInMap() {
        var encrypted = "encrypted";
        when(textEncryptor.encrypt(anyString())).thenReturn(encrypted);

        var token = "raw";
        Optional<String> actual =
                propertyExtractor.getOptionalParamEncrypted(Map.of("apiToken", token), API_TOKEN);

        assertThat(actual).isPresent().get().isEqualTo(encrypted);
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

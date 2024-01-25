package com.epam.reportportal.extension.github.info.impl;

import com.epam.ta.reportportal.entity.integration.IntegrationType;
import com.epam.ta.reportportal.entity.integration.IntegrationTypeDetails;
import com.epam.ta.reportportal.exception.ReportPortalException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PropertiesFilePluginInfoProviderTest {
    private static final String DESCRIPTION = "The integration provides an exchange of information between ReportPortal and the GitHub, such as posting issues and linking issues, getting updates on their statuses.";
    private static final String DESCRIPTION_KEY = "description";
    private static final String METADATA_KEY = "metadata";
    private static final String EMBEDDED_KEY = "embedded";
    private static final String MULTIPLE_KEY = "multiple";
    private static final String BINARY_DATA_KEY = "binaryData";

    @Test
    void providerShouldReturnAllPropertiesFromFileSuccessfully() {
        // given
        PropertiesFilePluginInfoProvider pluginInfoProvider =
                new PropertiesFilePluginInfoProvider("src/test/resources", "config.properties");
        var integrationType = new IntegrationType();
        var integrationTypeDetails = new IntegrationTypeDetails();
        integrationTypeDetails.setDetails(new HashMap<>());
        integrationType.setDetails(integrationTypeDetails);

        // when
        IntegrationType result = pluginInfoProvider.provide(integrationType);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDetails()).isNotNull();
        assertThat(result.getDetails().getDetails())
                .isNotNull()
                .containsKey(BINARY_DATA_KEY)
                .extracting(BINARY_DATA_KEY)
                .asInstanceOf(InstanceOfAssertFactories.MAP)
                .containsEntry("main", "remoteEntity.js")
                .containsEntry("icon", "plugin-icon.svg")
                .containsEntry(METADATA_KEY, "metadata.json");


        assertThat(result.getDetails().getDetails().get(DESCRIPTION_KEY))
                .isEqualTo(DESCRIPTION);
        assertThat(result.getDetails().getDetails().get(METADATA_KEY))
                .asInstanceOf(InstanceOfAssertFactories.MAP)
                .containsEntry(EMBEDDED_KEY, true)
                .containsEntry(MULTIPLE_KEY, true);
    }

    @Test
    void providerShouldReturnAllBinaryDataUnchangedSuccessfully() {
        // given
        PropertiesFilePluginInfoProvider pluginInfoProvider =
                new PropertiesFilePluginInfoProvider("src/test/resources", "config.properties");
        var integrationType = new IntegrationType();
        var integrationTypeDetails = new IntegrationTypeDetails();
        HashMap<String, Object> details = new HashMap<>();
        details.put(BINARY_DATA_KEY, "originalValue");
        integrationTypeDetails.setDetails(details);
        integrationType.setDetails(integrationTypeDetails);

        // when
        IntegrationType result = pluginInfoProvider.provide(integrationType);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getDetails()).isNotNull();
        assertThat(result.getDetails().getDetails())
                .isNotNull()
                .containsKey(BINARY_DATA_KEY)
                .extracting(BINARY_DATA_KEY)
                .asInstanceOf(InstanceOfAssertFactories.STRING)
                .isEqualTo("originalValue");

        assertThat(result.getDetails().getDetails().get(DESCRIPTION_KEY))
                .isEqualTo(DESCRIPTION);
        assertThat(result.getDetails().getDetails().get(METADATA_KEY))
                .asInstanceOf(InstanceOfAssertFactories.MAP)
                .containsEntry(EMBEDDED_KEY, true)
                .containsEntry(MULTIPLE_KEY, true);
    }

    @Test
    void providerShouldThrownExceptionDuringPropertiesReading() {
        // given
        PropertiesFilePluginInfoProvider pluginInfoProvider =
                new PropertiesFilePluginInfoProvider("src/test/resources", "config1.properties");
        var integrationType = new IntegrationType();
        var integrationTypeDetails = new IntegrationTypeDetails();
        integrationTypeDetails.setDetails(new HashMap<>());
        integrationType.setDetails(integrationTypeDetails);

        // when / then
        assertThatThrownBy(() -> pluginInfoProvider.provide(integrationType))
                .hasMessage("Unable to load binary data by id 'src/test/resources/config1.properties'")
                .isExactlyInstanceOf(ReportPortalException.class);
    }
}

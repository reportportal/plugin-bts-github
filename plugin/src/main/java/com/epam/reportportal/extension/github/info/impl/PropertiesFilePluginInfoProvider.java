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

package com.epam.reportportal.extension.github.info.impl;


import com.epam.reportportal.extension.github.info.PluginInfoProvider;
import com.epam.ta.reportportal.entity.integration.IntegrationType;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author RiverSharks
 */
public class PropertiesFilePluginInfoProvider implements PluginInfoProvider {
    private static final String BINARY_DATA_KEY = "binaryData";
    private static final String DESCRIPTION_KEY = "description";
    private static final String METADATA_KEY = "metadata";
    private static final String PLUGIN_DESCRIPTION =
            "The integration provides an exchange of information between ReportPortal and the GitHub, such as posting issues and linking issues, getting updates on their statuses.";
    private static final Map<String, Object> PLUGIN_METADATA = Map.of(
            "embedded", true,
            "multiple", true
    );

    private final String resourcesDir;
    private final String propertyFile;

    public PropertiesFilePluginInfoProvider(String resourcesDir, String propertyFile) {
        this.resourcesDir = resourcesDir;
        this.propertyFile = propertyFile;
    }

    @Override
    public IntegrationType provide(IntegrationType integrationType) {
        Objects.requireNonNull(integrationType.getDetails());
        Objects.requireNonNull(integrationType.getDetails().getDetails());

        Map<String, Object> details = integrationType.getDetails().getDetails();
        details.computeIfAbsent(BINARY_DATA_KEY, key -> loadBinaryDataInfo());
        details.put(DESCRIPTION_KEY, PLUGIN_DESCRIPTION);
        details.put(METADATA_KEY, PLUGIN_METADATA);

        return integrationType;
    }

    private Map<String, String> loadBinaryDataInfo() {
        try (InputStream propertiesStream = Files.newInputStream(Paths.get(resourcesDir, propertyFile))) {
            Properties binaryDataProperties = new Properties();
            binaryDataProperties.load(propertiesStream);
            return binaryDataProperties.entrySet()
                    .stream()
                    .collect(HashMap::new,
                            (map, entry) -> map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())),
                            HashMap::putAll
                    );
        } catch (IOException ex) {
            throw new ReportPortalException(ErrorType.UNABLE_TO_LOAD_BINARY_DATA, ex.getMessage());
        }
    }
}

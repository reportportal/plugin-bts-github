package com.epam.reportportal.extension.github.info;

import com.epam.ta.reportportal.entity.integration.IntegrationType;

public interface PluginInfoProvider {
	IntegrationType provide(IntegrationType integrationType);
}

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

package com.epam.reportportal.extension.github.event.plugin;

import com.epam.reportportal.extension.event.PluginEvent;
import com.epam.reportportal.extension.github.event.EventHandlerFactory;
import org.springframework.context.ApplicationListener;

import static java.util.Optional.ofNullable;

/**
 * @author Andrei Piankouski
 */
public class PluginEventListener implements ApplicationListener<PluginEvent> {

	private final String pluginId;
	private final EventHandlerFactory<PluginEvent> pluginEventEventHandlerFactory;

	public PluginEventListener(String pluginId, EventHandlerFactory<PluginEvent> pluginEventEventHandlerFactory) {
		this.pluginId = pluginId;
		this.pluginEventEventHandlerFactory = pluginEventEventHandlerFactory;
	}

	@Override
	public void onApplicationEvent(PluginEvent event) {
		if (supports(event)) {
			ofNullable(pluginEventEventHandlerFactory.getEventHandler(event.getType())).ifPresent(pluginEventEventHandler -> pluginEventEventHandler
					.handle(event));
		}
	}

	private boolean supports(PluginEvent event) {
		return pluginId.equals(event.getPluginId());
	}
}

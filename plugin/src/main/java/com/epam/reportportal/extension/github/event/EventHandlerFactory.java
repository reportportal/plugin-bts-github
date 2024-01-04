package com.epam.reportportal.extension.github.event;

import com.epam.reportportal.extension.github.event.handler.EventHandler;

/**
 * @author Andrei Piankouski
 */
public interface EventHandlerFactory<T> {

    EventHandler<T> getEventHandler(String key);
}

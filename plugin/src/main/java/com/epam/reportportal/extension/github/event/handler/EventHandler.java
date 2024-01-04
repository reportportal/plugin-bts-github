package com.epam.reportportal.extension.github.event.handler;

/**
 * @author Andrei Piankouski
 */
public interface EventHandler<T> {

	void handle(T event);
}

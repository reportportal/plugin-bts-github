package com.epam.reportportal.extension.github;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

/**
 * @author Andrei Piankouski
 */
public class GithubPlugin extends Plugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper - A wrapper over plugin instance.
     */
    public GithubPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
}

package com.epam.reportportal.extension.github;

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.reportportal.extension.IntegrationGroupEnum;
import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.ReportPortalExtensionPoint;
import com.epam.reportportal.extension.common.IntegrationTypeProperties;
import com.epam.reportportal.extension.event.PluginEvent;
import com.epam.reportportal.extension.github.command.GetIssueFieldsCommand;
import com.epam.reportportal.extension.github.command.GetIssueTypesCommand;
import com.epam.reportportal.extension.github.command.PostTicketCommand;
import com.epam.reportportal.extension.github.command.RetrieveCreateParamsCommand;
import com.epam.reportportal.extension.github.event.plugin.PluginEventHandlerFactory;
import com.epam.reportportal.extension.github.event.plugin.PluginEventListener;
import com.epam.reportportal.extension.github.generated.api.IssuesApi;
import com.epam.reportportal.extension.github.info.impl.PropertiesFilePluginInfoProvider;
import com.epam.reportportal.extension.github.provider.mapper.IssuesMapper;
import com.epam.reportportal.extension.github.provider.rest.ApiClientCustomized;
import com.epam.reportportal.extension.github.utils.MemoizingSupplier;
import com.epam.reportportal.extension.util.RequestEntityConverter;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.IntegrationTypeRepository;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasypt.util.text.BasicTextEncryptor;
import org.pf4j.Extension;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Andrei Piankouski
 */
@Extension
public class GitHubPluginExtension implements ReportPortalExtensionPoint, DisposableBean {
    public static final String BINARY_DATA_PROPERTIES_FILE_ID = "binary-data.properties";
    private static final String PLUGIN_ID = "GitHub";
    private static final String DOCUMENTATION_LINK_FIELD = "documentationLink";
    private static final String DOCUMENTATION_LINK = "https://reportportal.io/docs/plugins/GitHubBTS";

    private final String resourcesDir;
    private final IssuesApi issuesApi;
    private final IssuesMapper issuesMapper;
    private final Supplier<ApplicationListener<PluginEvent>> pluginLoadedListenerSupplier;
    private final Supplier<Map<String, PluginCommand<?>>> pluginCommandMapping;
    private final Supplier<Map<String, CommonPluginCommand<?>>> commonPluginCommandMapping;
    private final Supplier<RequestEntityConverter> requestEntityConverter;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IntegrationTypeRepository integrationTypeRepository;
    @Autowired
    private IntegrationRepository integrationRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BasicTextEncryptor textEncryptor;
    @Autowired
    private ObjectMapper objectMapper;


    public GitHubPluginExtension(Map<String, Object> initParams) {
        this.resourcesDir = IntegrationTypeProperties.RESOURCES_DIRECTORY.getValue(initParams)
                .map(String::valueOf).orElse("");

        this.pluginLoadedListenerSupplier = new MemoizingSupplier<>(
                () -> new PluginEventListener(PLUGIN_ID, new PluginEventHandlerFactory(
                        integrationTypeRepository,
                        integrationRepository,
                        new PropertiesFilePluginInfoProvider(resourcesDir, BINARY_DATA_PROPERTIES_FILE_ID)
                )));
        this.requestEntityConverter = new MemoizingSupplier<>(() -> new RequestEntityConverter(objectMapper));
        this.pluginCommandMapping = new MemoizingSupplier<>(this::getCommands);
        this.commonPluginCommandMapping = new MemoizingSupplier<>(this::getCommonCommands);

        this.issuesApi = new IssuesApi(new ApiClientCustomized());
        this.issuesMapper = new IssuesMapper();
    }

    @PostConstruct
    public void createIntegration() {
        initListeners();
    }

    private void initListeners() {
        ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(
                AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class
        );
        applicationEventMulticaster.addApplicationListener(pluginLoadedListenerSupplier.get());
    }

    @Override
    public void destroy() {
        removeListeners();
    }

    private void removeListeners() {
        ApplicationEventMulticaster applicationEventMulticaster = applicationContext.getBean(
                AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                ApplicationEventMulticaster.class
        );
        applicationEventMulticaster.removeApplicationListener(pluginLoadedListenerSupplier.get());
    }

    @Override
    public Map<String, ?> getPluginParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(ALLOWED_COMMANDS, new ArrayList<>(pluginCommandMapping.get().keySet()));
        params.put(DOCUMENTATION_LINK_FIELD, DOCUMENTATION_LINK);
        params.put(COMMON_COMMANDS, new ArrayList<>(commonPluginCommandMapping.get().keySet()));
        return params;
    }

    @Override
    public CommonPluginCommand<?> getCommonCommand(String commandName) {
        return commonPluginCommandMapping.get().get(commandName);
    }

    @Override
    public PluginCommand<?> getIntegrationCommand(String commandName) {
        return pluginCommandMapping.get().get(commandName);
    }

    @Override
    public IntegrationGroupEnum getIntegrationGroup() {
        return IntegrationGroupEnum.BTS;
    }

    private Map<String, PluginCommand<?>> getCommands() {
        var getIssueTypesCommand = new GetIssueTypesCommand(projectRepository);
        var getIssueFieldsCommand = new GetIssueFieldsCommand(projectRepository);
        var postTicketCommand = new PostTicketCommand(
                projectRepository,
                requestEntityConverter.get(),
                textEncryptor,
                issuesApi,
                issuesMapper);
        return Map.of(
                getIssueTypesCommand.getName(), getIssueTypesCommand,
                getIssueFieldsCommand.getName(), getIssueFieldsCommand,
                postTicketCommand.getName(), postTicketCommand
        );
    }

    private Map<String, CommonPluginCommand<?>> getCommonCommands() {
        var retrieveCreateCommand = new RetrieveCreateParamsCommand(textEncryptor);
        return Map.of(
                retrieveCreateCommand.getName(), retrieveCreateCommand
        );
    }
}

package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.ProjectMemberCommand;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetIssueTypesCommand extends ProjectMemberCommand<List<String>> {

    public GetIssueTypesCommand(ProjectRepository projectRepository) {
        super(projectRepository);
    }

    @Override
    public String getName() {
        return "getIssueTypes";
    }

    @Override
    protected List<String> invokeCommand(Integration integration, Map<String, Object> map) {
        return Arrays.stream(IssueType.values()).map(IssueType::getName).collect(Collectors.toList());
    }
}

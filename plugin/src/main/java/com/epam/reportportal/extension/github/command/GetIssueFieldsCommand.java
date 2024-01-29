package com.epam.reportportal.extension.github.command;

import com.epam.reportportal.extension.ProjectMemberCommand;
import com.epam.ta.reportportal.dao.ProjectRepository;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.ws.model.externalsystem.PostFormField;

import java.util.List;
import java.util.Map;

public class GetIssueFieldsCommand extends ProjectMemberCommand<List<PostFormField>> {

    public GetIssueFieldsCommand(ProjectRepository projectRepository) {
        super(projectRepository);
    }

    @Override
    public String getName() {
        return "getIssueFields";
    }

    @Override
    protected List<PostFormField> invokeCommand(Integration integration, Map<String, Object> params) {
        return GitHubIssueField.getAvailableFieldsMeta();
    }
}

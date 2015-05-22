package com.seqis.jenkinsci.plugins.qualitycollector.action;

import com.seqis.data.entity.TestCaseExecution;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TableDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestObjectVersionService;

public class QualityCollectorTeamToolViewAction extends QualityCollectorGroupedViewAction {
	private static final String QUALITY_COLLECTOR_TEAM_TOOL_VIEW_URL = "team_tool_view";
	private static final String QUALITY_COLLECTOR_TEAM_TOOL_VIEW_NAME = "Team and Tool View";
	private static final String QUALITY_COLLECTOR_TEAM_TOOL_VIEW_ICONFILENAME = "/plugin/quality-collector/images/qc-symbol.png";

	public QualityCollectorTeamToolViewAction(TableDataService tableDataService, 
			TestObjectVersionService testObjectVersionService) {
		super(tableDataService, testObjectVersionService);
	}
	
	@Override
	public String getDisplayName() {
		return QUALITY_COLLECTOR_TEAM_TOOL_VIEW_NAME;
	}

	@Override
	public String getIconFileName() {
		return QUALITY_COLLECTOR_TEAM_TOOL_VIEW_ICONFILENAME;
	}

	@Override
	public String getUrlName() {
		return QUALITY_COLLECTOR_TEAM_TOOL_VIEW_URL;
	}

	@Override
	public String getDescriptionFor(TestCaseExecution testCaseExecution) {
		String output = "";
		if (testCaseExecution.getTeam() != null && !"".equals(testCaseExecution.getTeam().getName())) {
			output = "Team: " + testCaseExecution.getTeam().getName();
		}
		if (testCaseExecution.getTestCase() != null
				&& !"".equals(testCaseExecution.getTestCase().getTestToolName())) {
			if (!"".equals(output)) {
				output = output + "<br>";
			}
			output = output + "Tool: " + testCaseExecution.getTestCase().getTestToolName();
		}
		return output;
	}
}

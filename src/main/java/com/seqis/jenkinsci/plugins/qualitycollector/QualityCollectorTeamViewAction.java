package com.seqis.jenkinsci.plugins.qualitycollector;

import com.seqis.data.entity.TestCaseExecution;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TableDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestObjectVersionService;

public class QualityCollectorTeamViewAction extends QualityCollectorGroupedViewAction {
	private static final String QUALITY_COLLECTOR_TEAM_VIEW_URL = "team_view";
	private static final String QUALITY_COLLECTOR_TEAM_VIEW_NAME = "Team View";
	private static final String QUALITY_COLLECTOR_TEAM_VIEW_ICONFILENAME = "/plugin/quality-collector/images/qc-symbol.png";

	public QualityCollectorTeamViewAction(TableDataService tableDataService, 
			TestObjectVersionService testObjectVersionService) {
		super(tableDataService, testObjectVersionService);
	}
	
	@Override
	public String getDisplayName() {
		return QUALITY_COLLECTOR_TEAM_VIEW_NAME;
	}

	@Override
	public String getIconFileName() {
		return QUALITY_COLLECTOR_TEAM_VIEW_ICONFILENAME;
	}

	@Override
	public String getUrlName() {
		return QUALITY_COLLECTOR_TEAM_VIEW_URL;
	}

	@Override
	public String getDescriptionFor(TestCaseExecution testCaseExecution) {
		if (testCaseExecution.getTeam() == null) {
			return "No Team assigned";
		}
		if (testCaseExecution.getTeam().getName() == null) {
			return "Nameless Team";
		}
		return testCaseExecution.getTeam().getName();
	}
}

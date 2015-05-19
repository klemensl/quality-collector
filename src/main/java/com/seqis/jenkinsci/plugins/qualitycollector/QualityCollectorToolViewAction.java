package com.seqis.jenkinsci.plugins.qualitycollector;

import com.seqis.data.entity.TestCaseExecution;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TableDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestObjectVersionService;

public class QualityCollectorToolViewAction extends QualityCollectorGroupedViewAction {
	private static final String QUALITY_COLLECTOR_TOOL_VIEW_URL = "tool_view";
	private static final String QUALITY_COLLECTOR_TOOL_VIEW_NAME = "Tool View";
	private static final String QUALITY_COLLECTOR_TOOL_VIEW_ICONFILENAME = "/plugin/quality-collector/images/qc-symbol.png";

	public QualityCollectorToolViewAction(TableDataService tableDataService, 
			TestObjectVersionService testObjectVersionService) {
		super(tableDataService, testObjectVersionService);
	}

	@Override
	public String getDisplayName() {
		return QUALITY_COLLECTOR_TOOL_VIEW_NAME;
	}

	@Override
	public String getIconFileName() {
		return QUALITY_COLLECTOR_TOOL_VIEW_ICONFILENAME;
	}

	@Override
	public String getUrlName() {
		return QUALITY_COLLECTOR_TOOL_VIEW_URL;
	}

	@Override
	public String getDescriptionFor(TestCaseExecution testCaseExecution) {
		if (testCaseExecution.getTestCase() == null
				|| testCaseExecution.getTestCase().getTestToolName() == null) {
			return "No Tool assigned";
		}
		return testCaseExecution.getTestCase().getTestToolName();
	}
}

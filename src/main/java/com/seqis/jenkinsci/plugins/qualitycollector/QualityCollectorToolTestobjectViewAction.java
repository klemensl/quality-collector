package com.seqis.jenkinsci.plugins.qualitycollector;

import com.seqis.data.entity.TestCaseExecution;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TableDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestObjectVersionService;

public class QualityCollectorToolTestobjectViewAction extends QualityCollectorGroupedViewAction {
	private static final String QUALITY_COLLECTOR_TOOL_TESTOBJECT_VIEW_URL = "tool_testobject_view";
	private static final String QUALITY_COLLECTOR_TOOL_TESTOBJECT_VIEW_NAME = "Tool and Testobject View";
	private static final String QUALITY_COLLECTOR_TOOL_TESTOBJECT_VIEW_ICONFILENAME = "/plugin/quality-collector/images/qc-symbol.png";

	public QualityCollectorToolTestobjectViewAction(TableDataService tableDataService, 
			TestObjectVersionService testObjectVersionService) {
		super(tableDataService, testObjectVersionService);
	}
	
	@Override
	public String getDisplayName() {
		return QUALITY_COLLECTOR_TOOL_TESTOBJECT_VIEW_NAME;
	}

	@Override
	public String getIconFileName() {
		return QUALITY_COLLECTOR_TOOL_TESTOBJECT_VIEW_ICONFILENAME;
	}

	@Override
	public String getUrlName() {
		return QUALITY_COLLECTOR_TOOL_TESTOBJECT_VIEW_URL;
	}

	@Override
	public String getDescriptionFor(TestCaseExecution testCaseExecution) {
		String output = "";
		if (testCaseExecution.getTestCase() != null
				&& !"".equals(testCaseExecution.getTestCase().getTestToolName())) {
			output = "Tool: " + testCaseExecution.getTestCase().getTestToolName();
		}
		if (testCaseExecution.getTestObject() != null
				&& !"".equals(testCaseExecution.getTestObject().getName())) {
			if (!"".equals(output)) {
				output = output + "<br>";
			}
			output = output + "Testobject: " + testCaseExecution.getTestObject().getName();
		}
		return output;
	}
}

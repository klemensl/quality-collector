package com.seqis.jenkinsci.plugins.qualitycollector.action;

import com.seqis.data.entity.TestCaseExecution;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TableDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestObjectVersionService;

public class QualityCollectorTestobjectViewAction extends QualityCollectorGroupedViewAction {
	private static final String QUALITY_COLLECTOR_TESTOBJECT_VIEW_URL = "testobject_view";
	private static final String QUALITY_COLLECTOR_TESTOBJECT_VIEW_NAME = "Testobject View";
	private static final String QUALITY_COLLECTOR_TESTOBJECT_VIEW_ICONFILENAME = "/plugin/quality-collector/images/qc-symbol.png";

	public QualityCollectorTestobjectViewAction(TableDataService tableDataService, 
			TestObjectVersionService testObjectVersionService) {
		super(tableDataService, testObjectVersionService);
	}
	
	@Override
	public String getDisplayName() {
		return QUALITY_COLLECTOR_TESTOBJECT_VIEW_NAME;
	}

	@Override
	public String getIconFileName() {
		return QUALITY_COLLECTOR_TESTOBJECT_VIEW_ICONFILENAME;
	}

	@Override
	public String getUrlName() {
		return QUALITY_COLLECTOR_TESTOBJECT_VIEW_URL;
	}

	@Override
	public String getDescriptionFor(TestCaseExecution testCaseExecution) {
		if (testCaseExecution.getTestObject() == null || testCaseExecution.getTestObject().getName() == null) {
			return "No Testobject assigned";
		}
		return testCaseExecution.getTestObject().getName();
	}
}

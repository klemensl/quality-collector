package com.seqis.jenkinsci.plugins.qualitycollector;

import hudson.model.Action;

import java.util.logging.Logger;

import jenkins.model.ModelObjectWithContextMenu;

import org.kohsuke.stapler.bind.JavaScriptMethod;

import com.seqis.data.entity.TestCaseExecution;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TableWithChartsDto;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TableDataService;
import com.seqis.jenkinsci.plugins.qualitycollector.service.TestObjectVersionService;

public abstract class QualityCollectorGroupedViewAction implements Action, ActionWithDescription,
		ModelObjectWithContextMenu.ContextMenuVisibility  {
	private static final Logger logger = Logger.getLogger(Constants.QUALITY_COLLECTOR_LOGGER_NAME);
	private TableDataService tableDataService;
	private TestObjectVersionService testObjectVersionService;

	public QualityCollectorGroupedViewAction(TableDataService tableDataService, 
			TestObjectVersionService testObjectVersionService) {
		super();
		this.tableDataService = tableDataService;
		this.testObjectVersionService = testObjectVersionService;
	}

	@Override
	public abstract String getDisplayName();
	
	@Override
	public abstract String getIconFileName();

	@Override
	public abstract String getUrlName();

	@Override
	public abstract String getDescriptionFor(TestCaseExecution testCaseExecution);

	@Override
	public boolean isVisible() {
		return true;
	}
	
	@JavaScriptMethod
	public TableWithChartsDto getTestExecutionSummaryTable(String versionIdAsString) {
		logger.info("getTestExecutionSummeryTable called");
		return this.tableDataService.getTestExecutionSummaryTableFor(this,
				this.testObjectVersionService.getTestObjectVersionForIdAsString(versionIdAsString));
	}	
}

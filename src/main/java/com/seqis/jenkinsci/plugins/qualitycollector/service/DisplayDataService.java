package com.seqis.jenkinsci.plugins.qualitycollector.service;

import java.util.Collection;

import com.seqis.data.entity.TestObjectVersion;
import com.seqis.jenkinsci.plugins.qualitycollector.ActionWithDescription;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.ChartDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.MainPageDisplayDataDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TestExecutionSummaryTableEntryDto;

public class DisplayDataService extends CommonDataService {

	public DisplayDataService(TestExecutionSummaryService testExecutionSummaryService) {
		super(testExecutionSummaryService);
	}

	public MainPageDisplayDataDto getMainPageDisplayDataDtoFor(ActionWithDescription action, TestObjectVersion testObjectVersion) {
		final Collection<TestExecutionSummaryTableEntryDto> testExecutionSummarys = super.getTestExecutionSummaryEntrysFor(action, testObjectVersion);
		if (testExecutionSummarys == null) {
			return null;
		}

		final TestExecutionSummaryTableEntryDto testExecutionSummary = testExecutionSummarys.iterator().next();
		final ChartDto mainChart = super.createChartFor(testExecutionSummary);
		return new MainPageDisplayDataDto(testExecutionSummary.getLastExecutionSummary(), mainChart);
	}
}
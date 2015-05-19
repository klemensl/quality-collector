package com.seqis.jenkinsci.plugins.qualitycollector.service;

import java.text.DateFormat;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.seqis.data.entity.TestObjectVersion;
import com.seqis.jenkinsci.plugins.qualitycollector.ActionWithDescription;
import com.seqis.jenkinsci.plugins.qualitycollector.Constants;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.ChartDatasetDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.ChartDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TestExecutionSummaryDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TestExecutionSummaryTableEntryDto;
import com.seqis.jenkinsci.plugins.qualitycollector.util.Timespan;

public abstract class CommonDataService {
	private static final Logger logger = Logger.getLogger(Constants.QUALITY_COLLECTOR_LOGGER_NAME);

	protected final TestExecutionSummaryService testExecutionSummaryService;

	public CommonDataService(TestExecutionSummaryService testExecutionSummaryService) {
		this.testExecutionSummaryService = testExecutionSummaryService;
	}

	protected Collection<TestExecutionSummaryTableEntryDto> getTestExecutionSummaryEntrysFor(ActionWithDescription action, TestObjectVersion testObjectVersion) {
		final Timespan chartTimespan = new Timespan(1);
		final long barTimespan = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

		final Collection<TestExecutionSummaryTableEntryDto> testExecutionSummarys = 
				this.testExecutionSummaryService.getTestExecutionSummaryEntrysFor(chartTimespan, barTimespan, 
						Constants.NUMBER_OF_LAST_EXECUTIONS_SUMMARY_CHART, action, testObjectVersion);

		if (testExecutionSummarys == null || testExecutionSummarys.isEmpty()) {
			logger.log(Level.WARNING, "data not found");
			return null;
		}
		return testExecutionSummarys;
	}

	protected ChartDto createChartFor(TestExecutionSummaryTableEntryDto entry) {
		final long[] inconclusives = new long[entry.getLastExecutionSummarys().size()];
		final long[] ignoreds = new long[entry.getLastExecutionSummarys().size()];
		final long[] failures = new long[entry.getLastExecutionSummarys().size()];
		final long[] oks = new long[entry.getLastExecutionSummarys().size()];
		final String[] labels = new String[entry.getLastExecutionSummarys().size()];

		int i = 0;
		for (TestExecutionSummaryDto testExecutionSummary : entry.getLastExecutionSummarys()) {
			inconclusives[i] = testExecutionSummary.getInconclusiveCount();
			ignoreds[i] = testExecutionSummary.getIgnoredCount();
			failures[i] = testExecutionSummary.getFailureCount();
			oks[i] = testExecutionSummary.calculateOKCount();
			labels[i] = DateFormat.getDateInstance(Constants.STANDART_DATE_FORMAT).format(testExecutionSummary.getStart());
			i++;
		}

		final ChartDatasetDto[] datasets = new ChartDatasetDto[4];
		datasets[0] = ChartDatasetDto.OK(oks);
		datasets[1] = ChartDatasetDto.FAILED(failures);
		datasets[2] = ChartDatasetDto.INCONCLUSIVE(inconclusives);
		datasets[3] = ChartDatasetDto.IGNORED(ignoreds);

		return new ChartDto(labels, datasets);
	}
}
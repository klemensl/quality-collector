package com.seqis.jenkinsci.plugins.qualitycollector.service;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.seqis.data.entity.TestObjectVersion;
import com.seqis.jenkinsci.plugins.qualitycollector.ActionWithDescription;
import com.seqis.jenkinsci.plugins.qualitycollector.Constants;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.ChartDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.PieChartEntryDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TableWithChartsDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TestExecutionSummaryDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TestExecutionSummaryTableEntryDto;

public class TableDataService extends CommonDataService {

	public TableDataService(TestExecutionSummaryService testExecutionSummaryService) {
		super(testExecutionSummaryService);
	}

	public TableWithChartsDto getTestExecutionSummaryTableFor(ActionWithDescription action, TestObjectVersion testObjectVersion) {
		final Collection<TestExecutionSummaryTableEntryDto> testExecutionSummarys = super.getTestExecutionSummaryEntrysFor(action, testObjectVersion);
		if (testExecutionSummarys == null) {
			return null;
		}
		
		final List<PieChartEntryDto> summaryChartEntryList = new ArrayList<PieChartEntryDto>();
		//final PieChartEntryDto[] summaryChartEntries = new PieChartEntryDto[testExecutionSummarys.size() * 4];
		final String[][] tableData = new String[testExecutionSummarys.size()][4];
		final ChartDto[][] chartsForTable = new ChartDto[testExecutionSummarys.size()][1];

		int i = 0;
		for (TestExecutionSummaryTableEntryDto testExecutionSummary : testExecutionSummarys) {
			//this.createPieChartEntry(summaryChartEntries, testExecutionSummary, i, testExecutionSummarys.size());
			summaryChartEntryList.addAll(this.createPieChartEntriesFor(testExecutionSummary));
			
			tableData[i] = this.createTableDataFor(testExecutionSummary, i);
			chartsForTable[i][0] = super.createChartFor(testExecutionSummary);
			i++;
		}

		this.groupPieChartEntriesByState(summaryChartEntryList);
		
		return new TableWithChartsDto(Constants.TEST_EXECUTIONS_SUMMARY_TABLE_TITELS,
				Constants.TEST_EXECUTIONS_SUMMARY_CHART_NAMES, 
				testObjectVersion.getName(),
				summaryChartEntryList.toArray(new PieChartEntryDto[0]), tableData, chartsForTable);
	}

	


	/*private void createPieChartEntry(PieChartEntryDto[] summaryChart, TestExecutionSummaryTableEntryDto actEntry, int entryNumber, int totalEntrys) {
		summaryChart[0 * totalEntrys + entryNumber] = PieChartEntryDto.FAILED( 
				actEntry.getLastExecutionSummary().getFailureCount(), 
				actEntry.getLastExecutionSummary().getEntryDescription());		
		summaryChart[1 * totalEntrys + entryNumber] = PieChartEntryDto.INCONCLUSIVE( 
				actEntry.getLastExecutionSummary().getInconclusiveCount(), 
				actEntry.getLastExecutionSummary().getEntryDescription());
		summaryChart[2 * totalEntrys + entryNumber] = PieChartEntryDto.IGNORED(
				actEntry.getLastExecutionSummary().getIgnoredCount(), 
				actEntry.getLastExecutionSummary().getEntryDescription());
		summaryChart[3 * totalEntrys + entryNumber] = PieChartEntryDto.OK(
				actEntry.getLastExecutionSummary().getOKCount(), 
				actEntry.getLastExecutionSummary().getEntryDescription());
	}*/


	private List<PieChartEntryDto> createPieChartEntriesFor(TestExecutionSummaryTableEntryDto entry) {
		final List<PieChartEntryDto> piechartEntries = new ArrayList<PieChartEntryDto>();

		piechartEntries.add(PieChartEntryDto.FAILED( 
				entry.getLastExecutionSummary().getFailureCount(), 
				entry.getLastExecutionSummary().getEntryDescription()));		
		piechartEntries.add(PieChartEntryDto.INCONCLUSIVE( 
				entry.getLastExecutionSummary().getInconclusiveCount(), 
				entry.getLastExecutionSummary().getEntryDescription()));
		piechartEntries.add(PieChartEntryDto.IGNORED(
				entry.getLastExecutionSummary().getIgnoredCount(), 
				entry.getLastExecutionSummary().getEntryDescription()));
		piechartEntries.add(PieChartEntryDto.OK(
				entry.getLastExecutionSummary().calculateOKCount(), 
				entry.getLastExecutionSummary().getEntryDescription()));
		
		return piechartEntries;
	}

	private String[] createTableDataFor(TestExecutionSummaryTableEntryDto entry, int entryNumber) {
		final TestExecutionSummaryDto lastExecutionSummary = entry.getLastExecutionSummary();

		final String summaryCssClass;
		if (lastExecutionSummary.getFailureCount() > 0) {
			summaryCssClass = "quality_collector_failure";
		} else if (lastExecutionSummary.getInconclusiveCount() > 0) {
			summaryCssClass = "quality_collector_inconclusive";
		} else if (lastExecutionSummary.getIgnoredCount() > 0) {
			summaryCssClass = "quality_collector_ignored";
		} else {
			summaryCssClass = "quality_collector_ok";
		}

		final String[] tableRow = new String[4];
		tableRow[0] = lastExecutionSummary.getEntryDescription();
		tableRow[1] = String.format("<canvas id='%s%s' style='width: 100%%; height: 150px;padding-left: 20px;'>" + 
																	"<param name='tablechart_id' value='%s'>" + 
																"</canvas>", 
																Constants.TEST_EXECUTIONS_SUMMARY_CHART_NAMES[0], entryNumber, entryNumber);
		tableRow[2] = DateFormat.getDateInstance(Constants.STANDART_DATE_FORMAT).format(lastExecutionSummary.getStart());
		tableRow[3] = String.format("<div class='%s'>%s/%s</div>", summaryCssClass, lastExecutionSummary.calculateOKCount(), lastExecutionSummary.getTestCount());
		return tableRow;
	}
	
	private void groupPieChartEntriesByState(List<PieChartEntryDto> summaryChartEntryList) {
		Collections.sort(summaryChartEntryList, new Comparator<PieChartEntryDto>() {
			@Override
			public int compare(PieChartEntryDto pieChartEntry1, PieChartEntryDto pieChartEntry2) {
				if (Constants.QUALITY_COLLECTOR_COLOR_OK.equals(pieChartEntry2.getColor())) {
					return 4;
				} else if (Constants.QUALITY_COLLECTOR_COLOR_IGNORED.equals(pieChartEntry2.getColor())) {
					return 3;
				} else if (Constants.QUALITY_COLLECTOR_COLOR_INCONCLUSIVE.equals(pieChartEntry2.getColor())) {
					return 2;
				} else if (Constants.QUALITY_COLLECTOR_COLOR_FAILED.equals(pieChartEntry2.getColor())) {
					return 1;
				} else {				
					return 0;
				}
			}
		});
	}
}

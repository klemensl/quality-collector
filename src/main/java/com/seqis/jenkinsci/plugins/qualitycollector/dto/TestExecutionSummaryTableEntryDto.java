package com.seqis.jenkinsci.plugins.qualitycollector.dto;

import java.util.SortedSet;
import java.util.TreeSet;

public class TestExecutionSummaryTableEntryDto {
	private SortedSet<TestExecutionSummaryDto> lastExecutionSummarys;
	private TestExecutionSummaryDto lastExecutionSummary;

	public TestExecutionSummaryTableEntryDto() {
		this.lastExecutionSummarys = new TreeSet<TestExecutionSummaryDto>();
	}
	
	public SortedSet<TestExecutionSummaryDto> getLastExecutionSummarys() {
		return this.lastExecutionSummarys;
	}

	public TestExecutionSummaryDto getLastExecutionSummary() {
		return this.lastExecutionSummary;
	}

	public void prependExecutionSummaryDto(TestExecutionSummaryDto testExecutionSummary) {
		this.lastExecutionSummarys.add(testExecutionSummary);
		if (this.lastExecutionSummary == null) {
			this.lastExecutionSummary = testExecutionSummary;
		}
	}
}

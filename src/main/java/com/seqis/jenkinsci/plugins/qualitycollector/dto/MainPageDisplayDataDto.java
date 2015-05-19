package com.seqis.jenkinsci.plugins.qualitycollector.dto;

public class MainPageDisplayDataDto {
	private ChartDto mainChart;
	private TestExecutionSummaryDto mainSummary;
	
	public MainPageDisplayDataDto(TestExecutionSummaryDto mainSummary, ChartDto mainChart) {
		this.mainChart = mainChart;
		this.mainSummary = mainSummary;
	}	
	
	public ChartDto getMainChart() {
		return this.mainChart;
	}
	
	public TestExecutionSummaryDto getMainSummary() {
		return this.mainSummary;
	}
}

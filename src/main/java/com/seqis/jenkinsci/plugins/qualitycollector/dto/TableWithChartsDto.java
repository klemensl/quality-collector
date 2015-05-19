package com.seqis.jenkinsci.plugins.qualitycollector.dto;


public class TableWithChartsDto {
	private String[][] tableData;
	private ChartDto[][] tableCharts;
	private String[] tableTitles;
	private String[] chartNames;
	private PieChartEntryDto[] summaryChart;
	private String testObjectVersionName;

	public TableWithChartsDto(String[] tableTitles, String[] chartNames,
			String testObjectVersionName, PieChartEntryDto[] summaryChart, String[][] tableData, ChartDto[][] tableCharts) {

		this.tableTitles = tableTitles;
		this.chartNames = chartNames;
		this.testObjectVersionName = testObjectVersionName;
		this.summaryChart = summaryChart;
		this.tableData = tableData;
		this.tableCharts = tableCharts;
	}

	public String[][] getTableData() {
		return this.tableData;
	}

	public ChartDto[][] getTableCharts() {
		return this.tableCharts;
	}

	public String[] getTableTitles() {
		return this.tableTitles;
	}

	public String[] getChartNames() {
		return this.chartNames;
	}

	public PieChartEntryDto[] getSummaryChart() {
		return this.summaryChart;
	}

	public String getTestObjectVersionName() {
		return this.testObjectVersionName;
	}
}

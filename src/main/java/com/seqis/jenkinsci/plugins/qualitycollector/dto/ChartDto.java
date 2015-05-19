package com.seqis.jenkinsci.plugins.qualitycollector.dto;

public class ChartDto {
	private String[] labels;
	private ChartDatasetDto[] datasets;

	public ChartDto(String[] labels, ChartDatasetDto[] datasets) {
		this.labels = labels;
		this.datasets = datasets;
	}
	
	public String[] getLabels() {
		return this.labels;
	}
	
	public ChartDatasetDto[] getDatasets() {
		return this.datasets;
	}
}

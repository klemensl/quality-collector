package com.seqis.jenkinsci.plugins.qualitycollector.dto;

import com.seqis.jenkinsci.plugins.qualitycollector.Constants;

public class PieChartEntryDto {
	private long value;
  private String color;
  private String highlight;
  private String label;
  private String description;
	
	private PieChartEntryDto(String color, String highlight, long value, String label, String description) {
		this.color = color;
		this.highlight = highlight;
		this.value = value;
		this.label = label;
		this.description = description;
	}
    
	public long getValue() {
		return this.value;
	}
	
	public String getColor() {
		return this.color;
	}

	public String getHighlight() {
		return this.highlight;
	}

	public String getLabel() {
		return this.label;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public static PieChartEntryDto OK(long okCount, String description) {
		return new PieChartEntryDto(
				Constants.QUALITY_COLLECTOR_COLOR_OK,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_OK, 
				okCount, description + "<br>OK", description.replace("<br>", ""));
	}
	
	public static PieChartEntryDto FAILED(long failureCount, String description) {
		return new PieChartEntryDto(
				Constants.QUALITY_COLLECTOR_COLOR_FAILED,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_FAILURE, 
				failureCount, description + "<br>Failure", description.replace("<br>", ""));
	}
	
	public static PieChartEntryDto INCONCLUSIVE(long inconclusiveCount, String description) {
		return new PieChartEntryDto(
				Constants.QUALITY_COLLECTOR_COLOR_INCONCLUSIVE,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_INCONCLUSIVE, 
				inconclusiveCount, description + "<br>Inconclusive", description.replace("<br>", ""));
	}
	
	public static PieChartEntryDto IGNORED(long ignoredCount, String description) {
		return new PieChartEntryDto(
				Constants.QUALITY_COLLECTOR_COLOR_IGNORED,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_IGNORED, 
				ignoredCount, description + "<br>Ignored", description.replace("<br>", ""));
	}
}

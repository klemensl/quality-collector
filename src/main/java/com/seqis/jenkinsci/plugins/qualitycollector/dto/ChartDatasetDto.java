package com.seqis.jenkinsci.plugins.qualitycollector.dto;

import com.seqis.jenkinsci.plugins.qualitycollector.Constants;

public class ChartDatasetDto {
	private long[] data;
	private String label;
	private String fillColor;
	private String strokeColor;
	private String highlightFill;
	private String highlightStroke;

	public long[] getData() {
		return this.data;
	}

	public String getLabel() {
		return this.label;
	}

	public String getFillColor() {
		return this.fillColor;
	}

	public String getStrokeColor() {
		return this.strokeColor;
	}

	public String getHighlightFill() {
		return this.highlightFill;
	}

	public String getHighlightStroke() {
		return this.highlightStroke;
	}

	public void setHighlightStroke(String highlightStroke) {
		this.highlightStroke = highlightStroke;
	}
	
	public static ChartDatasetDto OK(long[] oks) {
		return new ChartDatasetDto(oks, "OK", 
				Constants.QUALITY_COLLECTOR_COLOR_OK,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_OK);
	}
	
	public static ChartDatasetDto FAILED(long[] failures) {
		return new ChartDatasetDto(failures, "Failures",
				Constants.QUALITY_COLLECTOR_COLOR_FAILED,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_FAILURE);
	}
	
	public static ChartDatasetDto INCONCLUSIVE(long[] inconclusives) {
		return new ChartDatasetDto(inconclusives, "Inconclusive",
				Constants.QUALITY_COLLECTOR_COLOR_INCONCLUSIVE,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_INCONCLUSIVE);
	}
	
	public static ChartDatasetDto IGNORED(long[] ignores) {
		return new ChartDatasetDto(ignores, "Ignored", 
				Constants.QUALITY_COLLECTOR_COLOR_IGNORED,
				Constants.QUALITY_COLLECTOR_COLOR_HIGHLIGHT_IGNORED);
	}

	public ChartDatasetDto(long[] data, String label, String fillColor, String highlightFill) {
		this.data = data;
		this.label = label;
		this.fillColor = fillColor;
		this.strokeColor = highlightFill;
		this.highlightFill = highlightFill;
		this.highlightStroke = highlightFill;
	}
}

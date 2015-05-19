package com.seqis.jenkinsci.plugins.qualitycollector;

import java.text.DateFormat;

public interface Constants {
	public static final String QUALITY_COLLECTOR_COLOR_OK = "rgba(0, 255, 0, 0.6)";

	public static final String QUALITY_COLLECTOR_COLOR_HIGHLIGHT_OK = "rgba(0, 255, 0, 0.9)";

	public static final String QUALITY_COLLECTOR_COLOR_IGNORED = "rgba(0, 0, 255, 0.6)";

	public static final String QUALITY_COLLECTOR_COLOR_HIGHLIGHT_IGNORED = "rgba(0, 0, 255, 0.9)";

	public static final String QUALITY_COLLECTOR_COLOR_INCONCLUSIVE = "rgba(255, 255, 0, 0.6)";

	public static final String QUALITY_COLLECTOR_COLOR_HIGHLIGHT_INCONCLUSIVE = "rgba(255, 255, 0, 0.9)";

	public static final String QUALITY_COLLECTOR_COLOR_FAILED = "rgba(255, 0, 0, 0.6)";

	public static final String QUALITY_COLLECTOR_COLOR_HIGHLIGHT_FAILURE = "rgba(255, 0, 0, 0.9)";

	public static final int NUMBER_OF_LAST_EXECUTIONS_SUMMARY_CHART = 10;

	public static final String[] TEST_EXECUTIONS_SUMMARY_TABLE_TITELS = { 
			"Team",
			"Last " + NUMBER_OF_LAST_EXECUTIONS_SUMMARY_CHART + " Runs",
			"Date of last run", "Result of last run", "Test tool" };

	public static final String[] TEST_EXECUTIONS_SUMMARY_CHART_NAMES = { 
			"Last " + NUMBER_OF_LAST_EXECUTIONS_SUMMARY_CHART + " Runs" };

	public static final String QUALITY_COLLECTOR_LOGGER_NAME = "com.seqis.jenkinsci.plugins.qualitycollector";

	public static final int STANDART_DATE_FORMAT = DateFormat.MEDIUM;
}

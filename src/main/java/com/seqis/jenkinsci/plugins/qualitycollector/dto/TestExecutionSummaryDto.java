package com.seqis.jenkinsci.plugins.qualitycollector.dto;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

import com.seqis.data.entity.TestCaseExecution;
import com.seqis.jenkinsci.plugins.qualitycollector.Constants;

public class TestExecutionSummaryDto implements Comparable<TestExecutionSummaryDto> {
	private Date start;
	private Date end;
	private String dateFormated;
	private String entryDescription;
	private long testCount;
	private long failureCount;
	private long inconclusiveCount;
	private long ignoredCount;
	
	public TestExecutionSummaryDto(Date start, Date end, String entryDescription, Collection<TestCaseExecution> executions) {
		this.start = start;
		this.end = end;
		this.testCount = 0;
		this.ignoredCount = 0;
		this.inconclusiveCount = 0;
		this.dateFormated = DateFormat.getDateInstance(Constants.STANDART_DATE_FORMAT).format(start);
		this.entryDescription = entryDescription;
		
		for (TestCaseExecution actExecution : executions) {
			this.testCount++;
			if (actExecution.getResult() == TestCaseExecution.Result.IGNORED.value()) {
				this.ignoredCount++;
			} else if (actExecution.getResult() == TestCaseExecution.Result.INCONCLUSIVE.value()) {
				this.inconclusiveCount++;
			} else if (actExecution.getResult() == TestCaseExecution.Result.FAILED.value()) {
				this.failureCount++;
			}
		}
	}

	public Date getStart() {
		return this.start;
	}

	public Date getEnd() {
		return this.end;
	}


	public long getTestCount() {
		return this.testCount;
	}


	public long getFailureCount() {
		return this.failureCount;
	}

	public long getInconclusiveCount() {
		return this.inconclusiveCount;
	}

	public long getIgnoredCount() {
		return this.ignoredCount;
	}

	public String getDateFormated() {
		return this.dateFormated;
	}

	public String getEntryDescription() {
		return this.entryDescription;
	}

	public long calculateOKCount() {
		return this.getTestCount() - this.getInconclusiveCount() - this.getFailureCount()	- this.getIgnoredCount();
	}

	@Override
	public int compareTo(TestExecutionSummaryDto o) {
		return this.start.compareTo(o.getStart());
	}
}
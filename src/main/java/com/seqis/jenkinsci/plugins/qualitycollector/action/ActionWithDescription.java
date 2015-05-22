package com.seqis.jenkinsci.plugins.qualitycollector.action;

import com.seqis.data.entity.TestCaseExecution;

public interface ActionWithDescription {
	public String getDescriptionFor(TestCaseExecution testCaseExecution); 
}

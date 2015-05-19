package com.seqis.jenkinsci.plugins.qualitycollector.service;

import java.util.List;

import com.seqis.data.entity.TestObjectVersion;

public class TestObjectVersionService {

	private List<TestObjectVersion> testObjectVersions;
	
	public TestObjectVersionService(List<TestObjectVersion> testObjectVersions) {
		this.testObjectVersions = testObjectVersions;
	}	
	
	public List<TestObjectVersion> getTestObjectVersions() {
		return this.testObjectVersions;
	}

	public TestObjectVersion getTestObjectVersionForIdAsString(String versionIdAsString) {
		for (TestObjectVersion actTestObjectVersion : testObjectVersions) {
			if (Long.toString(actTestObjectVersion.getId()).equals(versionIdAsString)) {
				return actTestObjectVersion;
			}
		}
		return null;
	}
}

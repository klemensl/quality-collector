package com.seqis.jenkinsci.plugins.qualitycollector.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.seqis.data.Datastore;
import com.seqis.data.entity.TestCase;
import com.seqis.data.entity.TestCaseExecution;
import com.seqis.data.entity.TestObjectVersion;
import com.seqis.jenkinsci.plugins.qualitycollector.ActionWithDescription;
import com.seqis.jenkinsci.plugins.qualitycollector.Constants;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TestExecutionSummaryDto;
import com.seqis.jenkinsci.plugins.qualitycollector.dto.TestExecutionSummaryTableEntryDto;
import com.seqis.jenkinsci.plugins.qualitycollector.util.Timespan;

public class TestExecutionSummaryService {
	private static final Logger logger = Logger.getLogger(Constants.QUALITY_COLLECTOR_LOGGER_NAME);
	
	private final Datastore datastore;

	public TestExecutionSummaryService(Datastore datastore) {
		this.datastore = datastore;
	}

	// JAVA7
	public Collection<TestExecutionSummaryTableEntryDto> getTestExecutionSummaryEntrysFor(Timespan summaryTimespan, 
			long testExecutionDuration,	int maximumTestExecutionSummaryCount, ActionWithDescription action, TestObjectVersion testObjectVersion) {

		final long millisAtStart = System.currentTimeMillis();
		logger.log(Level.INFO, "dataloading started");
		
		final List<TestCaseExecution> testCaseExecutions = this.datastore.findTestCaseExecutionsBy(
				summaryTimespan.getStart(), summaryTimespan.getEnd(), testObjectVersion);
		
		final Timespan currentTestExecutionTimespan = new Timespan();
		currentTestExecutionTimespan.setBothTimes(summaryTimespan.getEnd());
		currentTestExecutionTimespan.addMillisToStart(-testExecutionDuration);
		
		final SortedMap<String, TestExecutionSummaryTableEntryDto> descriptionToTableEntry = new TreeMap<String, TestExecutionSummaryTableEntryDto>();
		final Map<String, Map<TestCase, TestCaseExecution>> descriptionToLastTestCaseExecutions = new HashMap<String, Map<TestCase, TestCaseExecution>>();
		for (TestCaseExecution actTestCaseExecution : testCaseExecutions) {
			if (actTestCaseExecution.getStart().before(currentTestExecutionTimespan.getStart())) {
				for (String description : descriptionToLastTestCaseExecutions.keySet()) {
					if (!descriptionToTableEntry.containsKey(description)) {
						descriptionToTableEntry.put(description, new TestExecutionSummaryTableEntryDto());
					}
					
					final TestExecutionSummaryTableEntryDto entry = descriptionToTableEntry.get(description);
					final Collection<TestCaseExecution> testCaseExecutionsForEntry = descriptionToLastTestCaseExecutions.get(description).values();
					if ((entry != null) && (entry.getLastExecutionSummarys().size() < maximumTestExecutionSummaryCount)) {
						entry.prependExecutionSummaryDto(new TestExecutionSummaryDto(
								currentTestExecutionTimespan.getStart(), currentTestExecutionTimespan.getEnd(), 
								description, testCaseExecutionsForEntry));
					}
				}
				
				descriptionToLastTestCaseExecutions.clear();
				while (actTestCaseExecution.getStart().before(currentTestExecutionTimespan.getStart())) {
					currentTestExecutionTimespan.addMillisToBoth(-testExecutionDuration);
				}
			}
			
			final String description = action.getDescriptionFor(actTestCaseExecution);
			if (!descriptionToLastTestCaseExecutions.containsKey(description)) {
				descriptionToLastTestCaseExecutions.put(description, new HashMap<TestCase, TestCaseExecution>());
			}
			
			final Map<TestCase, TestCaseExecution> lastTestCaseExecutions = descriptionToLastTestCaseExecutions.get(description);
			if (!lastTestCaseExecutions.containsKey(actTestCaseExecution.getTestCase())) {
				lastTestCaseExecutions.put(actTestCaseExecution.getTestCase(), actTestCaseExecution);
			}
		}
		
		for (String description : descriptionToLastTestCaseExecutions.keySet()) {
			if (!descriptionToTableEntry.containsKey(description)) {
				descriptionToTableEntry.put(description, new TestExecutionSummaryTableEntryDto());
			}
			
			final TestExecutionSummaryTableEntryDto entry = descriptionToTableEntry.get(description);
			final Collection<TestCaseExecution> testCaseExecutionsForEntry = descriptionToLastTestCaseExecutions.get(description).values();
			if ((entry != null) && (entry.getLastExecutionSummarys().size() < maximumTestExecutionSummaryCount)) {
				entry.prependExecutionSummaryDto(new TestExecutionSummaryDto(
						currentTestExecutionTimespan.getStart(), currentTestExecutionTimespan.getEnd(), 
						description, testCaseExecutionsForEntry));
			}
		}
		
		final long millisAtEnd = System.currentTimeMillis();
		logger.log(Level.INFO, String.format("dataloading finished after %s ms", millisAtEnd - millisAtStart));

		return descriptionToTableEntry.values();
	}

	/*private void prependTestExecutionSummaryToAllEntryIfEnoughSpace(
			SortedMap<String, TestExecutionSummaryTableEntryDto> descriptionToTableEntry,
			Map<String, Map<TestCase, TestCaseExecution>> descriptionToLastTestCaseExecutions,
			Timespan currentTestExecutionTimespan,
			int maximumTestExecutionSummaryCount) {
		
		for (String description : descriptionToLastTestCaseExecutions.keySet()) {
			if (!descriptionToTableEntry.containsKey(description)) {
				descriptionToTableEntry.put(description, new TestExecutionSummaryTableEntryDto());
			}
			
			final TestExecutionSummaryTableEntryDto entry = descriptionToTableEntry.get(description);
			final Collection<TestCaseExecution> testCaseExecutionsForEntry = descriptionToLastTestCaseExecutions.get(description).values();
			if ((entry != null) && (entry.getLastExecutionSummarys().size() < maximumTestExecutionSummaryCount)) {
				entry.prependExecutionSummaryDto(new TestExecutionSummaryDto(
						currentTestExecutionTimespan.getStartTime(), currentTestExecutionTimespan.getEndTime(), 
						description, testCaseExecutionsForEntry));
			}
		}
	}*/

	

	
	

	/* Wenn ich sowas (ähnliches) mach, dann geb ich dem wenigstens einen Namen, der erklärt, wieso das returnierte entry "gültig" ist
	 * private TestExecutionSummaryTableEntryDto entryToAddExecution(String description, Map<String, TestExecutionSummaryTableEntryDto> searchMap, int numberOfOldSummarysInInlineChart) {
		final TestExecutionSummaryTableEntryDto entryToAdd = searchMap.get(description);
		if (entryToAdd == null) {
			return null;
		}
		if (entryToAdd.getLastExecutionSummarys().size() < numberOfOldSummarysInInlineChart) {
			return entryToAdd;
		}
		return null;
	}*/

	/*public TestObjectVersion getTestObjectVersion() {
		return this.rootAction.getTestObjectVersion();
	}*/
	
	
	//JAVA8
	/*public Collection<TestExecutionSummaryTableEntryDto> getTestExecutionSummaryEntrysFor(Timespan summaryTimespan, 
			long testExecutionDuration,	int maximumTestExecutionSummaryCount, ActionWithDescription action, TestObjectVersion testObjectVersion) {

		final long millisAtStart = System.currentTimeMillis();
		logger.log(Level.INFO, "dataloading started");
		
		final List<TestCaseExecution> testCaseExecutions = this.datastore.findTestCaseExecutionsBy(
				summaryTimespan.getStart(), summaryTimespan.getEnd(), testObjectVersion);
		
		final Timespan currentTestExecutionTimespan = new Timespan();
		currentTestExecutionTimespan.setBothTimes(summaryTimespan.getEnd());
		currentTestExecutionTimespan.addMillisToStart(-testExecutionDuration);
		
		
		final Map<String, Map<Timespan, List<TestCaseExecution>>> mapped = testCaseExecutions.stream().filter((TestCaseExecution tcex) -> summaryTimespan.contains(tcex.getStart(), tcex.getEnd()))
				.collect(Collectors.groupingBy((TestCaseExecution tcex) -> action.getDescriptionFor(tcex),
						Collectors.groupingBy((TestCaseExecution tcex) -> this.getTestCaseExecutionTimespanFor(tcex, summaryTimespan, testExecutionDuration), LatestTestCaseExecution.toList())));

		
		final List<TestExecutionSummaryTableEntryDto> entries = new ArrayList<TestExecutionSummaryTableEntryDto>();
		for (String description : mapped.keySet()) {
			final TestExecutionSummaryTableEntryDto entry = new TestExecutionSummaryTableEntryDto();
			
			for (Timespan ts : mapped.get(description).keySet()) {
				final Collection<TestCaseExecution> testCaseExecutionsForEntry = mapped.get(description).get(ts);
				if ((entry != null) && (entry.getLastExecutionSummarys().size() < maximumTestExecutionSummaryCount)) {
					entry.prependExecutionSummaryDto(new TestExecutionSummaryDto(ts.getStart(), ts.getEnd(), description, testCaseExecutionsForEntry));
				}
			}
			
			entries.add(entry);
		}
		
		logger.log(Level.INFO, String.format("dataloading finished after %s ms", System.currentTimeMillis() - millisAtStart));

		return entries;
	}
	
	private Timespan getTestCaseExecutionTimespanFor(TestCaseExecution tcex, Timespan summaryTimespan, long testExecutionDuration) {
		if (!summaryTimespan.contains(tcex.getStart(), tcex.getEnd())) {
			throw new IllegalArgumentException("TestCaseExecution not in given Timespan");
		}
		
		final Timespan currentTestExecutionTimespan = new Timespan();
		currentTestExecutionTimespan.setBothTimes(summaryTimespan.getEnd());
		currentTestExecutionTimespan.substractMillisFromStart(testExecutionDuration);

		while ((summaryTimespan.contains(currentTestExecutionTimespan)) && (!currentTestExecutionTimespan.contains(tcex.getStart(), tcex.getEnd()))) {
			currentTestExecutionTimespan.substractMillisFromBoth(testExecutionDuration);
		}
		
		return currentTestExecutionTimespan;
	}
	
	private static class LatestTestCaseExecution implements Collector<TestCaseExecution, List<TestCaseExecution>, List<TestCaseExecution>> {

		@Override
		public Supplier<List<TestCaseExecution>> supplier() {
			return ArrayList::new;
		}

		@Override
		public BiConsumer<List<TestCaseExecution>, TestCaseExecution> accumulator() {
			return (accumulator, newEntry) -> accumulator.add(newEntry);
		}

		@Override
		public BinaryOperator<List<TestCaseExecution>> combiner() {
			return (accumulator1, accumulator2) -> { 
				accumulator1.addAll(accumulator2); 
				return accumulator1; 
			};
		}

		@Override
		public Function<List<TestCaseExecution>, List<TestCaseExecution>> finisher() {
			return allTCEXs -> {
				final Map<TestCase, TestCaseExecution> latest = new HashMap<TestCase, TestCaseExecution>();
				for (TestCaseExecution tcex : allTCEXs) {
					final TestCase testcase = tcex.getTestCase();

					if (latest.containsKey(testcase)) {
						final TestCaseExecution storedTCEX = latest.get(testcase);
						if (storedTCEX.getEnd().after(tcex.getEnd())) {
							latest.put(testcase, tcex);
						}
					} else {
						latest.put(testcase, tcex);
					}
				}

				return new ArrayList<TestCaseExecution>(latest.values());
			};
		}

		@Override
		public Set<java.util.stream.Collector.Characteristics> characteristics() {
			return EnumSet.of(Characteristics.UNORDERED);
		}
		
	  public static Collector<TestCaseExecution, ?, List<TestCaseExecution>> toList() {
	  	return new LatestTestCaseExecution();
	  }
	}*/
}

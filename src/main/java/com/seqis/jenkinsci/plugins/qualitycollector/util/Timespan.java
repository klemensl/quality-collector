package com.seqis.jenkinsci.plugins.qualitycollector.util;

import java.util.Calendar;
import java.util.Date;

public class Timespan {
	protected final Calendar start = Calendar.getInstance();
	protected final Calendar end = Calendar.getInstance();	

	public Timespan(){		
	}
	
	public Timespan(int monthsBetweenStartAndEnd) {
		this.setStartToLastSecondOfDay();
		this.addMonthToStart(-1);
		this.setEndToLastSecondOfDay();
	}	
	
	public Date getStart() {
		return this.start.getTime();
	}
	
	public Date getEnd() {
		return this.end.getTime();
	}	

	public void setBothTimes(Date dateForBoth) {
		this.start.setTime(dateForBoth);
		this.end.setTime(dateForBoth);
	}
	
	public void setStartToLastSecondOfDay() {
		this.start.set(Calendar.HOUR_OF_DAY, 23);
		this.start.set(Calendar.MINUTE, 59);
		this.start.set(Calendar.SECOND, 59);
	}
	
	public void setEndToLastSecondOfDay() {
		this.end.set(Calendar.HOUR_OF_DAY, 23);
		this.end.set(Calendar.MINUTE, 59);
		this.end.set(Calendar.SECOND, 59);
	} 	
	
	public void addMillisToStart(long millis) {
		this.start.setTimeInMillis(this.start.getTimeInMillis()+millis);
	}		
	
	public void substractMillisFromStart(long millis) {
		this.start.setTimeInMillis(this.start.getTimeInMillis()-millis);
	}	
	
	public void addMillisToBoth(long millis) {
		this.start.setTimeInMillis(this.start.getTimeInMillis()+millis);
		this.end.setTimeInMillis(this.end.getTimeInMillis()+millis);
	}	
	
	public void substractMillisFromBoth(long millis) {
		this.start.setTimeInMillis(this.start.getTimeInMillis()-millis);
		this.end.setTimeInMillis(this.end.getTimeInMillis()-millis);
	}	
	
	public void addMonthToStart(int months) {
		this.start.add(Calendar.MONTH, months);
	}
	
	public boolean contains(Date start, Date end) {
		if ((start == null) || (end == null)) {
			return false;
		}
		return this.getStart().getTime() <= start.getTime() && this.getEnd().getTime() >= end.getTime();
	}
	
	public boolean contains(Timespan timespan) {
		if (timespan == null) {
			return false;
		}
		return this.getStart().getTime() <= timespan.getStart().getTime() && this.getEnd().getTime() >= timespan.getEnd().getTime();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Timespan)) {
			return false;
		}
		
		final Timespan t = (Timespan) o;
		return this.getStart().getTime() == t.getStart().getTime() && this.getEnd().getTime() == t.getEnd().getTime();
	}

	@Override
	public int hashCode() {
		return this.getStart().hashCode() + this.getEnd().hashCode();
	}
}

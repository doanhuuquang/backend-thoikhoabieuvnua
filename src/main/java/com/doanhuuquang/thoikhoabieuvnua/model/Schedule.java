package com.doanhuuquang.thoikhoabieuvnua.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Schedule {
	private String semesterString;
	private LocalDate semesterStartDate;
	private Map<Integer, WeeklySchedule> weeklySchedules;
	
	public Schedule() {
		weeklySchedules = new HashMap<>();
	}

	public Schedule(String semesterString, LocalDate semesterStartDate, Map<Integer, WeeklySchedule> weeklySchedules) {
		super();
		this.semesterString = semesterString;
		this.semesterStartDate = semesterStartDate;
		this.weeklySchedules = weeklySchedules;
	}

	public String getSemesterString() {
		return semesterString;
	}

	public void setSemesterString(String semesterString) {
		this.semesterString = semesterString;
	}

	public LocalDate getSemesterStartDate() {
		return semesterStartDate;
	}

	public void setSemesterStartDate(LocalDate semesterStartDate) {
		this.semesterStartDate = semesterStartDate;
	}

	public Map<Integer, WeeklySchedule> getWeeklySchedules() {
		return weeklySchedules;
	}

	public void setWeeklySchedules(Map<Integer, WeeklySchedule> weeklySchedules) {
		this.weeklySchedules = weeklySchedules;
	}
}

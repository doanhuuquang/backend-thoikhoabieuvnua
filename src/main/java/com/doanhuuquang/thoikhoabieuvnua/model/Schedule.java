package com.doanhuuquang.thoikhoabieuvnua.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedule {
	private String semesterString;
	private LocalDate semesterStartDate;
	private Map<LocalDate, List<Subject>> schedules;
	
	public Schedule() {
		schedules = new HashMap<>();
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

	public Map<LocalDate, List<Subject>> getSchedules() {
		return schedules;
	}

	public void setSchedules(Map<LocalDate, List<Subject>> schedules) {
		this.schedules = schedules;
	}
}

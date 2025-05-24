package com.doanhuuquang.thoikhoabieuvnua.model;

import java.util.ArrayList;
import java.util.List;

public class DailySchedule {
	private List<Subject> subjects;
	
	public DailySchedule() {
		subjects = new ArrayList<>();
	}

	public DailySchedule(List<Subject> subjects) {
		super();
		this.subjects = subjects;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
}

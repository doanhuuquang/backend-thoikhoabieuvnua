package com.doanhuuquang.thoikhoabieuvnua.model;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public class WeeklySchedule {
	private Map<DayOfWeek, DailySchedule> dailySchedules;
	
	public WeeklySchedule() {
		dailySchedules = new HashMap<>();
	}

	public WeeklySchedule(Map<DayOfWeek, DailySchedule> dailySchedules) {
		super();
		this.dailySchedules = dailySchedules;
	}

	public Map<DayOfWeek, DailySchedule> getDailySchedules() {
		return dailySchedules;
	}

	public void setDailySchedules(Map<DayOfWeek, DailySchedule> dailySchedules) {
		this.dailySchedules = dailySchedules;
	}
}

package com.doanhuuquang.thoikhoabieuvnua.service;

import com.doanhuuquang.thoikhoabieuvnua.model.Schedule;

public interface ScheduleService {
	public Schedule getSchedule(String studentCode, String password, String semesterCode);
}

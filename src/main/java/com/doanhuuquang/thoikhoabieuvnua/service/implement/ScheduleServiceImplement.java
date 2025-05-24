package com.doanhuuquang.thoikhoabieuvnua.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doanhuuquang.thoikhoabieuvnua.model.Schedule;
import com.doanhuuquang.thoikhoabieuvnua.scraper.WebScraper;
import com.doanhuuquang.thoikhoabieuvnua.service.ScheduleService;

@Service
public class ScheduleServiceImplement implements ScheduleService {
	@Autowired
	private WebScraper webScraper;
	
	@Override
	public Schedule getSchedule(String studentCode, String password, String semesterCode) {
		return webScraper.fetchScheduleOnWeb(studentCode, password, semesterCode);
	}
}

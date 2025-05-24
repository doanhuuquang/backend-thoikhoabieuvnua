package com.doanhuuquang.thoikhoabieuvnua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doanhuuquang.thoikhoabieuvnua.DTO.UserDTO;
import com.doanhuuquang.thoikhoabieuvnua.model.Schedule;
import com.doanhuuquang.thoikhoabieuvnua.service.ScheduleService;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
	@Autowired
	ScheduleService scheduleService;

	@PostMapping("/{semesterCode}")
	public ResponseEntity<?> getSchedule(@PathVariable String semesterCode, @RequestBody UserDTO userDTO) {
		Schedule schedule = scheduleService.getSchedule(userDTO.getStudentCode(), userDTO.getPassword(), semesterCode);
		
		if(schedule.getSemesterStartDate() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Có lỗi xảy ra khi tải thời khóa biểu. "
                    		+ "Vui lòng kiểm tra lại thông tin tài khoản sinh viên. "
                    		+ "Nếu bạn nghĩ là lỗi hệ thống xin vui lòng liên hệ với chúng tôi để được hỗ trợ!");
		}
		
		return ResponseEntity.ok(schedule);
	}
}

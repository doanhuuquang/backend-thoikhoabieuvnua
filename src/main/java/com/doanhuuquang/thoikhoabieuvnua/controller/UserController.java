package com.doanhuuquang.thoikhoabieuvnua.controller;

import com.doanhuuquang.thoikhoabieuvnua.DTO.UserDTO;
import com.doanhuuquang.thoikhoabieuvnua.config.SecurityConfig;
import com.doanhuuquang.thoikhoabieuvnua.model.User;
import com.doanhuuquang.thoikhoabieuvnua.repository.UserRepository;
import com.doanhuuquang.thoikhoabieuvnua.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
        	User user = userService.login(userDTO.getStudentCode(), userDTO.getPassword());
            
            if (user != null) {
            	return ResponseEntity.ok(user);
            } else {
            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Tài khoản hoặc mật khẩu không chính xác!");
            }
        } catch ( Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra, vui lòng thử lại sau bạn nhé!");
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
        	User user = userRepository.findUserByStudentCode(userDTO.getStudentCode());
        	
        	if (user != null) {
        		return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Tài khoản này đã được đăng ký trước đó rồi!");
        	}
        	
        	user = userService.register(userDTO.getStudentCode(), userDTO.getPassword());
            
            if (user != null) {
            	return ResponseEntity.ok(user);
            } else {
            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Thông tin tài khoản sinh viên không chính xác! Bạn có đang bị nhầm lẫn ở đâu đó không?");
            }
        } catch ( Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra, vui lòng thử lại sau bạn nhé!");
        }
    }
}
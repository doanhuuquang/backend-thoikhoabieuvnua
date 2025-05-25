package com.doanhuuquang.thoikhoabieuvnua.controller;

import com.doanhuuquang.thoikhoabieuvnua.DTO.UserDTO;
import com.doanhuuquang.thoikhoabieuvnua.config.SecurityConfig;
import com.doanhuuquang.thoikhoabieuvnua.model.User;
import com.doanhuuquang.thoikhoabieuvnua.repository.UserRepository;
import com.doanhuuquang.thoikhoabieuvnua.service.UserService;

import java.util.HashMap;
import java.util.Map;
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
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDTO userDTO) {
        User user = userService.login(userDTO.getStudentCode(), userDTO.getPassword());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("user", user);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDTO userDTO) {
        User user = userService.register(userDTO.getStudentCode(), userDTO.getPassword());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("user", user);
        return ResponseEntity.ok(response);
    }
}
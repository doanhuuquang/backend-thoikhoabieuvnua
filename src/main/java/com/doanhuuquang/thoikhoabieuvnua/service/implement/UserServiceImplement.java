package com.doanhuuquang.thoikhoabieuvnua.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doanhuuquang.thoikhoabieuvnua.model.User;
import com.doanhuuquang.thoikhoabieuvnua.repository.UserRepository;
import com.doanhuuquang.thoikhoabieuvnua.scraper.WebScraper;
import com.doanhuuquang.thoikhoabieuvnua.service.UserService;

@Service
public class UserServiceImplement implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WebScraper webScraper;
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public User login(String studentCode, String password) {
		// Kiểm tra trong database
		User user = userRepository.findUserByStudentCode(studentCode);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return user;
		}

		return null;
	}

	@Override
	public User register(String studentCode, String password) {
		// Kiểm tra thông tin đăng nhập từ trang đào tạo
		User userLoginFromWeb = webScraper.verifyStudentLogin(studentCode, password);
		
		if (userLoginFromWeb != null) {
			String hashedPassword = passwordEncoder.encode(password);
			userLoginFromWeb.setPassword(hashedPassword);
			
			User userSaved = userRepository.save(userLoginFromWeb);
			return userSaved;
		}
		
		return null;
	}
}
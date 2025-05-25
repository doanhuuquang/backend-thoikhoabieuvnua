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
		if (studentCode == null || password == null) {
			throw new IllegalArgumentException("Mã sinh viên và mật khẩu không được để trống");
		}

		User user = userRepository.findUserByStudentCode(studentCode);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return user;
		} else {
			throw new RuntimeException("Tài khoản hoặc mật khẩu không chính xác");
		}
	}

	@Override
	public User register(String studentCode, String password) {
		if (studentCode == null || password == null) {
			throw new IllegalArgumentException("Mã sinh viên hoặc mật khẩu không được null");
		}

		User existingUser = userRepository.findUserByStudentCode(studentCode);
		if (existingUser != null) {
			throw new RuntimeException(
					"Tài khoản này đã được đăng ký trước đó rồi, vui lòng chuyển sang đăng nhập nhé");
		}

		User userFromWeb = webScraper.verifyStudentLoginOnWeb(studentCode, password);

		String hashedPassword = passwordEncoder.encode(password);
		userFromWeb.setPassword(hashedPassword);
		User userSaved = userRepository.save(userFromWeb);
		return userSaved;
	}
}
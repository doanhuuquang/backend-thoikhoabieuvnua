package com.doanhuuquang.thoikhoabieuvnua.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.doanhuuquang.thoikhoabieuvnua.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	User findUserByStudentCode(String studentCode);
}

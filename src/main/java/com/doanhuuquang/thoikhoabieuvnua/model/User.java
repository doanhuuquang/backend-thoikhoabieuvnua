package com.doanhuuquang.thoikhoabieuvnua.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
	@Id
	private String id;
	private String name;
	private String studentCode;
	private String password;

	public User() {
		super();
	}
	
	public User(String name, String studentCode, String password) {
		super();
		this.name = name;
		this.studentCode = studentCode;
		this.password = password;
	}

	public User(String id, String name, String studentCode, String password) {
		super();
		this.id = id;
		this.name = name;
		this.studentCode = studentCode;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

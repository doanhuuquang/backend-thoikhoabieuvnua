package com.doanhuuquang.thoikhoabieuvnua.model;

public class Subject {
	private String code;
	private String name;
	private String group;
	private int credit;
	private String classCode;
	private int start;
	private int numberOfLessons;
	private String room;
	private String lecturerName;
	
	public Subject() {}
	
	public Subject(String code, String name, String group, int credit, String classCode, int start, int numberOfLessons,
			String room, String lecturerName) {
		super();
		this.code = code;
		this.name = name;
		this.group = group;
		this.credit = credit;
		this.classCode = classCode;
		this.start = start;
		this.numberOfLessons = numberOfLessons;
		this.room = room;
		this.lecturerName = lecturerName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getNumberOfLessons() {
		return numberOfLessons;
	}

	public void setNumberOfLessons(int numberOfLessons) {
		this.numberOfLessons = numberOfLessons;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}
}

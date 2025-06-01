package com.doanhuuquang.thoikhoabieuvnua.scraper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.doanhuuquang.thoikhoabieuvnua.model.Subject;

public class ScheduleParser {
	private Document doc;
	private LocalDate semesterStartDate;

	public ScheduleParser(LocalDate semesterStartDate) {
		this.semesterStartDate = semesterStartDate;
	}

	public Document parseHtmlToDocument(String html) {
		return Jsoup.parse(html);
	}

	public Map<LocalDate, List<Subject>> getSchedule(String html) {
		Map<LocalDate, List<Subject>> schedules = new TreeMap<>();
		doc = parseHtmlToDocument(html);

		Element table = doc.selectFirst("tbody");
		Elements rows = table.select("tr");

		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			Element row = rows.get(rowIndex);
			Elements cols = row.select("td");

			cols.removeIf(col -> col.hasClass("d-none"));

			if (cols.isEmpty()) {
				continue;
			}

			Subject subject = getSubject(table, rowIndex);

			String weekString = cols.get(cols.size() - 1).text();

			DayOfWeek day = getDayOfWeek(table, rowIndex);

			if (day == null || subject == null) {
				continue;
			}

			int weekCount = 1;
			for (char c : weekString.toCharArray()) {
				if (Character.isDigit(c)) {

					LocalDate weekStart = semesterStartDate.plusWeeks(weekCount - 1);
					LocalDate subjectDate = weekStart.with(day);
					
					List<Subject> subjects = schedules.get(subjectDate);
					if (subjects == null) {
						subjects = new ArrayList<>();
						schedules.put(subjectDate, subjects);
					}
					
					Subject subjectCopy = new Subject(
							subject.getCode(),
							subject.getName(),
							subject.getGroup(),
							subject.getCredit(),
							subject.getClassCode(),
							subject.getStart(),
							subject.getNumberOfLessons(),
							subject.getRoom(),
							subject.getLecturerName(),
							subjectDate
					);
					
					subjects.add(subjectCopy);
				}

				weekCount++;
			}
		}

		return schedules;
	}

	private DayOfWeek getDayOfWeek(Element table, int rowIndex) {
		Element row = table.select("tr").get(rowIndex);
		Elements cols = row.select("td");

		cols.removeIf(col -> col.hasClass("d-none"));

		if (cols.isEmpty()) {
			return null;
		}

		String thu;
		if (cols.size() < 7) {
			thu = cols.get(0).text();
		} else {
			thu = cols.get(5).text();
		}

		return getDayOfWeekFromString(thu);
	}

	private Subject getSubject(Element table, int rowIndex) {
		try {
			Element row = table.select("tr").get(rowIndex);
			Elements cols = row.select("td");

			cols.removeIf(col -> col.hasClass("d-none"));

			if (cols.isEmpty()) {
				return null;
			}

			Subject subject = new Subject();

			if (cols.size() < 7 && rowIndex > 0) {
				for (int i = rowIndex - 1; i >= 0; i--) {
					Element rowParent = table.select("tr").get(i);
					Elements parentCols = rowParent.select("td");

					boolean hasRowspan = parentCols.stream().anyMatch(td -> td.hasAttr("rowspan"));
					if (hasRowspan) {
						parentCols.removeIf(col -> col.hasClass("d-none"));

						if (!parentCols.isEmpty()) {
							subject.setCode(parentCols.get(0).text().trim());
							subject.setName(parentCols.get(1).text().trim());
							subject.setGroup(parentCols.get(2).text().trim());
							subject.setCredit(safeParseInt(parentCols.get(3).text().trim()));
							subject.setClassCode(parentCols.get(4).text().trim());
							break;
						}
					}
				}

				if (cols.size() > 4) {
					subject.setStart(safeParseInt(cols.get(1).text().trim()));
					subject.setNumberOfLessons(safeParseInt(cols.get(2).text().trim()));
					subject.setRoom(cols.get(3).text().trim());
					String lecturerName = cols.get(4).text().trim();
					subject.setLecturerName(
							lecturerName != null && lecturerName != "" ? lecturerName : "Đang cập nhật");

				}
			} else if (cols.size() >= 10) {
				subject.setCode(cols.get(0).text().trim());
				subject.setName(cols.get(1).text().trim());
				subject.setGroup(cols.get(2).text().trim());
				subject.setCredit(safeParseInt(cols.get(3).text().trim()));
				subject.setClassCode(cols.get(4).text().trim());
				subject.setStart(safeParseInt(cols.get(6).text().trim()));
				subject.setNumberOfLessons(safeParseInt(cols.get(7).text().trim()));
				subject.setRoom(cols.get(8).text().trim());
				String lecturerName = cols.get(9).text().trim();
				subject.setLecturerName(lecturerName != null && lecturerName != "" ? lecturerName : "Đang cập nhật");
			}

			return subject;
		} catch (Exception e) {
			return null;
		}
	}

	public DayOfWeek getDayOfWeekFromString(String string) {
		switch (string.toUpperCase()) {
		case "2":
			return DayOfWeek.MONDAY;
		case "3":
			return DayOfWeek.TUESDAY;
		case "4":
			return DayOfWeek.WEDNESDAY;
		case "5":
			return DayOfWeek.THURSDAY;
		case "6":
			return DayOfWeek.FRIDAY;
		case "7":
			return DayOfWeek.SATURDAY;
		case "CN":
			return DayOfWeek.SUNDAY;
		default:
			return null;
		}
	}

	private int safeParseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}

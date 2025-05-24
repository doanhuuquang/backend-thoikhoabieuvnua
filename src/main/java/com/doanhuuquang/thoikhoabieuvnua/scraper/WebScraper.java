package com.doanhuuquang.thoikhoabieuvnua.scraper;

import com.doanhuuquang.thoikhoabieuvnua.model.User;
import com.doanhuuquang.thoikhoabieuvnua.model.Schedule;
import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class WebScraper {

	public static final String URL_DAO_TAO_VNUA = "https://daotao.vnua.edu.vn/";
	public static final String URL_DAO_TAO_VNUA_TKB_TUAN = "https://daotao.vnua.edu.vn/#/tkb-tuan";
	public static final String URL_DAO_TAO_VNUA_TKB_HK = "https://daotao.vnua.edu.vn/#/tkb-hocky";
	
	public static final String LINK_BUTTON_TKB_TUAN = "/html/body/app-root/div/div/div/div[1]/div/div/div[2]/app-right/app-chucnang/div/div[2]/ul[9]/li/div[1]/a[1]";
	public static final String LINK_BUTTON_TKB_HK = "/html/body/app-root/div/div/div/div[1]/div/div/div[2]/app-right/app-chucnang/div/div[2]/ul[10]/li/div[1]/a[1]";

	private static final String USER_NAME_INPUT = "input[name='username']";
	private static final String PASSWORD_INPUT = "input[name='password']";
	private static final String LOGIN_BUTTON = "button:has-text('Đăng nhập')";
	private static final String USER_NAME_LOGGED = "/html/body/app-root[1]/div/div/div/div[1]/div/div/div[2]/app-right/app-login/div/div[2]/div[1]/table/tr[2]/td[2]/span";
	
	private static final String COMBO_BOX_XPATH = "//*[@id=\"fullScreen\"]/div[2]/div[2]/div[1]/ng-select";
    private static final String DROP_DOWN_ITEM_SELECTOR = ".ng-option";
    private static final String SEMESTER_COMBO_BOX_XPATH = "/html/body/app-root[1]/div/div/div/div[1]/div/div/div[1]/app-thoikhoabieu-tuan/div[1]/div[2]/div[1]/div[1]/ng-select/div/div";
    private static final String SEMESTER_DROP_DOWN_SELECTOR = ".ng-option";
    private static final String SEMESTER_TABLE_COMBO_BOX_XPATH = "/html/body/app-root[1]/div/div/div/div[1]/div/div/div[1]/app-tkb-hocky/div/div[2]/div[1]/div/ng-select/div";

	private static final boolean HEADLESS_MODE = false;
	private static final int TIMEOUT = 20000;
	
	private Playwright playwright;
	private Browser browser;
	
	@PostConstruct
	public void init() {
		playwright = Playwright.create();
		LaunchOptions options = new LaunchOptions().setHeadless(HEADLESS_MODE);
		browser = playwright.chromium().launch(options);
	}

	@PreDestroy
	public void cleanup() {
		if (browser != null) {
			browser.close();
		}
		if (playwright != null) {
			playwright.close();
		}
	}
	
	public Page loginWeb(String studentCode, String password) {
		try {
			Page page = browser.newPage();
			page.navigate(URL_DAO_TAO_VNUA, new Page.NavigateOptions().setTimeout(TIMEOUT));
			waitForPageLoad(page);

			Locator usernameInput = page.locator(USER_NAME_INPUT);
			Locator passwordInput = page.locator(PASSWORD_INPUT);
			Locator loginButton = page.locator(LOGIN_BUTTON);

			usernameInput.fill(studentCode);
			passwordInput.fill(password);
			loginButton.click();

			waitForPageLoad(page);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User verifyStudentLogin(String studentCode, String password) {
		Page page = null;

		try {
			page = loginWeb(studentCode, password);
			if (page == null) return null;

			ElementHandle userNameElement = page.waitForSelector("xpath=" + USER_NAME_LOGGED,
					new Page.WaitForSelectorOptions().setTimeout(TIMEOUT).setState(WaitForSelectorState.VISIBLE));

			return userNameElement == null ? null : new User(userNameElement.innerText(), studentCode, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (page != null) page.close();
		}
	}

	
//	public Schedule fetchScheduleOnWeb(String studentCode, String password) {
//		Page page = null;
//		
//		try {
//			page = loginWeb(studentCode, password);
//			
//			Schedule schedule = new Schedule();
//			schedule.setSemesterStartDate(fetchStartDateOfTerm(page));
//			
//			return schedule;
//		} catch (Exception e) {
//			return null;
//		} finally {
//			if(page != null) {
//				page.close();
//			}
//		}
//	}
	
	public Schedule fetchScheduleOnWeb(String studentCode, String password, String semesterCode) {
		Page page = null;

		try {
			page = loginWeb(studentCode, password);
			if (page == null) return null;

			String fullSemesterText = convertSemesterCode(semesterCode);
			page.locator("xpath=" + LINK_BUTTON_TKB_TUAN).click();
			waitForPageLoad(page);

			page.locator("xpath=" + SEMESTER_COMBO_BOX_XPATH).click();
			waitForPageLoad(page);
			
			Schedule schedule = new Schedule();

			List<ElementHandle> semesterList = page.querySelectorAll(SEMESTER_DROP_DOWN_SELECTOR);
			for (ElementHandle element : semesterList) {
				if (fullSemesterText.equalsIgnoreCase(element.innerText().trim())) {
					schedule.setSemesterString(element.innerText().trim());
					element.click();
					break;
				}
			}
			
			waitForPageLoad(page);
			LocalDate date = fetchStartDateOfTerm(page);
			
			schedule.setSemesterStartDate(date);

			return schedule;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (page != null) page.close();
		}
	}

	public List<ElementHandle> fetchSemesterList(Page page) {
        try {
        	page.locator("xpath=" + SEMESTER_COMBO_BOX_XPATH).click();
            waitForPageLoad(page);

            return page.querySelectorAll(SEMESTER_DROP_DOWN_SELECTOR);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
	
	public LocalDate fetchStartDateOfTerm(Page page) {
		try {
			page.waitForTimeout(2000);
			page.locator("xpath=" + COMBO_BOX_XPATH).click();
			waitForPageLoad(page);

			List<ElementHandle> weeks = page.querySelectorAll(DROP_DOWN_ITEM_SELECTOR);
			if (weeks.isEmpty()) return null;

			ElementHandle firstWeek = weeks.get(0);
			String weekId = firstWeek.getAttribute("id");
			if (weekId == null || !weekId.contains("-")) return null;

			int weekNumber = Integer.parseInt(weekId.split("-")[1]) + 1;
			LocalDate currentWeekDate = parseStringToDate(firstWeek.innerText());

			return (currentWeekDate == null) ? null : currentWeekDate.minusWeeks(weekNumber - 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String fetchTableSchedule(Page page, int semesterIndex) {
		try {
			page.navigate(URL_DAO_TAO_VNUA_TKB_HK);
			waitForPageLoad(page);

			page.locator("xpath=" + SEMESTER_TABLE_COMBO_BOX_XPATH).click();
			waitForPageLoad(page);

			List<ElementHandle> semesters = page.querySelectorAll(SEMESTER_DROP_DOWN_SELECTOR);
			if (semesterIndex <= 0 || semesterIndex > semesters.size()) {
				System.err.println("Invalid semester index: " + semesterIndex);
				return null;
			}

			semesters.get(semesterIndex - 1).click();
			waitForPageLoad(page);

			return page.content();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String convertSemesterCode(String semesterCode) {
	    if (semesterCode == null || semesterCode.length() != 9) return "";

	    String hocKy = semesterCode.substring(0, 1); // 1
	    String namBatDau = semesterCode.substring(1, 5); // 2023
	    String namKetThuc = semesterCode.substring(5, 9); // 2024

	    return "Học kỳ " + hocKy + " - Năm học " + namBatDau + " - " + namKetThuc;
	}
	
	public LocalDate parseStringToDate(String dateString) {
        if (dateString == null || !dateString.contains("[")) return null;

        int startIndex = dateString.indexOf('[');
        int endIndex = dateString.indexOf(']');
        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) return null;

        String insideBrackets = dateString.substring(startIndex + 1, endIndex);
        for (String part : insideBrackets.split(" ")) {
            if (part.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return LocalDate.parse(part, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        }
        return null;
    }
	
	private void waitForPageLoad(Page page) {
	    page.waitForLoadState(LoadState.NETWORKIDLE);
	}
}
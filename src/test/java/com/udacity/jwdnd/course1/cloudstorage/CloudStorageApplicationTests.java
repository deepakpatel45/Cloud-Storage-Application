package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 */
	private void doMockSignUp(String firstName, String lastName,
							  String userName, String password) {

		WebDriverWait webDriverWait =
				new WebDriverWait(driver, Duration.ofSeconds(2));

		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		WebElement inputFirstName =
				driver.findElement(By.id("inputFirstName"));
		inputFirstName.sendKeys(firstName);

		WebElement inputLastName =
				driver.findElement(By.id("inputLastName"));
		inputLastName.sendKeys(lastName);

		WebElement inputUsername =
				driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);

		WebElement inputPassword =
				driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);

		WebElement buttonSignUp =
				driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		Assertions.assertTrue(
				driver.findElement(By.id("success-msg"))
						.getText()
						.contains("You successfully signed up!")
		);
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 */
	private void doLogIn(String userName, String password) {

		driver.get("http://localhost:" + this.port + "/login");

		WebDriverWait webDriverWait =
				new WebDriverWait(driver, Duration.ofSeconds(2));

		WebElement loginUserName =
				driver.findElement(By.id("inputUsername"));
		loginUserName.sendKeys(userName);

		WebElement loginPassword =
				driver.findElement(By.id("inputPassword"));
		loginPassword.sendKeys(password);

		WebElement loginButton =
				driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

	@Test
	public void testRedirection() {
		doMockSignUp("Redirection", "Test", "RT", "123");

		Assertions.assertEquals(
				"http://localhost:" + this.port + "/login",
				driver.getCurrentUrl()
		);
	}

	@Test
	public void testBadUrl() {
		doMockSignUp("URL", "Test", "UT", "123");
		doLogIn("UT", "123");

		driver.get("http://localhost:" + this.port + "/some-random-page");

		Assertions.assertFalse(
				driver.getPageSource().contains("Whitelabel Error Page")
		);
	}

	@Test
	public void testLargeUpload() {
		doMockSignUp("Large File", "Test", "LFT", "123");
		doLogIn("LFT", "123");

		WebDriverWait webDriverWait =
				new WebDriverWait(driver, Duration.ofSeconds(2));

		String fileName = "upload5m.zip";

		webDriverWait.until(
				ExpectedConditions.visibilityOfElementLocated(
						By.id("fileUpload"))
		);

		WebElement fileSelectButton =
				driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton =
				driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		Assertions.assertTrue(
				driver.getPageSource()
						.contains("File is too large to upload.")
		);

		Assertions.assertFalse(
				driver.getPageSource()
						.contains("HTTP Status 403 – Forbidden")
		);
	}
}
package com.janitri.base;

import com.janitri.utils.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        String headless = System.getProperty("headless", "false");

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ff = new FirefoxOptions();
                if (Boolean.parseBoolean(headless)) ff.addArguments("-headless"); // correct flag
                driver = new FirefoxDriver(ff);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edge = new EdgeOptions();
                if (Boolean.parseBoolean(headless)) edge.addArguments("--headless=new");
                driver = new EdgeDriver(edge);
                break;
            default: // chrome
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 1);
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments("--disable-infobars");
                options.addArguments("--start-maximized");
                if (Boolean.parseBoolean(headless)) options.addArguments("--headless=new");
                options.setAcceptInsecureCerts(true);
                driver = new ChromeDriver(options);
        }

        long implicitWait = parseLongOrDefault(Config.get("implicitWaitSeconds"), 10);
        long pageLoadTimeout = parseLongOrDefault(Config.get("pageLoadTimeoutSeconds"), 30);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.get(Config.get("baseUrl"));
    }

    private long parseLongOrDefault(String value, long defaultValue) {
        try {
            if (value == null || value.isEmpty()) return defaultValue;
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

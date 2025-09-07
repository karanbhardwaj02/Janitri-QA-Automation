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
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(String browser) {
        String headless = System.getProperty("headless", "false");
        if (browser == null) browser = "chrome";

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ff = new FirefoxOptions();
                if (Boolean.parseBoolean(headless)) ff.addArguments("-headless");
                driver = new FirefoxDriver(ff);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edge = new EdgeOptions();
                if (Boolean.parseBoolean(headless)) edge.addArguments("--headless=new");
                driver = new EdgeDriver(edge);
                break;
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                // Auto-allow notifications and disable popups
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

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
            Long.parseLong(Config.get("implicitWaitSeconds"))
        ));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
            Long.parseLong(Config.get("pageLoadTimeoutSeconds"))
        ));
        driver.get(Config.get("baseUrl"));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

package com.janitri.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // A resilient set of alternative locators to increase portability across minor UI changes
    private final List<By> userLocators = Arrays.asList(
            By.cssSelector("input[name='email']"),
            By.cssSelector("input[type='email']"),
            By.id("email"),
            By.name("username"),
            By.cssSelector("input[placeholder*='Email']"),
            By.cssSelector("input[placeholder*='User']")
    );

    private final List<By> passwordLocators = Arrays.asList(
            By.cssSelector("input[name='password']"),
            By.cssSelector("input[type='password']"),
            By.id("password"),
            By.cssSelector("input[placeholder*='Password']")
    );

    private final List<By> loginButtonLocators = Arrays.asList(
            By.cssSelector("button[type='submit']"),
            By.xpath("//button[.//text()[contains(.,'Login') or contains(.,'Log in')]]"),
            By.xpath("//button[contains(.,'Login') or contains(.,'Log in')]")
    );

    private final List<By> eyeToggleLocators = Arrays.asList(
            By.cssSelector("[aria-label*='show'],[aria-label*='toggle']"),
            By.cssSelector("[class*='eye']"),
            By.cssSelector("button[class*='visibility'], span[class*='visibility']"),
            By.xpath("//button//*[name()='svg' and contains(@class,'eye')]/.."),
            By.xpath("//button[contains(.,'show') or contains(.,'Show') or contains(.,'view')]")
    );

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private WebElement findFirst(List<By> candidates) {
        for (By by : candidates) {
            List<WebElement> list = driver.findElements(by);
            if (!list.isEmpty()) return list.get(0);
        }
        throw new NoSuchElementException("Element not found with provided locators");
    }

    public WebElement username() { return findFirst(userLocators); }
    public WebElement password() { return findFirst(passwordLocators); }
    public WebElement loginButton() { return findFirst(loginButtonLocators); }
    public WebElement eyeToggle() { return findFirst(eyeToggleLocators); }

    public void typeUsername(String val) {
        WebElement u = username();
        u.clear();
        u.sendKeys(val);
    }
    public void typePassword(String val) {
        WebElement p = password();
        p.clear();
        p.sendKeys(val);
    }
    public void clickLogin() {
        loginButton().click();
    }

    public boolean areCoreElementsPresent() {
        try {
            wait.until(d -> username().isDisplayed() && password().isDisplayed() && loginButton().isDisplayed());
            // eye toggle may be optional; don't fail the test if missing
            try { eyeToggle().isDisplayed(); } catch (Exception ignore) {}
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Methods requested in assignment =====
    public boolean testLoginButtonDisabledWhenFieldAreEmpty() {
        try {
            // Try to verify disabled state
            WebElement btn = loginButton();
            // Some UIs disable via 'disabled' attribute or CSS classes
            boolean disabledAttr = Boolean.parseBoolean(btn.getAttribute("disabled"));
            boolean ariaDisabled = "true".equalsIgnoreCase(btn.getAttribute("aria-disabled"));
            boolean enabledApi = btn.isEnabled();
            // Return true if any notion of 'disabled' is detected when fields are empty
            return disabledAttr || ariaDisabled || !enabledApi;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean testPasswordMaskedbutton() {
        try {
            WebElement p = password();
            // should be masked by default
            boolean masked = "password".equalsIgnoreCase(p.getAttribute("type"));
            try {
                WebElement eye = eyeToggle();
                eye.click();
                wait.until(d -> "text".equalsIgnoreCase(password().getAttribute("type")));
                boolean unmasked = "text".equalsIgnoreCase(password().getAttribute("type"));
                // reset back if possible
                try { eye.click(); } catch (Exception ignore) {}
                return masked && unmasked;
            } catch (NoSuchElementException nse) {
                // If no toggle exists, at least ensure masked by default
                return masked;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String testInvalidLoginShowErrorMsg() {
        typeUsername("invalid@example.com");
        typePassword("WrongPass123!");
        clickLogin();
        // Try to capture a generic error toast/message
        List<By> errorLocators = Arrays.asList(
                By.cssSelector(".error, .toast-error, .alert-danger, .MuiAlert-message, [role='alert']"),
                By.xpath("//*[contains(@class,'error') or contains(@class,'danger') or contains(@class,'helper-text') or @role='alert']"),
                By.xpath("//*[contains(text(),'invalid') or contains(text(),'Incorrect') or contains(text(),'Wrong') or contains(text(),'failed')]")
        );
        try {
            for (By by : errorLocators) {
                List<WebElement> msgs = new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
                if (!msgs.isEmpty()) return msgs.get(0).getText();
            }
        } catch (TimeoutException ignored) {}
        return "No explicit error message located";
    }
}

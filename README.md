# Janitri QA Automation (Login Page)

Java + Selenium + TestNG project using Page Object Model (POM).  
Covers: element presence, disabled login when empty, password mask/unmask, and invalid login error capture.

## Prerequisites
- JDK 11 or newer (11/17 recommended)
- Maven 3.8+
- Internet access (WebDriverManager downloads drivers automatically)
- Chrome/Firefox/Edge (Chrome default)

## Run
```bash
mvn clean test          # runs with Chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dheadless=true
```
Reports appear in `target/surefire-reports`.

## Structure
- `src/main/java/com/janitri/base/BaseTest.java` – driver setup/teardown
- `src/main/java/com/janitri/pages/LoginPage.java` – page object with required methods
- `src/test/java/com/janitri/tests/LoginTests.java` – TestNG tests
- `src/test/resources/config.properties` – base URL & timeouts
- `testng.xml` – TestNG suite file

## Notes
- Browser notifications are auto-allowed in Chrome options.
- Locators are written resiliently to survive minor UI changes.
- No valid credentials are used; tests assert UI behaviors only.

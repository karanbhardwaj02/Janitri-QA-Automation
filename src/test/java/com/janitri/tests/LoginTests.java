package com.janitri.tests;

import com.janitri.base.BaseTest;
import com.janitri.pages.LoginPage;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(priority = 1)
    public void verifyElementsPresent() {
        LoginPage page = new LoginPage(driver);
        Assert.assertTrue(page.areCoreElementsPresent(), "Core login elements should be present");
    }

    @Test(priority = 2)
    public void testLoginButtonDisabledWhenFieldAreEmpty() {
        LoginPage page = new LoginPage(driver);
        Assert.assertTrue(page.testLoginButtonDisabledWhenFieldAreEmpty(),
                "Login button should be disabled when fields are empty");
    }

    @Test(priority = 3)
    public void testPasswordMaskedbutton() {
        LoginPage page = new LoginPage(driver);
        Assert.assertTrue(page.testPasswordMaskedbutton(), "Password mask/unmask behavior should work");
    }

    @Test(priority = 4)
    public void testInvalidLoginShowErrorMsg() {
        LoginPage page = new LoginPage(driver);
        String msg = page.testInvalidLoginShowErrorMsg();
        Reporter.log("Captured error message: " + msg, true);
        Assert.assertNotNull(msg);
    }
}

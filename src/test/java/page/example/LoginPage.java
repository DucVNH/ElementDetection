package page.example;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement header = getWebElement("header", locators);
        WebElement inputUsername = getWebElement("inputUsername", locators);
        WebElement inputPassword = getWebElement("inputPassword", locators);
        WebElement btnLogin = getWebElement("btnLogin", locators);

        softAssert.assertTrue(header != null);
        softAssert.assertTrue(inputUsername != null);
        softAssert.assertTrue(inputPassword != null);
        softAssert.assertTrue(btnLogin != null);
    }
}

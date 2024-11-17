package page.orangehrm;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OrangeHRMLoginPage extends BasePage {
    public OrangeHRMLoginPage(WebDriver driver) {
        super(driver);
    }

    public void logIn(String username, String password) {
        waitForPageLoaded();
        WebElement inputUsername = getWebElement("inputUsername", locators);
        WebElement inputPassword = getWebElement("inputPassword", locators);
        WebElement btnLogin = getWebElement("btnLogin", locators);
        sendKeys(inputUsername, username);
        sendKeys(inputPassword, password);
        clickOnElement(btnLogin);
        waitForPageLoaded();
    }
}

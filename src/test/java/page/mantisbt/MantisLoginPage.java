package page.mantisbt;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MantisLoginPage extends BasePage {
    public MantisLoginPage(WebDriver driver) {
        super(driver);
    }

    public void logIn(String username, String password) {
        waitForPageLoaded();
        WebElement usernameElement = getWebElement("username", locators);
        WebElement btnLogin = getWebElement("btnLogin", locators);
        sendKeys(usernameElement, username);
        clickOnElement(btnLogin);
        waitForPageLoaded();
        WebElement passwordElement = getWebElement("password", locators);
        btnLogin = getWebElement("btnLogin", locators);
        sendKeys(passwordElement, password);
        clickOnElement(btnLogin);
        waitForPageLoaded();
    }
}

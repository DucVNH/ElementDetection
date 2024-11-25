package page.admidio;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AdmidioLoginPage extends BasePage {
    public AdmidioLoginPage(WebDriver driver) {
        super(driver);
    }

    public void logIn(String username, String password) {
        waitForPageLoaded();
        WebElement inputUsername = getWebElement("inputUsername", locators);
        WebElement inputPassword = getWebElement("inputPassword", locators);
        WebElement btnSignIn = getWebElement("btnSignIn", locators);
        sendKeys(inputUsername, username);
        sendKeys(inputPassword, password);
        clickOnElement(btnSignIn);
        waitForPageLoaded();
    }
}

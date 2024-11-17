package page.automationintesting;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class SeleniumTestPage extends BasePage {
    public SeleniumTestPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        WebElement firstName = getWebElement("inputFirstName", locators);
        WebElement lastName = getWebElement("inputSurName", locators);
        verifyElementVisible(firstName, softAssert);
        sendKeys(firstName, "Duc");
        verifyElementVisible(lastName, softAssert);
        sendKeys(lastName, "Nguyen");
    }
}

package page.orangehrm;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class ViewSystemUsers extends BasePage {
    public ViewSystemUsers(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement inputUsername = getWebElement("inputUsername", locators);
        WebElement userRole = getWebElement("userRole", locators);
        WebElement employeeName = getWebElement("employeeName", locators);
        WebElement status = getWebElement("status", locators);
        WebElement btnReset = getWebElement("btnReset", locators);
        WebElement btnSearch = getWebElement("btnSearch", locators);
        WebElement btnAdd = getWebElement("btnAdd", locators);
        WebElement columnUsername = getWebElement("columnUsername", locators);
        WebElement userRoleAdmin = getWebElement("userRoleAdmin", locators);
        WebElement columnStatus = getWebElement("columnStatus", locators);

        softAssert.assertTrue(inputUsername != null);
        softAssert.assertTrue(userRole != null);
        softAssert.assertTrue(employeeName != null);
        softAssert.assertTrue(status != null);
        softAssert.assertTrue(btnReset != null);
        softAssert.assertTrue(btnSearch != null);
        softAssert.assertTrue(btnAdd != null);
        softAssert.assertTrue(columnUsername != null);
        softAssert.assertTrue(userRoleAdmin != null);
        softAssert.assertTrue(columnStatus != null);
    }
}

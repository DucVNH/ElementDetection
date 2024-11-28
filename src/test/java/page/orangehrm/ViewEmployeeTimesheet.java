package page.orangehrm;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class ViewEmployeeTimesheet extends BasePage {
    public ViewEmployeeTimesheet(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement inputEmployeeName = getWebElement("inputEmployeeName", locators);
        WebElement btnView = getWebElement("btnView", locators);
        WebElement columnEmployeeName = getWebElement("columnEmployeeName", locators);
        WebElement columnTimesheetPeriod = getWebElement("columnTimesheetPeriod", locators);
        WebElement columnActions = getWebElement("columnActions", locators);

        softAssert.assertTrue(inputEmployeeName != null);
        softAssert.assertTrue(btnView != null);
        softAssert.assertTrue(columnEmployeeName != null);
        softAssert.assertTrue(columnTimesheetPeriod != null);
        softAssert.assertTrue(columnActions != null);
    }
}

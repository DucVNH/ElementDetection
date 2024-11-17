package page.orangehrm;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class ViewEmployeeList extends BasePage {
    public ViewEmployeeList(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement employeeInfo = getWebElement("employeeInfo", locators);
        WebElement inputEmployeeName = getWebElement("inputEmployeeName", locators);
        WebElement inputEmployeeId = getWebElement("inputEmployeeId", locators);
        WebElement selectEmploymentStatus = getWebElement("selectEmploymentStatus", locators);
        WebElement selectInclude = getWebElement("selectInclude", locators);
        WebElement inputSupervisorName = getWebElement("inputSupervisorName", locators);
        WebElement selectJobTitle = getWebElement("selectJobTitle", locators);
        WebElement selectSubUnit = getWebElement("selectSubUnit", locators);
        WebElement btnReset = getWebElement("btnReset", locators);
        WebElement btnSearch = getWebElement("btnSearch", locators);
        WebElement btnAdd = getWebElement("btnAdd", locators);
        WebElement columnLastName = getWebElement("columnLastName", locators);
        WebElement columnActions = getWebElement("columnActions", locators);
        WebElement rowNguyen = getWebElement("rowNguyen", locators);

        softAssert.assertTrue(employeeInfo != null);
        softAssert.assertTrue(inputEmployeeName != null);
        softAssert.assertTrue(inputEmployeeId != null);
        softAssert.assertTrue(selectEmploymentStatus != null);
        softAssert.assertTrue(selectInclude != null);
        softAssert.assertTrue(inputSupervisorName != null);
        softAssert.assertTrue(selectJobTitle != null);
        softAssert.assertTrue(selectSubUnit != null);
        softAssert.assertTrue(btnReset != null);
        softAssert.assertTrue(btnSearch != null);
        softAssert.assertTrue(btnAdd != null);
        softAssert.assertTrue(columnLastName != null);
        softAssert.assertTrue(columnActions != null);
        softAssert.assertTrue(rowNguyen != null);
    }
}

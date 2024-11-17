package page.mantisbt;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class ViewAllBugPage extends BasePage {
    public ViewAllBugPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement btnReset = getWebElement("btnReset", locators);
        WebElement btnSave = getWebElement("btnSave", locators);
        WebElement reporter = getWebElement("reporter", locators);
        WebElement assignedTo = getWebElement("assignedTo", locators);
        WebElement btnApplyFilter = getWebElement("btnApplyFilter", locators);
        WebElement inputSearch = getWebElement("inputSearch", locators);
        WebElement btnCSVExport = getWebElement("btnCSVExport", locators);
        WebElement btnSummary = getWebElement("btnSummary", locators);
        WebElement btnExcelExport = getWebElement("btnExcelExport", locators);
        WebElement category = getWebElement("category", locators);

        softAssert.assertTrue(btnReset != null);
        softAssert.assertTrue(btnSave != null);
        softAssert.assertTrue(reporter != null);
        softAssert.assertTrue(assignedTo != null);
        softAssert.assertTrue(btnApplyFilter != null);
        softAssert.assertTrue(inputSearch != null);
        softAssert.assertTrue(btnCSVExport != null);
        softAssert.assertTrue(btnSummary != null);
        softAssert.assertTrue(btnExcelExport != null);
        softAssert.assertTrue(category != null);
    }
}

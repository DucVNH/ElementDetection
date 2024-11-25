package page.mantisbt;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class SidePanel extends BasePage {
    public SidePanel(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement myView = getWebElement("myView", locators);
        WebElement viewIssues = getWebElement("viewIssues", locators);
        WebElement reportIssue = getWebElement("reportIssue", locators);
        WebElement changeLog = getWebElement("changeLog", locators);
        WebElement roadMap = getWebElement("roadMap", locators);
        WebElement summary = getWebElement("summary", locators);
        WebElement manage = getWebElement("manage", locators);

        softAssert.assertTrue(myView != null);
        softAssert.assertTrue(viewIssues != null);
        softAssert.assertTrue(reportIssue != null);
        softAssert.assertTrue(changeLog != null);
        softAssert.assertTrue(roadMap != null);
        softAssert.assertTrue(summary != null);
        softAssert.assertTrue(manage != null);
    }

    public void goToMyView() {
        clickElementByLocatorId("myView");
    }

    public void goToViewIssues() {
        clickElementByLocatorId("viewIssues");
    }

    public void goToReportIssue() {
        clickElementByLocatorId("reportIssue");
    }

    public void goToChangeLog() {
        clickElementByLocatorId("changeLog");
    }

    public void goToRoadMap() {
        clickElementByLocatorId("roadMap");
    }

    public void goToSummary() {
        clickElementByLocatorId("summary");
    }

    public void goToManage() {
        clickElementByLocatorId("manage");
    }
}

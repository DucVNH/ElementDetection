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
        waitForPageLoaded();
        WebElement element = getWebElement("myView", locators);
        clickOnElement(element);
        waitForPageLoaded();
    }

    public void goToViewIssues() {
        waitForPageLoaded();
        WebElement element = getWebElement("viewIssues", locators);
        clickOnElement(element);
        waitForPageLoaded();
    }

    public void goToReportIssue() {
        waitForPageLoaded();
        WebElement element = getWebElement("reportIssue", locators);
        clickOnElement(element);
        waitForPageLoaded();
    }

    public void goToChangeLog() {
        waitForPageLoaded();
        WebElement element = getWebElement("changeLog", locators);
        clickOnElement(element);
        waitForPageLoaded();
    }

    public void goToRoadMap() {
        waitForPageLoaded();
        WebElement myView = getWebElement("roadMap", locators);
        clickOnElement(myView);
        waitForPageLoaded();
    }

    public void goToSummary() {
        waitForPageLoaded();
        WebElement myView = getWebElement("summary", locators);
        clickOnElement(myView);
        waitForPageLoaded();
    }

    public void goToManage() {
        waitForPageLoaded();
        WebElement myView = getWebElement("manage", locators);
        clickOnElement(myView);
        waitForPageLoaded();
    }
}

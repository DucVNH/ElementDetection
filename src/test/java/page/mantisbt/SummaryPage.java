package page.mantisbt;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class SummaryPage extends BasePage {
    public SummaryPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement summaryTab = getWebElement("summaryTab", locators);
        WebElement testProject = getWebElement("testProject", locators);
        WebElement uatProject = getWebElement("uatProject", locators);
        WebElement minorSeverity = getWebElement("minorSeverity", locators);
        WebElement longestOpenIssue = getWebElement("longestOpenIssue", locators);
        WebElement longestOpen = getWebElement("longestOpen", locators);
        WebElement longestOpenTable = getWebElement("longestOpenTable", locators);
        WebElement reporterEffectiveness = getWebElement("reporterEffectiveness", locators);
        WebElement reporterBySolution = getWebElement("reporterBySolution", locators);
        WebElement developerBySolution = getWebElement("developerBySolution", locators);

        softAssert.assertTrue(summaryTab != null);
        softAssert.assertTrue(testProject != null);
        softAssert.assertTrue(uatProject != null);
        softAssert.assertTrue(minorSeverity != null);
        softAssert.assertTrue(longestOpenIssue != null);
        softAssert.assertTrue(longestOpen != null);
        softAssert.assertTrue(longestOpenTable != null);
        softAssert.assertTrue(reporterEffectiveness != null);
        softAssert.assertTrue(reporterBySolution != null);
        softAssert.assertTrue(developerBySolution != null);
    }
}

package page.admidio;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class AnnouncementsPage extends BasePage {
    public AnnouncementsPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement btnAddEntry = getWebElement("btnAddEntry", locators);
        WebElement btnEditCategories = getWebElement("btnEditCategories", locators);
        WebElement selectCategory = getWebElement("selectCategory", locators);
        WebElement loremIpsumHeader = getWebElement("loremIpsumHeader", locators);
        WebElement loremIpsumBody = getWebElement("loremIpsumBody", locators);
        WebElement testHeader = getWebElement("testHeader", locators);
        WebElement testBody = getWebElement("testBody", locators);

        softAssert.assertTrue(btnAddEntry != null);
        softAssert.assertTrue(btnEditCategories != null);
        softAssert.assertTrue(selectCategory != null);
        softAssert.assertTrue(loremIpsumHeader != null);
        softAssert.assertTrue(loremIpsumBody != null);
        softAssert.assertTrue(testHeader != null);
        softAssert.assertTrue(testBody != null);
    }
}

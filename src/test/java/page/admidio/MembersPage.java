package page.admidio;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class MembersPage extends BasePage {
    public MembersPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement btnCreateMember = getWebElement("btnCreateMember", locators);
        WebElement btnChangeHistory = getWebElement("btnChangeHistory", locators);
        WebElement btnImportMembers = getWebElement("btnImportMembers", locators);
        WebElement btnEditProfileFields = getWebElement("btnEditProfileFields", locators);
        WebElement checkboxShowAll = getWebElement("checkboxShowAll", locators);
        WebElement inputSearch = getWebElement("inputSearch", locators);
        WebElement btnPrevious = getWebElement("btnPrevious", locators);
        WebElement btnNext = getWebElement("btnNext", locators);

        softAssert.assertTrue(btnCreateMember != null);
        softAssert.assertTrue(btnChangeHistory != null);
        softAssert.assertTrue(btnImportMembers != null);
        softAssert.assertTrue(btnEditProfileFields != null);
        softAssert.assertTrue(checkboxShowAll != null);
        softAssert.assertTrue(inputSearch != null);
        softAssert.assertTrue(btnPrevious != null);
        softAssert.assertTrue(btnNext != null);
    }
}

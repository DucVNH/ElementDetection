package page.mantisbt;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class ManagePage extends BasePage {
    public ManagePage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement manageProjects = getWebElement("manageProjects", locators);
        clickOnElement(manageProjects);
        WebElement testProject = getWebElement("testProject", locators);
        WebElement testCategory = getWebElement("testCategory", locators);
        WebElement addCategoryBtn = getWebElement("addCategoryBtn", locators);
        softAssert.assertTrue(testProject != null);
        softAssert.assertTrue(testCategory != null);
        softAssert.assertTrue(addCategoryBtn != null);

        WebElement manageTags = getWebElement("manageTags", locators);
        clickOnElement(manageTags);
        WebElement regressionTag = getWebElement("regressionTag", locators);
        WebElement createTagBtn = getWebElement("createTagBtn", locators);
        softAssert.assertTrue(regressionTag != null);
        softAssert.assertTrue(createTagBtn != null);

        WebElement manageCustomFields = getWebElement("manageCustomFields", locators);
        clickOnElement(manageCustomFields);
        WebElement ipsumField = getWebElement("ipsumField", locators);
        WebElement loremField = getWebElement("loremField", locators);
        WebElement newCustomFieldBtn = getWebElement("newCustomFieldBtn", locators);
        softAssert.assertTrue(ipsumField != null);
        softAssert.assertTrue(loremField != null);
        softAssert.assertTrue(newCustomFieldBtn != null);
    }

    public void clickTabManageTags() {
        waitForPageLoaded();
        WebElement manageTags = getWebElement("manageTags", locators);
        clickOnElement(manageTags);
    }
}

package page.admidio;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class GroupsAndRolesPage extends BasePage {
    public GroupsAndRolesPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement btnCreateRole = getWebElement("btnCreateRole", locators);
        WebElement btnShowPermissions = getWebElement("btnShowPermissions", locators);
        WebElement btnEditCategories = getWebElement("btnEditCategories", locators);
        WebElement btnConfigureLists = getWebElement("btnConfigureLists", locators);
        WebElement selectRoleTypes = getWebElement("selectRoleTypes", locators);
        WebElement administrator = getWebElement("administrator", locators);
        WebElement member = getWebElement("member", locators);

        softAssert.assertTrue(btnCreateRole != null);
        softAssert.assertTrue(btnShowPermissions != null);
        softAssert.assertTrue(btnEditCategories != null);
        softAssert.assertTrue(btnConfigureLists != null);
        softAssert.assertTrue(selectRoleTypes != null);
        softAssert.assertTrue(administrator != null);
        softAssert.assertTrue(member != null);
    }
}

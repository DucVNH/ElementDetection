package page.admidio;

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
        WebElement overview = getWebElement("overview", locators);
        WebElement announcements = getWebElement("announcements", locators);
        WebElement docsAndFiles = getWebElement("docsAndFiles", locators);
        WebElement messages = getWebElement("messages", locators);
        WebElement photos = getWebElement("photos", locators);
        WebElement groupsAndRoles = getWebElement("groupsAndRoles", locators);
        WebElement events = getWebElement("events", locators);
        WebElement categoryReport = getWebElement("categoryReport", locators);
        WebElement webLinks = getWebElement("webLinks", locators);
        WebElement newRegistrations = getWebElement("newRegistrations", locators);
        WebElement members = getWebElement("members", locators);
        WebElement databaseBackup = getWebElement("databaseBackup", locators);
        WebElement menu = getWebElement("menu", locators);
        WebElement preferences = getWebElement("preferences", locators);

        softAssert.assertTrue(overview != null);
        softAssert.assertTrue(announcements != null);
        softAssert.assertTrue(docsAndFiles != null);
        softAssert.assertTrue(messages != null);
        softAssert.assertTrue(photos != null);
        softAssert.assertTrue(groupsAndRoles != null);
        softAssert.assertTrue(events != null);
        softAssert.assertTrue(categoryReport != null);
        softAssert.assertTrue(webLinks != null);
        softAssert.assertTrue(newRegistrations != null);
        softAssert.assertTrue(members != null);
        softAssert.assertTrue(databaseBackup != null);
        softAssert.assertTrue(menu != null);
        softAssert.assertTrue(preferences != null);
    }

    public void goToOverview() {
        clickElementByLocatorId("overview");
    }
    public void goToAnnouncements() {
        clickElementByLocatorId("announcements");
    }
    public void goToDocsAndFiles() {
        clickElementByLocatorId("docsAndFiles");
    }
    public void goToMessages() {
        clickElementByLocatorId("messages");
    }
    public void goToPhotos() {
        clickElementByLocatorId("photos");
    }
    public void goToGroupsAndRoles() {
        clickElementByLocatorId("groupsAndRoles");
    }
    public void goToEvents() {
        clickElementByLocatorId("events");
    }
    public void goToCategoryReport() {
        clickElementByLocatorId("categoryReport");
    }
    public void goToWeblinks() {
        clickElementByLocatorId("webLinks");
    }
    public void goToNewRegistrations() {
        clickElementByLocatorId("newRegistrations");
    }
    public void goToMembers() {
        clickElementByLocatorId("members");
    }
    public void goToDatabaseBackup() {
        clickElementByLocatorId("databaseBackup");
    }
    public void goToMenu() {
        clickElementByLocatorId("menu");
    }
    public void goToPreferences() {
        clickElementByLocatorId("preferences");
    }
}

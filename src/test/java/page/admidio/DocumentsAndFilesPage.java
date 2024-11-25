package page.admidio;

import core.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class DocumentsAndFilesPage extends BasePage {
    public DocumentsAndFilesPage(WebDriver driver) {
        super(driver);
    }

    public void verifyPage(SoftAssert softAssert) {
        waitForPageLoaded();
        WebElement btnUploadFiles = getWebElement("btnUploadFiles", locators);
        WebElement btnCreateFolder = getWebElement("btnCreateFolder", locators);
        WebElement btnPermissions = getWebElement("btnPermissions", locators);
        WebElement columnName = getWebElement("columnName", locators);
        WebElement columnSize = getWebElement("columnSize", locators);
        WebElement rowTST = getWebElement("rowTST", locators);
        WebElement rowUAT = getWebElement("rowUAT", locators);

        softAssert.assertTrue(btnUploadFiles != null);
        softAssert.assertTrue(btnCreateFolder != null);
        softAssert.assertTrue(btnPermissions != null);
        softAssert.assertTrue(columnName != null);
        softAssert.assertTrue(columnSize != null);
        softAssert.assertTrue(rowTST != null);
        softAssert.assertTrue(rowUAT != null);
    }
}

package page.orangehrm;

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
        WebElement admin = getWebElement("admin", locators);
        WebElement pim = getWebElement("pim", locators);
        WebElement leave = getWebElement("leave", locators);
        WebElement time = getWebElement("time", locators);
        WebElement recruitment = getWebElement("recruitment", locators);
        WebElement myInfo = getWebElement("myInfo", locators);
        WebElement performance = getWebElement("performance", locators);
        WebElement dashboard = getWebElement("dashboard", locators);
        WebElement directory = getWebElement("directory", locators);
        WebElement maintenance = getWebElement("maintenance", locators);
        WebElement buzz = getWebElement("buzz", locators);

        softAssert.assertTrue(admin != null);
        softAssert.assertTrue(pim != null);
        softAssert.assertTrue(leave != null);
        softAssert.assertTrue(time != null);
        softAssert.assertTrue(recruitment != null);
        softAssert.assertTrue(myInfo != null);
        softAssert.assertTrue(performance != null);
        softAssert.assertTrue(dashboard != null);
        softAssert.assertTrue(directory != null);
        softAssert.assertTrue(maintenance != null);
        softAssert.assertTrue(buzz != null);
    }
}

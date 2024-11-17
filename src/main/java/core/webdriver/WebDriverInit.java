package core.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverInit {
    public static WebDriver initWebDriver() {
        WebDriver driver = new ChromeDriver();
        return driver;
    }
}

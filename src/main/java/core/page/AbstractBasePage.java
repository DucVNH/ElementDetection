package core.page;

import core.helper.LogHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractBasePage {

    public static final int SMALL_TIMEOUT = 3;
    public static final int MEDIUM_TIMEOUT = 15;
    public static final int DEFAULT_TIMEOUT = 60;
    protected static final LogHelper logger = LogHelper.getInstance();
    protected RemoteWebDriver driver;

    public void initElements(RemoteWebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getWebName() {
        return this.getClass().getPackage().getName().split("\\.")[1];
    }

    public String getPageName() {
        return this.getClass().getSimpleName();
    }

    public void waitForPageLoaded(long timeout) {
        ExpectedCondition<Boolean> expectation = driverWait -> {
            assert driverWait != null;
            return ((JavascriptExecutor) driverWait).executeScript("return document.readyState").toString().equalsIgnoreCase("complete");
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.until(expectation);
        } catch (InterruptedException e) {
            logger.error("Timeout waiting for Page Load Request to complete. {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (WebDriverException e) {
            if (!e.getMessage().contains("target frame detached")) {
                throw new Error(e.getMessage());
            }
        }
    }

    public void waitForPageLoaded() {
        logger.info("Wait for page load");
        waitForPageLoaded(DEFAULT_TIMEOUT);
    }

    public WebElement waitForElementVisible(WebElement webElement, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public WebElement waitForElementVisible(WebElement webElement) {
        return waitForElementVisible(webElement, Duration.ofDays(SMALL_TIMEOUT));
    }

    public WebElement waitForElementClickable(WebElement webElement) {
        return waitForElementClickable(webElement, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WebElement waitForElementClickable(WebElement webElement, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    private void tryToClick(WebElement element, Duration timeout) {
        try {
            waitForElementClickable(element, timeout).click();
        } catch (StaleElementReferenceException e) {
            boolean isClicked = false;
            int loop = 3;
            while (loop > 0 && !isClicked) {
                try {
                    waitForElementRefreshedAndVisible(element, timeout);
                    waitForElementClickable(element, timeout).click();
                    isClicked = true;
                } catch (StaleElementReferenceException | TimeoutException ignored) {
                    logger.info("Attempting to click on element");
                    loop--;
                }
            }
            if (!isClicked) {
                logger.fail("Click on element {} as failed", element);
            }
        } catch (ElementClickInterceptedException e2) {
            clickWithJavascript(element);
        }
    }

    public void clickOnElement(WebElement webElement, int timeout) {
        tryToClick(webElement, Duration.ofSeconds(timeout));
    }

    public void clickOnElement(WebElement webElement) {
        clickOnElement(webElement, MEDIUM_TIMEOUT);
    }

    public void clickWithJavascript(WebElement webElement) {
        JavascriptExecutor jsExecutor = driver;
        String script = "arguments[0].click();";
        try {
            jsExecutor.executeScript(script, webElement);
            Thread.sleep(200);
        } catch (StaleElementReferenceException se) {
            waitForElementRefreshedAndVisible(webElement);
            jsExecutor.executeScript(script, webElement);
        } catch (WebDriverException e) {
            logger.fail(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isVisible(WebElement element, Duration timeout) {
        boolean isVisible = false;
        try {
            waitForElementVisible(element, timeout);
            isVisible = true;
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ignored) {
            //ignored
        }
        return isVisible;
    }

    public boolean isElementPresent(By element, Duration timeout) {
        boolean isPresent = false;
        try {
            waitForElementPresent(element, timeout);
            isPresent = true;
        } catch (NoSuchElementException | TimeoutException ignored) {
            //ignored
        }
        return isPresent;
    }

    public void sendKeys(WebElement element, String keysToSend) {
        waitForElementVisible(element);
        waitForElementClickable(element).clear();
        element.sendKeys(keysToSend);
    }

    public void verifyElementVisible(WebElement element, SoftAssert softAssert) {
        softAssert.assertTrue(isVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT)),
                String.format("Element is not visible."));
    }

    public WebElement waitForElementPresent(By element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.presenceOfElementLocated(element));
    }

    public WebElement waitForElementRefreshedAndVisible(WebElement element) {
        return waitForElementRefreshedAndVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WebElement waitForElementRefreshedAndVisible(WebElement element, Duration timeOut) {
        return new WebDriverWait(driver, timeOut)
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
    }

    public static String getLabel(WebElement element) {
        if (element.findElements(By.xpath("./parent::div//label")).size() > 0) {
            return element.findElement(By.xpath("./parent::div//label")).getText();
        } else if (element.findElements(By.xpath("./parent::div/parent::div//label")).size() > 0) {
            return element.findElement(By.xpath("./parent::div/parent::div//label")).getText();
        } else if (element.findElements(By.xpath("./parent::div/parent::div/parent::div//label")).size() > 0) {
            return element.findElement(By.xpath("./parent::div/parent::div/parent::div//label")).getText();
        } else if (element.findElements(By.xpath("./parent::div/parent::div/parent::div/parent::div//label")).size() > 0) {
            return element.findElement(By.xpath("./parent::div/parent::div/parent::div/parent::div//label")).getText();
        } else if (element.findElements(By.xpath("./parent::div/parent::div/parent::div/parent::div/parent::div//label")).size() > 0) {
            return element.findElement(By.xpath("./parent::div/parent::div/parent::div/parent::div/parent::div//label")).getText();
        } else {
            return null;
        }
    }

    public static String getAbsoluteXPath(WebElement element) {
        StringBuilder xpath = new StringBuilder();
        while (element != null) {
            // Get the tag name of the current element
            String tagName = element.getTagName();

            // Get the index of the element among its siblings
            String index = getElementIndex(element);

            // Build the XPath segment for the current element
            xpath.insert(0, "/" + tagName + (index.isEmpty() ? "" : "[" + index + "]"));

            // Attempt to find the parent element
            try {
                element = element.findElement(By.xpath("..")); // Move up to the parent
            } catch (Exception e) {
                // If the parent cannot be found, we can assume we reached the root
                element = null; // Exit loop
            }
        }
        return xpath.toString();
    }

    private static String getElementIndex(WebElement element) {
        List<WebElement> siblings = element.findElements(By.xpath("preceding-sibling::" + element.getTagName()));
        return String.valueOf(siblings.size() + 1); // 1-based index
    }

    // Helper method to get attributes of a WebElement
    public static List<String> getElementAttributes(WebElement element) {
        List<String> attributes = new ArrayList<>();
        List<String> attributeNames = Arrays.asList("id", "name", "class", "href", "src", "style", "type", "value", "title", "alt", "placeholder", "role");

        for (String attrName : attributeNames) {
            if (attrName.equals("href") && element.getAttribute("href") != null) {
                String attrValue = element.getAttribute("outerHTML");
                if (attrValue != null && attrValue.contains("href"))
                    attributes.add(attrName + "=\"" + attrValue.split("href=\"")[1].split("\"")[0] + "\"");
            } else {
                String attrValue = element.getAttribute(attrName);
                if (attrValue != null)
                    attributes.add(attrName + "=\"" + attrValue + "\"");
            }
        }

        return attributes;
    }

    // Helper method to get the position of the element among its siblings
    public static int getElementPosition(WebElement element, List<WebElement> siblings) {
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i).equals(element)) {
                return i + 1; // XPath positions are 1-based
            }
        }
        return -1; // Return -1 if not found
    }

    // Dummy method for getPositionStar (you can define your own logic)
    public static int getElementPositionStar(WebElement element, List<WebElement> siblings) {
        return getElementPosition(element, siblings); // Placeholder: can customize this logic
    }
}
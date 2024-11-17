package core.page;

import core.common.CommonActions;
import core.helper.HttpHelper;
import core.helper.LogHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AbstractBasePage {

    public static final int SMALL_TIMEOUT = 3;
    public static final int MEDIUM_TIMEOUT = 15;
    public static final int DEFAULT_TIMEOUT = 60;
    public static final int LONG_TIMEOUT = 600;
    protected static final LogHelper logger = LogHelper.getInstance();
    protected RemoteWebDriver driver;

    public void initElements(RemoteWebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    protected void waitUtil(Function<? super WebDriver, ? extends Object> isTrue) {
        waitUtil(isTrue, Duration.ofSeconds(10));
    }

    protected void waitUtil(Function<? super WebDriver, ? extends Object> isTrue, Duration timeOutInSeconds) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutInSeconds);
        webDriverWait.until(isTrue);
    }

    private static final String TIME_OUT_EXCEPTION = "Timeout exception {}";
    private static final String INNER_HTML = "innerHTML";
    private static final String VALUE_ATTR = "value";

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

    public WebElement waitForElementVisible(String dynamicXpath, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXpath)));
    }

    public WebElement waitForElementVisible(String dynamicXpath) {
        return this.waitForElementVisible(dynamicXpath, Duration.ofDays(DEFAULT_TIMEOUT));
    }
    
    public int getNumberOfElement(String xpath){
        int NoOfElement =0;
        try{
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            NoOfElement = elements.size();
        }catch (NoSuchElementException e){
            NoOfElement = 0;
        }catch(Exception e){
            logger.info(e.getMessage());
        }
        return NoOfElement;
    }
    public boolean isElementExisted(String xpath){
        int noOfElements = getNumberOfElement(xpath);
        if(noOfElements>0){
            return true;
        }
        return false;
    }

    public boolean isElementExisted(WebElement element){
        boolean present;
        try {
            element.isDisplayed();
            present = true;
        } catch (NoSuchElementException e) {
            present = false;
        }
        return present;
    }
    public WebElement waitForElementVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForOneOfElementsVisible(String dynamicXpath) {
        return waitForOneOfElementsVisible(By.xpath(dynamicXpath));
    }

    public WebElement waitForOneOfElementsVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        ExpectedCondition<WebElement> expectedCondition = driverWait -> {
            assert driver != null;
            return driver.findElements(locator).stream().filter(WebElement::isDisplayed).findAny().orElse(null);
        };
        return wait.until(expectedCondition);
    }

    public List<WebElement> waitForNestedElementsVisible(WebElement webElement, String childXpath) {
        return waitForNestedElementsVisible(webElement, By.xpath(childXpath));
    }

    public List<WebElement> waitForNestedElementsVisible(WebElement webElement, By locator) {
        return waitForNestedElementsVisible(webElement, locator, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public List<WebElement> waitForNestedElementsVisible(WebElement webElement, By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(webElement, locator));
    }

    public WebElement waitForNestedElementsPresent(WebElement webElement, By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(webElement, locator));
    }

    public WebElement waitForNestedElementsPresent(WebElement webElement, String childXpath) {
        return waitForNestedElementsPresent(webElement, By.xpath(childXpath), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WebElement waitForFirstNestedElementsVisible(WebElement webElement, String childXpath) {
        return waitForElementVisible(waitForNestedElementsPresent(webElement, childXpath));
    }

    public WebElement waitForElementClickable(String locator) {
        return waitForElementClickable(waitForElementVisible(locator), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WebElement waitForElementClickable(WebElement webElement) {
        return waitForElementClickable(webElement, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WebElement waitForElementClickable(WebElement webElement, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public Boolean waitUntilPageContainsText(String strText){
        return waitUntilPageContainsText(strText, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public Boolean waitUntilPageContainsText(String strText, Duration timeout){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.getPageSource().contains(strText);
            }
        });
    }

    public void clickOnVisibleElement(WebElement element, Duration timeout) {
        waitForElementVisible(element, timeout).click();
    }

    public void clickOnVisibleElement(WebElement element) {
        clickOnElement(waitForElementVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT)));
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

    public void scrollToElement(WebElement element) {
        scrollToElementByJavaScript(element);
        int headerVerticalSize = 0;
        Actions actions = new Actions(driver);
        actions.moveToElement(element, 0, -1 * headerVerticalSize).build().perform();
    }
    public void scrollToElement(String xpath) {
        WebElement e = driver.findElement(By.xpath(xpath));
        int headerVerticalSize = 0;
        Actions actions = new Actions(driver);
        actions.moveToElement(e, 0, -1 * headerVerticalSize).build().perform();
    }

    public void scrollToElementByJavaScript(WebElement element) {
        driver.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void hoverElement(WebElement element) {
        Actions actionHover = new Actions(driver);
        actionHover.moveToElement(element).build().perform();
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

    public void clickOnVisibleElementJS(WebElement webElement, Duration timeout) {
        try {
            waitForElementClickable(webElement, timeout);
        } catch (StaleElementReferenceException stale) {
            waitForElementRefreshedAndVisible(webElement, timeout);
            waitForElementClickable(webElement, timeout);
        }
        clickWithJavascript(webElement, timeout);
    }

    public void clickOnVisibleElementJS(WebElement webElement) {
        clickOnVisibleElementJS(webElement, Duration.ofSeconds(MEDIUM_TIMEOUT));
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

    public boolean isVisible(WebElement element) {
        return isVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public boolean isVisible(String dynamicXpath, Duration timeout) {
        boolean isVisible = false;
        try {
            waitForElementVisible(dynamicXpath, timeout);
            isVisible = true;
        } catch (StaleElementReferenceException | NoSuchElementException | TimeoutException ignored) {
            //ignored
        }
        return isVisible;
    }

    public boolean isVisible(String dynamicXpath) {
        return isVisible(dynamicXpath, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForElementIsDisabled(WebElement element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        ExpectedCondition<Boolean> elementIsDisabled = driverWait -> {
            boolean isDisabled = false;
            try {
                isDisabled = !element.isEnabled();
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {
                //ignored
            }
            return isDisabled;
        };
        try {
            wait.until(elementIsDisabled);
        } catch (TimeoutException e) {
            logger.fail("Element should be disabled");
        }
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

    public boolean isElementPresent(By element) {
        return isElementPresent(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public boolean isElementClickable(WebElement element) {
        return element.isEnabled();
    }

    public void selectOptionByLabel(WebElement element, String label) {
        Select select = new Select(element);
        select.selectByVisibleText(label);
    }

    public void selectOptionByIndex(WebElement element, int index) {
        Select select = new Select(element);
        select.selectByIndex(index - 1);
    }

    public void selectOptionByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByValue(value);
    }

    public void sendKeysOnVisibleElement(WebElement webElement, String keys) {
        waitForElementVisible(webElement).clear();
        webElement.sendKeys(keys);
    }

    public void sendKeysOnVisibleElement(WebElement webElement, String keys, Duration timeout) {
        waitForElementVisible(webElement, timeout).clear();
        webElement.sendKeys(keys);
    }

    public void sendKeys(WebElement element, String keysToSend) {
        waitForElementVisible(element);
        waitForElementClickable(element).clear();
        element.sendKeys(keysToSend);
    }

    public void clearByBackspace(WebElement element) {
        waitForElementVisible(element);
        while (element.getAttribute(VALUE_ATTR).length() > 0) {
            element.sendKeys(Keys.BACK_SPACE);
        }
    }

    public void waitNumberOfWindow(int numberOfWindow, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindow));
    }

    public void waitNumberOfWindow(int numberOfWindow) {
        waitNumberOfWindow(numberOfWindow, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public boolean isNumberOfWindowToBe(int numberOfWindow, Duration timeout) {
        boolean ret = false;
        try {
            waitNumberOfWindow(numberOfWindow, timeout);
            ret = true;
        } catch (TimeoutException e) {
            logger.info(TIME_OUT_EXCEPTION, e.getMessage());
        }
        return ret;
    }


    public int getNumberOfWindow() {
        return driver.getWindowHandles().size();
    }

    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    public void waitForWindowTitle(String windowTitle) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.titleContains(windowTitle));
    }

    public void waitForElementNotPresent(By element) {
        waitForElementNotPresent(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForElementNotPresent(By element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
    }

    public boolean waitForElementAttributeContains(WebElement element, String attribute, String value) {
        return waitForElementAttributeContains(element, attribute, value, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public boolean waitForElementAttributeContains(WebElement element, String attribute, String value, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public void waitForElementAttributeContains(By element, String attribute, String value, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    public void waitForElementAttributeContains(By element, String attribute, String value) {
        waitForElementAttributeContains(element, attribute, value, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public boolean waitForElementAttributeToBe(WebElement element, String attribute, String value, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public boolean waitForElementAttributeToBe(WebElement element, String attribute, String value) {
        return waitForElementAttributeToBe(element, attribute, value, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForElementAttributeNotContains(WebElement element, String attribute, String value, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        ExpectedCondition<Boolean> expectedCondition = driverWait -> {
            String currentValue = element.getAttribute(attribute);
            if (currentValue == null) {
                currentValue = element.getCssValue(attribute);
            }
            return !currentValue.contains(value);
        };
        wait.until(expectedCondition);
    }

    public void waitForElementAttributeNotContains(WebElement element, String attribute, String value) {
        waitForElementAttributeNotContains(element, attribute, value, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForElementIsNotDisplayed(WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        ExpectedCondition<Boolean> elementIsNotDisplayed = driverWait -> {
            try {
                webElement.isDisplayed();
                return false;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return true;
            }
        };
        wait.until(elementIsNotDisplayed);
    }

    public boolean isElementEnable(WebElement webElement) {
        return webElement.isEnabled();
    }

    public void waitForElementHasText(WebElement webElement) {
        waitForElementHasText(webElement, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForElementHasText(WebElement element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        ExpectedCondition<Boolean> hasText = driverWait -> {
            String value = element.getText();
            if (null == value || value.isEmpty()) {
                value = element.getAttribute(VALUE_ATTR);
            }

            if (null == value || value.isEmpty()) {
                value = element.getAttribute("innerText");
            }

            return value != null && !value.isEmpty();
        };
        wait.until(hasText);
    }

    public void waitForElementHasText(WebElement webElement, String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.until(ExpectedConditions.attributeToBe(webElement, INNER_HTML, text));
    }

    public void waitForTextToBePresentInElementLocated(By webElement, String text) {
        waitForTextToBePresentInElementLocated(waitForElementPresent(webElement), text);
    }

    public void waitForTextToBePresentInElementLocated(WebElement element, String text, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOf(element));
        ExpectedCondition<Boolean> textToBePresent = driverWait -> {
            String value = element.getText();
            if (null == value || !value.contains(text)) {
                value = element.getAttribute(VALUE_ATTR);
            }

            if (null == value || !value.contains(text)) {
                value = element.getAttribute("innerText");
            }

            return value != null && value.contains(text);
        };
        wait.until(textToBePresent);
    }

    public void waitForTextToBePresentInElementLocated(WebElement element, String text) {
        waitForTextToBePresentInElementLocated(element, text, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void switchToNextWindow() {
        String currentWindowHandle = driver.getWindowHandle();
        List<String> windowHandles = getWindowHandles();
        windowHandles.remove(currentWindowHandle);
        driver.switchTo().window(windowHandles.get(windowHandles.size() - 1));
    }

    public void switchToAnotherWindow(String parentWindow) {
        switchToNextWindow();
    }

    public void switchToNewWindow(List<String> currentWindowHandles) {
        waitNumberOfWindow(currentWindowHandles.size() + 1);
        List<String> windowHandles = getWindowHandles();
        windowHandles.removeAll(currentWindowHandles);
        switchToWindow(windowHandles.get(0));
        waitForPageLoaded();
    }

    public void switchToFrame(WebElement element) {
        waitForElementVisible(element);
        driver.switchTo().frame(element);
    }

    public void waitForElementInvisible(WebElement webElement) {
        waitForElementInvisible(webElement, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForElementInvisible(WebElement webElement, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        ExpectedCondition<Boolean> invisibilityOfElement = driverWait -> {
            try {
                return !webElement.isDisplayed();
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {
                return true;
            }
        };

        wait.until(invisibilityOfElement);
    }

    public void waitForElementInvisible(By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        ExpectedCondition<Boolean> invisibilityOfElement = driverWait -> {
            try {
                return !driver.findElement(locator).isDisplayed();
            } catch (StaleElementReferenceException | NoSuchElementException ignored) {
                return true;
            }
        };

        wait.until(invisibilityOfElement);
    }

    public boolean isInvisible(By locator, Duration timeOut) {
        boolean result = false;
        try {
            waitForElementInvisible(locator, timeOut);
            result = true;
        } catch (TimeoutException ignored) {
            //ignored
        }
        return result;
    }

    public boolean isInvisible(WebElement element, Duration timeOut) {
        boolean result = false;
        try {
            waitForElementInvisible(element, timeOut);
            result = true;
        } catch (TimeoutException ignored) {
            //ignored
        }
        return result;
    }

    public boolean isInvisible(String xpath, Duration timeout) {
        return isInvisible(By.xpath(xpath), timeout);
    }

    public int getNumberOfSelectedOption(WebElement webElement) {
        return new Select(webElement).getAllSelectedOptions().size();
    }

    public void verifyTextVisible(String expectedText, WebElement element, Duration timeout) {
        waitForTextToBePresentInElementLocated(element, expectedText);
        String actualText = waitForElementVisible(element, timeout).getText().trim();
        Assert.assertTrue(actualText.contains(expectedText),
                String.format("Actual: %s, Expected: %s", actualText, expectedText));
    }

    public void verifyTextVisible(String expectedText, WebElement element, String errorMessage) {
        String actualText = waitForElementVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT)).getText().trim();
        Assert.assertTrue(actualText.contains(expectedText), errorMessage);
    }

    public void verifyTextVisible(String expectedText, WebElement element) {
        verifyTextVisible(expectedText, element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void verifyElementVisible(WebElement element, SoftAssert softAssert) {
//        Assert.assertTrue(isVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT)));
        softAssert.assertTrue(isVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT)),
                String.format("Element is not visible."));
    }

    public void verifyElementVisible(WebElement element, Duration timeout) {
        Assert.assertTrue(isVisible(element, timeout));
    }

    public void verifyElementNotVisible(WebElement element) {
        Assert.assertFalse(isVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT)));
    }

    public void verifyElementNotVisible(WebElement element, Duration timeout) {
        Assert.assertFalse(isVisible(element, timeout));
    }

    public void switchToWindowTitle(String title) {
        String currentWindow = driver.getWindowHandle();  //will keep current window to switch back
        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getTitle().equals(title)) {
                //This is the one you're looking for
                break;
            } else {
                driver.switchTo().window(currentWindow);
//                logger.error("Could not found windows with title: " + title);
            }
        }
    }

    public List<WebElement> waitForAllElementsVisible(List<WebElement> webElements, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public List<WebElement> waitForAllElementsVisible(List<WebElement> webElements) {
        return waitForAllElementsVisible(webElements, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForAllElementsInvisible(List<WebElement> webElements, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.invisibilityOfAllElements(webElements));
    }

    public void waitForAllElementsInvisible(List<WebElement> webElements) {
        waitForAllElementsInvisible(webElements, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    private boolean waitForUrlContains(String url, Duration timeOutInSeconds, Duration sleepInMillis) {
        logger.info("Wait for url contains: " + url);
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds, sleepInMillis);

        ExpectedCondition<Boolean> expectedCondition = driverWait -> {
            String currentUrl = "";
            try {
                currentUrl = URLDecoder.decode(getCurrentUrl(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.info(e.getMessage());
            }

            if (currentUrl != null && currentUrl.contains(url)) {
                logger.info("Current url is: {}", currentUrl);
                return true;
            } else {
                return false;
            }
        };
        return wait.until(expectedCondition);
    }

    public boolean waitForUrlContains(String url, Duration timeOutInSeconds) {
        return waitForUrlContains(url, timeOutInSeconds, Duration.ofMillis(500));
    }

    public boolean waitForUrlContains(String url) {
        return waitForUrlContains(url, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForUrlRedirectsContains(String url) {
        waitForUrlContains(url, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(500));
    }

    public List<String> getWindowHandles() {
        return new ArrayList<>(driver.getWindowHandles());
    }

    public void waitForUrlChange(String url, Duration timeOutInSeconds) {
        logger.info("Wait for url different from: {}", url);
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);

        ExpectedCondition<Boolean> expectedCondition = driverWait -> {
            String currentUrl = getCurrentUrl();

            if (!currentUrl.equals(url)) {
                logger.info("Current url is: {}", currentUrl);
                return true;
            } else {
                return false;
            }
        };
        wait.until(expectedCondition);
    }

    public void waitForUrlChange(String url) {
        waitForUrlChange(url, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }


    public int getCurrentWindowIndex() {
        String currentWindowHandle = driver.getWindowHandle();
        int index = 0;
        for (String winHandle : driver.getWindowHandles()) {
            index = index + 1;
            if (winHandle.equalsIgnoreCase(currentWindowHandle)) {
                break;
            }
        }
        return index;
    }

    public void switchToWindowIndex(int index) {
        String handle = (String) driver.getWindowHandles().toArray()[index - 1];
        driver.switchTo().window(handle);
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public void openUrlInNewTab(String url) {
        driver.executeScript("window.open();");
//        switchToWindowTitle("");
        switchToNextWindow();
        this.navigateToUrl(url);
        waitForPageLoaded();
        CommonActions.holdOn(SMALL_TIMEOUT);
    }

    public WebElement waitForElementPresent(By element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.presenceOfElementLocated(element));
    }

    public WebElement waitForElementPresent(By element) {
        return waitForElementPresent(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WebElement waitForElementPresentByXpath(String dynamicXpath) {
        return waitForElementPresentByXpath(dynamicXpath, Duration.ofSeconds(MEDIUM_TIMEOUT));
    }

    public WebElement waitForElementPresentByXpath(String dynamicXpath, Duration timeout) {
        return waitForElementPresent(By.xpath(dynamicXpath), timeout);
    }

    public void dragAndDropElement(WebElement elementFrom, WebElement elementTo) {
        Actions actions = new Actions(driver);
        actions.dragAndDrop(elementFrom, elementTo).build().perform();
    }

    public boolean isNestedElementVisible(WebElement parentElement, By locator, Duration timeout) {
        boolean isNested = false;
        try {
            waitForNestedElementsVisible(parentElement, locator, timeout);
            isNested = true;
        } catch (TimeoutException ignored) {
            //ignored
        }
        return isNested;
    }

    public boolean isNestedElementVisible(WebElement parentElement, String childXpath) {
        return isNestedElementVisible(parentElement, By.xpath(childXpath), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public boolean isNestedElementPresent(WebElement parentElement, By locator, Duration timeout) {
        boolean isNested = false;
        try {
            waitForNestedElementsPresent(parentElement, locator, timeout);
            isNested = true;
        } catch (TimeoutException ignored) {
            //ignored
        }
        return isNested;
    }

    public void acceptBrowserAlert() {
        acceptBrowserAlert(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void acceptBrowserAlert(Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            logger.info("The alert is not presented: " + e.getMessage());
        }
    }

    public void dismissBrowserAlert() {
        dismissBrowserAlert(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void dismissBrowserAlert(Duration timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
        } catch (TimeoutException e) {
            logger.info("The alert is not presented: " + e.getMessage());
        }
    }

    public void clickUsingActions(WebElement element) {
        Actions actions = new Actions(driver);
        waitForElementClickable(element);
        actions.moveToElement(element).click(element).perform();
    }

    /**
     * Close the current tab
     */
    public void closeCurrentTab() {
        int currentWindowIndex = getCurrentWindowIndex();
        JavascriptExecutor executor = driver;
        executor.executeScript("window.close();");
        switchToWindow(getWindowHandles().get(0));
    }

    public void swithToFirstTab() {
        driver.switchTo().window("0");
    }

    public void closeOtherTabs() {
        String currentWindowHandle = getCurrentWindowHandle();
        List<String> windowHandles = getWindowHandles();

        windowHandles.remove(currentWindowHandle);
        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            driver.close();
        }
        driver.switchTo().window(currentWindowHandle);
    }

    public void waitForElementNotStale(WebElement element) {
        waitForElementNotStale(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForElementNotStale(WebElement element, Duration timeOut) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeOut);
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element)));
        } catch (TimeoutException e) {
            logger.info("No Stale element");
        }
    }

    public WebElement getFirstSelectedOption(WebElement element) {
        return new Select(element).getFirstSelectedOption();
    }

    public List<String> getTextFromListElements(List<WebElement> listElements) {
        return listElements.stream().map(e -> e.getText().trim()).collect(Collectors.toList());
    }

    public List<String> getTextFromListElements(By locator) {
        return getTextFromListElements(driver.findElements(locator));
    }

    /*
     * Return link status by sending a request with different request method
     * First, try to use OPTIONs. It's fast but sometimes not work with Padi resource
     * Then, try to use GET. It's slow but work most of time
     */

    public boolean isLinkActive(String txtUrl) {
        if (HttpHelper.isLinkActive(txtUrl, "OPTIONS")) {
            return true;
        } else return HttpHelper.isLinkActive(txtUrl, "GET");
    }

    public WebElement waitForElementRefreshedAndClickable(WebElement element) {
        return waitForElementRefreshedAndClickable(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public WebElement waitForElementRefreshedAndClickable(WebElement element, Duration timeOut) {
        return new WebDriverWait(driver, timeOut)
                .until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
    }

    public WebElement waitForElementRefreshedAndVisible(WebElement element) {
        return waitForElementRefreshedAndVisible(element, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public boolean waitForElementIsStale(WebElement element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until(ExpectedConditions.stalenessOf(element));
            logger.info(ExpectedConditions.stalenessOf(element).toString());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public WebElement waitForElementRefreshedAndVisible(WebElement element, Duration timeOut) {
        return new WebDriverWait(driver, timeOut)
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
    }

    public void setWindowSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    public List<WebElement> waitForAllElementsPresent(By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public List<WebElement> waitForAllElementsPresent(String dynamicXpath) {
        return waitForAllElementsPresent(By.xpath(dynamicXpath), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public List<WebElement> waitForAllElementsPresent(By locator) {
        return waitForAllElementsPresent(locator, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForAlert(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public void waitForAlert() {
        this.waitForAlert(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void navigateToUrl(String url) {
        driver.navigate().to(url);
    }

    public void navigateBack() {
        waitForPageLoaded();
        driver.navigate().back();
    }

    public void switchToWindowUrl(String url) {
        Set<String> windowUrls = driver.getWindowHandles();
        List<String> winUrls = new ArrayList<>(windowUrls);
        for (int i = 0; i <= windowUrls.size(); i++) {
            if (i == windowUrls.size()) {
                logger.info("Could not found windows with url: " + url);
            }
            if (driver.switchTo().window(winUrls.get(i)).getCurrentUrl().contains(url)) {
                logger.info("This is the one we're looking for");
                break;
            }
        }
    }

    public void verifyNestedElementVisible(WebElement element, String childXpath) {
        Assert.assertTrue(isNestedElementVisible(element, childXpath));
    }

    public void waitForJQueryToLoad(Duration timeout) {
        (new WebDriverWait(driver, timeout)).until(driverWait -> {
            JavascriptExecutor js = (JavascriptExecutor) driverWait;
            return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
        });
    }

    public boolean isElementAttributeContains(WebElement element, String attribute, String value, Duration timeout) {
        boolean isContains = false;
        try {
            waitForElementAttributeContains(element, attribute, value, timeout);
            isContains = true;
        } catch (TimeoutException ignored) {
            //ignored
        }
        return isContains;
    }

    public boolean isElementAttributeContains(WebElement element, String attribute, String value) {
        return isElementAttributeContains(element, attribute, value, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getUrlOriginByJs() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return window.location.origin");
    }

    public void sendKeysByAction(WebElement element, String value) {
        Actions actions = new Actions(driver);
        element.clear();
        actions.click(element).pause(1000).sendKeys(value).perform();
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    public void switchToFirstWindow() {
        List<String> listWindows = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(listWindows.get(0));
    }

    public void clickWithJavascript(WebElement webElement, Duration timeout) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(String.format("var el=arguments[0]; setTimeout(function() { el.click(); }, %s);", timeout.toString()), webElement);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public void verifyCurrentURLIsCorrect(String url) {
        Assert.assertTrue(this.waitForUrlContains(url),
                String.format("Expected %s but found %s", url, driver.getCurrentUrl()));
    }

    public void verifyElementAttributeNotExist(String dynamicXpath, String attribute, Duration timeout) {
        Assert.assertNull(waitForElementVisible(dynamicXpath, timeout).getAttribute(attribute));
    }

    public String getElementAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    public void verifyElementAttributeNotExist(String dynamicXpath, String attribute) {
        verifyElementAttributeNotExist(dynamicXpath, attribute, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForNumberOfElementToBe(By element, int number) {
        waitForNumberOfElementToBe(element, number, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public void waitForNumberOfElementToBe(By element, int number, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.numberOfElementsToBe(element, number));
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

    /*
    // Helper to get the position of an element among its siblings
    private static int getElementPosition(WebElement element) {
        WebElement parent = element.findElement(By.xpath(".."));
        List<WebElement> siblings = parent.findElements(By.xpath("./*"));

        int position = 1;  // XPath positions start from 1
        for (WebElement sibling : siblings) {
            if (sibling.equals(element)) {
                return position;
            }
            position++;
        }
        return -1;
    }
    */
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
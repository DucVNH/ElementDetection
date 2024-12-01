package core.page;

import core.entity.ElementLocator;
import core.entity.PageLocators;
import core.helper.JsonHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static core.entity.PageLocators.getElementLocator;
import static core.entity.PageLocators.getMontotoXPath;
import static core.entity.PageLocators.getRobulaXPath;
import static core.entity.PageLocators.updateXPath;
import static core.helper.FileUtils.fileExists;
import static core.helper.ScreenshotUtils.screenshot;
import static core.helper.algorithm.montoto.Montoto.generateMontotoXPath;
import static core.helper.algorithm.robula.UtilsROBULAPlusNew.generateRobulaXPath;

public class BasePage extends AbstractBasePage {
    protected PageLocators locators; // Shared locators for the current page
    public static long totalExecutionTime = 0; // Cumulative time taken
    public static int totalExecutions = 0;     // Total number of calls

    public BasePage(WebDriver driver) {
        this.driver = (RemoteWebDriver) driver;

        String webName = getClass().getPackage().getName().split("\\.")[1]; // Dynamically retrieve package name
        String pageName = getClass().getSimpleName();       // Dynamically retrieve class name

        this.locators = JsonHelper.loadLocators(webName, pageName);
        this.locators.setWebName(webName);
        this.locators.setPageName(pageName);
    }

    public void init(WebDriver driver) {
        logger.info("Initializing Page: {}", this.getClass().getName());
        super.driver = (RemoteWebDriver) driver;
        initElements((RemoteWebDriver) driver);
    }

    public void refreshPage() {
        logger.info("Refresh page");
        driver.navigate().refresh();
        waitForPageLoaded();
    }

    public void waitFileExist(String strFilePath) throws InterruptedException {
        waitFileExist(strFilePath, 30);
    }
    public void waitFileExist(String strFilePath, int timeout) throws InterruptedException {
        int count = 0;
        while(!fileExists(strFilePath) && count < timeout){
            Thread.sleep(1000);
            count++;
        }
    }

    public void getScreenshot() throws IOException {
        screenshot(driver);
    }

    public void printPageSource(){
        logger.info(driver.getPageSource());
    }

    public String pageVisibility() {
        return (String) driver.executeScript("return document.visibilityState");
    }

    public void clickButton(String strButtonName){
        Actions actions = new Actions(driver);
        switch (strButtonName){
            case "Home":
                actions.sendKeys(Keys.HOME).perform();
                break;
            case "End":
                actions.sendKeys(Keys.END).perform();
                break;
            default:
                break;
        }
    }

    public WebElement getWebElement(String elementId, PageLocators locators) {
        try {
            String robulaXpath = getRobulaXPath(elementId, locators);
            String montotoXpath = getMontotoXPath(elementId, locators);
            List<WebElement> robulaResult = driver.findElements(By.xpath(robulaXpath));
            List<WebElement> montotoResult = driver.findElements(By.xpath(montotoXpath));
            if (robulaResult.size() == 1 && montotoResult.size() == 1) {
                if (robulaResult.get(0).equals(montotoResult.get(0)))
                    return robulaResult.get(0);
                else {
                    ElementLocator oldElement = getElementLocator(elementId, locators);
                    int robulaScore = calculateScore(robulaResult.get(0), oldElement);
                    int montotoScore = calculateScore(robulaResult.get(0), oldElement);
                    return robulaScore > montotoScore ? robulaResult.get(0) : montotoResult.get(0);
                }
            } else if (robulaResult.size() == 1 && montotoResult.size() != 1) {
                updateSingleLocator(elementId, robulaResult.get(0), locators, "Montoto");
                return robulaResult.get(0);
            } else if (robulaResult.size() != 1 && montotoResult.size() == 1) {
                updateSingleLocator(elementId, montotoResult.get(0), locators, "Robula");
                return montotoResult.get(0);
            } else if (robulaResult.size() != 1 && montotoResult.size() != 1) {
                throw new NoSuchElementException(String.format("Can not locate element %s by Xpath.", elementId));
            }
        } catch (Exception e) {
            WebElement bestMatchElement = findBestMatch(elementId, locators);
            updateLocatorData(elementId, bestMatchElement, locators);
            return bestMatchElement;
        }
        return null;
    }

    // Method to score web elements based on matching attributes
    public WebElement findBestMatch(String elementId, PageLocators locators) {
        long startTime = System.nanoTime(); // Start time measurement

        ElementLocator oldElement = getElementLocator(elementId, locators);
        boolean useAncestorXpath = !oldElement.getAttributes().getAncestorXpath().equals("") &&
                isElementPresent(By.xpath(oldElement.getAttributes().getAncestorXpath()), Duration.ofSeconds(5));

        List<WebElement> idMatch = findPossibleIdMatch(oldElement);
        List<WebElement> tagMatch = findPossibleTagMatch(oldElement, useAncestorXpath);
        List<WebElement> classMatch = findPossibleClassMatch(oldElement, useAncestorXpath);
        List<WebElement> textMatch = findPossibleTextMatch(oldElement, useAncestorXpath);
        List<WebElement> labelMatch = findPossibleLabelMatch(oldElement, useAncestorXpath);
        List<WebElement> hrefMatch = findPossibleHrefMatch(oldElement);
        Set<WebElement> candidates = new LinkedHashSet<>();
        if (idMatch != null) candidates.addAll(idMatch);
        if (tagMatch != null) candidates.addAll(tagMatch);
        if (classMatch != null) candidates.addAll(classMatch);
        if (textMatch != null) candidates.addAll(textMatch);
        if (labelMatch != null) candidates.addAll(labelMatch);
        if (hrefMatch != null) candidates.addAll(hrefMatch);
        List<WebElement> elements = new ArrayList<>(candidates);
        WebElement bestMatch = null;
        int highestScore = 0;

        for (WebElement element : elements) {
            int score = calculateScore(element, oldElement);

            // Update best match if the current element has a higher score
            if (score > highestScore) {
                highestScore = score;
                bestMatch = element;
            }
        }

        long endTime = System.nanoTime(); // End time measurement
        long elapsedTime = endTime - startTime; // Calculate elapsed time
        double timeInSec = (double) elapsedTime / 1_000_000_000;
        logger.info(String.format("Time taken to locate element %s: %f", elementId, timeInSec));
        totalExecutionTime += elapsedTime;
        totalExecutions++;

        return bestMatch;
    }

    public void updateLocatorData(String elementId, WebElement bestMatch, PageLocators locators) {
        ElementLocator oldElement = getElementLocator(elementId, locators);
        String absoluteXpath = getAbsoluteXPath(bestMatch);
        try {
            String newRobula = generateRobulaXPath(driver, absoluteXpath).replaceAll("\"", "'");
            String newMontoto = generateMontotoXPath(driver, bestMatch).replaceAll("\"", "'");
            updateXPath(oldElement, newRobula, newMontoto, locators);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSingleLocator(String elementId, WebElement bestMatch, PageLocators locators, String locatorName) {
        ElementLocator oldElement = getElementLocator(elementId, locators);
        String absoluteXpath = getAbsoluteXPath(bestMatch);
        try {
            switch (locatorName) {
                case "Robula": {
                    String newRobula = generateRobulaXPath(driver, absoluteXpath).replaceAll("\"", "'");
                    updateXPath(oldElement, newRobula, "", locators);
                    break;
                }
                case "Montoto": {
                    String newMontoto = generateMontotoXPath(driver, bestMatch).replaceAll("\"", "'");
                    updateXPath(oldElement, "", newMontoto, locators);
                    break;
                }
                default: break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to calculate the score for a given web element
    private static int calculateScore(WebElement element, ElementLocator oldElement) {
        int score = 0;

        // Example of attribute weights
        int idWeight = 4;
        int classWeight = 2;
        int tagWeight = 2;
        int textWeight = 1;
        int labelWeight = 1;
        int hrefWeight = 2;

        // Scoring logic
        if (!oldElement.getAttributes().getId().equals("") && element.getAttribute("id") != null && element.getAttribute("id").equals(oldElement.getAttributes().getId())) {
            score += idWeight;
        }

        if (!oldElement.getAttributes().getClassName().equals("") && element.getAttribute("class") != null && element.getAttribute("class").contains(oldElement.getAttributes().getClassName())) {
            score += classWeight;
        }

        if (!oldElement.getAttributes().getTag().equals("") && element.getTagName() != null && element.getTagName().equals(oldElement.getAttributes().getTag())) {
            score += tagWeight;
        }

        String elementText = element.getText();
        String elementPlaceholder = element.getAttribute("placeholder");
        if (!oldElement.getAttributes().getText().equals("") && elementText != null &&
                (elementText.contains(oldElement.getAttributes().getText()) || oldElement.getAttributes().getText().contains(elementText))) {
            score += textWeight;
        }
        if (!oldElement.getAttributes().getText().equals("") && elementText != null &&
                elementText.equals(oldElement.getAttributes().getText())) {
            score += textWeight;
        }
        if (!oldElement.getAttributes().getText().equals("") && elementPlaceholder != null &&
                (elementPlaceholder.contains(oldElement.getAttributes().getText()) || oldElement.getAttributes().getText().contains(elementPlaceholder))) {
            score += textWeight;
        }

        String label = getLabel(element);
        if (!oldElement.getAttributes().getLabel().equals("") && label != null && label != "" &&
                (
                    label.contains(oldElement.getAttributes().getLabel()) ||
                    oldElement.getAttributes().getLabel().contains(label)
                )
        ) {
            score += labelWeight;
        }

        if (!oldElement.getAttributes().getHref().equals("") && element.getAttribute("href") != null
                && element.getAttribute("href").contains(oldElement.getAttributes().getHref())) {
            score += hrefWeight;
        }

        return score;
    }

    public List<WebElement> findPossibleIdMatch(ElementLocator element) {
        if (element.getAttributes().getId().equals("")) {
            return null;
        } else {
            String xpath = String.format("//*[@id = '%s']", element.getAttributes().getId());
            return driver.findElements(By.xpath(xpath));
        }
    }

    public List<WebElement> findPossibleTagMatch(ElementLocator element, boolean useAncestorXpath) {
        if (element.getAttributes().getTag().equals("")) {
            return null;
        } else {
            String xpath;
            if (useAncestorXpath) {
                xpath = element.getAttributes().getAncestorXpath() + "//" + element.getAttributes().getTag();
            } else {
                xpath = "//" + element.getAttributes().getTag();
            }
            return driver.findElements(By.xpath(xpath));
        }
    }

    public List<WebElement> findPossibleClassMatch(ElementLocator element, boolean useAncestorXpath) {
        if (element.getAttributes().getClassName().equals("")) {
            return null;
        } else {
            String xpath;
            if (useAncestorXpath) {
                xpath = String.format("%s//*[contains(@class, '%s')]", element.getAttributes().getAncestorXpath(), element.getAttributes().getClassName());
            } else {
                xpath = String.format("//*[contains(@class, '%s')]", element.getAttributes().getClassName());
            }
            return driver.findElements(By.xpath(xpath));
        }
    }

    public List<WebElement> findPossibleTextMatch(ElementLocator element, boolean useAncestorXpath) {
        if (element.getAttributes().getText().equals("")) {
            return null;
        } else {
            String[] words = element.getAttributes().getText().split(" ");
            Set<WebElement> candidates = new LinkedHashSet<>();
            String ancestor = (useAncestorXpath) ? element.getAttributes().getAncestorXpath() : "";
            for (String word : words) {
                try {
                    String xpath = String.format("%s//*[contains(text(), '%s') or contains(@placeholder, '%s') or contains(@value, '%s')]", ancestor, word, word, word);
                    // Find elements that contain the word
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    candidates.addAll(elements); // Add elements to Set to avoid duplicates
                } catch (Exception e) {
                    System.out.println("No elements found for word: " + word);
                }
            }
            return new ArrayList<>(candidates);
        }
    }

    public List<WebElement> findPossibleLabelMatch(ElementLocator element, boolean useAncestorXpath) {
        if (element.getAttributes().getLabel().equals("")) {
            return null;
        } else {
            String[] words = element.getAttributes().getLabel().split(" ");
            Set<WebElement> candidates = new LinkedHashSet<>();
            String ancestor = (useAncestorXpath) ? element.getAttributes().getAncestorXpath() : "";
            for (String word : words) {
                try {
                    String xpath = String.format("%s//label[contains(text(), '%s')]/parent::div/parent::div//*", ancestor, word);
                    // Find elements that under the label contain the word
                    List<WebElement> elements = driver.findElements(By.xpath(xpath));
                    candidates.addAll(elements); // Add elements to Set to avoid duplicates
                } catch (Exception e) {
                    System.out.println("No elements found for label: " + word);
                }
            }
            return new ArrayList<>(candidates);
        }
    }

    public List<WebElement> findPossibleHrefMatch(ElementLocator element) {
        if (element.getAttributes().getHref().equals("")) {
            return null;
        } else {
            String xpath = String.format("//*[contains(@href, '%s')]", element.getAttributes().getHref());
            return driver.findElements(By.xpath(xpath));
        }
    }

    public void clickElementByLocatorId(String id) {
        waitForPageLoaded();
        WebElement element = getWebElement(id, locators);
        clickOnElement(element);
        waitForPageLoaded();
    }
}
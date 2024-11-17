package core.helper.algorithm.montoto;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import java.util.List;

import static core.page.AbstractBasePage.getAbsoluteXPath;
import static core.page.AbstractBasePage.getElementAttributes;
import static core.page.AbstractBasePage.getElementPosition;

public class Montoto {

    // Method to generate the XPath expression for a WebElement
    public static String generateMontotoXPath(WebDriver driver, WebElement element) {
        String finalXpath = "";
        WebElement currentElement = element;    // Initialize the current node

        // Loop until we reach the root of the DOM tree
        while (currentElement != null && !"/html[1]".equals(getAbsoluteXPath(currentElement))) {
            String attribute = "";
            String tagName = currentElement.getTagName(); // Get tag name of current node

            // Check if the node is uniquely identifiable
            String potentialXPath = String.format("/%s", tagName) + finalXpath;
            if (isUniqueXPath(driver, "/" + potentialXPath, element)) {
                return "/" + potentialXPath;  // If it's unique, return it
            }

            // Get text content if available
            String textContent = currentElement.getText().trim();

            if (!textContent.isEmpty()) {
                attribute = String.format("normalize-space(text()) = '%s'", textContent);
                if (isViableXPath(driver, "/" + generateCandidateXPath(potentialXPath, tagName, attribute), element)) {
                    if (isUniqueXPath(driver, "/" + generateCandidateXPath(potentialXPath, tagName, attribute), element)) {
                        return "/" + generateCandidateXPath(potentialXPath, tagName, attribute);  // If it's unique, return it
                    }
                } else { attribute = ""; }
            }

            // Get attributes of current node
            List<String> attributes = getElementAttributes(currentElement);

            // Append attributes to the XPath
            for (String attr : attributes) {
                if (!attr.contains("=\"\"") && !attr.contains("style")) {
                    attribute = (attribute.length() > 0) ? attribute + " and @" + attr : "@" + attr;
                    if (isViableXPath(driver, "/" + generateCandidateXPath(potentialXPath, tagName, attribute), element)) {
                        if (isUniqueXPath(driver, "/" + generateCandidateXPath(potentialXPath, tagName, attribute), element)) {
                            return "/" + generateCandidateXPath(potentialXPath, tagName, attribute);  // If it's unique, return it
                        }
                    } else { attribute = ""; }
                }
            }


            // If still not unique, continue traversing up the DOM tree
            finalXpath = generateCandidateXPath(potentialXPath, tagName, attribute);  // Add to the expression

            // Move to parent
            currentElement = currentElement.findElement(By.xpath(".."));
        }

        // If not unique, include position among siblings
        if (isUniqueXPath(driver, "/" + finalXpath, element)) {
            return "/" + finalXpath;  // If it's unique, return it
        } else {
            List<WebElement> siblings = driver.findElements(By.xpath("/" + finalXpath));
            int position = getElementPosition(element, siblings);
            return "(/" + finalXpath + ")[" + position + "]";
        }
    }

    // Helper to check if the XPath is unique within the document
    private static boolean isUniqueXPath(WebDriver driver, String xpath, WebElement targetElement) {
        // Find elements that match the generated XPath
        List<WebElement> elements = driver.findElements(By.xpath(xpath));

        // Return true if the XPath matches the input element specifically
        return elements.size() == 1 && elements.get(0).equals(targetElement);
    }

    private static boolean isViableXPath(WebDriver driver, String xpath, WebElement targetElement) {
        // Find elements that match the generated XPath
        List<WebElement> elements = driver.findElements(By.xpath(xpath));

        // Return true if the XPath matches the input element specifically
        return elements.size() > 0;
    }

    // Helper to generate a candidate XPath expression
    private static String generateCandidateXPath(String baseXpath, String tagName, String attribute) {
        if (attribute.length() > 0)
            return baseXpath.substring(0, tagName.length() + 1) + "[" + attribute + "]" + baseXpath.substring(tagName.length() + 1);
        else
            return baseXpath;
    }

    private static String addPosition(String baseXpath, String tagName, String attribute, int position) {
        if (attribute.length() > 0)
            return baseXpath.substring(0, tagName.length() + 1) + "[" + attribute + "][" + position + "]" + baseXpath.substring(tagName.length() + 1);
        else
            return baseXpath.substring(0, tagName.length() + 1) + "[" + position + "]" + baseXpath.substring(tagName.length() + 1);
    }
}

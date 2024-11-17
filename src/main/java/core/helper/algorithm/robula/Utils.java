package core.helper.algorithm.robula;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Utils {

    // method calculating Recall value
    public static double recall(RemoteWebDriver driver, String xPath, WebElement target) {
        List<WebElement> elements = driver.findElements(By.xpath(xPath));
        if (elements.contains(target)) return 1.0; // Equivalent to perfect recall
        else return 0.0; // Target element not found

    }

    public static boolean myContains(List<XPathInfo> list, String e) {
        for (XPathInfo temp : list) {
            if (temp.getXpath().toString().equals(e)) {
                return true;
            }
        }
        return false;
    }

    // method calculating Precision value
    public static double precision(RemoteWebDriver driver, String xPath, WebElement target) {
        List<WebElement> elements = driver.findElements(By.xpath(xPath));
        if (elements.contains(target)) return 1.0 / elements.size(); // Precision is 1 / number of elements found
        else return 0.0; // If target element is not found, return precision as 0.0
    }

    public static int numberOfSubString(String str, String findStr) {

        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;

    }

    public static String removeAttributesValues(String xpath) {

        String output = "";
        boolean insideSingleQuote = false;
        boolean insideDoubleQuote = false;
        for (int i = 0; i < xpath.length(); i++) {

            if (xpath.charAt(i) == '"' && insideDoubleQuote == false && insideSingleQuote == false) {
                insideDoubleQuote = true;

            } else if (xpath.charAt(i) == '"' && insideDoubleQuote == true && insideSingleQuote == false) {
                insideDoubleQuote = false;
            } else if (xpath.charAt(i) == '\'' && insideDoubleQuote == false && insideSingleQuote == false) {
                insideSingleQuote = true;
            } else if (xpath.charAt(i) == '\'' && insideDoubleQuote == false && insideSingleQuote == true) {
                insideSingleQuote = false;
            }

            if (insideDoubleQuote == false && insideSingleQuote == false) {
                output = output + xpath.charAt(i);
            }

        }
        output = output.replaceAll("'", "_").replaceAll("\"", "_");
        return output;
    }

    public static int getXPathNumberOfLevels(String string) {
        String cleaned = Utils.removeAttributesValues(string);
        cleaned = cleaned.replace("//", "/");
        return Utils.numberOfSubString(cleaned, "/");
    }

}

class ElementInfo {
    String tagName = new String();
    ArrayList<String> attributes = new ArrayList<String>();
    LinkedHashSet<Set<String>> setAttributes = new LinkedHashSet<Set<String>>();
    int position = -1;
    int positionStar = -1;
    String text = new String();
}

class AlgorithmSettings {
    boolean singleSlash;
    boolean specializeStars;
    boolean euristicSelection;
    String name;
    boolean powerSetAttributes;
    boolean orderedInsert;
    boolean text;
}


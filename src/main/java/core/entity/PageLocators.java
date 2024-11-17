package core.entity;

import java.time.LocalDate;
import java.util.List;

import static core.helper.JsonHelper.saveLocatorsToFile;

public class PageLocators {
    private List<ElementLocator> elements;
    private String webName;
    private String pageName;

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setElements(List<ElementLocator> elements) {
        this.elements = elements;
    }

    public List<ElementLocator> getElements() {
        return elements;
    }

    public static String getRobulaXPath(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getXpaths().getRobula();
            }
        }
        return null; // Handle when elementId not found
    }

    public static String getMontotoXPath(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getXpaths().getMontoto();
            }
        }
        return null; // Handle when elementId not found
    }

    public static ElementLocator getElementLocator(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element;
            }
        }
        return null;
    }

    public static String getElementAncestorXpath(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getAttributes().getAncestorXpath();
            }
        }
        return null;
    }

    public static String getElementId(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getAttributes().getId();
            }
        }
        return null;
    }

    public static String getElementTag(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getAttributes().getTag();
            }
        }
        return null;
    }

    public static String getElementClass(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getAttributes().getClassName();
            }
        }
        return null;
    }

    public static String getElementText(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getAttributes().getText();
            }
        }
        return null;
    }

    public static String getElementLabel(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getAttributes().getLabel();
            }
        }
        return null;
    }

    public static String getElementHref(String elementId, PageLocators locators) {
        for (ElementLocator element : locators.getElements()) {
            if (element.getElementId().equals(elementId)) {
                return element.getAttributes().getHref();
            }
        }
        return null;
    }

    public static void updateXPath(ElementLocator element, String newRobulaXPath, String newMontotoXPath, PageLocators locators) throws Exception {
        HistoryEntry historyEntry = new HistoryEntry();
        historyEntry.setUpdateDate(LocalDate.now().toString());
        if (!newRobulaXPath.equals("")) {
            historyEntry.setOldRobulaXPath(element.getXpaths().getRobula());
            historyEntry.setNewRobulaXPath(newRobulaXPath);
            element.getXpaths().setRobula(newRobulaXPath);
        }
        if (!newMontotoXPath.equals("")) {
            historyEntry.setOldMontotoXPath(element.getXpaths().getMontoto());
            historyEntry.setNewMontotoXPath(newMontotoXPath);
            element.getXpaths().setMontoto(newMontotoXPath);
        }

        element.getHistory().add(historyEntry);
        element.setLastUpdate(LocalDate.now().toString());

        // Write back to file
        saveLocatorsToFile(locators);
    }

}



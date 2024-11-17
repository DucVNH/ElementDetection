package core.entity;

import java.util.List;

public class ElementLocator {
    private String elementId;
    private XPathData xpaths;
    private Attributes attributes;
    private String lastUpdate;
    private List<HistoryEntry> history;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public XPathData getXpaths() {
        return xpaths;
    }

    public void setXpaths(XPathData xpaths) {
        this.xpaths = xpaths;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<HistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryEntry> history) {
        this.history = history;
    }
}


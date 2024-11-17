package core.entity;

public class Attributes {
    private String ancestorXpath;
    private String id;
    private String tag;
    private String className;
    private String text;
    private String label;
    private String href;

    public String getAncestorXpath() {
        return ancestorXpath;
    }

    public void setAncestorXpath(String ancestorXpath) {
        this.ancestorXpath = ancestorXpath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

package org.cauli.ui.selenium.element;

/**
 * 
 */
public class TempElement {
    private String id;
    private String xpath;
    private String by;
    public TempElement(ElementInfo elementInfo){
        this.id=elementInfo.getId();
        this.xpath=elementInfo.getXpath();
        this.by=elementInfo.getBy();
    }
    public TempElement(String id,String jquery,String xpath){
        this.id=id;
        this.by=jquery;
        this.xpath=xpath;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }
}

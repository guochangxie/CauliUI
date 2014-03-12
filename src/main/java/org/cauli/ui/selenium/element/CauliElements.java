package org.cauli.ui.selenium.element;

import org.apache.commons.io.IOUtils;
import org.cauli.ui.selenium.browser.IBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @auther sky
 */
public class CauliElements {

    private List<CauliElement> elements;
    private IBrowser browser;

    public CauliElements(IBrowser browser,String location){
        this.browser=browser;
        WebDriver driver=browser.getCurrentBrowserDriver();
        if(this.browser.isUseJQuery()){
            if(location.contains("->")){
                By by = LocationParse.parseLocation(location,driver.getPageSource());
                List<WebElement> webElements = driver.findElements(by);
                this.elements=WebElementTransfer.transferWebElements(webElements,browser);
            }else{
                List<WebElement> webElements = jquery(location,browser);
                this.elements=WebElementTransfer.transferWebElements(webElements,browser);
            }
        }else{
            List<WebElement> webElements=driver.findElements(LocationParse.parseLocation(location,driver.getPageSource()));
            this.elements=WebElementTransfer.transferWebElements(webElements,browser);
        }

    }


    protected List<WebElement> jquery(String jquery,IBrowser browser){
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("jquery.js");
        String jqueryJs = null;
        try {
            jqueryJs = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebDriver driver = browser.getCurrentBrowserDriver();
        return (List<WebElement>) ( (JavascriptExecutor)driver).executeScript(jqueryJs+"return jQuery(\"" + jquery + "\")");
    }


    public CauliElement get(int index){
        return this.elements.get(index);
    }



}

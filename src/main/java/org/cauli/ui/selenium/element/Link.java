package org.cauli.ui.selenium.element;

import com.auto.ui.browser.IBrowser;


public class Link extends Element {

    public Link(IBrowser browser, TempElement tempElement) {
        super(browser, tempElement);
    }


    public Link(IBrowser browser) {
        super(browser);
    }

  
    public String getHref(){
        return this.getAttribute("href");
    }
}

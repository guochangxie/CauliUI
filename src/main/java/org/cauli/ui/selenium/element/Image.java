package org.cauli.ui.selenium.element;

import com.auto.ui.browser.IBrowser;


public class Image extends Element {

    public Image(IBrowser browser, TempElement tempElement) {
        super(browser, tempElement);
    }

    public Image(IBrowser browser) {
        super(browser);
    }
    

    public String getsrc(){
        return getAttribute("src");
    }
}

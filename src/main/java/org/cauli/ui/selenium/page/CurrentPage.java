package org.cauli.ui.selenium.page;

import org.cauli.ui.selenium.browser.IBrowser;
import org.cauli.ui.selenium.browser.PageDelegate;

import java.lang.reflect.Constructor;

/**
 * Created by tianqing.wang on 14-3-17
 */
public class CurrentPage extends Page{

    public <T extends SourcePage>T proxy(Class<T> pageClass){
        try {
            Constructor constructor = pageClass.getConstructor(IBrowser.class);
            PageDelegate.register((ICurrentPage) constructor.newInstance(getBrowser()));
            return (T) PageDelegate.getDispatcher();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("代理页面的时候出现了错误...");
        }

    }

}

package org.cauli.ui.selenium.page;


import org.cauli.ui.selenium.browser.IBrowser;
import org.cauli.ui.selenium.element.IElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;


/**
 * @author tianqing.wang
 */
public interface ICurrentPage extends IPage {

    public IBrowser getBrowser();

    public void open(String url);

    public <T>T find(Class<T> clazz, String location);
    
    public <T>T find(Class<T> clazz);

    public IElement find(String location);

    public IElement element(String id);

    public <T>T element(Class<T> clazz,String id);

    public void assertAlert();

    public void assertTextNotPresent(String text) ;

    public void assertTitle(String title);

    public void assertTextPresent(String text);

    public String getPageSource();

    public String dealAlert();

    public String dealConfirm(boolean isyes);

    public String dealPrompt(boolean isyes, String text);

    public Object runJavaScript(String js, Object... objects);

    public Object runAsynJavaScript(String js, Object... objects);

    //public Set<String> getWindows();

    //public ICurrentPage selectLateOpenedWindow();

    public ICurrentPage openNewWindow(String url);

    public WebDriver getCurrentWindow();

    public void setBrowser(IBrowser browser);

    public <T> T frame(Class<T> clazz);

    public ICurrentPage frame(int index);

    public ICurrentPage frame(String nameOrId);

    public ICurrentPage frame(By by);

    public ICurrentPage frame(By by, int index);
    
    public IElement $(String jquery);

	void keypress(Keys key);

	void release();

}

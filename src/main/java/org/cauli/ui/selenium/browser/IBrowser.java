package org.cauli.ui.selenium.browser;


import com.auto.ui.page.ICurrentPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Set;


/**
 * @author tianqing.wang
 * @param <T>
 */
public interface IBrowser {
	
	public void pageLoadTimeout(int seconds);
	
	public void elementLoadTimeout(int seconds);
	
    public com.auto.ui.page.ICurrentPage open(String url);

    public void maxWindow();

    public void closeAllWindows();

    public void back();

    public void refresh();

    public void forward();

    public Set<String> getWindows();

    public com.auto.ui.page.ICurrentPage selectDefaultWindow();

    public ICurrentPage selectFrame(By by);

    public ICurrentPage selectFrame(int index);

    public ICurrentPage selectFrame(By by, int index);

    public ICurrentPage selectLastOpenedPage();

    public ICurrentPage selectWindowByTitle(String title);

    public ICurrentPage selectWindowByUrl(String url);

    public ICurrentPage selectWindowContainsTitle(String title);

    public ICurrentPage selectWindowContainsUrl(String url);

    public ICurrentPage currentPage();

    public WebDriver getCurrentBrowserDriver();

    public Object runJavaScript(String js, Object... objects);

    public Object runAsynJavaScript(String js, Object... objects);

    public void takeScreetShot(String path);

    public boolean isClosed();

    public void setClosed(boolean isclose);

    public WindowSource getWindowSource();

    public <T> T page(Class<T> pageClass);

    public void openNew(String url);


}

package org.cauli.ui.selenium.page;

import com.auto.ui.element.IElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import java.util.Set;

/**
 * @author celeskyking
 */
public interface IPage {

    public Set<Cookie> getAllCookies();

    void deleteAllCookies();

    public String getTitle();

    public String getUrl();

    public String getCookieByName(String name);

    public IElement currentElement();

    //public static <T> T load(Class<T> clazz);

    public IElement element(String id);

    public IElement element(By by);

}

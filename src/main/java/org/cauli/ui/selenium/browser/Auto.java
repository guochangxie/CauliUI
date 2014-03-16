package org.cauli.ui.selenium.browser;


import org.cauli.ui.selenium.element.IElement;
import org.cauli.ui.selenium.element.Select;
import org.cauli.ui.selenium.element.Table;
import org.cauli.ui.selenium.page.ICurrentPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tianqing.wang
 */
public class Auto {

    private static ICurrentPage currentPage;
    public static ThreadLocal<Set<Engine>> browserSet=new ThreadLocal<Set<Engine>>(){
        public Set<Engine> initialValue(){
            return new HashSet<Engine>();
        }
    };
    private static Logger logger = LoggerFactory.getLogger(Auto.class);
    public  static ThreadLocal<BrowserManager> local = new ThreadLocal<BrowserManager>(){
        public BrowserManager initialValue(){
            return new BrowserManager();
        }
    };
    public static void require(Engine[] browsers){
        for(Engine browser:browsers){
            browserSet.get().add(browser);
        }
    }

    public static void require(String value){
        Engine b=Enum.valueOf(Engine.class,value.toUpperCase().trim());
        require(b);
    }

    public static void require(Engine browser){
        BrowserManager browserManager=new BrowserManager();
        browserManager.setBrowser(new Browser(browser));
        local.set(browserManager);

    }
    public static void require(Engine browser,String url){
        BrowserManager browserManager=new BrowserManager();
        try {
            browserManager.setBrowser(new Browser(browser,new URL(url)));
        } catch (MalformedURLException e) {
            logger.error("没有连接到远程节点的服务器，远程浏览器引用失败！请检查环境配置是否正确！");
            throw new RuntimeException("没有连接到远程节点的服务器，远程浏览器引用失败！请检查环境配置是否正确",e);
        }
        local.set(browserManager);
    }
    
    public static void requireRemote(Engine browser,String url){
    	BrowserManager browserManager=new BrowserManager();
        try {
            browserManager.setBrowser(new RemoteBrowser(browser,new URL(url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("连接远程的serverURL出现了错误");
        }
        local.set(browserManager);
    }
    public static IBrowser browser(){
        return local.get().getBrowser();
    }

    public static ICurrentPage go(String url){
        return browser().open(url);
    }
    public static IElement $(String jquery){
    	return browser().currentPage().$(jquery);
    }

    public void to(ICurrentPage page){

    }

    public static void maxWindow(){
        browser().maxWindow();
    }

    public static void quit(){
        browser().closeAllWindows();
    }

    public static void back(){
        browser().back();
    }

    public static void openNew(String url){
        browser().openNew(url);
    }
    public static void refresh(){
        browser().refresh();
    }

    public static Select select(String location){
        return browser().currentPage().select(location);
    }

    public static void forward(){
        browser().forward();
    }

    public static Set<String> getWindows(){
        return browser().getWindows();
    }

    public static ICurrentPage selectDefaultWindow(){
        return browser().selectDefaultWindow();
    }

    public static ICurrentPage selectLastOpenedPage(){
        return  browser().selectLastOpenedPage();
    }

    public static ICurrentPage selectWindowByTitle(String title){
        return browser().selectWindowByTitle(title);
    }

    public static ICurrentPage selectWindowByUrl(String url){
        return browser().selectWindowByUrl(url);
    }

    //public ICurrentPage selectWindowContainsTitle(String title);

    public static ICurrentPage selectWindowContainsUrl(String url){
        return browser().selectWindowContainsUrl(url);
    }

    public static ICurrentPage currentPage(){
        return browser().currentPage();
    }

    public static WebDriver getCurrentBrowserDriver(){
        return browser().getCurrentBrowserDriver();
    }

    public static Object runJavaScript(String js,Object... objects){
        return browser().runJavaScript(js, objects);
    }

    public static Object runAsynJavaScript(String js,Object... objects){
        return browser().runAsynJavaScript(js, objects);
    }

    public static void takeScreetShot(String path){
        browser().takeScreetShot(path);
    }

    public static boolean isClosed(){
        return browser().isClosed();
    }

    public static void setClosed(boolean isclose){
        browser().setClosed(isClosed());
    }

    public static void clearBrowserManager(){
        browserSet.get().clear();
    }

    public static boolean remove(Browser browser){
        return browserSet.get().remove(browser);
    }

    public static Table table(String location){
        return currentPage().table(location);
    }

	public static <T> T page(Class<T> clazz) {
        try {
            T page= (T) Class.forName(clazz.getName()).newInstance();
            return page;
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        }
    }

    public static void pageLoadTimeout(int seconds){
    	browser().pageLoadTimeout(seconds);
    }

    public static void elementLoadTimeout(int seconds){
    	browser().elementLoadTimeout(seconds);
    }
    

}

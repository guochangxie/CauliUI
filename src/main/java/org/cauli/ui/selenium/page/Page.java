package org.cauli.ui.selenium.page;

import org.cauli.ui.selenium.browser.IBrowser;
import org.cauli.ui.selenium.element.*;
import org.cauli.ui.selenium.listener.ActionListenerProxy;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Page implements ICurrentPage {
    private Logger logger = LoggerFactory.getLogger(Page.class);
    private String commit;
    private Map<String, TempElement> elementMap;
    private Actions actions;
    public WebDriver getCurrentwindow() {
        return currentwindow;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    private WebDriver currentwindow;

    public IBrowser getBrowser() {
        return browser;
    }
    protected Page(){
    	  this.setElements(new HashMap<String,TempElement>());
    }
    private IBrowser browser;
    public Page(IBrowser browser){
        this.browser=browser;
        this.currentwindow=browser.getCurrentBrowserDriver();
        this.setElements(new HashMap<String,TempElement>());
        this.actions=new Actions(getCurrentwindow());
    }

    public Page(WebDriver driver){
        this.currentwindow=driver;
        this.setElements(new HashMap<String,TempElement>());
    }

    public void setBrowser(IBrowser browser){
        this.browser=browser;
        this.currentwindow=browser.getCurrentBrowserDriver();
    }

    public <T> T frame(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor(ICurrentPage.class).newInstance(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ICurrentPage frame(int index) {
        return null;
    }

    @Override
    public ICurrentPage frame(String nameOrId) {
        return null;
    }

    @Override
    public ICurrentPage frame(By by) {
        return null;
    }

    @Override
    public ICurrentPage frame(By by, int index) {
        return null;
    }


    @Override
    public Set<Cookie> getAllCookies() {
        return this.currentwindow.manage().getCookies();
    }

    @Override
    public void deleteAllCookies() {
        this.currentwindow.manage().deleteAllCookies();
        logger.info("["+this.commit+"]进行了删除所有cookie的操作");
    }

    @Override
    public String getTitle() {
        return this.currentwindow.getTitle();
    }

    @Override
    public String getUrl() {
        return this.currentwindow.getCurrentUrl();
    }

    @Override
    public String getCookieByName(String name) {
        return this.currentwindow.manage().getCookieNamed(name).getValue();
    }

	public static <T> T page(Class<T> clazz) {
        try {
            T currentpage= (T) Class.forName(clazz.getName()).newInstance();
            return currentpage;
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        }
    }


    @Override
    public void open(String url) {
        this.currentwindow.get(url);
        logger.info("["+this.commit+"]页面跳转到了页面"+url);
    }

    @Override
    public <T> T find(Class<T> clazz, String location) {
        try {
            Constructor constructor = clazz.getConstructor(IBrowser.class,String.class);
            return (T) constructor.newInstance(getBrowser(),location);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoSuchElementException("没有找到此类型的元素:"+clazz.getName()) ;
        }
    }

    @Override
    public <T> T find(Class<T> clazz) {
        try {
            Constructor constructor = clazz.getConstructor(getBrowser().getClass());
            return (T) constructor.newInstance(getBrowser());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到此类型的元素:"+clazz.getName(),e) ;
        }
    }

    @Override
    public IElement find(String location) {
        return new CauliElement(getBrowser(),location);
    }

    public Select select(String location){
        Select select = new Select(getBrowser(),location);
        return select;
    }

    @Override
    public IElement element(String id) {
    	if(this.elementMap.get(id)!=null){
    		return new CauliElement(this.browser,getElements().get(id));
    	}
        return null;
        
    }

    @Override
    public void assertAlert() {
        try{
            this.browser.getCurrentBrowserDriver().switchTo().alert();
            logger.info("当前页面找到了alert，校验成功！");
        }catch (NoAlertPresentException e){
            logger.error("当前页面没有找到了alert，校验失败！");
            Assert.fail();
        }
    }

    @Override
    public void assertTextNotPresent(String text) {
        if(getPageSource().contains(text)){
            logger.info("当前页面找到指定的内容，校验成功！");
        }else{
            logger.error("当前页面没有找到指定内容"+text+"，校验失败！");
            Assert.fail();
        }
    }

    @Override
    public void assertTitle(String title) {
        if(getTitle().equals(title)){
            logger.info("当前页面的title值["+title+"]校验成功，校验成功！");
        }else{
            logger.error("当前页面title值["+title+"]校验失败，断言失败！");
            Assert.fail();
        }
    }

    @Override
    public void assertTextPresent(String text) {
        if(getPageSource().contains(text)){
            logger.info("当前页面没有找到指定的内容，校验成功！");
        }else{
            logger.error("当前页面找到了指定内容"+text+"，校验失败！");
            Assert.fail();
        }
    }

    @Override
    public String getPageSource() {
        return this.currentwindow.getPageSource();
    }


    @Override
    public String dealAlert() {
        ActionListenerProxy.getDispatcher().beforedealAlert();
        String alerMessage=null;
        try{
            Alert alert=this.browser.getCurrentBrowserDriver().switchTo().alert();
            alerMessage=alert.getText();
            alert.accept();
            ActionListenerProxy.getDispatcher().afterdealAlert();
            return alerMessage;
        }catch(Exception e){
            ActionListenerProxy.getDispatcher().afterdealAlert();
            logger.warn("["+this.commit+"]没有找到alert窗口，程序将继续运行，可能会出现异常，请查看代码是否正确");
            return null;
        }
    }

    @Override
    public String dealConfirm(boolean isyes) {
        ActionListenerProxy.getDispatcher().beforedealConfirm();
        String alerMessage=null;
        try{
            Alert alert=this.browser.getCurrentBrowserDriver().switchTo().alert();
            alerMessage=alert.getText();
            if(isyes){
                alert.accept();
            }else{
                alert.dismiss();
            }
            ActionListenerProxy.getDispatcher().afterdealConfirm();
            return alerMessage;
        }catch(Exception e){
            ActionListenerProxy.getDispatcher().afterdealConfirm();
            logger.warn("["+this.commit+"]没有找到comfirm窗口，程序将继续运行，可能会出现异常，请查看代码是否正确");
            return null;
        }
    }

    @Override
    public String dealPrompt(boolean isyes, String text) {
        ActionListenerProxy.getDispatcher().beforedealConfirm();
        String alerMessage=null;
        try{
            Alert alert=this.browser.getCurrentBrowserDriver().switchTo().alert();
            alert.sendKeys(text);
            alerMessage=alert.getText();
            if(isyes){
                alert.accept();
            }else{
                alert.dismiss();
            }
            ActionListenerProxy.getDispatcher().afterdealConfirm();
            return alerMessage;
        }catch(Exception e){
            ActionListenerProxy.getDispatcher().afterdealConfirm();
            logger.warn("["+this.commit+"]没有找到prompt窗口，程序将继续运行，可能会出现异常，请查看代码是否正确");
            return null;
        }
    }

    @Override
    public Object runJavaScript(String js, Object... objects) {
        return ((JavascriptExecutor)this.currentwindow).executeScript(js, objects);
    }

    @Override
    public Object runAsynJavaScript(String js, Object... objects) {
        return ((JavascriptExecutor)this.currentwindow).executeAsyncScript(js, objects);
    }

    @Override
    public ICurrentPage openNewWindow(String url) {
        runJavaScript("window.open(\""+url+"\")");
        logger.info("在新的窗口打开了链接"+url);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.browser.selectLastOpenedPage();
        return this.browser.currentPage();
    }

    @Override
    public WebDriver getCurrentWindow() {
        return this.browser.getCurrentBrowserDriver();
    }


    public void setCurrentwindow(WebDriver currentwindow) {
        this.currentwindow = currentwindow;
    }



	@Override
	public <T> T element(Class<T> clazz, String id) {
		try {
			Constructor<T> ctor = clazz.getConstructor(IBrowser.class,String.class);
			return ctor.newInstance(getBrowser(),getTempElement(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, TempElement> getElements() {
		return elementMap;
	}
	
	public TempElement getTempElement(String id){
		return elementMap.get(id);
	}
	
	public void setElements(Map<String, TempElement> elementMap) {
		this.elementMap = elementMap;
	}
	
	public void addElement(String id,TempElement element){
		this.elementMap.put(id, element);
	}

    public void addElements(Map<String,TempElement>map){
        this.elementMap.putAll(map);
    }
	
	public IElement $(String jquery) {
		CauliElement cauliElement= new CauliElement(browser,jquery);
        cauliElement.setId(jquery);
        return  cauliElement;
	}


    
    @Override
    public void keypress(Keys key) {
            this.actions.sendKeys(key).perform();
            
    }

    @Override
    public void release() {
            this.actions.release().build().perform();
            
    }

    @Override
    public Table table(String location) {
        Table table = new Table(getBrowser(),location);
        return table;
    }
}


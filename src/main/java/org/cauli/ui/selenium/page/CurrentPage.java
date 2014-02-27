package org.cauli.ui.selenium.page;

import com.auto.ui.browser.IBrowser;
import com.auto.ui.element.*;
import com.auto.ui.listener.ActionListenerProxy;
import com.auto.ui.source.XMLParse;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CurrentPage implements ICurrentPage {
    private Logger logger = Logger.getLogger(CurrentPage.class);
    private static Object currentpage;
    private XMLParse xmlParse;
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
    protected CurrentPage(){
    	  this.setElements(new HashMap<String,TempElement>());
    }
    private IBrowser browser;
    public CurrentPage(IBrowser browser){
        this.browser=browser;
        this.currentwindow=browser.getCurrentBrowserDriver();
        this.setElements(new HashMap<String,TempElement>());
        this.actions=new Actions(getCurrentwindow());
    }

    public CurrentPage(WebDriver driver){
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

    @Override
    public IElement currentElement() {
        return null;
    }

    @SuppressWarnings("unchecked")
	public static <T> T page(Class<T> clazz) {
        CurrentPage.currentpage=null;
        try {
            currentpage=Class.forName(clazz.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到这个class类"+clazz.getName()+",请检查类是否被加载或者类名是否正确");
        }
        return (T)currentpage;
    }


    @Override
    public void open(String url) {
        this.currentwindow.get(url);
        logger.info("["+this.commit+"]页面跳转到了页面"+url);
    }


    @Override
    public IElement element() {
        return new Element(this.browser);
    }

    @Override
    public IElement element(String id) {
    	if(this.elementMap.get(id)!=null){
    		return new Element(this.browser,getElements().get(id));
    	}else{
    		return new Element(this.browser,this.xmlParse.getTempElment(id));
    	}
        
    }

    @Override
    public IElement element(By by) {
        Element element = new Element(getBrowser());
        element.setElement(getBrowser().getCurrentBrowserDriver().findElement(by));
        return element;
    }

    @Override
    public IElement element(By by, Integer index) {
        Element element = new Element(getBrowser());
        element.setElement(getBrowser().getCurrentBrowserDriver().findElements(by).get(index));
        return element;
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
            logger.error("当前页面title值["+title+"]校验失败，校验成功！");
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
    public Map<String, String> getHeaders() {
        PageResponse pageResponse=new PageResponse(this);
        Map<String,String> headermap=pageResponse.getHeaderMap();
        pageResponse.close();
        return headermap;
    }

    @Override
    public List<String> getJavaScriptURL() {
        PageResponse pageResponse=new PageResponse(this);
        List<String> jslist=pageResponse.getJavaScriptURL();
        pageResponse.close();
        return jslist;
    }

    @Override
    public String getHeaderByName(String name) {
        return getHeaders().get(name);
    }

    @Override
    public String getPageSource() {
        return this.currentwindow.getPageSource();
    }

    @Override
    public Integer getStatusCode() {
        PageResponse pageResponse=new PageResponse(this);
        Integer code = pageResponse.getStatusCode();
        pageResponse.close();
        return code;
    }


    @Override
    public List<String> getAllCssURLByLinked() {
        PageResponse pageResponse=new PageResponse(this);
        List<String> csslist=pageResponse.getAllCssUrlByLinked();
        pageResponse.close();
        return csslist;
    }

    @Override
    public boolean isGzip() {
        PageResponse pageResponse=new PageResponse(this);
        boolean bool=pageResponse.isGzip();
        pageResponse.close();
        return bool;
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
	@Override
	public ICurrentPage frame(int index) {
		this.browser.getCurrentBrowserDriver().switchTo().frame(index);
		return this.browser.currentPage();
	}
	@Override
	public ICurrentPage frame(String nameOrId) {
		this.browser.getCurrentBrowserDriver().switchTo().frame(nameOrId);
		return this.browser.currentPage();
	}
	@Override
	public ICurrentPage frame(By by) {
		WebElement element = this.browser.getCurrentBrowserDriver().findElement(by);
		this.browser.getCurrentBrowserDriver().switchTo().frame(element);
		return this.browser.currentPage();
	}
	@Override
	public ICurrentPage frame(By by, int index) {
		List<WebElement> elements = this.browser.getCurrentBrowserDriver().findElements(by);
		this.browser.getCurrentBrowserDriver().switchTo().frame(elements.get(index));
		return this.browser.currentPage();
	}

    public void setCurrentwindow(WebDriver currentwindow) {
        this.currentwindow = currentwindow;
    }

    public void setXmlParse(XMLParse xmlParse) {
        this.xmlParse = xmlParse;
    }
	

	@Override
	public <T> T element(Class<T> clazz, String id) {
		try {
			Constructor<T> ctor = clazz.getConstructor(IBrowser.class,String.class);
			return ctor.newInstance(getBrowser(),getTempElement(id));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public <T> T element(Class<T> clazz) {
		try {
			Constructor<T> ctor = clazz.getConstructor(IBrowser.class);
			return ctor.newInstance(this.browser);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
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
		return new Element(browser,jquery);
	}

    public Select select(String id){
        Select select= new Select(browser,getElements().get(id));
        select.setId(id);
        return select;
    }

    @Override
    public Select select(By by) {
        return new Select(browser,by);
    }

    @Override
    public Table table(By by) {
        Table table = new Table(browser);
        table.setElement(browser.getCurrentBrowserDriver().findElement(by));
        return table;
    }

    public Table table(String id){
        Table table= new Table(browser,getElements().get(id));
        table.setId(id);
        return table;
    }

    public XMLParse getXmlParse() {
        return xmlParse;
    }
    
    @Override
    public void keypress(Keys key) {
            this.actions.sendKeys(key).perform();
            
    }

    @Override
    public void release() {
            this.actions.release().build().perform();
            
    }
}


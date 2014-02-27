package org.cauli.ui.selenium.element;

import com.auto.ui.browser.IBrowser;
import com.auto.ui.listener.ActionListenerProxy;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *@author 王天庆
 * 这是一个元素类，单纯封装的一个元素类
 */
public class Element implements IElement {
    private Logger logger = Logger.getLogger(Element.class);
    private IBrowser browser;
    private WebElement element;
    private TempElement tempElement;
    private Actions actions;
    private String id;
    private String jquery;
    private String xpath;
    public Element(IBrowser browser,TempElement tempElement){
        this.browser=browser;
        actions=new Actions(this.browser.getCurrentBrowserDriver());
        this.tempElement= tempElement;
        this.id=tempElement.getId();
        this.jquery = tempElement.getBy();
        this.xpath=tempElement.getXpath();
        if(!"".equals(jquery)){
            this.element=jquery(this.jquery);
        }else if(!"".equals(xpath)){
            this.element=getBrowser().getCurrentBrowserDriver().findElement(By.xpath(xpath));
        }

    }

    public Element(IBrowser browser){
        this.browser=browser;
        actions=new Actions(this.getBrowser().getCurrentBrowserDriver());
        this.id="Element";
        this.element=new RemoteWebElement();
    }

    public Element(IBrowser browser,String jquery){
        this(browser);
        this.element = jquery(jquery);
        this.id=jquery;
    }

    public WebElement jquery(String jquery){
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("jquery.js");
        String jqueryJs = null;
        try {
            jqueryJs = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebDriver driver = getBrowser().getCurrentBrowserDriver();
        return (WebElement)( (JavascriptExecutor)driver).executeScript(jqueryJs+"return jQuery(\"" + jquery + "\")[0]");
    }

    @Override
    public Element addLocator(By by) {
        this.element.findElement(by);
        return this;
    }

    @Override
    public void click() {
        ActionListenerProxy.getDispatcher().beforeClickOn();
        if(isExist()){
            element.click();
            logger.info("["+id+"]点击操作成功");

        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，点击失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，点击失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterClickOn();
    }

    @Override
    public void doubleClick() {
        ActionListenerProxy.getDispatcher().beforedoubleClick();
        if(isExist()){
            actions.doubleClick().build().perform();
            logger.info("["+id+"]双击操作成功");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，双击击失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，双击失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterdoubleClick();

    }

    @Override
    public void keyDown(Keys key) {
        ActionListenerProxy.getDispatcher().beforekeyDown();
        if(isExist()){
            actions.keyDown(key).build().perform();
            logger.info("["+id+"]按下键盘按钮["+key+"]成功");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，按下按键["+key+"]失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，按下按钮["+key+"]失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterkeyDown();
    }

    @Override
    public void keyUp(Keys key) {
        ActionListenerProxy.getDispatcher().beforekeyUp();
        if(isExist()){
            actions.keyDown(key).build().perform();
            logger.info("["+id+"]按下键盘按钮"+key+"成功");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，松开按键"+key+"失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，松开按键"+key+"失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterkeyUp();
    }

    @Override
    public void assertAttribute(String attr,String value) {
        if(getAttribute(attr).equals(value)){
            logger.info("["+id+"]这个元素的属性["+attr+"]的值[="+value+"]断言正确");
        }else{
            logger.error("["+id+"]这个元素的属性["+attr+"]的值[="+value+"]断言失败");
            Assert.fail();
        }
    }

    @Override
    public void assertEditable() {
        if(isEnable()){
            logger.info("["+id+"]这个元素可编辑的断言成功");
        }else{
            logger.error("["+id+"]这个元素可编辑性断言失败");
            Assert.fail();
        }
    }

    @Override
    public void assertNotEditable() {
        if(!isEnable()){
            logger.info("["+id+"]这个元素不可编辑的断言成功");
        }else{
            logger.error("["+id+"]这个元素不可编辑性断言失败");
            Assert.fail();
        }
    }

    @Override
    public void assertSelected() {
        if(isSelected()){
            logger.info("["+id+"]这个元素可选择性断言成功");
        }else{
            logger.error("["+id+"]这个元素可选择性断言失败");
            Assert.fail();
        }
    }

    @Override
    public void assertIsExist() {
        if(isExist()){
            logger.info("["+id+"]这个元素存在性断言成功");
        }else{
            logger.error("["+id+"]这个元素存在性断言失败");
            Assert.fail();
        }
    }

    @Override
    public void assertText(String text) {
        if(getText().equals(text)){
            logger.info("["+id+"]这个元素的文本值["+text+"]断言成功");
        }else{
            logger.error("["+id+"]这个元素的文本值["+text+"]断言失败");
            Assert.fail();
        }
    }

    @Override
    public void assertValue(String value) {
        if(getAttribute("value").equals(value)){
            logger.info("["+id+"]这个元素的value值["+value+"]断言成功");
        }else{
            logger.error("["+id+"]这个元素的value值["+value+"]断言失败");
            Assert.fail();
        }
    }

    @Override
    public void clear() {
        ActionListenerProxy.getDispatcher().beforeclear();
        if(isExist()){
            element.clear();
            logger.info("["+id+"]清空操作成功");

        }else{
            logger.error("[" + id + "]元素查找失败，可能这个元素不存在，清空失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，清空失败！");
        }
        ActionListenerProxy.getDispatcher().afterclear();
    }

    @Override
    public void input(String text) {
        ActionListenerProxy.getDispatcher().beforeSendkeys();
        if(isExist()){
            element.sendKeys(text);
            logger.info("["+id+"]输入["+text+"]值操作成功");

        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，输入["+text+"]失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，输入["+text+"]失败！");
        }
        ActionListenerProxy.getDispatcher().afterSendkeys();
    }

    @Override
    public void focus() {
        this.element.sendKeys("");
        this.browser.getWindowSource().windowsCheck();
    }

    @Override
    public String getAttribute(String attr) {
        if(isExist()){
            String attrvalue = this.element.getAttribute(attr);
            logger.info("["+id+"]获取属性[attr]的值[="+attrvalue+"]成功操作成功");
            return attrvalue;
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，获取属性"+attr+"失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，获取属性["+attr+"]的值失败！");
        }
    }

    @Override
    public String getText() {
        if(isExist()){
            String text=this.element.getText();
            logger.info("["+id+"]获取文本值["+text+"]操作成功");
            return text;
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，获取文本值失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，获取文本值失败！");
        }
    }

    @Override
    public String getCssValue(String name) {
        if(isExist()){
            String cssvalue = this.element.getCssValue(name);
            logger.info("["+id+"]元素获取css["+name+"]的值[="+cssvalue+"]操作成功");
            return cssvalue;
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，获取Css["+name+"]值失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，获取css["+name+"]失败！");
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public Point getLocation() {
        if(isExist()){
            Point point=this.element.getLocation();
            logger.info("["+id+"]获取元素位置操作成功");
            return point;
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，获取位置失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，获取位置失败！");
        }
    }


    @Override
    public int[] getSize() {
        if(isExist()){
            Dimension ds= this.element.getSize();
            int[] size=new int[]{ds.getHeight(),ds.getWidth()};
            logger.info("["+id+"]获取元素大小操作成功");
            return size;
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，获取元素大小失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，获取元素大小失败！");
        }
    }

    @Override
    public String getTagName() {
        if(isExist()){
            String tagname=this.element.getTagName();
            logger.info("["+id+"]获取元素标签名["+tagname+"]操作成功");
            return tagname;
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，获取元素标签名失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，获取元素标签名失败！");
        }
    }

    @Override
    public void scroll() {
        ActionListenerProxy.getDispatcher().beforescroll();
        if(isExist()){
            Point point=getLocation();
            int x=point.getX();
            int y=point.getY();
            this.browser.runJavaScript("window.scrollTo("+x+","+y+")");
            logger.info("["+id+"]视角移动到了["+x+","+y+"]的位置");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，移动视角失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，移动视角失败！");
        }
        ActionListenerProxy.getDispatcher().afterscroll();
    }

    @Override
    public void mouseOver() {
        ActionListenerProxy.getDispatcher().beforeMouseOver();
        if(isExist()){
            this.actions.moveToElement(this.element).build().perform();
            logger.info("["+id+"]元素处鼠标悬浮成功！");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，鼠标悬浮失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，鼠标悬浮失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterMouseOver();
    }

    @Override
    public void submit() {
        ActionListenerProxy.getDispatcher().beforesubmit();
        if(isExist()){
            this.element.submit();
            logger.info("["+id+"]元素提交表单成功！");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，提交表单失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，提交表单失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().aftersubmit();
    }

    @Override
    public boolean isDisplay() {
        if(isExist()){
            return this.element.isDisplayed();
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在,判断元素是否可见失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，判断元素是否可见失败！");
        }
    }

    @Override
    public boolean isEnable() {
        if(isExist()){
            return this.element.isEnabled();
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，判断元素是否可编辑失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，判断元素是否可编辑失败！");
        }
    }


    @Override
    public boolean isSelected() {
        if(isExist()){
            return this.element.isSelected();
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，判断元素是否被选择失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，判断元素是否被选择失败！");
        }
    }

    @Override
    public void dragAndDrop(IElement e) {
        ActionListenerProxy.getDispatcher().beforedragAndDrop();
        if(isExist()){
            this.actions.dragAndDropBy(this.element,e.getLocation().getX(),e.getLocation().getY()).build().perform();
            logger.info("["+id+"]元素拖拽成功");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，元素拖拽失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，元素拖拽失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterdragAndDrop();
    }

    @Override
    public void dragAndDrop(Point point) {
        ActionListenerProxy.getDispatcher().beforedragAndDrop();
        if(isExist()){
            this.actions.dragAndDropBy(this.element,point.getX(),point.getY());
            logger.info("["+id+"]元素拖拽成功");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，元素拖拽失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，元素拖拽失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterdragAndDrop();
    }

    @Override
    public void leftDown() {
        ActionListenerProxy.getDispatcher().beforeleftDown();
        if(isExist()){
            this.actions.clickAndHold(this.element);
            logger.info("["+id+"]元素处按住左键");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，元素处按住左键失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，元素处按住左键失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterleftDown();
    }

    @Override
    public void leftUp() {
        ActionListenerProxy.getDispatcher().beforeleftUp();
        if(isExist()){
            this.actions.release(this.element);
            logger.info("["+id+"]元素处松开左键");
        }else{
            logger.error("["+id+"]元素查找失败，可能这个元素不存在，元素处松开左键失败！");
            throw new NoSuchElementException("["+id+"]元素查找失败，可能这个元素不存在，元素处按住松开失败！");
        }
        this.browser.getWindowSource().windowsCheck();
        ActionListenerProxy.getDispatcher().afterleftUp();
    }


    public boolean isExist(){
        if(this.element==null){
            return false;
        }else{
            return true;
        }

    }

    public IBrowser getBrowser() {
        return browser;
    }

    public TempElement getTempElement() {
        return tempElement;
    }


    public WebElement getElement(){
        return this.element;
    }

    @Override
    public IElement next() {
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("jquery.js");
        String jqueryJs = null;
        try {
            jqueryJs = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebDriver driver = getBrowser().getCurrentBrowserDriver();
        WebElement webElement= (WebElement)( (JavascriptExecutor)driver).executeScript(jqueryJs+"return jQuery(\"" + jquery + "\")[0].next()");
        Element element1 = new Element(this.browser);
        element1.setElement(webElement);
        return element1;
    }

    @Override
    public List<IElement> brothers() {
        List<IElement> list = new ArrayList<IElement>();
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("jquery.js");
        String jqueryJs = null;
        try {
            jqueryJs = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebDriver driver = getBrowser().getCurrentBrowserDriver();
        List<WebElement> webElements= (List<WebElement>) ( (JavascriptExecutor)driver).executeScript(jqueryJs+"return jQuery(\"" + jquery + "\")[0].siblings()");
        for(WebElement webElement : webElements){
            Element element = new Element(getBrowser());
            element.setElement(webElement);
            list.add(element);
        }
        return list;
    }

    @Override
    public IElement child(By by) {
        Element child = new Element(getBrowser());
        child.setElement(getElement().findElement(by));
        return child;
    }

    @Override
    public IElement childs(By by,int index) {
        Element child = new Element(getBrowser());
        child.setElement(getElement().findElements(by).get(index));
        return child;
    }

    @Override
    public void contextClick() {
       this.actions.contextClick(this.element).build().perform();
    }


    public IElement node(String cssSelector){
        return new Element(browser,cssSelector);
    }

	public void setElement(WebElement element) {
		this.element = element;
	}
	
	public void keyPress(Keys key){
		this.element.sendKeys(key);
	}
	
	public void release(){
		this.actions.release(this.element).build().perform();
	}

	@Override
	public void keyPress(String keys) {
		this.element.sendKeys(Keys.chord(keys));
	}


    
    
}

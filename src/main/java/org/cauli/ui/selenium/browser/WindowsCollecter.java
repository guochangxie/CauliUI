package org.cauli.ui.selenium.browser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ouamaqing
 * Date: 13-5-30
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class WindowsCollecter extends EventObject {
    private Logger logger = LoggerFactory.getLogger(WindowsCollecter.class);
    public Set<String> windowhandles;
    public int windowNums;
    //这个里面存title和window信息
    private Map<String,WindowInfo> windowInfoMap;
    private List<WindowInfo> windowInfoList;
    private String currentSessionId;
    //这个存储url和window信息
    private Map<String,WindowInfo> windowInfourlMap;
    private IBrowser browser;
    public WindowsCollecter(Object source,IBrowser browser) {
        super(source);
        this.browser=browser;
        this.windowInfoMap=new HashMap<String, WindowInfo>();
        this.windowInfourlMap = new HashMap<String,WindowInfo>();
        this.windowInfoList=new ArrayList<WindowInfo>();
        this.windowNums=0;
        //this.windowhandles=browser.getWindows();
        this.windowhandles=new HashSet<String>();

    }

    public void updateWindows(){
        if(browser.getWindows().size()-this.windowNums>1){
            logger.warn("在测试过程中出现了同时打开多个页面的情况，页面收集器将对页面的索引查找功能可能会出现混乱，谨慎使用通过index来切换页面。");
        }
        Set<String> handles=browser.getWindows();
        if(handles.size()>this.windowNums){
            logger.info("窗口的句柄数增多，进行句柄收集操作, Handles Number->{}",windowNums);
            if(!isAlertHandle()){
                String currentWindowHandle=browser.getCurrentBrowserDriver().getWindowHandle();
                for(String windowhandle:handles){
                    if(this.windowhandles.contains(windowhandle)){
                        continue;
                    }else{
                        browser.getCurrentBrowserDriver().switchTo().window(windowhandle);
                        String url=browser.getCurrentBrowserDriver().getCurrentUrl();
                        String title=browser.getCurrentBrowserDriver().getTitle();
                        WindowInfo windowInfo = new WindowInfo(browser,url,windowhandle,title);
                        windowInfoMap.put(title,windowInfo);
                        windowInfourlMap.put(url,windowInfo);
                        windowInfoList.add(windowInfo);
                        this.windowNums=handles.size();
                        this.windowhandles=handles;
                        logger.info("添加了新的窗口信息->"+title);
                    }

                }
                browser.getCurrentBrowserDriver().switchTo().window(currentWindowHandle);
            }
        }else if(handles.size()<this.windowNums){
            String currentWindowHandle=browser.getCurrentBrowserDriver().getWindowHandle();
            for(String windowhandle:this.windowhandles){
                if(handles.contains(windowhandle)){
                    continue;
                }else{
                    browser.getCurrentBrowserDriver().switchTo().window(windowhandle);
                    String title=browser.currentPage().getTitle();
                    windowInfoList.remove(windowInfoMap.get(title));
                    windowInfoMap.remove(title);
                    windowInfourlMap.remove(browser.currentPage().getUrl());
                    this.windowNums=handles.size();
                    logger.info("更新了窗口信息，窗口->"+title+"被删除了");
                }
            }
            browser.getCurrentBrowserDriver().switchTo().window(currentWindowHandle);
        }else if(handles.size()==this.windowNums&&isCurrentPageChanged()){
//            String title=this.browser.getCurrentBrowserDriver().getTitle();
//            String currentUrl=this.browser.getCurrentBrowserDriver().getCurrentUrl();
//            String windownhandle=this.browser.getCurrentBrowserDriver().getWindowHandle();
//            WindowInfo windowInfo=new WindowInfo(this.browser,currentUrl,windownhandle,title);
//            this.windowInfoMap.put(title,windowInfo);
//            this.windowInfourlMap.put(currentUrl,windowInfo);
//            this.windowInfoList.remove(this.windowInfoList.get(windowInfoList.size()-1));
//            this.windowInfoList.add(windowInfo);
            logger.info("当前页面发生了变化，切换到了新的页面--->{},url->{}",browser.getCurrentBrowserDriver().getTitle(),browser.getCurrentBrowserDriver().getCurrentUrl());
        }

    }

    private boolean isAlertHandle(){
        try{
            browser.getCurrentBrowserDriver().switchTo().alert();
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public Map<String,WindowInfo> getWindowInfoMap(){
        return this.windowInfoMap;
    }

    public String getLastWindowhandle(){
        WindowInfo windowInfo=this.windowInfoList.get(this.windowNums-1);
        return windowInfo.getWindowHandle();
    }

    public String getFirstWindowhandle(){
        WindowInfo windowInfo = this.windowInfoMap.get(0);
        return windowInfo.getWindowHandle();
    }

    public String getWindowhandleByIndex(Integer index){
        WindowInfo windowInfo=this.windowInfoList.get(index-1);
        return windowInfo.getWindowHandle();
    }

    private boolean isCurrentPageChanged(){
        String currentWindowHandle = this.browser.getCurrentBrowserDriver().getWindowHandle();
        for(WindowInfo windowInfo:this.windowInfoList){
            if(windowInfo.getWindowHandle().equals(currentWindowHandle)){
            	try{
            		if(!windowInfo.getTitle().equals(this.browser.getCurrentBrowserDriver().getTitle())
                            ||!windowInfo.getUrl().equals(this.browser.getCurrentBrowserDriver().getCurrentUrl())){
                        return true;
                    }
            	}catch(Exception e){
            		return false;
            	}
                
            }
        }
        return false;
    }

}

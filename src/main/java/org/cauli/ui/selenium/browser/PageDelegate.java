package org.cauli.ui.selenium.browser;

import org.cauli.ui.selenium.page.ICurrentPage;

import java.lang.reflect.*;


/**
 * Created by celeskyking on 14-3-16
 */
public class PageDelegate {

    private static ICurrentPage page;
    private static ICurrentPage dispatcher = (ICurrentPage) Proxy.newProxyInstance(ICurrentPage.class.getClassLoader(),
            new Class<?>[]{ICurrentPage.class},
            new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] arg2) throws Throwable {
                    try {
                        if(page==null){
                            throw new RuntimeException("未找到可代理的页面");
                        }else{
                            return method.invoke(page,arg2);
                        }
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    }
                }
            });

    public static ICurrentPage getDispatcher(){
        return page;
    }

    public static void register(ICurrentPage currentPage){
        page=currentPage;
    }
}

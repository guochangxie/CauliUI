package org.cauli.ui.selenium.browser;


import com.sun.jdi.ClassNotPreparedException;
import org.cauli.ui.selenium.page.ICurrentPage;

import java.lang.reflect.*;


/**
 * @auther sky
 */
public class PageDelegate {

    private static ICurrentPage page;
    private static ICurrentPage dispatcher = (ICurrentPage) Proxy.newProxyInstance(ICurrentPage.class.getClassLoader(),
            new Class<?>[]{ICurrentPage.class},
            new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] arg2) throws Throwable {
                    try {
                        method.invoke(page,arg2);
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    }
                    return null;
                }
            });



    public static void unregister(Class<?>clazz){
        page=null;

    }

    public static ICurrentPage getDispatcher() {
        return page;
    }

    public static <T>T register(Class<T>clazz,IBrowser browser){
        try {
            Constructor constructor = clazz.getConstructor(IBrowser.class);
            if(constructor==null){
                throw new ClassNotFoundException("这个类和Page类不匹配");
            }
            page = (ICurrentPage) constructor.newInstance(browser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

}

package org.cauli.junit.statement;

import com.auto.anno.Browsers;
import com.auto.anno.Listener;
import com.auto.junit.anno.Retry;
import com.auto.junit.exception.TestFailedError;
import com.auto.junit.runtime.RuntimeMethod;
import com.auto.ui.browser.Auto;
import com.auto.ui.browser.Browser;
import com.auto.ui.listener.ActionListener;
import com.auto.ui.listener.ActionListenerProxy;
import org.apache.log4j.Logger;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class InterceptorStatement extends Statement {
	private Logger logger = Logger.getLogger(InterceptorStatement.class);
	protected final FrameworkMethod testMethod;
    protected Object target;
    private int times=0;
    private Class<? extends ActionListener>[] cls;
	public InterceptorStatement(FrameworkMethod testMethod, Object target) {
		this.testMethod=testMethod;
		this.target=target;
		//new PhantomLoad().load();
	}
	private List<Interceptor> interceptors = new ArrayList<Interceptor>();

	@Override
	public void evaluate() throws Throwable {
		 String className=testMethod.getMethod().getDeclaringClass().getName();
	        String name="Case:"+className.substring(className.lastIndexOf(".")+1, className.length())+"=>"+testMethod.getName();
	        RuntimeMethod.setName(name);
	        for(Interceptor interceptor:interceptors){
	            interceptor.interceptorBefore(testMethod, target);
	        }
	        //*****我们来定义方法级别的监听器注解，可以为专门的一个方法添加注解。也可以去判断类是否有监听器的注解来实现自动的注册和卸载。
	        loadListener();
	        if(this.testMethod.getMethod().isAnnotationPresent(Retry.class)){
	            times=this.testMethod.getMethod().getAnnotation(Retry.class).value();
	            logger.info("["+ RuntimeMethod.getName()+"]>>>>>>"+this.testMethod.getName()+">>>这个case执行失败的话会被重新执行"+times+"次");
	            //System.out.println(this.testMethod.getMethod().getDeclaringClass().getName());
	        }else if(this.testMethod.getMethod().getDeclaringClass().isAnnotationPresent(Retry.class)){
	            times=this.testMethod.getMethod().getDeclaringClass().getAnnotation(Retry.class).value();
	            logger.info("["+ RuntimeMethod.getName()+"]>>>>>>["+this.testMethod.getName()+"]>>>这个case执行失败的话会被重新执行"+times+"次");
	        }else{
	            this.times=0;
	        }
			logger.info("["+ RuntimeMethod.getName()+"]*******************测试用例["+this.testMethod.getName()+"]开始执行*****************");
	        if(testMethod.getMethod().isAnnotationPresent(Browsers.class)){
	            Browsers browsers=testMethod.getMethod().getAnnotation(Browsers.class);
	            Browser[] bs=browsers.value();
	            Auto.require(bs);
	            if(Auto.browserSet.get().size()!=0){
	                //System.out.println(Auto.browserSet.get().size());
	                Iterator<Browser> iterator = Auto.browserSet.get().iterator();
	                while(iterator.hasNext()){
	                    Browser browser=iterator.next();
	                    Auto.require(browser);
	                    for(int i=0;i<=times;i++){
	                        try{
	                            testMethod.invokeExplosively(target);
	                            break;
	                        }catch(Exception e){
	                            logger.error("["+ RuntimeMethod.getName()+"]用例执行失败了,异常信息->"+e.getMessage());
	                            if(i==times){
	                                throw new TestFailedError(this.testMethod.getName()+"]用例执行失败了！",e);
	                            }else{
	                                logger.info("["+ RuntimeMethod.getName()+"]用例执行失败，重新执行失败的方法-->"+testMethod.getName());
	                            }
	                        }
	                    }
	                }
	                Auto.local.get().setBrowser(null);
	            }else{
	                throw new RuntimeException("["+ RuntimeMethod.getName()+"]注解中给定的浏览器数据不正确！");
	            }
	        }else if(testMethod.getMethod().getDeclaringClass().isAnnotationPresent(Browsers.class)){
	        	 Browsers browsers=testMethod.getMethod().getDeclaringClass().getAnnotation(Browsers.class);
		            Browser[] bs=browsers.value();
		            Auto.require(bs);
		            if(Auto.browserSet.get().size()!=0){
		                //System.out.println(Auto.browserSet.get().size());
		                Iterator<Browser> iterator = Auto.browserSet.get().iterator();
		                while(iterator.hasNext()){
		                    Browser browser=iterator.next();
		                    Auto.require(browser);
		                    for(int i=0;i<=times;i++){
		                        try{
		                            testMethod.invokeExplosively(target);
		                            break;
		                        }catch(Exception e){
		                            logger.error("["+ RuntimeMethod.getName()+"]用例执行失败了,异常信息->"+e.getMessage());
		                            if(i==times){
		                                throw new TestFailedError(this.testMethod.getName()+"]用例执行失败了！",e);
		                            }else{
		                                logger.info("["+ RuntimeMethod.getName()+"]用例执行失败，重新执行失败的方法-->"+testMethod.getName());
		                            }
		                        }
		                    }
		                }
		                Auto.local.get().setBrowser(null);
		            }else{
		                throw new RuntimeException("["+ RuntimeMethod.getName()+"]注解中给定的浏览器数据不正确！");
		            }
	        }else{
	            for(int i=0;i<=times;i++){
	                try{
	                    testMethod.invokeExplosively(target);
	                    break;
	                }catch(Exception e){
	                    logger.error("["+ RuntimeMethod.getName()+"]用例执行失败了,异常信息->"+e.getMessage());
	                    if(i==times){
	                        throw new TestFailedError(this.testMethod.getName()+"]用例执行失败了！",e);
	                    }else{
	                        logger.info("["+ RuntimeMethod.getName()+"]用例执行失败，重新执行失败的方法-->"+testMethod.getName());
	                        ActionListenerProxy.getDispatcher().onRetryError();
	                    }
	                }
	            }
	        }

			for(Interceptor interceptor:interceptors){
				interceptor.interceptorAfter(testMethod, target);
			}
	        logger.info("["+RuntimeMethod.getName()+"]*******************测试用例["+testMethod.getName()+"]执行结束****************");
	        RuntimeMethod.setName(null);
	        clearActionListeners();
	}                                                                     000000
	
	
	public void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
	}

    public void removeInterceptor(Interceptor interceptor){
        interceptors.remove(interceptor);
    }

    public FrameworkMethod getTestMethod() {
        return testMethod;
    }

    public Object getTarget() {
        return target;
    }
    
    
    private void loadListener(){
    	if(this.testMethod.getMethod().getDeclaringClass().isAnnotationPresent(Listener.class)){
    		Listener listener = this.testMethod.getMethod().getDeclaringClass().getAnnotation(Listener.class);
    		this.cls = listener.value();
    		for(Class<? extends ActionListener> al : cls){
    			ActionListenerProxy.register(al);
    		}
    	}else if(this.testMethod.getMethod().isAnnotationPresent(Listener.class)){
    		Listener listener = this.testMethod.getAnnotation(Listener.class);
    		this.cls = listener.value();
    		for(Class<? extends ActionListener> al : cls){
    			ActionListenerProxy.register(al);
    		}
    	}
    }
    
    private void clearActionListeners(){
    	if(this.cls!=null){
    		for(Class<? extends ActionListener> cl : this.cls){
        		ActionListenerProxy.unregister(cl);
        	}
    	}
    	
    }
}

package org.cauli.junit.statement;

import org.apache.log4j.Logger;
import org.cauli.junit.anno.Retry;
import org.cauli.junit.exception.TestFailedError;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;


public class InterceptorStatement extends Statement {
	private Logger logger = Logger.getLogger(InterceptorStatement.class);
	protected final FrameworkMethod testMethod;
    protected Object target;
    private int times=0;
	public InterceptorStatement(FrameworkMethod testMethod, Object target) {
		this.testMethod=testMethod;
		this.target=target;
		//new PhantomLoad().load();
	}
	private List<Interceptor> interceptors = new ArrayList<Interceptor>();

    @Override
	public void evaluate() throws Throwable {
        for(Interceptor interceptor:interceptors){
            interceptor.interceptorBefore(testMethod, target);
        }
        //*****我们来定义方法级别的监听器注解，可以为专门的一个方法添加注解。也可以去判断类是否有监听器的注解来实现自动的注册和卸载。
        if(this.testMethod.getMethod().isAnnotationPresent(Retry.class)){
            times=this.testMethod.getMethod().getAnnotation(Retry.class).value();
            logger.info(this.testMethod.getName()+">>>这个case执行失败的话会被重新执行"+times+"次");
            //System.out.println(this.testMethod.getMethod().getDeclaringClass().getName());
        }else if(this.testMethod.getMethod().getDeclaringClass().isAnnotationPresent(Retry.class)){
            times=this.testMethod.getMethod().getDeclaringClass().getAnnotation(Retry.class).value();
            logger.info(this.testMethod.getName()+"]>>>这个case执行失败的话会被重新执行"+times+"次");
        }else{
            this.times=0;
        }
        logger.info("*******************测试用例["+this.testMethod.getName()+"]开始执行*****************");
        for(int i=0;i<=times;i++){
            try{
                testMethod.invokeExplosively(target);
                break;
            }catch(Exception e){
                logger.error("用例执行失败了,异常信息->"+e.getMessage());
                if(i==times){
                    throw new TestFailedError(this.testMethod.getName()+"]用例执行失败了！",e);
                }else{
                    logger.info("用例执行失败，重新执行失败的方法-->"+testMethod.getName());
                }
            }
        }

        for(Interceptor interceptor:interceptors){
            interceptor.interceptorAfter(testMethod, target);
        }
        logger.info("*******************测试用例["+testMethod.getName()+"]执行结束****************");
	}
	
	
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

}

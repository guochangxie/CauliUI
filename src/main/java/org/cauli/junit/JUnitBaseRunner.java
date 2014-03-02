package org.cauli.junit;



import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.cauli.instrument.ClassPool;
import org.cauli.junit.anno.InterceptorClass;
import org.cauli.junit.anno.ThreadRunner;
import org.cauli.junit.statement.Interceptor;
import org.cauli.junit.statement.InterceptorStatement;
import org.cauli.ui.annotation.CauliRule;
import org.cauli.ui.annotation.Listener;
import org.junit.Rule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 王天庆
 * */
public class JUnitBaseRunner extends Feeder{
    public JUnitBaseRunner(final Class<?> klass)
            throws InitializationError {
        super(klass);
        setScheduler(new RunnerScheduler() {
            ExecutorService executorService = Executors.newFixedThreadPool(
                    klass.isAnnotationPresent(ThreadRunner.class) ?
                            klass.getAnnotation(ThreadRunner.class).threads() :1,
                    new NamedThreadFactory(klass.getSimpleName()));
            CompletionService<Void> completionService = new ExecutorCompletionService<Void>(executorService);
            Queue<Future<Void>> tasks = new LinkedList<Future<Void>>();

            public void schedule(Runnable childStatement) {
                tasks.offer(completionService.submit(childStatement, null));
            }


            public void finished() {
                try {
                    while (!tasks.isEmpty())
                        tasks.remove(completionService.take());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    while (!tasks.isEmpty())
                        tasks.poll().cancel(true);
                    executorService.shutdownNow();
                }
            }

        });
    }
    static final class NamedThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final ThreadGroup group;

        NamedThreadFactory(String poolName) {
            group = new ThreadGroup(poolName + "-" + poolNumber.getAndIncrement());
        }


        public Thread newThread(Runnable r) {
            return new Thread(group, r, group.getName() + "-thread-" + threadNumber.getAndIncrement(), 0);
        }
    }


    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        InterceptorStatement statement = new InterceptorStatement(method, test);
        return statement;
    }


    public List<TestRule> getRules()  {
        List<TestRule> testRules = Lists.newArrayList();
        Set<Class<?>> classes = ClassPool.getClassPool();
        for(Class<?> clazz :classes){
            if(clazz.isAnnotationPresent(CauliRule.class)||clazz.isAnnotationPresent(Listener.class)){
                try {
                    testRules.add((TestRule) clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return testRules;
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            filter(new CauliFilter());
        } catch (NoTestsRemainException e) {
            e.printStackTrace();
        }
        super.run(notifier);
    }

    @Override
    protected List<TestRule> getTestRules(Object target) {
        List<TestRule> result = getTestClass().getAnnotatedMethodValues(target,
                Rule.class, TestRule.class);

        result.addAll(getTestClass().getAnnotatedFieldValues(target,
                Rule.class, TestRule.class));
        result.addAll(getRules());
        return result;
    }
}

package org.cauli.junit;

import org.junit.runners.model.FrameworkMethod;

import java.util.List;

/**
 * Created by tianqing.wang on 14-2-28
 */
public interface ParameterProvider {

    public List<FrameworkMethod> generator(FrameworkMethod method);
}

package org.cauli.junit;

import org.junit.runners.model.FrameworkMethod;

import java.util.List;

/**
 * Created by tianqing.wang on 14-2-28
 */
public class ParameterGenerator {
    private String file;
    public ParameterGenerator(String file){
        this.file=file;
    }

    public List<FrameworkMethod> generator(FrameworkMethod method){
        return null;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}

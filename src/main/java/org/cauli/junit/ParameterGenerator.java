package org.cauli.junit;

import com.google.common.collect.Lists;
import org.cauli.junit.commons.StringUtils;
import org.cauli.junit.info.DefaultInfoProvider;
import org.junit.runners.model.FrameworkMethod;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by tianqing.wang on 14-2-28
 */
public class ParameterGenerator implements ParameterProvider{
    private File file;
    private FileGenerator fileGenerator;
    public ParameterGenerator(File file){
        this.file=file;
    }

    public List<FrameworkMethod> generator(FrameworkMethod method) throws IOException {
        List<FrameworkMethod> result = Lists.newArrayList();
        DefaultInfoProvider infoProvider = new DefaultInfoProvider();
        Method m = method.getMethod();
        if(!file.exists()){
            throw new RuntimeException(getClass().getName()+"找不到文件...");
        }else{
            if(file.getName().endsWith("txt")){
                this.fileGenerator=new TXTGenerator(file);
            }else{
                this.fileGenerator=new ExcelGenerator(file,method.getMethod());
            }
        }
        List<RowParameter> rowParameters = fileGenerator.generator();
        for(RowParameter parameter:rowParameters){
            Object[] params = new Object[parameter.getParams().size()];
            int i=0;
            for(String string:parameter.getParams()){
                Class<?> clazz = m.getParameterTypes()[i];
                if(clazz.getName().endsWith("Integer")){
                    if(string.contains(".")){
                        string=string.substring(0,string.indexOf("."));
                    }
                    params[i]= StringUtils.toInteger(string);
                }else if(clazz.getName().endsWith("Boolean")){
                    params[i]=StringUtils.toBoolean(string);
                }else if(clazz.getName().endsWith("String")){
                    if(string.equals("<empty>")){
                        params[i]="";
                    }else if(string.equals("<null>")){
                        params[i]=null;
                    }else{
                        params[i]=string;
                    }
                }else if(clazz.getName().endsWith("Double")){
                    params[i]=StringUtils.toDouble(string);
                }else if(clazz.getName().endsWith("Float")){
                    params[i]=StringUtils.toFloat(string);
                }else if(clazz.getName().endsWith("Long")){
                    params[i]=StringUtils.toLong(string);
                }else{
                    throw new UnsupportedOperationException("暂时不支持非基础属性的参数形式，支持String,Integer,Boolean," +
                            "Double,Float,Long");
                }
                i++;
            }
            FrameworkMethodWithParameters frameworkMethodWithParameters=new FrameworkMethodWithParameters(m,params,infoProvider.testInfo(m,params));
            result.add(frameworkMethodWithParameters);
        }
        return null;
    }

    public FileGenerator getFileGenerator() {
        return fileGenerator;
    }

    public void setFileGenerator(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}

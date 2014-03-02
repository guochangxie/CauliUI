package org.cauli.junit;

import com.google.common.collect.Lists;
import org.apache.commons.exec.util.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by celeskyking on 14-3-2
 */
public class TXTGenerator implements FileGenerator{
    private File txt;
    public TXTGenerator(File txt){
        this.txt=txt;
    }

    public List<RowParameter> generator() {
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(this.txt);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取文件的错误...");
        }
        List<RowParameter> parameters = Lists.newArrayList();
        for(String line:lines){
            RowParameter rowParameter=new RowParameter();
            String[] strings = StringUtils.split(line,",");
            for(String str:strings){
                rowParameter.addParam(str);
            }
            parameters.add(rowParameter);
        }
        return parameters;
    }
}

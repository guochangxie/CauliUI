package org.cauli.junit;

import java.util.List;

/**
 * Created by celeskyking on 14-3-2
 */
public class RowParameter {
    public List<String> params;

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public void addParam(String value){
        this.params.add(value);
    }
}

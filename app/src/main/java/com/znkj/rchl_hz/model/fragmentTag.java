package com.znkj.rchl_hz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo-pc on 2018-01-29.
 */
public class fragmentTag implements Serializable {

    private String dm;
    private String name;

    public static fragmentTag ft = new fragmentTag();

    private fragmentTag(){}

    public static fragmentTag getFtTag(){
        return ft;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

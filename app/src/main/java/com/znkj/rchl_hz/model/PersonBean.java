package com.znkj.rchl_hz.model;

/**
 * Created by heziwen on 2018/4/13.
 * 作用：
 */

public class PersonBean {
    private String name;
    private String dm;
    //private int age;

    public PersonBean(String name, String dm) {
        this.name = name;
        this.dm = dm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }
}

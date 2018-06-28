package com.znkj.rchl_hz.contact;

import java.io.Serializable;

public class Contact implements Serializable {
    private String mName;
    private int mType;
    private int imgUrl;

    public Contact(String name, int type, int url) {
        mName = name;
        mType = type;
        imgUrl = url;
    }

    public String getmName() {
        return mName;
    }

    public int getmType() {
        return mType;
    }

    public int getImgUrl() {
        return imgUrl;
    }

}

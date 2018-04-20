package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/3.
 */

public class policys implements Serializable {
    public String TYPE;
    public String TypeName;
    public String SubTitle;
    public String DEADLINE;
    public String PRICE;

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getDEADLINE() {
        return DEADLINE;
    }

    public void setDEADLINE(String DEADLINE) {
        this.DEADLINE = DEADLINE;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }
}

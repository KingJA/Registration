package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class PhotoListInfo implements Serializable {
    String INDEX;//"INDEX":"1"  下标
    String REMARK;//"REMARK":"前侧车身照"    图片名字
    boolean IsValid;//"IsValid":true    是否需要展示
    boolean IsRequire;//"IsRequire":true    是否必选

    public String getINDEX() {
        return INDEX;
    }

    public void setINDEX(String INDEX) {
        this.INDEX = INDEX;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public boolean isValid() {
        return IsValid;
    }

    public void setValid(boolean valid) {
        IsValid = valid;
    }

    public boolean isRequire() {
        return IsRequire;
    }

    public void setRequire(boolean require) {
        IsRequire = require;
    }
}

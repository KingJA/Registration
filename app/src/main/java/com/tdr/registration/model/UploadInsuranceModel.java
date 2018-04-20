package com.tdr.registration.model;

import java.io.Serializable;

/**
 *   需要上传的保险对象
 */

public class UploadInsuranceModel implements Serializable{
    private String POLICYID;
    private String Type;
    private String REMARKID;
    private String DeadLine;
    private String BUYDATE;

    public String getPOLICYID() {
        return POLICYID;
    }

    public void setPOLICYID(String POLICYID) {
        this.POLICYID = POLICYID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getREMARKID() {
        return REMARKID;
    }

    public void setREMARKID(String REMARKID) {
        this.REMARKID = REMARKID;
    }

    public String getDeadLine() {
        return DeadLine;
    }

    public void setDeadLine(String deadLine) {
        DeadLine = deadLine;
    }

    public String getBUYDATE() {
        return BUYDATE;
    }

    public void setBUYDATE(String BUYDATE) {
        this.BUYDATE = BUYDATE;
    }
}

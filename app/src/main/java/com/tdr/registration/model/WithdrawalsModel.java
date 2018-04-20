package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/2.
 */

public class WithdrawalsModel implements Serializable {
    /**
     *
     "FROM_USERID": null,
     "FROM_USERNAME": null,
     "TO_USERNAME": "管理员",
     "ACTTYPE": null,

     "LISTID": "DBCAB385-1A41-4697-A9AE-6581C2452779",
     "USERID": "618D9E54-F8B0-4B7B-A389-39596F6CA849",
     "PAYEENAME": "iOS",
     "PAYEEPHONE": "13163240531",
     "PAYEEALIPAY": "13163240531",
     "APPLYAMOUNT": 100,
     "STATE": 0,
     "APPLYTIME": "2017-11-02 17:15:48",
     "OPERATETIME": null,
     "REMARK": null,
     "SOB": 1
     */
    boolean Selelct;
    String FROM_USERID;
    String FROM_USERNAME;
    String BILLTIME;
    String TO_USERNAME;
    String ACTTYPE_NAME;
    String LISTID;
    String USERID;
    String PAYEENAME;
    String PAYEEPHONE;
    String PAYEEALIPAY;
    String APPLYAMOUNT;
    String STATE;
    String APPLYTIME;
    String OPERATETIME;
    String REMARK;
    String SOB;



    public boolean isSelelct() {
        return Selelct;
    }

    public void setSelelct(boolean selelct) {
        Selelct = selelct;
    }

    public String getFROM_USERID() {
        return FROM_USERID;
    }

    public void setFROM_USERID(String FROM_USERID) {
        this.FROM_USERID = FROM_USERID;
    }

    public String getFROM_USERNAME() {
        return FROM_USERNAME;
    }

    public void setFROM_USERNAME(String FROM_USERNAME) {
        this.FROM_USERNAME = FROM_USERNAME;
    }

    public String getBILLTIME() {
        return BILLTIME;
    }

    public void setBILLTIME(String BILLTIME) {
        this.BILLTIME = BILLTIME;
    }

    public String getTO_USERNAME() {
        return TO_USERNAME;
    }

    public void setTO_USERNAME(String TO_USERNAME) {
        this.TO_USERNAME = TO_USERNAME;
    }

    public String getACTTYPE_NAME() {
        return ACTTYPE_NAME;
    }

    public void setACTTYPE_NAME(String ACTTYPE_NAME) {
        this.ACTTYPE_NAME = ACTTYPE_NAME;
    }

    public String getLISTID() {
        return LISTID;
    }

    public void setLISTID(String LISTID) {
        this.LISTID = LISTID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getPAYEENAME() {
        return PAYEENAME;
    }

    public void setPAYEENAME(String PAYEENAME) {
        this.PAYEENAME = PAYEENAME;
    }

    public String getPAYEEPHONE() {
        return PAYEEPHONE;
    }

    public void setPAYEEPHONE(String PAYEEPHONE) {
        this.PAYEEPHONE = PAYEEPHONE;
    }

    public String getPAYEEALIPAY() {
        return PAYEEALIPAY;
    }

    public void setPAYEEALIPAY(String PAYEEALIPAY) {
        this.PAYEEALIPAY = PAYEEALIPAY;
    }

    public String getAPPLYAMOUNT() {
        return APPLYAMOUNT;
    }

    public void setAPPLYAMOUNT(String APPLYAMOUNT) {
        this.APPLYAMOUNT = APPLYAMOUNT;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getAPPLYTIME() {
        return APPLYTIME;
    }

    public void setAPPLYTIME(String APPLYTIME) {
        this.APPLYTIME = APPLYTIME;
    }

    public String getOPERATETIME() {
        return OPERATETIME;
    }

    public void setOPERATETIME(String OPERATETIME) {
        this.OPERATETIME = OPERATETIME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getSOB() {
        return SOB;
    }

    public void setSOB(String SOB) {
        this.SOB = SOB;
    }
}

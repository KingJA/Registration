package com.tdr.registration.model;

/**
 * Created by Administrator on 2017/11/8.
 */

public class OrderModel {
    /**
     * "LISTID": "8c9bb726-470b-41be-96fe-1401bdcbf4b8",
     * "BILLNO": "2017110814314733164901",
     * "RECORDID": "A7B8E84C628648B9B855BC5A5830F15E",
     * "UNITID": "37180C76-66A2-D45A-5E05-36801A8C0935",
     * "USERID": "618D9E54-F8B0-4B7B-A389-39596F6CA849",
     * "ACTTYPE": 1,
     * "MONEY": 80.0,
     * "PAYTYPE": 1,
     * "PAYSTATUS": 0,
     * "BILLTIME": "2017-09-18 16:25:24",
     * "INTIME": "2017-11-08 14:31:47",
     * "REMARK": "0000001",
     * "SOB": 1
     */


    private boolean Selelct;
    private String LISTID;
    private String BILLNO;
    private String RECORDID;
    private String UNITID;
    private String USERID;
    private String ACTTYPE;    // 交易类型 1 支付  2 提现
    private String MONEY;
    private String PAYTYPE;
    private String PAYSTATUS;// 支付状态(0 未支付  1 已支付 2 已结算)
    private String BILLTIME;// 交易时间
    private String INTIME;
    private String REMARK;
    private String SOB;

    public boolean isSelelct() {
        return Selelct;
    }

    public void setSelelct(boolean selelct) {
        Selelct = selelct;
    }

    public String getLISTID() {
        return LISTID;
    }

    public void setLISTID(String LISTID) {
        this.LISTID = LISTID;
    }

    public String getBILLNO() {
        return BILLNO;
    }

    public void setBILLNO(String BILLNO) {
        this.BILLNO = BILLNO;
    }

    public String getRECORDID() {
        return RECORDID;
    }

    public void setRECORDID(String RECORDID) {
        this.RECORDID = RECORDID;
    }

    public String getUNITID() {
        return UNITID;
    }

    public void setUNITID(String UNITID) {
        this.UNITID = UNITID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getACTTYPE() {
        return ACTTYPE;
    }

    public void setACTTYPE(String ACTTYPE) {
        this.ACTTYPE = ACTTYPE;
    }

    public String getMONEY() {
        return MONEY;
    }

    public void setMONEY(String MONEY) {
        this.MONEY = MONEY;
    }

    public String getPAYTYPE() {
        return PAYTYPE;
    }

    public void setPAYTYPE(String PAYTYPE) {
        this.PAYTYPE = PAYTYPE;
    }

    public String getPAYSTATUS() {
        return PAYSTATUS;
    }

    public void setPAYSTATUS(String PAYSTATUS) {
        this.PAYSTATUS = PAYSTATUS;
    }

    public String getBILLTIME() {
        return BILLTIME;
    }

    public void setBILLTIME(String BILLTIME) {
        this.BILLTIME = BILLTIME;
    }

    public String getINTIME() {
        return INTIME;
    }

    public void setINTIME(String INTIME) {
        this.INTIME = INTIME;
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

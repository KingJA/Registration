package com.tdr.registration.model;

/**
 * Created by Administrator on 2017/7/31.
 */

public class VisitTypeModel {

    public String REASONID;
    public String CASE_STATUS;
    public String PAY_STATUS;
    public String WITHDRAWALS_STATUS;
    public String NAME;
    public boolean isSelect = false;

    public String getWITHDRAWALS_STATUS() {
        return WITHDRAWALS_STATUS;
    }

    public void setWITHDRAWALS_STATUS(String WITHDRAWALS_STATUS) {
        this.WITHDRAWALS_STATUS = WITHDRAWALS_STATUS;
    }

    public String getPAY_STATUS() {
        return PAY_STATUS;
    }

    public void setPAY_STATUS(String PAY_STATUS) {
        this.PAY_STATUS = PAY_STATUS;
    }


    public String getREASONID() {
        return REASONID;
    }

    public void setREASONID(String REASONID) {
        this.REASONID = REASONID;
    }

    public String getCASE_STATUS() {
        return CASE_STATUS;
    }

    public void setCASE_STATUS(String CASE_STATUS) {
        this.CASE_STATUS = CASE_STATUS;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

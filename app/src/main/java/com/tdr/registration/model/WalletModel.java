package com.tdr.registration.model;

/**
 * Created by Administrator on 2017/10/31.
 */

public class WalletModel {
    //      "USERID": "618D9E54-F8B0-4B7B-A389-39596F6CA849",
//              "BALANCE": 0,
//             "CWABLEBAL": 0,
//             "PAYPASSWORD": null
    String USERID;
    String BALANCE;
    String CWABLEBAL;
    String PAYPASSWORD;

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String BALANCE) {
        this.BALANCE = BALANCE;
    }

    public String getCWABLEBAL() {
        return CWABLEBAL;
    }

    public void setCWABLEBAL(String CWABLEBAL) {
        this.CWABLEBAL = CWABLEBAL;
    }

    public String getPAYPASSWORD() {
        return PAYPASSWORD;
    }

    public void setPAYPASSWORD(String PAYPASSWORD) {
        this.PAYPASSWORD = PAYPASSWORD;
    }
}

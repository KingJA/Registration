package com.tdr.kingja.entity;

import java.io.Serializable;

/**
 * Description:TODO
 * Create Time:2018/6/13 13:15
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryInfo implements Serializable{

    /**
     * BATTERYID : F8DF6B37-BFDD-4EBD-A139-7FD12B8A14A9
     * VehicleType : 1
     * ECID : 5FEFBF2C-0296-4E59-AC81-89BB47BBD73C
     * PLATENUMBER : 0456456
     * BATTERY_RECORDID : 100000180
     * BATTERY_THEFTNO : 80231111111
     * VEHICLEBRAND : 大苏打
     * VEHICLEMODELS : 31
     * COLORID : 是否
     * BATTERY_QUANTITY : 3
     * BUYDATE : 2018-05-31 09:18:00
     * PRICE : 3123.0
     * OWNER_NAME : jj
     * OWNER_CARDID : 1
     * OWNER_CARDTYPE : 5
     * OWNER_PHONE : 15858585558
     * OWNER_ADDRESS : hh
     * PHOTOLIST : CE8921DC-7AC8-4613-81AD-C99D324265F0
     * REMARK : null
     * CURRENT_STATUS : 0
     * STATUS_CHANGE_DATE : 2018-05-31 09:18:47
     * STATUS_CHANGE_USERID : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * ISVALID : 1
     * UNITID : null
     * CREATED_USER_ID : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * CREATED_ON : 2018-05-31 09:18:47
     * OPERATORTIME : 2018-05-31 09:18:47
     * OPERATORID : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * UNITNAME :
     * IsStolenCar : 0
     * OPERATOR_NAME : 管理员
     * CREATED_USER_NAME : 管理员
     * STATUS_CHANGE_USER_NAME : 管理员
     */

    private String BATTERYID;
    private String VehicleType;
    private String ECID;
    private String PLATENUMBER;
    private String BATTERY_RECORDID;
    private String BATTERY_THEFTNO;
    private String VEHICLEBRAND;
    private String VEHICLEMODELS;
    private String COLORID;
    private int BATTERY_QUANTITY;
    private String BUYDATE;
    private double PRICE;
    private String OWNER_NAME;
    private String OWNER_CARDID;
    private int OWNER_CARDTYPE;
    private String OWNER_PHONE;
    private String OWNER_ADDRESS;
    private String PHOTOLIST;
    private String REMARK;
    private int CURRENT_STATUS;
    private String STATUS_CHANGE_DATE;
    private String STATUS_CHANGE_USERID;
    private int ISVALID;
    private String UNITID;
    private String CREATED_USER_ID;
    private String CREATED_ON;
    private String OPERATORTIME;
    private String OPERATORID;
    private String UNITNAME;
    private int IsStolenCar;
    private String OPERATOR_NAME;
    private String CREATED_USER_NAME;
    private String STATUS_CHANGE_USER_NAME;

    public String getBATTERYID() {
        return BATTERYID;
    }

    public void setBATTERYID(String BATTERYID) {
        this.BATTERYID = BATTERYID;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String VehicleType) {
        this.VehicleType = VehicleType;
    }

    public String getECID() {
        return ECID;
    }

    public void setECID(String ECID) {
        this.ECID = ECID;
    }

    public String getPLATENUMBER() {
        return PLATENUMBER;
    }

    public void setPLATENUMBER(String PLATENUMBER) {
        this.PLATENUMBER = PLATENUMBER;
    }

    public String getBATTERY_RECORDID() {
        return BATTERY_RECORDID;
    }

    public void setBATTERY_RECORDID(String BATTERY_RECORDID) {
        this.BATTERY_RECORDID = BATTERY_RECORDID;
    }

    public String getBATTERY_THEFTNO() {
        return BATTERY_THEFTNO;
    }

    public void setBATTERY_THEFTNO(String BATTERY_THEFTNO) {
        this.BATTERY_THEFTNO = BATTERY_THEFTNO;
    }

    public String getVEHICLEBRAND() {
        return VEHICLEBRAND;
    }

    public void setVEHICLEBRAND(String VEHICLEBRAND) {
        this.VEHICLEBRAND = VEHICLEBRAND;
    }

    public String getVEHICLEMODELS() {
        return VEHICLEMODELS;
    }

    public void setVEHICLEMODELS(String VEHICLEMODELS) {
        this.VEHICLEMODELS = VEHICLEMODELS;
    }

    public String getCOLORID() {
        return COLORID;
    }

    public void setCOLORID(String COLORID) {
        this.COLORID = COLORID;
    }

    public int getBATTERY_QUANTITY() {
        return BATTERY_QUANTITY;
    }

    public void setBATTERY_QUANTITY(int BATTERY_QUANTITY) {
        this.BATTERY_QUANTITY = BATTERY_QUANTITY;
    }

    public String getBUYDATE() {
        return BUYDATE;
    }

    public void setBUYDATE(String BUYDATE) {
        this.BUYDATE = BUYDATE;
    }

    public double getPRICE() {
        return PRICE;
    }

    public void setPRICE(double PRICE) {
        this.PRICE = PRICE;
    }

    public String getOWNER_NAME() {
        return OWNER_NAME;
    }

    public void setOWNER_NAME(String OWNER_NAME) {
        this.OWNER_NAME = OWNER_NAME;
    }

    public String getOWNER_CARDID() {
        return OWNER_CARDID;
    }

    public void setOWNER_CARDID(String OWNER_CARDID) {
        this.OWNER_CARDID = OWNER_CARDID;
    }

    public int getOWNER_CARDTYPE() {
        return OWNER_CARDTYPE;
    }

    public void setOWNER_CARDTYPE(int OWNER_CARDTYPE) {
        this.OWNER_CARDTYPE = OWNER_CARDTYPE;
    }

    public String getOWNER_PHONE() {
        return OWNER_PHONE;
    }

    public void setOWNER_PHONE(String OWNER_PHONE) {
        this.OWNER_PHONE = OWNER_PHONE;
    }

    public String getOWNER_ADDRESS() {
        return OWNER_ADDRESS;
    }

    public void setOWNER_ADDRESS(String OWNER_ADDRESS) {
        this.OWNER_ADDRESS = OWNER_ADDRESS;
    }

    public String getPHOTOLIST() {
        return PHOTOLIST;
    }

    public void setPHOTOLIST(String PHOTOLIST) {
        this.PHOTOLIST = PHOTOLIST;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public int getCURRENT_STATUS() {
        return CURRENT_STATUS;
    }

    public void setCURRENT_STATUS(int CURRENT_STATUS) {
        this.CURRENT_STATUS = CURRENT_STATUS;
    }

    public String getSTATUS_CHANGE_DATE() {
        return STATUS_CHANGE_DATE;
    }

    public void setSTATUS_CHANGE_DATE(String STATUS_CHANGE_DATE) {
        this.STATUS_CHANGE_DATE = STATUS_CHANGE_DATE;
    }

    public String getSTATUS_CHANGE_USERID() {
        return STATUS_CHANGE_USERID;
    }

    public void setSTATUS_CHANGE_USERID(String STATUS_CHANGE_USERID) {
        this.STATUS_CHANGE_USERID = STATUS_CHANGE_USERID;
    }

    public int getISVALID() {
        return ISVALID;
    }

    public void setISVALID(int ISVALID) {
        this.ISVALID = ISVALID;
    }

    public String getUNITID() {
        return UNITID;
    }

    public void setUNITID(String UNITID) {
        this.UNITID = UNITID;
    }

    public String getCREATED_USER_ID() {
        return CREATED_USER_ID;
    }

    public void setCREATED_USER_ID(String CREATED_USER_ID) {
        this.CREATED_USER_ID = CREATED_USER_ID;
    }

    public String getCREATED_ON() {
        return CREATED_ON;
    }

    public void setCREATED_ON(String CREATED_ON) {
        this.CREATED_ON = CREATED_ON;
    }

    public String getOPERATORTIME() {
        return OPERATORTIME;
    }

    public void setOPERATORTIME(String OPERATORTIME) {
        this.OPERATORTIME = OPERATORTIME;
    }

    public String getOPERATORID() {
        return OPERATORID;
    }

    public void setOPERATORID(String OPERATORID) {
        this.OPERATORID = OPERATORID;
    }

    public String getUNITNAME() {
        return UNITNAME;
    }

    public void setUNITNAME(String UNITNAME) {
        this.UNITNAME = UNITNAME;
    }

    public int getIsStolenCar() {
        return IsStolenCar;
    }

    public void setIsStolenCar(int IsStolenCar) {
        this.IsStolenCar = IsStolenCar;
    }

    public String getOPERATOR_NAME() {
        return OPERATOR_NAME;
    }

    public void setOPERATOR_NAME(String OPERATOR_NAME) {
        this.OPERATOR_NAME = OPERATOR_NAME;
    }

    public String getCREATED_USER_NAME() {
        return CREATED_USER_NAME;
    }

    public void setCREATED_USER_NAME(String CREATED_USER_NAME) {
        this.CREATED_USER_NAME = CREATED_USER_NAME;
    }

    public String getSTATUS_CHANGE_USER_NAME() {
        return STATUS_CHANGE_USER_NAME;
    }

    public void setSTATUS_CHANGE_USER_NAME(String STATUS_CHANGE_USER_NAME) {
        this.STATUS_CHANGE_USER_NAME = STATUS_CHANGE_USER_NAME;
    }
}

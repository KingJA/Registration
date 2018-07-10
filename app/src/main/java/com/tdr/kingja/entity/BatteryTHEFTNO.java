package com.tdr.kingja.entity;

/**
 * Description:TODO
 * Create Time:2018/7/10 9:36
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryTHEFTNO {

    /**
     * Number : 3
     * Field : THEFTNO3
     * Name : 温州溯源标签
     * Value : 32804
     * EqType : 8024
     * Regular : ^$|^(8024)\d{10}$
     * Alias : 2.4G
     * VehicleType : 1
     * isValid : true
     */

    private int Number;
    private String Field;
    private String Name;
    private String Value;
    private String EqType;
    private String Regular;
    private String Alias;
    private int VehicleType;
    private boolean isValid;

    public int getNumber() {
        return Number;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }

    public String getField() {
        return Field;
    }

    public void setField(String Field) {
        this.Field = Field;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getEqType() {
        return EqType;
    }

    public void setEqType(String EqType) {
        this.EqType = EqType;
    }

    public String getRegular() {
        return Regular;
    }

    public void setRegular(String Regular) {
        this.Regular = Regular;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String Alias) {
        this.Alias = Alias;
    }

    public int getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(int VehicleType) {
        this.VehicleType = VehicleType;
    }

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
}

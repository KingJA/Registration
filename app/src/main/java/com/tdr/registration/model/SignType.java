package com.tdr.registration.model;

/**
 * Description:TODO
 * Create Time:2018/5/31 13:33
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class SignType {
    private int Number;
    private String Field;
    private String Name;
    private String Value;
    private String Alias;
    private String EqType;
    private String Regular;
    private String VehicleType;
    private boolean isValid;
    private boolean IsScan;

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getEqType() {
        return EqType;
    }

    public void setEqType(String eqType) {
        EqType = eqType;
    }

    public String getRegular() {
        return Regular;
    }

    public void setRegular(String regular) {
        Regular = regular;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isScan() {
        return IsScan;
    }

    public void setScan(boolean scan) {
        IsScan = scan;
    }

    @Override
    public String toString() {
        return "SignType{" +
                "Number=" + Number +
                ", Field='" + Field + '\'' +
                ", Name='" + Name + '\'' +
                ", Value='" + Value + '\'' +
                ", Alias='" + Alias + '\'' +
                ", EqType='" + EqType + '\'' +
                ", Regular='" + Regular + '\'' +
                ", VehicleType='" + VehicleType + '\'' +
                ", isValid=" + isValid +
                ", IsScan=" + IsScan +
                '}';
    }
}

package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2017/1/6.
 */

public class RegistrationPointModel implements Serializable{


    /**
     * RegistersiteId : 017F9BDED31F4FFF9C08E774B5B8B202
     * RegistersiteName : 温州鹿城点
     * DepartmentId : 3303001
     * CodeNo : null
     * Adress : 浙江省温州市龙湾区蒲州派出所
     * Lat : 119
     * Lng : 27.032836
     * Name : 徐圣国
     * Phone : 13500000101
     * Remark : 测试青年
     */

    private String RegistersiteId;
    private String RegistersiteName;
    private String DepartmentId;
    private String CodeNo;
    private String Adress;
    private double Lat;
    private double Lng;
    private String Name;
    private String Phone;
    private String Remark;

    public String getRegistersiteId() {
        return RegistersiteId;
    }

    public void setRegistersiteId(String RegistersiteId) {
        this.RegistersiteId = RegistersiteId;
    }

    public String getRegistersiteName() {
        return RegistersiteName;
    }

    public void setRegistersiteName(String RegistersiteName) {
        this.RegistersiteName = RegistersiteName;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String DepartmentId) {
        this.DepartmentId = DepartmentId;
    }

    public String getCodeNo() {
        return CodeNo;
    }

    public void setCodeNo(String CodeNo) {
        this.CodeNo = CodeNo;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String Adress) {
        this.Adress = Adress;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double Lat) {
        this.Lat = Lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double Lng) {
        this.Lng = Lng;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
}

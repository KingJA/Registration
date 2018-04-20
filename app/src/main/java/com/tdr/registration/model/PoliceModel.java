package com.tdr.registration.model;

/**
 * 派出所
 * Created by Linus_Xie on 2016/9/28.
 */
public class PoliceModel {
    private String name;
    private String zipCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public PoliceModel(String name, String zipCode) {
        this.name = name;
        this.zipCode = zipCode;
    }

    public PoliceModel() {
        super();
    }


    @Override
    public String toString() {
        return "PoliceModel{" +
                "name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}

package com.tdr.registration.model;

import java.util.List;

/**
 * 区
 * Created by Linus_Xie on 2016/9/28.
 */
public class DistrictModel {
    private String name;
    private String zipCode;
    private List<PoliceModel> policeModels;

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

    public List<PoliceModel> getPoliceModels() {
        return policeModels;
    }

    public void setPoliceModels(List<PoliceModel> policeModels) {
        this.policeModels = policeModels;
    }

    public DistrictModel() {
    }

    public DistrictModel(String name, String zipCode, List<PoliceModel> policeModels) {
        this.name = name;
        this.zipCode = zipCode;
        this.policeModels = policeModels;
    }

    @Override
    public String toString() {
        return "DistrictModel{" +
                "name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", policeModels=" + policeModels +
                '}';
    }
}


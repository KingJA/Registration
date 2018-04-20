package com.tdr.registration.model;

import java.util.List;

/**
 * 市
 * Created by Linus_Xie on 2016/9/28.
 */
public class CityModel {
    private String name;
    private String zipCode;
    private List<DistrictModel> districtModels;

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

    public List<DistrictModel> getDistrictModels() {
        return districtModels;
    }

    public void setDistrictModels(List<DistrictModel> districtModels) {
        this.districtModels = districtModels;
    }

    public CityModel() {
    }

    public CityModel(String name, String zipCode, List<DistrictModel> districtModels) {
        this.name = name;
        this.zipCode = zipCode;
        this.districtModels = districtModels;
    }

    @Override
    public String toString() {
        return "CityModel{" +
                "name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", districtModels=" + districtModels +
                '}';
    }
}

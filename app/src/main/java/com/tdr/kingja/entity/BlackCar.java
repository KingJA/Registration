package com.tdr.kingja.entity;

import java.util.List;

/**
 * Description:TODO
 * Create Time:2018/6/19 13:09
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BlackCar {

    /**
     * VehicleBrandName : 奥通
     * PlateNumber : 0969696
     * ColorName : 粉色
     * CreatedOn : null
     * EngineNo : 4254245
     * ShelvesNo : 78787
     * VehicleType : 1
     * OwnerName : 防盗登记1
     * PhotoListFile : [{"Photo":"1fa6f54626f38835041a9ddafb742f1d","PhotoFile":"dd"}]
     */

    private String VehicleBrandName;
    private String PlateNumber;
    private String ColorName;
    private Object CreatedOn;
    private String EngineNo;
    private String ShelvesNo;
    private String VehicleType;
    private String OwnerName;
    private List<PhotoListFileBean> PhotoListFile;

    public String getVehicleBrandName() {
        return VehicleBrandName;
    }

    public void setVehicleBrandName(String VehicleBrandName) {
        this.VehicleBrandName = VehicleBrandName;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String PlateNumber) {
        this.PlateNumber = PlateNumber;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String ColorName) {
        this.ColorName = ColorName;
    }

    public Object getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Object CreatedOn) {
        this.CreatedOn = CreatedOn;
    }

    public String getEngineNo() {
        return EngineNo;
    }

    public void setEngineNo(String EngineNo) {
        this.EngineNo = EngineNo;
    }

    public String getShelvesNo() {
        return ShelvesNo;
    }

    public void setShelvesNo(String ShelvesNo) {
        this.ShelvesNo = ShelvesNo;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String VehicleType) {
        this.VehicleType = VehicleType;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String OwnerName) {
        this.OwnerName = OwnerName;
    }

    public List<PhotoListFileBean> getPhotoListFile() {
        return PhotoListFile;
    }

    public void setPhotoListFile(List<PhotoListFileBean> PhotoListFile) {
        this.PhotoListFile = PhotoListFile;
    }

    public static class PhotoListFileBean {
        /**
         * Photo : 1fa6f54626f38835041a9ddafb742f1d
         * PhotoFile : dd
         */

        private String Photo;
        private String PhotoFile;

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String Photo) {
            this.Photo = Photo;
        }

        public String getPhotoFile() {
            return PhotoFile;
        }

        public void setPhotoFile(String PhotoFile) {
            this.PhotoFile = PhotoFile;
        }
    }
}

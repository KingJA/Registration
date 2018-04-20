package com.tdr.registration.model;

import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;

/**
 * 扣押对象类
 * Created by Linus_Xie on 2016/9/27.
 */
public class SeizeModel {

    /**
     * PHOTO1File :
     * PHOTO2File :
     * DISTRAINNO : SY2345687
     * OWNERNAME : Jacks
     * PHONE : 18365429785
     * IDENTIYCARD : 412326199503125139
     * DECKPLATENUMBER : YC135265
     * VEHICLEBRAND : 艾玛
     * COLORID : 2
     * ENGINENO : *
     * SHELVESNO : *
     * ISTRAFFIC : 1
     * DISTRAINUNIT : 绍兴市公安局
     * DISTRAINUNITID : 绍兴市公安局
     * HANDLEUNITID : 绍兴市公安局
     * DISTRAINTIME : 2016-05-25
     */

    @Column(name ="PHOTO1File")
    private String photo1File;
    @Column(name ="PHOTO2File")
    private String photo2File;
    @Column(name ="DISTRAINNO")
    private String distrainNo;
    @Column(name ="OWNERNAME")
    private String ownerName;
    @Column(name ="PHONE")
    private String phone;
    @Column(name ="IDENTIYCARD")
    private String identityCard;
    @Column(name ="DECKPLATENUMBER")
    private String deckPlateNumber;
    @Column(name ="VEHICLEBRAND")
    private String vehicleBrand;
    @Column(name ="COLORID")
    private String colorId;
    @Column(name ="ENGINENO")
    private String engineNo;
    @Column(name ="SHELVESNO")
    private String shelvesNo;
    @Column(name ="ISTRAFFIC")
    private String isTraffic;// 是否交警扣押，0：否，1：是
    @Column(name ="DISTRAINUNIT")
    private String distrainUnit;
    @Column(name ="DISTRAINUNITID")
    private String distrainUnitId;
    @Column(name ="HANDLEUNITID")
    private String handleUnitId;
    @Column(name ="DISTRAINTIME")
    private String distrainTime;

    public String getPhoto1File() {
        return photo1File;
    }

    public void setPhoto1File(String photo1File) {
        this.photo1File = photo1File;
    }

    public String getPhoto2File() {
        return photo2File;
    }

    public void setPhoto2File(String photo2File) {
        this.photo2File = photo2File;
    }

    public String getDistrainNo() {
        return distrainNo;
    }

    public void setDistrainNo(String distrainNo) {
        this.distrainNo = distrainNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getDeckPlateNumber() {
        return deckPlateNumber;
    }

    public void setDeckPlateNumber(String deckPlateNumber) {
        this.deckPlateNumber = deckPlateNumber;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getShelvesNo() {
        return shelvesNo;
    }

    public void setShelvesNo(String shelvesNo) {
        this.shelvesNo = shelvesNo;
    }

    public String getIsTraffic() {
        return isTraffic;
    }

    public void setIsTraffic(String isTraffic) {
        this.isTraffic = isTraffic;
    }

    public String getDistrainUnit() {
        return distrainUnit;
    }

    public void setDistrainUnit(String distrainUnit) {
        this.distrainUnit = distrainUnit;
    }

    public String getDistrainUnitId() {
        return distrainUnitId;
    }

    public void setDistrainUnitId(String distrainUnitId) {
        this.distrainUnitId = distrainUnitId;
    }

    public String getHandleUnitId() {
        return handleUnitId;
    }

    public void setHandleUnitId(String handleUnitId) {
        this.handleUnitId = handleUnitId;
    }

    public String getDistrainTime() {
        return distrainTime;
    }

    public void setDistrainTime(String distrainTime) {
        this.distrainTime = distrainTime;
    }
}

package com.tdr.registration.model;

import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2016/10/4.
 */
public class DistrainModel implements Serializable {

    /**
     * DistrainNoPredix : KY
     * DistrainNoSuffix : 201610040140531307
     * UNITName : 昆明市
     * VEHICLEBRANDName : 爱尚
     * COLORName : 灰色
     * OPERATORName : 林楠楠1
     * HANDLEUNITName : 昆明市
     * HANDLEUNITIDXQ : 0
     * DISTRAINNO : KY201610040140531307
     * LISTID : B921821B-9722-42FD-87AE-15F173CCB5DC
     * IDENTITYCARD : 330302198602010305
     * OWNERNAME : ceshi
     * PHONE : 18757707988
     * VEHICLEBRAND : 147
     * COLORID : 17
     * ENGINENO : hdhdhd
     * SHELVESNO : ydhdhdjd
     * DECKPLATENUMBER : null
     * DISTRAINUNIT : 市局交警支队一大队三中队
     * DISTRAINUNITID : 12B82978-8C5C-F36F-4E05-0007F01005EB
     * DISTRAINTIME : 2016-10-04 00:00:00
     * OPERATOR : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * OPERATETIME : 2016-10-04 13:40:53
     * UNITID : 37180C76-669F-845A-5E05-36801A8C0935
     * UNITNO : 5301
     * ISTRAFFIC : 1
     * ISBACK : null
     * STATUS : 0
     * REASON : null
     * PLATENUMBER : null
     * THEFTNO : null
     * THEFTNO2 : null
     * RESERVED1 : null
     * RESERVED2 : null
     * REMARK : null
     * HANDLEUNITID : 37180C76-669F-845A-5E05-36801A8C0935
     * PHOTO1 : null
     * PHOTO2 : null
     */
    @Column(name ="DistrainNoPredix")
    @SerializedName("DistrainNoPredix")
    private String DistrainNoPredix;


    @Column(name ="DistrainNoSuffix")
    @SerializedName("DistrainNoSuffix")
    private String DistrainNoSuffix;

    @Column(name ="UNITName")
    @SerializedName("UNITName")
    private String UnitName;

    @Column(name ="VEHICLEBRANDName")
    @SerializedName("VEHICLEBRANDName")
    private String VehicleBrandName;

    @Column(name ="COLORName")
    @SerializedName("COLORName")
    private String ColorName;


    @Column(name ="OPERATORName")
    @SerializedName("OPERATORName")
    private String OpeartorName;

    @Column(name ="HANDLEUNITName")
    @SerializedName("HANDLEUNITName")
    private String HandleUnitName;

    @Column(name ="HANDLEUNITIDXQ")
    @SerializedName("HANDLEUNITIDXQ")
    private String HandleUnitIdXq;

    @Column(name ="DISTRAINNO")
    @SerializedName("DISTRAINNO")
    private String DistrainNo;

    @Column(name ="LISTID",isId = true)
    @SerializedName("LISTID")
    private String ListId;

    @Column(name ="IDENTITYCARD")
    @SerializedName("IDENTITYCARD")
    private String IdentityCard;

    @Column(name ="OWNERNAME")
    @SerializedName("OWNERNAME")
    private String OwnerName;


    @Column(name ="PHONE")
    @SerializedName("PHONE")
    private String Phone;

    @Column(name ="VEHICLEBRAND")
    @SerializedName("VEHICLEBRAND")
    private String VehicleBrand;

    @Column(name ="COLORID")
    @SerializedName("COLORID")
    private String ColorId;

    @Column(name ="ENGINENO")
    @SerializedName("ENGINENO")
    private String EngineNo;

    @Column(name ="SHELVESNO")
    @SerializedName("SHELVESNO")
    private String ShelvesNo;

    @Column(name ="DECKPLATENUMBER")
    @SerializedName("DECKPLATENUMBER")
    private String DeckPlateNumber;

    @Column(name ="DISTRAINUNIT")
    @SerializedName("DISTRAINUNIT")
    private String DistrainUnit;

    @Column(name ="DISTRAINUNITID")
    @SerializedName("DISTRAINUNITID")
    private String DistrainUnitId;

    @Column(name ="DISTRAINTIME")
    @SerializedName("DISTRAINTIME")
    private String DistrainTime;

    @Column(name ="OPERATOR")
    @SerializedName("OPERATOR")
    private String Operator;

    @Column(name ="OPERATETIME")
    @SerializedName("OPERATETIME")
    private String OperatTime;

    @Column(name ="UNITID")
    @SerializedName("UNITID")
    private String UnitId;

    @Column(name ="UNITNO")
    @SerializedName("UNITNO")
    private String UnitNo;

    @Column(name ="ISTRAFFIC")
    @SerializedName("ISTRAFFIC")
    private String IsTraffic;

    @Column(name ="ISBACK")
    @SerializedName("ISBACK")
    private String IsBack;

    @Column(name ="STATUS")
    @SerializedName("STATUS")
    private String Status;

    @Column(name ="REASON")
    @SerializedName("REASON")
    private String Reason;

    @Column(name ="PLATENUMBER")
    @SerializedName("PLATENUMBER")
    private String PlateNumber;

    @Column(name ="THEFTNO")
    @SerializedName("THEFTNO")
    private String TheftNo;

    @Column(name ="THEFTNO2")
    @SerializedName("THEFTNO2")
    private String TheftNo2;

    @Column(name ="RESERVED1")
    @SerializedName("RESERVED1")
    private String Reserved1;

    @Column(name ="RESERVED2")
    @SerializedName("RESERVED2")
    private String Reserved2;

    @Column(name ="REMARK")
    @SerializedName("REMARK")
    private String Remark;

    @Column(name ="HANDLEUNITID")
    @SerializedName("HANDLEUNITID")
    private String HandleUnitId;

    @Column(name ="PHOTO1")
    @SerializedName("PHOTO1")
    private String Photo1;

    @Column(name ="PHOTO2")
    @SerializedName("PHOTO2")
    private String Photo2;

    public String getDistrainNoPredix() {
        return DistrainNoPredix;
    }

    public void setDistrainNoPredix(String DistrainNoPredix) {
        this.DistrainNoPredix = DistrainNoPredix;
    }

    public String getDistrainNoSuffix() {
        return DistrainNoSuffix;
    }

    public void setDistrainNoSuffix(String DistrainNoSuffix) {
        this.DistrainNoSuffix = DistrainNoSuffix;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getVehicleBrandName() {
        return VehicleBrandName;
    }

    public void setVehicleBrandName(String VehicleBrandName) {
        this.VehicleBrandName = VehicleBrandName;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String ColorName) {
        this.ColorName = ColorName;
    }

    public String getOpeartorName() {
        return OpeartorName;
    }

    public void setOpeartorName(String OpeartorName) {
        this.OpeartorName = OpeartorName;
    }

    public String getHandleUnitName() {
        return HandleUnitName;
    }

    public void setHandleUnitName(String HandleUnitName) {
        this.HandleUnitName = HandleUnitName;
    }

    public String getHandleUnitIdXq() {
        return HandleUnitIdXq;
    }

    public void setHandleUnitIdXq(String HandleUnitIdXq) {
        this.HandleUnitIdXq = HandleUnitIdXq;
    }

    public String getDistrainNo() {
        return DistrainNo;
    }

    public void setDistrainNo(String DistrainNo) {
        this.DistrainNo = DistrainNo;
    }

    public String getListId() {
        return ListId;
    }

    public void setListId(String ListId) {
        this.ListId = ListId;
    }

    public String getIdentityCard() {
        return IdentityCard;
    }

    public void setIdentityCard(String IdentityCard) {
        this.IdentityCard = IdentityCard;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String OwnerName) {
        this.OwnerName = OwnerName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getVehicleBrand() {
        return VehicleBrand;
    }

    public void setVehicleBrand(String VehicleBrand) {
        this.VehicleBrand = VehicleBrand;
    }

    public String getColorId() {
        return ColorId;
    }

    public void setColorId(String ColorId) {
        this.ColorId = ColorId;
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

    public String getDeckPlateNumber() {
        return DeckPlateNumber;
    }

    public void setDeckPlateNumber(String DeckPlateNumber) {
        this.DeckPlateNumber = DeckPlateNumber;
    }

    public String getDistrainUnit() {
        return DistrainUnit;
    }

    public void setDistrainUnit(String DistrainUnit) {
        this.DistrainUnit = DistrainUnit;
    }

    public String getDistrainUnitId() {
        return DistrainUnitId;
    }

    public void setDistrainUnitId(String DistrainUnitId) {
        this.DistrainUnitId = DistrainUnitId;
    }

    public String getDistrainTime() {
        return DistrainTime;
    }

    public void setDistrainTime(String DistrainTime) {
        this.DistrainTime = DistrainTime;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String Operator) {
        this.Operator = Operator;
    }

    public String getOperatTime() {
        return OperatTime;
    }

    public void setOperatTime(String OperatTime) {
        this.OperatTime = OperatTime;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String UnitNo) {
        this.UnitNo = UnitNo;
    }

    public String getIsTraffic() {
        return IsTraffic;
    }

    public void setIsTraffic(String IsTraffic) {
        this.IsTraffic = IsTraffic;
    }

    public String getIsBack() {
        return IsBack;
    }

    public void setIsBack(String IsBack) {
        this.IsBack = IsBack;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String PlateNumber) {
        this.PlateNumber = PlateNumber;
    }

    public String getTheftNo() {
        return TheftNo;
    }

    public void setTheftNo(String TheftNo) {
        this.TheftNo = TheftNo;
    }

    public String getTheftNo2() {
        return TheftNo2;
    }

    public void setTheftNo2(String TheftNo2) {
        this.TheftNo2 = TheftNo2;
    }

    public String getReserved1() {
        return Reserved1;
    }

    public void setReserved1(String Reserved1) {
        this.Reserved1 = Reserved1;
    }

    public String getReserved2() {
        return Reserved2;
    }

    public void setReserved2(String Reserved2) {
        this.Reserved2 = Reserved2;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getHandleUnitId() {
        return HandleUnitId;
    }

    public void setHandleUnitId(String HandleUnitId) {
        this.HandleUnitId = HandleUnitId;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String Photo1) {
        this.Photo1 = Photo1;
    }

    public String getPhoto2() {
        return Photo2;
    }

    public void setPhoto2(String Photo2) {
        this.Photo2 = Photo2;
    }
}

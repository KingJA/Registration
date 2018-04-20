package com.tdr.registration.model;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * 电动车登记对象
 * Created by Linus_Xie on 2016/9/22.
 */
@Table(name = "electricCarModel_db")
public class ElectricCarModel implements Serializable {


    /**
     * SourceName : 未知
     * UnitName : 昆明市
     * Operator : null
     * Operator2 : null
     * AlertUserName : null
     * AlertSellerName : null
     * IsStolenCar : false
     * CreatedUserName : 林楠楠1
     * UpdatedUserName : 林楠楠1
     * Login : admin
     * CanPolicyEdit : false
     * VehicleBrandName : 壮华
     * ColorName : 棕色
     * ColorName2 : null
     * PhotoListFile : []
     * ISTEMPRECORD : 0
     * EcId : ABDA1055-6B0F-4EC1-8E23-63A77398A24E
     * RegisterType : 1
     * VehicleBrand : 1267
     * VehicleType : 1
     * PlateNumber : XS455778
     * VehicleModels : 123123
     * ColorId : 7
     * EngineNo : 77
     * ShelvesNo : 777
     * BuyDate : 2016-10-04 00:00:00
     * Price : null
     * Photo1 : 323E42CA-D233-46AE-AE6C-CAF7C0A8004D
     * Photo2 : 91B2D394-FD3D-4E45-9BA2-FE3B3115D720
     * Photo3 : E2592AFD-DA1C-4334-B6B1-A867E01F078C
     * Photo4 : 409D7CCE-89E0-421E-B45A-C6CD95B392A0
     * PhotoList : null
     * OwnerName : 高
     * CardId : 420115199306206650
     * Address : null
     * ResidentAddress : 温州
     * Phone1 : 13163240531
     * Phone2 : null
     * Mobile : null
     * TRANSACTMAN : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * UnitID : 37180C76-669F-845A-5E05-36801A8C0935
     * UnitNo : 5301
     * OPERATORTIME : 2016-10-04 15:12:49
     * ALTERTIME : 2016-10-04 20:51:07
     * Remark : null
     * VEHID : null
     * CARTYPE : 1
     * ISCONFIRM : 0
     * ISSCRAP : 0
     * SCRAPTIME : null
     * SCRAPMAN : null
     * SCRAPREMARK : null
     * Source : 0
     * DataIP : ::1
     * RESERVE1 : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * RESERVE2 : null
     * RESERVE3 : null
     * THEFTNO : 1243532821
     * THEFTNO2 : 3552024716
     * FIRSTECID : ABDA1055-6B0F-4EC1-8E23-63A77398A24E
     * CHANGESTATE : 0
     * TRANSFERSTATE : 0
     * LOCKSTATE : 1
     * RESERVED4 : null
     * RESERVED5 : null
     * ISINVALID : 0
     * ICNO : null
     * POLICYS : [{"POLICYID":"886B6273-972C-42ED-B556-A3F6F8AAAB2F","POLICY":"","TYPE":2,"POLICYDATE":null,"STARTDATE":"2016-10-05 00:00:00","STARTHOUR":"0","ENDDATE":"2017-10-05 00:00:00","ENDHOUR":"0","ISSCRAP":null,"SCRAPTIME":"0001-01-01 00:00:00","SCRAPMAN":null,"SCRAPREMARK":null,"ISPRString":0,"DATAIP":null,"TRANSACTMAN":null,"UNITID":null,"INSUREDTYPE":0,"INSUREDNATURE":0,"INSUREDFLAG":0,"INSURED":"高","IDENTITYCARD":"420115199306206650","PHONE":"13163240531","BEINSURED":null,"IDENTITYCARD2":null,"PHONE2":null,"OWNERNAME":null,"CARDID":null,"VEHICLEBRAND":null,"COLORID":null,"ENGINENO":null,"SHELVESNO":null,"BUYDATE":null,"THEFTNO":null,"THEFTNO2":null,"INTIME":"0001-01-01 00:00:00","RESERVE1":null,"RESERVE2":null,"ECID":null,"PRICE":null,"DEADLINE":1,"TRANSACTNAME":null,"UNITNAME":null,"INSURERTYPE":0,"REMARKID":"0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0","CANCEL":0,"TypeName":"防盗备案装置费"},{"POLICYID":"76F4E923-739D-4E1F-A6B4-AAE1768AADFE","POLICY":"","TYPE":4,"POLICYDATE":null,"STARTDATE":"2016-10-05 00:00:00","STARTHOUR":"0","ENDDATE":"2018-10-05 00:00:00","ENDHOUR":"0","ISSCRAP":null,"SCRAPTIME":"0001-01-01 00:00:00","SCRAPMAN":null,"SCRAPREMARK":null,"ISPRINT":0,"DATAIP":null,"TRANSACTMAN":null,"UNITID":null,"INSUREDTYPE":0,"INSUREDNATURE":0,"INSUREDFLAG":0,"INSURED":"高","IDENTITYCARD":"420115199306206650","PHONE":"13163240531","BEINSURED":null,"IDENTITYCARD2":null,"PHONE2":null,"OWNERNAME":null,"CARDID":null,"VEHICLEBRAND":null,"COLORID":null,"ENGINENO":null,"SHELVESNO":null,"BUYDATE":null,"THEFTNO":null,"THEFTNO2":null,"INTIME":"0001-01-01 00:00:00","RESERVE1":null,"RESERVE2":null,"ECID":null,"PRICE":null,"DEADLINE":2,"TRANSACTNAME":null,"UNITNAME":null,"INSURERTYPE":0,"REMARKID":null,"CANCEL":0,"TypeName":null}]
     * INSURERTYPE : null
     * IsDelete : 0
     * HasRFID : 1
     * CreateUserId : null
     * CreatedOn : 2016/10/4 15:12:49
     * CARDTYPE : 1
     * ColorId2 : null
     * REGISTERID : null
     * INVALID_REASON : null
     * INVALIDMAN : null
     * INVALIDTIME : 0001-01-01 00:00:00
     * Install_Price : null
     */
    @Column(name = "EcId", isId = true)
    private String SourceName;
    private String UnitName;
    private String Operator;
    private String Operator2;
    private String AlertUserName;
    private String AlertSellerName;
    private boolean IsStolenCar;
    private String CreatedUserName;
    private String UpdatedUserName;
    private String Login;
    private boolean CanPolicyEdit;
    private String VehicleBrandName;
    private String ColorName;
    private String ColorName2;
    private String ISTEMPRECORD;
    private String EcId;
    private String RegisterType;
    private String VehicleBrand;
    private String VehicleType;
    private String PlateNumber;
    private String VehicleModels;
    private String ColorId;
    private String EngineNo;
    private String ShelvesNo;
    private String BuyDate;
    private String Price;
    private String Photo1;
    private String Photo2;
    private String Photo3;
    private String Photo4;
    private String PhotoList;
    private String OwnerName;
    private String CardId;
    private String Address;
    private String ResidentAddress;
    private String Phone1;
    private String Phone2;
    private String Mobile;
    private String TRANSACTMAN;
    private String UnitID;
    private String UnitNo;
    private String OPERATORTIME;
    private String ALTERTIME;
    private String Remark;
    private String VEHID;
    private String CARTYPE;
    private String PLATETYPE;
    private String ISCONFIRM;
    private String ISSCRAP;
    private String SCRAPTIME;
    private String SCRAPMAN;
    private String SCRAPREMARK;
    private String Source;
    private String DataIP;
    private String RESERVE1;
    private String RESERVE2;
    private String RESERVE3;
    private String THEFTNO;
    private String THEFTNO2;
    private String FIRSTECID;
    private String CHANGESTATE;
    private String TRANSFERSTATE;
    private String LOCKSTATE;
    private String RESERVED4;
    private String RESERVED5;
    private String ISINVALID;
    private String ICNO;
    private String INSURERTYPE;
    private String IsDelete;
    private String HasRFID;
    private String CreateUserId;
    private String CreatedOn;
    private String CARDTYPE;
    private String ColorId2;
    private String REGISTERID;
    private String INVALID_REASON;
    private String INVALIDMAN;
    private String INVALIDTIME;
    private String Install_Price;
    private List<PhotoModel> PhotoListFile;



    /**
     * POLICYID : 886B6273-972C-42ED-B556-A3F6F8AAAB2F
     * POLICY :
     * TYPE : 2
     * POLICYDATE : null
     * STARTDATE : 2016-10-05 00:00:00
     * STARTHOUR : 0
     * ENDDATE : 2017-10-05 00:00:00
     * ENDHOUR : 0
     * ISSCRAP : null
     * SCRAPTIME : 0001-01-01 00:00:00
     * SCRAPMAN : null
     * SCRAPREMARK : null
     * ISPRINT : 0
     * DATAIP : null
     * TRANSACTMAN : null
     * UNITID : null
     * INSUREDTYPE : 0
     * INSUREDNATURE : 0
     * INSUREDFLAG : 0
     * INSURED : 高
     * IDENTITYCARD : 420115199306206650
     * PHONE : 13163240531
     * BEINSURED : null
     * IDENTITYCARD2 : null
     * PHONE2 : null
     * OWNERNAME : null
     * CARDID : null
     * VEHICLEBRAND : null
     * COLORID : null
     * ENGINENO : null
     * SHELVESNO : null
     * BUYDATE : null
     * THEFTNO : null
     * THEFTNO2 : null
     * INTIME : 0001-01-01 00:00:00
     * RESERVE1 : null
     * RESERVE2 : null
     * ECID : null
     * PRICE : null
     * DEADLINE : 1
     * TRANSACTNAME : null
     * UNITNAME : null
     * INSURERTYPE : 0
     * REMARKID : 0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0
     * CANCEL : 0
     * TypeName : 防盗备案装置费
     */

    private List<PolicysBean> POLICYS;

    public String getSourceName() {
        return SourceName;
    }

    public void setSourceName(String SourceName) {
        this.SourceName = SourceName;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String Operator) {
        this.Operator = Operator;
    }

    public String getOperator2() {
        return Operator2;
    }

    public void setOperator2(String Operator2) {
        this.Operator2 = Operator2;
    }

    public String getAlertUserName() {
        return AlertUserName;
    }

    public void setAlertUserName(String AlertUserName) {
        this.AlertUserName = AlertUserName;
    }

    public String getAlertSellerName() {
        return AlertSellerName;
    }

    public void setAlertSellerName(String AlertSellerName) {
        this.AlertSellerName = AlertSellerName;
    }

    public boolean isIsStolenCar() {
        return IsStolenCar;
    }

    public void setIsStolenCar(boolean IsStolenCar) {
        this.IsStolenCar = IsStolenCar;
    }

    public String getCreatedUserName() {
        return CreatedUserName;
    }

    public void setCreatedUserName(String CreatedUserName) {
        this.CreatedUserName = CreatedUserName;
    }

    public String getUpdatedUserName() {
        return UpdatedUserName;
    }

    public void setUpdatedUserName(String UpdatedUserName) {
        this.UpdatedUserName = UpdatedUserName;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public boolean isCanPolicyEdit() {
        return CanPolicyEdit;
    }

    public void setCanPolicyEdit(boolean CanPolicyEdit) {
        this.CanPolicyEdit = CanPolicyEdit;
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

    public String getColorName2() {
        return ColorName2;
    }

    public void setColorName2(String ColorName2) {
        this.ColorName2 = ColorName2;
    }

    public String getISTEMPRECORD() {
        return ISTEMPRECORD;
    }

    public void setISTEMPRECORD(String ISTEMPRECORD) {
        this.ISTEMPRECORD = ISTEMPRECORD;
    }

    public String getEcId() {
        return EcId;
    }

    public void setEcId(String EcId) {
        this.EcId = EcId;
    }

    public String getRegisterType() {
        return RegisterType;
    }

    public void setRegisterType(String RegisterType) {
        this.RegisterType = RegisterType;
    }

    public String getVehicleBrand() {
        return VehicleBrand;
    }

    public void setVehicleBrand(String VehicleBrand) {
        this.VehicleBrand = VehicleBrand;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String VehicleType) {
        this.VehicleType = VehicleType;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String PlateNumber) {
        this.PlateNumber = PlateNumber;
    }

    public String getVehicleModels() {
        return VehicleModels;
    }

    public void setVehicleModels(String VehicleModels) {
        this.VehicleModels = VehicleModels;
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

    public String getBuyDate() {
        return BuyDate;
    }

    public void setBuyDate(String BuyDate) {
        this.BuyDate = BuyDate;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
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

    public String getPhoto3() {
        return Photo3;
    }

    public void setPhoto3(String Photo3) {
        this.Photo3 = Photo3;
    }

    public String getPhoto4() {
        return Photo4;
    }

    public void setPhoto4(String Photo4) {
        this.Photo4 = Photo4;
    }

    public String getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(String PhotoList) {
        this.PhotoList = PhotoList;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String OwnerName) {
        this.OwnerName = OwnerName;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String CardId) {
        this.CardId = CardId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getResidentAddress() {
        return ResidentAddress;
    }

    public void setResidentAddress(String ResidentAddress) {
        this.ResidentAddress = ResidentAddress;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String Phone1) {
        this.Phone1 = Phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String Phone2) {
        this.Phone2 = Phone2;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getTRANSACTMAN() {
        return TRANSACTMAN;
    }

    public void setTRANSACTMAN(String TRANSACTMAN) {
        this.TRANSACTMAN = TRANSACTMAN;
    }

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String UnitID) {
        this.UnitID = UnitID;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String UnitNo) {
        this.UnitNo = UnitNo;
    }

    public String getOPERATORTIME() {
        return OPERATORTIME;
    }

    public void setOPERATORTIME(String OPERATORTIME) {
        this.OPERATORTIME = OPERATORTIME;
    }

    public String getALTERTIME() {
        return ALTERTIME;
    }

    public void setALTERTIME(String ALTERTIME) {
        this.ALTERTIME = ALTERTIME;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getVEHID() {
        return VEHID;
    }

    public void setVEHID(String VEHID) {
        this.VEHID = VEHID;
    }

    public String getCARTYPE() {
        return CARTYPE;
    }

    public void setCARTYPE(String CARTYPE) {
        this.CARTYPE = CARTYPE;
    }

    public String getISCONFIRM() {
        return ISCONFIRM;
    }

    public void setISCONFIRM(String ISCONFIRM) {
        this.ISCONFIRM = ISCONFIRM;
    }

    public String getISSCRAP() {
        return ISSCRAP;
    }

    public void setISSCRAP(String ISSCRAP) {
        this.ISSCRAP = ISSCRAP;
    }

    public String getSCRAPTIME() {
        return SCRAPTIME;
    }

    public void setSCRAPTIME(String SCRAPTIME) {
        this.SCRAPTIME = SCRAPTIME;
    }

    public String getSCRAPMAN() {
        return SCRAPMAN;
    }

    public void setSCRAPMAN(String SCRAPMAN) {
        this.SCRAPMAN = SCRAPMAN;
    }

    public String getSCRAPREMARK() {
        return SCRAPREMARK;
    }

    public void setSCRAPREMARK(String SCRAPREMARK) {
        this.SCRAPREMARK = SCRAPREMARK;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String Source) {
        this.Source = Source;
    }

    public String getDataIP() {
        return DataIP;
    }

    public void setDataIP(String DataIP) {
        this.DataIP = DataIP;
    }

    public String getRESERVE1() {
        return RESERVE1;
    }

    public void setRESERVE1(String RESERVE1) {
        this.RESERVE1 = RESERVE1;
    }

    public String getRESERVE2() {
        return RESERVE2;
    }

    public void setRESERVE2(String RESERVE2) {
        this.RESERVE2 = RESERVE2;
    }

    public String getRESERVE3() {
        return RESERVE3;
    }

    public void setRESERVE3(String RESERVE3) {
        this.RESERVE3 = RESERVE3;
    }


    public String getTHEFTNO() {
        return THEFTNO;
    }

    public void setTHEFTNO(String THEFTNO) {
        this.THEFTNO = THEFTNO;
    }

    public String getTHEFTNO2() {
        return THEFTNO2;
    }

    public void setTHEFTNO2(String THEFTNO2) {
        this.THEFTNO2 = THEFTNO2;
    }

    public String getFIRSTECID() {
        return FIRSTECID;
    }

    public void setFIRSTECID(String FIRSTECID) {
        this.FIRSTECID = FIRSTECID;
    }

    public String getCHANGESTATE() {
        return CHANGESTATE;
    }

    public void setCHANGESTATE(String CHANGESTATE) {
        this.CHANGESTATE = CHANGESTATE;
    }

    public String getTRANSFERSTATE() {
        return TRANSFERSTATE;
    }

    public void setTRANSFERSTATE(String TRANSFERSTATE) {
        this.TRANSFERSTATE = TRANSFERSTATE;
    }

    public String getLOCKSTATE() {
        return LOCKSTATE;
    }

    public void setLOCKSTATE(String LOCKSTATE) {
        this.LOCKSTATE = LOCKSTATE;
    }

    public String getRESERVED4() {
        return RESERVED4;
    }

    public void setRESERVED4(String RESERVED4) {
        this.RESERVED4 = RESERVED4;
    }

    public String getRESERVED5() {
        return RESERVED5;
    }

    public void setRESERVED5(String RESERVED5) {
        this.RESERVED5 = RESERVED5;
    }

    public String getISINVALID() {
        return ISINVALID;
    }

    public void setISINVALID(String ISINVALID) {
        this.ISINVALID = ISINVALID;
    }

    public String getICNO() {
        return ICNO;
    }

    public void setICNO(String ICNO) {
        this.ICNO = ICNO;
    }

    public String getINSURERTYPE() {
        return INSURERTYPE;
    }

    public void setINSURERTYPE(String INSURERTYPE) {
        this.INSURERTYPE = INSURERTYPE;
    }

    public String getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(String IsDelete) {
        this.IsDelete = IsDelete;
    }

    public String getHasRFID() {
        return HasRFID;
    }

    public void setHasRFID(String HasRFID) {
        this.HasRFID = HasRFID;
    }

    public String getCreateUserId() {
        return CreateUserId;
    }

    public void setCreateUserId(String CreateUserId) {
        this.CreateUserId = CreateUserId;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String CreatedOn) {
        this.CreatedOn = CreatedOn;
    }

    public String getCARDTYPE() {
        return CARDTYPE;
    }

    public void setCARDTYPE(String CARDTYPE) {
        this.CARDTYPE = CARDTYPE;
    }

    public String getColorId2() {
        return ColorId2;
    }

    public void setColorId2(String ColorId2) {
        this.ColorId2 = ColorId2;
    }

    public String getREGISTERID() {
        return REGISTERID;
    }

    public void setREGISTERID(String REGISTERID) {
        this.REGISTERID = REGISTERID;
    }

    public String getINVALID_REASON() {
        return INVALID_REASON;
    }

    public void setINVALID_REASON(String INVALID_REASON) {
        this.INVALID_REASON = INVALID_REASON;
    }

    public String getINVALIDMAN() {
        return INVALIDMAN;
    }

    public void setINVALIDMAN(String INVALIDMAN) {
        this.INVALIDMAN = INVALIDMAN;
    }

    public String getINVALIDTIME() {
        return INVALIDTIME;
    }

    public void setINVALIDTIME(String INVALIDTIME) {
        this.INVALIDTIME = INVALIDTIME;
    }

    public String getInstall_Price() {
        return Install_Price;
    }

    public void setInstall_Price(String Install_Price) {
        this.Install_Price = Install_Price;
    }

    public List<PhotoModel> getPhotoListFile() {
        return PhotoListFile;
    }

    public void setPhotoListFile(List<PhotoModel> PhotoListFile) {
        this.PhotoListFile = PhotoListFile;
    }

    public String getPLATETYPE() {
        return PLATETYPE;
    }

    public void setPLATETYPE(String PLATETYPE) {
        this.PLATETYPE = PLATETYPE;
    }

    public List<PolicysBean> getPOLICYS() {
        return POLICYS;
    }

    public void setPOLICYS(List<PolicysBean> POLICYS) {
        this.POLICYS = POLICYS;
    }

    public static class PolicysBean implements Serializable {
//        private String POLICYID;
//        private String POLICY;
//        private String TYPE;
//        private String POLICYDATE;
//        private String STARTDATE;
//        private String STARTHOUR;
//        private String ENDDATE;
//        private String ENDHOUR;
//        private String ISSCRAP;
//        private String SCRAPTIME;
//        private String SCRAPMAN;
//        private String SCRAPREMARK;
//        private String ISPRINT;
//        private String DATAIP;
//        private String TRANSACTMAN;
//        private String UNITID;
//        private String INSUREDTYPE;
//        private String INSUREDNATURE;
//        private String INSUREDFLAG;
//        private String INSURED;
//        private String IDENTITYCARD;
//        private String PHONE;
//        private String BEINSURED;
//        private String IDENTITYCARD2;
//        private String PHONE2;
//        private String OWNERNAME;
//        private String CARDID;
//        private String VEHICLEBRAND;
//        private String COLORID;
//        private String ENGINENO;
//        private String SHELVESNO;
//        private String BUYDATE;
//        private String THEFTNO;
//        private String THEFTNO2;
//        private String INTIME;
//        private String RESERVE1;
//        private String RESERVE2;
//        private String ECID;
//        private String PRICE;
//        private String DEADLINE;
//        private String TRANSACTNAME;
//        private String UNITNAME;
//        private String INSURERTYPE;
//        private String REMARKID;
//        private String CANCEL;
//        private String TypeName;

        private String POLICYID;
        private String POLICY;
        private String TYPE;
        private String POLICYDATE;
        private String STARTDATE;
        private String STARTHOUR;
        private String ENDDATE;
        private String ENDHOUR;
        private String ISSCRAP;
        private String SCRAPTIME;
        private String SCRAPMAN;
        private String SCRAPREMARK;
        private String ISPRINT;
        private String DATAIP;
        private String TRANSACTMAN;
        private String UNITID;
        private String INSUREDTYPE;
        private String INSUREDNATURE;
        private String INSUREDFLAG;
        private String INSURED;
        private String IDENTITYCARD;
        private String PHONE;
        private String BEINSURED;
        private String IDENTITYCARD2;
        private String PHONE2;
        private String OWNERNAME;
        private String CARDID;
        private String VEHICLEBRAND;
        private String COLORID;
        private String ENGINENO;
        private String SHELVESNO;
        private String BUYDATE;
        private String THEFTNO;
        private String THEFTNO2;
        private String INTIME;
        private String RESERVE1;
        private String RESERVE2;
        private String ECID;
        private String PRICE;
        private String DEADLINE;
        private String TRANSACTNAME;
        private String UNITNAME;
        private String INSURERTYPE;
        private String CID;
        private String REMARKID;
        private String CANCEL;
        private String STATE;
        private String TypeName;
        private String SubTitle;
        private String ISVALID;
        private String ISBUY;

        public String getCID() {
            return CID;
        }

        public void setCID(String CID) {
            this.CID = CID;
        }

        public String getSTATE() {
            return STATE;
        }

        public void setSTATE(String STATE) {
            this.STATE = STATE;
        }

        public String getSubTitle() {
            return SubTitle;
        }

        public void setSubTitle(String subTitle) {
            SubTitle = subTitle;
        }

        public String getISVALID() {
            return ISVALID;
        }

        public void setISVALID(String ISVALID) {
            this.ISVALID = ISVALID;
        }

        public String getISBUY() {
            return ISBUY;
        }

        public void setISBUY(String ISBUY) {
            this.ISBUY = ISBUY;
        }

        public String getPOLICYID() {
            return POLICYID;
        }

        public void setPOLICYID(String POLICYID) {
            this.POLICYID = POLICYID;
        }

        public String getPOLICY() {
            return POLICY;
        }

        public void setPOLICY(String POLICY) {
            this.POLICY = POLICY;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getPOLICYDATE() {
            return POLICYDATE;
        }

        public void setPOLICYDATE(String POLICYDATE) {
            this.POLICYDATE = POLICYDATE;
        }

        public String getSTARTDATE() {
            return STARTDATE;
        }

        public void setSTARTDATE(String STARTDATE) {
            this.STARTDATE = STARTDATE;
        }

        public String getSTARTHOUR() {
            return STARTHOUR;
        }

        public void setSTARTHOUR(String STARTHOUR) {
            this.STARTHOUR = STARTHOUR;
        }

        public String getENDDATE() {
            return ENDDATE;
        }

        public void setENDDATE(String ENDDATE) {
            this.ENDDATE = ENDDATE;
        }

        public String getENDHOUR() {
            return ENDHOUR;
        }

        public void setENDHOUR(String ENDHOUR) {
            this.ENDHOUR = ENDHOUR;
        }

        public String getISSCRAP() {
            return ISSCRAP;
        }

        public void setISSCRAP(String ISSCRAP) {
            this.ISSCRAP = ISSCRAP;
        }

        public String getSCRAPTIME() {
            return SCRAPTIME;
        }

        public void setSCRAPTIME(String SCRAPTIME) {
            this.SCRAPTIME = SCRAPTIME;
        }

        public String getSCRAPMAN() {
            return SCRAPMAN;
        }

        public void setSCRAPMAN(String SCRAPMAN) {
            this.SCRAPMAN = SCRAPMAN;
        }

        public String getSCRAPREMARK() {
            return SCRAPREMARK;
        }

        public void setSCRAPREMARK(String SCRAPREMARK) {
            this.SCRAPREMARK = SCRAPREMARK;
        }

        public String getISPRINT() {
            return ISPRINT;
        }

        public void setISPRINT(String ISPRINT) {
            this.ISPRINT = ISPRINT;
        }

        public String getDATAIP() {
            return DATAIP;
        }

        public void setDATAIP(String DATAIP) {
            this.DATAIP = DATAIP;
        }

        public String getTRANSACTMAN() {
            return TRANSACTMAN;
        }

        public void setTRANSACTMAN(String TRANSACTMAN) {
            this.TRANSACTMAN = TRANSACTMAN;
        }

        public String getUNITID() {
            return UNITID;
        }

        public void setUNITID(String UNITID) {
            this.UNITID = UNITID;
        }

        public String getINSUREDTYPE() {
            return INSUREDTYPE;
        }

        public void setINSUREDTYPE(String INSUREDTYPE) {
            this.INSUREDTYPE = INSUREDTYPE;
        }

        public String getINSUREDNATURE() {
            return INSUREDNATURE;
        }

        public void setINSUREDNATURE(String INSUREDNATURE) {
            this.INSUREDNATURE = INSUREDNATURE;
        }

        public String getINSUREDFLAG() {
            return INSUREDFLAG;
        }

        public void setINSUREDFLAG(String INSUREDFLAG) {
            this.INSUREDFLAG = INSUREDFLAG;
        }

        public String getINSURED() {
            return INSURED;
        }

        public void setINSURED(String INSURED) {
            this.INSURED = INSURED;
        }

        public String getIDENTITYCARD() {
            return IDENTITYCARD;
        }

        public void setIDENTITYCARD(String IDENTITYCARD) {
            this.IDENTITYCARD = IDENTITYCARD;
        }

        public String getPHONE() {
            return PHONE;
        }

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public String getBEINSURED() {
            return BEINSURED;
        }

        public void setBEINSURED(String BEINSURED) {
            this.BEINSURED = BEINSURED;
        }

        public String getIDENTITYCARD2() {
            return IDENTITYCARD2;
        }

        public void setIDENTITYCARD2(String IDENTITYCARD2) {
            this.IDENTITYCARD2 = IDENTITYCARD2;
        }

        public String getPHONE2() {
            return PHONE2;
        }

        public void setPHONE2(String PHONE2) {
            this.PHONE2 = PHONE2;
        }

        public String getOWNERNAME() {
            return OWNERNAME;
        }

        public void setOWNERNAME(String OWNERNAME) {
            this.OWNERNAME = OWNERNAME;
        }

        public String getCARDID() {
            return CARDID;
        }

        public void setCARDID(String CARDID) {
            this.CARDID = CARDID;
        }

        public String getVEHICLEBRAND() {
            return VEHICLEBRAND;
        }

        public void setVEHICLEBRAND(String VEHICLEBRAND) {
            this.VEHICLEBRAND = VEHICLEBRAND;
        }

        public String getCOLORID() {
            return COLORID;
        }

        public void setCOLORID(String COLORID) {
            this.COLORID = COLORID;
        }

        public String getENGINENO() {
            return ENGINENO;
        }

        public void setENGINENO(String ENGINENO) {
            this.ENGINENO = ENGINENO;
        }

        public String getSHELVESNO() {
            return SHELVESNO;
        }

        public void setSHELVESNO(String SHELVESNO) {
            this.SHELVESNO = SHELVESNO;
        }

        public String getBUYDATE() {
            return BUYDATE;
        }

        public void setBUYDATE(String BUYDATE) {
            this.BUYDATE = BUYDATE;
        }

        public String getTHEFTNO() {
            return THEFTNO;
        }

        public void setTHEFTNO(String THEFTNO) {
            this.THEFTNO = THEFTNO;
        }

        public String getTHEFTNO2() {
            return THEFTNO2;
        }

        public void setTHEFTNO2(String THEFTNO2) {
            this.THEFTNO2 = THEFTNO2;
        }

        public String getINTIME() {
            return INTIME;
        }

        public void setINTIME(String INTIME) {
            this.INTIME = INTIME;
        }

        public String getRESERVE1() {
            return RESERVE1;
        }

        public void setRESERVE1(String RESERVE1) {
            this.RESERVE1 = RESERVE1;
        }

        public String getRESERVE2() {
            return RESERVE2;
        }

        public void setRESERVE2(String RESERVE2) {
            this.RESERVE2 = RESERVE2;
        }

        public String getECID() {
            return ECID;
        }

        public void setECID(String ECID) {
            this.ECID = ECID;
        }

        public String getPRICE() {
            return PRICE;
        }

        public void setPRICE(String PRICE) {
            this.PRICE = PRICE;
        }

        public String getDEADLINE() {
            return DEADLINE;
        }

        public void setDEADLINE(String DEADLINE) {
            this.DEADLINE = DEADLINE;
        }

        public String getTRANSACTNAME() {
            return TRANSACTNAME;
        }

        public void setTRANSACTNAME(String TRANSACTNAME) {
            this.TRANSACTNAME = TRANSACTNAME;
        }

        public String getUNITNAME() {
            return UNITNAME;
        }

        public void setUNITNAME(String UNITNAME) {
            this.UNITNAME = UNITNAME;
        }

        public String getINSURERTYPE() {
            return INSURERTYPE;
        }

        public void setINSURERTYPE(String INSURERTYPE) {
            this.INSURERTYPE = INSURERTYPE;
        }

        public String getREMARKID() {
            return REMARKID;
        }

        public void setREMARKID(String REMARKID) {
            this.REMARKID = REMARKID;
        }

        public String getCANCEL() {
            return CANCEL;
        }

        public void setCANCEL(String CANCEL) {
            this.CANCEL = CANCEL;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }

    }

}

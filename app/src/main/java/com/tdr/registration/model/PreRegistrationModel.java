package com.tdr.registration.model;

import java.io.Serializable;
import java.util.List;

/**
 * 龙岩预登记对象
 * Created by linus_Xie on 2016/10/28.
 */

public class PreRegistrationModel implements Serializable{

    /**
     * BeautifiedREGISTERID : 1000 0099
     * VEHICLEBRANDName : 宝马
     * ColorName : 紫色
     * InsertUserName : null
     * UNITName : null
     * AlertUserName : null
     * _ShowAll : true
     * REGISTERID : 10000099
     * VEHICLEBRAND : 1942
     * VEHICLETYPE : 1
     * VEHICLEMODELS : 1
     * COLORID : 19
     * ENGINENO : 123456
     * SHELVESNO : 123456
     * BUYDATE : 2016-10-28 00:00:00
     * PRICE : null
     * OWNERNAME : cesi
     * CARDID : 330302198602010305
     * PHONE1 : 18757707988
     * PHONE2 : null
     * INSURANCETYPE : null
     * CREATED_USERID : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * UNITID : 37180C76-669F-845A-5E05-36801A8C0935
     * UNITNO : 5301
     * OPERATORTIME : 2016-10-28 17:31:27
     * SOURCE : 0
     * DATAIP : 122.228.188.210
     * ISVALID : 1
     * ISREGISTER : 0
     * PLATENUMBER : 0123456
     * MODIFIER : null
     * MODIFIEDTIME : null
     * REMARK : null
     * REVERSED1 : null
     * REVERSED2 : null
     * REVERSED3 : null
     * CARDTYPE : 1
     * CARDADDRESS : ceshi
     * ADDRESS : sdsf
     * TEMPADDRESSNUM : null
     * VEHICLEBRANDNAME : 宝马
     * COLORNAME : 紫色
     * SOURCETYPE : 1
     * SOURCENUM : null
     * COLORID2 : 18
     * COLORNAME2 : 粉色
     * PhotoList": "[]",
     * FILENUMBER": null,
     * ISSIGN": null,
     * PhotoListFile": []
     */

    private String BeautifiedREGISTERID;
    private String VEHICLEBRANDName;
    private String ColorName;
    private String InsertUserName;
    private String UNITName;
    private String AlertUserName;
    private boolean _ShowAll;
    private String REGISTERID;
    private String VEHICLEBRAND;
    private String VEHICLETYPE;
    private String VEHICLEMODELS;
    private String COLORID;
    private String ENGINENO;
    private String SHELVESNO;
    private String BUYDATE;
    private String PRICE;
    private String OWNERNAME;
    private String CARDID;
    private String PHONE1;
    private String PHONE2;
    private String INSURANCETYPE;
    private String CREATED_USERID;
    private String UNITID;
    private String UNITNO;
    private String OPERATORTIME;
    private String SOURCE;
    private String DATAIP;
    private String ISVALID;
    private String ISREGISTER;
    private String PLATENUMBER;
    private String MODIFIER;
    private String MODIFIEDTIME;
    private String REMARK;
    private String REVERSED1;
    private String REVERSED2;
    private String REVERSED3;
    private String CARDTYPE;
    private String CARDADDRESS;
    private String ADDRESS;
    private String TEMPADDRESSNUM;
    private String VEHICLEBRANDNAME;
    private String COLORNAME;
    private String SOURCETYPE;
    private String SOURCENUM;
    private String COLORID2;
    private String COLORNAME2;
    private String PhotoList;
    private String FILENUMBER;
    private String ISSIGN;
    private List<PhotoModel> PhotoListFile;

    public String getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(String photoList) {
        PhotoList = photoList;
    }

    public String getFILENUMBER() {
        return FILENUMBER;
    }

    public void setFILENUMBER(String FILENUMBER) {
        this.FILENUMBER = FILENUMBER;
    }

    public String getISSIGN() {
        return ISSIGN;
    }

    public void setISSIGN(String ISSIGN) {
        this.ISSIGN = ISSIGN;
    }

    public List<PhotoModel> getPhotoListFile() {
        return PhotoListFile;
    }

    public void setPhotoListFile(List<PhotoModel> photoListFile) {
        PhotoListFile = photoListFile;
    }

    public String getBeautifiedREGISTERID() {
        return BeautifiedREGISTERID;
    }

    public void setBeautifiedREGISTERID(String BeautifiedREGISTERID) {
        this.BeautifiedREGISTERID = BeautifiedREGISTERID;
    }

    public String getVEHICLEBRANDName() {
        return VEHICLEBRANDName;
    }

    public void setVEHICLEBRANDName(String VEHICLEBRANDName) {
        this.VEHICLEBRANDName = VEHICLEBRANDName;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String ColorName) {
        this.ColorName = ColorName;
    }

    public String getInsertUserName() {
        return InsertUserName;
    }

    public void setInsertUserName(String InsertUserName) {
        this.InsertUserName = InsertUserName;
    }

    public String getUNITName() {
        return UNITName;
    }

    public void setUNITName(String UNITName) {
        this.UNITName = UNITName;
    }

    public String getAlertUserName() {
        return AlertUserName;
    }

    public void setAlertUserName(String AlertUserName) {
        this.AlertUserName = AlertUserName;
    }

    public boolean is_ShowAll() {
        return _ShowAll;
    }

    public void set_ShowAll(boolean _ShowAll) {
        this._ShowAll = _ShowAll;
    }

    public String getREGISTERID() {
        return REGISTERID;
    }

    public void setREGISTERID(String REGISTERID) {
        this.REGISTERID = REGISTERID;
    }

    public String getVEHICLEBRAND() {
        return VEHICLEBRAND;
    }

    public void setVEHICLEBRAND(String VEHICLEBRAND) {
        this.VEHICLEBRAND = VEHICLEBRAND;
    }

    public String getVEHICLETYPE() {
        return VEHICLETYPE;
    }

    public void setVEHICLETYPE(String VEHICLETYPE) {
        this.VEHICLETYPE = VEHICLETYPE;
    }

    public String getVEHICLEMODELS() {
        return VEHICLEMODELS;
    }

    public void setVEHICLEMODELS(String VEHICLEMODELS) {
        this.VEHICLEMODELS = VEHICLEMODELS;
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

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
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

    public String getPHONE1() {
        return PHONE1;
    }

    public void setPHONE1(String PHONE1) {
        this.PHONE1 = PHONE1;
    }

    public String getPHONE2() {
        return PHONE2;
    }

    public void setPHONE2(String PHONE2) {
        this.PHONE2 = PHONE2;
    }

    public String getINSURANCETYPE() {
        return INSURANCETYPE;
    }

    public void setINSURANCETYPE(String INSURANCETYPE) {
        this.INSURANCETYPE = INSURANCETYPE;
    }

    public String getCREATED_USERID() {
        return CREATED_USERID;
    }

    public void setCREATED_USERID(String CREATED_USERID) {
        this.CREATED_USERID = CREATED_USERID;
    }

    public String getUNITID() {
        return UNITID;
    }

    public void setUNITID(String UNITID) {
        this.UNITID = UNITID;
    }

    public String getUNITNO() {
        return UNITNO;
    }

    public void setUNITNO(String UNITNO) {
        this.UNITNO = UNITNO;
    }

    public String getOPERATORTIME() {
        return OPERATORTIME;
    }

    public void setOPERATORTIME(String OPERATORTIME) {
        this.OPERATORTIME = OPERATORTIME;
    }

    public String getSOURCE() {
        return SOURCE;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getDATAIP() {
        return DATAIP;
    }

    public void setDATAIP(String DATAIP) {
        this.DATAIP = DATAIP;
    }

    public String getISVALID() {
        return ISVALID;
    }

    public void setISVALID(String ISVALID) {
        this.ISVALID = ISVALID;
    }

    public String getISREGISTER() {
        return ISREGISTER;
    }

    public void setISREGISTER(String ISREGISTER) {
        this.ISREGISTER = ISREGISTER;
    }

    public String getPLATENUMBER() {
        return PLATENUMBER;
    }

    public void setPLATENUMBER(String PLATENUMBER) {
        this.PLATENUMBER = PLATENUMBER;
    }

    public String getMODIFIER() {
        return MODIFIER;
    }

    public void setMODIFIER(String MODIFIER) {
        this.MODIFIER = MODIFIER;
    }

    public String getMODIFIEDTIME() {
        return MODIFIEDTIME;
    }

    public void setMODIFIEDTIME(String MODIFIEDTIME) {
        this.MODIFIEDTIME = MODIFIEDTIME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getREVERSED1() {
        return REVERSED1;
    }

    public void setREVERSED1(String REVERSED1) {
        this.REVERSED1 = REVERSED1;
    }

    public String getREVERSED2() {
        return REVERSED2;
    }

    public void setREVERSED2(String REVERSED2) {
        this.REVERSED2 = REVERSED2;
    }

    public String getREVERSED3() {
        return REVERSED3;
    }

    public void setREVERSED3(String REVERSED3) {
        this.REVERSED3 = REVERSED3;
    }

    public String getCARDTYPE() {
        return CARDTYPE;
    }

    public void setCARDTYPE(String CARDTYPE) {
        this.CARDTYPE = CARDTYPE;
    }

    public String getCARDADDRESS() {
        return CARDADDRESS;
    }

    public void setCARDADDRESS(String CARDADDRESS) {
        this.CARDADDRESS = CARDADDRESS;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getTEMPADDRESSNUM() {
        return TEMPADDRESSNUM;
    }

    public void setTEMPADDRESSNUM(String TEMPADDRESSNUM) {
        this.TEMPADDRESSNUM = TEMPADDRESSNUM;
    }

    public String getVEHICLEBRANDNAME() {
        return VEHICLEBRANDNAME;
    }

    public void setVEHICLEBRANDNAME(String VEHICLEBRANDNAME) {
        this.VEHICLEBRANDNAME = VEHICLEBRANDNAME;
    }

    public String getCOLORNAME() {
        return COLORNAME;
    }

    public void setCOLORNAME(String COLORNAME) {
        this.COLORNAME = COLORNAME;
    }

    public String getSOURCETYPE() {
        return SOURCETYPE;
    }

    public void setSOURCETYPE(String SOURCETYPE) {
        this.SOURCETYPE = SOURCETYPE;
    }

    public String getSOURCENUM() {
        return SOURCENUM;
    }

    public void setSOURCENUM(String SOURCENUM) {
        this.SOURCENUM = SOURCENUM;
    }

    public String getCOLORID2() {
        return COLORID2;
    }

    public void setCOLORID2(String COLORID2) {
        this.COLORID2 = COLORID2;
    }

    public String getCOLORNAME2() {
        return COLORNAME2;
    }

    public void setCOLORNAME2(String COLORNAME2) {
        this.COLORNAME2 = COLORNAME2;
    }
}

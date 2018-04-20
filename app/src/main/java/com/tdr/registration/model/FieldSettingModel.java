package com.tdr.registration.model;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Linus_Xie on 2016/12/20.
 */
public class FieldSettingModel {

    /**
     * LISTID : 44190EEFDBBB22BAE0536901A8C01946
     * CityID : B416C86B5AAE421B9FF1E9BF01C74B47
     * ModuleID : 44190EEFDBBA22BAE0536901A8C01946
     * FieldKey : 21190EEFDBBC22AE0536901A8C01946
     * DisplayName : 联系手机
     * IsDisplayed : 1
     * IsRequired : 1
     * DefaultValue : null
     * ModuleName : 备案登记
     * Andriod_Var : editOwnerPhone1
     * IOS_Var : null
     */
    private String LISTID;
    private String CITYID;
    private String MODULEID;
    private String FIELDKEY;
    private String DISPLAYNAME;
    private String ISDISPLAYED;
    private String ISREQUIRED;
    private String DEFAULTVALUE;
    private String MODULENAME;
    private String ANDRIOD_VAR;
    private String IOS_VAR;

    public String getLISTID() {
        return LISTID;
    }

    public void setLISTID(String LISTID) {
        this.LISTID = LISTID;
    }

    public String getCITYID() {
        return CITYID;
    }

    public void setCITYID(String CITYID) {
        this.CITYID = CITYID;
    }

    public String getMODULEID() {
        return MODULEID;
    }

    public void setMODULEID(String MODULEID) {
        this.MODULEID = MODULEID;
    }

    public String getFIELDKEY() {
        return FIELDKEY;
    }

    public void setFIELDKEY(String FIELDKEY) {
        this.FIELDKEY = FIELDKEY;
    }

    public String getDISPLAYNAME() {
        return DISPLAYNAME;
    }

    public void setDISPLAYNAME(String DISPLAYNAME) {
        this.DISPLAYNAME = DISPLAYNAME;
    }

    public String getISDISPLAYED() {
        return ISDISPLAYED;
    }

    public void setISDISPLAYED(String ISDISPLAYED) {
        this.ISDISPLAYED = ISDISPLAYED;
    }

    public String getISREQUIRED() {
        return ISREQUIRED;
    }

    public void setISREQUIRED(String ISREQUIRED) {
        this.ISREQUIRED = ISREQUIRED;
    }

    public String getDEFAULTVALUE() {
        return DEFAULTVALUE;
    }

    public void setDEFAULTVALUE(String DEFAULTVALUE) {
        this.DEFAULTVALUE = DEFAULTVALUE;
    }

    public String getMODULENAME() {
        return MODULENAME;
    }

    public void setMODULENAME(String MODULENAME) {
        this.MODULENAME = MODULENAME;
    }

    public String getANDRIOD_VAR() {
        return ANDRIOD_VAR;
    }

    public void setANDRIOD_VAR(String ANDRIOD_VAR) {
        this.ANDRIOD_VAR = ANDRIOD_VAR;
    }

    public String getIOS_VAR() {
        return IOS_VAR;
    }

    public void setIOS_VAR(String IOS_VAR) {
        this.IOS_VAR = IOS_VAR;
    }
}

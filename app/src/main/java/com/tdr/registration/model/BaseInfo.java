package com.tdr.registration.model;

import com.google.gson.annotations.SerializedName;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2016/9/18.
 */
@Table(name = "base_db")
public class BaseInfo implements Serializable {

    /**
     * LISTID : 9d5aa0630d7f479cb895a2a9328f52cc
     * CITYCODE : 3306
     * CITYNAME : 绍兴
     * FULL_SPELL : SHAOXIN
     * AB_SPELL : SX
     * API_URL : http://192.168.1.25:18421/mobileservice.asmx
     * PLATENUMBER_REGULAR : [{"CarType":1,"Regular":"^(YC|KQ|SY|ZJ|CZ|XC|GX|PJ|BH)\\d{6}$"}]
     * CAR_TYPE : 1,2,3,99
     * ISVALID : 1
     * CREATETIME : 2016-09-18 17:16:28
     * UPDATETIME : 2016-09-18 17:16:28
     * APPNAME:****************
     */

    @Column(name = "LISTID", isId = true)
    @SerializedName("LISTID")
    private String listId;


    @Column(name = "CITYCODE")
    @SerializedName("CITYCODE")
    private String cityCode;


    @Column(name = "CITYNAME")
    @SerializedName("CITYNAME")
    private String cityName;


    @Column(name = "FULL_SPELL")
    @SerializedName("FULL_SPELL")
    private String fullSpell;


    @Column(name = "AB_SPELL")
    @SerializedName("AB_SPELL")
    private String abSpell;


    @Column(name = "API_URL")
    @SerializedName("API_URL")
    private String apiUrl;


    @Column(name = "PLATENUMBER_REGULAR")
    @SerializedName("PLATENUMBER_REGULAR")
    private String platenumberRegular;


    @Column(name = "CAR_TYPE")
    @SerializedName("CAR_TYPE")
    private String cardType;


    @Column(name = "ISVALID")
    @SerializedName("ISVALID")
    private String isValid;


    @Column(name = "CREATETIME")
    @SerializedName("CREATETIME")
    private String createTime;


    @Column(name = "UPDATETIME")
    @SerializedName("UPDATETIME")
    private String updateTime;
    /**
     * [{"KEY":"100000000188","DEVICETYPE":"8001,8002","CONTENT":"ELDER","PCCODE":"0802","PROVINCEABBR":"GX","CITYABBR":"LZ","XQCode":""}]
     * BLUETOOTH_REGULAR : [{\"KEY\":\"010203040506\",\"DEVICETYPE\":\"8001,8002\",\"CONTENT\":\"ELDER\",\"PCCODE\":\"0701\",\"PROVINCEABBR\":\"YN\",\"CITYABBR\":\"KM\"}]
     * CanPolicyEdit : 0
     */

    @Column(name = "BLUETOOTH_REGULAR")
    @SerializedName("BLUETOOTH_REGULAR")
    private String bluetooth_Regular;


    @Column(name = "CanPolicyEdit")
    @SerializedName("CanPolicyEdit")
    private String canPolicyEdit;//0不编辑（不显示），1可编辑（显示）
    /**
     * PHOTODEFIND : 车辆标签涂胶水照片,电池标签涂胶水照片,车辆标签安装位置,电池标签安装位置
     * PLATEFORM : ANDROID,IOS
     * APPNAME : 电动车备案登记系统
     */

    @Column(name = "PHOTODEFIND")
    @SerializedName("PHOTODEFIND")
    private String photoDefind;

    @Column(name = "PHOTOCONFIG")
    @SerializedName("PHOTOCONFIG")
    private String photoConfig;

    @Column(name = "PREPHOTOCONFIG")
    @SerializedName("PREPHOTOCONFIG")
    private String prephotoConfig;

    @Column(name = "PLATEFORM")
    @SerializedName("PLATEFORM")
    private String plateFrom;

    @Column(name = "APPNAME")
    @SerializedName("APPNAME")
    private String appName;

    /**
     * VERSION:1.0
     */
    @Column(name = "VERSION")
    @SerializedName("VERSION")
    private String version;
    /**
     * APPCONFIG : [{"key":"WhiteList","value":1},{"key":"WhiteListUrl","value":"http://192.168.1.25:18424"}]
     */

    @Column(name = "APPCONFIG")
    @SerializedName("APPCONFIG")
    private String appConfig;

    @Column(name = "FIELDSETTING")
    @SerializedName("FIELDSETTING")
    private String fieldSetting;

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getFullSpell() {
        return fullSpell;
    }

    public void setFullSpell(String fullSpell) {
        this.fullSpell = fullSpell;
    }

    public String getAbSpell() {
        return abSpell;
    }

    public void setAbSpell(String abSpell) {
        this.abSpell = abSpell;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getPlatenumberRegular() {
        return platenumberRegular;
    }

    public void setPlatenumberRegular(String platenumberRegular) {
        this.platenumberRegular = platenumberRegular;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public BaseInfo(String cityName, String fullSpell) {
        this.cityName = cityName;
        this.fullSpell = fullSpell;
    }

    /**
     * !!!!!没有此构造方法在findAll的时候会出现All elements are null
     */
    public BaseInfo() {
    }

    public String getBluetooth_Regular() {
        return bluetooth_Regular;
    }

    public void setBluetooth_Regular(String bluetooth_Regular) {
        this.bluetooth_Regular = bluetooth_Regular;
    }

    public String getCanPolicyEdit() {
        return canPolicyEdit;
    }

    public void setCanPolicyEdit(String canPolicyEdit) {
        this.canPolicyEdit = canPolicyEdit;
    }

    public String getPhotoDefind() {
        return photoDefind;
    }

    public void setPhotoDefind(String photoDefind) {
        this.photoDefind = photoDefind;
    }

    public String getPlateFrom() {
        return plateFrom;
    }

    public void setPlateFrom(String plateFrom) {
        this.plateFrom = plateFrom;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(String appConfig) {
        this.appConfig = appConfig;
    }

    public String getPhotoConfig() {
        return photoConfig;
    }


    public void setPhotoConfig(String photoConfig) {
        this.photoConfig = photoConfig;
    }

    public String getPrephotoConfig() {
        return prephotoConfig;
    }

    public void setPrephotoConfig(String prephotoConfig) {
        this.prephotoConfig = prephotoConfig;
    }

    public String getFieldSetting() {
        return fieldSetting;
    }

    public void setFieldSetting(String fieldSetting) {
        this.fieldSetting = fieldSetting;
    }
}

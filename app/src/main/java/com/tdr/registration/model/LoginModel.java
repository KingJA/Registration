package com.tdr.registration.model;

import java.util.List;

/**
 * 登录的时候需要传入的对象
 * Created by Linus_Xie on 2016/9/13.
 */
public class LoginModel {


    /**
     * UserID : 618D9E54-F8B0-4B7B-A389-39596F6CA849
     * UserName : admin
     * BeiAnDianName : 绍兴市公安局
     * DispName : 市管理员
     * RegionID : 1E8D90ED-DF84-CD0B-5E05-30207A021944
     * RegionName : 绍兴市公安局
     * AccessToken : dcfa33704edd480da606bd9229f2c5d2admin
     * CodeVersion : 1.0.1
     * RegionNo : 3306
     * RoleName : null
     * RolePowers : 104,204,304,404,61,62,101,111,112,201,301,401,901,1001,1800,2400,2701,102,202,302,402,902,1002,1900,103,203,303,403,903,1003,2000,2100,107,108,109,80,105,300,400,1200,10,110,900,30,4000,40,50,70,60,200,500,2700,1000,100,1100,3700,4001,3300,800,2800
     * UserType : 1
     * Binding : []
     * ErrorCode : 0
     * Data : null
     */

    private String UserID;
    private String UserName;
    private String BeiAnDianName;
    private String DispName;
    private String RegionID;
    private String RegionName;
    private String AccessToken;
    private String CodeVersion;
    private String RegionNo;
    private Object RoleName;
    private String RolePowers;
    private String UserType;
    private String ErrorCode;
    private String Data;
    private String City;
    private List<?> Binding;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getBeiAnDianName() {
        return BeiAnDianName;
    }

    public void setBeiAnDianName(String BeiAnDianName) {
        this.BeiAnDianName = BeiAnDianName;
    }

    public String getDispName() {
        return DispName;
    }

    public void setDispName(String DispName) {
        this.DispName = DispName;
    }

    public String getRegionID() {
        return RegionID;
    }

    public void setRegionID(String RegionID) {
        this.RegionID = RegionID;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String RegionName) {
        this.RegionName = RegionName;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String AccessToken) {
        this.AccessToken = AccessToken;
    }

    public String getCodeVersion() {
        return CodeVersion;
    }

    public void setCodeVersion(String CodeVersion) {
        this.CodeVersion = CodeVersion;
    }

    public String getRegionNo() {
        return RegionNo;
    }

    public void setRegionNo(String RegionNo) {
        this.RegionNo = RegionNo;
    }

    public Object getRoleName() {
        return RoleName;
    }

    public void setRoleName(Object RoleName) {
        this.RoleName = RoleName;
    }

    public String getRolePowers() {
        return RolePowers;
    }

    public void setRolePowers(String RolePowers) {
        this.RolePowers = RolePowers;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public List<?> getBinding() {
        return Binding;
    }

    public void setBinding(List<?> Binding) {
        this.Binding = Binding;
    }
}

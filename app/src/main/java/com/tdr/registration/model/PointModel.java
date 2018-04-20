package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2017/1/10.
 */

public class PointModel implements Serializable{

    /**
     * ConfigId : 8CD600E3DA36497AB49E770FC31E5D3A
     * RegistersiteId : 456B54AA0AE648AD89685D66AED85180
     * OnTime : 08:00
     * OffTime : 12:00
     * InstallCnt : 50
     * SurplusNum : 50
     * InTime : 2016-12-22 05:22:55
     */

    private String ConfigId;
    private String RegistersiteId;
    private String OnTime;
    private String OffTime;
    private String InstallCnt;
    private String SurplusNum;
    private String InTime;

    public String getConfigId() {
        return ConfigId;
    }

    public void setConfigId(String ConfigId) {
        this.ConfigId = ConfigId;
    }

    public String getRegistersiteId() {
        return RegistersiteId;
    }

    public void setRegistersiteId(String RegistersiteId) {
        this.RegistersiteId = RegistersiteId;
    }

    public String getOnTime() {
        return OnTime;
    }

    public void setOnTime(String OnTime) {
        this.OnTime = OnTime;
    }

    public String getOffTime() {
        return OffTime;
    }

    public void setOffTime(String OffTime) {
        this.OffTime = OffTime;
    }

    public String getInstallCnt() {
        return InstallCnt;
    }

    public void setInstallCnt(String InstallCnt) {
        this.InstallCnt = InstallCnt;
    }

    public String getSurplusNum() {
        return SurplusNum;
    }

    public void setSurplusNum(String SurplusNum) {
        this.SurplusNum = SurplusNum;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String InTime) {
        this.InTime = InTime;
    }
}

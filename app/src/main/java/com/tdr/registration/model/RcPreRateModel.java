package com.tdr.registration.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xiezu on 2017/1/11.
 */

public class RcPreRateModel extends PreModel implements Serializable {


    /**
     * Vehiclemodels : null
     * CardType : 4
     * Phone2 : null
     * Remark : null
     * State : 0
     * IsDelete : 0
     * RegistersiteId : null
     * ConfigId : 421134F018DF4CAEB67CCB7909EA56EA
     * ReservateTime : 2016-12-21 06:19:28
     * Register_Config : null
     * Registersite : null
     * Seq : null
     */
    private String RegistersiteId;
    private String ConfigId;
    private String ReservateTime;
    private String Register_Config;
    private String Registersite;
    private String Seq;

    public String getRegistersiteId() {
        return RegistersiteId;
    }

    public void setRegistersiteId(String RegistersiteId) {
        this.RegistersiteId = RegistersiteId;
    }

    public String getConfigId() {
        return ConfigId;
    }

    public void setConfigId(String ConfigId) {
        this.ConfigId = ConfigId;
    }

    public String getReservateTime() {
        return ReservateTime;
    }

    public void setReservateTime(String ReservateTime) {
        this.ReservateTime = ReservateTime;
    }

    public String getRegister_Config() {
        return Register_Config;
    }

    public void setRegister_Config(String Register_Config) {
        this.Register_Config = Register_Config;
    }

    public String getRegistersite() {
        return Registersite;
    }

    public void setRegistersite(String Registersite) {
        this.Registersite = Registersite;
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String Seq) {
        this.Seq = Seq;
    }
}

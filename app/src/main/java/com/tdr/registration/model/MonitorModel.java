package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Linus_Xie on 2016/12/16.
 */

public class MonitorModel implements Serializable {

    /**
     * StationName : 云南省昆明市官渡区北京路与拓东路
     * DEVICEID : 562242
     * ELECTROMBILEID : 0000057
     * MONITORTIME : 2016-12-16 17:29:39
     * SERVICETIME : 2016-12-16 17:32:31
     * DATABASETIME : 2016-12-16 17:33:02
     * REMARK : null
     * SEARCHPLATENUMBER : 0000057
     * LNG : 102.718298
     * LAT : 25.036183
     * RUNSTATE : 0
     * STOPSTATE : 0
     * RUNNINGTIME : 0
     */

    private String StationName;
    private String DEVICEID;
    private String ELECTROMBILEID;
    private String MONITORTIME;
    private String SERVICETIME;
    private String DATABASETIME;
    private String REMARK;
    private String SEARCHPLATENUMBER;
    private double LNG;
    private double LAT;
    private String RUNSTATE;
    private String STOPSTATE;
    private String RUNNINGTIME;

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String StationName) {
        this.StationName = StationName;
    }

    public String getDEVICEID() {
        return DEVICEID;
    }

    public void setDEVICEID(String DEVICEID) {
        this.DEVICEID = DEVICEID;
    }

    public String getELECTROMBILEID() {
        return ELECTROMBILEID;
    }

    public void setELECTROMBILEID(String ELECTROMBILEID) {
        this.ELECTROMBILEID = ELECTROMBILEID;
    }

    public String getMONITORTIME() {
        return MONITORTIME;
    }

    public void setMONITORTIME(String MONITORTIME) {
        this.MONITORTIME = MONITORTIME;
    }

    public String getSERVICETIME() {
        return SERVICETIME;
    }

    public void setSERVICETIME(String SERVICETIME) {
        this.SERVICETIME = SERVICETIME;
    }

    public String getDATABASETIME() {
        return DATABASETIME;
    }

    public void setDATABASETIME(String DATABASETIME) {
        this.DATABASETIME = DATABASETIME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getSEARCHPLATENUMBER() {
        return SEARCHPLATENUMBER;
    }

    public void setSEARCHPLATENUMBER(String SEARCHPLATENUMBER) {
        this.SEARCHPLATENUMBER = SEARCHPLATENUMBER;
    }

    public double getLNG() {
        return LNG;
    }

    public void setLNG(double LNG) {
        this.LNG = LNG;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public String getRUNSTATE() {
        return RUNSTATE;
    }

    public void setRUNSTATE(String RUNSTATE) {
        this.RUNSTATE = RUNSTATE;
    }

    public String getSTOPSTATE() {
        return STOPSTATE;
    }

    public void setSTOPSTATE(String STOPSTATE) {
        this.STOPSTATE = STOPSTATE;
    }

    public String getRUNNINGTIME() {
        return RUNNINGTIME;
    }

    public void setRUNNINGTIME(String RUNNINGTIME) {
        this.RUNNINGTIME = RUNNINGTIME;
    }
}

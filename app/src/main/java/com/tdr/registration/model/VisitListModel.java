package com.tdr.registration.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class VisitListModel implements Serializable {
    /**
     * {
     * "VISITRESULTDES": "运到外地(带回老家)",
     * "RECORDLIST": [
     * {
     * "RECORDID": "11111",
     * "CASEID": "7D0CD082B4AD4721B576CFCF11BBAE1B",
     * "ECID": null,
     * "STATUS": 3,
     * "REASONID": "FD559ADA8F7A4881BF3EB6475C6C4D06",
     * "VISITRESULTDES": "运到外地(带回老家)",
     * "REMARK": "测试",
     * "UNITNAME": "昆明市",
     * "OPERATOR": "618D9E54-F8B0-4B7B-A389-39596F6CA849",
     * "OPERATORTIME": "2017-07-28 14:41:13",
     * "REVISIT_TYPE": 2
     * }
     * ],
     * "CASEID": "7D0CD082B4AD4721B576CFCF11BBAE1B",
     * "ECID": "AA8CF931-2BB2-4BEA-A8AB-F34063522962",
     * "PLATENUMBER": "0000019",
     * "UNITID": "37180C76-669F-845A-5E05-36801A8C0935",
     * "OWNERNAME": "大力",
     * "CARDID": "330326199408015618",
     * "PHONE": "18958903392",
     * "CREATED_ON": "2017-04-25 22:10:37",
     * "LASTMONITORTIME": "0001-01-01 00:00:00",
     * "STATUS": 1,
     * "VISITRESULT": 3,
     * "VISITTIME": "2017-07-28 14:40:43",
     * "INTIME": "2017-07-27 20:31:25"
     * },
     */


    boolean Selelct;

    public boolean isSelelct() {
        return Selelct;
    }

    public void setSelelct(boolean selelct) {
        Selelct = selelct;
    }

    String VISITRESULTDES;
    List<recordlist> RECORDLIST;
    String CASEID;
    String ECID;
    String PLATENUMBER;
    String UNITID;
    String OWNERNAME;
    String CARDID;
    String PHONE;
    String CREATED_ON;
    String LASTMONITORTIME;
    String STATUS;
    String VISITRESULT;
    String VISITTIME;
    String INTIME;
    String TALK_TIME;

    public String getVISITRESULTDES() {
        return VISITRESULTDES;
    }

    public void setVISITRESULTDES(String VISITRESULTDES) {
        this.VISITRESULTDES = VISITRESULTDES;
    }

    public List<recordlist> getRECORDLIST() {
        return RECORDLIST;
    }

    public void setRECORDLIST(List<recordlist> RECORDLIST) {
        this.RECORDLIST = RECORDLIST;
    }

    public String getCASEID() {
        return CASEID;
    }

    public void setCASEID(String CASEID) {
        this.CASEID = CASEID;
    }

    public String getECID() {
        return ECID;
    }

    public void setECID(String ECID) {
        this.ECID = ECID;
    }

    public String getPLATENUMBER() {
        return PLATENUMBER;
    }

    public void setPLATENUMBER(String PLATENUMBER) {
        this.PLATENUMBER = PLATENUMBER;
    }

    public String getUNITID() {
        return UNITID;
    }

    public void setUNITID(String UNITID) {
        this.UNITID = UNITID;
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

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getCREATED_ON() {
        return CREATED_ON;
    }

    public void setCREATED_ON(String CREATED_ON) {
        this.CREATED_ON = CREATED_ON;
    }

    public String getLASTMONITORTIME() {
        return LASTMONITORTIME;
    }

    public void setLASTMONITORTIME(String LASTMONITORTIME) {
        this.LASTMONITORTIME = LASTMONITORTIME;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getVISITRESULT() {
        return VISITRESULT;
    }

    public void setVISITRESULT(String VISITRESULT) {
        this.VISITRESULT = VISITRESULT;
    }

    public String getVISITTIME() {
        return VISITTIME;
    }

    public void setVISITTIME(String VISITTIME) {
        this.VISITTIME = VISITTIME;
    }

    public String getINTIME() {
        return INTIME;
    }

    public void setINTIME(String INTIME) {
        this.INTIME = INTIME;
    }

    public String getTALK_TIME() {
        return TALK_TIME;
    }

    public void setTALK_TIME(String TALK_TIME) {
        this.TALK_TIME = TALK_TIME;
    }

    public static class recordlist implements Serializable {
        String RECORDID;
        String CASEID;
        String ECID;
        String STATUS;
        String REASONID;
        String VISITRESULTDES;
        String REMARK;
        String UNITNAME;
        String OPERATOR;
        String OPERATORTIME;
        String REVISIT_TYPE;

        public String getRECORDID() {
            return RECORDID;
        }

        public void setRECORDID(String RECORDID) {
            this.RECORDID = RECORDID;
        }

        public String getCASEID() {
            return CASEID;
        }

        public void setCASEID(String CASEID) {
            this.CASEID = CASEID;
        }

        public String getECID() {
            return ECID;
        }

        public void setECID(String ECID) {
            this.ECID = ECID;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getREASONID() {
            return REASONID;
        }

        public void setREASONID(String REASONID) {
            this.REASONID = REASONID;
        }

        public String getVISITRESULTDES() {
            return VISITRESULTDES;
        }

        public void setVISITRESULTDES(String VISITRESULTDES) {
            this.VISITRESULTDES = VISITRESULTDES;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public String getUNITNAME() {
            return UNITNAME;
        }

        public void setUNITNAME(String UNITNAME) {
            this.UNITNAME = UNITNAME;
        }

        public String getOPERATOR() {
            return OPERATOR;
        }

        public void setOPERATOR(String OPERATOR) {
            this.OPERATOR = OPERATOR;
        }

        public String getOPERATORTIME() {
            return OPERATORTIME;
        }

        public void setOPERATORTIME(String OPERATORTIME) {
            this.OPERATORTIME = OPERATORTIME;
        }

        public String getREVISIT_TYPE() {
            return REVISIT_TYPE;
        }

        public void setREVISIT_TYPE(String REVISIT_TYPE) {
            this.REVISIT_TYPE = REVISIT_TYPE;
        }
    }

}

package com.tdr.registration.model;




import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 黑车对象
 * Created by Linus_Xie on 2016/10/15.
 */

@Table(name = "blackCar_db")
public class BlackCarModel implements Serializable {


    /**
     * SYNCID : 8190
     * THEFTNO : 286397980
     * THEFTNO2 : 4203591254
     * PLATENUMBER : YC000002
     * STATE : 0
     * OPERATORTIME : 2015-10-09 10:52:55
     */

    @Column(name = "id", isId = true)
    private String id;
    @Column(name = "SYNCID")
    @SerializedName("SYNCID")
    private String SYNCID;
    @Column(name = "THEFTNO")
    @SerializedName("THEFTNO")
    private String THEFTNO;
    @Column(name = "THEFTNO2")
    @SerializedName("THEFTNO2")
    private String THEFTNO2;
    @Column(name = "PLATENUMBER")
    @SerializedName("PLATENUMBER")
    private String PLATENUMBER;
    @Column(name = "STATE")
    @SerializedName("STATE")
    private String STATE;//0 新增    2 删除
    @Column(name = "OPERATORTIME")
    @SerializedName("OPERATORTIME")
    private String OPERATORTIME;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSYNCID() {
        return SYNCID;
    }

    public void setSYNCID(String SYNCID) {
        this.SYNCID = SYNCID;
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

    public String getPLATENUMBER() {
        return PLATENUMBER;
    }

    public void setPLATENUMBER(String PLATENUMBER) {
        this.PLATENUMBER = PLATENUMBER;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getOPERATORTIME() {
        return OPERATORTIME;
    }

    public void setOPERATORTIME(String OPERATORTIME) {
        this.OPERATORTIME = OPERATORTIME;
    }
}

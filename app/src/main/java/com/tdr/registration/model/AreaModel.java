package com.tdr.registration.model;




import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 辖区列表
 */
@Table(name = "area_db")
public class AreaModel implements Serializable {


    /**
     * value : 37180C76-66A3-645A-5E05-36801A8C0935
     * name : 六甲乡派出所
     * pvalue : 37180C76-66A0-B45A-5E05-36801A8C0935
     * isdirect : 0
     * type : 2
     * unitno : 530111203
     */
    @Column(name = "VALUE", isId = true)
    @SerializedName("value")
    private String value;
    @Column(name = "NAME")
    @SerializedName("name")
    private String name;
    @Column(name = "PVALUE")
    @SerializedName("pvalue")
    private String pvalue;
    @Column(name = "ISDIRECT")
    @SerializedName("isdirect")
    private String isdirect;
    @Column(name = "TYPE")
    @SerializedName("type")
    private String type;
    @Column(name = "UNITNO")
    @SerializedName("unitno")
    private String unitno;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPvalue() {
        return pvalue;
    }

    public void setPvalue(String pvalue) {
        this.pvalue = pvalue;
    }

    public String getIsdirect() {
        return isdirect;
    }

    public void setIsdirect(String isdirect) {
        this.isdirect = isdirect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }
}

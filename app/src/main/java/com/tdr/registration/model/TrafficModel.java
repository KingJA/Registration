package com.tdr.registration.model;

import com.google.gson.annotations.SerializedName;



import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 交警列表
 * Created by Linus_Xie on 2016/9/28.
 */
@Table(name = "traffic_db")
public class TrafficModel implements Serializable {

    /**
     * value : 12B82978-8C5C-D36F-4E05-0007F01005EB
     * name : 市局交警支队一大队一中队
     * pvalue : 330302
     * isdirect : 0
     * type : 0
     */
    @Column(name = "value", isId = true)
    @SerializedName("value")
    private String value;

    @Column(name ="name")
    @SerializedName("name")
    private String name;

    @Column(name ="pvalue")
    @SerializedName("pValue")
    private String pValue;

    @Column(name ="isdirect")
    @SerializedName("isDirect")
    private String isDirect;

    @Column(name ="type")
    @SerializedName("type")
    private String type;

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

    public String getPValue() {
        return pValue;
    }

    public void setPValue(String pValue) {
        this.pValue = pValue;
    }

    public String getIsDirect() {
        return isDirect;
    }

    public void setIsDirect(String isDirect) {
        this.isDirect = isDirect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

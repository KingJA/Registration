package com.tdr.registration.model;

import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Linus_Xie on 2016/9/13.
 */
@Table(name = "bikeInfo_db")
public class BikeCode {

    /**
     * INTIME : 2015-08-27 11:18:00
     * SORT : 0
     * NAME : 绿一
     * CODEID : a3e64819-6326-4662-80b2-5ab71a2e6497
     * REMARK : 电动车品牌
     * UPDATETIME : 2015-08-27 11:18:00
     * TYPE : 1
     * CODE : 1965
     */


    @Column(name ="CODEID", isId = true)
    @SerializedName("CODEID")
    private String codeId;

    @Column(name ="INTIME")
    @SerializedName("INTIME")
    private String inTime;

    @Column(name ="SORT")
    @SerializedName("SORT")
    private String sort;

    @Column(name ="NAME")
    @SerializedName("NAME")
    private String name;



    @Column(name ="REMARK")
    @SerializedName("REMARK")
    private String remark;

    @Column(name ="UPDATETIME")
    @SerializedName("UPDATETIME")
    private String updateTime;

    @Column(name ="TYPE")
    @SerializedName("TYPE")
    private String type;

    @Column(name ="CODE")
    @SerializedName("CODE")
    private String code;

    @Column(name ="ISDELETE")
    @SerializedName("ISDELETE")
    private String isdelete;

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BikeCode() {
    }

}

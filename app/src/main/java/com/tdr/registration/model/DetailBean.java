package com.tdr.registration.model;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 保险细节
 * Created by Linus_Xie on 2016/9/21.
 */
@Table(name = "insuranceDetail_db")
public class DetailBean implements Serializable {
/*
"RemarkID": "0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0",
        "CID": null,
        "Name": "150元",
        "Price": 150,
        "DeadLine": 3,
        "IsValid": 1,
        "CreateTime": "2016-09-19 00:00:00",
        "Claim": null
        */
    /**
     * RemarkID : 0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0
     * Name : 1年/40元
     * Price : 40
     * DeadLine : 1
     * IsValid : 1
     * CreateTime : 2016-09-21 09:07:04
     */

    @Column(name = "id", isId = true)
    private String id;
    @Column(name = "RemarkID")
    private String RemarkID;
    @Column(name = "CID")
    private String CID;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Price")
    private String Price;
    @Column(name = "DeadLine")
    private String DeadLine;
    @Column(name = "IsValid")
    private String IsValid;
    @Column(name = "CreateTime")
    private String CreateTime;
    @Column(name = "Claim")
    private String Claim;

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getClaim() {
        return Claim;
    }

    public void setClaim(String claim) {
        Claim = claim;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarkID() {
        return RemarkID;
    }

    public void setRemarkID(String RemarkID) {
        this.RemarkID = RemarkID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getDeadLine() {
        return DeadLine;
    }

    public void setDeadLine(String DeadLine) {
        this.DeadLine = DeadLine;
    }

    public String getIsValid() {
        return IsValid;
    }

    public void setIsValid(String IsValid) {
        this.IsValid = IsValid;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}

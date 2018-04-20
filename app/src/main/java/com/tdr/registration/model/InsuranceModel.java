package com.tdr.registration.model;




import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * 保险对象
 * Created by Linus_Xie on 2016/9/20.
 */
@Table(name = "insurance_db")
public class InsuranceModel implements Serializable {
 /*   "Data": [
    {
        "ListID": "6C5027BC-4B81-41DD-9641-272693C2E260",
            "Name": "防盗备案装置费",
            "SubTitle": "送3年盗抢险",
            "Description": null,
            "RemarkID": "0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0",
            "IsChoose": 1,
            "IsValid": 1,
            "Sort": 1,
            "TypeId": 2,
            "CreateTime": "2016-09-19 00:00:00",
            "VehicleType": 0,
            "UserType": 0,
            "Detail": [
        {
            "RemarkID": "0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0",
                "CID": null,
                "Name": "150元",
                "Price": 150,
                "DeadLine": 3,
                "IsValid": 1,
                "CreateTime": "2016-09-19 00:00:00",
                "Claim": null
        }
            ]
    }
    ]
    */

    /**
     * ListID : 6C5027BC-4B81-41DD-9641-272693C2E260
     * Name : 第三者责任险+驾驶人意外险
     * SubTitle : 责任险+意外险
     * RemarkID : 0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0
     * IsChoose : 1
     * IsValid : 1
     * TypeId : 1
     * CreateTime : 2016-09-21 09:07:04
     * Detail : [{"RemarkID":"0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0","Name":"1年/40元","Price":40,"DeadLine":1,"IsValid":1,"CreateTime":"2016-09-21 09:07:04"},{"RemarkID":"0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0","Name":"2年/75元","Price":75,"DeadLine":2,"IsValid":1,"CreateTime":"2016-09-21 09:07:04"},{"RemarkID":"0B9EF315-A5B7-4FAA-9AA0-29A4F852CBB0","Name":"3年/105元","Price":105,"DeadLine":3,"IsValid":1,"CreateTime":"2016-09-21 09:07:04"}]
     */
    @Column(name = "ListID",isId = true)
    private String ListID;//主键

    @Column(name = "Name")
    private String Name;//保险名称

    @Column(name = "SubTitle")
    private String SubTitle;//子标题


    @Column(name = "Description")
    private String Description;//

    @Column(name = "RemarkID")
    private String RemarkID;//说明ID

    @Column(name = "IsChoose")
    private String IsChoose;//是否必选（0否,1是）

    @Column(name = "IsValid")
    private String IsValid;//是否有效（0否,1是）

    @Column(name = "Sort")
    private String Sort;//

    @Column(name = "CreateTime")
    private String CreateTime;//创建时间

    @Column(name = "TypeId")
    private String TypeId;//保险类型

    @Column(name = "VehicleType")
    private String VehicleType;//车辆类型

    @Column(name = "UserType")
    private String UserType;//

    @Column(name = "Detail")
    private List<DetailBean> Detail;

    @Column(name = "Contract")
    private String Contract;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }


    public String getListID() {
        return ListID;
    }

    public void setListID(String ListID) {
        this.ListID = ListID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String SubTitle) {
        this.SubTitle = SubTitle;
    }

    public String getRemarkID() {
        return RemarkID;
    }

    public void setRemarkID(String RemarkID) {
        this.RemarkID = RemarkID;
    }

    public String getIsChoose() {
        return IsChoose;
    }

    public void setIsChoose(String IsChoose) {
        this.IsChoose = IsChoose;
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

    public String getTypeId() {
        return TypeId;
    }

    public void setTypeId(String typeId) {
        TypeId = typeId;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public List<DetailBean> getDetail() {
        return Detail;
    }

    public void setDetail(List<DetailBean> Detail) {
        this.Detail = Detail;
    }

    public String getContract() {
        return Contract;
    }

    public void setContract(String contract) {
        Contract = contract;
    }
}

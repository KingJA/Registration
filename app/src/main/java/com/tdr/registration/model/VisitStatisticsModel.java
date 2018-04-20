package com.tdr.registration.model;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class VisitStatisticsModel {

    String UnitId;
    String UnitNo;
    String UnitName;
    String UnitType;
    String ManageUnitId;
    String Need_Visit_Total;
    String Has_Visit_Total;
    String IsTotalRow;
    List<Records> records;

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getUnitType() {
        return UnitType;
    }

    public void setUnitType(String unitType) {
        UnitType = unitType;
    }

    public String getManageUnitId() {
        return ManageUnitId;
    }

    public void setManageUnitId(String manageUnitId) {
        ManageUnitId = manageUnitId;
    }

    public String getNeed_Visit_Total() {
        return Need_Visit_Total;
    }

    public void setNeed_Visit_Total(String need_Visit_Total) {
        Need_Visit_Total = need_Visit_Total;
    }

    public String getHas_Visit_Total() {
        return Has_Visit_Total;
    }

    public void setHas_Visit_Total(String has_Visit_Total) {
        Has_Visit_Total = has_Visit_Total;
    }

    public String getIsTotalRow() {
        return IsTotalRow;
    }

    public void setIsTotalRow(String isTotalRow) {
        IsTotalRow = isTotalRow;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public class Records {
        String Status;
        String ParentStatus;
        String StatusTxt;
        String ResultCnt;

       public String getStatus() {
           return Status;
       }

       public void setStatus(String status) {
           Status = status;
       }

       public String getParentStatus() {
           return ParentStatus;
       }

       public void setParentStatus(String parentStatus) {
           ParentStatus = parentStatus;
       }

       public String getStatusTxt() {
           return StatusTxt;
       }

       public void setStatusTxt(String statusTxt) {
           StatusTxt = statusTxt;
       }

       public String getResultCnt() {
           return ResultCnt;
       }

       public void setResultCnt(String resultCnt) {
           ResultCnt = resultCnt;
       }
   }
}

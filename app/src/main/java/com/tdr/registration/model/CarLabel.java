package com.tdr.registration.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class CarLabel implements Serializable {
    String VehicleBrandName;
    String ColorName;
    String ColorName2;
    String EcId;
    String PlateNumber;
    String EngineNo;
    String ShelvesNo;
    String BuyDate;
    String Price;
    String OwnerName;
    String CardId;
    String ResidentAddress;
    String Phone1;
    String Phone2;
    String CreatedOn;
    String Remark;
    List<equipments> Equipments;

    public String getVehicleBrandName() {
        return VehicleBrandName;
    }

    public void setVehicleBrandName(String vehicleBrandName) {
        VehicleBrandName = vehicleBrandName;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public String getColorName2() {
        return ColorName2;
    }

    public void setColorName2(String colorName2) {
        ColorName2 = colorName2;
    }

    public String getEcId() {
        return EcId;
    }

    public void setEcId(String ecId) {
        EcId = ecId;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public String getEngineNo() {
        return EngineNo;
    }

    public void setEngineNo(String engineNo) {
        EngineNo = engineNo;
    }

    public String getShelvesNo() {
        return ShelvesNo;
    }

    public void setShelvesNo(String shelvesNo) {
        ShelvesNo = shelvesNo;
    }

    public String getBuyDate() {
        return BuyDate;
    }

    public void setBuyDate(String buyDate) {
        BuyDate = buyDate;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getResidentAddress() {
        return ResidentAddress;
    }

    public void setResidentAddress(String residentAddress) {
        ResidentAddress = residentAddress;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        Phone1 = phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public List<equipments> getEquipments() {
        return Equipments;
    }

    public void setEquipments(List<equipments> equipments) {
        Equipments = equipments;
    }

    public class equipments implements Serializable{
        String LISTID;
        String ECID;
        String PlateNumber;
        int SIGNTYPE;
        String THEFTNO;
        String ORI_THEFTNO;
        String ORI_PLATENUMBER;
        String OPERATORTIME;
        String ISDELETE;
        String ISDEPLOYED;
        String BindTime;
        String UnBindTime;
        String SIGNTYPENAME;
        extrainfo ExtraInfo;

        public String getSIGNTYPENAME() {
            return SIGNTYPENAME;
        }

        public void setSIGNTYPENAME(String SIGNTYPENAME) {
            this.SIGNTYPENAME = SIGNTYPENAME;
        }

        public String getLISTID() {
            return LISTID;
        }

        public void setLISTID(String LISTID) {
            this.LISTID = LISTID;
        }

        public String getECID() {
            return ECID;
        }

        public void setECID(String ECID) {
            this.ECID = ECID;
        }

        public String getPlateNumber() {
            return PlateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            PlateNumber = plateNumber;
        }

        public int getSIGNTYPE() {
            return SIGNTYPE;
        }

        public void setSIGNTYPE(int SIGNTYPE) {
            this.SIGNTYPE = SIGNTYPE;
        }

        public String getTHEFTNO() {
            return THEFTNO;
        }

        public void setTHEFTNO(String THEFTNO) {
            this.THEFTNO = THEFTNO;
        }

        public String getORI_THEFTNO() {
            return ORI_THEFTNO;
        }

        public void setORI_THEFTNO(String ORI_THEFTNO) {
            this.ORI_THEFTNO = ORI_THEFTNO;
        }

        public String getORI_PLATENUMBER() {
            return ORI_PLATENUMBER;
        }

        public void setORI_PLATENUMBER(String ORI_PLATENUMBER) {
            this.ORI_PLATENUMBER = ORI_PLATENUMBER;
        }

        public String getOPERATORTIME() {
            return OPERATORTIME;
        }

        public void setOPERATORTIME(String OPERATORTIME) {
            this.OPERATORTIME = OPERATORTIME;
        }

        public String getISDELETE() {
            return ISDELETE;
        }

        public void setISDELETE(String ISDELETE) {
            this.ISDELETE = ISDELETE;
        }

        public String getISDEPLOYED() {
            return ISDEPLOYED;
        }

        public void setISDEPLOYED(String ISDEPLOYED) {
            this.ISDEPLOYED = ISDEPLOYED;
        }

        public String getBindTime() {
            return BindTime;
        }

        public void setBindTime(String bindTime) {
            BindTime = bindTime;
        }

        public String getUnBindTime() {
            return UnBindTime;
        }

        public void setUnBindTime(String unBindTime) {
            UnBindTime = unBindTime;
        }

        public extrainfo getExtraInfo() {
            return ExtraInfo;
        }

        public void setExtraInfo(extrainfo extraInfo) {
            ExtraInfo = extraInfo;
        }

        public  class extrainfo implements Serializable {
            String Photo;

            public String getPhoto() {
                return Photo;
            }

            public void setPhoto(String photo) {
                Photo = photo;
            }
        }
    }
}

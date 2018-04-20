package com.tdr.registration.model;

import com.tdr.registration.util.VehiclesStorageUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/25.
 */

public class ConfirmInsuranceModel implements Serializable {
    private String PlateNumber;
    private String Name;
    private String CardType;
    private String CardID;
    private String Phone;
    private List<Insurance> Insurance;

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public List<ConfirmInsuranceModel.Insurance> getInsurance() {
        return Insurance;
    }

    public void setInsurance(List<ConfirmInsuranceModel.Insurance> insurance) {
        Insurance = insurance;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

   public static class Insurance implements Serializable{
    private String Title;
    private String DeadLine;
    private String Money;
    private String SubTitle;
    private String Hyperlink;

    public String getDeadLine() {
        return DeadLine;
    }

    public void setDeadLine(String deadLine) {
        DeadLine = deadLine;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getHyperlink() {
        return Hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        Hyperlink = hyperlink;
    }
}

}

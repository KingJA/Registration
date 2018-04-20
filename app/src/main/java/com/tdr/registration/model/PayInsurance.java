package com.tdr.registration.model;

/**
 * Created by Administrator on 2017/5/16.
 */

import java.io.Serializable;
import java.util.List;

/**
 * 保险支付清单
 */
public class PayInsurance implements Serializable {
    private String EcId;
    private String PayNo;
    private String TradeNo;
    private String PlateNumber;
    private String Total_Amount;
    private String Trade_Status;
    private List<policys> Policys;
    private String Content;
    private String Order_Type;
    private int PaymentWay;
    private String BigPayNo;
    private String CreateTime;

    public String getEcId() {
        return EcId;
    }

    public void setEcId(String ecId) {
        EcId = ecId;
    }

    public String getPayNo() {
        return PayNo;
    }

    public void setPayNo(String payNo) {
        PayNo = payNo;
    }

    public String getTradeNo() {
        return TradeNo;
    }

    public void setTradeNo(String tradeNo) {
        TradeNo = tradeNo;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public String getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        Total_Amount = total_Amount;
    }

    public String getTrade_Status() {
        return Trade_Status;
    }

    public void setTrade_Status(String trade_Status) {
        Trade_Status = trade_Status;
    }

    public List<policys> getPolicys() {
        return Policys;
    }


    public void setPolicys(List<policys> policys) {
        Policys = policys;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getOrder_Type() {
        return Order_Type;
    }

    public void setOrder_Type(String order_Type) {
        Order_Type = order_Type;
    }

    public String getBigPayNo() {
        return BigPayNo;
    }

    public void setBigPayNo(String bigPayNo) {
        BigPayNo = bigPayNo;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getPaymentWay() {
        return PaymentWay;
    }

    public void setPaymentWay(int paymentWay) {
        PaymentWay = paymentWay;
    }
}

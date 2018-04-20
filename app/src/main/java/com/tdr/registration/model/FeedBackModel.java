package com.tdr.registration.model;

/**
 * 反馈对象
 * Created by Linus_Xie on 2016/10/17.
 */

public class FeedBackModel {
    private String LISTID;//指令主键
    private String FEEDBACK;//反馈说明
    private String FEEDBACKSTATUS;//反馈结果状态（0未找到，1找到车，2人车并获）
    private String PHOTOLIST;//反馈上传的图片   多张 以 英文逗号隔开
    private String PLATENUMBER;

    public String getLISTID() {
        return LISTID;
    }

    public void setLISTID(String LISTID) {
        this.LISTID = LISTID;
    }

    public String getFEEDBACK() {
        return FEEDBACK;
    }

    public void setFEEDBACK(String FEEDBACK) {
        this.FEEDBACK = FEEDBACK;
    }

    public String getFEEDBACKSTATUS() {
        return FEEDBACKSTATUS;
    }

    public void setFEEDBACKSTATUS(String FEEDBACKSTATUS) {
        this.FEEDBACKSTATUS = FEEDBACKSTATUS;
    }

    public String getPHOTOLIST() {
        return PHOTOLIST;
    }

    public void setPHOTOLIST(String PHOTOLIST) {
        this.PHOTOLIST = PHOTOLIST;
    }

    public String getPLATENUMBER() {
        return PLATENUMBER;
    }

    public void setPLATENUMBER(String PLATENUMBER) {
        this.PLATENUMBER = PLATENUMBER;
    }
}

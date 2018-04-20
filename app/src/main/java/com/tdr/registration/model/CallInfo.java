package com.tdr.registration.model;

/**
 * Created by Administrator on 2017/10/20.
 */

public class CallInfo {

    public String number;   // 号码
    public long date;       // 日期
    public int type;        // 类型：1来电、2去电、3未接、4拒接
    public String Time;     // 时长
    public CallInfo(String number, long date, int type,String time) {
        this.number = number;
        this.date = date;
        this.type = type;
        this.Time= time;
    }

    @Override
    public String toString() {
        return "CallInfo{" +
                "number='" + number + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", Time=" + Time +
                '}';
    }
}

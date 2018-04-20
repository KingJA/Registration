package com.tdr.registration.model;

import java.util.List;

/**
 * Created by Administrator on 2017/10/18.
 */

public class VisitCenterModel {

//        "today": 0,
//                "week": 0,
//                "month": 0,
//                "detail": [
//        {
//            "date": "10月19日",
//                "needVisitTotal": 16,
//                "hasVisitTotal": 0,
//                "noVisitTotal": 16
//        }
//        ]
    String today;
    String week;
    String month;
    public List<Detail>  detail;

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public class Detail{
        String date;
        String needVisitTotal;
        String hasVisitTotal;
        String noVisitTotal;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNeedVisitTotal() {
            return needVisitTotal;
        }

        public void setNeedVisitTotal(String needVisitTotal) {
            this.needVisitTotal = needVisitTotal;
        }

        public String getHasVisitTotal() {
            return hasVisitTotal;
        }

        public void setHasVisitTotal(String hasVisitTotal) {
            this.hasVisitTotal = hasVisitTotal;
        }

        public String getNoVisitTotal() {
            return noVisitTotal;
        }

        public void setNoVisitTotal(String noVisitTotal) {
            this.noVisitTotal = noVisitTotal;
        }
    }
}

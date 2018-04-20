package com.tdr.registration.model;

import java.util.List;

/**
 * Created by Linus_Xie on 2016/10/8.
 */
public class ResultInsuranceModel {


    /**
     * unitno : null
     * unitid : 37180C76-66A3-D45A-5E05-36801A8C0935
     * unitname : 福海街道派出所
     * unittype : 2
     * pid : 37180C76-66A0-C45A-5E05-36801A8C0935
     * usertype : null
     * userid : null
     * created_user_id : null
     * username : null
     * zs : 0
     * ybq : 0
     * wbq : 0
     * xxs : 0
     * ysl : 0
     * cpbg : 0
     * ysbx : 0
     * platenumber : null
     * Items : [{"TypeId":2,"Title":"防盗备案装置费","SumCount":0,"SumMoney":0,"SubItems":[{"Title":"防盗备案装置费","deadline":1,"count":0,"money":0},{"Title":"防盗备案装置费","deadline":2,"count":0,"money":0},{"Title":"防盗备案装置费","deadline":3,"count":0,"money":0}]},{"TypeId":4,"Title":"责任险","SumCount":0,"SumMoney":0,"SubItems":[{"Title":"责任险","deadline":1,"count":0,"money":0},{"Title":"责任险","deadline":2,"count":0,"money":0}]}]
     */

    private String unitno;
    private String unitid;
    private String unitname;
    private String unittype;
    private String pid;
    private String usertype;
    private String userid;
    private String created_user_id;
    private String username;
    private String zs;
    private String ybq;
    private String wbq;
    private String xxs;
    private String ysl;
    private String cpbg;
    private String ysbx;
    private String platenumber;
    /**
     * TypeId : 2
     * Title : 防盗备案装置费
     * SumCount : 0
     * SumMoney : 0
     * SubItems : [{"Title":"防盗备案装置费","deadline":1,"count":0,"money":0},{"Title":"防盗备案装置费","deadline":2,"count":0,"money":0},{"Title":"防盗备案装置费","deadline":3,"count":0,"money":0}]
     */

    private List<ItemsBean> Items;

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUnittype() {
        return unittype;
    }

    public void setUnittype(String unittype) {
        this.unittype = unittype;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCreated_user_id() {
        return created_user_id;
    }

    public void setCreated_user_id(String created_user_id) {
        this.created_user_id = created_user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public String getYbq() {
        return ybq;
    }

    public void setYbq(String ybq) {
        this.ybq = ybq;
    }

    public String getWbq() {
        return wbq;
    }

    public void setWbq(String wbq) {
        this.wbq = wbq;
    }

    public String getXxs() {
        return xxs;
    }

    public void setXxs(String xxs) {
        this.xxs = xxs;
    }

    public String getYsl() {
        return ysl;
    }

    public void setYsl(String ysl) {
        this.ysl = ysl;
    }

    public String getCpbg() {
        return cpbg;
    }

    public void setCpbg(String cpbg) {
        this.cpbg = cpbg;
    }

    public String getYsbx() {
        return ysbx;
    }

    public void setYsbx(String ysbx) {
        this.ysbx = ysbx;
    }

    public String getPlatenumber() {
        return platenumber;
    }

    public void setPlatenumber(String platenumber) {
        this.platenumber = platenumber;
    }

    public List<ItemsBean> getItems() {
        return Items;
    }

    public void setItems(List<ItemsBean> Items) {
        this.Items = Items;
    }

    public static class ItemsBean {
        private String TypeId;
        private String Title;
        private String SumCount;
        private String SumMoney;
        /**
         * Title : 防盗备案装置费
         * deadline : 1
         * count : 0
         * money : 0
         */

        private List<SubItemsBean> SubItems;

        public String getTypeId() {
            return TypeId;
        }

        public void setTypeId(String TypeId) {
            this.TypeId = TypeId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getSumCount() {
            return SumCount;
        }

        public void setSumCount(String SumCount) {
            this.SumCount = SumCount;
        }

        public String getSumMoney() {
            return SumMoney;
        }

        public void setSumMoney(String SumMoney) {
            this.SumMoney = SumMoney;
        }

        public List<SubItemsBean> getSubItems() {
            return SubItems;
        }

        public void setSubItems(List<SubItemsBean> SubItems) {
            this.SubItems = SubItems;
        }

        public static class SubItemsBean {
            private String Title;
            private String deadline;
            private String count;
            private String money;

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public String getDeadline() {
                return deadline;
            }

            public void setDeadline(String deadline) {
                this.deadline = deadline;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }
        }
    }
}

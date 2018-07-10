package com.tdr.kingja.entity;

import com.google.gson.annotations.SerializedName;
import com.tdr.registration.model.SignTypeInfo;

import java.util.List;

/**
 * Description:TODO
 * Create Time:2018/7/10 10:13
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class GetSettingInfo {

    /**
     * Mnnicipal : {"Name":"温州市公安局","Id":"F591BF0D-DCA1-114F-1E04-0007F01007DF","No":"530101","unittype":0,
     * "CityName":"温州2016测试"}
     * PlateNumberRegular : (^(ZL|LW|OH|RA|YQ|DT|YJ|PY|CN|CH|TS|JK|LR|LC)\d{6}$)
     * VehicleNumberRegular : ^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\u4e00-\u9fa5]$
     * VehiclePlateNumberRegular : {"1":{"des":"电动车","reg":"(^(ZL|LW|OH|RA|YQ|DT|YJ|PY|CN|CH|TS|JK|LR|LC)\\d{6}$)",
     * "info":"车牌格式：1234567"},"2":{"des":"助力车","reg":"(^\\d{7}$)|(^\\d{7}\\+\\d{7}$)","info":"车牌格式：1234567"},
     * "3":{"des":"摩托车","reg":"^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$",
     * "info":"车牌格式：云A12345"},"4":{"des":"警车",
     * "reg":"^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$","info":"车牌格式：云A12345"},
     * "5":{"des":"三轮车","reg":"(^\\d{7}$)|(^\\d{7}\\+\\d{7}$)","info":"车牌格式：1234567"}}
     * PN : null
     * AREACODE : null
     * HasPolicy : true
     * MapType : BaiDu
     * Picture : 8
     * SpecialRole :
     * LimitBlack : false
     * LimitLoginType : false
     * LimitIOS : false
     * Source : [{"name":"110报案","value":"1"},{"name":"派出所报案","value":"2"},{"name":"警综系统","value":"4"}]
     * VehicletypeSelect : null
     * Select : {"FeedbackType":[{"name":"人车并获","value":"3"},{"name":"查获车辆","value":"4"}],
     * "WithoutFeedbackType":[{"name":"人车并获","value":"3"},{"name":"查获车辆","value":"4"},{"name":"查获标签","value":"5"},
     * {"name":"协助查获","value":"8"}],"vehicletype":[{"name":"电动车","value":"1"},{"name":"助力车","value":"2"}]}
     * EqtypeModule : 8
     * AllowEditLeftDay : 7
     * SignTypeInfo : [{"Number":1,"Field":"THEFTNO","Name":"车辆标签","Value":"32803","Alias":"920","EqType":"8023",
     * "VehicleType":"1","isValid":true},{"Number":2,"Field":"THEFTNO2","Name":"电池标签","Value":"32801","Alias":"2.4G",
     * "EqType":"8021","VehicleType":"1","isValid":true},{"Number":3,"Field":"THEFTNO3","Name":"温州标签",
     * "Value":"32804","Alias":"2.4G","EqType":"8024","VehicleType":"1","isValid":true}]
     * PhotoConfig : []
     * lang : {"vehicle":"电动车","address":"现住址","isblack":"","cannotdeploy":"","NonePermission":"车辆未布控,
     * 请到车辆监控栏目申请查看轨迹","UndueString":"该防盗设备到期时间{0}年{1}月{2}日，请注意提前到附近派出所更换防盗设备","ExpireString":"该防盗设备已过期，请到附近派出所更换防盗设备"}
     * InsSysConfig : {"EnableSynergy":false,"CrossFeedback":true,"InsSysDUUnits":"","DeployAutoIns":7,
     * "EnableOffset":true,"OffsetLNG":0,"OffsetLAT":-9.0E-5}
     * hasPlateNumber : false
     * paymentWay : alipay
     * requestNeedTradeNo : true
     * openRenewal : false
     * UnitClass : null
     * login : {"closeAppLogin":false}
     * vehicle : {"ManyVehicleTypeOfDifferentPolicy":true,"OpenStockManage":false,"TheftnoConvert":true,"agent":true,
     * "WarrantyDay":365,"WarrantyString":null,"UnWarrantyString":null}
     * ReadLocalInsurance : false
     * SendSMS : false
     * BatteryTHEFTNO : {"Number":3,"Field":"THEFTNO3","Name":"温州溯源标签","Value":"32804","EqType":"8024","Regular":"^$
     * |^(8024)\\d{10}$","Alias":"2.4G","VehicleType":1,"isValid":true}
     */

    private MnnicipalBean Mnnicipal;
    private String PlateNumberRegular;
    private String VehicleNumberRegular;
    private VehiclePlateNumberRegularBean VehiclePlateNumberRegular;
    private Object PN;
    private Object AREACODE;
    private boolean HasPolicy;

    public boolean isBlackCheck() {
        return BlackCheck;
    }

    public void setBlackCheck(boolean blackCheck) {
        BlackCheck = blackCheck;
    }

    public String getEngineNoRegular() {
        return EngineNoRegular;
    }

    public void setEngineNoRegular(String engineNoRegular) {
        EngineNoRegular = engineNoRegular;
    }

    public String getShelvesNoRegular() {
        return ShelvesNoRegular;
    }

    public void setShelvesNoRegular(String shelvesNoRegular) {
        ShelvesNoRegular = shelvesNoRegular;
    }

    private boolean BlackCheck;
    private String EngineNoRegular;
    private String ShelvesNoRegular;
    private String MapType;
    private int Picture;
    private String SpecialRole;
    private boolean LimitBlack;
    private boolean LimitLoginType;
    private boolean LimitIOS;
    private Object VehicletypeSelect;
    private SelectBean Select;
    private int EqtypeModule;
    private int AllowEditLeftDay;
    private LangBean lang;
    private InsSysConfigBean InsSysConfig;
    private boolean hasPlateNumber;
    private String paymentWay;
    private boolean requestNeedTradeNo;
    private boolean openRenewal;
    private Object UnitClass;
    private LoginBean login;
    private VehicleBean vehicle;
    private boolean ReadLocalInsurance;
    private boolean SendSMS;
    private BatteryTHEFTNOBean BatteryTHEFTNO;
    private List<SourceBean> Source;
    private List<SignTypeInfo> SignType;
    private List<?> PhotoConfig;

    public MnnicipalBean getMnnicipal() {
        return Mnnicipal;
    }

    public void setMnnicipal(MnnicipalBean Mnnicipal) {
        this.Mnnicipal = Mnnicipal;
    }

    public String getPlateNumberRegular() {
        return PlateNumberRegular;
    }

    public void setPlateNumberRegular(String PlateNumberRegular) {
        this.PlateNumberRegular = PlateNumberRegular;
    }

    public String getVehicleNumberRegular() {
        return VehicleNumberRegular;
    }

    public void setVehicleNumberRegular(String VehicleNumberRegular) {
        this.VehicleNumberRegular = VehicleNumberRegular;
    }

    public VehiclePlateNumberRegularBean getVehiclePlateNumberRegular() {
        return VehiclePlateNumberRegular;
    }

    public void setVehiclePlateNumberRegular(VehiclePlateNumberRegularBean VehiclePlateNumberRegular) {
        this.VehiclePlateNumberRegular = VehiclePlateNumberRegular;
    }

    public Object getPN() {
        return PN;
    }

    public void setPN(Object PN) {
        this.PN = PN;
    }

    public Object getAREACODE() {
        return AREACODE;
    }

    public void setAREACODE(Object AREACODE) {
        this.AREACODE = AREACODE;
    }

    public boolean isHasPolicy() {
        return HasPolicy;
    }

    public void setHasPolicy(boolean HasPolicy) {
        this.HasPolicy = HasPolicy;
    }

    public String getMapType() {
        return MapType;
    }

    public void setMapType(String MapType) {
        this.MapType = MapType;
    }

    public int getPicture() {
        return Picture;
    }

    public void setPicture(int Picture) {
        this.Picture = Picture;
    }

    public String getSpecialRole() {
        return SpecialRole;
    }

    public void setSpecialRole(String SpecialRole) {
        this.SpecialRole = SpecialRole;
    }

    public boolean isLimitBlack() {
        return LimitBlack;
    }

    public void setLimitBlack(boolean LimitBlack) {
        this.LimitBlack = LimitBlack;
    }

    public boolean isLimitLoginType() {
        return LimitLoginType;
    }

    public void setLimitLoginType(boolean LimitLoginType) {
        this.LimitLoginType = LimitLoginType;
    }

    public boolean isLimitIOS() {
        return LimitIOS;
    }

    public void setLimitIOS(boolean LimitIOS) {
        this.LimitIOS = LimitIOS;
    }

    public Object getVehicletypeSelect() {
        return VehicletypeSelect;
    }

    public void setVehicletypeSelect(Object VehicletypeSelect) {
        this.VehicletypeSelect = VehicletypeSelect;
    }

    public SelectBean getSelect() {
        return Select;
    }

    public void setSelect(SelectBean Select) {
        this.Select = Select;
    }

    public int getEqtypeModule() {
        return EqtypeModule;
    }

    public void setEqtypeModule(int EqtypeModule) {
        this.EqtypeModule = EqtypeModule;
    }

    public int getAllowEditLeftDay() {
        return AllowEditLeftDay;
    }

    public void setAllowEditLeftDay(int AllowEditLeftDay) {
        this.AllowEditLeftDay = AllowEditLeftDay;
    }

    public LangBean getLang() {
        return lang;
    }

    public void setLang(LangBean lang) {
        this.lang = lang;
    }

    public InsSysConfigBean getInsSysConfig() {
        return InsSysConfig;
    }

    public void setInsSysConfig(InsSysConfigBean InsSysConfig) {
        this.InsSysConfig = InsSysConfig;
    }

    public boolean isHasPlateNumber() {
        return hasPlateNumber;
    }

    public void setHasPlateNumber(boolean hasPlateNumber) {
        this.hasPlateNumber = hasPlateNumber;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public boolean isRequestNeedTradeNo() {
        return requestNeedTradeNo;
    }

    public void setRequestNeedTradeNo(boolean requestNeedTradeNo) {
        this.requestNeedTradeNo = requestNeedTradeNo;
    }

    public boolean isOpenRenewal() {
        return openRenewal;
    }

    public void setOpenRenewal(boolean openRenewal) {
        this.openRenewal = openRenewal;
    }

    public Object getUnitClass() {
        return UnitClass;
    }

    public void setUnitClass(Object UnitClass) {
        this.UnitClass = UnitClass;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public VehicleBean getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleBean vehicle) {
        this.vehicle = vehicle;
    }

    public boolean isReadLocalInsurance() {
        return ReadLocalInsurance;
    }

    public void setReadLocalInsurance(boolean ReadLocalInsurance) {
        this.ReadLocalInsurance = ReadLocalInsurance;
    }

    public boolean isSendSMS() {
        return SendSMS;
    }

    public void setSendSMS(boolean SendSMS) {
        this.SendSMS = SendSMS;
    }

    public BatteryTHEFTNOBean getBatteryTHEFTNO() {
        return BatteryTHEFTNO;
    }

    public void setBatteryTHEFTNO(BatteryTHEFTNOBean BatteryTHEFTNO) {
        this.BatteryTHEFTNO = BatteryTHEFTNO;
    }

    public List<SourceBean> getSource() {
        return Source;
    }

    public void setSource(List<SourceBean> Source) {
        this.Source = Source;
    }

    public List<SignTypeInfo> getSignType() {
        return SignType;
    }

    public void setSignType(List<SignTypeInfo> SignType) {
        this.SignType = SignType;
    }

    public List<?> getPhotoConfig() {
        return PhotoConfig;
    }

    public void setPhotoConfig(List<?> PhotoConfig) {
        this.PhotoConfig = PhotoConfig;
    }

    public static class MnnicipalBean {
        /**
         * Name : 温州市公安局
         * Id : F591BF0D-DCA1-114F-1E04-0007F01007DF
         * No : 530101
         * unittype : 0
         * CityName : 温州2016测试
         */

        private String Name;
        private String Id;
        private String No;
        private int unittype;
        private String CityName;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getNo() {
            return No;
        }

        public void setNo(String No) {
            this.No = No;
        }

        public int getUnittype() {
            return unittype;
        }

        public void setUnittype(int unittype) {
            this.unittype = unittype;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String CityName) {
            this.CityName = CityName;
        }
    }

    public static class VehiclePlateNumberRegularBean {
        /**
         * 1 : {"des":"电动车","reg":"(^(ZL|LW|OH|RA|YQ|DT|YJ|PY|CN|CH|TS|JK|LR|LC)\\d{6}$)","info":"车牌格式：1234567"}
         * 2 : {"des":"助力车","reg":"(^\\d{7}$)|(^\\d{7}\\+\\d{7}$)","info":"车牌格式：1234567"}
         * 3 : {"des":"摩托车","reg":"^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$",
         * "info":"车牌格式：云A12345"}
         * 4 : {"des":"警车","reg":"^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$",
         * "info":"车牌格式：云A12345"}
         * 5 : {"des":"三轮车","reg":"(^\\d{7}$)|(^\\d{7}\\+\\d{7}$)","info":"车牌格式：1234567"}
         */

        @SerializedName("1")
        private _$1Bean _$1;
        @SerializedName("2")
        private _$2Bean _$2;
        @SerializedName("3")
        private _$3Bean _$3;
        @SerializedName("4")
        private _$4Bean _$4;
        @SerializedName("5")
        private _$5Bean _$5;

        public _$1Bean get_$1() {
            return _$1;
        }

        public void set_$1(_$1Bean _$1) {
            this._$1 = _$1;
        }

        public _$2Bean get_$2() {
            return _$2;
        }

        public void set_$2(_$2Bean _$2) {
            this._$2 = _$2;
        }

        public _$3Bean get_$3() {
            return _$3;
        }

        public void set_$3(_$3Bean _$3) {
            this._$3 = _$3;
        }

        public _$4Bean get_$4() {
            return _$4;
        }

        public void set_$4(_$4Bean _$4) {
            this._$4 = _$4;
        }

        public _$5Bean get_$5() {
            return _$5;
        }

        public void set_$5(_$5Bean _$5) {
            this._$5 = _$5;
        }

        public static class _$1Bean {
            /**
             * des : 电动车
             * reg : (^(ZL|LW|OH|RA|YQ|DT|YJ|PY|CN|CH|TS|JK|LR|LC)\d{6}$)
             * info : 车牌格式：1234567
             */

            private String des;
            private String reg;
            private String info;

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getReg() {
                return reg;
            }

            public void setReg(String reg) {
                this.reg = reg;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }

        public static class _$2Bean {
            /**
             * des : 助力车
             * reg : (^\d{7}$)|(^\d{7}\+\d{7}$)
             * info : 车牌格式：1234567
             */

            private String des;
            private String reg;
            private String info;

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getReg() {
                return reg;
            }

            public void setReg(String reg) {
                this.reg = reg;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }

        public static class _$3Bean {
            /**
             * des : 摩托车
             * reg : ^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\u4e00-\u9fa5]$
             * info : 车牌格式：云A12345
             */

            private String des;
            private String reg;
            private String info;

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getReg() {
                return reg;
            }

            public void setReg(String reg) {
                this.reg = reg;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }

        public static class _$4Bean {
            /**
             * des : 警车
             * reg : ^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\u4e00-\u9fa5]$
             * info : 车牌格式：云A12345
             */

            private String des;
            private String reg;
            private String info;

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getReg() {
                return reg;
            }

            public void setReg(String reg) {
                this.reg = reg;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }

        public static class _$5Bean {
            /**
             * des : 三轮车
             * reg : (^\d{7}$)|(^\d{7}\+\d{7}$)
             * info : 车牌格式：1234567
             */

            private String des;
            private String reg;
            private String info;

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getReg() {
                return reg;
            }

            public void setReg(String reg) {
                this.reg = reg;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }
    }

    public static class SelectBean {
        private List<FeedbackTypeBean> FeedbackType;
        private List<WithoutFeedbackTypeBean> WithoutFeedbackType;
        private List<VehicletypeBean> vehicletype;

        public List<FeedbackTypeBean> getFeedbackType() {
            return FeedbackType;
        }

        public void setFeedbackType(List<FeedbackTypeBean> FeedbackType) {
            this.FeedbackType = FeedbackType;
        }

        public List<WithoutFeedbackTypeBean> getWithoutFeedbackType() {
            return WithoutFeedbackType;
        }

        public void setWithoutFeedbackType(List<WithoutFeedbackTypeBean> WithoutFeedbackType) {
            this.WithoutFeedbackType = WithoutFeedbackType;
        }

        public List<VehicletypeBean> getVehicletype() {
            return vehicletype;
        }

        public void setVehicletype(List<VehicletypeBean> vehicletype) {
            this.vehicletype = vehicletype;
        }

        public static class FeedbackTypeBean {
            /**
             * name : 人车并获
             * value : 3
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class WithoutFeedbackTypeBean {
            /**
             * name : 人车并获
             * value : 3
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class VehicletypeBean {
            /**
             * name : 电动车
             * value : 1
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public static class LangBean {
        /**
         * vehicle : 电动车
         * address : 现住址
         * isblack :
         * cannotdeploy :
         * NonePermission : 车辆未布控,请到车辆监控栏目申请查看轨迹
         * UndueString : 该防盗设备到期时间{0}年{1}月{2}日，请注意提前到附近派出所更换防盗设备
         * ExpireString : 该防盗设备已过期，请到附近派出所更换防盗设备
         */

        private String vehicle;
        private String address;
        private String isblack;
        private String cannotdeploy;
        private String NonePermission;
        private String UndueString;
        private String ExpireString;

        public String getVehicle() {
            return vehicle;
        }

        public void setVehicle(String vehicle) {
            this.vehicle = vehicle;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIsblack() {
            return isblack;
        }

        public void setIsblack(String isblack) {
            this.isblack = isblack;
        }

        public String getCannotdeploy() {
            return cannotdeploy;
        }

        public void setCannotdeploy(String cannotdeploy) {
            this.cannotdeploy = cannotdeploy;
        }

        public String getNonePermission() {
            return NonePermission;
        }

        public void setNonePermission(String NonePermission) {
            this.NonePermission = NonePermission;
        }

        public String getUndueString() {
            return UndueString;
        }

        public void setUndueString(String UndueString) {
            this.UndueString = UndueString;
        }

        public String getExpireString() {
            return ExpireString;
        }

        public void setExpireString(String ExpireString) {
            this.ExpireString = ExpireString;
        }
    }

    public static class InsSysConfigBean {
        /**
         * EnableSynergy : false
         * CrossFeedback : true
         * InsSysDUUnits :
         * DeployAutoIns : 7
         * EnableOffset : true
         * OffsetLNG : 0
         * OffsetLAT : -9.0E-5
         */

        private boolean EnableSynergy;
        private boolean CrossFeedback;
        private String InsSysDUUnits;
        private int DeployAutoIns;
        private boolean EnableOffset;
        private int OffsetLNG;
        private double OffsetLAT;

        public boolean isEnableSynergy() {
            return EnableSynergy;
        }

        public void setEnableSynergy(boolean EnableSynergy) {
            this.EnableSynergy = EnableSynergy;
        }

        public boolean isCrossFeedback() {
            return CrossFeedback;
        }

        public void setCrossFeedback(boolean CrossFeedback) {
            this.CrossFeedback = CrossFeedback;
        }

        public String getInsSysDUUnits() {
            return InsSysDUUnits;
        }

        public void setInsSysDUUnits(String InsSysDUUnits) {
            this.InsSysDUUnits = InsSysDUUnits;
        }

        public int getDeployAutoIns() {
            return DeployAutoIns;
        }

        public void setDeployAutoIns(int DeployAutoIns) {
            this.DeployAutoIns = DeployAutoIns;
        }

        public boolean isEnableOffset() {
            return EnableOffset;
        }

        public void setEnableOffset(boolean EnableOffset) {
            this.EnableOffset = EnableOffset;
        }

        public int getOffsetLNG() {
            return OffsetLNG;
        }

        public void setOffsetLNG(int OffsetLNG) {
            this.OffsetLNG = OffsetLNG;
        }

        public double getOffsetLAT() {
            return OffsetLAT;
        }

        public void setOffsetLAT(double OffsetLAT) {
            this.OffsetLAT = OffsetLAT;
        }
    }

    public static class LoginBean {
        /**
         * closeAppLogin : false
         */

        private boolean closeAppLogin;

        public boolean isCloseAppLogin() {
            return closeAppLogin;
        }

        public void setCloseAppLogin(boolean closeAppLogin) {
            this.closeAppLogin = closeAppLogin;
        }
    }

    public static class VehicleBean {
        /**
         * ManyVehicleTypeOfDifferentPolicy : true
         * OpenStockManage : false
         * TheftnoConvert : true
         * agent : true
         * WarrantyDay : 365
         * WarrantyString : null
         * UnWarrantyString : null
         */

        private boolean ManyVehicleTypeOfDifferentPolicy;
        private boolean OpenStockManage;
        private boolean TheftnoConvert;
        private boolean agent;
        private int WarrantyDay;
        private Object WarrantyString;
        private Object UnWarrantyString;

        public boolean isManyVehicleTypeOfDifferentPolicy() {
            return ManyVehicleTypeOfDifferentPolicy;
        }

        public void setManyVehicleTypeOfDifferentPolicy(boolean ManyVehicleTypeOfDifferentPolicy) {
            this.ManyVehicleTypeOfDifferentPolicy = ManyVehicleTypeOfDifferentPolicy;
        }

        public boolean isOpenStockManage() {
            return OpenStockManage;
        }

        public void setOpenStockManage(boolean OpenStockManage) {
            this.OpenStockManage = OpenStockManage;
        }

        public boolean isTheftnoConvert() {
            return TheftnoConvert;
        }

        public void setTheftnoConvert(boolean TheftnoConvert) {
            this.TheftnoConvert = TheftnoConvert;
        }

        public boolean isAgent() {
            return agent;
        }

        public void setAgent(boolean agent) {
            this.agent = agent;
        }

        public int getWarrantyDay() {
            return WarrantyDay;
        }

        public void setWarrantyDay(int WarrantyDay) {
            this.WarrantyDay = WarrantyDay;
        }

        public Object getWarrantyString() {
            return WarrantyString;
        }

        public void setWarrantyString(Object WarrantyString) {
            this.WarrantyString = WarrantyString;
        }

        public Object getUnWarrantyString() {
            return UnWarrantyString;
        }

        public void setUnWarrantyString(Object UnWarrantyString) {
            this.UnWarrantyString = UnWarrantyString;
        }
    }

    public static class BatteryTHEFTNOBean {
        /**
         * Number : 3
         * Field : THEFTNO3
         * Name : 温州溯源标签
         * Value : 32804
         * EqType : 8024
         * Regular : ^$|^(8024)\d{10}$
         * Alias : 2.4G
         * VehicleType : 1
         * isValid : true
         */

        private int Number;
        private String Field;
        private String Name;
        private String Value;
        private String EqType;
        private String Regular;
        private String Alias;
        private int VehicleType;
        private boolean isValid;

        public int getNumber() {
            return Number;
        }

        public void setNumber(int Number) {
            this.Number = Number;
        }

        public String getField() {
            return Field;
        }

        public void setField(String Field) {
            this.Field = Field;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }

        public String getEqType() {
            return EqType;
        }

        public void setEqType(String EqType) {
            this.EqType = EqType;
        }

        public String getRegular() {
            return Regular;
        }

        public void setRegular(String Regular) {
            this.Regular = Regular;
        }

        public String getAlias() {
            return Alias;
        }

        public void setAlias(String Alias) {
            this.Alias = Alias;
        }

        public int getVehicleType() {
            return VehicleType;
        }

        public void setVehicleType(int VehicleType) {
            this.VehicleType = VehicleType;
        }

        public boolean isIsValid() {
            return isValid;
        }

        public void setIsValid(boolean isValid) {
            this.isValid = isValid;
        }
    }

    public static class SourceBean {
        /**
         * name : 110报案
         * value : 1
         */

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}

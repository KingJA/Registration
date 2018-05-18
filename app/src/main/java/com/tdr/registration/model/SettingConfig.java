package com.tdr.registration.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description:TODO
 * Create Time:2018/5/18 9:36
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class SettingConfig {

    /**
     * mnnicipal : {"name":"天津市公安局","id":"441349BE-E2FD-B124-8E05-36901A8C0632","no":"1201","unittype":0,
     * "CityName":"天津"}
     * SystemInfo : {"version":"1.0.8.1","title":"登录","copyright":"©浙江天地人科技有限公司版权所有",
     * "project":"天津市公安局物联网电动自行车登记上牌防盗备案系统","system":[{"name":"车辆查缉系统","path":"/Home/InsIndex2","isValid":true},
     * {"name":"车辆监控查询","path":"/md/m/index","isValid":false}]}
     * plateNumberRegular : ^\d{7}$
     * vehicleNumberRegular : ^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\u4e00-\u9fa5]$
     * VehiclePlateNumberRegular : {"1":{"des":"电动车","reg":"^\\d{7}$","info":"车牌格式：1234567"},"2":{"des":"助力车","reg":"
     * (^\\d{7}$)|(^\\d{7}\\+\\d{7}$)","info":"车牌格式：1234567"},"3":{"des":"摩托车",
     * "reg":"^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$","info":"车牌格式：津A12345"},
     * "4":{"des":"警车","reg":"^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$",
     * "info":"车牌格式：云A12345"},"5":{"des":"三轮车","reg":"(^\\d{7}$)|(^\\d{7}\\+\\d{7}$)","info":"车牌格式：1234567"}}
     * plateformat : 车牌格式：1234567
     * vehiclePlateformat : 车牌格式：0123456
     * hasPlateNumber : false
     * paymentWay : abchina
     * openRenewal : false
     * SpecialRole :
     * isShowBlackCarTrack : true
     * readMapDataType : BaiDu
     * isShowCarDeployApplyRecord : false
     * LimitLoginType : false
     * AllowEditLeftDay : 7
     * ReadLocalInsurance : false
     * HideSensitive : false
     * login : {"closeAppLogin":false,"isQrcode":true,"IsSzzS":false,"SzzsUrl":"/Home/SzzS"}
     * lang : {"vehicle":"电动车","address":"现住址","isblack":"","caseUnit":"案发区域","caseAddress":"案发地址","cannotdeploy":"",
     * "NonePermission":"车辆未布控,请到车辆监控栏目申请查看轨迹"}
     * pathMap : {"mobileStationPath":true}
     * vehicle : {"ManyVehicleTypeOfDifferentPolicy":false,"vehicleType":false,"carType":true,"IsShowCommit":false,
     * "hasPlateType":false,"CanPolicyEdit":true,"isHidePolicyModel":false,"agent":false,"multiplePicture":true,
     * "ChangeType":[{"name":"更换车牌","value":"1"},{"name":"更换车牌","value":"1"},{"name":"更换车辆标签","value":"2"},
     * {"name":"更换电池标签","value":"4"}],"PhotoConfig":[{"INDEX":"1","REMARK":"前侧车身照","IsValid":true,"IsRequire":true},
     * {"INDEX":"2","REMARK":"电机号、车架号的查验表","IsValid":true,"IsRequire":true},{"INDEX":"3","REMARK":"备案登记表",
     * "IsValid":true,"IsRequire":true},{"INDEX":"4","REMARK":"后侧车身照","IsValid":true,"IsRequire":true},{"INDEX":"5",
     * "REMARK":"标签安装位置","IsValid":true,"IsRequire":true}],"ShowInvoice":true,"ReadPreregister":true,
     * "OpenStockManage":false,"TheftnoConvert":true,"FreeChange":false,"CheckBlack_before":0,"CheckBlack_after":0,
     * "WarrantyDay":365,"WarrantyString":null,"UnWarrantyString":null}
     * deploy : {"otherDeploy":false}
     * detain : {"isToFormal":true,"isReturn":false}
     * register : {"uploadPicture":true,"IsRegisterPrint":false,"except":["4","7"],"palteNumberPrefix":"",
     * "PreRegisterPhotoConfig":[{"INDEX":"1","REMARK":"前车身照","IsValid":true,"IsRequire":true},{"INDEX":"4",
     * "REMARK":"后车身照","IsValid":true,"IsRequire":true},{"INDEX":"3","REMARK":"申请表","IsValid":true,"IsRequire":true},
     * {"INDEX":"2","REMARK":"电机号、车架号的查验表","IsValid":true,"IsRequire":true}]}
     * Select : {"INSURERTYPE":[{"IsValid":true,"name":"中华联合财产保险","value":"2"}],"FeedbackType":[{"name":"人车并获",
     * "value":"3"},{"name":"查获车辆","value":"4"},{"name":"查获标签","value":"5"},{"name":"未查获车辆","value":"6"},
     * {"name":"协助查获","value":"8"}],"WithoutFeedbackType":[{"name":"人车并获","value":"3"},{"name":"查获车辆","value":"4"},
     * {"name":"查获标签","value":"5"},{"name":"协助查获","value":"8"}],"vehicletype":[{"name":"电动车","value":"1",
     * "childVehicleType":null},{"name":"摩托车","value":"3","childVehicleType":null}]}
     * hasPolicy : true
     * mapType : BaiDu
     * Picture : 8
     * LimitBlack : false
     * LimitIOS : false
     * Source : [{"name":"110报案","value":"1"},{"name":"派出所报案","value":"2"},{"name":"警综系统","value":"4"}]
     * VehicletypeSelect : null
     * EqtypeModule : 2
     * SignType : [{"Number":1,"Field":"THEFTNO","Name":"车辆标签","Value":"32803","Alias":"920","EqType":"8023",
     * "Regular":null,"VehicleType":"1","isValid":true}]
     * InsSysConfig : {"EnableSynergy":false,"CrossFeedback":true,"InsSysDUUnits":"","DeployAutoIns":6,
     * "EnableOffset":true,"OffsetLNG":0,"OffsetLAT":-9.0E-5}
     * UnitClass : null
     * InsuranceFromUnit : false
     * RegistrationSwitch : false
     * WhiteListApp : 0
     * WhiteListWeb : 0
     * WhiteListUrl : null
     * HasPreregister : 0
     * IsScanLabel : 0
     * IsScanCard : 0
     * ANDROID_MIN : null
     * ANDROID_MAX : null
     * ANDROIDINSSYS_MIN : null
     * ANDROIDINSSYS_MAX : null
     * IOS_MIN : null
     * EnableInvoice : 0
     * ChangeType : null
     * THEFTNO1_REGULAR : null
     * THEFTNO2_REGULAR : null
     * IsDoubleSign : 0
     * IsConfirm : 0
     * HasAgent : 0
     * IsScanDjh : 0
     * IsScanCjh : 0
     * ShowQR : 0
     */

    private MnnicipalBean mnnicipal;
    private SystemInfoBean SystemInfo;
    private String plateNumberRegular;
    private String vehicleNumberRegular;
    private VehiclePlateNumberRegularBean VehiclePlateNumberRegular;
    private String plateformat;
    private String vehiclePlateformat;
    private boolean hasPlateNumber;
    private String paymentWay;
    private boolean openRenewal;
    private String SpecialRole;
    private boolean isShowBlackCarTrack;
    private String readMapDataType;
    private boolean isShowCarDeployApplyRecord;
    private boolean LimitLoginType;
    private int AllowEditLeftDay;
    private boolean ReadLocalInsurance;
    private boolean HideSensitive;
    private LoginBean login;
    private LangBean lang;
    private PathMapBean pathMap;
    private VehicleBean vehicle;
    private DeployBean deploy;
    private DetainBean detain;
    private RegisterBean register;
    private SelectBean Select;
    private boolean hasPolicy;
    private String mapType;
    private int Picture;
    private boolean LimitBlack;
    private boolean LimitIOS;
    private Object VehicletypeSelect;
    private int EqtypeModule;
    private InsSysConfigBean InsSysConfig;
    private Object UnitClass;
    private boolean InsuranceFromUnit;
    private boolean RegistrationSwitch;
    private int WhiteListApp;
    private int WhiteListWeb;
    private Object WhiteListUrl;
    private int HasPreregister;
    private int IsScanLabel;
    private int IsScanCard;
    private Object ANDROID_MIN;
    private Object ANDROID_MAX;
    private Object ANDROIDINSSYS_MIN;
    private Object ANDROIDINSSYS_MAX;
    private Object IOS_MIN;
    private int EnableInvoice;
    private Object ChangeType;
    private Object THEFTNO1_REGULAR;
    private Object THEFTNO2_REGULAR;
    private int IsDoubleSign;
    private int IsConfirm;
    private int HasAgent;
    private int IsScanDjh;
    private int IsScanCjh;
    private int ShowQR;
    private List<SourceBean> Source;
    private List<SignTypeBean> SignType;

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

    private String EngineNoRegular;
    private String ShelvesNoRegular;
    public MnnicipalBean getMnnicipal() {
        return mnnicipal;
    }

    public void setMnnicipal(MnnicipalBean mnnicipal) {
        this.mnnicipal = mnnicipal;
    }

    public SystemInfoBean getSystemInfo() {
        return SystemInfo;
    }

    public void setSystemInfo(SystemInfoBean SystemInfo) {
        this.SystemInfo = SystemInfo;
    }

    public String getPlateNumberRegular() {
        return plateNumberRegular;
    }

    public void setPlateNumberRegular(String plateNumberRegular) {
        this.plateNumberRegular = plateNumberRegular;
    }

    public String getVehicleNumberRegular() {
        return vehicleNumberRegular;
    }

    public void setVehicleNumberRegular(String vehicleNumberRegular) {
        this.vehicleNumberRegular = vehicleNumberRegular;
    }

    public VehiclePlateNumberRegularBean getVehiclePlateNumberRegular() {
        return VehiclePlateNumberRegular;
    }

    public void setVehiclePlateNumberRegular(VehiclePlateNumberRegularBean VehiclePlateNumberRegular) {
        this.VehiclePlateNumberRegular = VehiclePlateNumberRegular;
    }

    public String getPlateformat() {
        return plateformat;
    }

    public void setPlateformat(String plateformat) {
        this.plateformat = plateformat;
    }

    public String getVehiclePlateformat() {
        return vehiclePlateformat;
    }

    public void setVehiclePlateformat(String vehiclePlateformat) {
        this.vehiclePlateformat = vehiclePlateformat;
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

    public boolean isOpenRenewal() {
        return openRenewal;
    }

    public void setOpenRenewal(boolean openRenewal) {
        this.openRenewal = openRenewal;
    }

    public String getSpecialRole() {
        return SpecialRole;
    }

    public void setSpecialRole(String SpecialRole) {
        this.SpecialRole = SpecialRole;
    }

    public boolean isIsShowBlackCarTrack() {
        return isShowBlackCarTrack;
    }

    public void setIsShowBlackCarTrack(boolean isShowBlackCarTrack) {
        this.isShowBlackCarTrack = isShowBlackCarTrack;
    }

    public String getReadMapDataType() {
        return readMapDataType;
    }

    public void setReadMapDataType(String readMapDataType) {
        this.readMapDataType = readMapDataType;
    }

    public boolean isIsShowCarDeployApplyRecord() {
        return isShowCarDeployApplyRecord;
    }

    public void setIsShowCarDeployApplyRecord(boolean isShowCarDeployApplyRecord) {
        this.isShowCarDeployApplyRecord = isShowCarDeployApplyRecord;
    }

    public boolean isLimitLoginType() {
        return LimitLoginType;
    }

    public void setLimitLoginType(boolean LimitLoginType) {
        this.LimitLoginType = LimitLoginType;
    }

    public int getAllowEditLeftDay() {
        return AllowEditLeftDay;
    }

    public void setAllowEditLeftDay(int AllowEditLeftDay) {
        this.AllowEditLeftDay = AllowEditLeftDay;
    }

    public boolean isReadLocalInsurance() {
        return ReadLocalInsurance;
    }

    public void setReadLocalInsurance(boolean ReadLocalInsurance) {
        this.ReadLocalInsurance = ReadLocalInsurance;
    }

    public boolean isHideSensitive() {
        return HideSensitive;
    }

    public void setHideSensitive(boolean HideSensitive) {
        this.HideSensitive = HideSensitive;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public LangBean getLang() {
        return lang;
    }

    public void setLang(LangBean lang) {
        this.lang = lang;
    }

    public PathMapBean getPathMap() {
        return pathMap;
    }

    public void setPathMap(PathMapBean pathMap) {
        this.pathMap = pathMap;
    }

    public VehicleBean getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleBean vehicle) {
        this.vehicle = vehicle;
    }

    public DeployBean getDeploy() {
        return deploy;
    }

    public void setDeploy(DeployBean deploy) {
        this.deploy = deploy;
    }

    public DetainBean getDetain() {
        return detain;
    }

    public void setDetain(DetainBean detain) {
        this.detain = detain;
    }

    public RegisterBean getRegister() {
        return register;
    }

    public void setRegister(RegisterBean register) {
        this.register = register;
    }

    public SelectBean getSelect() {
        return Select;
    }

    public void setSelect(SelectBean Select) {
        this.Select = Select;
    }

    public boolean isHasPolicy() {
        return hasPolicy;
    }

    public void setHasPolicy(boolean hasPolicy) {
        this.hasPolicy = hasPolicy;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public int getPicture() {
        return Picture;
    }

    public void setPicture(int Picture) {
        this.Picture = Picture;
    }

    public boolean isLimitBlack() {
        return LimitBlack;
    }

    public void setLimitBlack(boolean LimitBlack) {
        this.LimitBlack = LimitBlack;
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

    public int getEqtypeModule() {
        return EqtypeModule;
    }

    public void setEqtypeModule(int EqtypeModule) {
        this.EqtypeModule = EqtypeModule;
    }

    public InsSysConfigBean getInsSysConfig() {
        return InsSysConfig;
    }

    public void setInsSysConfig(InsSysConfigBean InsSysConfig) {
        this.InsSysConfig = InsSysConfig;
    }

    public Object getUnitClass() {
        return UnitClass;
    }

    public void setUnitClass(Object UnitClass) {
        this.UnitClass = UnitClass;
    }

    public boolean isInsuranceFromUnit() {
        return InsuranceFromUnit;
    }

    public void setInsuranceFromUnit(boolean InsuranceFromUnit) {
        this.InsuranceFromUnit = InsuranceFromUnit;
    }

    public boolean isRegistrationSwitch() {
        return RegistrationSwitch;
    }

    public void setRegistrationSwitch(boolean RegistrationSwitch) {
        this.RegistrationSwitch = RegistrationSwitch;
    }

    public int getWhiteListApp() {
        return WhiteListApp;
    }

    public void setWhiteListApp(int WhiteListApp) {
        this.WhiteListApp = WhiteListApp;
    }

    public int getWhiteListWeb() {
        return WhiteListWeb;
    }

    public void setWhiteListWeb(int WhiteListWeb) {
        this.WhiteListWeb = WhiteListWeb;
    }

    public Object getWhiteListUrl() {
        return WhiteListUrl;
    }

    public void setWhiteListUrl(Object WhiteListUrl) {
        this.WhiteListUrl = WhiteListUrl;
    }

    public int getHasPreregister() {
        return HasPreregister;
    }

    public void setHasPreregister(int HasPreregister) {
        this.HasPreregister = HasPreregister;
    }

    public int getIsScanLabel() {
        return IsScanLabel;
    }

    public void setIsScanLabel(int IsScanLabel) {
        this.IsScanLabel = IsScanLabel;
    }

    public int getIsScanCard() {
        return IsScanCard;
    }

    public void setIsScanCard(int IsScanCard) {
        this.IsScanCard = IsScanCard;
    }

    public Object getANDROID_MIN() {
        return ANDROID_MIN;
    }

    public void setANDROID_MIN(Object ANDROID_MIN) {
        this.ANDROID_MIN = ANDROID_MIN;
    }

    public Object getANDROID_MAX() {
        return ANDROID_MAX;
    }

    public void setANDROID_MAX(Object ANDROID_MAX) {
        this.ANDROID_MAX = ANDROID_MAX;
    }

    public Object getANDROIDINSSYS_MIN() {
        return ANDROIDINSSYS_MIN;
    }

    public void setANDROIDINSSYS_MIN(Object ANDROIDINSSYS_MIN) {
        this.ANDROIDINSSYS_MIN = ANDROIDINSSYS_MIN;
    }

    public Object getANDROIDINSSYS_MAX() {
        return ANDROIDINSSYS_MAX;
    }

    public void setANDROIDINSSYS_MAX(Object ANDROIDINSSYS_MAX) {
        this.ANDROIDINSSYS_MAX = ANDROIDINSSYS_MAX;
    }

    public Object getIOS_MIN() {
        return IOS_MIN;
    }

    public void setIOS_MIN(Object IOS_MIN) {
        this.IOS_MIN = IOS_MIN;
    }

    public int getEnableInvoice() {
        return EnableInvoice;
    }

    public void setEnableInvoice(int EnableInvoice) {
        this.EnableInvoice = EnableInvoice;
    }

    public Object getChangeType() {
        return ChangeType;
    }

    public void setChangeType(Object ChangeType) {
        this.ChangeType = ChangeType;
    }

    public Object getTHEFTNO1_REGULAR() {
        return THEFTNO1_REGULAR;
    }

    public void setTHEFTNO1_REGULAR(Object THEFTNO1_REGULAR) {
        this.THEFTNO1_REGULAR = THEFTNO1_REGULAR;
    }

    public Object getTHEFTNO2_REGULAR() {
        return THEFTNO2_REGULAR;
    }

    public void setTHEFTNO2_REGULAR(Object THEFTNO2_REGULAR) {
        this.THEFTNO2_REGULAR = THEFTNO2_REGULAR;
    }

    public int getIsDoubleSign() {
        return IsDoubleSign;
    }

    public void setIsDoubleSign(int IsDoubleSign) {
        this.IsDoubleSign = IsDoubleSign;
    }

    public int getIsConfirm() {
        return IsConfirm;
    }

    public void setIsConfirm(int IsConfirm) {
        this.IsConfirm = IsConfirm;
    }

    public int getHasAgent() {
        return HasAgent;
    }

    public void setHasAgent(int HasAgent) {
        this.HasAgent = HasAgent;
    }

    public int getIsScanDjh() {
        return IsScanDjh;
    }

    public void setIsScanDjh(int IsScanDjh) {
        this.IsScanDjh = IsScanDjh;
    }

    public int getIsScanCjh() {
        return IsScanCjh;
    }

    public void setIsScanCjh(int IsScanCjh) {
        this.IsScanCjh = IsScanCjh;
    }

    public int getShowQR() {
        return ShowQR;
    }

    public void setShowQR(int ShowQR) {
        this.ShowQR = ShowQR;
    }

    public List<SourceBean> getSource() {
        return Source;
    }

    public void setSource(List<SourceBean> Source) {
        this.Source = Source;
    }

    public List<SignTypeBean> getSignType() {
        return SignType;
    }

    public void setSignType(List<SignTypeBean> SignType) {
        this.SignType = SignType;
    }

    public static class MnnicipalBean {
        /**
         * name : 天津市公安局
         * id : 441349BE-E2FD-B124-8E05-36901A8C0632
         * no : 1201
         * unittype : 0
         * CityName : 天津
         */

        private String name;
        private String id;
        private String no;
        private int unittype;
        private String CityName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
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

    public static class SystemInfoBean {
        /**
         * version : 1.0.8.1
         * title : 登录
         * copyright : ©浙江天地人科技有限公司版权所有
         * project : 天津市公安局物联网电动自行车登记上牌防盗备案系统
         * system : [{"name":"车辆查缉系统","path":"/Home/InsIndex2","isValid":true},{"name":"车辆监控查询","path":"/md/m/index",
         * "isValid":false}]
         */

        private String version;
        private String title;
        private String copyright;
        private String project;
        private List<SystemBean> system;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public List<SystemBean> getSystem() {
            return system;
        }

        public void setSystem(List<SystemBean> system) {
            this.system = system;
        }

        public static class SystemBean {
            /**
             * name : 车辆查缉系统
             * path : /Home/InsIndex2
             * isValid : true
             */

            private String name;
            private String path;
            private boolean isValid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public boolean isIsValid() {
                return isValid;
            }

            public void setIsValid(boolean isValid) {
                this.isValid = isValid;
            }
        }
    }

    public static class VehiclePlateNumberRegularBean {
        /**
         * 1 : {"des":"电动车","reg":"^\\d{7}$","info":"车牌格式：1234567"}
         * 2 : {"des":"助力车","reg":"(^\\d{7}$)|(^\\d{7}\\+\\d{7}$)","info":"车牌格式：1234567"}
         * 3 : {"des":"摩托车","reg":"^[\\u4e00-\\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\\u4e00-\\u9fa5]$",
         * "info":"车牌格式：津A12345"}
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
             * reg : ^\d{7}$
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
             * info : 车牌格式：津A12345
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

    public static class LoginBean {
        /**
         * closeAppLogin : false
         * isQrcode : true
         * IsSzzS : false
         * SzzsUrl : /Home/SzzS
         */

        private boolean closeAppLogin;
        private boolean isQrcode;
        private boolean IsSzzS;
        private String SzzsUrl;

        public boolean isCloseAppLogin() {
            return closeAppLogin;
        }

        public void setCloseAppLogin(boolean closeAppLogin) {
            this.closeAppLogin = closeAppLogin;
        }

        public boolean isIsQrcode() {
            return isQrcode;
        }

        public void setIsQrcode(boolean isQrcode) {
            this.isQrcode = isQrcode;
        }

        public boolean isIsSzzS() {
            return IsSzzS;
        }

        public void setIsSzzS(boolean IsSzzS) {
            this.IsSzzS = IsSzzS;
        }

        public String getSzzsUrl() {
            return SzzsUrl;
        }

        public void setSzzsUrl(String SzzsUrl) {
            this.SzzsUrl = SzzsUrl;
        }
    }

    public static class LangBean {
        /**
         * vehicle : 电动车
         * address : 现住址
         * isblack :
         * caseUnit : 案发区域
         * caseAddress : 案发地址
         * cannotdeploy :
         * NonePermission : 车辆未布控,请到车辆监控栏目申请查看轨迹
         */

        private String vehicle;
        private String address;
        private String isblack;
        private String caseUnit;
        private String caseAddress;
        private String cannotdeploy;
        private String NonePermission;

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

        public String getCaseUnit() {
            return caseUnit;
        }

        public void setCaseUnit(String caseUnit) {
            this.caseUnit = caseUnit;
        }

        public String getCaseAddress() {
            return caseAddress;
        }

        public void setCaseAddress(String caseAddress) {
            this.caseAddress = caseAddress;
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
    }

    public static class PathMapBean {
        /**
         * mobileStationPath : true
         */

        private boolean mobileStationPath;

        public boolean isMobileStationPath() {
            return mobileStationPath;
        }

        public void setMobileStationPath(boolean mobileStationPath) {
            this.mobileStationPath = mobileStationPath;
        }
    }

    public static class VehicleBean {
        /**
         * ManyVehicleTypeOfDifferentPolicy : false
         * vehicleType : false
         * carType : true
         * IsShowCommit : false
         * hasPlateType : false
         * CanPolicyEdit : true
         * isHidePolicyModel : false
         * agent : false
         * multiplePicture : true
         * ChangeType : [{"name":"更换车牌","value":"1"},{"name":"更换车牌","value":"1"},{"name":"更换车辆标签","value":"2"},
         * {"name":"更换电池标签","value":"4"}]
         * PhotoConfig : [{"INDEX":"1","REMARK":"前侧车身照","IsValid":true,"IsRequire":true},{"INDEX":"2",
         * "REMARK":"电机号、车架号的查验表","IsValid":true,"IsRequire":true},{"INDEX":"3","REMARK":"备案登记表","IsValid":true,
         * "IsRequire":true},{"INDEX":"4","REMARK":"后侧车身照","IsValid":true,"IsRequire":true},{"INDEX":"5",
         * "REMARK":"标签安装位置","IsValid":true,"IsRequire":true}]
         * ShowInvoice : true
         * ReadPreregister : true
         * OpenStockManage : false
         * TheftnoConvert : true
         * FreeChange : false
         * CheckBlack_before : 0
         * CheckBlack_after : 0
         * WarrantyDay : 365
         * WarrantyString : null
         * UnWarrantyString : null
         */

        private boolean ManyVehicleTypeOfDifferentPolicy;
        private boolean vehicleType;
        private boolean carType;
        private boolean IsShowCommit;
        private boolean hasPlateType;
        private boolean CanPolicyEdit;
        private boolean isHidePolicyModel;
        private boolean agent;
        private boolean multiplePicture;
        private boolean ShowInvoice;
        private boolean ReadPreregister;
        private boolean OpenStockManage;
        private boolean TheftnoConvert;
        private boolean FreeChange;
        private int CheckBlack_before;
        private int CheckBlack_after;
        private int WarrantyDay;
        private Object WarrantyString;
        private Object UnWarrantyString;
        private List<ChangeTypeBean> ChangeType;
        private List<PhotoConfigBean> PhotoConfig;

        public boolean isManyVehicleTypeOfDifferentPolicy() {
            return ManyVehicleTypeOfDifferentPolicy;
        }

        public void setManyVehicleTypeOfDifferentPolicy(boolean ManyVehicleTypeOfDifferentPolicy) {
            this.ManyVehicleTypeOfDifferentPolicy = ManyVehicleTypeOfDifferentPolicy;
        }

        public boolean isVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(boolean vehicleType) {
            this.vehicleType = vehicleType;
        }

        public boolean isCarType() {
            return carType;
        }

        public void setCarType(boolean carType) {
            this.carType = carType;
        }

        public boolean isIsShowCommit() {
            return IsShowCommit;
        }

        public void setIsShowCommit(boolean IsShowCommit) {
            this.IsShowCommit = IsShowCommit;
        }

        public boolean isHasPlateType() {
            return hasPlateType;
        }

        public void setHasPlateType(boolean hasPlateType) {
            this.hasPlateType = hasPlateType;
        }

        public boolean isCanPolicyEdit() {
            return CanPolicyEdit;
        }

        public void setCanPolicyEdit(boolean CanPolicyEdit) {
            this.CanPolicyEdit = CanPolicyEdit;
        }

        public boolean isIsHidePolicyModel() {
            return isHidePolicyModel;
        }

        public void setIsHidePolicyModel(boolean isHidePolicyModel) {
            this.isHidePolicyModel = isHidePolicyModel;
        }

        public boolean isAgent() {
            return agent;
        }

        public void setAgent(boolean agent) {
            this.agent = agent;
        }

        public boolean isMultiplePicture() {
            return multiplePicture;
        }

        public void setMultiplePicture(boolean multiplePicture) {
            this.multiplePicture = multiplePicture;
        }

        public boolean isShowInvoice() {
            return ShowInvoice;
        }

        public void setShowInvoice(boolean ShowInvoice) {
            this.ShowInvoice = ShowInvoice;
        }

        public boolean isReadPreregister() {
            return ReadPreregister;
        }

        public void setReadPreregister(boolean ReadPreregister) {
            this.ReadPreregister = ReadPreregister;
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

        public boolean isFreeChange() {
            return FreeChange;
        }

        public void setFreeChange(boolean FreeChange) {
            this.FreeChange = FreeChange;
        }

        public int getCheckBlack_before() {
            return CheckBlack_before;
        }

        public void setCheckBlack_before(int CheckBlack_before) {
            this.CheckBlack_before = CheckBlack_before;
        }

        public int getCheckBlack_after() {
            return CheckBlack_after;
        }

        public void setCheckBlack_after(int CheckBlack_after) {
            this.CheckBlack_after = CheckBlack_after;
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

        public List<ChangeTypeBean> getChangeType() {
            return ChangeType;
        }

        public void setChangeType(List<ChangeTypeBean> ChangeType) {
            this.ChangeType = ChangeType;
        }

        public List<PhotoConfigBean> getPhotoConfig() {
            return PhotoConfig;
        }

        public void setPhotoConfig(List<PhotoConfigBean> PhotoConfig) {
            this.PhotoConfig = PhotoConfig;
        }

        public static class ChangeTypeBean {
            /**
             * name : 更换车牌
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

        public static class PhotoConfigBean {
            /**
             * INDEX : 1
             * REMARK : 前侧车身照
             * IsValid : true
             * IsRequire : true
             */

            private String INDEX;
            private String REMARK;
            private boolean IsValid;
            private boolean IsRequire;

            public String getINDEX() {
                return INDEX;
            }

            public void setINDEX(String INDEX) {
                this.INDEX = INDEX;
            }

            public String getREMARK() {
                return REMARK;
            }

            public void setREMARK(String REMARK) {
                this.REMARK = REMARK;
            }

            public boolean isIsValid() {
                return IsValid;
            }

            public void setIsValid(boolean IsValid) {
                this.IsValid = IsValid;
            }

            public boolean isIsRequire() {
                return IsRequire;
            }

            public void setIsRequire(boolean IsRequire) {
                this.IsRequire = IsRequire;
            }
        }
    }

    public static class DeployBean {
        /**
         * otherDeploy : false
         */

        private boolean otherDeploy;

        public boolean isOtherDeploy() {
            return otherDeploy;
        }

        public void setOtherDeploy(boolean otherDeploy) {
            this.otherDeploy = otherDeploy;
        }
    }

    public static class DetainBean {
        /**
         * isToFormal : true
         * isReturn : false
         */

        private boolean isToFormal;
        private boolean isReturn;

        public boolean isIsToFormal() {
            return isToFormal;
        }

        public void setIsToFormal(boolean isToFormal) {
            this.isToFormal = isToFormal;
        }

        public boolean isIsReturn() {
            return isReturn;
        }

        public void setIsReturn(boolean isReturn) {
            this.isReturn = isReturn;
        }
    }

    public static class RegisterBean {
        /**
         * uploadPicture : true
         * IsRegisterPrint : false
         * except : ["4","7"]
         * palteNumberPrefix :
         * PreRegisterPhotoConfig : [{"INDEX":"1","REMARK":"前车身照","IsValid":true,"IsRequire":true},{"INDEX":"4","REMARK":"后车身照","IsValid":true,"IsRequire":true},{"INDEX":"3","REMARK":"申请表","IsValid":true,"IsRequire":true},{"INDEX":"2","REMARK":"电机号、车架号的查验表","IsValid":true,"IsRequire":true}]
         */

        private boolean uploadPicture;
        private boolean IsRegisterPrint;
        private String palteNumberPrefix;
        private List<String> except;
        private List<PreRegisterPhotoConfigBean> PreRegisterPhotoConfig;

        public boolean isUploadPicture() {
            return uploadPicture;
        }

        public void setUploadPicture(boolean uploadPicture) {
            this.uploadPicture = uploadPicture;
        }

        public boolean isIsRegisterPrint() {
            return IsRegisterPrint;
        }

        public void setIsRegisterPrint(boolean IsRegisterPrint) {
            this.IsRegisterPrint = IsRegisterPrint;
        }

        public String getPalteNumberPrefix() {
            return palteNumberPrefix;
        }

        public void setPalteNumberPrefix(String palteNumberPrefix) {
            this.palteNumberPrefix = palteNumberPrefix;
        }

        public List<String> getExcept() {
            return except;
        }

        public void setExcept(List<String> except) {
            this.except = except;
        }

        public List<PreRegisterPhotoConfigBean> getPreRegisterPhotoConfig() {
            return PreRegisterPhotoConfig;
        }

        public void setPreRegisterPhotoConfig(List<PreRegisterPhotoConfigBean> PreRegisterPhotoConfig) {
            this.PreRegisterPhotoConfig = PreRegisterPhotoConfig;
        }

        public static class PreRegisterPhotoConfigBean {
            /**
             * INDEX : 1
             * REMARK : 前车身照
             * IsValid : true
             * IsRequire : true
             */

            private String INDEX;
            private String REMARK;
            private boolean IsValid;
            private boolean IsRequire;

            public String getINDEX() {
                return INDEX;
            }

            public void setINDEX(String INDEX) {
                this.INDEX = INDEX;
            }

            public String getREMARK() {
                return REMARK;
            }

            public void setREMARK(String REMARK) {
                this.REMARK = REMARK;
            }

            public boolean isIsValid() {
                return IsValid;
            }

            public void setIsValid(boolean IsValid) {
                this.IsValid = IsValid;
            }

            public boolean isIsRequire() {
                return IsRequire;
            }

            public void setIsRequire(boolean IsRequire) {
                this.IsRequire = IsRequire;
            }
        }
    }

    public static class SelectBean {
        private List<INSURERTYPEBean> INSURERTYPE;
        private List<FeedbackTypeBean> FeedbackType;
        private List<WithoutFeedbackTypeBean> WithoutFeedbackType;
        private List<VehicletypeBean> vehicletype;

        public List<INSURERTYPEBean> getINSURERTYPE() {
            return INSURERTYPE;
        }

        public void setINSURERTYPE(List<INSURERTYPEBean> INSURERTYPE) {
            this.INSURERTYPE = INSURERTYPE;
        }

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

        public static class INSURERTYPEBean {
            /**
             * IsValid : true
             * name : 中华联合财产保险
             * value : 2
             */

            private boolean IsValid;
            private String name;
            private String value;

            public boolean isIsValid() {
                return IsValid;
            }

            public void setIsValid(boolean IsValid) {
                this.IsValid = IsValid;
            }

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
             * childVehicleType : null
             */

            private String name;
            private String value;
            private Object childVehicleType;

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

            public Object getChildVehicleType() {
                return childVehicleType;
            }

            public void setChildVehicleType(Object childVehicleType) {
                this.childVehicleType = childVehicleType;
            }
        }
    }

    public static class InsSysConfigBean {
        /**
         * EnableSynergy : false
         * CrossFeedback : true
         * InsSysDUUnits :
         * DeployAutoIns : 6
         * EnableOffset : true
         * OffsetLNG : 0.0
         * OffsetLAT : -9.0E-5
         */

        private boolean EnableSynergy;
        private boolean CrossFeedback;
        private String InsSysDUUnits;
        private int DeployAutoIns;
        private boolean EnableOffset;
        private double OffsetLNG;
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

        public double getOffsetLNG() {
            return OffsetLNG;
        }

        public void setOffsetLNG(double OffsetLNG) {
            this.OffsetLNG = OffsetLNG;
        }

        public double getOffsetLAT() {
            return OffsetLAT;
        }

        public void setOffsetLAT(double OffsetLAT) {
            this.OffsetLAT = OffsetLAT;
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

    public static class SignTypeBean {
        /**
         * Number : 1
         * Field : THEFTNO
         * Name : 车辆标签
         * Value : 32803
         * Alias : 920
         * EqType : 8023
         * Regular : null
         * VehicleType : 1
         * isValid : true
         */

        private int Number;
        private String Field;
        private String Name;
        private String Value;
        private String Alias;
        private String EqType;
        private Object Regular;
        private String VehicleType;
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

        public String getAlias() {
            return Alias;
        }

        public void setAlias(String Alias) {
            this.Alias = Alias;
        }

        public String getEqType() {
            return EqType;
        }

        public void setEqType(String EqType) {
            this.EqType = EqType;
        }

        public Object getRegular() {
            return Regular;
        }

        public void setRegular(Object Regular) {
            this.Regular = Regular;
        }

        public String getVehicleType() {
            return VehicleType;
        }

        public void setVehicleType(String VehicleType) {
            this.VehicleType = VehicleType;
        }

        public boolean isIsValid() {
            return isValid;
        }

        public void setIsValid(boolean isValid) {
            this.isValid = isValid;
        }
    }
}

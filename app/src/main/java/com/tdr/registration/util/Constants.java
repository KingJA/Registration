package com.tdr.registration.util;

import com.tdr.registration.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Linus_Xie on 2016/6/20.
 */
public class Constants {

    public static final int HANDLER_KEY_GETVERSION_SUCCESS = 0;
    public static final int HANDLER_KEY_GETVERSION_FAIL = HANDLER_KEY_GETVERSION_SUCCESS + 1;
    /**
     * 请求CODE
     */
    public final static int HANDLER_KEY_CHANNELREQUEST = HANDLER_KEY_GETVERSION_FAIL + 1;
    /**
     * 调整返回的RESULTCODE
     */
    public final static int HANDLER_KEY_CHANNELRESULT = HANDLER_KEY_CHANNELREQUEST + 1;

    // SharedPreferences文件名称
    public static final String APPLICATION_NAME = "appDetails.xml";

    /**
     * Webservice参数
     */
    public static final String WEBSERVER_URL = "https://ykgcapp.iotone.cn/OpenService.asmx";// WebServices访问地址，正式服
//    public static final String WEBSERVER_URL = "http://183.129.130.119:48012/OpenService.asmx";// WebServices访问地址，测试====>外网
//    public static final String WEBSERVER_URL = "http://10.130.0.104:8012/OpenService.asmx";// WebServices访问地址，测试====>测试内网
//    public static final String WEBSERVER_URL = "http://183.129.130.119:58012/OpenService.asmx";// WebServices访问地址，测试====>测试外网
//    public static final String WEBSERVER_URL = "http://10.130.0.104:8071/OpenService.asmx";// WebServices访问地址，测试
//    public static final String WEBSERVER_URL = "http://10.130.0.104:8812/OpenService.asmx";// WebServices访问地址，测试
//    public static final String WEBSERVER_URL = "http://183.129.130.119:52000/OpenService.asmx";// WebServices访问地址，测试
    public static final String WEBSERVER_NAMESPACE = "http://tempuri.org/";// 命名空间
    public static final String WEBSERVER_OPENAPI = "OpenApi";//通用接口
    public static final String WEBSERVER_LOGIN = "Login";//登录接口1.0
    public static final String WEBSERVER_LOGIN_V2 = "Login_V2";//登陆接口2.0
    public static final String WEBSERVER_GETINSURANCECONFIGURE = "GetInsuranceConfigure";//保险相关配置
    public static final String WEBSERVER_GETCODETABLE = "GetCodeTable";//获取统一编码表
    public static final String WEBSERVER_ADDELECTRICCAR = "AddElectricCar";//上传电动车信息(备案登记)
    public static final String WEBSERVER_GETELECTRICCARBYPLATENUMBER2 = "GetElectricCarByPlateNumber2";//获取电动车信息（根据车牌号和身份证号）public static final String WEBSERVER_GETELECTRICCARBYPLATENUMBER2 = "GetElectricCarByPlateNumber2";//获取电动车信息（根据车牌号和身份证号）
    public static final String WEBSERVER_GETELECTRICCARBYPLATENUMBER3 = "GetElectricCarByPlateNumber3";//获取黑车车辆信息
    public static final String WEBSERVER_CHANGEPLATENUMBER = "ChangePlateNumber";// 车牌变更/更换防盗装置
    public static final String WEBSERVER_EDITELECTRICCAR = "EditElectricCar";// 信息变更
    public static final String WEBSERVER_CARSCRAPPED = "CarScrapped";//车辆报废
    public static final String WEBSERVER_ELECTRICCARTRANSFER = "ElectricCarTransfer";//车辆过户
    public static final String WEBSERVER_GETTRAFFICLIST = "GetTrafficList";//获取交警大队列表
    public static final String WEBSERVER_ADDDISTRAINCAR = "AddDistrainCar";//添加扣押记录
    public static final String WEBSERVER_GETDISTRAINCARS = "GetDistrainCars";//获取扣押车辆信息
    public static final String WEBSERVER_GETPICTURE = "GetPicture";//获取图片
    public static final String WEBSERVER_GETXQANDPCS = "GetXqAndPcs";//获取辖区编码表
    public static final String WEBSERVER_GETREGISTRATIONSTATISTICS = "GetRegistrationStatistics";//电动车登记信息统计
    public static final String WEBSERVER_GETREGISTRATIONSTATISTICSBYUSER = "GetRegistrationStatisticsByUser";//电动车用户登记信息统计
    public static final String WEBSERVER_CHECKSTOLENVEHICLE = "CheckStolenVehicle";//核查电动车信息
    public static final String WEBSERVER_DEPLOY = "Deploy";//车辆布控
    public static final String WEBSERVER_CHANGEPWD = "ChangePwd";//修改密码
    public static final String WEBSERVER_INSSYS = "InsSys";//指令系统GetCodeTable
    public static final String WEBSERVER_GETELECTRICCARBYPLATENUMBER = "GetElectricCarByPlateNumber";//获取电动车信息
    public static final String WEBSERVER_EDITELECTRICCARPOLICY = "EditElectricCarPolicy";//编辑电动车保险信息
    public static final String WEBSERVER_GETPREREGISTERS = "GetPreRegisters";//获取预登记信息
    public static final String WEBSERVER_CHECKTHEFTNO = "CheckTHEFTNO";//标签检验
    public static final String WEBSERVER_ADDELECTRICCARFROMDISTRAIN = "AddElectricCarFromDistrain";//电动车扣押转正式信息
    public static final String WEBSERVER_GETPREREGISTERSBYCARDID = "GetPreRegistersByCardId";//获取预登记信息by cardid
    public static final String WEBSERVER_ADDPREREGISTER = "AddPreRegister";//添加预登记
    public static final String WEBSERVER_GETBLACKLIST = "GetBlackList";//黑车下发
    public static final String WEBSERVER_UPLOADVOUCHER = "UploadVoucher";//上传凭证
    public static final String WEBSERVER_GETMONITORDATA = "GetMonitorData";//获取车辆轨迹信息分页（无登录校验，限内网）
    public static final String WEWBSERVER_UPLOADOUTTIMECERT = "UploadOutTimeCERT";//上传“超期未追回电动车”证明
    public static final String WEBSERVER_GETREGISTERSITELIST = "GetRegistersiteList";//获取备案登记点列表(警用app接口)
    public static final String WEBSERVER_GETREGISTER_CONFIGLIST = "GetRegister_ConfigList";//获取预约时间列表
    public static final String WEBSERVER_GETRCPRERATE = "GetRcPreRate";//获取安装点预约列表(警用APP接口)
    public static final String WEBSERVER_GETSERVERTIME = "GetServerTime";
    public static final String WEBSERVER_CHECKTOKEN = "CheckToken";//验证token是否有效

    public static final String WEBSERVER_CARDHOLDER = "CardHolder";//卡包功能

    public static final String WEBSERVER_GETPAYBILLLIST = "GetPayBillList";//获取支付列表

    public static final String WEBSERVER_CHECKVERSION = "CheckVersion";//检查升级

    public static final String WEBSERVER_GETBACKVISITRESULTDIC = "GetBackVisitResultDic";//车辆回访类型
    public static final String WEBSERVER_GETBACKVISITPAGER = "GetBackVisitPager";//车辆回访列表
    public static final String WEBSERVER_GETBACKVISITBYDATEPAGER = "GetBackVisitByDatePager";//回访中心
    public static final String WEBSERVER_ADDBACKVISITRECORD = "AddBackVisitRecord";//车辆回访

    public static final String WEBSERVER_GETCARBYPLATENUMBER = "GetCarByPlateNumber";//保险续费车辆查询
    public static final String WEBSERVER_GETRENEWALINSURANCECONFIGURE = "GetRenewalInsuranceConfigure";//续费保险，新购保险
    public static final String WEBSERVER_INSUREDRENEWAL = "InsuredRenewal";//保险续费提交
    public static final String WEBSERVER_GETPREREGISTERLIST = "GetPreRegisterList";//电信预登记查询
    public static final String WEBSERVER_GETPREREGISTERSTATISTICS = "GetPreRegisterStatistics";//电信预登记统计
    public static final String WEBSERVER_GETBACKVISITCENTER = "GetBackVisitCenter";//回访中心
    public static final String WEBSERVER_GETBACKVISITSTATISTICS = "GetBackVisitStatistics";//回访统计

    public static final String HTTP_GETWALLET  = "/api/Wallet/GetWallet";//钱包
    public static final String HTTP_CWAPPLY  = "/api/Wallet/CWApply";//申请提现
    public static final String HTTP_GETAPPLYLIST  = "/api/Wallet/GetApplyList";//提现明细
    public static final String HTTP_GETBILLRECORDLIST_FORUSER  = "/api/Wallet/GetBillRecordList_ForUser";//订单明细
    public static final String HTTP_GetPhotoUrl = "api/Picture/get?pid=";//照片URL
    public static final String HTTP_GetCarInfo = "api/EquipMap/GetCarInfo";//获取车辆标签情况
    public static final String HTTP_signtype = "api/code2/signtype";//获取车辆标签类型
    public static final String HTTP_Bind = "api/EquipMap/Bind";//绑定/更换标签
    public static final String HTTP_UnBind = "api/EquipMap/UnBind";//标签解绑
    public static final String HTTP_GetModelByPlateNumber = "api/TransferAppointment/GetModelByPlateNumber";//过户预约信息
    public static final String HTTP_GetSetting = "/api/Cache/GetSetting";//获取配置信息
    public static final String HTTP_UploadPicture = "http://10.130.0.104:818/api/Picture/Upload2";//上传图片
    public static final String HTTP_GetPayBill = "api/PayBill/GetPayBill";//获取订单支付状态
    public static final String HTTP_PolicyConfig = "/api/ElectricCar/PolicyConfig";//获取保险信息
    public static final String HTTP_VehicleBoardStatisticsAPP = "/api/Statistics/VehicleBoardStatisticsAPP";//上牌统计
    public static final String HTTP_ElectricPager = "/api/ElectricCar/ElectricPager";//上牌查询
    public static final String HTTP_ToElectricCar = "/api/ElectricCar/ToElectricCar";//免费上牌转备案登记
    public static final String HTTP_GetRecordNo = "/api/ElectricCarBattery/GetRecordNo";//获取备案登记好号
    public static final String HTTP_GetBatteryModel = "/api/ElectricCarBattery/GetBatteryModel";//获取电瓶信息
    public static final String HTTP_AddElectricCarBattery = "/api/ElectricCarBattery/AddElectricCarBattery";//添加电池备案登记
    public static final String HTTP_ChangeBatter = "/api/ElectricCarBattery/ChangeBatter";//更换电池备案登记
    public static final String HTTP_RecoveryBattery = "/api/ElectricCarBattery/RecoveryBattery";//回收电瓶
    public static final String HTTP_BlackCheck = "/api/BlackList/BlackCheck";//黑色检查


    /**
     * Des加密Key
     */
    public static final String DES_KEY = "OPENAPI.";

    /**
     * 蓝牙参数
     */
    public static final String UUID_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR_WRITE = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_NOTIFY_1 = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR_READ = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_DESCRIPTOR = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_DESCRIPTOR_WRITE = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_DESCRIPTOR_READ = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    /**
     * 打印机参数
     */
    public static final String CONNECT_STATUS = "connect.status";
    public static final String ACTION_CONNECT_STATUS = "action.connect.status";

    /**
     * 权限码
     */

    public static final String JURISDICTION_REGISTRATION = "101";//备案登记
    public static final String JURISDICTION_CHANGE_REGISTRATION = "102";//信息变更
    public static final String JURISDICTION_STATISTICS = "4001";//备案登记个人统计
    public static final String JURISDICTION_PRE_REGISTRATION= "2711";//电信预登记
    public static final String JURISDICTION_PRE_REGISTRATION_QUERY= "2712";//电信预登记查询
    public static final String JURISDICTION_PRE_REGISTRATION_STATISTICS= "4002";//电信预登记个人统计
    public static final String JURISDICTION_PRE_REGISTRATION_SHANGPAI_STATISTICS= "4003";//上牌统计
    public static final String JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE= "2713";//免费上牌
    public static final String JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE_QUERY= "2714";//免费上牌查询


    public static final Jurisdiction JURISDICTION_REGISTRATION2 = new Jurisdiction(JURISDICTION_REGISTRATION, R.mipmap.ic_registration,"备案登记");
    public static final Jurisdiction JURISDICTION_CHANGE_REGISTRATION2 = new Jurisdiction(JURISDICTION_CHANGE_REGISTRATION, R.mipmap.ic_infochange,"信息变更");
    public static final Jurisdiction JURISDICTION_STATISTICS2 = new Jurisdiction(JURISDICTION_STATISTICS, R.mipmap.ic_personstatistics,"个人统计");
    public static final Jurisdiction JURISDICTION_PRE_REGISTRATION2 = new Jurisdiction(JURISDICTION_PRE_REGISTRATION, R.mipmap.pre_registration,"预登记");
    public static final Jurisdiction JURISDICTION_PRE_REGISTRATION_QUERY2 = new Jurisdiction(JURISDICTION_PRE_REGISTRATION_QUERY, R.mipmap.pre_registration_query,"预登记查询");
    public static final Jurisdiction JURISDICTION_PRE_REGISTRATION_STATISTICS2 = new Jurisdiction(JURISDICTION_PRE_REGISTRATION_STATISTICS, R.mipmap.ic_personstatistics,"个人统计");
    public static final Jurisdiction JURISDICTION_PRE_REGISTRATION_SHANGPAI_STATISTICS2 = new Jurisdiction(JURISDICTION_PRE_REGISTRATION_SHANGPAI_STATISTICS, R.mipmap.ic_shangpai_statistics,"上牌统计");
    public static final Jurisdiction JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE2 = new Jurisdiction(JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE, R.mipmap.ic_shangpai_free,"免费上牌");
    public static final Jurisdiction JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE_QUERY2 = new Jurisdiction(JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE_QUERY, R.mipmap.ic_shangpai_free_query,"免费上牌查询");


    public final static List<Jurisdiction> JURISDICTIONS = Arrays.asList(
            JURISDICTION_REGISTRATION2,
            JURISDICTION_CHANGE_REGISTRATION2,
            JURISDICTION_STATISTICS2,
            JURISDICTION_PRE_REGISTRATION2,
            JURISDICTION_PRE_REGISTRATION_QUERY2,
            JURISDICTION_PRE_REGISTRATION_STATISTICS2,
            JURISDICTION_PRE_REGISTRATION_SHANGPAI_STATISTICS2,
            JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE2,
            JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE_QUERY2);

    public static final class  Jurisdiction{
        String Jur;
        int ic;
        String name;
        public  Jurisdiction( String J,int i,String n){
            ic=i;
            Jur=J;
            name=n;
        }
        public int getIc() {
            return ic;
        }
        public String getJur() {
            return Jur;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

package com.tdr.registration.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tdr.registration.base.MyApplication;

/**
 * 电动车存储字段
 * Created by Linus_Xie on 2016/10/20.
 */

public class VehiclesStorageUtils {
    public static final String REGISTERID = "RegisterId";//预登记ID
    public static final String ECID = "Ecid";// 车辆id
    public static final String CARTYPE = "CARTYPE";// 车辆类型
    public static final String VEHICLETYPE = "VEHICLETYPE";//1  电动车 2  助力车  3  摩托车
    public static final String ISCONFIRM = "ISCONFIRM";// 是否承诺书
    public static final String PLATENUMBER = "PlateNumber";// 车牌号
    public static final String PLATETYPE = "PlateType";//车牌类型
    public static final String VEHICLEBRAND = "VehicleBrand";// 车辆品牌Code
    public static final String VEHICLEBRANDNAME = "VehiclebrandName";//车辆品牌
    public static final String SHELVESNO = "ShelvesNo";// 车架号
    public static final String ENGINENO = "EngineNo";// 电机号
    public static final String COLOR1ID = "Color1Id";// 颜色Code
    public static final String COLOR1NAME = "Color1Nanme";//颜色1名称
    public static final String COLOR2ID = "Color2Id";// 颜色2Code
    public static final String COLOR2NAME = "Color2Name";// 颜色2
    public static final String BUYDATE = "BuyDate";// 车辆购买时间
    public static final String OWNERNAME = "OwnerName";// 车辆所有者
    public static final String IDENTITY = "Identity";// 证件号码
    public static final String PHONE1 = "Phone1";// 联系方式1
    public static final String PHONE2 = "Phone2";// 联系方式2
    public static final String CURRENTADDRESS = "CurrentAddress";// 现居住地址
    public static final String REMARK = "Remark";// 备注
    public static final String CARDTYPE = "CARDTYPE";// 证件类型,1身份证 2 组织机构代码 3 台胞证 4 军人证 5 护照
    public static final String THEFTNO = "THEFTNO";//防盗标签
    public static final String THEFTNO2 = "THEFTNO2";

    public static final String PHOTOLIST1 = "PHOTOLIST1";
    public static final String PHOTOLIST2 = "PHOTOLIST2";
    public static final String PHOTOLIST3 = "PHOTOLIST3";

    public static final String INSURANCE = "INSURANCE";



    //=============代办人参数====================
    public static final String ISCOMMISSION = "isommission";//是否代办
    public static final String COMMISSIONCARDTYPE = "commissionCardType";//代办人证件类型
    public static final String COMMISSIONNAME = "commissionName";//代办人姓名
    public static final String COMMISSIONIDENTITY = "commissionIdentity";//代办人证件号码
    public static final String COMMISSIONPHONE1 = "commissionPhone1";//代办人联系方式
    public static final String COMMISSIONADDRESS = "commissionAddress";//代办人现住址
    public static final String COMMISSIONREMARK = "commissionRemark";//代办人备注

    /**r
     * 此方法清空传递的电动车实体类各个字段信息
     */

    public static void clearData() {
        Utils.ClearData();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(REGISTERID, "");
        editor.putString(CARTYPE, "");
        editor.putString(VEHICLETYPE, "");
        editor.putString(ISCONFIRM, "");
        editor.putString(PLATENUMBER, "");
        editor.putString(PLATETYPE, "");
        editor.putString(VEHICLEBRAND, "");
        editor.putString(VEHICLEBRANDNAME, "");
        editor.putString(SHELVESNO, "");
        editor.putString(ENGINENO, "");
        editor.putString(COLOR1ID, "");
        editor.putString(COLOR1NAME, "");
        editor.putString(COLOR2ID, "");
        editor.putString(COLOR2NAME, "");
        editor.putString(BUYDATE, "");
        editor.putString(OWNERNAME, "");
        editor.putString(IDENTITY, "");
        editor.putString(PHONE1, "");
        editor.putString(PHONE2, "");
        editor.putString(CURRENTADDRESS, "");
        editor.putString(REMARK, "");
        editor.putString(CARDTYPE, "");
        editor.putString(THEFTNO, "");
        editor.putString(THEFTNO2, "");

        editor.putString(ISCOMMISSION, "");
        editor.putString(COMMISSIONCARDTYPE, "");
        editor.putString(COMMISSIONNAME, "");
        editor.putString(COMMISSIONIDENTITY, "");
        editor.putString(COMMISSIONPHONE1, "");
        editor.putString(COMMISSIONADDRESS, "");
        editor.putString(COMMISSIONREMARK, "");
        editor.putString(PHOTOLIST1, "");
        editor.putString(PHOTOLIST2, "");
        editor.putString(PHOTOLIST3, "");

        editor.commit();
    }

    public static String getVehiclesAttr(String attrname) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.context).getString(attrname, "");
    }

    public static void setVehiclesAttr(String attrname, String value) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.context).edit().putString(attrname, value).commit();
    }


}

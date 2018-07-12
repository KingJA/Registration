package com.tdr.registration.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.kingja.entity.BatteryTHEFTNO;
import com.tdr.registration.model.LabelType;
import com.tdr.registration.model.SignTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Description:TODO
 * Create Time:2018/5/31 13:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class InterfaceChecker {
    private static final String NEW_INTERFACE = "1";
    private static final String TAG = "InterfaceChecker";

    public static boolean isNewInterface() {
        return NEW_INTERFACE.equals(SpSir.getDefault().getInterfaceVersion());
    }
    public static boolean isCheckBlack() {
        return NEW_INTERFACE.equals(SpSir.getDefault().getInterfaceVersion())&&SpSir.getDefault().getBlackCheck();
    }
    public static boolean hasBatteryTHEFTNO() {
        return NEW_INTERFACE.equals(SpSir.getDefault().getInterfaceVersion())&& !TextUtils.isEmpty(SpSir.getDefault().getBatteryTHEFTNO());
    }
    /**
     * 设置电动车VehicleType 1,三轮车VehicleType 5 配置
     *
     * @param signTypeInfos
     * @return
     */
    public static void setElectroCar(List<SignTypeInfo> signTypeInfos) {
        Gson gson = new Gson();
        List<SignTypeInfo> electroCarSignTypeInfos = new ArrayList<>();
        List<SignTypeInfo> threeElectroCarSignTypeInfos = new ArrayList<>();
        for (SignTypeInfo signTypeInfo : signTypeInfos) {
            if (signTypeInfo.isValid()) {
                if ("1".equals(signTypeInfo.getVehicleType())) {
                    electroCarSignTypeInfos.add(signTypeInfo);
                } else if ("5".equals(signTypeInfo.getVehicleType())) {
                    threeElectroCarSignTypeInfos.add(signTypeInfo);
                }
            }
        }
        SpSir.getDefault().setElectroCarSignTypes(gson.toJson(electroCarSignTypeInfos));
        SpSir.getDefault().setThreeElectroCarSignTypes(gson.toJson(threeElectroCarSignTypeInfos));
    }

    public static boolean checkBatteryTHEFTNO(String BatteryTheftNo) {
        String theftNoRegular = SpSir.getDefault().getBatteryTHEFTNO();
        Pattern pattern = Pattern.compile(theftNoRegular);
        Matcher matcher = pattern.matcher(BatteryTheftNo);
        if (!matcher.matches()) {
            ToastUtil.showToast("标签格式有误");
            return false;
        }
        return true;

    }

    private static List<SignTypeInfo> getElectroCarSignTypes() {
        Gson gson = new Gson();
        String electroCarSignTypes = SpSir.getDefault().getElectroCarSignTypes();
        List<SignTypeInfo> signTypeInfoList = gson.fromJson(electroCarSignTypes, new TypeToken<List<SignTypeInfo>>() {
        }.getType());
        return signTypeInfoList;
    }

    private static List<SignTypeInfo> getThreeElectroCarSignTypes() {
        Gson gson = new Gson();
        String threeElectroCarSignTypes = SpSir.getDefault().getThreeElectroCarSignTypes();
        List<SignTypeInfo> signTypeInfoList = gson.fromJson(threeElectroCarSignTypes, new TypeToken<List<SignTypeInfo>>() {
        }.getType());
        return signTypeInfoList;
    }

    public static List<SignTypeInfo> getSignTypes(String VehicleType) {
        if ("1".equals(VehicleType)) {
            return getElectroCarSignTypes();
        } else {
            return getThreeElectroCarSignTypes();
        }
    }

    public static List<LabelType> getExtraSignTypes(String vehicleType) {
        List<SignTypeInfo> signTypeInfos = getSignTypes(vehicleType);
        List<LabelType> results = new ArrayList<>();
        for (SignTypeInfo signTypeInfo : signTypeInfos) {
            if (signTypeInfo.getNumber() >= 3) {
                LabelType labelType = new LabelType();
                labelType.setName(signTypeInfo.getName());
                labelType.setValue(signTypeInfo.getNumber());
                labelType.setRegular(signTypeInfo.getRegular());
                results.add(labelType);
            }
        }
        return results;
    }

    public static List<LabelType> getChangeSignTypes(String vehicleType) {
        List<SignTypeInfo> signTypeInfos = getSignTypes(vehicleType);
        List<LabelType> results = new ArrayList<>();
        for (SignTypeInfo signTypeInfo : signTypeInfos) {
            if (signTypeInfo.getNumber() >= 2) {
                LabelType labelType = new LabelType();
                labelType.setName(signTypeInfo.getName());
                labelType.setValue(signTypeInfo.getNumber());
                labelType.setRegular(signTypeInfo.getRegular());
                results.add(labelType);
            }
        }
        return results;
    }
}

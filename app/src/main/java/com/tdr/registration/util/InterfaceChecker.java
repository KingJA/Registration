package com.tdr.registration.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.model.LabelType;
import com.tdr.registration.model.SignType;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


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
    /**
     * 设置电动车VehicleType 1,三轮车VehicleType 5 配置
     *
     * @param signTypes
     * @return
     */
    public static void setElectroCar(List<SignType> signTypes) {
        Gson gson = new Gson();
        List<SignType> electroCarSignTypes = new ArrayList<>();
        List<SignType> threeElectroCarSignTypes = new ArrayList<>();
        for (SignType signType : signTypes) {
            if (signType.isValid()) {
                if ("1".equals(signType.getVehicleType())) {
                    electroCarSignTypes.add(signType);
                } else if ("5".equals(signType.getVehicleType())) {
                    threeElectroCarSignTypes.add(signType);
                }
            }
        }
        SpSir.getDefault().setElectroCarSignTypes(gson.toJson(electroCarSignTypes));
        SpSir.getDefault().setThreeElectroCarSignTypes(gson.toJson(threeElectroCarSignTypes));
    }

    private static List<SignType> getElectroCarSignTypes() {
        Gson gson = new Gson();
        String electroCarSignTypes = SpSir.getDefault().getElectroCarSignTypes();
        List<SignType> signTypeList = gson.fromJson(electroCarSignTypes, new TypeToken<List<SignType>>() {
        }.getType());
        return signTypeList;
    }

    private static List<SignType> getThreeElectroCarSignTypes() {
        Gson gson = new Gson();
        String threeElectroCarSignTypes = SpSir.getDefault().getThreeElectroCarSignTypes();
        List<SignType> signTypeList = gson.fromJson(threeElectroCarSignTypes, new TypeToken<List<SignType>>() {
        }.getType());
        return signTypeList;
    }

    public static List<SignType> getSignTypes(String VehicleType) {
        if ("1".equals(VehicleType)) {
            return getElectroCarSignTypes();
        } else {
            return getThreeElectroCarSignTypes();
        }
    }

    public static List<LabelType> getExtraSignTypes(String vehicleType) {
        List<SignType> signTypes = getSignTypes(vehicleType);
        List<LabelType> results = new ArrayList<>();
        for (SignType signType : signTypes) {
            if (signType.getNumber() >= 3) {
                LabelType labelType = new LabelType();
                labelType.setName(signType.getName());
                labelType.setValue(signType.getNumber());
                labelType.setRegular(signType.getRegular());
                results.add(labelType);
            }
        }
        return results;
    }

    public static List<LabelType> getChangeSignTypes(String vehicleType) {
        List<SignType> signTypes = getSignTypes(vehicleType);
        List<LabelType> results = new ArrayList<>();
        for (SignType signType : signTypes) {
            if (signType.getNumber() >= 2) {
                LabelType labelType = new LabelType();
                labelType.setName(signType.getName());
                labelType.setValue(signType.getNumber());
                labelType.setRegular(signType.getRegular());
                results.add(labelType);
            }
        }
        return results;
    }
}

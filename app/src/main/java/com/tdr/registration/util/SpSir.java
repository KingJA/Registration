package com.tdr.registration.util;

import android.content.SharedPreferences;

import com.tdr.registration.base.App;

/**
 * Description:TODO
 * Create Time:2018/5/18 14:17
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class SpSir {
    private static SpSir spSir;
    private static SharedPreferences defaultSp;

    private SpSir() {
        defaultSp = App.getSP();
    }

    public static SpSir getDefault() {
        if (spSir == null) {
            synchronized (SpSir.class) {
                if (spSir == null) {
                    spSir = new SpSir();
                }
            }
            return spSir;
        }
        return spSir;

    }

    public void setEngineNoRegular(String value) {
        save(SpConstants.ENGINENO_REGULAR, value);
    }

    public String getEngineNoRegular() {
        return get(SpConstants.ENGINENO_REGULAR, SpConstants.EMPTY);
    }

    public void setShelvesNoRegular(String value) {
        save(SpConstants.SHELVESNO_REGULAR, value);
    }

    public String getShelvesNoRegular() {
        return get(SpConstants.SHELVESNO_REGULAR, SpConstants.EMPTY);
    }

    public void setInterfaceVersion(String value) {
        save(SpConstants.INTERFACE_VERSION, value);
    }

    public String getInterfaceVersion() {
        return get(SpConstants.INTERFACE_VERSION, SpConstants.EMPTY);
    }

    public void setElectroCarSignTypes(String value) {
        save(SpConstants.ElectroCarSignTypes, value);
    }

    public String getElectroCarSignTypes() {
        return get(SpConstants.ElectroCarSignTypes, SpConstants.EMPTY);
    }


    public void setThreeElectroCarSignTypes(String value) {
        save(SpConstants.ThreeElectroCarSignTypes, value);
    }

    public String getThreeElectroCarSignTypes() {
        return get(SpConstants.ThreeElectroCarSignTypes, SpConstants.EMPTY);
    }

    public void setBlackCheck(boolean value) {
        save(SpConstants.BlackCheck, value);
    }

    public boolean getBlackCheck() {
        return get(SpConstants.BlackCheck, SpConstants.FALSE);
    }


    public void setBindTagVehicleType(String value) {
        save(SpConstants.BindTagVehicleType, value);
    }

    public String getBindTagVehicleType() {
        return get(SpConstants.BindTagVehicleType, SpConstants.EMPTY);
    }

    public String get(String key, String defValue) {
        return defaultSp.getString(key, defValue);
    }
    public boolean get(String key, boolean defValue) {
        return defaultSp.getBoolean(key, defValue);
    }

    private void save(String key, String value) {
        defaultSp.edit().putString(key, value).apply();
    }

    private void save(String key, boolean value) {
        defaultSp.edit().putBoolean(key, value).apply();
    }

    private void save(String key, int value) {
        defaultSp.edit().putInt(key, value).apply();
    }
}

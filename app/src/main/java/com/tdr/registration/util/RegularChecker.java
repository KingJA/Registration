package com.tdr.registration.util;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:TODO
 * Create Time:2018/5/18 14:39
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class RegularChecker {

    private static final String TAG = "RegularChecker";

    private static boolean check(String regular, String value, String tip) {
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            ToastUtil.showToast(tip);
            return false;
        }
        return true;
    }

    public static boolean checkEngineNoRegular(String engineNo) {
        Log.e(TAG, "校验engineNo: "+engineNo );
        if (TextUtils.isEmpty(engineNo)) {
            ToastUtil.showToast("请输入电机号");
            return false;
        }
        if ("*".equals(engineNo)) {
            return true;
        }
        String engineNoRegular = SpSir.getDefault().getEngineNoRegular();
        Log.e(TAG, "校验engineNoRegular: "+engineNoRegular );
        if (TextUtils.isEmpty(engineNoRegular)||"null".equals(engineNoRegular)) {
            return true;
        }
        return check(engineNoRegular, engineNo, "电机号不匹配");
    }

    public static boolean checkShelvesNoRegular(String shelvesNo) {
        if (TextUtils.isEmpty(shelvesNo)) {
            ToastUtil.showToast("请输入车架号");
            return false;
        }
        if ("*".equals(shelvesNo)) {
            return true;
        }
        String shelvesNoRegular = SpSir.getDefault().getShelvesNoRegular();
        if (TextUtils.isEmpty(shelvesNoRegular)||"null".equals(shelvesNoRegular)) {
            return true;
        }
        return check(shelvesNoRegular, shelvesNo, "车架号不匹配");
    }
}

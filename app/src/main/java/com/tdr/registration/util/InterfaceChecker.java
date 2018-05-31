package com.tdr.registration.util;

/**
 * Description:TODO
 * Create Time:2018/5/31 13:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class InterfaceChecker {
    private static final String NEW_INTERFACE="1";

    public static boolean isNewInterface() {
        return NEW_INTERFACE.equals(SpSir.getDefault().getInterfaceVersion());
    }
}

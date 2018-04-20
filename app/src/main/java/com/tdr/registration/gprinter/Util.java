package com.tdr.registration.gprinter;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;

/**
 * Created by Linus_Xie on 2016/11/3.
 */

public class Util {
    public static String SimToTra(String simpStr) {
        String traditionalStr = null;
        try {
            JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
            traditionalStr = jChineseConvertor.s2t(simpStr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return traditionalStr;
    }
}

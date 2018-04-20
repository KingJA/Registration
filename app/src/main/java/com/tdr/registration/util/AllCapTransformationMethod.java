package com.tdr.registration.util;

import android.text.method.ReplacementTransformationMethod;

/**
 * 设置EditText输入的文字全部变成大写或小写
 * editText.setTransformationMethod(new AllCapTransformationMethod(true));
 * false就是小写
 * Created by Linus_Xie on 2016/9/20.
 */
public class AllCapTransformationMethod extends ReplacementTransformationMethod {

    private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private boolean allUpper = false;

    public AllCapTransformationMethod(boolean needUpper) {
        this.allUpper = needUpper;
    }

    @Override
    protected char[] getOriginal() {
        if (allUpper) {
            return lower;
        } else {
            return upper;
        }
    }

    @Override
    protected char[] getReplacement() {
        if (allUpper) {
            return upper;
        } else {
            return lower;
        }
    }
}

package com.tdr.registration.util;

/**
 * 天地人编码解码
 * Created by Linus_Xie on 2016/10/9.
 */
public class TendencyEncrypt {
    /**
     * 源字符串
     */
    private static final char[] SSCode = new char[] { 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '0', '+', '=' };

    /**
     * 转码字符串
     */
    private static final char[] EnCode = new char[] { '1', '2', 'b', 'W', 'd',
            'e', 'f', 'Y', 'R', 'S', 'T', 'U', 'V', '=', 'X', 'Q', 'g', 'h',
            'i', '4', '9', '0', 'm', 'n', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'K', 'L', 'M', 'P', 'I', 'J', 'N', 'O', 's', 't', 'u', 'v',
            'o', 'p', 'q', 'r', '5', '6', '7', '8', 'w', 'x', 'y', 'z', 'Z',
            'a', '3', 'j', 'k', 'l', '+', 'c' };

    private static int GetSSCodeIndex(char chr) {
        for (int i = 0; i < SSCode.length; i++) {
            if (SSCode[i] == chr) {
                return i;
            }
        }
        return -1;
    }

    private static int GetEnCodeIndex(char chr){
        for (int i = 0; i < EnCode.length; i++) {
            if (EnCode[i] == chr) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 编码
     * @param base64string
     * @return
     */
    public static String encrypt(String base64string) {
        String outString = "";
        for (int i = 0; i < base64string.length(); i++) {
            String getString = base64string.substring(i, i + 1);
            int getIndex = GetSSCodeIndex(getString.toCharArray()[0]);
            if (getIndex == -1) {
                outString += getString;
            } else {
                outString += EnCode[getIndex];
            }
        }
        return outString;
    }

    /**
     * 反编码
     * @param base64string
     * @return
     */
    public static String decrypt(String base64string){//http://wz.app.iotone.cn/?baVWd5V96WV415VW168jsc
        String outString = "";
        for (int i = 0; i < base64string.length(); i++) {
            String getString = base64string.substring(i, i + 1);
            int getIndex = GetEnCodeIndex(getString.toCharArray()[0]);
            if (getIndex == -1) {
                outString += getString;
            } else {
                outString += SSCode[getIndex];//R
            }
        }
        return outString;
    }
}

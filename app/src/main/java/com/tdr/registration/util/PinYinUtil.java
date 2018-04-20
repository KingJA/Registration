package com.tdr.registration.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * Created by Administrator on 2017/6/7.
 */

public class PinYinUtil {
    public static StringBuffer sb = new StringBuffer();

    /**
     * 获取汉字字符串的首字母，英文字符不变
     * 例如：阿飞→af
     */
    public static String getPinYinHeadChar(String chines) {
        sb.setLength(0);
        char[] chars = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > 128) {
                try {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(chars[i], defaultFormat)[0].charAt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 获取汉字字符串的第一个字母
     */
    public static String getPinYinFirstLetter(String str) {
        sb.setLength(0);
        char c = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyinArray != null) {
            sb.append(pinyinArray[0].charAt(0));
        } else {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 获取汉字字符串的汉语拼音，英文字符不变
     */
    public static String getPinYin(String chines) {
        sb.setLength(0);
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(nameChar[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 筛选
     * @param str1
     * @param str2
     * @return
     */
    public static boolean checkname(String str1,String str2){
        str1=str1.trim();
        boolean ret=false;
        if(str2.matches("^[a-zA-Z]*")){
           if(check(str1,str2)||checkPinYin1(str1,str2.toUpperCase())||checkPinYin2(str1,str2.toUpperCase())){
               ret=true;
           }
            if(check(str1,str2)||checkPinYin1(str1,str2.toLowerCase())||checkPinYin2(str1,str2.toLowerCase())){
                ret=true;
            }
        }else{
            ret= check(str1,str2)||checkPinYin1(str1,str2)||checkPinYin2(str1,str2);
        }
        return ret;
    }

    /**
     * 简单筛选
     * @param str1
     * @param str2
     * @return
     */
    private static boolean check(String str1,String str2){
        boolean ret=false;
        if(str1.contains(str2)){
            ret=true;
        }
        return ret;
    }
    /**
     * 中文筛选
     * @param str1
     * @param str2
     * @return
     */
    private static boolean checkPinYin1(String str1,String str2){
        boolean ret=false;
        for(int i = 0; i < str1.length(); i++){
            String name= PinYinUtil.getPinYin(str1.substring(i, i+1));
            if(name.startsWith(str2)){
//                mLog.e("check","条件"+str2+"   文字："+str1+"  拼音："+name);
                ret= true;
            }
        }
        return ret;
    }

    /**
     * 首字母筛选
     * @param str1
     * @param str2
     * @return
     */
    private static boolean checkPinYin2(String str1,String str2){
        boolean ret=false;
        String name= PinYinUtil.getPinYinHeadChar(str1);
        if(name.contains(str2)){
//            mLog.e("check","条件"+str2+"   文字："+str1+"  文字简写："+name);
            ret=true;
        }
        return ret;
    }
}

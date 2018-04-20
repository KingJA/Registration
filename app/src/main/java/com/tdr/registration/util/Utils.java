package com.tdr.registration.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.tdr.registration.base.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 工具类
 * Created by Linus_Xie on 2016/7/27.
 */
public class Utils {

    public static String ServerTime = "";

    /**
     * 自定义Toast
     *
     * @param context
     * @param msg
     */
    public static final void myToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(String msg) {
        Toast toast = Toast.makeText(MyApplication.getContext(), "", Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 手机正则表达式
     *
     * @param phone
     * @return boolean
     */
    public static boolean isPhone(String phone) {
        String regExp = "0?(1)[0-9]{10}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    /**
     * 校验18位身份证号码
     */
    public static boolean isIDCard18(final String value) {
        if (value == null || value.length() != 18)
            return false;
        if (!value.matches("[\\d]+[X]?"))
            return false;
        String code = "10X98765432";
        int weight[] = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
                8, 4, 2, 1};
        int nSum = 0;
        for (int i = 0; i < 17; ++i) {
            nSum += (int) (value.charAt(i) - '0') * weight[i];
        }
        int nCheckNum = nSum % 11;
        char chrValue = value.charAt(17);
        char chrCode = code.charAt(nCheckNum);
        if (chrValue == chrCode)
            return true;
        if (nCheckNum == 2 && (chrValue + ('a' - 'A') == chrCode))
            return true;
        return false;
    }

    /**
     * 获取版本号(内部识别号)
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取versionName
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转为 dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕的宽度
     */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static final boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void httppost(String url) {
        RequestParams params = new RequestParams(url);
//            params.setSslSocketFactory(...); // 设置ssl
        params.addQueryStringParameter("accessToken", DESCoder.encrypt("GETCITYLIST", Constants.DES_KEY));
        params.addQueryStringParameter("infoJsonStr", "_" + "ANDROID");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mLog.e("onSuccess:" + result);
//                    Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLog.e("onError:" + ex.getMessage());
//                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mLog.e("onCancelled:");
//                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * byte转String
     *
     * @param bt
     * @return
     */
    public static String Byte2Str(byte[] bt) {
        return Base64.encode(bt);
    }

    /**
     * Bitmap转换为byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * base64字符串转图片
     *
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = android.util.Base64.decode(string, android.util.Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 初始化数据，避免空指针
     *
     * @param str
     * @return
     */
    public static String initNullStr(String str) {
        if ("null".equals(str) || null == str || "".equals(str)) {
            return "";
        }
        return str;
    }

    /**
     * 压缩图片用ThumbnailUtils
     */
    public static Bitmap thumbnailBitmap(Bitmap bitmap, int reqsW, int reqsH) {
        int old_width = bitmap.getWidth();
        int old_height = bitmap.getHeight();
        // 计算，目标是300*400
        int ratio_width = reqsW;
        int ratio_height = reqsH;
        // 新宽、高
        int new_width = 0;
        int new_height = 0;
        // 缩放参考策略
        if (old_width * ratio_height > old_height * ratio_width) {
            new_width = ratio_width;
            new_height = old_height * ratio_width / old_width;
        } else {
            new_height = ratio_height;
            new_width = old_width * ratio_height / old_height;
        }
        Bitmap new_bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, new_width, new_height);
        return new_bitmap;
    }

    public static Bitmap thumbnailBitmap(Bitmap bitmap) {
        int old_width = bitmap.getWidth();
        int old_height = bitmap.getHeight();
        // 计算，目标是300*400
        int ratio_width = 680;
        int ratio_height = 800;
        // 新宽、高
        int new_width = 0;
        int new_height = 0;
        // 缩放参考策略
        if (old_width * ratio_height > old_height * ratio_width) {
            new_width = ratio_width;
            new_height = old_height * ratio_width / old_width;
        } else {
            new_height = ratio_height;
            new_width = old_width * ratio_height / old_height;
        }
        Bitmap new_bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, new_width, new_height);
        return new_bitmap;
    }

    /**
     * byte——>String
     *
     * @param src
     * @return
     */
    public static final String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 身份证号码，隐藏中间的出身年月日
     */
    public static final String hideID(String id) {
        if (id.length() == 18) {
            String a = id.substring(2, 16);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length(); i++) {
                String b = a.replace(a, "*");
                sb.append(b);
            }
            String newId = id.substring(0, 2) + sb.toString()
                    + id.substring(16, id.length());
            return newId;
        } else {
            return id;
        }
    }

    public static int[] getLocation(View v) {
        int[] loc = new int[4];
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        loc[0] = location[0];
        loc[1] = location[1];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);

        loc[2] = v.getMeasuredWidth();
        loc[3] = v.getMeasuredHeight();

        //base = computeWH();
        return loc;
    }

    /**
     * 数组截取：从arrData的offset开始获取len个长度的byte生成的len的byte[]
     *
     * @param arrData
     * @param offset
     * @param len
     * @return
     */
    public static final byte[] GetByteArrayByLength(byte[] arrData, int offset, int len) {
        byte[] newData = new byte[len];
        try {
            for (int i = offset; i < offset + len; i++) {
                newData[i - offset] = arrData[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newData;
    }
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    /**
     * short转换成byte数组
     *
     * @param s
     * @return
     */
    public static final byte[] shortToBytes(short s) {//01e2
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 1 - i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 0xff);
        }
        return shortBuf;
    }

    /**
     * short 转 byte 高低位互换
     *
     * @param s
     * @return byte[]
     */
    public static final byte[] shortToByte(short s) {//e201
        byte[] shortBuf = new byte[2];
        shortBuf[0] = (byte) (s & 0xff);
        shortBuf[1] = (byte) ((s >>> 8) & 0xff);
        return shortBuf;
    }

    public static String HL_Transposition(String s) {
        Log.e("Pan", "换位前=" + s);
        String ret = "";
        String sA = s.substring(0, 2);
        String sB = s.substring(2);
        int A = Integer.parseInt(sA, 16);
        int B = Integer.parseInt(sB, 16);
        if (A > B) {
            ret = sB + sA;
        }
        Log.e("Pan", "换位后=" + ret);
        return ret;
    }

    public static byte[] HL_Transposition(byte[] s) {
        Log.e("Pan", "换位前=" + s);
        byte temp; // 记录临时中间值
        int size = s.length; // 数组大小
        byte s2[]=new byte[size];
        List<Byte> l=new ArrayList<>();
        for (byte b : s) {
            l.add(b);
        }
        Collections.sort(l);
        for (int i = 0; i < l.size(); i++) {
            s2[i]=l.get(i);
        }
        Log.e("Pan", "s2="+bytesToHexString(s2));
        for (int i = 0; i < size ; i++) {
            for (int j = i + 1; j < size; j++) {
                Log.e("Pan", "si="+s[i]+"   sj"+s[j]);

                if (s[i] < s[j]) { // 交换两数的位置
                    temp = s[i];
                    s[i] = s[j];
                    s[j] = temp;
                }
            }
        }
        Log.e("Pan", "换位后");
        for (int i = 0; i < s.length; i++) {
            Log.e("Pan", "s=" + s[i]);
        }
        return s;
    }

    public static int changeInt(String a) {
        Log.e("Pan", "a=" + a);
        char[] ss = a.toCharArray();
        List<String> dd = new ArrayList<>();
        for (int i = 0; i < ss.length; i++) {
            Log.e("Pan", "ss=" + ss[i]);
            if (i != 0 && i % 2 == 0) {
                String d = "" + ss[i] + ss[i - 1];
                dd.add(d);
            }
        }
        Collections.sort(dd, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 1;
            }
        });
        String number = "";
        for (int i = 0; i < dd.size(); i++) {
            number += dd.get(i);
        }
        Log.e("Pan", "number=" + number);
        return Integer.valueOf(number);
    }

    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    public static boolean check_FrameOrMotor(String fram) {
        boolean back = true;
        int f = 0;
        int f2 = 0;
        for (int i = 1; i <= fram.length(); i++) {
            if (fram.substring(i - 1, i).equals("*")) {
                f++;
            } else {
                f2++;
            }
        }
        if (fram.length() == f) {
            back = false;
        }
        if (f2 > 0 && f > f2) {
            back = false;
        }
        return back;
    }

    /**
     * 判断两个byte数组是否相等
     *
     * @param myByte
     * @param otherByte
     * @return
     */
    public static final boolean checkByte(byte[] myByte, byte[] otherByte) {
        boolean b = false;
        int len = myByte.length;
        if (len == otherByte.length) {
            for (int i = 0; i < len; i++) {
                if (myByte[i] != otherByte[i]) {
                    return b;
                }
            }
            b = true;
        }
        return b;
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static final String getNowTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 日期泛型
     *
     * @param date
     * @return
     */
    public static String setDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 年月日时分秒泛型
     *
     * @param date
     * @return
     */
    public static String setTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 获取当前系统日期
     *
     * @return
     */
    public static final String getNowDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * String转成byte数组
     */

    public static byte[] hexStringToBytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static final byte[] intTobyte(int s) {
        byte[] shortBuf = new byte[2];
        shortBuf[0] = (byte) (s & 0xff);
        shortBuf[1] = (byte) ((s >>> 8) & 0xff);
        return shortBuf;
    }

    public static final byte[] intTobyte2(int s) {
        byte[] shortBuf = new byte[1];
        shortBuf[0] = (byte) (s & 0xff);
        return shortBuf;
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param src src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     * <p>
     * 89860070111257004158--->0x89 0x86.... 0
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            System.out.println("0x" + src.substring(i * 2, m) + src.substring(m, n));
            ret[i] = (byte) Integer.parseInt((src.substring(i * 2, m) + src.substring(m, n)).trim(), 16);
        }
        return ret;
    }

    /**
     * 若都不为空，将arrSrc2添加到arrSrc1的末尾组合成新的byte数组
     *
     * @param arrSrc1
     * @param arrSrc2
     * @return
     */
    public static final byte[] ByteArrayCopy(byte[] arrSrc1, byte[] arrSrc2) {
        byte[] arrDes = null;
        if (arrSrc2 == null) {
            arrDes = arrSrc1;
            return arrDes;
        }

        if (arrSrc1 != null) {
            arrDes = new byte[arrSrc1.length + arrSrc2.length];
            System.arraycopy(arrSrc1, 0, arrDes, 0, arrSrc1.length);
            System.arraycopy(arrSrc2, 0, arrDes, arrSrc1.length, arrSrc2.length);
        } else {
            arrDes = new byte[arrSrc2.length];
            System.arraycopy(arrSrc2, 0, arrDes, 0, arrSrc2.length);
        }

        return arrDes;
    }

    /**
     * 获取两数相除结果
     *
     * @param i 分子
     * @param j 分母
     * @return
     */
    public static String getDivide(int i, int j) {
        NumberFormat f = NumberFormat.getNumberInstance();
        f.setMaximumFractionDigits(1);
        f.setMinimumFractionDigits(1);
        double d = (i * 1.0 / j) * 100;
        return f.format(d);
    }

    /**
     * List to String 中间“，”
     *
     * @param stringList
     * @return
     */
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 奇偶数
     */
    public static String maleOrFemale(String identity) {
        String sex = "";
        int mf = Integer.valueOf(identity.substring(16, 17));
        if (mf % 2 == 0) {
            sex = "女";
        } else {
            sex = "男";
        }
        return sex;
    }

    /**
     * 身份证提取出生年月日
     * 330302 1986 02 01 0305
     */
    public static String identityToBirthday(String identity) {
        String birthday = "";
        String year = identity.substring(6, 10);
        String month = identity.substring(10, 12);
        String day = identity.substring(12, 14);
        birthday = year + "年" + month + "月" + day + "日";
        return birthday;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 读取本地图片
     *
     * @param dst
     * @param width
     * @param height
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (true) {
                if (roundedSize >= initialSize)
                    return roundedSize;
                roundedSize <<= 1;
            }
        }

        return 8 * ((initialSize + 7) / 8);
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 获取文件名
     *
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    private static SimpleDateFormat sdf;

    public static boolean isDateOk(String s1, String s2) {
        try {
            Date d1 = sdf.parse(s1);
            Date d2 = sdf.parse(s2);
            long dif = d1.getTime() - d2.getTime();
            long days = dif / (1000 * 60 * 60 * 24);
            if (days < -31) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String dateWithoutTime(String time) {
        String date = time.split(" ")[0];
        return date;
    }

    /**
     * byte数组转换成Short
     *
     * @param b
     * @return
     */
    public static final short bytesToShort(byte[] b) {
        return (short) ((b[0] << 8) + (b[1] & 0xFF));
    }

    //long类型转成byte数组
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[6];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    public static byte[] byteChange(byte[] a) {
        a[0] = (byte) (a[0] + a[1]);
        a[1] = (byte) (a[0] - a[1]);
        a[0] = (byte) (a[0] - a[1]);
        return a;
    }

    public static boolean CheckPhoneNumber(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 1000;

    public static void LOGE(String TAG, String msg) {
        if (msg == null) {
            msg = "";
        }
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 1000; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static void ClearData() {
//        int size = (int) SharedPreferencesUtils.get("PhotoListSize", 0);
//        Log.e("Pan","size="+size);
        for (int i = 1; i <= 8; i++) {
            SharedPreferencesUtils.put("Photo:" + i, "");
        }
//        SharedPreferencesUtils.put("PhotoListSize", 0);
    }

    public static void mToast(Context mContext, String tag, String msg) {
//        Toast.makeText(mContext,"日志("+tag+":\n"+msg+")",Toast.LENGTH_LONG).show();
    }

    public static void CheckBuyTime(String BuyTime, GetServerTime GST) {
        getServerTime(BuyTime, GST);

    }

    public static boolean CheckBuyTime(String BuyTime) {
        mLog.e("BuyTime=" + BuyTime + "   ServerTime=" + ServerTime);
        long day = 1000 * 60 * 60 * 24;
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date NT = df.parse(BuyTime);
            Date ST = df.parse(ServerTime);
            if (NT.getTime() - ST.getTime() >= day) {
                mLog.e("NT=" + NT.getTime() + "   ST=" + ST.getTime() + "    NT-ST=" + (NT.getTime() - ST.getTime()) + "   超过1天");
                return false;
            } else {
                mLog.e("NT=" + NT.getTime() + "   ST=" + ST.getTime() + "    NT-ST=" + (NT.getTime() - ST.getTime()) + "   未超过1天");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean CheckBuyTime(String BuyTime, String ServerTime) {
        mLog.e("BuyTime=" +BuyTime + "   ServerTime=" +ServerTime);

        long day = 1000 * 60 * 60 * 24;
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date NT = df.parse(BuyTime);
            Date ST = df.parse(ServerTime);
            if (NT.getTime() - ST.getTime() >= day) {
                mLog.e("NT=" + NT.getTime() + "   ST=" + ST.getTime() + "    NT-ST=" + (NT.getTime() - ST.getTime()) + "   超过1天");
                return false;
            } else {
                mLog.e("NT=" + NT.getTime() + "   ST=" + ST.getTime() + "    NT-ST=" + (NT.getTime() - ST.getTime()) + "   未超过1天");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean ContrastTime(String Time1, String Time2) {
        long Minute = 1000 * 60;

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date T1 = df.parse(Time1);
            Date T2 = df.parse(Time2);
            if (T1.getTime() - T2.getTime() >= Minute) {
                mLog.e("T1=" + T1.getTime() + "   T2=" + T2.getTime() + "    T1-ST=" + (T1.getTime() - T2.getTime()) + "   超过1分钟");
                return false;
            } else {
                mLog.e("T1=" + T1.getTime() + "   T2=" + T2.getTime() + "    T1-ST=" + (T1.getTime() - T2.getTime()) + "   未超过1分钟");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void getServerTime(final String BuyTime, final GetServerTime GST) {
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETSERVERTIME, null, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                ServerTime = result;
                if (result != null) {
                    mLog.e("ServerTime=" + result);
                    if (GST != null) {
                        GST.ServerTime(result, CheckBuyTime(BuyTime, result));
                    }
                }
            }
        });
    }



    public static void getServerTime() {
        mLog.e("getServerTime");
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETSERVERTIME, null, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                mLog.e("ServerTime=" + result);
                if (result != null) {
                    ServerTime = result;
                }
            }
        });
    }

    public interface GetServerTime {
        void ServerTime(String ServerTime, boolean Check);
    }

    public static void CompleteConfig() {
        mLog.e("------------------------------");
        String isChecked = (String) SharedPreferencesUtils.get("isChecked", "");
        mLog.e("isChecked=" + isChecked);
        String whiteListUrl = (String) SharedPreferencesUtils.get("whiteListUrl", "");
        mLog.e("whiteListUrl=" + whiteListUrl);
        String hasPreregister = (String) SharedPreferencesUtils.get("hasPreregister", "");
        mLog.e("hasPreregister=" + hasPreregister);
        String isScanLabel = (String) SharedPreferencesUtils.get("isScanLabel", "");
        mLog.e("isScanLabel=" + isScanLabel);
        String isScanCard = (String) SharedPreferencesUtils.get("isScanCard", "");
        mLog.e("isScanCard=" + isScanCard);
        String REGULAR = (String) SharedPreferencesUtils.get("REGULAR", "");
        mLog.e("REGULAR=" + REGULAR);
        String REGULAR2 = (String) SharedPreferencesUtils.get("REGULAR2", "");
        mLog.e("REGULAR2=" + REGULAR2);
        String IsDoubleSign = (String) SharedPreferencesUtils.get("IsDoubleSign", "");
        mLog.e("IsDoubleSign=" + IsDoubleSign);
        String EnableInvoice = (String) SharedPreferencesUtils.get("EnableInvoice", "");
        mLog.e("EnableInvoice=" + EnableInvoice);
        String IsShowPay = (String) SharedPreferencesUtils.get("IsShowPay", "");
        mLog.e("IsShowPay=" + IsShowPay);
        String ShowWallet = (String) SharedPreferencesUtils.get("ShowWallet", "");
        mLog.e("ShowWallet=" + IsShowPay);

        String ChangeType = (String) SharedPreferencesUtils.get("ChangeType", "");
        mLog.e("ChangeType=" + ChangeType);
        String HasAgent = (String) SharedPreferencesUtils.get("HasAgent", "");
        mLog.e("HasAgent=" + HasAgent);
        String IsConfirm = (String) SharedPreferencesUtils.get("IsConfirm", "");
        mLog.e("IsConfirm=" + IsConfirm);
        String IsScanDjh = (String) SharedPreferencesUtils.get("IsScanDjh", "");
        mLog.e("IsScanDjh=" + IsScanDjh);
        String IsScanCjh = (String) SharedPreferencesUtils.get("IsScanCjh", "");
        mLog.e("IsScanCjh=" + IsScanCjh);
        String ShowQR = (String) SharedPreferencesUtils.get("ShowQR", "");
        mLog.e("ShowQR=" + ShowQR);
        String IsTransferReserve = (String) SharedPreferencesUtils.get("IsTransferReserve", "");
        mLog.e("IsTransferReserve=" + IsTransferReserve);



        mLog.e("------------------------------");
        if (isChecked.equals("")) {
            SharedPreferencesUtils.put("isChecked", "0");
        }
        if (hasPreregister.equals("")) {
            SharedPreferencesUtils.put("hasPreregister", "0");
        }
        if (isScanLabel.equals("")) {
            SharedPreferencesUtils.put("isScanLabel", "0");
        }
        if (isScanCard.equals("")) {
            SharedPreferencesUtils.put("isScanCard", "0");
        }
        if (IsDoubleSign.equals("")) {
            SharedPreferencesUtils.put("IsDoubleSign", "0");
        }
        if (EnableInvoice.equals("")) {
            SharedPreferencesUtils.put("isChecked", "0");
        }
        if (IsShowPay.equals("")) {
            SharedPreferencesUtils.put("IsShowPay", "0");
        }
        if (ShowWallet.equals("")) {
            SharedPreferencesUtils.put("ShowWallet", "0");
        }
        if (ChangeType.equals("")) {
            SharedPreferencesUtils.put("ChangeType", "1,2,4");
        }
        if (IsConfirm.equals("")) {
            SharedPreferencesUtils.put("IsConfirm", "0");
        }
        if (IsScanDjh.equals("")) {
            SharedPreferencesUtils.put("IsScanDjh", "0");
        }
        if (IsScanCjh.equals("")) {
            SharedPreferencesUtils.put("IsScanCjh", "0");
        }
        if (ShowQR.equals("")) {
            SharedPreferencesUtils.put("ShowQR", "1");
        }
        if (IsTransferReserve.equals("")) {
            SharedPreferencesUtils.put("IsTransferReserve", "0");
        }
    }

    /**
     * @param text
     * @return String    返回类型
     * @Title: fmtMicrometer
     * @Description: 格式化数字为千分位
     */
    public static String fmtMicrometer(String text) {
        if (text == null) {
            return "0.00";
        }
        DecimalFormat df = null;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.00");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0.00");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

    /**
     * 获取当前月份往前12个月的月份列表
     *
     * @return
     */
    public static List<String> getMonth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        mLog.e(year + " 年 " + month + " 月");
        List<String> Month = new ArrayList<>();
        int index = 0;
        month += 12;
        for (int i = 12; i >= 1; i--) {
            int sum = month - 12 - index;
            if (sum == 1) {
                index -= 12;
            }
            if (sum == 12 && month - 12 < 12) {
                year--;
            }
            if (sum / 10 >= 1) {
                Month.add(year + "-" + sum);
            } else {
                Month.add(year + "-0" + sum);
            }
            index++;
        }
        return Month;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getdate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if (month / 10 >= 1) {
            return year + "-" + month;
        } else {
            return year + "-0" + month;
        }

    }
    public static byte[] deleteByte(byte[] bs, int index) {
        if(index==0){
            return bs;
        }
        List<byte[]>  list=new ArrayList<>();
        for (int i = 0; i <index ; i++) {
            list.add(deleteAt(bs,0));
        }
//        LOG.BLE("deleteAt=" + Utils.bytesToHexString(list.get(list.size()-1)));
        return list.get(list.size()-1);
    }
    public static byte[] deleteAt(byte[] bs, int index){
        int length = bs.length - 1;
        byte[] ret = new byte[length];

        if(index == bs.length - 1){
            System.arraycopy(bs, 0, ret, 0, length);
        }else if(index < bs.length - 1){
            for(int i = index; i < length; i++) {
                bs[i] = bs[i + 1];
            }
            System.arraycopy(bs, 0, ret, 0, length);
        }
        return ret;
    }

    public static int getRandom(int min,int max) {
      return new Random().nextInt(max)%(max-min+1) + min;
    }
}

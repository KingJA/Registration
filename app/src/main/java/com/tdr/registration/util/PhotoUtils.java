package com.tdr.registration.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import id.zelory.compressor.Compressor;


/**
 * 处理照片工具类
 */

public class PhotoUtils {
    public final static int CAMERA_REQESTCODE = 2017;//照相机回调值
    public static File imageFile;//照片临时文件
    public static String mCurrentPhotoPath;//临时文件地址
    public static String mPicName;
    public static String photoStr;
    public static int CurrentapiVersion = android.os.Build.VERSION.SDK_INT;

    public static void TakePicture(Activity mActivity, String picName) {
        mPicName = picName;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {//判断是否有相机应用
            try {
                MobclickAgent.reportError(mActivity, "创建临时图片文件");
                imageFile = createImageFile(mActivity, mPicName);//创建临时图片文件
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (imageFile != null) {
                MobclickAgent.reportError(mActivity, "打开相机");
                Uri photoURI = FileProvider.getUriForFile(mActivity, "com.tdr.registration", imageFile);
                List<ResolveInfo> resInfoList = mActivity.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    mActivity.grantUriPermission(packageName, photoURI,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mActivity.startActivityForResult(takePictureIntent, CAMERA_REQESTCODE);
            }
        }
    }

    public static void TakePicture2(Activity mActivity, String picName) {
        mPicName = picName;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mActivity.startActivityForResult(intent, CAMERA_REQESTCODE);
    }

    public static void sevephoto(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(Environment.getExternalStorageDirectory()
                        + "/Registration/" + mPicName + ".jpg");

                mLog.e("name=" + f.getName());
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private static File createImageFile(Activity mActivity, String picName) throws IOException {
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, picName + ".jpg");
        mCurrentPhotoPath = image.getAbsolutePath();
        mLog.e("mCurrentPhotoPath:" + mCurrentPhotoPath);
        MobclickAgent.reportError(mActivity, "PhotoPath：" + mCurrentPhotoPath);
        return image;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @return degree旋转的角度
     */
    public static int readPictureDegree() {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(mCurrentPhotoPath);
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

    public static Bitmap getBitmapFromFile(File dst) {
        return getBitmapFromFile(dst, 600, 800);//(720*1280)
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

    public static void DeletePhoto(File file) {
        if (file.isFile()) {
            if (isphoto(file.getName())) {
                file.delete();
            }
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                if (isphoto(file.getName())) {
                    file.delete();
                }
                return;
            }
            for (File f : childFile) {
                DeletePhoto(f);
            }
            if (isphoto(file.getName())) {
                file.delete();
            }
        }
    }

    private static boolean isphoto(String fName) {
        boolean isPhoto = false;
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")|| FileEnd.equals("webp")) {
            isPhoto = true;
        }
        return isPhoto;
    }

    public static Bitmap getPhotoBitmap() {
//        try {
            mLog.e("PhotoUtils.imageFile.getAbsolutePath()=" + PhotoUtils.imageFile.getAbsolutePath());
            int degree = readPictureDegree();
        Bitmap b = getBitmapFromFile(imageFile);
//        Bitmap b = compressBitmap(zoomImg(imageFile,400,600));
//            Bitmap b1 = new Compressor(App.getContext()).compressToBitmap(imageFile);

//            Bitmap b= zoomImg(new Compressor(App.getContext()).compressToBitmap(imageFile),480,640);
//            Bitmap b = new Compressor(App.getContext())
//                    .setMaxWidth(480)
//                    .setMaxHeight(640)
//                    .setQuality(50)
//                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                    .compressToBitmap(imageFile);
//            seve(b,"test");
            mLog.e("=======1==========" );
//            Bitmap b= new Compressor(App.getContext()).compressToBitmap(imageFile);
//            uploadfile(new Compressor(App.getContext()).compressToFile(imageFile));
            Bitmap bitmap = rotaingImageView(degree, b );
//            sevephoto(bitmap);
            mPicName = Utils.getFileName(imageFile.getPath());
            photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            return bitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
    private static void uploadfile(File F){
        List<KeyValue> list = new ArrayList<KeyValue>();
        list.add(new KeyValue("file",F));
//        RP.addBodyParameter("upload", F);

        RequestParams RP = new RequestParams( Constants.HTTP_UploadPicture);
        MultipartBody body=new MultipartBody(list,"UTF-8");
        RP.setRequestBody(body);
        mLog.e("RP=" +  RP.toString());
        HttpUtils.UploadFile(RP, new HttpUtils.HttpPostUpLoadFileCallBack() {
            public void UpLoadFilePostCallBack(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        mLog.e("result=" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.showToast("JSON解析出错");
                        }
                    } else {
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mLog.e("Http访问结果：" + Finish);
                }
            }
        });
    }
    public static void seve(final Bitmap bitmap,final String Name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(Environment.getExternalStorageDirectory()
                        + "/Registration/" +Name + ".webp");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static Bitmap getPhotoBitmap(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//        Bitmap b = zoomImg(bitmap, 600, 800);
        photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
        mLog.e("2Bitmap.W=" + bitmap.getWidth());
        mLog.e("2Bitmap.H=" + bitmap.getHeight());
        return bitmap;
    }

    /**
     * 图片与屏幕的宽高差距比例
     *
     * @param mActivity
     * @param options
     * @return
     */
    public static int calculateInSampleSize(Activity mActivity, Bitmap options) {
        // 源图片的高度和宽度
        int bitmapheight = options.getHeight();
        int bitmapwidth = options.getWidth();
        int inSampleSize = 1;
        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        mLog.e("bitmapheight=" + bitmapheight + "   bitmapwidth=" + bitmapwidth);
        if (bitmapheight > height || bitmapwidth > width) {
            // 计算出实际宽高和目标宽高的比率
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > height && (halfWidth / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        } else {

            int heightRatio = Math.round(height / bitmapheight);
            int widthRatio = Math.round(width / bitmapwidth);
            mLog.e("h缩放比=" + heightRatio);
            mLog.e("w缩放比=" + widthRatio);
            if (heightRatio > widthRatio) {
                inSampleSize = widthRatio;
            } else {
                inSampleSize = heightRatio;
            }
        }
        mLog.e("缩放比=" + inSampleSize);
        return inSampleSize;
    }

    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            mLog.e("文件不存在!");
        }
        return size;
    }

    public static Bitmap zoomImg(File F, int newWidth, int newHeight) {
        Bitmap bm = BitmapFactory.decodeFile(F.getAbsolutePath());
        mLog.e("Bitmap.W=" + bm.getWidth());
        mLog.e("Bitmap.H=" + bm.getHeight());
        try {
            mLog.e("BitmapSize=" + getFileSize(F));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zoomImg(bm, newWidth, newHeight);
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        mLog.e("Bitmap.W=" + bm.getWidth());
        mLog.e("Bitmap.H=" + bm.getHeight());
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    public static int getRatioSize(int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageHeight = 800;
        int imageWidth = 600;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    public static void compressImageToFile(Bitmap bmp, File file) {
        // 0-100 100为不压缩
        int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap compressBitmap(File F) {
        return compressBitmap(BitmapFactory.decodeFile(F.getAbsolutePath()));
    }

    public static Bitmap compressBitmap(Bitmap image) {
        // 最大图片大小 100KB
        int maxSize = 100;
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(image.getWidth(), image.getHeight());
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio, image.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image, null, rect, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // JNI调用保存图片到SD卡 这个关键
//        saveBitmap(result, options, filePath, true);
        // 释放Bitmap
        return result;
    }

}

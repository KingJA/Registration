/*
 * File Name: 		ACamera.java
 * 
 * Copyright(c) 2011 Yunmai Co.,Ltd.
 * 
 * 		 All rights reserved.
 * 					
 */

package com.yunmai.android.idcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tdr.registration.R;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.yunmai.android.base.BaseActivity;
import com.yunmai.android.engine.OcrEngine;
import com.yunmai.android.other.CameraManager;
import com.yunmai.android.vo.IDCard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 拍摄图像
 *
 * @author fangcm
 */
public class ACamera extends BaseActivity implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Button camera_shutter_a;
    private Button camera_recog;
    private Button camera_flash;
    private CameraManager mCameraManager;
    private List<String> flashList;
    private int flashPostion = 0;
    private byte[] idcardA = null;
    private IDCard idCard;
    public ZProgressHUD zProgressHUD;
    private String url;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 抓取图像成功
                idcardA = msg.getData().getByteArray("picData");
            } else {
                Toast.makeText(ACamera.this, R.string.camera_take_picture_error, Toast.LENGTH_SHORT).show();
            }
            camera_shutter_a.setEnabled(true);
            mCameraManager.initDisplay();
            request();
        }

    };
    private Handler mOcrHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            zProgressHUD.dismiss();
            switch (msg.what) {
                case OcrEngine.RECOG_FAIL:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_blur, Toast.LENGTH_SHORT).show();
                    break;
                case OcrEngine.RECOG_BLUR:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_blur, Toast.LENGTH_SHORT).show();
                    break;
                case OcrEngine.RECOG_OK:
                    String img = String.valueOf(msg.obj);
                    Intent intent = new Intent();
                    mLog.e(img);
                    intent.putExtra("name", idCard.getName());
                    intent.putExtra("card", idCard.getCardNo());
                    intent.putExtra("sex",idCard.getSex());
                    intent.putExtra("birth",idCard.getBirth());
                    intent.putExtra("address", idCard.getAddress());
                    intent.putExtra("img", url);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case OcrEngine.RECOG_IMEI_ERROR:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_imei, Toast.LENGTH_SHORT).show();
                    break;
                case OcrEngine.RECOG_FAIL_CDMA:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_cdma, Toast.LENGTH_SHORT).show();
                    break;
                case OcrEngine.RECOG_LICENSE_ERROR:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_licens, Toast.LENGTH_SHORT).show();
                    break;
                case OcrEngine.RECOG_TIME_OUT:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_time_out, Toast.LENGTH_SHORT).show();
                    break;
                case OcrEngine.RECOG_ENGINE_INIT_ERROR:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_engine_init, Toast.LENGTH_SHORT).show();
                    break;
                case OcrEngine.RECOG_COPY:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_fail_copy, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(ACamera.this, R.string.reco_dialog_blur, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_camera);
        mCameraManager = new CameraManager(ACamera.this, mHandler);
        zProgressHUD = new ZProgressHUD(this);
        zProgressHUD.setMessage("识别中");
        zProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mLog.e("sd:"+Environment.getExternalStorageDirectory().getAbsolutePath());

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/text.txt");
        try {
            file.createNewFile();
            mLog.e("sd", 1 + "");
        } catch (IOException e) {
            e.printStackTrace();
            mLog.e("sd", 2 + "");
        }
        initViews();
//		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/ccyxtest1/");
//		if(!file.exists()){
//			file.mkdirs();
//		}
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        idcardA = null;
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        mCameraManager.initDisplay();
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        } else {
            setResult(2000);
        }
    }

    private void initViews() {
        // find view
        camera_shutter_a = (Button) findViewById(R.id.camera_shutter_a);
        camera_recog = (Button) findViewById(R.id.camera_recog);
        camera_flash = (Button) findViewById(R.id.camera_flash);
        camera_shutter_a.setOnClickListener(mLsnClick);
        camera_recog.setOnClickListener(mLsnClick);
        camera_flash.setOnClickListener(mLsnClick);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(ACamera.this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private OnClickListener mLsnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.camera_shutter_a:
                    camera_shutter_a.setEnabled(false);
                    mCameraManager.setTakeIdcardA();
                    mCameraManager.requestFocuse();
//						Toast.makeText(ACamera.this, "请拍摄证件正面", Toast.LENGTH_LONG).show();
//						return;
                    break;
                case R.id.camera_recog:
                    if (idcardA == null) {
                        Toast.makeText(ACamera.this, "请拍摄证件正面", Toast.LENGTH_LONG).show();
                        return;
                    }
                    zProgressHUD.show();
                    request();
                    break;
                case R.id.camera_flash:
                    flashPostion++;
                    if (flashPostion < flashList.size()) {
                        setFlash(flashList.get(flashPostion));
                    } else {
                        flashPostion = 0;
                        setFlash(flashList.get(flashPostion));
                    }
                    break;
            }
        }

    };

    private void setFlash(String flashModel) {
        mCameraManager.setCameraFlashMode(flashModel);
        if (flashModel.equals(Parameters.FLASH_MODE_ON)) {
            camera_flash.setText("闪光灯开");
        } else if (flashModel.equals(Parameters.FLASH_MODE_OFF)) {
            camera_flash.setText("闪光灯关");
        } else {
            camera_flash.setText("闪光灯自动");
        }
    }

    private List<String> getSupportFlashModel() {
        List<String> list = new ArrayList<String>();
        if (mCameraManager.isSupportFlash(Parameters.FLASH_MODE_OFF)) {
            list.add(Parameters.FLASH_MODE_OFF);
        }
        if (mCameraManager.isSupportFlash(Parameters.FLASH_MODE_ON)) {
            list.add(Parameters.FLASH_MODE_ON);
        }
        if (mCameraManager.isSupportFlash(Parameters.FLASH_MODE_AUTO)) {
            list.add(Parameters.FLASH_MODE_AUTO);
        }
        return list;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // Debug.i(TAG, "surfaceCreated");
        try {
            mCameraManager.openCamera(holder);
            flashList = getSupportFlashModel();
            if (flashList == null || flashList.size() == 0) {
                camera_flash.setText("闪光灯无法设置");
                camera_flash.setEnabled(false);
            } else {
                setFlash(flashList.get(0));
            }
            if (!mCameraManager.isSupportAutoFocus()) {
                Toast.makeText(getBaseContext(), "不支持自动对焦！", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(ACamera.this, R.string.camera_open_error,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (width > height) {
            mCameraManager.setPreviewSize(width, height);
        } else {
            mCameraManager.setPreviewSize(height, width);
        }
        mCameraManager.initDisplay();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.closeCamera();
    }

    /**
     * 处理
     */
    private void request() {
        zProgressHUD.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OcrEngine ocrEngine = new OcrEngine();
                try {
                    byte[] data = idcardA;
                    saveImg(data);
                    String headPath = newPath();//头像图片路径
//					String headPath = "";
//					idCard = ocrEngine.recognize(ACamera.this, data, null,headPath);
//					if (idCard.getRecogStatus() == OcrEngine.RECOG_OK) {
//						mOcrHandler.sendMessage(mOcrHandler.obtainMessage(OcrEngine.RECOG_OK, headPath));
//					}else {
//						mOcrHandler.sendEmptyMessage(idCard.getRecogStatus());
//					}
//				} catch (Exception e) {
//					mOcrHandler.sendEmptyMessage(OcrEngine.RECOG_FAIL);
//				}
//				OcrEngine ocrEngine = new OcrEngine();
//				try {
//					byte[] data = getIntent().getByteArrayExtra("idcardA");
//			saveImg(data);
//			String headPath = newHeadPath();//头像图片路径

//					String headPath = "";
                    mLog.d("recognize==>", "开始识别");
                    idCard = ocrEngine.recognize(ACamera.this, data, null, headPath);

                    mLog.d("recognize==>", "结束识别");
                    if (idCard.getRecogStatus() == OcrEngine.RECOG_OK) {
                        mOcrHandler.sendMessage(mOcrHandler.obtainMessage(OcrEngine.RECOG_OK, headPath));
                    } else {
                        mOcrHandler.sendEmptyMessage(idCard.getRecogStatus());
                    }
                } catch (Exception e) {
                    mOcrHandler.sendEmptyMessage(OcrEngine.RECOG_FAIL);
                }
            }
        }).start();
    }

    private void saveImg(byte[] data) {
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            url = Environment.getExternalStorageDirectory().getPath() + "/ccidimg/" + "shibbie.jpg";
            File imageFile = new File(url);
//			if(imageFile.exists()){
//				imageFile.delete();
//			}
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ccidimg/");
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (bm != null) {
                bm.recycle();
                bm = null;
            }
        }
    }

    public String newImageName() {
        return "ccyx" + new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + ".jpg";
    }

    public static String newHeadPath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/idheadimg/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return Environment.getExternalStorageDirectory().getPath() + "/idheadimg/" + getTime("yyMMddHHmmssSSS") + ".bmp";
    }

    public static String newPath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ccidimg/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return Environment.getExternalStorageDirectory().getPath() + "/ccidimg/" + "touxiang" + ".bmp";
    }

    private static String getTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

}
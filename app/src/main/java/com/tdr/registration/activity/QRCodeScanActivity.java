package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tdr.registration.R;
import com.tdr.registration.activity.normal.RegisterFirstNormalActivity2;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * 二维码扫描
 */
@ContentView(R.layout.activity_scan_qrcode)
public class QRCodeScanActivity extends Activity implements QRCodeView.Delegate, View.OnClickListener {
    @ViewInject(R.id.ZXV_ScanQRCode)
    private QRCodeView ZXV_ScanQRCode;

    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;

    @ViewInject(R.id.IV_Light)
    private ImageView IV_Light;

    @ViewInject(R.id.TV_Input)
    private TextView TV_Input;

    private Activity mActivity;
    private int ScanType=0;
    private boolean isShow;
    private boolean isPlateNumber;
    private String ButtonName;
    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;
    private boolean Light = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        AutoLight();
    }

    private void initview() {
        mActivity = this;
        IV_Back.setOnClickListener(this);
        IV_Light.setOnClickListener(this);
        TV_Input.setOnClickListener(this);

        ZXV_ScanQRCode.setDelegate(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            ScanType = bundle.getInt("ScanType");
            isShow = bundle.getBoolean("isShow");
            isPlateNumber = bundle.getBoolean("isPlateNumber");
            ButtonName = bundle.getString("ButtonName");
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (isShow) {
            TV_Input.setVisibility(View.VISIBLE);
            String locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
            if (locCityName.contains("南宁")) {
                TV_Input.setText("输入标签编码");
            } else if (locCityName.contains("龙岩")) {
                TV_Input.setText("输入二维码");
            }
            if (ButtonName != null && !ButtonName.equals("")) {
                TV_Input.setText(ButtonName);
            }
        }
        if(ScanType==0){
            ZXV_ScanQRCode.changeToScanQRCodeStyle();//扫二维码
        }else{
            ZXV_ScanQRCode.changeToScanBarcodeStyle();
        }

//        ZXV_ScanQRCode.startSpot();//开始扫描
        ZXV_ScanQRCode.startSpotAndShowRect();//开始识别显示扫描框
    }
    public void AutoLight(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 8) {
            Light=false;
            //早上
        } else if (hour >= 8 && hour < 11) {
            Light=false;
            //上午
        } else if (hour >= 11 && hour < 13) {
            Light=false;
            //中午
        } else if (hour >= 13 && hour < 18) {
            Light=false;
            //下午
        } else {
            Light=true;
            //晚上
        }
        if(Light){
            ZXV_ScanQRCode.openFlashlight();//开灯
        }else{
            ZXV_ScanQRCode.closeFlashlight();//关灯
        }
        IV_Light.setBackgroundResource(Light?R.mipmap.light_on:R.mipmap.light_off);
        mLog.e("1Light="+Light);
    }
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    private void dialogShow(int flag) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View inputView = mInflater.inflate(R.layout.layout_input_plate, null);
            final TextView textPlateNumber = (TextView) inputView.findViewById(R.id.text_plateNumber);
            final EditText editPlateNumber = (EditText) inputView.findViewById(R.id.edit_plateNumber);
            final TextView textPlateNumberConfirm = (TextView) inputView.findViewById(R.id.text_plateNumberConfirm);
            final EditText editPlateNumberConfirm = (EditText) inputView.findViewById(R.id.edit_plateNumberConfirm);
            if (!isPlateNumber) {
                textPlateNumber.setText("二维码内容");
                textPlateNumberConfirm.setText("确认二维码");
            }
            editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
            editPlateNumberConfirm.setTransformationMethod(new AllCapTransformationMethod(true));
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(null)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(inputView, mActivity).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    if (editPlateNumber.getText().toString().trim().equals("") || editPlateNumberConfirm.getText().toString().trim().equals("")) {
                        Utils.myToast(mActivity, "请输入信息");
                    } else if (editPlateNumber.getText().toString().toUpperCase().trim().equals(editPlateNumberConfirm.getText().toString().toUpperCase().trim())) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result", editPlateNumber.getText().toString().toUpperCase().trim());
                        resultIntent.putExtra("isPlateNumber", "0");
                        resultIntent.putExtra("isScan", true);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Utils.myToast(mActivity, "两次信息输入不一致，请重新输入");
                    }
                }
            }).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ZXV_ScanQRCode.startCamera();
        ZXV_ScanQRCode.showScanRect();
    }

    @Override
    protected void onStop() {
        ZXV_ScanQRCode.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ZXV_ScanQRCode.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        ZXV_ScanQRCode.startSpot();
        Intent intent = new Intent();
        intent.putExtra("result", result);
        intent.putExtra("isPlateNumber", "1");
        setResult(RESULT_OK, intent);
//        Log.e("Pan","扫描结果="+result);
        finish();

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        mLog.e("打开相机出错");
    }

    public void setScan() {
        ZXV_ScanQRCode.startSpot();//开始扫描
        ZXV_ScanQRCode.stopSpot();//停止扫描
        ZXV_ScanQRCode.startSpotAndShowRect();//开始识别显示扫描框
        ZXV_ScanQRCode.stopSpotAndHiddenRect();//暂停识别隐藏扫描框
        ZXV_ScanQRCode.showScanRect();//显示扫描框
        ZXV_ScanQRCode.hiddenScanRect();//隐藏扫描框
        ZXV_ScanQRCode.startCamera();//开始预览
        ZXV_ScanQRCode.stopCamera();//停止预览
        ZXV_ScanQRCode.openFlashlight();//开灯
        ZXV_ScanQRCode.closeFlashlight();//关灯
        ZXV_ScanQRCode.changeToScanBarcodeStyle();//扫条形码
        ZXV_ScanQRCode.changeToScanQRCodeStyle();//扫二维码
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_Back:
                finish();
                break;
            case R.id.IV_Light:
                mLog.e("Light="+Light);
                if(Light){
                    Light=false;
                    ZXV_ScanQRCode.closeFlashlight();//关灯
                }else{
                    Light=true;
                    ZXV_ScanQRCode.openFlashlight();//开灯
                }
                IV_Light.setBackgroundResource(Light?R.mipmap.light_on:R.mipmap.light_off);
                break;
            case R.id.TV_Input:
                dialogShow(0);
                break;
        }
    }
}

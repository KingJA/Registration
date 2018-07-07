package com.tdr.kingja.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.kingja.supershapeview.view.SuperShapeTextView;
import com.orhanobut.logger.Logger;
import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.kingja.entity.BatteryInfo;
import com.tdr.kingja.utils.CheckUtil;
import com.tdr.kingja.utils.DialogUtil;
import com.tdr.kingja.utils.GoUtil;
import com.tdr.kingja.utils.ImageUtil;
import com.tdr.kingja.view.dialog.BaseListDialog;
import com.tdr.kingja.view.dialog.DoubleDialog;
import com.tdr.registration.R;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:电瓶更换-登记新电瓶
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryChangeActivity extends BaseTitleActivity {


    @BindView(R.id.tv_power_register_plateName)
    TextView tvPowerRegisterPlateName;
    @BindView(R.id.tv_power_register_recordId)
    TextView tvPowerRegisterRecordId;
    @BindView(R.id.tv_power_register_batteryCount)
    TextView tvPowerRegisterBatteryCount;
    @BindView(R.id.ll_power_register_batteryCount)
    LinearLayout llPowerRegisterBatteryCount;
    @BindView(R.id.et_power_register_BrandName)
    EditText etPowerRegisterBrandName;
    @BindView(R.id.tv_power_register_type)
    EditText tvPowerRegisterType;
    @BindView(R.id.tv_power_register_color)
    TextView tvPowerRegisterColor;
    @BindView(R.id.ll_power_register_color)
    LinearLayout llPowerRegisterColor;
    @BindView(R.id.tv_power_register_buyDate)
    TextView tvPowerRegisterBuyDate;
    @BindView(R.id.ll_power_register_buyDate)
    LinearLayout llPowerRegisterBuyDate;
    @BindView(R.id.et_power_register_buyPrice)
    EditText etPowerRegisterBuyPrice;
    @BindView(R.id.tv_power_register_tagId)
    TextView tvPowerRegisterTagId;
    @BindView(R.id.iv_power_register_scan)
    ImageView ivPowerRegisterScan;
    @BindView(R.id.et_power_register_remark)
    EditText etPowerRegisterRemark;
    @BindView(R.id.iv_power_register_photo1)
    ImageView ivPowerRegisterPhoto1;
    @BindView(R.id.iv_power_register_photo2)
    ImageView ivPowerRegisterPhoto2;
    @BindView(R.id.iv_power_register_photo3)
    ImageView ivPowerRegisterPhoto3;
    @BindView(R.id.stv_power_register)
    SuperShapeTextView stvPowerRegister;
    //    private CarRegisterInfo carRegisterInfo;
    private TimePickerView timePickerView;
    private static final int REQUEST_BATTERY_COUNT = 0x01;
    private static final int REQUEST_PHOTO1 = 0x02;
    private static final int REQUEST_PHOTO2 = 0x03;
    private static final int REQUEST_PHOTO3 = 0x04;
    private static final int REQUEST_SCANNIN_QR_CODE = 0x08;
    private String colorId;
    private BaseListDialog colorSelector;
    private String photo1;
    private List<BikeCode> colorList = new ArrayList<>();
    private DbManager db;
    private String recordId;
    private BatteryInfo batteryInfo;
    private String photo2;
    private String photo3;


    @OnClick({R.id.ll_power_register_batteryCount, R.id.ll_power_register_buyDate, R.id.ll_power_register_color, R.id
            .iv_power_register_photo1, R.id.iv_power_register_photo2, R.id.iv_power_register_photo3,R.id.iv_power_register_scan, R.id.stv_power_register})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_power_register_batteryCount:
                GoUtil.goActivityForResult(this, BatteryCountSelectActivity.class, REQUEST_BATTERY_COUNT);
                break;
            case R.id.ll_power_register_color:
                colorSelector.show();
                break;
            case R.id.ll_power_register_buyDate:
                timePickerView.show();
                break;
            case R.id.iv_power_register_photo1:
                takePhoto(REQUEST_PHOTO1);
                break;
            case R.id.iv_power_register_photo2:
                takePhoto(REQUEST_PHOTO2);
                break;
            case R.id.iv_power_register_photo3:
                takePhoto(REQUEST_PHOTO3);
                break;
            case R.id.iv_power_register_scan:
                goScanCamera(0, true, false, "请输入二维码");
                break;
            case R.id.stv_power_register:
                registerBattery();
                break;
            default:
                break;
        }
    }

    private void registerBattery() {
        final String batteryCount = tvPowerRegisterBatteryCount.getText().toString().trim();
        final String batteryBrandName = etPowerRegisterBrandName.getText().toString().trim();
        final String batteryType = tvPowerRegisterType.getText().toString().trim();
        String batteryColor = tvPowerRegisterColor.getText().toString().trim();
        final String batteryBuyDate = tvPowerRegisterBuyDate.getText().toString().trim();
        final String batteryTheftNo = tvPowerRegisterTagId.getText().toString().trim();
        final String batteryPrice = etPowerRegisterBuyPrice.getText().toString().trim();
        final String remark = etPowerRegisterRemark.getText().toString().trim();

        Log.e(TAG, "batteryPrice: " + batteryPrice);
        Log.e(TAG, "remark: " + remark);

        if (!CheckUtil.checkEmpty(batteryCount, "请选择电池节数")
                || !CheckUtil.checkEmpty(batteryBrandName, "请输入品牌")
                || !CheckUtil.checkEmpty(batteryType, "请输入型号")
                || !CheckUtil.checkEmpty(batteryColor, "请选择颜色")
                || !CheckUtil.checkEmpty(batteryBuyDate, "请选择购买时间")
                || !CheckUtil.checkEmpty(photo1, "请上传照片")) {
            return;
        }

        DoubleDialog doubleDialog = new DoubleDialog(this, "提示", "是否回收旧电瓶","不回收","回收");
        doubleDialog.setOnDoubleClickListener(new DoubleDialog.OnDoubleClickListener() {
            @Override
            public void onCancle() {
                doChange(batteryCount, batteryBrandName, batteryType, batteryBuyDate, batteryTheftNo, batteryPrice, remark,"2");
            }

            @Override
            public void onConfirm() {
                doChange(batteryCount, batteryBrandName, batteryType, batteryBuyDate, batteryTheftNo, batteryPrice, remark,"1");
            }
        });
        doubleDialog.show();
    }

    private void doChange(String batteryCount, String batteryBrandName, String batteryType, String batteryBuyDate,
                          String batteryTheftNo, String batteryPrice, String remark,String operate) {
        showProgress(true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("BATTERYID", batteryInfo.getBATTERYID());
        map.put("ECID", batteryInfo.getECID());
        map.put("PLATENUMBER", batteryInfo.getPLATENUMBER());
        map.put("OWNER_NAME", batteryInfo.getOWNER_NAME());
        map.put("OWNER_CARDID", batteryInfo.getOWNER_CARDID());
        map.put("OWNER_CARDTYPE", batteryInfo.getOWNER_CARDTYPE() + "");
        map.put("OWNER_PHONE", batteryInfo.getOWNER_PHONE());
        map.put("OWNER_ADDRESS", batteryInfo.getOWNER_ADDRESS());
        map.put("UNITID", batteryInfo.getUNITID());
        map.put("VehicleType", batteryInfo.getVehicleType());
        map.put("BATTERY_RECORDID", recordId);
        map.put("BATTERY_THEFTNO", batteryTheftNo);
        map.put("VEHICLEBRAND", batteryBrandName);
        map.put("VEHICLEMODELS", batteryType);
        map.put("COLORID", colorId);
        map.put("BATTERY_QUANTITY", batteryCount);
        map.put("BUYDATE", batteryBuyDate);
        map.put("PRICE", batteryPrice);
        map.put("PHOTOLIST", getPhotoList());
        map.put("REMARK", remark);
        map.put("operate", operate);
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_ChangeBatter);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        HttpUtils.postK(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                showProgress(false);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    String data = jsonObject.getString("Data");
                    if (errorCode == 0) {
                        DialogUtil.showSuccess(BatteryChangeActivity.this,"电瓶更换成功");
                    } else {
                        ToastUtil.showToast(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex) {
                showProgress(false);
            }
        });
    }

    private String getPhotoList() {
        StringBuffer sb = new StringBuffer(photo1);
        if (!TextUtils.isEmpty(photo2)) {
            sb.append(",").append(photo2);
        }
        if (!TextUtils.isEmpty(photo3)) {
            sb.append(",").append(photo3);
        }
        return sb.toString();
    }

    private void takePhoto(int requestCode) {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), requestCode);
    }


    /**
     * 扫码
     *
     * @param ScanType   扫描类型
     * @param showInput  是否显示录入框
     * @param isPlate    是否扫描车牌
     * @param ButtonName 按钮文本
     */
    private void goScanCamera(int ScanType, boolean showInput, boolean isPlate, String ButtonName) {
        Bundle bundle = new Bundle();
        bundle.putInt("ScanType", ScanType);
        bundle.putBoolean("isShow", showInput);
        bundle.putBoolean("isPlateNumber", isPlate);
        bundle.putString("ButtonName", ButtonName);
        ActivityUtil.goActivityForResultWithBundle(this, QRCodeScanActivity.class, bundle,
                REQUEST_SCANNIN_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_BATTERY_COUNT:
                    String count = data.getStringExtra("count");
                    tvPowerRegisterBatteryCount.setText(count);
                    break;
                case REQUEST_PHOTO1:
                    Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                    ivPowerRegisterPhoto1.setImageBitmap(bitmap1);
                    photo1 = ImageUtil.bitmapToBase64(bitmap1);
                    break;
                case REQUEST_PHOTO2:
                    Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
                    ivPowerRegisterPhoto2.setImageBitmap(bitmap2);
                    photo2 = ImageUtil.bitmapToBase64(bitmap2);
                    break;
                case REQUEST_PHOTO3:
                    Bitmap bitmap3 = (Bitmap) data.getExtras().get("data");
                    ivPowerRegisterPhoto3.setImageBitmap(bitmap3);
                    photo3 = ImageUtil.bitmapToBase64(bitmap3);
                    break;
                case REQUEST_SCANNIN_QR_CODE:
                    String result = data.getExtras().getString("result");
                    Log.e(TAG, "result: " + result);
                    tvPowerRegisterTagId.setText(result);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void initVariable() {
        batteryInfo = (BatteryInfo) getIntent().getSerializableExtra("batteryInfo");
        db = x.getDb(DBUtils.getDb());
        try {
            colorList = db.selector(BikeCode.class).where("type", "=", "4").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getContentTitle() {
        return "电瓶更换";
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_power_change;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initData() {
        tvPowerRegisterPlateName.setText(batteryInfo.getPLATENUMBER());
        initDateSelector();
        initColorSelector();
    }

    private void initColorSelector() {
        colorSelector = new BaseListDialog<BikeCode>(this, colorList) {
            @Override
            protected void fillLvData(List<BikeCode> list, int position, TextView tv) {
                tv.setText(list.get(position).getName());
            }

            @Override
            protected void onItemSelect(BikeCode colorBean) {
                tvPowerRegisterColor.setText(colorBean.getName());
                colorId = colorBean.getCode();
            }
        };
    }

    private void initDateSelector() {
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvPowerRegisterBuyDate.setText(Utils.setDate(date));
            }
        });
    }

    @Override
    protected void initNet() {
        showProgress(true);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_GetRecordNo);
        HttpUtils.get(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                showProgress(false);
                Logger.d(result);
                try {
                    JSONObject resultObject = new JSONObject(result);
                    int errorCode = resultObject.getInt("ErrorCode");
                    String data = resultObject.getString("Data");
                    if (errorCode == 0) {
                        recordId = data;
                        tvPowerRegisterRecordId.setText(data);
                    } else {
                        ToastUtil.showToast(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {
                showProgress(false);
            }
        });
    }

    public static void goActivity(Context context, BatteryInfo batteryInfo) {
        Intent intent = new Intent(context, BatteryChangeActivity.class);
        intent.putExtra("batteryInfo", batteryInfo);
        context.startActivity(intent);
    }

}

package com.tdr.kingja.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.tdr.kingja.entity.CarRegisterInfo;
import com.tdr.kingja.utils.GoUtil;
import com.tdr.kingja.utils.ImageUtil;
import com.tdr.kingja.view.dialog.BaseListDialog;
import com.tdr.registration.R;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.activity.normal.RegisterCarActivity;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:电瓶登记-登记新电瓶
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class PowerRegisterActivity extends BaseTitleActivity {


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
    @BindView(R.id.iv_power_register_photo)
    ImageView ivPowerRegisterPhoto;
    @BindView(R.id.stv_power_register)
    SuperShapeTextView stvPowerRegister;
    private CarRegisterInfo carRegisterInfo;
    private TimePickerView timePickerView;
    private static final int REQUEST_BATTERY_COUNT = 0x01;
    private static final int REQUEST_CAMERA = 0x02;
    private static final int REQUEST_SCANNIN_QR_CODE = 0x03;
    private String colorId;
    private BaseListDialog colorSelector;
    private String photoBase64;
    private List<BikeCode> colorList = new ArrayList<>();
    private DbManager db;

    @OnClick({R.id.ll_power_register_batteryCount, R.id.ll_power_register_buyDate, R.id.ll_power_register_color, R.id
            .iv_power_register_photo, R.id.iv_power_register_scan})
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
            case R.id.iv_power_register_photo:
                takePhoto();
                break;
            case R.id.iv_power_register_scan:
                goScanCamera(0, true, false, "请输入二维码");
                break;
            default:
                break;
        }
    }


    private void takePhoto() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
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
                case REQUEST_CAMERA:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivPowerRegisterPhoto.setImageBitmap(bitmap);
                    photoBase64 = ImageUtil.bitmapToBase64(bitmap);
                    break;

                case REQUEST_SCANNIN_QR_CODE:
                    String result = data.getExtras().getString("result");
                    Log.e(TAG, "result: "+result );
                    tvPowerRegisterTagId.setText(result);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void initVariable() {
        carRegisterInfo = (CarRegisterInfo) getIntent().getSerializableExtra("carRegisterInfo");
        db = x.getDb(DBUtils.getDb());
        try {
            colorList = db.selector(BikeCode.class).where("type", "=", "4").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getContentTitle() {
        return "登记新电瓶";
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_power_register;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initData() {
        tvPowerRegisterPlateName.setText(carRegisterInfo.getPlateNumber());
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
                colorId = colorBean.getCodeId();
                Log.e(TAG, "colorId: " + colorId);
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

    public static void goActivity(Context context, CarRegisterInfo carRegisterInfo) {
        Intent intent = new Intent(context, PowerRegisterActivity.class);
        intent.putExtra("carRegisterInfo", carRegisterInfo);
        context.startActivity(intent);
    }

}

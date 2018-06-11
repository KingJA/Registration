package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆查询
 */
public class CarCheckActivity extends BaseActivity {
    private static final String TAG = "CarCheckActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_scanPlateNumber)
    ImageView imageScanPlateNumber;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.linear_plateNumber)
    LinearLayout linearPlateNumber;
    @BindView(R.id.edit_engineNumber)
    EditText editEngineNumber;
    @BindView(R.id.edit_shelvesNumber)
    EditText editShelvesNumber;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Context mContext;
    private ZProgressHUD mProgressHUD;
    private Intent intent;

    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码扫描回调
    private ParsingQR mQR;
    private String locCityName = "";
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carcheck);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
        intent = new Intent();
        mQR = new ParsingQR();
        initView();
    }

    private void initView() {
        textTitle.setText("车辆查询");
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        if (locCityName.contains("天津")) {
            editPlateNumber.setHint("请输入电动自行车车牌");
        }
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
    }

    @OnClick({R.id.image_back, R.id.image_scanPlateNumber, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.image_scanPlateNumber:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPlateNumber", true);
                bundle.putString("activity", "");
                ActivityUtil.goActivityForResultWithBundle(CarCheckActivity.this, QRCodeScanActivity.class, bundle,
                        SCANNIN_GREQUEST_CODE);
                break;
            case R.id.btn_search:
                if (TextUtils.isEmpty(editPlateNumber.getText().toString()
                        .trim())
                        && TextUtils.isEmpty(editEngineNumber.getText().toString()
                        .trim())
                        && TextUtils.isEmpty(editShelvesNumber.getText().toString()
                        .trim())) {
                    Utils.myToast(mContext, "查询条件不可全为空");
                    break;
                }
                queryPlateNumber();
                break;
            default:
                break;
        }
    }

    private void queryPlateNumber() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("W_CPH", editPlateNumber.getText().toString().toUpperCase().trim());
        map.put("W_FDJH", editEngineNumber.getText().toString().trim());
        map.put("W_CJH", editShelvesNumber.getText().toString().trim());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_CHECKSTOLENVEHICLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Logger.json(result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            int type = jsonObject.getInt("Type");
                            if (type == 1) {//黑车
                                JSONObject json = jsonObject.getJSONObject("ElectricCar");
                                dialogShow(0, "该车辆为黑车，是否查看详情？", json, 1);
                            } else if (type == 2) {
                                JSONObject json = jsonObject.getJSONObject("ElectricCar");
                                Utils.myToast(mContext, "查询成功，正在跳转到详情页...");
                                Bundle bundle = new Bundle();
                                bundle.putString("ElectricCar", json.toString());
                                bundle.putInt("type", 0);
                                ActivityUtil.goActivityWithBundle(CarCheckActivity.this, CheckShowActivity.class,
                                        bundle);
//                                finish();
                            } else if (type == 4) {
                                JSONObject json = jsonObject.getJSONObject("ElectricCar");
                                dialogShow(0, "该车辆为报废车辆，是否查看详情？", json, 0);
                            }
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(CarCheckActivity.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        Utils.myToast(mContext, "JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.myToast(mContext, "获取数据超时，请检查网络连接");
                }
            }
        });
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String message, final JSONObject json, final int type) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            dialogBuilder.withTitle("提示").withTitleColor("#333333")
                    .withMessage(message).isCancelableOnTouchOutside(false)
                    .withEffect(effectstype).withButton1Text("取消")
                    .withButton2Text("下一步")
                    .setCustomView(R.layout.custom_view, mContext)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("ElectricCar", json.toString());
                    bundle.putInt("type", type);
                    ActivityUtil.goActivityWithBundle(CarCheckActivity.this, CheckShowActivity.class, bundle);
//                    finish();
                }
            }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.myToast(mContext, "没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    String isPlateNumber = bundle.getString("isPlateNumber");
                    String plateNumberRead = mQR.plateNumber(scanResult);
                    if (isPlateNumber.equals("0")) {
                        editPlateNumber.setText(scanResult);
                    } else {
                        editPlateNumber.setText(plateNumberRead);
                    }
                    queryPlateNumber();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

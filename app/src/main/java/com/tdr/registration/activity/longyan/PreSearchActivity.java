package com.tdr.registration.activity.longyan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.activity.RegisterPersonalActivity;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预登记查询
 * 宁德（福鼎）、龙岩
 */

public class PreSearchActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Context mContext;
    private Gson mGson;
    private PreRegistrationModel model;

    private ZProgressHUD mProgressHUD;
    private Activity mActivity;
    private String locCityName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_query);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
        getRegular();
        mGson = new Gson();
        initView();
    }

    private void initView() {
        textTitle.setText("预登记查询");
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
    }

    @OnClick({R.id.image_back, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_search:
                String plateNumber = editPlateNumber.getText().toString().toUpperCase().trim();
                if (editPlateNumber.equals("")) {
                    Utils.myToast(mContext, "查询条件不可为空");
                    break;
                }
                String hasPreregister = (String) SharedPreferencesUtils.get("hasPreregister", "");
               /* if (cardTypes.equals("98")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    bundle.putString("palteNumber", plateNumber);
                    bundle.putString("cardTypes", "98");
                    ActivityUtil.goActivityWithBundle(PreSearchActivity.this, PreToOfficialFirstLongYanActivity.class, bundle);
                    finish();
                } else {*/
                mProgressHUD.show();
                HashMap<String, String> map = new HashMap<>();
                map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
                map.put("plateNumber", plateNumber);
                WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPREREGISTERS, map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            Utils.LOGE("Pan", "预登记转正式：" + result);
                            try {
                                JSONObject json = new JSONObject(result);
                                int errorCode = json.getInt("ErrorCode");
                                String data = json.getString("Data");
                                if (errorCode == 0) {
                                    if (checkJson(data) == 0) {
                                        model = mGson.fromJson(data, new TypeToken<PreRegistrationModel>() {
                                        }.getType());
                                        if(locCityName.contains("龙岩")){
                                            scanQR();
                                        }else{
                                            Bundle bundle = new Bundle();
                                            for (PhotoModel photoModel : model.getPhotoListFile()) {
                                                photoModel.setPhotoFile("");
                                            }
                                            bundle.putString("InType","Registration");
                                            bundle.putSerializable("RegistrationModel", model);
                                            SharedPreferencesUtils.put("preregisters", data);
                                            ActivityUtil.goActivityWithBundle(PreSearchActivity.this, RegisterPersonalActivity.class, bundle);

                                        }
                                    } else {
                                        dialogShow(data);
                                    }
                                    mProgressHUD.dismiss();
                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                    //AppManager.getAppManager().finishActivity(HomeActivity.class);
                                    SharedPreferencesUtils.put("token", "");
                                    ActivityUtil.goActivityAndFinish(PreSearchActivity.this, LoginActivity.class);
                                } else {
                                    Utils.myToast(mContext, data);
                                    mProgressHUD.dismiss();
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
                /*}*/
                break;
        }
    }


    private int checkJson(String json) {
        try {
            PreRegistrationModel model = mGson.fromJson(json, new TypeToken<PreRegistrationModel>() {
            }.getType());
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }

    private void scanQR() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShow", true);
        bundle.putString("activity", "tianjin");
        bundle.putString("ButtonName", "请输入二维码");
        ActivityUtil.goActivityForResultWithBundle(PreSearchActivity.this, QRCodeScanActivity.class, bundle, SCANNIN_GREQUEST_CODE);
    }


    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow( String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
                .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("重新查询")
                .setCustomView(R.layout.custom_view, mContext).withButton2Text("继续登记").setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                String VEHICLETYPE= VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE);
                if("".equals(VEHICLETYPE)){
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, "1");
                }
                ActivityUtil.goActivityAndFinish(PreSearchActivity.this, RegisterPersonalActivity.class);
            }
        }).show();

    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(PreSearchActivity.this, HomeActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码回调值*
    private String REGULAR = "";

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
                    String labelNumber = bundle.getString("result");
                    Pattern pat = Pattern.compile(REGULAR);
                    Matcher mat = pat.matcher(labelNumber + "");
                    if (!mat.matches()) {
                        Utils.myToast(mContext, "输入的二维码格式错误");
                        return;
                    } else {
                        GoPreRegistration(labelNumber);
                    }
                }
            }
        }
    }
    private void GoPreRegistration(String labelNumber){
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO, labelNumber);
        if(model.getVEHICLETYPE()==null|| model.getVEHICLETYPE().equals("")){
            model.setVEHICLETYPE("1");
        }

        Bundle bundle1 = new Bundle();
        sevephoto();
        bundle1.putString("cardTypes", "");
        bundle1.putSerializable("model", model);
        bundle1.putString("palteNumber", model.getPLATENUMBER());
        ActivityUtil.goActivityWithBundle(PreSearchActivity.this, PreToOfficialFirstLongYanActivity.class, bundle1);
        finish();
    }
    private void sevephoto() {
        for (PhotoModel photoModel : model.getPhotoListFile()) {
            SharedPreferencesUtils.put(photoModel.getPhoto(), photoModel.getPhotoFile());
            photoModel.setPhotoFile("");
        }
    }

    private void getRegular() {
        Bundle bundle = (Bundle) getIntent().getExtras();

        REGULAR = (String) SharedPreferencesUtils.get("REGULAR", "");
        if (REGULAR.equals("")) {
            DbManager db = x.getDb(DBUtils.getDb());
            String city = (String) SharedPreferencesUtils.get("locCityName", "");
//            List<BaseInfo> ResultList = db.findAllByWhere(BaseInfo.class, " cityName=\"" + city + "\"");
            List<BaseInfo> ResultList = null;
            try {
                ResultList = db.selector(BaseInfo.class).where("cityName", "=", city).findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (ResultList == null) {
                ResultList = new ArrayList<BaseInfo>();
            }
            BaseInfo BI = ResultList.get(0);
            mLog.e("AppConfig:" + BI.getAppConfig());
            try {
                JSONArray ja = new JSONArray(BI.getAppConfig());
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jsonObject = ja.getJSONObject(i);
                    mLog.e("AppConfig" + jsonObject.toString());
                    String key = jsonObject.getString("key");
                    switch (key) {
                        case "THEFTNO1_REGULAR":
                            REGULAR = jsonObject.getString("value");
                            SharedPreferencesUtils.put("REGULAR", REGULAR);
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mLog.e("REGULAR:" + REGULAR);

    }

}

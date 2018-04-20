package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.kunming.ChangeFirstKunMingActivity;
import com.tdr.registration.activity.normal.ChangeFirstNormalActivity2;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.ReturnModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆查询统一界面
 */
public class ElectricInfoSearchActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ElectricInfoSearchActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.linear_plateNumber)
    LinearLayout linearPlateNumber;
    @BindView(R.id.edit_ownerIdentity)
    EditText editOwnerIdentity;
    @BindView(R.id.linear_ownerIdentity)
    LinearLayout linearOwnerIdentity;
    @BindView(R.id.btn_search)
    Button btnSearch;

    String rolePower = "";//权限代码

    private ZProgressHUD mProgressHUD;

    private Context mContext;

    private Gson mGson;
    private ElectricCarModel model = new ElectricCarModel();
    private ReturnModel returnModel = new ReturnModel();
    private DbManager db;
    private Activity mActivity;
    private String locCityName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_info_search);
        ButterKnife.bind(this);

        db = x.getDb(DBUtils.getDb());
        mGson = new Gson();
        mContext = this;
        mActivity = this;
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        editOwnerIdentity.setTransformationMethod(new AllCapTransformationMethod(true));

        initData();

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        rolePower = bundle.getString("rolePower");
        switch (rolePower) {
            case "ChangeFirstNormal":
                textTitle.setText("信息变更");
                break;
            case "CanPolicyEdit":
                textTitle.setText("套餐变更");
                break;
            case "101":

                break;
            case "102":

                break;
            case "105":
                textTitle.setText("车牌补办");
                break;
            case "108":
                textTitle.setText("车辆过户");
                break;
            case "109":
                textTitle.setText("车辆报废");
                break;
            case "113":
                textTitle.setText("被盗申报");
                break;
            case "1300106":
                textTitle.setText("车辆发还");
                linearOwnerIdentity.setVisibility(View.GONE);
                break;
            case "114":
                textTitle.setText("服务延期");
                linearOwnerIdentity.setVisibility(View.GONE);
                break;
        }
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        if (locCityName.contains("天津")) {
            editPlateNumber.setHint("请输入电动自行车车牌");
        }
    }

    @OnClick({R.id.image_back, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.btn_search:

                String plateNumber = editPlateNumber.getText().toString().toUpperCase().trim();
                if (plateNumber.equals("")) {
                    Utils.myToast(mContext, "请输入车牌号");
                    break;
                }

                HashMap<String, String> map = new HashMap<>();
                String method = "";
                if(rolePower.equals("114")){
//
                    map.put("token", (String) SharedPreferencesUtils.get("token", ""));
                    map.put("platenumber", plateNumber);
                    method = Constants.WEBSERVER_GETCARBYPLATENUMBER;
                }else if (rolePower.equals("1300106")) {
                    map.put("token", (String) SharedPreferencesUtils.get("token", ""));
                    map.put("DataTypeCode", "GetReimInfo");
                    map.put("content", plateNumber);
                    method = Constants.WEBSERVER_INSSYS;
                } else {
                    String ownerIdentity = editOwnerIdentity.getText().toString().trim().toUpperCase();
                    if (ownerIdentity.equals("")) {
                        Utils.myToast(mContext, "请输入车主证件号码");
                        break;
                    }
                    map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
                    map.put("platenumber", plateNumber);
                    map.put("idCard", ownerIdentity);
                    if (rolePower.equals("113")) {
                        method = Constants.WEBSERVER_GETELECTRICCARBYPLATENUMBER3;
                    } else {
                        method = Constants.WEBSERVER_GETELECTRICCARBYPLATENUMBER2;
                    }

                }

                mProgressHUD.show();
                WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), method, map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            Utils.LOGE("Pan", result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    mProgressHUD.dismiss();
                                    if (rolePower.equals("1300106")) {
                                        returnModel = mGson.fromJson(data, new TypeToken<ReturnModel>() {
                                        }.getType());
                                        dialogShow();
                                    } else {
                                        model = mGson.fromJson(data, new TypeToken<ElectricCarModel>() {
                                        }.getType());
                                        dialogShow(0, "");
                                    }

                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                    //AppManager.getAppManager().finishActivity(HomeActivity.class);
                                    SharedPreferencesUtils.put("token", "");
                                    ActivityUtil.goActivityAndFinish(ElectricInfoSearchActivity.this, LoginActivity.class);
                                } else {
                                    Utils.myToast(mContext, data);
                                    mProgressHUD.dismiss();
                                }
                            } catch (JSONException e) {
                                mProgressHUD.dismiss();
                                e.printStackTrace();
                                Utils.myToast(mContext, "JSON解析出错");
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, "获取数据超时，请检查网络连接");
                        }
                    }
                });

                break;
        }
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(final int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View layoutShow = mInflater.inflate(R.layout.layout_showcar, null);
            TextView textPlateNum = (TextView) layoutShow.findViewById(R.id.text_plateNum);
            textPlateNum.setText(model.getPlateNumber());
            TextView textOwnerIdentity = (TextView) layoutShow.findViewById(R.id.text_ownerIdentity);
            textOwnerIdentity.setText(model.getCardId());
            TextView textOwnerName = (TextView) layoutShow.findViewById(R.id.text_ownerName);
            textOwnerName.setText(model.getOwnerName());
            TextView textPlateBrand = (TextView) layoutShow.findViewById(R.id.text_plateBrand);
            textPlateBrand.setText(model.getVehicleBrandName());

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(layoutShow, mContext);
            dialogBuilder.withTitle("车辆信息").withTitleColor("#333333")
                    .withButton1Text("取消").withButton2Text("下一步")
                    .withMessage(null).withEffect(effectstype)
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
                    bundle.putSerializable("model", model);
                    switch (rolePower) {
                        case "ChangeFirstNormal":
                            ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, ChangeFirstNormalActivity2.class, bundle);
                            finish();
                            break;
                        case "CanPolicyEdit":
                            boolean canPolicyEdit = model.isCanPolicyEdit();
                            Bundle bundle2 = new Bundle();
                            bundle2.putBoolean("canPolicyEdit", canPolicyEdit);
                            bundle2.putSerializable("model", model);
                            if (!canPolicyEdit) {
                                Utils.myToast(mContext, model.getPlateNumber() + " 保险超过可修改期限，不可修改");
                                return;
                            }
                            ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, InsuranceModifyActivity.class, bundle2);

                            break;
                        case "101":

                            break;

                        case "102":

                            break;

                        case "105":
                            mLog.e("车牌补办");
                            ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, CarReissueActivity2.class, bundle);
                            finish();
                            break;
                        case "108":
                            ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, CarTransferActivity.class, bundle);
                            finish();
                            break;
                        case "109":
                            ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, CarScrapActivity.class, bundle);
                            finish();
                            break;
                        case "113":
                            List<ElectricCarModel.PolicysBean> policy = model.getPOLICYS();
                            ElectricCarModel.PolicysBean ECPB = null;
                            for (ElectricCarModel.PolicysBean bean : policy) {
                                if (bean.getTYPE().equals("2")) {
                                    ECPB=bean;
                                }
                            }
                            if(ECPB!=null){
                                ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, InsuranceClaimsActivity.class, bundle);
                                finish();
                            }else{
                                Utils.myToast(mContext, "该辆车没有购买车辆被盗险");
                            }
                            break;
                        case "114":
                            ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, InsuranceRenew.class, bundle);
                            finish();
                            break;
                    }
                }
            }).show();
        }
    }

    private void dialogShow() {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
                dialogBuilder = NiftyDialogBuilder.getInstance(this);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(this);
        View layoutShow = mInflater.inflate(R.layout.layout_showcar, null);
        TextView textPlateNum = (TextView) layoutShow.findViewById(R.id.text_plateNum);
        textPlateNum.setText(returnModel.getCar().getPlateNumber());
        TextView textOwnerIdentity = (TextView) layoutShow.findViewById(R.id.text_ownerIdentity);
        textOwnerIdentity.setText(returnModel.getCar().getCardId());
        TextView textOwnerName = (TextView) layoutShow.findViewById(R.id.text_ownerName);
        textOwnerName.setText(returnModel.getCar().getOwnerName());
        TextView textPlateBrand = (TextView) layoutShow.findViewById(R.id.text_plateBrand);
        textPlateBrand.setText(returnModel.getCar().getVehicleBrandName());

        dialogBuilder.isCancelable(false);
        dialogBuilder.setCustomView(layoutShow, mContext);
        dialogBuilder.withTitle("车辆信息").withTitleColor("#333333")
                .withButton1Text("取消").withButton2Text("下一步")
                .withMessage(null).withEffect(effectstype)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();

                    }
                }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rolePower) {
                    case "1300106":
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("return", returnModel);
                        ActivityUtil.goActivityWithBundle(ElectricInfoSearchActivity.this, CarReturnActivity.class, bundle);
                        dialogBuilder.dismiss();
                        finish();
                        break;
                }
            }
        }).show();

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

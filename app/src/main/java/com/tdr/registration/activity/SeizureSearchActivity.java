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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.kunming.RegisterFirstKunMingActivity;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.DistrainModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.AppManager;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆扣押管理，查询扣押车辆
 */
public class SeizureSearchActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SeizureSearchActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.edit_seizureNumber)
    EditText editSeizureNumber;
    @BindView(R.id.edit_ownerIdentity)
    EditText editOwnerIdentity;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private ZProgressHUD mProgressHUD;
    private Context mContext;

    private List<DistrainModel> distrainModels;
    private ElectricCarModel electricCarModel = new ElectricCarModel();
    private Gson mGson;
    private Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seizuresearch);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        initView();
    }

    private void initView() {
        textTitle.setText("扣押管理");
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        editSeizureNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        editOwnerIdentity.setTransformationMethod(new AllCapTransformationMethod(true));

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
                String plateNumber = editSeizureNumber.getText().toString().toUpperCase().trim();
                /*if (plateNumber.equals("")) {
                    Utils.myToast(mContext, "请输入扣押编号");
                    break;
                }*/
                String ownerIdentity = editOwnerIdentity.getText().toString().trim().toUpperCase();
                if (plateNumber.equals("") && ownerIdentity.equals("")) {
                    Utils.myToast(mContext, "查询条件不能同时为空");
                    break;
                }
                /*if (!Utils.isIDCard18(ownerIdentity)) {
                    Utils.myToast(mContext, "输入的身份证号码格式有误");
                    break;
                }*/
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DISTRAINNO", editSeizureNumber.getText().toString().toUpperCase().trim());
            jsonObject.put("IDENTITYCARD", editOwnerIdentity.getText().toString().toUpperCase().trim());
            jsonObject.put("STATUS", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressHUD.show();
        final HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("adqJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETDISTRAINCARS, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Utils.LOGE("Pan","扣押管理："+result);
                    try {
                        JSONObject object = new JSONObject(result);
                        int errorCode = object.getInt("ErrorCode");
                        String data = object.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            distrainModels = mGson.fromJson(data, new TypeToken<List<DistrainModel>>() {
                            }.getType());
                            Bundle bundle = new Bundle();
                            ArrayList list = new ArrayList();
                            list.add(distrainModels);
                            bundle.putParcelableArrayList("distrain", list);
                            ActivityUtil.goActivityWithBundle(SeizureSearchActivity.this, SeizureListActivity.class, bundle);
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(SeizureSearchActivity.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    private void dialogShow(int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View layoutShow = mInflater.inflate(R.layout.layout_show_distrain, null);
            TextView textOwnerName = (TextView) layoutShow.findViewById(R.id.text_ownerName);
            textOwnerName.setText(distrainModels.get(0).getOwnerName());
            TextView textOwnerIdentity = (TextView) layoutShow.findViewById(R.id.text_ownerIdentity);
            textOwnerIdentity.setText(distrainModels.get(0).getIdentityCard());
            TextView textPlateBrand = (TextView) layoutShow.findViewById(R.id.text_plateBrand);
            textPlateBrand.setText(distrainModels.get(0).getVehicleBrandName());
            TextView textCarColor = (TextView) layoutShow.findViewById(R.id.text_carColor);
            textCarColor.setText(distrainModels.get(0).getColorName());
            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(layoutShow, mContext);
            dialogBuilder.withTitle("扣押信息").withTitleColor("#333333")
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
                    bundle.putSerializable("model", electricCarModel);
                    String cityName = (String) SharedPreferencesUtils.get("locCityName", "");
                    if (cityName.contains("昆明")) {
                        ActivityUtil.goActivityWithBundle(SeizureSearchActivity.this, RegisterFirstKunMingActivity.class, bundle);
                        finish();
                    }
                }
            }).show();
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

package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.ui.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.AppManager;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 保险查询
 */

public class InsuranceQueryActivity extends BaseActivity {
    private static final String TAG = "InsuranceQueryActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Context mContext;
    private ZProgressHUD mProgressHUD;
    private ElectricCarModel model;
    private Gson mGson;
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_query);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        initView();
    }

    private void initView() {
        textTitle.setText("保险查询");
        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));

        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
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
                if (editPlateNumber.equals("")) {
                    Utils.myToast(mContext, "查询条件不可为空");
                    break;
                }
                mProgressHUD.show();
                final HashMap<String, String> map = new HashMap<>();
                map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
                map.put("platenumber", plateNumber);
                WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETELECTRICCARBYPLATENUMBER, map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("result"+result);
                        Utils.LOGE("Pan","result="+result);
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    model = mGson.fromJson(data, new TypeToken<ElectricCarModel>() {
                                    }.getType());
                                    boolean canPolicyEdit = model.isCanPolicyEdit();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("canPolicyEdit", canPolicyEdit);
                                    bundle.putSerializable("model", model);
                                    if (!canPolicyEdit) {
                                        Utils.myToast(mContext, model.getPlateNumber() + " 保险超过可修改期限，不可修改");
                                    }
                                    ActivityUtil.goActivityWithBundle(InsuranceQueryActivity.this, InsuranceModifyActivity.class, bundle);
                                    finish();
                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                    //AppManager.getAppManager().finishActivity(HomeActivity.class);
                                    SharedPreferencesUtils.put("token","");
                                    ActivityUtil.goActivityAndFinish(InsuranceQueryActivity.this, LoginActivity.class);
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
                break;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

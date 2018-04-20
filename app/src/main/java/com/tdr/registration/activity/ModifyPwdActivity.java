package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppManager;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class ModifyPwdActivity extends BaseActivity {
    private static final String TAG = "ModifyPwdActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.edit_oldPwd)
    EditText editOldPwd;
    @BindView(R.id.edit_newPwd)
    EditText editNewPwd;
    @BindView(R.id.edit_confirmPwd)
    EditText editConfirmPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Context mContext;

    private ZProgressHUD mProgressHUD;
    private Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypwd);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        initView();
    }

    private void initView() {
        textTitle.setText("修改密码");
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
    }


    @OnClick({R.id.image_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.btn_submit:
                String olderPwd = editOldPwd.getText().toString();
                if (olderPwd.equals("")) {
                    Utils.myToast(mContext, "请输入旧密码");
                    break;
                }
                String newPwd = editNewPwd.getText().toString();
                String confirmPwd = editConfirmPwd.getText().toString();
                if (newPwd.equals("")) {
                    Utils.myToast(mContext, "请输入新密码");
                    break;
                }
                if (confirmPwd.equals("")) {
                    Utils.myToast(mContext, "请再次输入密码");
                    break;
                }
                if (!newPwd.equals(confirmPwd)) {
                    Utils.myToast(mContext, "输入的两次密码不一致");
                    break;
                }
                mProgressHUD.show();
                HashMap<String, String> map = new HashMap<>();
                map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
                map.put("oldpwd", olderPwd);
                map.put("newpwd", newPwd);
                WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHANGEPWD, map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            try {
                                JSONObject json = new JSONObject(result);
                                int errorCode = json.getInt("ErrorCode");
                                String data = json.getString("Data");
                                if (errorCode == 0 || errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                    //AppManager.getAppManager().finishActivity(HomeActivity.class);
                                    SharedPreferencesUtils.put("token","");
                                    ActivityUtil.goActivityAndFinish(ModifyPwdActivity.this, LoginActivity.class);
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

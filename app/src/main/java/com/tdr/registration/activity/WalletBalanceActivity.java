package com.tdr.registration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.model.WalletModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;

/**
 * 钱包余额
 */
@ContentView(R.layout.activity_wallet_balance)
public class WalletBalanceActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;

    @ViewInject(R.id.TV_Balance)
    private TextView TV_Balance;

    @ViewInject(R.id.LL_FrozenBalance)
    private LinearLayout LL_FrozenBalance;

    @ViewInject(R.id.TV_FrozenBalance)
    private TextView TV_FrozenBalance;

    @ViewInject(R.id.RL_OrderDetails)
    private RelativeLayout RL_OrderDetails;

    @ViewInject(R.id.RL_PresentationDetails)
    private RelativeLayout RL_PresentationDetails;

    @ViewInject(R.id.TV_Withdrawals)
    private TextView TV_Withdrawals;


    private Activity mActivity;
    private ZProgressHUD mProgressDialog;
    private Gson mGson;
    private WalletModel WM;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        GetWallet();
    }

    private void initview() {
        mActivity = this;
        mProgressDialog = new ZProgressHUD(this);
        mProgressDialog.setMessage("");
        mProgressDialog.setSpinnerType(2);
        mGson=new Gson();
        IV_Back.setOnClickListener(this);
        LL_FrozenBalance.setOnClickListener(this);
        RL_OrderDetails.setOnClickListener(this);
        RL_PresentationDetails.setOnClickListener(this);
        TV_Withdrawals.setOnClickListener(this);

    }
    public void GetWallet() {
        mProgressDialog.show();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("UserID",(String) SharedPreferencesUtils.get("userId", ""));
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants.HTTP_GETWALLET);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        mLog.e("GetWallet:" + JB.toString());
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    mLog.e("result:" + result);
                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                WM = mGson.fromJson(data, new TypeToken<WalletModel>() {
                                }.getType());
                                TV_Balance.setText(Utils.fmtMicrometer(WM.getCWABLEBAL()));
                                TV_FrozenBalance.setText(Utils.fmtMicrometer(WM.getBALANCE()));
                                mProgressDialog.dismiss();
                            }else{
                                Utils.showToast(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressDialog.dismiss();
                            Utils.showToast("JSON解析出错");
                        }
                    } else {
                        mProgressDialog.dismiss();
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mProgressDialog.dismiss();
                    mLog.e("Http访问结果：" + Finish);

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetWallet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_Back:
                finish();
                break;
            case R.id.LL_FrozenBalance:
                Utils.showToast("未出账金额");
                break;
            case R.id.RL_OrderDetails:
                ActivityUtil.goActivity(mActivity, OrderListActivity.class);
                break;
            case R.id.RL_PresentationDetails:
                ActivityUtil.goActivity(mActivity, WithdrawalsListActivity.class);
                break;
            case R.id.TV_Withdrawals:
                if(WM!=null){
                    if(Double.parseDouble(WM.getCWABLEBAL())<=0){
                        Utils.showToast("可提现的余额不足");
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putString("Cwablebal",WM.getCWABLEBAL());
                        ActivityUtil.goActivityWithBundle(mActivity, WithdrawalsApplyActivity.class,bundle);
                    }
                }else{
                    Utils.showToast("您尚未开通钱包功能");
                }
                break;

        }
    }
}

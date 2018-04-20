package com.tdr.registration.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.policys;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.PayResult;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.MyRadioButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 支付页面
 */

@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.IV_Back)
    ImageView IV_Back;
    @ViewInject(R.id.TV_CarPlate)
    TextView TV_CarPlate;
    @ViewInject(R.id.TV_PayMoney)
    TextView TV_PayMoney;
    @ViewInject(R.id.TV_Pay)
    TextView TV_Pay;
    @ViewInject(R.id.LL_InsuranceList)
    LinearLayout LL_InsuranceList;
    @ViewInject(R.id.LL_PayTypeList)
    LinearLayout LL_PayTypeList;

    @ViewInject(R.id.MRB_AliPay)
    MyRadioButton MRB_AliPay;


    @ViewInject(R.id.LL_Pay_Success)
    LinearLayout LL_Pay_Success;
    @ViewInject(R.id.btn_submit)
    Button btn_submit;

    private Activity mActivity;
    private Gson mGson;
    private List<PayInsurance> PIList;
    private String UnPaid = "0";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        LL_Pay_Success.setVisibility(View.VISIBLE);
//                        ActivityUtil.goActivityAndFinish(PayActivity.this, HomeActivity.class);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdate();
    }

    //    final String orderInfo = "app_id=2017052307316905&biz_content=%7b%22body%22%3a%22%e6%88%91%e6%98%af%e6%b5%8b%e8%af%95%e6%95%b0%e6%8d%ae%22%2c%22out_trade_no%22%3a%2220170216test01%22%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%2c%22seller_id%22%3a%22tendencycw%40163.com%22%2c%22subject%22%3a%22App%e6%94%af%e4%bb%98%e6%b5%8b%e8%af%95DoNet%22%2c%22timeout_express%22%3a%2230m%22%2c%22total_amount%22%3a%220.01%22%7d&charset=UTF-8&format=json&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2017-05-25+16%3a40%3a37&version=1.0&sign=hOrUhL1fkbyUs1%2bINY2ngWmIRT7I19C9Svu1wOOwHputllLp8kAlxiPZriEuqen%2bVAakHfZ2GW%2fxz5H8BoDGy%2f1ExF4nZ%2f4qbyrgxayI2lAAZuYTx7SePh%2fT2yeZMG42QT91fAGyDXX98vtioIzeE3u8lJRpHHYwvTvGzjLWRKr2G5ieycZj1OWAFugkFZLh5jlZSBr8IOjGDupkkxpXK2Yer14FJIw6arPxZVceRFT5Plib7qZL4NEOVzZpVbv3YlNIwfmybbvoEOovDn2GA%2bXOsqtf3qeLGdl%2fuPqKws5FVc0fJmJMk0M7IXtvzfbS446CkP4QZaU7DuMZqHLc2Q%3d%3d";
    private void pay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                mLog.e("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void initview() {

        mActivity = this;
        mGson = new Gson();
        PIList = new ArrayList<PayInsurance>();
        MRB_AliPay.setSelected(true);
        TV_Pay.setOnClickListener(this);
        LL_Pay_Success.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        IV_Back.setOnClickListener(this);
    }

    private void initdate() {
        Bundle bundle = (Bundle) getIntent().getExtras();
        UnPaid = bundle.getString("UnPaid");
        String PayDate = bundle.getString("PayDate");
        if (PayDate != null && !PayDate.equals("")) {
            PIList = mGson.fromJson(PayDate, new TypeToken<List<PayInsurance>>() {
            }.getType());
        }else{
            ArrayList list = bundle.getParcelableArrayList("PayDate");
            PIList = (List<PayInsurance>) list.get(0);
        }

        for (PayInsurance pi : PIList) {
            for (int i = 0; i < pi.getPolicys().size(); i++) {
                AddInsuranceView(getInsuranceView(pi.getPolicys().get(i)));
            }
        }
        if (PIList.size() > 0) {
            mLog.e("总金额" + PIList.get(0).getTotal_Amount());
            TV_CarPlate.setText(PIList.get(0).getPlateNumber());
            TV_PayMoney.setText(FormatAmount(PIList.get(0).getTotal_Amount()));
        }
    }

    private View getInsuranceView(policys PIp) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.item_pay, null);
        TextView InsuranceName = (TextView) v.findViewById(R.id.TV_InsuranceName);
        TextView InsuranceExplain = (TextView) v.findViewById(R.id.TV_InsuranceExplain);
        TextView InsuranceAmount = (TextView) v.findViewById(R.id.TV_InsuranceAmount);

        InsuranceName.setText( PIp.getDEADLINE()==null|| PIp.getDEADLINE().equals("")||PIp.getDEADLINE().equals("null")?PIp.getTypeName():PIp.getTypeName() + "(" + PIp.getDEADLINE() + "年)");

        InsuranceExplain.setText(PIp.getSubTitle());
        InsuranceAmount.setText("¥" + FormatAmount(PIp.getPRICE()));
        return v;
    }

    private String FormatAmount(String Amount) {
        double a = Double.parseDouble(Amount);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }

    private void AddInsuranceView(View v) {
        LL_InsuranceList.addView(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TV_Pay:
                if (MRB_AliPay.isSelected()) {
                    pay(PIList.get(0).getContent());
//                    Utils.myToast(mActivity, "进入付款");
                } else {
                    Utils.myToast(mActivity, "请选择付款方式");
                }
                break;
            case R.id.IV_Back:
                if (UnPaid.equals("1")) {
                    finish();
                } else {
                    ActivityUtil.goActivityAndFinish(PayActivity.this, HomeActivity.class);
                }
                break;
            case R.id.LL_Pay_Success:
                break;
            case R.id.btn_submit:
                ActivityUtil.goActivityAndFinish(PayActivity.this, HomeActivity.class);
                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ActivityUtil.goActivityAndFinish(PayActivity.this, HomeActivity.class);
        }
        return true;
    }
}

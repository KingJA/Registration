package com.tdr.registration.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.UnPaidListAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PayResult;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 未支付订单列表
 */

@ContentView(R.layout.activity_unpaid)
public class UnpaidActivity extends BaseActivity {

    @ViewInject(R.id.RV_UnPaidList)
    RecyclerView RV_UnPaidList;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.image_back)
    ImageView image_back;

    @ViewInject(R.id.RL_UnPaid_Loding)
    RelativeLayout RL_UnPaid_Loding;
    @ViewInject(R.id.TV_UnPaid_min)
    TextView TV_UnPaid_min;

    @ViewInject(R.id.RL_UnPaid_NoData)
    RelativeLayout RL_UnPaid_NoData;


    private UnPaidListAdapter UPLA;
    private Activity mActivity;
    private Gson mGson;
    private List<PayInsurance> PIL;
    private PayInsurance PI;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String UnPaid;


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
                        PIL.remove(PI);
                        UPLA.UpDate(PIL);
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(UnpaidActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UnpaidActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        Bundle bundle = (Bundle) getIntent().getExtras();
        UnPaid = bundle.getString("UnPaid");
        if (UnPaid != null && UnPaid.equals("2")) {
//            String PayDate = bundle.getString("PayDate");
            ArrayList list = bundle.getParcelableArrayList("PayDate");
            PIL = (List<PayInsurance>) list.get(0);
//                PIL = mGson.fromJson(PayDate, new TypeToken<List<PayInsurance>>() {
//                }.getType());
            if (PIL != null) {
                for (int i = 0; i < PIL.size(); i++) {
                    mLog.e("EcId" + PIL.get(i).getEcId());
                }
                UPLA.UpDate(PIL);
            }
            RL_UnPaid_Loding.setVisibility(View.GONE);
        } else {
            UnPaid = "0";
            initdate();
        }
    }

    private void pay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(UnpaidActivity.this);
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
        PIL = new ArrayList<>();
        textTitle.setText("待付款");
        TV_UnPaid_min.setText("获取待支付列表...");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_UnPaidList.setLayoutManager(linearLayoutManager);
        RV_UnPaidList.setHasFixedSize(true);
        RV_UnPaidList.addItemDecoration(new RecyclerViewItemDecoration());
        UPLA = new UnPaidListAdapter(mActivity, PIL);
        UPLA.setOnItemClickLitener(new UnPaidListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                Utils.myToast(mActivity, "付款" + position);
                PI = PIL.get(position);
                List<PayInsurance> L = new ArrayList<PayInsurance>();
                L.add(PI);
                Bundle bundle = new Bundle();
                bundle.putString("UnPaid", "1");
                bundle.putString("PayDate", mGson.toJson(L));
                ActivityUtil.goActivityWithBundle(UnpaidActivity.this, PayActivity.class, bundle);

//                pay(PI.getContent());

            }
        });
        RV_UnPaidList.setAdapter(UPLA);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (UnPaid.equals("2")) {
            ActivityUtil.goActivityAndFinish(UnpaidActivity.this, HomeActivity.class);
        } else {
            finish();
        }
    }

    private void initdate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("PAYNO", "");
        map.put("PLATENUMBER", "");
        map.put("STATUS", "0");
        RL_UnPaid_Loding.setVisibility(View.VISIBLE);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPAYBILLLIST, map,
                new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            try {
                                JSONObject JB = new JSONObject(result);
                                String data = JB.getString("Data");
                                Utils.LOGE("Pan", data);
                                PIL = mGson.fromJson(data, new TypeToken<List<PayInsurance>>() {
                                }.getType());
                                if (PIL != null) {
                                    for (int i = 0; i < PIL.size(); i++) {
                                        mLog.e("EcId" + PIL.get(i).getEcId());
                                    }
                                    UPLA.UpDate(PIL);
                                }
                                RL_UnPaid_Loding.setVisibility(View.GONE);
                                if(PIL.size()==0){
                                    RL_UnPaid_NoData.setVisibility(View.VISIBLE);
                                }else{
                                    RL_UnPaid_NoData.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }


}

package com.tdr.registration.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Description:TODO
 * Create Time:2018/4/20 16:28
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class PayQcodeActivity extends BaseActivity {
    private TextView mTvTotalAmount;
    private ImageView mIvQrcode;
    private TextView mTvPlateNumber;
    private TextView mTextTitle;
    private String content;
    private String totalAmount;
    private String plateNumber;
    private String payNo;
    private Callback.Cancelable cancelable;
    private LinearLayout mLlPaySuccess;
    private Button mBtnSubmit;
    private Handler httpHandler;
    private HttpRunnable httpRunnable;
    private ImageView mIvBack;

    public static int FORM_REGISTER = 1;
    public static int FORM_ORDER_LIST = 2;
    private int from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_qcode);
        initVariable();
        initView();
        initData();
        initNet();
    }

    private void initNet() {
        checkPayed();
    }

    private void initVariable() {
        content = getIntent().getStringExtra("content");
        totalAmount = getIntent().getStringExtra("totalAmount");
        plateNumber = getIntent().getStringExtra("plateNumber");
        from = getIntent().getIntExtra("from", 1);
        payNo = getIntent().getStringExtra("payNo");
    }

    private void initView() {
        mTextTitle = findViewById(R.id.text_title);
        mTvTotalAmount = findViewById(R.id.tv_totalAmount);
        mIvQrcode = findViewById(R.id.iv_qrcode);
        mTvPlateNumber = findViewById(R.id.tv_plateNumber);
        mLlPaySuccess = findViewById(R.id.ll_pay_success);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mIvBack = findViewById(R.id.image_back);
        httpHandler = new Handler();
        httpRunnable = new HttpRunnable();
    }

    private void initData() {
        mTextTitle.setText("扫码支付");
        mTvTotalAmount.setText("¥ " + totalAmount);
        mTvPlateNumber.setText(plateNumber);
        CreateQRCode createqrcode = new CreateQRCode();
        createqrcode.execute(content);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayQcodeActivity.this, HomeActivity.class));
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void finishCurrentActivity() {
        if (from == FORM_ORDER_LIST) {
            finish();
        } else {
            startActivity(new Intent(PayQcodeActivity.this, HomeActivity.class));
        }
    }

    public static void goActivity(Context context, String content, String totalAmount, String plateNumber, String
            payNo, int from) {
        Intent intent = new Intent(context, PayQcodeActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("plateNumber", plateNumber);
        intent.putExtra("payNo", payNo);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    private class CreateQRCode extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return QRCodeEncoder.syncEncodeQRCode(params[0], BGAQRCodeUtil.dp2px(PayQcodeActivity.this, 150));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mIvQrcode.setImageBitmap(bitmap);
            } else {
                Utils.showToast("生成二维码失败");
            }
        }
    }

    public void checkPayed() {
        RequestParams rp = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_GetPayBill);
        rp.addBodyParameter("payno", payNo);
        cancelable = HttpUtils.get(rp, new HttpUtils.HttpGetCallBack() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    if (errorCode == 0) {
                        int data = jsonObject.getInt("Data");
                        if (data == 0) {
                            //未支付
                            httpHandler.postDelayed(httpRunnable, 5000);
                        } else {
                            //已支付
                            cancelable.cancel();
                            mLlPaySuccess.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //错误
                        String errorMsg = jsonObject.getString("Data");
                        Utils.showToast(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {
                Utils.showToast("网络异常");
                httpHandler.post(httpRunnable);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cancelable != null) {
            cancelable.cancel();
        }
        httpHandler.removeCallbacks(httpRunnable);
    }

    class HttpRunnable implements Runnable {

        @Override
        public void run() {
            checkPayed();
        }
    }

    @Override
    public void onBackPressed() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("确认离开该页面？")
                .isCancelableOnTouchOutside(false).withEffect(NiftyDialogBuilder.Effectstype.Fadein).withButton1Text
                ("取消")
                .setCustomView(R.layout.custom_view, this).withButton2Text("确认").setButton1Click(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                finishCurrentActivity();
            }
        }).show();
    }
}

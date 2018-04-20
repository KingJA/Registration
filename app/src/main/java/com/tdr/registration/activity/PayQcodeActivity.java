package com.tdr.registration.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.WithdrawalsModel;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.List;

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
        payNo = getIntent().getStringExtra("payNo");
    }

    private void initView() {
        mTextTitle = findViewById(R.id.text_title);
        mTvTotalAmount = findViewById(R.id.tv_totalAmount);
        mIvQrcode = findViewById(R.id.iv_qrcode);
        mTvPlateNumber = findViewById(R.id.tv_plateNumber);
    }

    private void initData() {
        mTextTitle.setText("扫码支付");
        mTvTotalAmount.setText("¥ " + totalAmount);
        mTvPlateNumber.setText(plateNumber);
        CreateQRCode createqrcode = new CreateQRCode();
        createqrcode.execute(content);
    }

    public static void goActivity(Context context, String content, String totalAmount, String plateNumber, String payNo) {
        Intent intent = new Intent(context, PayQcodeActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("plateNumber", plateNumber);
        intent.putExtra("payNo", payNo);
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
        RequestParams rp = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants.HTTP_GetPayBill);
        rp.addBodyParameter("payno", payNo);
        HttpUtils.get(rp, new HttpUtils.HttpGetCallBack() {
            @Override
            public void getcallback(String Finish, String paramString) {
                if (Finish.equals(HttpUtils.Success)) {
                    mLog.e("paramString:" + paramString);
                } else {
                    mLog.e("Http访问结果：" + Finish);
                }
            }
        });
    }
}

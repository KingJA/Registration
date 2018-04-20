package com.tdr.registration.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.CashierInputFilter;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;

/**
 * 钱包提现
 */
@ContentView(R.layout.acitivity_withdrawals_apply)
public class WithdrawalsApplyActivity extends Activity {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;

    @ViewInject(R.id.ET_Name)
    private EditText ET_Name;

    @ViewInject(R.id.ET_Phone)
    private EditText ET_Phone;

    @ViewInject(R.id.ET_PayID)
    private EditText ET_PayID;

    @ViewInject(R.id.ET_Money)
    private EditText ET_Money;
    @ViewInject(R.id.ET_Money_Point)
    private EditText ET_Money_Point;


    @ViewInject(R.id.BT_Next)
    private Button BT_Next;

    @ViewInject(R.id.TV_Prompt)
    private TextView TV_Prompt;
    private ZProgressHUD mProgressDialog;
    private String Cwablebal;
    private Activity mActivity;
    private boolean NeedPoint = false;
    private SpannableString spannableString;
    private SpannableString spannableString2;
    private ForegroundColorSpan colorSpan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
    }

    private void initview() {

        IV_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BT_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ET_Name.getText().toString().trim();
                String phont = ET_Phone.getText().toString().trim();
                String payid = ET_PayID.getText().toString().trim();
                String money = ET_Money.getText().toString().trim();
                if (name.equals("")) {
                    Utils.showToast(ET_Name.getHint().toString());
                    return;
                }
                if (phont.equals("")) {
                    Utils.showToast(ET_Phone.getHint().toString());
                    return;
                }
                if (payid.equals("")) {
                    Utils.showToast(ET_PayID.getHint().toString());
                    return;
                }
                if (money.equals("")) {
                    Utils.showToast(ET_Money.getHint().toString());
                    return;
                }
                double a = Double.parseDouble(money);
                double b = Double.parseDouble(Cwablebal);
                if (a > b) {
                    Utils.showToast("您提现的金额已超出可提现金额，请重新输入");
                    return;
                }
                CWAPPLY(name, phont, payid, money);
            }
        });

        setMoneyInputType();

    }

    private void initdata() {
        mActivity = this;
        mProgressDialog = new ZProgressHUD(this);
        mProgressDialog.setMessage("");
        mProgressDialog.setSpinnerType(2);
        Bundle bundle = (Bundle) getIntent().getExtras();
        Cwablebal = bundle.getString("Cwablebal");

        String money = "可提现金额" + Utils.fmtMicrometer(Cwablebal) + "元";
        colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        spannableString = new SpannableString(money);
        spannableString.setSpan(colorSpan, 5, spannableString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString2 = new SpannableString("金额已超过可提现余额");
        spannableString2.setSpan(colorSpan, 0, spannableString2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        TV_Prompt.setText(spannableString);

    }

    private void setMoneyInputType() {
        if (NeedPoint) {
            ET_Money_Point.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            InputFilter[] IF = {new CashierInputFilter()};
            ET_Money_Point.setFilters(IF);
            ET_Money_Point.setVisibility(View.VISIBLE);
            ET_Money.setVisibility(View.GONE);
        } else {
            ET_Money.setVisibility(View.VISIBLE);
            ET_Money_Point.setVisibility(View.GONE);
        }


        ET_Money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    double a = Double.parseDouble(s.toString());
                    double b = Double.parseDouble(Cwablebal);
                    if (a > b) {
                        TV_Prompt.setText(spannableString2);
                    } else {
                        TV_Prompt.setText(spannableString);
                    }
                }
            }
        });

        ET_Money_Point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    double a = Double.parseDouble(s.toString());
                    double b = Double.parseDouble(Cwablebal);
                    if (a > b) {
                        TV_Prompt.setText(spannableString2);
                    } else {
                        TV_Prompt.setText(spannableString);
                    }
                }
            }
        });
    }

    public void CWAPPLY(String name, String phont, String payid, String money) {
//        {"PAYEENAME":"申请人","PAYEEPHONE":"申请人手机","PAYEEALIPAY":"支付宝帐号","APPLYAMOUNT":"提款金额"}
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PAYEENAME", name);
        map.put("PAYEEPHONE", phont);
        map.put("PAYEEALIPAY", payid);
        map.put("APPLYAMOUNT", money);
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim()+ Constants.HTTP_CWAPPLY);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        mLog.e("CWAPPLY:" + JB.toString());
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
                                dialogShow(data);
                            } else {
                                Utils.showToast(data);
                            }
                            mProgressDialog.dismiss();
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

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        dialogBuilder.withTitle("提示")
                .setCustomView(R.layout.custom_view, mActivity)
                .withTitleColor("#333333")
                .withMessage(msg)
                .isCancelableOnTouchOutside(false)
                .withEffect(effectstype)
                .withButton1Text("確定")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        ActivityUtil.goActivityAndFinish(WithdrawalsApplyActivity.this, WithdrawalsListActivity.class);
                    }
                }).show();

    }

}

package com.tdr.registration.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆报废
 */
public class CarScrapActivity extends BaseActivity {
    private static final String TAG = "CarScrapActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_PlateNumber)
    TextView textPlateNumber;
    @BindView(R.id.text_brand)
    TextView textBrand;
    @BindView(R.id.text_color)
    TextView textColor;
    @BindView(R.id.text_owner)
    TextView textOwner;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_identityShow)
    TextView textIdentityShow;
    @BindView(R.id.edit_ownerName)
    EditText editOwnerName;
    @BindView(R.id.text_scrapTime)
    TextView textScrapTime;
    @BindView(R.id.edit_scrapRemarks)
    EditText editScrapRemarks;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.text_deal)
    TextView textDeal;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.relative_title)
    RelativeLayout relativeTitle;

    private Context mContext;
    private ElectricCarModel model = new ElectricCarModel();

    private ZProgressHUD mProgressHUD;

    private TimePickerView timePickerView;
    private Activity mActivity;
    private boolean CheckTime = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carscrap);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        initData();
    }

    private void initData() {
        textTitle.setText("车辆报废");
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.model = (ElectricCarModel) bundle.getSerializable("model");
        }
        textPlateNumber.setText(model.getPlateNumber());
        textBrand.setText(model.getVehicleBrandName());
        textOwner.setText(model.getOwnerName());
        textPhone.setText(model.getPhone1());
        textIdentityShow.setText(model.getCardId());
        textColor.setText(model.getColorName());

        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textScrapTime.setText(Utils.setDate(date));
                Utils.CheckBuyTime(textScrapTime.getText().toString(), new Utils.GetServerTime() {
                    @Override
                    public void ServerTime(String ST,boolean Check) {

                        CheckTime = Check;
                    }
                });
            }
        });
    }

    private Dialog dateDialog;// 日期选择弹出框
    private Calendar c = null;

    @Override
    protected Dialog onCreateDialog(int id) {
        if (dateDialog != null && dateDialog.isShowing())
            return dateDialog;
        switch (id) {
            case R.id.text_scrapTime:
                c = Calendar.getInstance();
                dateDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                String m = (month + 1) < 10 ? "0" + (month + 1)
                                        : "" + (month + 1);
                                String d = dayOfMonth < 10 ? "0" + dayOfMonth : ""
                                        + dayOfMonth;
                                textScrapTime.setText(year + "-" + m + "-" + d);
                            }
                        }, c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
        }
        return dateDialog;
    }

    @OnClick({R.id.image_back, R.id.text_scrapTime,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.text_scrapTime:
                timePickerView.show();
                break;
            case R.id.btn_submit:
                if (!checkData()) {
                    break;
                }
                dialogShow(0, "");
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
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("确认报废该车辆？")
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, mContext).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    sendMsg();
                }
            }).show();
        }
    }

    private void sendMsg() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ECID", model.getEcId());
            jsonObject.put("PlateNumber", model.getPlateNumber());
            jsonObject.put("SCRAPMAN",
                    textOwner.getText().toString().trim());
            jsonObject.put("CardId", model.getCardId());
            jsonObject.put("SCRAPTIME", textScrapTime.getText().toString().trim());
            jsonObject.put("SCRAPREMARK", editScrapRemarks.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CARSCRAPPED, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e("车辆报废："+result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int error = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (error == 0) {
                            Utils.myToast(mContext, data);
                            mProgressHUD.dismiss();
                            onBackPressed();
                        } else if (error == 1) {
                            mProgressHUD.dismiss();
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(CarScrapActivity.this, LoginActivity.class);
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
    }

    private boolean checkData() {
        String ownerName = editOwnerName.getText().toString().trim();
        if (ownerName.equals("")) {
            Utils.myToast(mContext, "请输入车主姓名");
            return false;
        }
        String scrapTime = textScrapTime.getText().toString().trim();
        if (scrapTime.equals("")) {
            Utils.myToast(mContext, "请选择报废时间");
            return false;
        }
        if (!CheckTime) {
            Utils.myToast(mContext, "您选择的时间已超过当前时间");
            return false;
        }
        return true;
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

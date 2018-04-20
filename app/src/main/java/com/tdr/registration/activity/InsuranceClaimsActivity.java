package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.tdr.registration.R;
import com.tdr.registration.activity.kunming.JustFuckForKMActivity;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 保险理赔
 */

public class InsuranceClaimsActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_plateNumber)
    TextView textPlateNumber;
    @BindView(R.id.text_vehicleMissTime)
    TextView textVehicleMissTime;
    @BindView(R.id.text_ownerName)
    TextView textOwnerName;
    @BindView(R.id.text_ownerCardType)
    TextView textOwnerCardType;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.text_ownerIdentity)
    TextView textOwnerIdentity;
    @BindView(R.id.text_ownerPhone)
    TextView textOwnerPhone;
    @BindView(R.id.edit_agentName)
    EditText editAgentName;
    @BindView(R.id.edit_claimsName)
    EditText editClaimsName;
    @BindView(R.id.relative_credentials)
    ImageView relativeCredentials;
    @BindView(R.id.text_credentials)
    TextView textCredentials;
    @BindView(R.id.linear_identity)
    LinearLayout linearIdentity;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.edit_bankCardOwner)
    EditText editBankCardOwner;
    @BindView(R.id.edit_bankCardNumber)
    EditText editBankCardNumber;
    @BindView(R.id.relative_cert)
    ImageView relativeCert;
    @BindView(R.id.text_cert)
    TextView textCert;
    @BindView(R.id.linear_cert)
    LinearLayout linearCert;

    private Context mContext;
    private Activity mActivity;
    private String photoStr = "";
    private String photoCredentialsStr = "";//证明照片
    private String photoCertStr = "";//理赔登记

    private ZProgressHUD mProgressHUD;
    private ElectricCarModel model = new ElectricCarModel();
    private String ecId = "";

    private TimePickerView timePickerView;
    private boolean CheckTime=false;
    private DbManager db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_claims);
        ButterKnife.bind(this);
        mActivity=this;
        mContext = this;
        db = x.getDb(DBUtils.getDb());
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textVehicleMissTime.setText(Utils.setDate(date));
                Utils.CheckBuyTime(textVehicleMissTime.getText().toString(), new Utils.GetServerTime() {
                    @Override
                    public void ServerTime(String ST, boolean Check) {
                        CheckTime = Check;
                    }
                });
            }
        });
        initData();
    }

    private void initData() {
        textTitle.setText("被盗申报");
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.model = (ElectricCarModel) bundle.getSerializable("model");
            ecId = model.getEcId();
            textPlateNumber.setText(model.getPlateNumber());
            textOwnerName.setText(model.getOwnerName());

            List<BikeCode> cardList=null;
            try {
                cardList = db.selector(BikeCode.class).where("type", "=", "6").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (cardList == null) {
                cardList = new ArrayList<BikeCode>();
            }

            Log.e("Pan","cardType:"+model.getCARDTYPE());
            for (BikeCode bikeCode : cardList) {
                if(model.getCARDTYPE().equals(bikeCode.getCode())){
                    textOwnerCardType.setText(bikeCode.getName());
                }
            }
            textOwnerIdentity.setText(model.getCardId());
            textOwnerPhone.setText(model.getPhone1());
        }

    }

    @OnClick({R.id.image_back, R.id.relative_credentials, R.id.relative_cert, R.id.text_vehicleMissTime, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.relative_credentials:
                PhotoUtils.TakePicture(mActivity,"credentials");
                break;
            case R.id.relative_cert:
                PhotoUtils.TakePicture(mActivity,"cert");
                break;
            case R.id.text_vehicleMissTime:
                timePickerView.show();
                break;
            case R.id.btn_next:
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        String plateNumber = textPlateNumber.getText().toString().trim();
       /* if (plateNumber.equals("")) {
            Utils.myToast(mContext, "请输入防盗车牌号");
            return;
        }*/
        String vehicleMissTime = textVehicleMissTime.getText().toString();
        if (vehicleMissTime.equals("")) {
            Utils.myToast(mContext, "请选择车辆丢失时间");
            return;
        }

        if (!CheckTime) {
            if (Utils.ServerTime == null || Utils.ServerTime.equals("")) {
                Utils.myToast(mContext, "获取服务器时间异常。");
            } else {
                Utils.myToast(mContext, "您选择的时间已超过当前时间");
            }
            return ;
        }

        String ownerName = textOwnerName.getText().toString();
        /*if (ownerName.equals("")) {
            Utils.myToast(mContext, "请输入车主姓名");
            return;
        }*/
        String ownerIdentity = textOwnerIdentity.getText().toString();
        /*if (ownerIdentity.equals("")) {
            Utils.myToast(mContext, "请输入车主身份证");
            return;
        }*/
        String ownerPhone = textOwnerPhone.getText().toString();
        if (ownerPhone.equals("")) {
            Utils.myToast(mContext, "请输入车主联系方式");
            return;
        }

        String bankCardOwner = editBankCardOwner.getText().toString();
        if (bankCardOwner.equals("")) {
            Utils.myToast(mContext, "请输入卡片户主姓名");
            return;
        }
        String bankCardNumber = editBankCardNumber.getText().toString();
        if (bankCardNumber.equals("")) {
            Utils.myToast(mContext, "请输入银行卡卡号");
            return;
        }
        String agentName = editAgentName.getText().toString();
        if (agentName.equals("")) {
            Utils.myToast(mContext, "请输入经办人姓名");
            return;
        }
        if (photoStr.equals("")) {
            Utils.myToast(mContext, "请拍摄证明照片");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EcId", ecId);
            jsonObject.put("PlateNumber", plateNumber);
            jsonObject.put("OwnerName", ownerName);
            jsonObject.put("CARDTYPE", model.getCARDTYPE());
            jsonObject.put("CardId", ownerIdentity);
            jsonObject.put("Phone", ownerPhone);
            jsonObject.put("InsurerType", model.getPOLICYS().get(0).getINSURERTYPE());
            jsonObject.put("BuyDate", model.getBuyDate());
            jsonObject.put("LoseDate", vehicleMissTime);
            jsonObject.put("PCSPhotoCERT", photoCredentialsStr);
            jsonObject.put("PCSOperator", agentName);
            jsonObject.put("CityCode", (String) SharedPreferencesUtils.get("regionNo", ""));
            jsonObject.put("XQName", (String) SharedPreferencesUtils.get("regionName", ""));
            jsonObject.put("PCSName", "");
            jsonObject.put("OPERATORUNITNAME", (String) SharedPreferencesUtils.get("regionName", ""));
            jsonObject.put("BANKCARDOWNER", bankCardOwner);
            jsonObject.put("BANKCARDNO", bankCardNumber);
            jsonObject.put("PHOTOCERT", photoCertStr);

            jsonObject.put("CERTRemark", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEWBSERVER_UPLOADOUTTIMECERT, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            dialogShow(0, "车牌号：" + model.getPlateNumber() + "  申请理赔成功！");
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                int degree = PhotoUtils.readPictureDegree();
                Bitmap bitmap = PhotoUtils.rotaingImageView(degree,
                        PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile));
                photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
                String path = PhotoUtils.imageFile.getPath();
                String name = Utils.getFileName(path);
                Drawable bd = new BitmapDrawable(bitmap);
                switch (name) {
                    case "credentials":
                        relativeCredentials.setBackground(bd);
                        textCredentials.setTextColor(getResources().getColor(R.color.white));
                        photoCredentialsStr = photoStr;
                        break;
                    case "cert":
                        relativeCert.setBackground(bd);
                        textCert.setTextColor(getResources().getColor(R.color.white));
                        photoCertStr = photoStr;
                        break;
                }
            }
        }
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(message)
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
                    onBackPressed();
                }
            }).show();
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(InsuranceClaimsActivity.this, HomeActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}

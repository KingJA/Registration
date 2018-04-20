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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.activity.kunming.JustFuckForKMActivity;
import com.tdr.registration.activity.kunming.RegisterFirstKunMingActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.gprinter.Util;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.ReturnModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆返还
 */
public class CarReturnActivity extends BaseActivity {


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
    @BindView(R.id.relative_voucher)
    ImageView relativeVoucher;
    @BindView(R.id.text_voucher)
    TextView textVoucher;
    @BindView(R.id.relative_carAndOwner)
    ImageView relativeCarAndOwner;
    @BindView(R.id.text_carAndOwner)
    TextView textCarAndOwner;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private ZProgressHUD mProgressHUD;

    private ReturnModel model = new ReturnModel();
    private ReturnModel.InfoBean infoBean = new ReturnModel.InfoBean();
    private ReturnModel.CarBean carBean = new ReturnModel.CarBean();

    private Context mContext;
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_return);
        ButterKnife.bind(this);
        mActivity=this;
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("车辆发还");
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
    }

    private void initData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.model = (ReturnModel) bundle.getSerializable("return");
            infoBean = model.getInfo();
            carBean = model.getCar();
            textPlateNumber.setText(carBean.getPlateNumber());
            textBrand.setText(carBean.getVehicleBrandName());
            textOwner.setText(carBean.getOwnerName());
            textPhone.setText(carBean.getPhone1());
            textIdentityShow.setText(carBean.getCardId());
            textColor.setText(carBean.getColorName());
        }

    }

    @OnClick({R.id.image_back, R.id.relative_voucher, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.relative_voucher:
                PhotoUtils.TakePicture(mActivity,"voucher");
                break;
            case R.id.btn_submit:
                if (!checkData()) {
                    break;
                }
                sendMsg();
                break;
        }
    }




    private boolean checkData() {
        String voucherStr = (String) SharedPreferencesUtils.get("voucher", "");
        if (voucherStr.equals("")) {
            Utils.myToast(mContext, "请拍摄返还凭单照片的照片");
            return false;
        }
       /* String carAndOwnerStr = (String) SharedPreferencesUtils.get("carAndOwner", "");
        if (carAndOwnerStr.equals("")) {
            Utils.myToast(mContext, "请拍摄人车合影照片的照片");
            return false;
        }*/
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                int degree = PhotoUtils.readPictureDegree();
                Bitmap bitmap = PhotoUtils.rotaingImageView(degree,
                        PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile));
                String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
                String path = PhotoUtils.imageFile.getPath();
                String name = Utils.getFileName(path);
                Drawable bd = new BitmapDrawable(bitmap);
                switch (name) {
                    case "voucher":
                        relativeVoucher.setBackground(bd);
                        SharedPreferencesUtils.put("voucher", photoStr);
                        textVoucher.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        }
    }

    private void sendMsg() {
        mProgressHUD.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("LISTID", infoBean.getLISTID());
            jsonObject.put("VOUCHERFile", SharedPreferencesUtils.get("voucher", ""));
            jsonObject.put("REMARK", editRemarks.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("token", (String) SharedPreferencesUtils.get("token", ""));
        map.put("DataTypeCode", "Reimbursement");
        map.put("content", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_INSSYS, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            dialogShow(0, "车牌号：" + carBean.getPlateNumber() + "  电动车发还成功！");
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

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
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
                    SharedPreferencesUtils.put("voucher", "");
                    ActivityUtil.goActivityAndFinish(CarReturnActivity.this, HomeActivity.class);
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("信息编辑中，确认离开该页面？")
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
                    Intent intent = new Intent();
                    intent.setClass(mContext, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }
            }).show();
        }

    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(CarReturnActivity.this, HomeActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

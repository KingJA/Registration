package com.tdr.registration.activity;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tdr.registration.R;
import com.tdr.registration.activity.kunming.JustFuckForKMActivity;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.imagecompress.LGImgCompressor;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.update.Util;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.AppManager;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PermissionUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆补办
 */
public class CarReissueActivity extends BaseActivity implements LGImgCompressor.CompressListener {
    private static final String TAG = "CarReissueActivity";
    @BindView(R.id.edit_labelA)
    EditText editLabelA;
    @BindView(R.id.edit_labelB)
    EditText editLabelB;
    private String[] permissionArray = new String[]{Manifest.permission.CAMERA};
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
    @BindView(R.id.check_labelA)
    CheckBox checkLabelA;
    @BindView(R.id.check_labelB)
    CheckBox checkLabelB;
    @BindView(R.id.check_plateNum)
    CheckBox checkPlateNum;
    @BindView(R.id.text_guleCar)
    TextView textGuleCar;
    @BindView(R.id.relative_guleCar)
    ImageView relativeGuleCar;
    @BindView(R.id.text_guleBattery)
    TextView textGuleBattery;
    @BindView(R.id.relative_guleBattery)
    ImageView relativeGuleBattery;
    @BindView(R.id.text_labelA)
    TextView textLabelA;
    @BindView(R.id.relative_labelA)
    ImageView relativeLabelA;
    @BindView(R.id.text_labelB)
    TextView textLabelB;
    @BindView(R.id.relative_labelB)
    ImageView relativeLabelB;
    @BindView(R.id.text_applicationForm)
    TextView textApplicationForm;
    @BindView(R.id.relative_applicationForm)
    ImageView relativeApplicationForm;
    @BindView(R.id.text_plateNum)
    TextView textPlateNum;
    @BindView(R.id.relative_plateNum)
    ImageView relativePlateNum;
    @BindView(R.id.text_identity)
    TextView textIdentity;
    @BindView(R.id.relative_identity)
    ImageView relativeIdentity;

    @BindView(R.id.text_invoice)
    TextView textInvoice;
    @BindView(R.id.relative_invoice)
    ImageView relativeInvoice;

    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Context mContext;
    private Activity mActivity;



    private ElectricCarModel model = new ElectricCarModel();

    private PhotoModel photoModel = new PhotoModel();
    private List<PhotoModel> photoModels = new ArrayList<>();
    private String photo1 = "";
    private String photo2 = "";
    private String photo3 = "";
    private String photo4 = "";
    private String photo5 = "";
    private String photo6 = "";
    private String photo7 = "";
    private String photo8 = "";

    private ZProgressHUD mProgressHUD;
    private Gson mGson;

    private int changeType = 0;
    private String city="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_reissue);
        ButterKnife.bind(this);
        mActivity=this;
        mContext = this;
        mGson = new Gson();
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        initView();
        clearImage();
        initData();
    }

    private void initView() {
        textTitle.setText("车辆补办");
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        if(city.contains("天津")){
            editPlateNumber.setHint("请输入电动自行车车牌");
        }
        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        checkPlateNum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeType += 1;
                } else {
                    changeType -= 1;
                }
            }
        });

        checkLabelA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeType += 2;
                } else {
                    changeType -= 2;
                }
            }
        });

        checkLabelB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeType += 4;
                } else {
                    changeType -= 4;
                }
            }
        });
    }

    private void clearImage() {
        Utils.ClearData();
        SharedPreferencesUtils.put("guleCar", "");
        SharedPreferencesUtils.put("guleBattery", "");
        SharedPreferencesUtils.put("labelA", "");
        SharedPreferencesUtils.put("labelB", "");
        SharedPreferencesUtils.put("plateNum", "");
        SharedPreferencesUtils.put("identity", "");
        SharedPreferencesUtils.put("applicationForm", "");
        SharedPreferencesUtils.put("invoice", "");
        SharedPreferencesUtils.put("install", "");
    }

    private void initData() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                LGImgCompressor.getInstance(this).withListener(this).
                        starCompress(Uri.fromFile(PhotoUtils.imageFile).toString(), 480, 600, 100);
            }
        }

    }

    @Override
    public void onCompressStart() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult) {
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR)//压缩失败
            return;

        File file = new File(compressResult.getOutPath());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            Drawable drawable = new BitmapDrawable(bitmap);
            String path = file.getPath();
            String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            if (path.contains("guleCar")) {
                relativeGuleCar.setBackground(drawable);
                SharedPreferencesUtils.put("guleCar", photoStr);
                textGuleCar.setTextColor(getResources().getColor(R.color.white));
            } else if (path.contains("guleBattery")) {
                relativeGuleBattery.setBackground(drawable);
                SharedPreferencesUtils.put("guleBattery", photoStr);
                textGuleBattery.setTextColor(getResources().getColor(R.color.white));
            } else if (path.contains("labelA")) {
                relativeLabelA.setBackground(drawable);
                SharedPreferencesUtils.put("labelA", photoStr);
                textLabelA.setTextColor(getResources().getColor(R.color.white));
            } else if (path.contains("labelB")) {
                relativeLabelB.setBackground(drawable);
                SharedPreferencesUtils.put("labelB", photoStr);
                textLabelB.setTextColor(getResources().getColor(R.color.white));
            } else if (path.contains("plateNum")) {
                relativePlateNum.setBackground(drawable);
                SharedPreferencesUtils.put("plateNum", photoStr);
                textPlateNum.setTextColor(getResources().getColor(R.color.white));
            } else if (path.contains("applicationForm")) {
                relativeApplicationForm.setBackground(drawable);
                SharedPreferencesUtils.put("applicationForm", photoStr);
                textApplicationForm.setTextColor(getResources().getColor(R.color.white));
            } else if (path.contains("invoice")) {
                relativeInvoice.setBackground(drawable);
                SharedPreferencesUtils.put("invoice", photoStr);
                textInvoice.setTextColor(getResources().getColor(R.color.white));
            }else if (path.contains("identity")) {
                relativeIdentity.setBackground(drawable);
                SharedPreferencesUtils.put("identity", photoStr);
                textIdentity.setTextColor(getResources().getColor(R.color.white));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.image_back, R.id.relative_guleCar, R.id.relative_guleBattery, R.id.relative_labelA, R.id.relative_labelB, R.id.relative_applicationForm, R.id.relative_plateNum, R.id.relative_invoice,R.id.relative_identity, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.relative_guleCar:
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                PhotoUtils.TakePicture(mActivity,"guleCar");
                break;
            case R.id.relative_guleBattery:
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                PhotoUtils.TakePicture(mActivity,"guleBattery");
                break;
            case R.id.relative_labelA:
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                PhotoUtils.TakePicture(mActivity,"labelA");
                break;
            case R.id.relative_labelB:
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                PhotoUtils.TakePicture(mActivity,"labelB");
                break;
            case R.id.relative_plateNum:
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                PhotoUtils.TakePicture(mActivity,"plateNum");
                break;
            case R.id.relative_identity:
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                PhotoUtils.TakePicture(mActivity,"identity");
                break;
            case R.id.relative_applicationForm:
                PhotoUtils.TakePicture(mActivity,"applicationForm");
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                break;
            case R.id.relative_invoice:
                PhotoUtils.TakePicture(mActivity,"invoice");
                PermissionUtils.checkPermissionArray(CarReissueActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                break;
            case R.id.btn_submit:
                if (!checkData()) {
                    break;
                }
                sendMsg();
                break;
        }
    }

    /**
     * 补办信息上传
     */
    private void sendMsg() {
        mProgressHUD.show();
        photo7 = (String) SharedPreferencesUtils.get("guleCar", "");
        photo8 = (String) SharedPreferencesUtils.get("guleBattery", "");
        photo5 = (String) SharedPreferencesUtils.get("labelA", "");
        photo6 = (String) SharedPreferencesUtils.get("labelB", "");
        photo4 = (String) SharedPreferencesUtils.get("plateNum", "");
        photo2 = (String) SharedPreferencesUtils.get("identity", "");
        photo1 = (String) SharedPreferencesUtils.get("applicationForm", "");
        photo3 = (String) SharedPreferencesUtils.get("invoice", "");

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EcId", model.getEcId());
            jsonObject.put("CHANGETYPE", changeType);
            jsonObject.put("PlateNumber", editPlateNumber.getText().toString().toUpperCase().trim());
            jsonObject.put("Remark", editRemarks.getText().toString());
            jsonObject.put("OwnerName", model.getOwnerName());
            jsonObject.put("CardId", model.getCardId());
            jsonObject.put("VehicleBrand", model.getVehicleBrandName());
            jsonObject.put("ColorId", model.getCardId());
            jsonObject.put("Phone1", model.getPhone1());
            jsonObject.put("ResidentAddress", model.getResidentAddress());
            if (checkLabelA.isChecked()) {
                String labelA = editLabelA.getText().toString().trim();
                jsonObject.put("THEFTNO1", labelA);
            }
            if (checkLabelB.isChecked()) {
                String labelB = editLabelB.getText().toString().trim();
                jsonObject.put("THEFTNO2", labelB);
            }
            if (!photo1.equals("")) {
                jsonObject.put("Photo1File", photo1);
            }
            if (!photo2.equals("")) {
                jsonObject.put("Photo2File", photo2);
            }
            if (!photo3.equals("")) {
                jsonObject.put("Photo3File", photo3);
            }
            if (!photo4.equals("")) {
                jsonObject.put("Photo4File", photo4);
            }

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject5 = new JSONObject();
            JSONObject jsonObject6 = new JSONObject();
            JSONObject jsonObject7 = new JSONObject();
            JSONObject jsonObject8 = new JSONObject();
            if (!photo5.equals("")) {
                jsonObject5.put("INDEX", "5");
                jsonObject5.put("Photo", "");
                jsonObject5.put("PhotoFile", photo5);
                jsonArray.put(jsonObject5);
            }
            if (!photo6.equals("")) {
                jsonObject6.put("INDEX", "6");
                jsonObject6.put("Photo", "");
                jsonObject6.put("PhotoFile", photo6);
                jsonArray.put(jsonObject6);
            }
            if (!photo7.equals("")) {
                jsonObject7.put("INDEX", "7");
                jsonObject7.put("Photo", "");
                jsonObject7.put("PhotoFile", photo7);
                jsonArray.put(jsonObject7);
            }
            if (!photo8.equals("")) {
                jsonObject8.put("INDEX", "8");
                jsonObject8.put("Photo", "");
                jsonObject8.put("PhotoFile", photo8);
                jsonArray.put(jsonObject7);
            }
            int arrayLength = jsonArray.length();
            if (arrayLength != 0) {
                jsonObject.put("PhotoListFile", jsonArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHANGEPLATENUMBER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            dialogShow(0, "车牌补办成功");
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(CarReissueActivity.this, LoginActivity.class);
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
                    //清空图片缓存
                    ActivityUtil.goActivityAndFinish(CarReissueActivity.this, HomeActivity.class);
                }
            }).show();
        }
    }

    private boolean checkData() {
        if (!checkPlateNum.isChecked() && !checkLabelA.isChecked() && !checkLabelB.isChecked()) {
            Utils.myToast(mContext, "请选择变更类型");
            return false;
        } else {
            if (checkPlateNum.isChecked()) {
                String plateNum = editPlateNumber.getText().toString().toUpperCase().trim();
                if (plateNum.equals("")) {
                    Utils.myToast(mContext, "请输入车牌号");
                    return false;
                }
            }
            if (checkLabelA.isChecked()) {
                String labelA = editLabelA.getText().toString().trim();
                if (labelA.equals("")) {
                    Utils.myToast(mContext, "请输入车辆标签编号");
                    return false;
                }
            }
            if (checkLabelB.isChecked()) {
                String labelB = editLabelB.getText().toString().trim();
                if (labelB.equals("")) {
                    Utils.myToast(mContext, "请输入电池标签编号");
                    return false;
                }
            }
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

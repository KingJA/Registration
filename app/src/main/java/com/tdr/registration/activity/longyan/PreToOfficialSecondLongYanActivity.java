package com.tdr.registration.activity.longyan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预登记转备案
 */
public class PreToOfficialSecondLongYanActivity extends BaseActivity {

    private static final String TAG = "PreToOfficialSecondLongYanActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_ownerName)
    TextView textOwnerName;
    @BindView(R.id.text_ownerIdentity)
    TextView textOwnerIdentity;
    @BindView(R.id.text_ownerPhone1)
    TextView textOwnerPhone1;
    @BindView(R.id.edit_ownerPhone2)
    EditText editOwnerPhone2;
    @BindView(R.id.edit_ownerCurrentAddress)
    EditText editOwnerCurrentAddress;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.btn_next)
    Button btnNext;

    private ZProgressHUD mProgressHUD;
    private Context mContext;

    private ParsingQR mQR;
    private Activity mActivity;
    private List<PhotoListInfo> PLI=new ArrayList<PhotoListInfo>();
    private String PhotoConfig;
    private List<UploadInsuranceModel> models;
    private String Version="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_official_second_longyan);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        Version= (String)SharedPreferencesUtils.get("Version","");
        mQR = new ParsingQR();
        initView();
        initData();
    }

    private void initView() {
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        textTitle.setText("预登记转备案");
        btnNext.setText("提交");
        Bundle bundle = (Bundle) getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("insurance");
        models = (List<UploadInsuranceModel>) list.get(0);
        PhotoConfig= bundle.getString("PhotoConfig");
        try {
            JSONArray JA = new JSONArray(PhotoConfig);
            JSONObject JB;
            PhotoListInfo pli;
            for (int i = 0; i < JA.length(); i++) {
                JB = new JSONObject(JA.get(i).toString());
                pli = new PhotoListInfo();
                pli.setINDEX(JB.getString("INDEX"));
                pli.setREMARK(JB.getString("REMARK"));
                pli.setValid(JB.getBoolean("IsValid"));
                pli.setRequire(JB.getBoolean("IsRequire"));
                if (pli.isValid()) {
                    PLI.add(pli);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        textOwnerName.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
        textOwnerIdentity.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
        textOwnerPhone1.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
        editOwnerPhone2.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE2));
        editOwnerCurrentAddress.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS));
        editRemarks.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REMARK));
    }

    @OnClick({R.id.image_back, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_next:
                String ownerCurrentAddress = editOwnerCurrentAddress.getText().toString().trim();
                if (ownerCurrentAddress.equals("")) {
                    Utils.myToast(mContext, "请输入现住址");
                    break;
                }
                sendMsg();
                break;
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.myToast(mContext, "没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                     labelNumber = bundle.getString("result");
                    Log.e(TAG, "onActivityResult: " + labelNumber);
                    sendMsg();
                }
            }
        }
    }*/

    private void sendMsg() {
        JSONArray JA = new JSONArray();
        JSONObject JB;
        String index;
        String PhotoFile;
        try {
            for (int i = 0; i < PLI.size(); i++) {
                index = PLI.get(i).getINDEX();
                PhotoFile = (String) SharedPreferencesUtils.get("Photo:" + index, "");
                JB = new JSONObject();
                JB.put("INDEX", index);
                JB.put("Photo", "");
                JB.put("PhotoFile", PhotoFile);
                JA.put(JB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        try {
            json.put("EcId", UUID.randomUUID().toString().toUpperCase());
            json.put("HasRFID", "1");// 是否有有源标签,1有，0无
            json.put("VehicleModels", "");
            json.put("Photo1File", "");
            json.put("Photo2File", "");
            json.put("Photo3File", "");
            json.put("Photo4File", "");

            json.put("REGISTERID",VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REGISTERID));
            json.put("CARTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE));
            json.put("ISCONFIRM", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ISCONFIRM));
            json.put("VehicleBrand", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
            json.put("PlateNumber", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
            json.put("ShelvesNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
            json.put("EngineNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
            json.put("ColorId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID));
            json.put("ColorId2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2ID));
            json.put("BuyDate", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));
            json.put("Price", "");

            json.put("CARDTYPE", "1");
            json.put("OwnerName", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
            json.put("CardId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
            json.put("Phone1", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
            json.put("Phone2", editOwnerPhone2.getText().toString().trim());
            json.put("ResidentAddress", editOwnerCurrentAddress.getText().toString().trim());
            json.put("Remark", editRemarks.getText().toString().trim());
            json.put("THEFTNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
            json.put("VEHICLETYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
            JSONArray JA1 = new JSONArray();
            JSONObject JB1 = null;
            for (UploadInsuranceModel uploadInsuranceModel : models) {
                JB1 = new JSONObject();
                if(Version.equals("1")){
                    JB1.put("CID", uploadInsuranceModel.getPOLICYID());
                    JB1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                }else{
                    JB1.put("POLICYID", "");
                    JB1.put("Type", uploadInsuranceModel.getType());
                    JB1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                    JB1.put("DeadLine", uploadInsuranceModel.getDeadLine());
                    JB1.put("BUYDATE", uploadInsuranceModel.getBUYDATE());
                }
                JA1.put(JB1);
            }
            json.put("POLICYS", JA1);
            json.put("PhotoListFile", JA);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", json.toString());
        mProgressHUD.show();
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_ADDELECTRICCAR, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER) + "  电动车信息上传成功！");
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(PreToOfficialSecondLongYanActivity.this, LoginActivity.class);
                        } else {
                            Utils.myToast(mContext, data);
                            mProgressHUD.dismiss();
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
                    Utils.ClearData();
                    VehiclesStorageUtils.clearData();
                    ActivityUtil.goActivityAndFinish(PreToOfficialSecondLongYanActivity.this, HomeActivity.class);
                }
            }).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

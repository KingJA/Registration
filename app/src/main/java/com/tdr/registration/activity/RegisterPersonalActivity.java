package com.tdr.registration.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.normal.RegisterCarActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.DX_PreRegistrationModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.mLog;

import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PreModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.RegistrPop;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 昆明等地区备案登记第二页 人员信息
 */
public class RegisterPersonalActivity extends BaseActivity implements View.OnClickListener, AdapterView
        .OnItemClickListener, RegistrPop.OnRegistrPopClickListener {
    private static final String TAG = "RegisterPersonalActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.linear_identity)
    LinearLayout linearIdentity;
    @BindView(R.id.relative_identity)
    ImageView relativeIdentity;
    @BindView(R.id.text_identity)
    TextView textIdentity;
    @BindView(R.id.text_currentAddressStart)
    TextView textCurrentAddressStart;
    @BindView(R.id.edit_ownerName)
    EditText editOwnerName;
    @BindView(R.id.edit_ownerIdentity)
    EditText editOwnerIdentity;
    @BindView(R.id.edit_ownerPhone1)
    EditText editOwnerPhone1;
    @BindView(R.id.edit_ownerPhone2)
    EditText editOwnerPhone2;
    @BindView(R.id.edit_ownerCurrentAddress)
    EditText editOwnerCurrentAddress;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.text_cardType)
    TextView textCardType;
    @BindView(R.id.relative_commission)
    RelativeLayout relativeCommission;
    @BindView(R.id.check_commission)
    CheckBox checkCommission;
    @BindView(R.id.text_commissionCardType)
    TextView textCommissionCardType;
    @BindView(R.id.edit_commissionName)
    EditText editCommissionName;
    @BindView(R.id.edit_commissionIdentity)
    EditText editCommissionIdentity;
    @BindView(R.id.edit_commissionPhone1)
    EditText editCommissionPhone1;
    @BindView(R.id.linear_commission)
    LinearLayout linearCommission;
    @BindView(R.id.LL_Agent)
    LinearLayout LL_Agent;

    @BindView(R.id.edit_AGENTADDR)
    EditText edit_AGENTADDR;
    @BindView(R.id.edit_agentremark)
    EditText edit_agentremark;


    @BindView(R.id.text_showOwnerPhone1)
    TextView textShowOwnerPhone1;

    private Activity mActivity;
    private Context mContext;
    private Intent intent;

    private String isCommission = "1";//1，没有代办人；0，没有代办人
    private final static int SCANNIN_GREQUEST_CODE = 03160;//二维码回调值
    private final static int PRE_SHOW_CODE = 1314;//预登记展示回调值
    private final static int SCANNIN_QR_CODE = 0514;//二维码回调值

    private DbManager db;

    private ColorAdapter cardTypeAdapter;
    private List<SortModel> cardTypeList = new ArrayList<SortModel>();
    private List<BikeCode> cardList = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap = new HashMap<Integer, Integer>();
    private int cardType = 0;
    private String mCardType = "";
    private String mCardTypeId = "";
    private ColorAdapter cardTypeAdapter2;
    private List<SortModel> cardTypeList2 = new ArrayList<SortModel>();
    private List<BikeCode> cardList2 = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap2 = new HashMap<Integer, Integer>();
    private int cardType2 = 0;
    private String mCardType2 = "";
    private String mCardTypeId2 = "";

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private String city = "";//当前帐号所在市
    private String activity = "";
    private String listId = "";
    private String photoStr = "";
    private String locCityName = "";
    private Gson mGson;
    private ZProgressHUD mProgressHUD;
    private RegistrPop registrPop;
    private ElectricCarModel model;
    private List<InsuranceModel> insuranceModelsList = new ArrayList<>();//保险集合
    //app内自主预登记的model
    private List<PreRegistrationModel> preRegistrationModels = new ArrayList<>();
    private PreRegistrationModel preRegistrationModel = new PreRegistrationModel();

    private List<PreRegistrationModel> preForKMModels = new ArrayList<>();
    private PreRegistrationModel preForKMModel = new PreRegistrationModel();
    //卡包自主预登记
    private PreModel preModel = new PreModel();
    private List<PreModel> preModels = new ArrayList<>();

    private String labelNumber = "";//有源标签编号
    private List<DX_PreRegistrationModel> PRList;
    private PreRegistrationModel RM;
    private String HasAgent = "";
    private DX_PreRegistrationModel PRM;
    private String InType = "";
    private String REGISTRATION;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);
        ButterKnife.bind(this);
        mActivity = this;
        mContext = this;
        intent = new Intent();
        mGson = new Gson();
        db = x.getDb(DBUtils.getDb());
        city = (String) SharedPreferencesUtils.get("city", "");
        REGISTRATION = (String) SharedPreferencesUtils.get("REGISTRATION", "");
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            InType = bundle.getString("InType");
            if (InType.equals("PreRegistration")) {
                PRM = (DX_PreRegistrationModel) TransferUtil.retrieve("PreRegistrationModel");
                TransferUtil.retrieve("PreRegistrationModel");
            } else if (InType.equals("Registration")) {
                RM = (PreRegistrationModel) bundle.getSerializable("RegistrationModel");
                if (RM == null) {
                    model = (ElectricCarModel) bundle.getSerializable("model");
                    activity = bundle.getString("activity");
                    listId = Utils.initNullStr(bundle.getString("distrainCarListID"));
                }
            }
        }

        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        HasAgent = (String) SharedPreferencesUtils.get("HasAgent", "");
        mLog.e("HasAgent=" + HasAgent);

        initView();
        initData();
        if (RM != null) {
            dealPreByPlateNumber(RM);
        }
        if (InType.equals("PreRegistration")) {
            dealModel(PRM);
        }

    }


    private void scanQR() {
        String isScanLabel = (String) SharedPreferencesUtils.get("isScanLabel", "");
        mLog.e("2isScanLabel=" + isScanLabel);
        if (isScanLabel.equals("1")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShow", true);
            bundle.putString("activity", "register");
            ActivityUtil.goActivityForResultWithBundle(RegisterPersonalActivity.this, QRCodeScanActivity.class, bundle,
                    SCANNIN_QR_CODE);
        }
    }

    private void initView() {

//        if (!locCityName.contains("南宁") && !locCityName.contains("郑州") && !locCityName.contains("许昌")) {
//        imageScan.setVisibility(View.VISIBLE);
        imageScan.setBackgroundResource(R.mipmap.register_pop);
//        }

        registrPop = new RegistrPop(imageScan, mActivity);
        registrPop.setOnRegistrPopClickListener(this);

        editOwnerIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
        if (activity.equals("")) {
            if (!REGISTRATION.equals("")) {
                textTitle.setText(REGISTRATION);
            } else {
                if (city.contains("温州")) {
                    textTitle.setText("登记备案");
                } else {
                    if (locCityName.contains("昆明") || locCityName.contains("天津")) {
                        textTitle.setText("防盗登记");
                    } else {
                        textTitle.setText("备案登记");
                    }
                }
            }
        } else {
            textTitle.setText("扣押转正式");
        }
        textCardType.setText("身份证");
//        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, "1");

        checkCommission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCommission = "0";
                    linearCommission.setVisibility(View.VISIBLE);
                } else {
                    isCommission = "1";
                    linearCommission.setVisibility(View.GONE);
                }
            }
        });
        if (locCityName.contains("昆明")) {
            textIdentity.setText("持车证和证件");
            textCurrentAddressStart.setVisibility(View.GONE);

        }
        linearIdentity.setVisibility(View.GONE);
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        if (HasAgent.equals("1")) {
            LL_Agent.setVisibility(View.VISIBLE);
        } else {
            LL_Agent.setVisibility(View.GONE);
        }
        linearCommission.setVisibility(View.GONE);

    }

    public void savePersonalInfo() {
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, editOwnerName.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, mCardTypeId);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, editOwnerIdentity.getText().toString()
                .trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, editOwnerPhone1.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, editOwnerPhone2.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, editOwnerCurrentAddress.getText()
                .toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, editRemarks.getText().toString().trim());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initData() {

        mCardTypeId = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE,"1");
        try {
            cardList = db.selector(BikeCode.class).where("type", "=", "6").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (cardList == null) {
            cardList = new ArrayList<BikeCode>();
        }

        Log.e("Pan", "cardType:" + mCardTypeId);
        for (BikeCode bikeCode : cardList) {
            if (mCardTypeId.equals(bikeCode.getCode())) {
                textCardType.setText(bikeCode.getName());
            }
        }


        cardType = Integer.parseInt(mCardTypeId) - 1;
        cardTypeMap.clear();
        cardTypeMap.put(cardType, 100);
        editOwnerName.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
        editOwnerIdentity.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
        editOwnerPhone1.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
        editOwnerPhone2.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE2));
        editOwnerCurrentAddress.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS));
        editRemarks.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REMARK));
        photoStr = (String) SharedPreferencesUtils.get("identity", "");
        if (!photoStr.equals("")) {
            Bitmap bitmap = Utils.stringtoBitmap(photoStr);
            Drawable bd = new BitmapDrawable(bitmap);
            relativeIdentity.setBackground(bd);
            textIdentity.setTextColor(getResources().getColor(R.color.white));
        }

        isCommission = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ISCOMMISSION);
        if (isCommission.equals("0")) {
            linearCommission.setVisibility(View.VISIBLE);
            mCardTypeId2 = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONCARDTYPE);

            for (BikeCode bikeCode : cardList) {
                if (mCardTypeId2.equals(bikeCode.getCode())) {
                    textCommissionCardType.setText(bikeCode.getName());
                }
            }

            cardType2 = Integer.parseInt(mCardTypeId2) - 1;
            cardTypeMap2.clear();
            cardTypeMap2.put(cardType2, 100);
            editCommissionName.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONNAME));
            editCommissionIdentity.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils
                    .COMMISSIONIDENTITY));
            editCommissionPhone1.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONPHONE1));
            edit_AGENTADDR.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONADDRESS));
            edit_agentremark.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONREMARK));
        }

        //判断这个城市是否有保险
        try {
            insuranceModelsList = db.findAll(InsuranceModel.class);

        } catch (DbException e) {
            e.printStackTrace();
        }
        if (insuranceModelsList == null) {
            insuranceModelsList = new ArrayList<InsuranceModel>();
        }
    }

    @OnClick({R.id.image_back, R.id.image_scan, R.id.btn_next, R.id.text_cardType, R.id.text_commissionCardType, R.id
            .relative_identity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.image_scan:
                registrPop.showPopupWindowDownOffset();
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                savePersonalInfo();
                mLog.e("VEHICLETYPE=" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
                Bundle bundle = new Bundle();
                bundle.putString("activity", activity);
                bundle.putString("distrainCarListID", listId);
                bundle.putString("IsPRE", "0");

                ActivityUtil.goActivityWithBundle(RegisterPersonalActivity.this, RegisterInsuranceActivity.class,
                        bundle);
                //TODO
//                if (insuranceModelsList.size() > 0) {
//                    //该城市有保险
//                    ActivityUtil.goActivityWithBundle(RegisterPersonalActivity.this, RegisterInsuranceActivity.class,
// bundle);
//                } else {
//                    ActivityUtil.goActivity(RegisterPersonalActivity.this, RegisterCarActivity.class);
//                }
                break;

            case R.id.text_cardType:
                dialogShow(0, "");
                break;

            case R.id.text_commissionCardType:
                dialogShow(1, "");
                break;

            case R.id.relative_identity:
                PhotoUtils.TakePicture(mActivity, "identity");
                break;
            default:
                break;
        }
    }

    private void SkipInsurance() {
        String deadLine = "";//保险年限
        List<UploadInsuranceModel> uploadInsuranceModels = new ArrayList<>();
        mLog.e("保险列表数=" + insuranceModelsList.size());
        for (int i = 0; i < insuranceModelsList.size(); i++) {
            UploadInsuranceModel uploadInsuranceModel = new UploadInsuranceModel();
            String remarkId = insuranceModelsList.get(i).getRemarkID();
            mLog.e("remarkId=" + remarkId);
//            List<DetailBean> DB = db.findAllByWhere(DetailBean.class, " RemarkID=\"" + remarkId + "\"");
            List<DetailBean> DB = null;
            try {
                DB = db.selector(DetailBean.class).where("RemarkID", "=", remarkId).findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < DB.size(); j++) {
                mLog.e("保险Name=" + DB.get(j).getName());
                mLog.e("保险Id=" + DB.get(j).getId());
                mLog.e("保险CreateTime=" + DB.get(j).getCreateTime());
                mLog.e("保险DeadLine=" + DB.get(j).getDeadLine());
                mLog.e("保险IsValid=" + DB.get(j).getIsValid());
                mLog.e("保险Price=" + DB.get(j).getPrice());
            }
            if (DB == null) {
                DB = new ArrayList<DetailBean>();
            }

            mLog.e("保险选项数=" + DB.size());
            for (int j = 0; j < DB.size(); j++) {
                deadLine = DB.get(j).getDeadLine();
            }
            mLog.e("保险选项" + deadLine);
            uploadInsuranceModel.setPOLICYID("");
            uploadInsuranceModel.setType(insuranceModelsList.get(i).getTypeId());
            uploadInsuranceModel.setREMARKID(insuranceModelsList.get(i).getRemarkID());
            uploadInsuranceModel.setDeadLine(deadLine);
            uploadInsuranceModel.setBUYDATE(Utils.getNowDate());
            uploadInsuranceModels.add(uploadInsuranceModel);
        }
        Bundle bundle = new Bundle();
        bundle.putString("activity", "");
        bundle.putString("distrainCarListID", listId);
        ArrayList list = new ArrayList();
        list.add(uploadInsuranceModels);
        bundle.putParcelableArrayList("insurance", list);
//        ActivityUtil.goActivityWithBundle(RegisterPersonalActivity.this, RegisterFirstNormalActivity.class, bundle);
        ActivityUtil.goActivityWithBundle(RegisterPersonalActivity.this, RegisterCarActivity.class, bundle);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.CAMERA_REQESTCODE:
                if (resultCode == RESULT_OK) {
                    int degree = PhotoUtils.readPictureDegree();
                    Bitmap bitmap = PhotoUtils.rotaingImageView(degree,
                            PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile));
                    photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
                    String path = PhotoUtils.imageFile.getPath();
                    String name = Utils.getFileName(path);
                    Drawable db = new BitmapDrawable(bitmap);
                    switch (name) {
                        case "identity":
                            relativeIdentity.setBackground(db);
                            SharedPreferencesUtils.put("identity", photoStr);
                            textIdentity.setTextColor(getResources().getColor(R.color.white));
                            break;
                    }
                }
                break;
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Utils.myToast(mContext, "没有扫描到二维码");
                        return;
                    } else {
                        mProgressHUD.show();
                        Bundle bundle = data.getExtras();
                        String scanResult = bundle.getString("result");
                        mLog.e("预登记SCANNIN_GREQUEST_CODE: " + scanResult);
                        if (scanResult.startsWith("TDR_APP")) {
                            query(scanResult);
                        } else {
                            getPre(scanResult);
                        }
                    }
                }
                break;
            case PRE_SHOW_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Utils.myToast(mContext, "没有选择预登记车辆");
                        return;
                    } else {
                        Bundle bundle = data.getExtras();
                        PreRegistrationModel preRegistrationModel = (PreRegistrationModel) bundle.getSerializable
                                ("preModels");
                        dealPreByPolice(preRegistrationModel);
                    }
                }
                break;
            case SCANNIN_QR_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Utils.myToast(mContext, "没有扫描到二维码");
                        return;
                    } else {
                        Bundle bundle = data.getExtras();
                        labelNumber = bundle.getString("result");
                        mLog.e("预登记SCANNIN_QR_CODE: " + labelNumber);
                        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO, labelNumber);
                    }
                }
                break;
        }
    }

    /**
     * 获取电信预登记
     */
    private void query(String registerId) {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("plateNumber", "");
        map.put("cardid", "");
        map.put("phone", "");
        map.put("registerId", registerId.substring(7));
        mLog.e("Pan", "map=" + map);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_GETPREREGISTERLIST, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Utils.LOGE("Pan", result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            try {
                                PRList = mGson.fromJson(data, new TypeToken<List<DX_PreRegistrationModel>>() {
                                }.getType());
                                if (PRList.get(0) != null) {
                                    if (VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE).equals
                                            (PRList.get(0).getVEHICLETYPE())) {
                                        dealModel(PRList.get(0));
                                    } else {
                                        dialogShow(7, "扫码获得的车辆类型与您选择的车辆类型不符，请退出登记重新选择车辆类型或者扫描相对应的车辆类型的二维码。");
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                Utils.showToast(data);
                            }

                        } else {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });

    }

    /**
     * 获取预登记
     *
     * @param content
     */
    private void getPre(String content) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PrerateID", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final HashMap<String, String> map = new HashMap<>();
        map.put("token", (String) SharedPreferencesUtils.get("token", ""));
        map.put("taskId", "");
        map.put("encryption", "");
        map.put("DataTypeCode", "GetPreRateOne");
        map.put("content", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_CARDHOLDER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e(result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int resultCode = json.getInt("ResultCode");
                        String resultText = json.getString("ResultText");
                        if (resultCode == 0) {
                            mProgressHUD.dismiss();
                            String content = json.getString("Content");
                            preModel = mGson.fromJson(content, new TypeToken<PreModel>() {
                            }.getType());
                            String state = preModel.getState();
                            if (state.equals("0")) {
                                dealModel(preModel);
                            } else {
                                Utils.myToast(mContext, "该预登记车辆已被登记");
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, resultText);
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

    @Override
    public void onBackPressed() {
//        dialogShow(4, "");
        savePersonalInfo();
        finish();

    }

    private boolean checkData() {
        if (mCardTypeId.equals("")) {
            Utils.myToast(mContext, "请选择证件类型");
            return false;
        }
//            if (SharedPreferencesUtils.get("identity", "").equals("")) {
//                Utils.myToast(mContext, "请拍摄证件照片");
//                return false;
//            }

        String ownerName = editOwnerName.getText().toString().trim();
        if (ownerName.equals("")) {
            Utils.myToast(mContext, "请输入车主姓名");
            return false;
        }
        String ownerIdentity = editOwnerIdentity.getText().toString().toUpperCase().trim();
        if (ownerIdentity.equals("")) {
            Utils.myToast(mContext, "请输入车主证件号码");
            return false;
        }
        if (mCardTypeId.equals("1")) {//如果车主证件类型为身份证（1）
            if (!Utils.isIDCard18(ownerIdentity)) {
                Utils.myToast(mContext, "输入的身份证号码格式有误");
                return false;
            }
        }

        String phone1 = editOwnerPhone1.getText().toString().trim();
        if (phone1.equals("")) {
            Utils.myToast(mContext, "请输入车主联系手机");
            return false;
        } else {
            if (!phone1.substring(0, 1).equals("1")) {
                Utils.myToast(mContext, "请输入正确的手机号码");
                return false;
            }
            if (phone1.length() != 11) {
                Utils.myToast(mContext, "输入的手机号码长度不符");
                return false;
            }
        }
//        else {
//            if(!Utils.CheckPhoneNumber(phone1)){
//                Utils.myToast(mContext, "请输入正确的车主联系手机号码");
//                return false;
//            }
//
//        }
        if (!locCityName.contains("昆明")) {
            String address = editOwnerCurrentAddress.getText().toString().trim();
            if (address.equals("")) {
                Utils.myToast(mContext, "请输入车主现住址");
                return false;
            }
        }

        if (isCommission.equals("0")) {
            String commissionName = editCommissionName.getText().toString().trim();
            if (commissionName.equals("")) {
                Utils.myToast(mContext, "请输入代办人姓名");
                return false;
            }
            String commissionCardType = textCommissionCardType.getText().toString();
            if (commissionCardType.equals("")) {
                Utils.myToast(mContext, "请选择代办人证件类型");
                return false;
            }
            String commissionIdentity = editCommissionIdentity.getText().toString().toUpperCase().trim();
            if (commissionIdentity.equals("")) {
                Utils.myToast(mContext, "请输入代办人证件号码");
                return false;
            }
            if (this.mCardTypeId2.equals("1")) {
                if (!Utils.isIDCard18(commissionIdentity)) {
                    Utils.myToast(mContext, "输入的代办人身份证号码格式有误");
                    return false;
                }
            }
            String commissionPhone1 = editCommissionPhone1.getText().toString().trim();
            if (commissionPhone1.equals("")) {
                Utils.myToast(mContext, "请输入代办人联系方式");
                return false;
            }
            String AGENTADDR = edit_AGENTADDR.getText().toString().trim();
            if (AGENTADDR.equals("")) {
                Utils.myToast(mContext, "请输入代办人现住址");
                return false;
            }

        }

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, ownerName);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, editOwnerIdentity.getText().toString()
                .toUpperCase().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, phone1);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, editOwnerPhone2.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, editOwnerCurrentAddress.getText()
                .toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, editRemarks.getText().toString().trim());

        //=======代办人=======
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCOMMISSION, isCommission);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONCARDTYPE, mCardTypeId2);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONNAME, editCommissionName.getText()
                .toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONIDENTITY, editCommissionIdentity.getText
                ().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONPHONE1, editCommissionPhone1.getText()
                .toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONADDRESS, edit_AGENTADDR.getText()
                .toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONREMARK, edit_agentremark.getText()
                .toString().trim());

        return true;
    }


    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors1.setOnItemClickListener(this);
            cardTypeList.clear();
            cardTypeAdapter = new ColorAdapter(mContext, cardTypeList,
                    cardTypeMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(cardTypeAdapter);

            for (int i = 0; i < cardList.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(cardList.get(i).getCode());
                sortModel.setName(cardList.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(cardList.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                cardTypeList.add(sortModel);
            }

            cardTypeAdapter.notifyDataSetChanged();

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mContext);
            dialogBuilder.withTitle("证件类型").withTitleColor("#333333")
                    .withButton1Text("取消").withButton2Text("选择")
                    .withMessage(null).withEffect(effectstype)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();

                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardTypeAdapter.notifyDataSetChanged();
                    mCardType = cardTypeList.get(cardType).getName();
                    mCardTypeId = cardTypeList.get(cardType).getGuid();

                    dialogBuilder.dismiss();
                    textCardType.setText(mCardType);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, mCardTypeId);
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color2, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors2);
            list_colors1.setOnItemClickListener(this);
            cardTypeList2.clear();
            cardTypeAdapter2 = new ColorAdapter(mContext, cardTypeList2,
                    cardTypeMap2, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(cardTypeAdapter2);
//            cardList2 = db.findAllByWhere(BikeCode.class, " type='6'");
            try {
                cardList2 = db.selector(BikeCode.class).where("type", "=", "6").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (cardList2 == null) {
                cardList2 = new ArrayList<BikeCode>();
            }
            for (int i = 0; i < cardList2.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(cardList2.get(i).getCode());
                sortModel.setName(cardList2.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(cardList2.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                cardTypeList2.add(sortModel);
            }

            cardTypeAdapter2.notifyDataSetChanged();

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mContext);
            dialogBuilder.withTitle("证件类型").withTitleColor("#333333")
                    .withButton1Text("取消").withButton2Text("选择")
                    .withMessage(null).withEffect(effectstype)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();

                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    textCommissionCardType.setText(mCardType2);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONCARDTYPE, mCardTypeId2);
                }
            }).show();
        } else if (flag == 3) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View identityView = mInflater.inflate(R.layout.layout_query_identity, null);
            final EditText editQueryIdentity = (EditText) identityView
                    .findViewById(R.id.edit_queryIdentity);

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(identityView, mContext);
            dialogBuilder.withTitle(msg).withTitleColor("#333333")
                    .withButton1Text("取消").withButton2Text("选择")
                    .withMessage(null).withEffect(effectstype)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();

                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    String queryIdentity = editQueryIdentity.getText().toString();
                    queryPreByIdentity(queryIdentity);
                }
            }).show();
        } else if (flag == 4) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("信息编辑中，确认离开该页面？")
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, mContext).withButton2Text("确认").setButton1Click(new View
                    .OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    SharedPreferencesUtils.put("preregisters", "");
                    SharedPreferencesUtils.put("preregistration", "");
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    TransferUtil.remove("PhotoList");
                    if (InType.equals("PreRegistration")) {
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(mContext, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }).show();
        } else if (flag == 5) {
            showdialog(msg, "防盗号");
        } else if (flag == 6) {
            showdialog(msg, "车牌号");
        } else if (flag == 7) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("确定")
                    .setCustomView(R.layout.custom_view, mContext).setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).show();
        }
    }

    private void showdialog(final String msg, String textname) {
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(this);
        View identityView = mInflater.inflate(R.layout.layout_query_identity, null);
        final EditText editQueryIdentity = (EditText) identityView
                .findViewById(R.id.edit_queryIdentity);
        final TextView textName = (TextView) identityView.findViewById(R.id.text_name);
        textName.setText(textname);
        dialogBuilder.isCancelable(false);
        dialogBuilder.setCustomView(identityView, mContext);
        dialogBuilder.withTitle(msg).withTitleColor("#333333")
                .withButton1Text("取消").withButton2Text("查询")
                .withMessage(null).withEffect(effectstype)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();

                    }
                }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                String plateNumber = editQueryIdentity.getText().toString();

                queryPreByPlateNumber(plateNumber);
            }
        }).show();
    }

    private void queryPreByPlateNumber(String plateNumber) {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("plateNumber", plateNumber);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_GETPREREGISTERS, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e("车牌号预登记" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, "查询成功，请继续操作");
                            preForKMModel = mGson.fromJson(data, new TypeToken<PreRegistrationModel>() {
                            }.getType());
                            for (PhotoModel photoModel : preForKMModel.getPhotoListFile()) {
                                photoModel.setPhotoFile("");
                            }
                            dealPreByPlateNumber(preForKMModel);
                            SharedPreferencesUtils.put("preregisters", data);
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


    private void dealPreByPlateNumber(PreRegistrationModel preForKMModel) {
        String vehicleType = Utils.initNullStr(preForKMModel.getVEHICLETYPE());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REGISTERID, preForKMModel.getREGISTERID());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, preForKMModel.getPLATENUMBER());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, preForKMModel.getVEHICLEBRANDNAME
                ());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, preForKMModel.getVEHICLEBRAND());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, preForKMModel.getColorName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, preForKMModel.getCOLORID());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, preForKMModel.getCOLORID2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2NAME, preForKMModel.getCOLORNAME2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, preForKMModel.getSHELVESNO());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, preForKMModel.getENGINENO());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, Utils.dateWithoutTime(preForKMModel
                .getBUYDATE()));
        if (Utils.initNullStr(preForKMModel.getCARDTYPE()).equals("")) {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, "1");
        } else {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, preForKMModel.getCARDTYPE());
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, preForKMModel.getOWNERNAME());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, preForKMModel.getCARDID());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, preForKMModel.getPHONE1());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, preForKMModel.getPHONE2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, preForKMModel.getADDRESS());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, preForKMModel.getREMARK());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, vehicleType);

        mLog.e("照片列表：" + preForKMModel.getPhotoListFile().toString());

//        mPhotoListFileBean = (List<PreForKMModel.PhotoListFileBean>) preForKMModel.getPhotoListFile();
//        for (int i = 0; i < mPhotoListFileBean.size(); i++) {
//            switch (Integer.valueOf(mPhotoListFileBean.get(i).getINDEX())) {//6(labelB),5(labelA),1(applicationForm
// ),7(guleCar),2(identity),4(plateNum),3(invoice),8(guleBattery)
//                case 5:
//                    SharedPreferencesUtils.put("applicationForm", mPhotoListFileBean.get(i).getPhotoFile());
//                    break;
//                case 7:
//                    SharedPreferencesUtils.put("identity", mPhotoListFileBean.get(i).getPhotoFile());
//                    break;
//                case 6:
//                    SharedPreferencesUtils.put("plateNum", mPhotoListFileBean.get(i).getPhotoFile());
//                    break;
//            }
//        }
        initData();
    }

    /**
     * 预登记查询通过身份证
     *
     * @param queryIdentity
     */
    private void queryPreByIdentity(String queryIdentity) {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("cardid", queryIdentity);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_GETPREREGISTERSBYCARDID, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e("证件号查询：" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            preRegistrationModels = mGson.fromJson(data, new TypeToken<List<PreRegistrationModel>>() {
                            }.getType());
                            if (preRegistrationModels.size() > 1) {
                                Bundle bundle = new Bundle();
                                ArrayList list = new ArrayList();
                                list.add(preRegistrationModels);
                                bundle.putParcelableArrayList("electricCarModelList", list);

                                ActivityUtil.goActivityForResultWithBundle(RegisterPersonalActivity.this,
                                        PreListActivity.class, bundle, PRE_SHOW_CODE);
                            } else if (preRegistrationModels.size() == 1) {
                                dealPreByPolice(preRegistrationModels.get(0));
                            }
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

    private void dealPreByPolice(PreRegistrationModel preRegistrationModel) {
        String VEHICLETYPE = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE);
        String vehicleType = Utils.initNullStr(preRegistrationModel.getVEHICLETYPE());
        if (!vehicleType.equals(VEHICLETYPE)) {
            Utils.myToast(mContext, "预登记车辆类型与所选类型不符，请重新选择车辆类型登记");
            return;
        } else {
            SharedPreferencesUtils.put("preregistration", mGson.toJson(preRegistrationModel.getPhotoListFile()));
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REGISTERID, preRegistrationModel.getREGISTERID());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, preRegistrationModel
                    .getPLATENUMBER());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, preRegistrationModel
                    .getVEHICLEBRANDNAME());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, preRegistrationModel
                    .getVEHICLEBRAND());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, preRegistrationModel.getColorName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, preRegistrationModel.getCOLORID());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, preRegistrationModel.getCOLORID2());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2NAME, preRegistrationModel.getCOLORNAME2());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, preRegistrationModel.getSHELVESNO());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, preRegistrationModel.getENGINENO());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, Utils.dateWithoutTime
                    (preRegistrationModel.getBUYDATE()));
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, preRegistrationModel.getCARDTYPE());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, preRegistrationModel.getOWNERNAME());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, preRegistrationModel.getCARDID());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, preRegistrationModel.getPHONE1());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, preRegistrationModel.getPHONE2());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, preRegistrationModel.getADDRESS
                    ());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, preRegistrationModel.getREMARK());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, vehicleType);
            initData();
        }
    }

    private void dealModel(DX_PreRegistrationModel preModel) {
        TransferUtil.save("PhotoList", preModel.getPhotoListFile());

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, preModel.getPLATENUMBER());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, preModel.getVEHICLETYPE());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REGISTERID, preModel.getREGISTERID());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, preModel.getVEHICLEBRAND());
//        List<BikeCode> bikeCodes = db.findAllByWhere(BikeCode.class, "code=\'" + preModel.getVehiclebrand() + "\'"
// + " and " + "type=\'1\'");
        List<BikeCode> bikeCodes = null;
        try {
            bikeCodes = db.selector(BikeCode.class).where("code", "=", preModel.getVEHICLEBRAND()).and("type", "=",
                    "1").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodes == null) {
            bikeCodes = new ArrayList<BikeCode>();
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, bikeCodes.get(0).getName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, preModel.getCOLORID());
//        List<BikeCode> bikeCodeList = db.findAllByWhere(BikeCode.class, "code=\'" + preModel.getColorID() + "\'" +
// " and " + "type=\'4\'");
        List<BikeCode> bikeCodeList = null;
        try {
            bikeCodeList = db.selector(BikeCode.class).where("code", "=", preModel.getCOLORID()).and("type", "=",
                    "4").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodeList == null) {
            bikeCodeList = new ArrayList<BikeCode>();
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, bikeCodeList.get(0).getName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, preModel.getSHELVESNO());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, preModel.getENGINENO());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, preModel.getCARDTYPE());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, preModel.getOWNERNAME());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, preModel.getCARDID());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, preModel.getPHONE1());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, preModel.getPHONE2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, preModel.getADDRESS());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, preModel.getBUYDATE());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, preModel.getREMARK());
        initData();
    }

    private void dealModel(PreModel preModel) {
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REGISTERID, preModel.getPrerateID());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, preModel.getVehiclebrand());
//        List<BikeCode> bikeCodes = db.findAllByWhere(BikeCode.class, "code=\'" + preModel.getVehiclebrand() + "\'"
// + " and " + "type=\'1\'");
        List<BikeCode> bikeCodes = null;
        try {
            bikeCodes = db.selector(BikeCode.class).where("code", "=", preModel.getVehiclebrand()).and("type", "=",
                    "1").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodes == null) {
            bikeCodes = new ArrayList<BikeCode>();
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, bikeCodes.get(0).getName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, preModel.getColorID());
//        List<BikeCode> bikeCodeList = db.findAllByWhere(BikeCode.class, "code=\'" + preModel.getColorID() + "\'" +
// " and " + "type=\'4\'");
        List<BikeCode> bikeCodeList = null;
        try {
            bikeCodeList = db.selector(BikeCode.class).where("code", "=", preModel.getColorID()).and("type", "=",
                    "4").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodeList == null) {
            bikeCodeList = new ArrayList<BikeCode>();
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, bikeCodeList.get(0).getName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, preModel.getShelvesno());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, preModel.getEngineno());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, preModel.getCardType());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, preModel.getOwnerName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, preModel.getCardid());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, preModel.getPhone1());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, preModel.getPhone2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, preModel.getRemark());
        initData();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                cardType = position;
                cardTypeMap.clear();
                cardTypeMap.put(cardType, 100);
                cardTypeAdapter.notifyDataSetChanged();
                mCardType = cardTypeList.get(cardType).getName();
                mCardTypeId = cardTypeList.get(cardType).getGuid();
                break;
            case R.id.list_colors2:
                cardType2 = position;
                cardTypeMap2.clear();
                cardTypeMap2.put(cardType2, 100);
                cardTypeAdapter2.notifyDataSetChanged();
                mCardType2 = cardTypeList2.get(cardType2).getName();
                mCardTypeId2 = cardTypeList2.get(cardType2).getGuid();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegistrPop(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putInt("ScanType", 0);
                bundle.putBoolean("isShow", true);
                bundle.putBoolean("isPlateNumber", false);
                bundle.putString("ButtonName", "输入自主预登记编号");
                ActivityUtil.goActivityForResultWithBundle(this, QRCodeScanActivity.class, bundle,
                        SCANNIN_GREQUEST_CODE);

//                intent.setClass(this, QRCodeScanActivity.class);
//                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case 1:
                dialogShow(3, "预登记查询");
                break;
            case 2:
                dialogShow(5, "登记号查询");
                break;
            case 3:
                dialogShow(6, "车牌号预登记查询");
                break;

        }
        registrPop.dismiss();
    }
}

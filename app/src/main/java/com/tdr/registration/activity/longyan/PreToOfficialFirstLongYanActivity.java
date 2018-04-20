package com.tdr.registration.activity.longyan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.RegisterThirdActivity;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.imagecompress.LGImgCompressor;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 备案登记第一页
 */

public class PreToOfficialFirstLongYanActivity extends BaseActivity implements AdapterView.OnItemClickListener, LGImgCompressor.CompressListener {

    //    private final static int CAMERA_REQESTCODE = 0316;//照相机回调值
    private static final String TAG = "PreToOfficialFirstLongYanActivity";

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;

    @BindView(R.id.rv_PhotoList)
    RecyclerView RV_PhotoList;




    @BindView(R.id.rb_newVehicle)
    RadioButton rbNewVehicle;
    @BindView(R.id.rb_oldVehicle)
    RadioButton rbOldVehicle;
    @BindView(R.id.group_vehicletype)
    RadioGroup groupVehicletype;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.group_promise)
    RadioGroup groupPromise;
    @BindView(R.id.linear_vehicleBrand)
    LinearLayout linearVehicleBrand;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.relative_vehicleBrand)
    RelativeLayout relativeVehicleBrand;
    @BindView(R.id.text_plateNumber)
    TextView textPlateNumber;
    @BindView(R.id.text_frame)
    TextView textFrame;
    @BindView(R.id.text_showMotor)
    TextView textShowMotor;
    @BindView(R.id.text_motor)
    TextView textMotor;
    @BindView(R.id.linear_vehicleColor)
    LinearLayout linearVehicleColor;
    @BindView(R.id.text_vehicleColor)
    TextView textVehicleColor;
    @BindView(R.id.relative_vehicleColor)
    RelativeLayout relativeVehicleColor;
    @BindView(R.id.linear_vehicleColor2)
    LinearLayout linearVehicleColor2;
    @BindView(R.id.text_vehicleColor2)
    TextView textVehicleColor2;
    @BindView(R.id.relative_vehicleColor2)
    RelativeLayout relativeVehicleColor2;
    @BindView(R.id.text_buyTime)
    TextView textBuyTime;
    @BindView(R.id.btn_next)
    Button btnNext;

    private Activity mActivity;
    private Context mContext;
    private DbManager db;

    private PreRegistrationModel model;

    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap = new HashMap<Integer, Integer>();
    private List<BikeCode> colorList = new ArrayList<BikeCode>();
    private String mColor2 = "";// 颜色2
    private String mColorId2 = "";// 颜色2的ID

    private CharacterParser characterParser;


    private String cardType = "";
    private String isConfirm = "";

    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码回调值*

    private String labelNumber = "";//有源标签编号

    private ZProgressHUD mProgressHUD;

    private String isAvailable = "";
    private List<InsuranceModel> InsuranceModels = null;
    private String city="";
    private ArrayList<PhotoListInfo> PLI;
    private String PhotoConfig;
    private LinearLayoutManager linearLayoutManager;
    private PhotoListAdapter PLA;
    private List<PhotoListAdapter.DrawableList> DrawableList=new ArrayList<PhotoListAdapter.DrawableList>();
    private String PlatenumberRegular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_official_first_longyan);
        ButterKnife.bind(this);
        mActivity = this;
        mContext = this;
        db = x.getDb(DBUtils.getDb());
        characterParser = CharacterParser.getInstance();
//        clearData();
//        scanQR();
       PlatenumberRegular= (String) SharedPreferencesUtils.get("PlatenumberRegular"+VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE),"");
        mLog.e(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE)+"PlatenumberRegular="+PlatenumberRegular);
        GetReady();
        SetPhotoList();
        initView();
        initData();
    }
    /**
     * 准备基础数据
     */
    private void GetReady() {
        db = x.getDb(DBUtils.getDb());
        city = (String) SharedPreferencesUtils.get("locCityName", "");
           List<BaseInfo> ResultList = null;
        try {
            ResultList = db.selector(BaseInfo.class).where("cityName","=",city).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(ResultList==null){
            ResultList=new ArrayList<BaseInfo>();
        }

        BaseInfo BI = ResultList.get(0);
        mLog.e("AppConfig:" + BI.getAppConfig());
        PLI = new ArrayList<PhotoListInfo>();
        PhotoConfig = BI.getPhotoConfig();
        mLog.e("PhotoConfig" + PhotoConfig);
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
                    mLog.e("REMARK="+pli.getREMARK()+"   INDEX="+pli.getINDEX());
                    PLI.add(pli);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * 加载图片列表
     */
    private void SetPhotoList() {
        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RV_PhotoList.setLayoutManager(linearLayoutManager);

        RV_PhotoList.addItemDecoration(new RecyclerViewItemDecoration());

        PLA = new PhotoListAdapter(mActivity, PLI);
        RV_PhotoList.smoothScrollToPosition(PLI.size());
        RV_PhotoList.smoothScrollToPosition(0);
        mLog.e("----------smoothScrollToPosition---------");
        PLA.setOnItemClickLitener(new PhotoListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String PhotoName = "Photo:" + PLI.get(position).getINDEX() + ":" + position;
                if (PhotoUtils.CurrentapiVersion > 20) {
                    PhotoUtils.TakePicture(mActivity, PhotoName);
                } else {
                    PhotoUtils.TakePicture2(mActivity, PhotoName);
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(mActivity, position + " long click",Toast.LENGTH_SHORT).show();

            }
        });
        RV_PhotoList.setAdapter(PLA);
    }

    private void repairList() {
        List<PhotoModel> pm = model.getPhotoListFile();
        List<PhotoModel> photolist = new ArrayList<PhotoModel>();
        for (int i = 0; i < PLI.size(); i++) {
            for (int j = 0; j < pm.size(); j++) {
                if (PLI.get(i).getINDEX().equals(pm.get(j).getINDEX())) {
                    photolist.add(pm.get(j));
                }
            }
        }
        for (PhotoModel PM : photolist) {
            mLog.e("Pan","getINDEX="+PM.getINDEX());
            mLog.e("Pan","getPhotoFile="+PM.getPhotoFile());
            Bitmap bitmap = Utils.stringtoBitmap(PM.getPhotoFile());
            Drawable drawable = new BitmapDrawable(bitmap);
            DrawableList.add(new PhotoListAdapter.DrawableList(PM.getINDEX(), drawable));
            PLA.UpDate(DrawableList);
            PLA.SevePhoto(PM.getINDEX(),PM.getPhotoFile());
        }

    }

    private void scanQR() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShow", true);
        bundle.putString("activity", "tianjin");
        bundle.putString("ButtonName", "请输入二维码");
        ActivityUtil.goActivityForResultWithBundle(PreToOfficialFirstLongYanActivity.this, QRCodeScanActivity.class, bundle, SCANNIN_GREQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                LGImgCompressor.getInstance(this).withListener(this).
                        starCompress(Uri.fromFile(PhotoUtils.imageFile).toString(), 480, 600, 100);
            }
        } else if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.myToast(mContext, "没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    labelNumber = bundle.getString("result");
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO, labelNumber);

                }
            }
        }
    }



    private void initView() {
        textTitle.setText("预登记转备案");
        groupVehicletype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbNewVehicle.getId()) {//新销售 0 ,已上路 1
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "0");
                    cardType = "0";
                } else {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "1");
                    cardType = "1";
                }
            }
        });
        groupPromise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbYes.getId()) {//默认“有来历承诺书”，有1 ,无 0
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, "1");
                    isConfirm = "1";
                } else {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, "0");
                    isConfirm = "0";
                }
            }
        });

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");

        textPlateNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                                Pattern pattern = Pattern.compile(PlatenumberRegular);
                Matcher matcher = pattern.matcher(s + "");
                if (matcher.matches()) {
                    String isChecked = (String) SharedPreferencesUtils.get("isChecked", "");
                    String whiteListUrl = (String) SharedPreferencesUtils.get("whiteListUrl", "");
                    if (isChecked.equals("1")) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("CityID", SharedPreferencesUtils.get("cityCode", ""));
                            jsonObject.put("TAGID", labelNumber);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("Token", (String) SharedPreferencesUtils.get("token", ""));
                        map.put("taskId", "");
                        map.put("Encryption", "");
                        map.put("DataTypeCode", "CheckWhiteList");
                        map.put("Content", jsonObject.toString());
                        WebServiceUtils.callWebService(mActivity, whiteListUrl, Constants.WEBSERVER_CARDHOLDER, map, new WebServiceUtils.WebServiceCallBack() {
                            @Override
                            public void callBack(String result) {
                                if (result != null) {

                                }
                            }
                        });

                    }
                }
            }
        });
    }

    private void initData() {

        try {
            InsuranceModels = db.findAll(InsuranceModel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (InsuranceModels == null) {
            InsuranceModels = new ArrayList<InsuranceModel>();
        }

        Bundle bundle = getIntent().getExtras();
        String locCardTypes = bundle.getString("cardTypes");
        String plateNumber = bundle.getString("plateNumber");

        if (locCardTypes.equals("98")) {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REGISTERID, plateNumber);
            textPlateNumber.setText(plateNumber);
        } else {
            model = (PreRegistrationModel) bundle.getSerializable("model");
            getphoto();
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REGISTERID, model.getREGISTERID());
            textPlateNumber.setText(model.getPLATENUMBER());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, model.getPLATENUMBER());
            textVehicleBrand.setText(model.getVEHICLEBRANDName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, model.getVEHICLEBRANDName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, model.getVEHICLEBRAND());
            textVehicleColor.setText(model.getColorName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, model.getColorName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, model.getCOLORID());
            textVehicleColor2.setText(model.getCOLORNAME2());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2NAME, Utils.initNullStr(model.getCOLORNAME2()));
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, Utils.initNullStr(model.getCOLORID2()));
            textFrame.setText(model.getSHELVESNO());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, model.getSHELVESNO());
            textMotor.setText(model.getENGINENO());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, model.getENGINENO());
            textBuyTime.setText(Utils.dateWithoutTime(model.getBUYDATE()));
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, model.getBUYDATE());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, model.getOWNERNAME());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, model.getPHONE1());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, Utils.initNullStr(model.getPHONE2()));
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, Utils.initNullStr(model.getADDRESS()));
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, model.getCARDID());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, model.getREMARK());

            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, model.getVEHICLETYPE());
            PlatenumberRegular= (String) SharedPreferencesUtils.get("PlatenumberRegular"+VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE),"");
            mLog.e(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE)+"=PlatenumberRegular="+PlatenumberRegular);

            if (model.getVEHICLETYPE().equals("1")) {
                textShowMotor.setText("电机号");
            } else if (model.getVEHICLETYPE().equals("3")) {
                textShowMotor.setText("发动机号");
            }
        }
        repairList();
    }

    private void getphoto(){
        for (PhotoModel photoModel : model.getPhotoListFile()) {
            if(!photoModel.getPhoto().equals("")){

                photoModel.setPhotoFile((String)SharedPreferencesUtils.get(photoModel.getPhoto(),""));
                SharedPreferencesUtils.put(photoModel.getPhoto(),"");
            }
        }
    }
    @OnClick({R.id.image_back,  R.id.text_vehicleColor2, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.text_vehicleColor2:
                dialogShow(1, "");
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                if (!isAvailable.equals("")) {
                    checkTheftNo();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("activity", TAG);
                    bundle.putString("PhotoConfig", PhotoConfig);
                    bundle.putString("IsPRE", "1");
                    if(InsuranceModels==null||InsuranceModels.size()==0){
                        ActivityUtil.goActivityWithBundle(PreToOfficialFirstLongYanActivity.this, PreToOfficialSecondLongYanActivity.class, bundle);
                    }else{
                        ActivityUtil.goActivityWithBundle(PreToOfficialFirstLongYanActivity.this, RegisterThirdActivity.class, bundle);
                    }
                }

                break;
        }
    }

    private void checkTheftNo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VehicleType", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
            jsonObject.put("THEFTNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKTHEFTNO, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("activity", TAG);
                            bundle.putString("PhotoConfig", PhotoConfig);
                            bundle.putString("IsPRE", "1");
//                            ActivityUtil.goActivityWithBundle(PreToOfficialFirstLongYanActivity.this, PreToOfficialSecondLongYanActivity.class, bundle);
                            if(InsuranceModels==null||InsuranceModels.size()==0){
                                ActivityUtil.goActivityWithBundle(PreToOfficialFirstLongYanActivity.this, PreToOfficialSecondLongYanActivity.class, bundle);
                            }else{
                                ActivityUtil.goActivityWithBundle(PreToOfficialFirstLongYanActivity.this, RegisterThirdActivity.class, bundle);
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        for (int i = 0; i < PLI.size(); i++) {
            if (!PLA.checkItemDate(i)) {
                Utils.myToast(mContext, "请拍摄" + PLI.get(i).getREMARK());
                return false;
            }
        }
        if (cardType.equals("")) {
            Utils.myToast(mContext, "请选择车辆类型");
            return false;
        }
        if (isConfirm.equals("")) {
            Utils.myToast(mContext, "请选择是否有来历承诺书");
            return false;
        }
        String plateNum = textPlateNumber.getText().toString().toUpperCase().trim();
        if (plateNum.equals("") || plateNum == null) {
            Utils.myToast(mContext, "请输入车牌");
            return false;
        }
        Pattern pattern = Pattern.compile(PlatenumberRegular);
        Matcher matcher = pattern.matcher(plateNum + "");
        if (!matcher.matches()) {
            Utils.myToast(mContext, "输入的车牌有误，请重新确认");
            return false;
        }
        String frame = textFrame.getText().toString().trim();
        if (frame.equals("") || frame == null) {
            Utils.myToast(mContext, "请输入车架号");
            return false;
        }
        String motor = textMotor.getText().toString().trim();
        if (motor.equals("") || motor == null) {
            Utils.myToast(mContext, "请输入电机号");
            return false;
        }
        String buyTime = textBuyTime.getText().toString();
        if (buyTime.equals("") || buyTime == null) {
            Utils.myToast(mContext, "请选择车辆购买时间");
            return false;
        }
        if(!Utils.CheckBuyTime(buyTime)){
            Utils.myToast(mContext, "您选择的时间已超过当前时间");
            return false;
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, plateNum);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, frame);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, motor);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, buyTime);
        return true;
    }


    @Override
    public void onBackPressed() {
        dialogShow(0, "");
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
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
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color2, null);
            ListView list_colors2 = (ListView) color_view
                    .findViewById(R.id.list_colors2);
            list_colors2.setOnItemClickListener(this);
            colorsList.clear();
            colorsAdapter = new ColorAdapter(mContext, colorsList,
                    colorsMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors2.setAdapter(colorsAdapter);
//            colorList = db.findAllByWhere(BikeCode.class, " type='4'");
            try {
                colorList = db.selector(BikeCode.class).where("type", "=", "4").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(colorList==null){
                colorList=new ArrayList<BikeCode>();
            }
            Log.e("颜色数量：", "" + colorList.size());
            for (int i = 0; i < colorList.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(colorList.get(i).getCode());
                sortModel.setName(colorList.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(colorList.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                colorsList.add(sortModel);
            }

            colorsAdapter.notifyDataSetChanged();

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mContext);
            dialogBuilder.withTitle("颜色列表").withTitleColor("#333333")
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
                    textVehicleColor2.setText(mColor2);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, mColorId2);
                    System.out.println("----------------选中颜色:"
                            + mColorId2);
                }
            }).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors2:
                colorsMap.clear();
                colorsMap.put(position, 100);
                colorsAdapter.notifyDataSetChanged();
                mColor2 = colorsList.get(position).getName();
                mColorId2 = colorsList.get(position).getGuid();
                break;
            default:
                break;
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
        if (PhotoUtils.CurrentapiVersion > 20) {
            UpDatePhotoItem(new File(compressResult.getOutPath()));
        }else{
            UpDatePhotoItem2(new File(compressResult.getOutPath()));
        }
    }
    private void UpDatePhotoItem(File file) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            Drawable drawable = new BitmapDrawable(bitmap);
            String path = file.getPath();
            String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            mLog.e(file.getName() + "  path=" + path);
            String PhotoName = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
            mLog.e("PhotoName=" + PhotoName);
            String Photoindex[] = PhotoName.split(":");
            mLog.e("Position=" + Photoindex[2] + "  Index=" + Photoindex[1]);
            PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList.findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
            my.Photo.setBackgroundDrawable(drawable);
            PLA.SevePhoto(Photoindex[1],photoStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpDatePhotoItem2(File file) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            Drawable drawable = new BitmapDrawable(bitmap);
            String Photoindex[] = PhotoUtils.mPicName.split(":");
            PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList.findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
            my.Photo.setBackgroundDrawable(drawable);
            my.PhotoName.setTextColor(Color.WHITE);
            PhotoUtils.sevephoto(bitmap);
            PLA.SevePhoto(Photoindex[1],photoStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

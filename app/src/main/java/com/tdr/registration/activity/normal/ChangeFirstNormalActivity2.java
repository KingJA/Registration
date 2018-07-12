package com.tdr.registration.activity.normal;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.tdr.registration.R;
import com.tdr.registration.activity.BrandActivity;
import com.tdr.registration.activity.ChangeSecondActivity;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.imagecompress.LGImgCompressor;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.SignTypeInfo;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.InterfaceChecker;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.RegularChecker;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 信息变更全国版
 */
@ContentView(R.layout.activity_register_car)
public class ChangeFirstNormalActivity2 extends BaseActivity implements View.OnClickListener, LGImgCompressor.CompressListener, AdapterView.OnItemClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码回调值
    private final static int SCANNIN_QR_CODE = 0514;//二维码回调值*
    private final static int BRAND_CODE = 2016;//品牌回调
    private Intent intent;
    private Activity mActivity;
    private Context mContext;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.image_scan)
    ImageView imageScan;
    @ViewInject(R.id.image_back)
    ImageView image_back;

    @ViewInject(R.id.rv_PhotoList)
    RecyclerView RV_PhotoList;
    /**
     * 车辆类型
     */
    @ViewInject(R.id.ll_CarType)
    LinearLayout LL_CarType;
    @ViewInject(R.id.rg_car_type)
    RadioGroup RG_car_type;
    @ViewInject(R.id.rb_new_car)
    RadioButton RB_new_car;
    @ViewInject(R.id.rb_old_car)
    RadioButton RB_old_car;
    /**
     * 车牌类型
     */
    @ViewInject(R.id.ll_plateType)
    LinearLayout LL_plateType;
    @ViewInject(R.id.rg_plateType)
    RadioGroup RG_plateType;
    @ViewInject(R.id.rb_temporaryPlate)
    RadioButton RB_temporaryPlate;
    @ViewInject(R.id.rb_formalPlate)
    RadioButton RB_formalPlate;

    /**
     * 车辆品牌
     */
    @ViewInject(R.id.rl_CarBrand)
    RelativeLayout RL_CarBrand;
    @ViewInject(R.id.tv_vehicleBrand)
    TextView TV_vehicleBrand;

    /**
     * 输入车牌号
     */
    @ViewInject(R.id.ll_plateNumber)
    LinearLayout LL_plateNumber;
    @ViewInject(R.id.et_plateNumber)
    EditText ET_plateNumber;

    /**
     * 扫描车牌号
     */
    @ViewInject(R.id.rl_plateNumber)
    RelativeLayout RL_plateNumber;
    @ViewInject(R.id.tv_Plate)
    TextView TV_Plate;
    @ViewInject(R.id.iv_scanPlate)
    ImageView IV_scanPlate;

    /**
     * 扫描标签ID
     */
    @ViewInject(R.id.rl_scanTheft)
    RelativeLayout RL_scanTheft;
    @ViewInject(R.id.tv_theftNo)
    TextView TV_theftNo;
    @ViewInject(R.id.tv_lable)
    TextView TV_lable;
    @ViewInject(R.id.iv_scanTheft)
    ImageView IV_scanTheft;


    /**
     * 扫描标签ID2
     */
    @ViewInject(R.id.rl_scanTheft2)
    RelativeLayout RL_scanTheft2;
    @ViewInject(R.id.tv_lable2)
    TextView TV_lable2;
    @ViewInject(R.id.tv_theftNo2)
    TextView TV_theftNo2;
    @ViewInject(R.id.iv_scanTheft2)
    ImageView IV_scanTheft2;

    /**
     * 防盗标签ID
     */
    @ViewInject(R.id.ll_tagId)
    LinearLayout LL_tagId;
    @ViewInject(R.id.tv_tagID)
    TextView TV_tagID;

    /**
     * 车架号
     */
    @ViewInject(R.id.ll_Frame_number)
    LinearLayout LL_Frame_number;
    @ViewInject(R.id.et_shelvesNo)
    TextView ET_frame;
    @ViewInject(R.id.IV_ScanFrameNumber)
    ImageView IV_ScanFrameNumber;


    /**
     * 电机号
     */
    @ViewInject(R.id.ll_MotorNumber)
    LinearLayout LL_MotorNumber;
    @ViewInject(R.id.et_engineNo)
    TextView ET_motor;
    @ViewInject(R.id.IV_ScanMotorNumber)
    ImageView IV_ScanMotorNumber;


    /**
     * 主颜色
     */
    @ViewInject(R.id.rl_vehicleColor)
    RelativeLayout RL_vehicleColor;
    @ViewInject(R.id.tv_vehicleColor)
    TextView TV_vehicleColor;
    /**
     * 副颜色
     */
    @ViewInject(R.id.rl_vehicleColor2)
    RelativeLayout RL_vehicleColor2;
    @ViewInject(R.id.tv_vehicleColor2)
    TextView TV_vehicleColor2;

    /**
     * 购买时间
     */
    @ViewInject(R.id.ll_buytime)
    LinearLayout LL_buytime;
    @ViewInject(R.id.tv_buyTime)
    TextView TV_buyTime;

    /**
     * 确定按钮
     */
    @ViewInject(R.id.btn_next)
    Button btn_next;

    private List<PhotoListInfo> PLI;
    private PhotoListAdapter PLA;
    List<PhotoModel> CPLIlist;

    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap = new HashMap<Integer, Integer>();
    private List<BikeCode> colorList = new ArrayList<BikeCode>();
    private String mColor;// 颜色
    private String mColorId;// 颜色的ID
    private String mColor2 = "";// 颜色2
    private String mColorId2 = "";// 颜色2的ID

    private DbManager db;
    private CharacterParser characterParser;

    private String city = "";//当前帐号所在市

    private String cardType = "";//车辆类型

    private ElectricCarModel  model = new ElectricCarModel();
    private List<PhotoModel> photoModels = new ArrayList<>();

    private String REGULAR = "";
    private String REGULAR2 = "";
    private String ISDOUBLESIGN = "";
//    private String photoList = "";
    private ParsingQR mQR;
    private TimePickerView timePickerView;
    private String plateType = "1";//车牌类型，目前天津在使用，1为正式，0是临时
    private LinearLayoutManager linearLayoutManager;
    private List<PhotoListAdapter.DrawableList> DrawableList;
    private String PhotoConfig = "";
    private String isScanLabel="";
    private boolean CheckTime=false;
    private boolean isManualInputPlate=true;
    private boolean isScanPlate = false;
    private String ServerTime="";
    private int scanTheft=0;
    private String ScanType = "";//扫描类型
//    private String CarRegular="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        GetReady();
        SetPhotoList();
        initView();

        Utils.ClearData();
        initData();
    }
    private void initView() {
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            model = (ElectricCarModel) bundle.getSerializable("model");
        }


        textTitle.setText("信息变更");
        ET_plateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        mLog.e("city=" + city);
        LL_plateType.setVisibility(View.GONE);
        RL_vehicleColor2.setVisibility(View.GONE);
        LL_tagId.setVisibility(View.GONE);

        mLog.e(isScanLabel.equals("1") ? "启用扫标签：" + isScanLabel : "禁用扫标签：" + isScanLabel);
//        mLog.e("Pan", "车辆类型：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
//        CarRegular = (String) SharedPreferencesUtils.get("PlatenumberRegular" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE), "");
//        mLog.e("Pan", "正则表达式：" + CarRegular);
//
        if (isScanLabel.equals("1")) {
            RL_scanTheft.setVisibility(View.VISIBLE);
            if (ISDOUBLESIGN.equals("1")) {
                RL_scanTheft2.setVisibility(View.VISIBLE);
            }else{
                RL_scanTheft2.setVisibility(View.GONE);
            }
        } else if (isScanLabel.equals("0")) {
            RL_scanTheft.setVisibility(View.GONE);
            RL_scanTheft2.setVisibility(View.GONE);
        }

        if (city.contains("天津")) {
            isManualInputPlate = false;
            textTitle.setText("防盗登记");
            LL_plateType.setVisibility(View.VISIBLE);
            ET_plateNumber.setHint("请输入电动自行车车牌");
        } else if (city.contains("昆明")) {
            isManualInputPlate = false;
            LL_CarType.setVisibility(View.GONE);
            TV_lable.setText("防盗号");
            TV_lable2.setText("防盗号2");
        } else if (city.contains("南宁")) {
            LL_CarType.setVisibility(View.GONE);
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗号");
            TV_theftNo.setHint("请扫码确认防盗号");
            TV_theftNo2.setHint("请扫码确认防盗号");
        } else if (city.contains("柳州") || city.contains("防城港")) {
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗装置");
            TV_lable2.setText("防盗装置2");
            TV_theftNo.setHint("请扫描防盗装置");
            TV_theftNo2.setHint("请扫描防盗装置");
        }else if (city.contains("六盘水") ) {
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗号");
            TV_lable2.setText("防盗号2");
        }else if (city.contains("丽水")) {
            isManualInputPlate = false;
        }

        VehiclesStorageUtils.setVehiclesAttr
                (VehiclesStorageUtils.VEHICLETYPE,model.getVehicleType());

        if (InterfaceChecker.isNewInterface()) {
            Log.e(TAG, "新接口: " );
            Log.e(TAG, "车辆类型: " +VehiclesStorageUtils.getVehiclesAttr
                    (VehiclesStorageUtils.VEHICLETYPE));
            //新接口方式
            List<SignTypeInfo> signTypeInfos = InterfaceChecker.getSignTypes(VehiclesStorageUtils.getVehiclesAttr
                    (VehiclesStorageUtils.VEHICLETYPE));

            Log.e(TAG, "标签数: "+ signTypeInfos.size() );
            if (signTypeInfos.size() == 1) {
                Log.e(TAG, "1个标签: " );
                RL_scanTheft.setVisibility(View.VISIBLE);
                RL_scanTheft2.setVisibility(View.GONE);
                TV_lable.setText(signTypeInfos.get(0).getName());
                REGULAR= signTypeInfos.get(0).getRegular();
            } else if (signTypeInfos.size() == 2|| signTypeInfos.size() == 3) {
                Log.e(TAG, "2个标签: " );
                RL_scanTheft.setVisibility(View.VISIBLE);
                RL_scanTheft2.setVisibility(View.VISIBLE);
                TV_lable.setText(signTypeInfos.get(0).getName());
                TV_lable2.setText(signTypeInfos.get(1).getName());
                REGULAR= signTypeInfos.get(0).getRegular();
                REGULAR2= signTypeInfos.get(1).getRegular();
            }
        }else{
            Log.e(TAG, "老接口: " );
            //老接口方式
            if (ISDOUBLESIGN.equals("1")) {
                RL_scanTheft.setVisibility(View.VISIBLE);
                RL_scanTheft2.setVisibility(View.VISIBLE);
            } else {
                RL_scanTheft.setVisibility(View.VISIBLE);
                RL_scanTheft2.setVisibility(View.GONE);
            }
        }
        Log.e(TAG, "REGULAR: "+REGULAR );


        if (isManualInputPlate) {
            RL_plateNumber.setVisibility(View.GONE);
            LL_plateNumber.setVisibility(View.VISIBLE);
        } else {
            RL_plateNumber.setVisibility(View.VISIBLE);
            LL_plateNumber.setVisibility(View.GONE);
        }
        image_back.setOnClickListener(this);
        imageScan.setOnClickListener(this);
        LL_buytime.setOnClickListener(this);
        RL_CarBrand.setOnClickListener(this);
        RL_vehicleColor.setOnClickListener(this);
        RL_vehicleColor2.setOnClickListener(this);
        IV_scanTheft.setOnClickListener(this);
        IV_scanTheft2.setOnClickListener(this);
        IV_scanPlate.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        LL_Frame_number.setOnClickListener(this);
        LL_MotorNumber.setOnClickListener(this);
        RG_car_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == RB_new_car.getId()) {//新销售 0 ,已上路 1
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "0");
                    cardType = "0";
                } else {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "1");
                    cardType = "1";
                }
            }
        });

        RG_plateType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == RB_formalPlate.getId()) {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATETYPE, "1");
                    plateType = "1";
                } else {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATETYPE, "0");
                    plateType = "0";
                }
            }
        });
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                TV_buyTime.setText(Utils.setDate(date));
                Utils.CheckBuyTime(TV_buyTime.getText().toString(), new Utils.GetServerTime() {
                    @Override
                    public void ServerTime(String ST,boolean Check) {
                        ServerTime=ST;
                        CheckTime=Check;
                    }
                });
            }
        });
    }

    /**
     * 准备基础数据
     */
    private void GetReady() {
        db = x.getDb(DBUtils.getDb());
        intent = new Intent();
        mQR = new ParsingQR();
        characterParser = CharacterParser.getInstance();
        mActivity = this;
        mContext = this;
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        isScanLabel = (String) SharedPreferencesUtils.get("isScanLabel", "");
//        List<BaseInfo> ResultList = db.findAllByWhere(BaseInfo.class, " cityName=\"" + city + "\"");
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
        REGULAR = (String) SharedPreferencesUtils.get("REGULAR", "");
        REGULAR2 = (String) SharedPreferencesUtils.get("REGULAR2", "");
        ISDOUBLESIGN = (String) SharedPreferencesUtils.get("ISDOUBLESIGN", "");

        if (ISDOUBLESIGN.equals("")||REGULAR.equals("")||REGULAR2.equals("")) {
            try {
                JSONArray ja = new JSONArray(BI.getAppConfig());
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jsonObject = ja.getJSONObject(i);
                    mLog.e("AppConfig" + jsonObject.toString());
                    String key = jsonObject.getString("key");
                    switch (key) {
                        case "THEFTNO1_REGULAR":
                            REGULAR = jsonObject.getString("value");
                            SharedPreferencesUtils.put("REGULAR", REGULAR);
                            break;
                        case "THEFTNO2_REGULAR":
                            REGULAR2 = jsonObject.getString("value");
                            SharedPreferencesUtils.put("REGULAR2", REGULAR2);
                            break;
                        case "IsDoubleSign":
                            ISDOUBLESIGN = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsDoubleSign", ISDOUBLESIGN);
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mLog.e("REGULAR:" + REGULAR);
        mLog.e("REGULAR2:" + REGULAR2);
        mLog.e("ISDOUBLESIGN:" + ISDOUBLESIGN);
        PLI = new ArrayList<PhotoListInfo>();
        CPLIlist = new ArrayList<PhotoModel>();
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

    private void initData() {

        DrawableList = new ArrayList<PhotoListAdapter.DrawableList>();
        repairList();
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, model.getCARTYPE());

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATETYPE, model.getPLATETYPE());

        if (model.getCARTYPE().equals("0")) {
            RB_new_car.setChecked(true);
        } else {
            RB_old_car.setChecked(true);
        }
        if (model.getPLATETYPE() != null) {
            if (model.getPLATETYPE().equals("0")) {
                RB_temporaryPlate.setChecked(true);
            } else {
                RB_formalPlate.setChecked(true);
            }
        }

        if (model.getRESERVE3() != null) {
            TV_theftNo.setText(model.getRESERVE3());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO, model.getRESERVE3());
        }
        if (model.getRESERVED4() != null) {
            TV_theftNo2.setText(model.getRESERVED4());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO2, model.getRESERVED4());
        }
        if (model.getPLATETYPE() == null) {
            plateType = "";
            RB_formalPlate.setChecked(false);
            RB_temporaryPlate.setChecked(false);
        } else {
            plateType = model.getPLATETYPE();
            if (plateType.equals("1")) {
                RB_formalPlate.setChecked(true);
            } else if (plateType.equals("0")) {
                RB_temporaryPlate.setChecked(true);
            }
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATETYPE, plateType);
        }

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, model.getISCONFIRM());
        TV_vehicleBrand.setText(model.getVehicleBrandName());


        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, model.getVehicleBrand());
        if(isManualInputPlate){
            ET_plateNumber.setText(model.getPlateNumber());
        }else{
            TV_Plate.setText(model.getPlateNumber());
        }

        ET_frame.setText(model.getShelvesNo());
        ET_motor.setText(model.getEngineNo());
        TV_vehicleColor.setText(model.getColorName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, model.getColorId());
        TV_vehicleColor2.setText(model.getColorName2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, model.getColorId2());
        TV_buyTime.setText(Utils.dateWithoutTime(model.getBuyDate()));

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, model.getCARDTYPE());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, model.getVehicleType());

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ECID, model.getEcId());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, model.getOwnerName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, model.getCardId());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, model.getResidentAddress());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, model.getRemark());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, model.getPhone1());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, model.getPhone2());
        Utils.CheckBuyTime(TV_buyTime.getText().toString(), new Utils.GetServerTime() {
            @Override
            public void ServerTime(String st,boolean Check) {
                ServerTime=st;
                CheckTime=Check;
            }
        });
    }

    private void repairList() {
        List<PhotoModel> pm = model.getPhotoListFile();
        List<PhotoModel> photolist = new ArrayList<PhotoModel>();
        for (int i = 0; i < PLI.size(); i++) {
            for (int j = 0; j < pm.size(); j++) {
//                mLog.e("Pan","i="+PLI.get(i).getINDEX()+"  j="+pm.get(j).getINDEX());
                if (PLI.get(i).getINDEX().equals(pm.get(j).getINDEX())) {
//                    mLog.e("Pan",i+"  pm="+pm.get(i).getINDEX());
                    photolist.add(pm.get(j));
                }
            }
        }
//        mLog.e("Pan","photolist.size="+photolist.size());
        for (PhotoModel PM : photolist) {
            mLog.e("Pan","getINDEX="+PM.getINDEX());
            initImages(PM.getINDEX(), PM.getPhoto());
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
//        PLA=new PhotoListAdapter(mActivity,PLI,DrawableList);
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


    /**
     * 获取图片
     */
    private void initImages(final String index, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pictureGUID", id);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPICTURE, map, new WebServiceUtils.WebServiceCallBack() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void callBack(final String result) {
                if (result != null) {
                    PLA.SevePhoto(index, result);
                    Bitmap bitmap = Utils.stringtoBitmap(result);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    DrawableList.add(new PhotoListAdapter.DrawableList(index, drawable));
                    PLA.UpDate(DrawableList);
                }
            }
        });
    }
        @Override
        public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.iv_scanTheft:
                ScanType="ScanTheft1";
                Scan(0,true,false,"请输入二维码");
                break;
            case R.id.iv_scanTheft2:
                ScanType="ScanTheft2";
                Scan(0,true,false,"请输入二维码");
                break;
            case R.id.iv_scanPlate:
                ScanType="ScanPlate";
                Scan(0,true,true,"请输入车牌号");
                break;
            case R.id.ll_Frame_number:
                ScanType="ScanFrame";
                Scan(1,true,false,"请输入车架号");
                break;
            case R.id.ll_MotorNumber:
                ScanType="ScanMotor";
                Scan(1,true,false,"请输入电机号");
                break;

            case R.id.ll_buytime:
                timePickerView.show();
                break;
            case R.id.rl_CarBrand:
                intent.setClass(ChangeFirstNormalActivity2.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rl_vehicleColor:
                dialogShow(1, "");
                break;
            case R.id.rl_vehicleColor2:
                dialogShow(2, "");
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString("PhotoConfig", PhotoConfig);
                ActivityUtil.goActivityWithBundle(ChangeFirstNormalActivity2.this, ChangeSecondActivity.class, bundle);
                break;
        }
    }
    /**
     * 扫码
     * @param ScanType      扫描类型
     * @param isshow        是否显示录入框
     * @param isPlate       是否扫描车牌
     * @param ButtonName    按钮文本
     */
    private void Scan(int ScanType,boolean isshow,boolean isPlate,String ButtonName){
        Bundle bundle = new Bundle();
        bundle.putInt("ScanType", ScanType);
        bundle.putBoolean("isShow", isshow);
        bundle.putBoolean("isPlateNumber", isPlate);
        bundle.putString("ButtonName", ButtonName);
        ActivityUtil.goActivityForResultWithBundle(ChangeFirstNormalActivity2.this, QRCodeScanActivity.class, bundle, SCANNIN_QR_CODE);
    }

    @Override
    public void onBackPressed() {
        dialogShow(0, "");
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String msg) {
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
            View color_view = mInflater.inflate(R.layout.layout_color, null);
            ListView list_colors = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors.setOnItemClickListener(this);
            colorsList.clear();
            colorsAdapter = new ColorAdapter(mContext, colorsList,
                    colorsMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors.setAdapter(colorsAdapter);
//            colorList = db.findAllByWhere(BikeCode.class, " type='4'");
            try {
                colorList=db.selector(BikeCode.class).where("type","=","4").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(colorList==null){
                colorList=new ArrayList<BikeCode>();
            }
            mLog.e("颜色数量：" + colorList.size());
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
                    TV_vehicleColor.setText(mColor);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, mColorId);
                    System.out.println("----------------选中颜色:"
                            + mColorId);
                }
            }).show();
        } else if (flag == 2) {
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
                colorList=db.selector(BikeCode.class).where("type","=","4").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(colorList==null){
                colorList=new ArrayList<BikeCode>();
            }
            mLog.e("颜色数量：" + colorList.size());
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
                    TV_vehicleColor2.setText(mColor2);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, mColorId2);
                    System.out.println("----------------选中颜色:"
                            + mColorId2);
                }
            }).show();
        }
    }

    private void GetPhoto(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        PhotoUtils.sevephoto(bitmap);
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
        LGImgCompressor.getInstance(this).withListener(this).
                starCompress(uri.toString(), 480, 600, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                mLog.e("CurrentapiVersion:" + PhotoUtils.CurrentapiVersion);
                if (PhotoUtils.CurrentapiVersion > 20) {
                    LGImgCompressor.getInstance(this).withListener(this).
                            starCompress(Uri.fromFile(PhotoUtils.imageFile).toString(), 480, 600, 100);
                } else {
                    GetPhoto(data);
                }
            }
        } else if (requestCode == SCANNIN_QR_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.myToast(mContext, "没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    String labelNumber = bundle.getString("result");
                    if (ScanType.equals("ScanPlate")) {
                        String num = "";
                        boolean isScan = bundle.getBoolean("isScan");
                        if (isScan) {
                            num =labelNumber;
                        } else {
                            num = mQR.plateNumber(labelNumber);
                        }
                        if (!num.equals("-1")) {
                            if (isManualInputPlate) {
                                ET_plateNumber.setText(num);
                            } else {
                                TV_Plate.setText(num);
                            }
                        } else {
                            Utils.myToast(mContext, "二维码不属于车牌");
                        }
                    } else if (ScanType.equals("ScanTheft1")){
                        if(checkTheft(1, labelNumber)){
                            TV_theftNo.setText(labelNumber);
                            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO, labelNumber);
                        }
                    }else if(ScanType.equals("ScanTheft2")){
                        if(checkTheft(2, labelNumber)){
                            TV_theftNo2.setText(labelNumber);
                            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO2, labelNumber);
                        }
                    }else if(ScanType.equals("ScanFrame")){
                        ET_frame.setText(labelNumber);
                    }else if(ScanType.equals("ScanMotor")){
                        ET_motor.setText(labelNumber);
                    }
                }
            }
        } else if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                TV_vehicleBrand.setText(brandName);
                String brandCode = data.getStringExtra("brandCode");
                model.setVehicleBrand(brandCode);
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
                SharedPreferencesUtils.put("brandName", brandName);//存储显示的车辆品牌

            }
        }

    }
    private boolean checkTheft(int type, String labelNumber) {
        boolean check = false;
        String msg1 = "";
        String msg2 = "";
        String regular = "";
        if (type == 1) {
            msg1 = "请输入" + TV_lable.getText() + "号";
            msg2 = "输入的" + TV_lable.getText() + "号格式错误";
            regular = REGULAR;
        } else {
            msg1 = "请输入" + TV_lable2.getText() + "号";
            msg2 = "输入的" + TV_lable2.getText() + "号格式错误";
            regular = REGULAR2;
        }
        if (labelNumber.equals("") || labelNumber == null) {
            Utils.showToast(msg1);
        } else {
            Pattern pat = Pattern.compile(regular);
            Matcher mat = pat.matcher(labelNumber + "");
            if (!mat.matches()) {
                Utils.showToast(msg2);
            } else {
                check = true;
            }
        }
        return check;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                colorsMap.clear();
                colorsMap.put(position, 100);
                colorsAdapter.notifyDataSetChanged();
                mColor = colorsList.get(position).getName();
                mColorId = colorsList.get(position).getGuid();
                break;
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
        if (city.contains("天津")) {
            if (plateType.equals("")) {
                Utils.myToast(mContext, "请选择车牌类型");
                return false;
            }
        }

//        if (isConfirm.equals("")) {
//            Utils.myToast(mContext, "请选择是否有来历承诺书");
//            return false;
//        }
        String brand = model.getVehicleBrand();
        if (brand.equals("") || brand == null) {
            Utils.myToast(mContext, "请选择车辆品牌");
            return false;
        }
        String plateNum = "";
        if(isManualInputPlate){
            plateNum=ET_plateNumber.getText().toString().toUpperCase().trim();
        }else{
            plateNum=TV_Plate.getText().toString().toUpperCase().trim();
        }
        if (plateNum.equals("") || plateNum == null) {
            Utils.myToast(mContext, "请输入车牌");
            return false;
        }
       String PlatenumberRegular= (String) SharedPreferencesUtils.get("PlatenumberRegular"+model.getVehicleType(), "");

       mLog.e("PlatenumberRegular="+PlatenumberRegular);
        Pattern pattern = Pattern.compile(PlatenumberRegular);
        Matcher matcher = pattern.matcher(plateNum + "");
        if (!matcher.matches()) {
            Utils.myToast(mContext, "输入的车牌有误，请重新确认");
            return false;
        }

//        if (isScanLabel.equals("1")) {
//            String textTheftNoNum = TV_theftNo.getText().toString().toUpperCase().trim();
//            if (textTheftNoNum.equals("") || textTheftNoNum == null) {
//                Utils.myToast(mContext, "请输入" + TV_lable.getText() + "号");
//                return false;
//            } else {
//                if (!city.contains("龙岩")) {
//                    Pattern pat = Pattern.compile(REGULAR);
//                    Matcher mat = pat.matcher(textTheftNoNum + "");
//                    if (!mat.matches()) {
//                        Utils.myToast(mContext, "输入的" + TV_lable.getText() + "号格式错误");
//                        return false;
//                    }
//                }
//            }
//
//
//            if(ISDOUBLESIGN.equals("1")){
//                String textTheftNoNum2 = TV_theftNo2.getText().toString().toUpperCase().trim();
//                if (textTheftNoNum2.equals("") || textTheftNoNum2 == null) {
//                    Utils.myToast(mContext, "请输入" + TV_lable2.getText() + "号");
//                    return false;
//                } else {
//                    if (!city.contains("龙岩")) {
//                        Pattern pat = Pattern.compile(REGULAR2);
//                        Matcher mat = pat.matcher(textTheftNoNum2 + "");
//                        if (!mat.matches()) {
//                            Utils.myToast(mContext, "输入的" + TV_lable2.getText() + "号格式错误");
//                            return false;
//                        }
//                    }
//                }
//            }
//
//        }


        //TODO
        if (isScanLabel.equals("1")) {
            String theftNo = TV_theftNo.getText().toString().trim();
            if (theftNo.equals("")) {
                ToastUtil.showToast("请输入" + TV_lable.getText().toString().trim());
                return false;
            }
            if (ISDOUBLESIGN.equals("1")) {
                String theftNo2 = TV_theftNo2.getText().toString().trim();
                if (theftNo2.equals("")) {
                    ToastUtil.showToast("请输入" + TV_lable2.getText().toString().trim());
                    return false;
                }
            }
        }

        if (InterfaceChecker.isNewInterface()) {
            Log.e(TAG, "新接口: " );
            Log.e(TAG, "车辆类型: " +VehiclesStorageUtils.getVehiclesAttr
                    (VehiclesStorageUtils.VEHICLETYPE));
            //新接口方式
            List<SignTypeInfo> signTypeInfos = InterfaceChecker.getSignTypes(VehiclesStorageUtils.getVehiclesAttr
                    (VehiclesStorageUtils.VEHICLETYPE));

            Log.e(TAG, "标签数: "+ signTypeInfos.size() );
            if (signTypeInfos.size() == 1) {
                Log.e(TAG, "1个标签验证: " );
                String theftNo = TV_theftNo.getText().toString().trim();
                if (theftNo.equals("")) {
                    ToastUtil.showToast("请输入" + TV_lable.getText().toString().trim());
                    return false;
                }
            } else if (signTypeInfos.size() == 2|| signTypeInfos.size() == 3) {
                Log.e(TAG, "2个标签验证: " );
                String theftNo = TV_theftNo.getText().toString().trim();
                if (theftNo.equals("")) {
                    ToastUtil.showToast("请输入" + TV_lable.getText().toString().trim());
                    return false;
                }
                String theftNo2 = TV_theftNo2.getText().toString().trim();
                if (theftNo2.equals("")) {
                    ToastUtil.showToast("请输入" + TV_lable2.getText().toString().trim());
                    return false;
                }
            }
        }else{
            Log.e(TAG, "老接口: " );
            //老接口方式
            if (ISDOUBLESIGN.equals("1")) {
                Log.e(TAG, "2个标签验证: " );
                String theftNo = TV_theftNo.getText().toString().trim();
                if (theftNo.equals("")) {
                    ToastUtil.showToast("请输入" + TV_lable.getText().toString().trim());
                    return false;
                }
                String theftNo2 = TV_theftNo2.getText().toString().trim();
                if (theftNo2.equals("")) {
                    ToastUtil.showToast("请输入" + TV_lable2.getText().toString().trim());
                    return false;
                }
            } else {
                Log.e(TAG, "1个标签验证: " );
                String theftNo = TV_theftNo.getText().toString().trim();
                if (theftNo.equals("")) {
                    ToastUtil.showToast("请输入" + TV_lable.getText().toString().trim());
                    return false;
                }
            }
        }

        String shelvesNo = ET_frame.getText().toString().toUpperCase().trim();
        if (!RegularChecker.checkShelvesNoRegular(shelvesNo)) {
            return false;
        }
        String engineNo = ET_motor.getText().toString().toUpperCase().trim();
        if (!RegularChecker.checkEngineNoRegular(engineNo)) {
            return false;
        }

//        String frame = ET_frame.getText().toString().trim();
//        if (frame.equals("") || frame == null) {
//            Utils.myToast(mContext, "请输入车架号");
//            return false;
//        }
//        String motor = ET_motor.getText().toString().trim();
//        if (motor.equals("") || motor == null) {
//            Utils.myToast(mContext, "请输入电机号");
//            return false;
//        }
//        if(city.contains("昆明")){
//            if(!Utils.check_FrameOrMotor(frame)&&!Utils.check_FrameOrMotor(motor)){
//                Utils.myToast(mContext, "电机号与车架号必须正确录入其中一个");
//                return false;
//            }
//        }
        String color = model.getColorId();
        if (color.equals("") || color == null) {
            Utils.myToast(mContext, "请选择电动车颜色");
            return false;
        }
        String buyTime = TV_buyTime.getText().toString();
        if (buyTime.equals("") || buyTime == null) {
            Utils.myToast(mContext, "请选择车辆购买时间");
            return false;
        }
        if(!CheckTime){
            Utils.myToast(mContext, ServerTime+"\n您选择的时间已超过当前时间");
            return false;
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, plateNum);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, shelvesNo);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, engineNo);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, buyTime);

        return true;
    }

    @Override
    public void onCompressStart() {

    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath) {
        if (imageOutPath.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR)//压缩失败
            return;
        if (PhotoUtils.CurrentapiVersion > 20) {
            UpDatePhotoItem(new File(imageOutPath.getOutPath()));
        }else{
            UpDatePhotoItem2(new File(imageOutPath.getOutPath()));
        }
    }



}

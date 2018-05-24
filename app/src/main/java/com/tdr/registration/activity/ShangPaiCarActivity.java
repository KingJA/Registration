package com.tdr.registration.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.base.App;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.DX_PreRegistrationModel;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.PreModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.DESCoder;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.RegularChecker;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.RegistrPop;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 免费上牌 车辆登记
 */
public class ShangPaiCarActivity extends BaseActivity implements AdapterView.OnItemClickListener, RegistrPop
        .OnRegistrPopClickListener {

    private final static int SCANNIN_GREQUEST_CODE_CAR = 1996;//二维码回调值
    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码回调值
    private final static int SCANNIN_QR_CODE = 0514;//二维码回调值*
    private final static int BRAND_CODE = 2016;//品牌回调
    private final static int CONFIRMATION_INSURANCE = 1212;//确认保险
    private final static int PRE_SHOW_CODE = 1314;//预登记展示回调值

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.rv_PhotoList)
    RecyclerView RV_PhotoList;
    /**
     * 车辆类型
     */
    @BindView(R.id.ll_CarType)
    LinearLayout LL_CarType;
    @BindView(R.id.rg_car_type)
    RadioGroup RG_car_type;
    @BindView(R.id.rb_new_car)
    RadioButton RB_new_car;
    @BindView(R.id.rb_old_car)
    RadioButton RB_old_car;

    /**
     * 车牌类型
     */
    @BindView(R.id.ll_plateType)
    LinearLayout LL_plateType;
    @BindView(R.id.rg_plateType)
    RadioGroup RG_plateType;
    @BindView(R.id.rb_temporaryPlate)
    RadioButton RB_temporaryPlate;
    @BindView(R.id.rb_formalPlate)
    RadioButton RB_formalPlate;

    /**
     * 车辆品牌
     */
    @BindView(R.id.rl_CarBrand)
    RelativeLayout RL_CarBrand;
    @BindView(R.id.tv_vehicleBrand)
    TextView TV_vehicleBrand;

    /**
     * 输入车牌号
     */
    @BindView(R.id.ll_plateNumber)
    LinearLayout LL_plateNumber;
    @BindView(R.id.et_plateNumber)
    EditText ET_plateNumber;

    /**
     * 扫描车牌号
     */
    @BindView(R.id.rl_plateNumber)
    RelativeLayout RL_plateNumber;
    @BindView(R.id.tv_Plate)
    TextView TV_Plate;
    @BindView(R.id.iv_scanPlate)
    ImageView IV_scanPlate;

    /**
     * 扫描标签ID
     */
    @BindView(R.id.rl_scanTheft)
    RelativeLayout RL_scanTheft;
    @BindView(R.id.tv_lable)
    TextView TV_lable;
    @BindView(R.id.tv_theftNo)
    TextView TV_theftNo;
    @BindView(R.id.iv_scanTheft)
    ImageView IV_scanTheft;

    /**
     * 扫描标签ID2
     */
    @BindView(R.id.rl_scanTheft2)
    RelativeLayout RL_scanTheft2;
    @BindView(R.id.tv_lable2)
    TextView TV_lable2;
    @BindView(R.id.tv_theftNo2)
    TextView TV_theftNo2;
    @BindView(R.id.iv_scanTheft2)
    ImageView IV_scanTheft2;

    /**
     * 防盗标签ID
     */
    @BindView(R.id.ll_tagId)
    LinearLayout LL_tagId;
    @BindView(R.id.tv_tagID)
    TextView TV_tagID;

    /**
     * 车架号
     */
    @BindView(R.id.ll_Frame_number)
    LinearLayout LL_Frame_number;
    @BindView(R.id.et_shelvesNo)
    EditText etShelvesNo;
    @BindView(R.id.IV_ScanFrameNumber)
    ImageView IV_ScanFrameNumber;

    /**
     * 电机号
     */
    @BindView(R.id.ll_MotorNumber)
    LinearLayout LL_MotorNumber;
    @BindView(R.id.et_engineNo)
    EditText etEngineNo;
    @BindView(R.id.IV_ScanMotorNumber)
    ImageView IV_ScanMotorNumber;

    /**
     * 主颜色
     */
    @BindView(R.id.rl_vehicleColor)
    RelativeLayout RL_vehicleColor;
    @BindView(R.id.tv_vehicleColor)
    TextView TV_vehicleColor;
    /**
     * 副颜色
     */
    @BindView(R.id.rl_vehicleColor2)
    RelativeLayout RL_vehicleColor2;
    @BindView(R.id.tv_vehicleColor2)
    TextView TV_vehicleColor2;

    /**
     * 购买时间
     */
    @BindView(R.id.ll_buytime)
    LinearLayout LL_buytime;
    @BindView(R.id.tv_buyTime)
    TextView TV_buyTime;

    /**
     * 确定按钮
     */
    @BindView(R.id.btn_next)
    Button btn_next;

    private Intent intent;
    private Context mContext;
    private Activity mActivity;

    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap = new HashMap<Integer, Integer>();
    private List<BikeCode> colorList = new ArrayList<BikeCode>();
    private String mColor1 = "";// 颜色1
    private String mColorId1 = "";// 颜色1的ID
    private String mColor2 = "";// 颜色2
    private String mColorId2 = "";// 颜色2的ID
    private String brandCode = "";//品牌code

    private DbManager db;
    private CharacterParser characterParser;
    private String city = "";//当前帐号所在市
    private String carType = "";//车辆类型

    private String plateType = "1";//车牌类型，目前天津在使用，1为正式，0是临时

    private String isConfirm = "0";//来历承诺书

    private ZProgressHUD mProgressHUD;

    private String activity = "";
    private String listId = "";


    private List<UploadInsuranceModel> models = new ArrayList<>();

    private ParsingQR mQR;
    private TimePickerView timePickerView;
    private String ecId = "";
    private List<PhotoListInfo> PLI;
    private PhotoListAdapter PLA;
    private String REGULAR = "";
    private String REGULAR2 = "";
    private String ISDOUBLESIGN = "";

    private String isScanLabel = "";
    private String isScanCard = "";
    private boolean CheckTime = false;
    private String ScanType = "";//扫描类型

    private boolean isManualInputPlate = true;
    private List<PhotoListAdapter.DrawableList> DrawableList;
    private String InvoiceType = "";
    private App BA;
    private String Version;
    private String CarRegular = "";
    private String IsConfirm = "";
    private ConfirmInsuranceModel ConfirmInsuranceList;
    private String REGISTRATION;
    private RegistrPop registrPop;
    private DX_PreRegistrationModel prm;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shangpai_car);
        ButterKnife.bind(this);
        Version = (String) SharedPreferencesUtils.get("Version", "");
/*获取预登记传递的信息*/
        Bundle bundle = (Bundle) getIntent().getExtras();


        if (bundle != null) {
            String InType = bundle.getString("InType");
            if (InType.equals("PreRegistration")) {
                prm = (DX_PreRegistrationModel) TransferUtil.retrieve("PreRegistrationModel");
                TransferUtil.retrieve("PreRegistrationModel");
//                prm  = (DX_PreRegistrationModel) bundle.getSerializable("PreRegistrationModel");

            }
        }
        GetReady();
        initView();
        initData();
        if (prm != null) {
            Logger.d("prm有数据");
            dealModel(prm);
            SetPhotoList();
        } else {
            Logger.d("prm没数据");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.getServerTime();
    }

    /**
     * 准备基础数据
     */
    private void GetReady() {
        db = x.getDb(DBUtils.getDb());
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        isScanLabel = (String) SharedPreferencesUtils.get("isScanLabel", "");
        isScanCard = (String) SharedPreferencesUtils.get("isScanCard", "");
        IsConfirm = (String) SharedPreferencesUtils.get("IsConfirm", "");
        REGISTRATION = (String) SharedPreferencesUtils.get("REGISTRATION", "");
        intent = new Intent();
        characterParser = CharacterParser.getInstance();
        mActivity = this;
        BA = ((App) mActivity.getApplicationContext());
        mContext = this;
        mQR = new ParsingQR();
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);


        ecId = UUID.randomUUID().toString().toUpperCase();
        Bundle bundle = (Bundle) getIntent().getExtras();
//        if (bundle != null) {
//            activity = bundle.getString("activity");
//            listId = bundle.getString("distrainCarListID");
//            InvoiceType = bundle.getString("InvoiceType");
//            ArrayList list = bundle.getParcelableArrayList("insurance");
//            models = (List<UploadInsuranceModel>) list.get(0);
//            ConfirmInsuranceList = (ConfirmInsuranceModel) list.get(1);
//            mLog.e("ConfirmInsuranceModel=" + ConfirmInsuranceList.getInsurance().size());
//        }


        for (UploadInsuranceModel uploadInsuranceModel : models) {
            mLog.e("POLICYID= " + uploadInsuranceModel.getPOLICYID());
            mLog.e("Type= " + uploadInsuranceModel.getType());
            mLog.e("REMARKID= " + uploadInsuranceModel.getREMARKID());
            mLog.e("DeadLine= " + uploadInsuranceModel.getDeadLine());
            mLog.e("BUYDATE= " + uploadInsuranceModel.getBUYDATE());
        }
        PLI = new ArrayList<PhotoListInfo>();
        getPhotoConfig();

    }


    private void getPhotoConfig() {

//        List<BaseInfo> ResultList = db.findAllByWhere(BaseInfo.class, " cityName=\"" + city + "\"");
        List<BaseInfo> ResultList = null;
        try {
            ResultList = db.selector(BaseInfo.class).where("cityName", "=", city).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (ResultList == null) {
            ResultList = new ArrayList<BaseInfo>();
        }

        BaseInfo BI = ResultList.get(0);
        //        Log.e("Pan", "PhotoConfig" + BI.getPhotoConfig());
        mLog.e("AppConfig:" + BI.getAppConfig());
        REGULAR = (String) SharedPreferencesUtils.get("REGULAR", "");
        REGULAR2 = (String) SharedPreferencesUtils.get("REGULAR2", "");
        ISDOUBLESIGN = (String) SharedPreferencesUtils.get("ISDOUBLESIGN", "");

        mLog.e("Pan", "车辆类型：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
        CarRegular = (String) SharedPreferencesUtils.get("PlatenumberRegular" + VehiclesStorageUtils.getVehiclesAttr
                (VehiclesStorageUtils.VEHICLETYPE), "");
        mLog.e("Pan", "正则表达式：" + CarRegular);

        if (ISDOUBLESIGN.equals("") || REGULAR.equals("") || REGULAR2.equals("")) {
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
        try {
            JSONArray JA = new JSONArray(BI.getPhotoConfig());
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
        } catch (Exception e) {
            mLog.e("数据异常1");
            getBaseData();
            e.printStackTrace();
        }
        SetPhotoList();

    }

    /**
     * 加载图片列表
     */
    private void SetPhotoList() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RV_PhotoList.setLayoutManager(linearLayoutManager);

        RV_PhotoList.addItemDecoration(new RecyclerViewItemDecoration());
        List<PhotoListAdapter.DrawableList> DrawableList = new ArrayList<PhotoListAdapter.DrawableList>();
        List<PhotoModel> PhotoList = (List<PhotoModel>) TransferUtil.retrieve("PhotoList");
        if (PhotoList == null) {
            PhotoList = new ArrayList<>();
        }
        Logger.d("PhotoList:" + PhotoList.size());
        Logger.d("PLI:" + PhotoList.size());
        for (int i = 0; i < PLI.size(); i++) {
            String plateNumStr = (String) SharedPreferencesUtils.get("Photo:" + PLI.get(i).getINDEX(), "");
//            Log.e("Pan",plateNumStr.equals("")?"读取图片为空":"读取图片不为空");
            if (PhotoList.size() > 0 && !PhotoList.get(i).getPhotoFile().equals("") && plateNumStr.equals("")) {
                SharedPreferencesUtils.put("Photo:" + PLI.get(i).getINDEX(), PhotoList.get(i).getPhotoFile());
                Bitmap bitmap = Utils.stringtoBitmap(PhotoList.get(i).getPhotoFile());
                DrawableList.add(new PhotoListAdapter.DrawableList(PLI.get(i).getINDEX(), new BitmapDrawable(bitmap)));
            }
            if (!plateNumStr.equals("")) {
                Bitmap bitmap = Utils.stringtoBitmap(plateNumStr);
                DrawableList.add(new PhotoListAdapter.DrawableList(PLI.get(i).getINDEX(), new BitmapDrawable(bitmap)));
            }
        }
//        Log.e("Pan", "DrawableList.size="+DrawableList.size());
        PLA = new PhotoListAdapter(mActivity, PLI, DrawableList);
        RV_PhotoList.smoothScrollToPosition(PLI.size());
        RV_PhotoList.smoothScrollToPosition(0);

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
     * 加载view
     */
    private void initView() {
        imageScan.setVisibility(View.GONE);
        imageScan.setBackgroundResource(R.mipmap.register_pop);
        registrPop = new RegistrPop(imageScan, mActivity);
        registrPop.setOnRegistrPopClickListener(this);


        textTitle.setText("免费上牌");
        SetViewForCity();

        ET_plateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        String IsScanDjh = (String) SharedPreferencesUtils.get("IsScanDjh", "");
        mLog.e("IsScanDjh=" + IsScanDjh);
        String IsScanCjh = (String) SharedPreferencesUtils.get("IsScanCjh", "");
        mLog.e("IsScanCjh=" + IsScanCjh);
        if (IsScanCjh.equals("1")) {
            IV_ScanFrameNumber.setVisibility(View.VISIBLE);
        } else {
            IV_ScanFrameNumber.setVisibility(View.GONE);
        }
        if (IsScanDjh.equals("1")) {
            IV_ScanMotorNumber.setVisibility(View.VISIBLE);
        } else {
            IV_ScanMotorNumber.setVisibility(View.GONE);
        }
        etShelvesNo.setTransformationMethod(new AllCapTransformationMethod(true));
        etEngineNo.setTransformationMethod(new AllCapTransformationMethod(true));

        RG_car_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == RB_new_car.getId()) {//新销售 0 ,已上路 1
                    carType = "0";
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "0");
                } else {
                    carType = "1";
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "1");
                }
            }
        });


        RB_formalPlate.setChecked(true);

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
        if (!VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE).equals("")) {
            TV_buyTime.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));
        }
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
                    public void ServerTime(String ST, boolean Check) {
                        CheckTime = Check;
                    }
                });
            }
        });

    }

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initData() {
        String cartype = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE);
        if (!cartype.equals("")) {
            carType = cartype;
        }
        if (carType.equals("0")) {
            RB_new_car.setChecked(true);
        } else if (carType.equals("1")) {
            RB_old_car.setChecked(true);
        } else {
            RB_old_car.setChecked(true);
        }

        plateType = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATETYPE);
        if (plateType.equals("")) {
            plateType = "1";
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATETYPE, "1");
        }
        mLog.e("plateType=" + plateType);
        if (plateType.equals("1")) {
            RB_formalPlate.setChecked(true);
        } else if (plateType.equals("0")) {
            RB_temporaryPlate.setChecked(true);
        } else {
            RB_formalPlate.setChecked(true);
        }

        TV_vehicleBrand.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME));
        TV_vehicleColor.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1NAME));
        TV_vehicleColor2.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2NAME));
        if (isManualInputPlate) {
            ET_plateNumber.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
        } else {
            TV_Plate.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
        }
        TV_theftNo.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
        TV_theftNo2.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO2));

        TV_tagID.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
        etShelvesNo.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
        etEngineNo.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
        String time = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE);
        mLog.e("time=" + time);
        if (!time.equals("")) {
            TV_buyTime.setText(time);
        }
        Utils.CheckBuyTime(TV_buyTime.getText().toString(), new Utils.GetServerTime() {
            @Override
            public void ServerTime(String st, boolean Check) {
                CheckTime = Check;
            }
        });
        repairList();
    }

    private PreRegistrationModel preForKMModel = new PreRegistrationModel();

    private void repairList() {
        DrawableList = new ArrayList<PhotoListAdapter.DrawableList>();
        List<PhotoModel> pm = new ArrayList<PhotoModel>();
        List<PhotoModel> photolist = new ArrayList<PhotoModel>();
        String preregisters = (String) SharedPreferencesUtils.get("preregisters", "");
        String preregistration = (String) SharedPreferencesUtils.get("preregistration", "");
        String photoListFile = (String) SharedPreferencesUtils.get("PhotoListFile", "");


        mLog.e("Pan", "preregisters=" + preregisters);
        mLog.e("Pan", "preregistration=" + preregistration);
        mLog.e("Pan", "photoListFile=" + photoListFile);
        if (preregisters.equals("") && preregistration.equals("") && photoListFile.equals("")) {
            return;
        }
        if (!preregisters.equals("")) {
            preForKMModel = mGson.fromJson(preregisters, new TypeToken<PreRegistrationModel>() {
            }.getType());
            mLog.e("Pan", "preForKMModel=" + preForKMModel.getColorName());

            for (PhotoModel photoModel : preForKMModel.getPhotoListFile()) {
                PhotoModel PM = new PhotoModel();
                PM.setINDEX(photoModel.getINDEX());
                PM.setPhoto(photoModel.getPhoto());
                PM.setPhotoFile(photoModel.getPhotoFile());
                PM.setRemark(photoModel.getRemark());
                pm.add(PM);
            }
        } else if (!preregistration.equals("")) {
            List<PhotoModel> list = mGson.fromJson(preregistration, new TypeToken<List<PhotoModel>>() {
            }.getType());
            mLog.e("Pan", "list.size=" + list.size());

            for (PhotoModel photoModel : list) {
                PhotoModel PM = new PhotoModel();
                PM.setINDEX(photoModel.getINDEX());
                PM.setPhoto(photoModel.getPhoto());
                PM.setPhotoFile(photoModel.getPhotoFile());
                PM.setRemark(photoModel.getRemark());
                pm.add(PM);
            }
        } else if (!photoListFile.equals("")) {
            List<PhotoModel> list = mGson.fromJson(photoListFile, new TypeToken<List<PhotoModel>>() {
            }.getType());
            Logger.d("|photoList:" + list.size());

            for (PhotoModel photoModel : list) {
                PhotoModel PM = new PhotoModel();
                PM.setINDEX(photoModel.getINDEX());
                PM.setPhoto(photoModel.getPhoto());
                PM.setPhotoFile(photoModel.getPhotoFile());
                PM.setRemark(photoModel.getRemark());
                pm.add(PM);
            }
        }


        for (int i = 0; i < PLI.size(); i++) {
            for (int j = 0; j < pm.size(); j++) {
                if (PLI.get(i).getINDEX().equals(pm.get(j).getINDEX())) {
                    mLog.e("Pan", i + "  pm=" + pm.get(i).getINDEX());
                    photolist.add(pm.get(j));
                }
            }
        }
        mLog.e("Pan", "photolist.size=" + photolist.size());
        for (PhotoModel PM : photolist) {
            mLog.e("Pan", "getINDEX=" + PM.getINDEX());
            initImages(PM.getINDEX(), PM.getPhoto());
        }

    }

    /**
     * 获取图片
     */
    private void initImages(final String index, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pictureGUID", id);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_GETPICTURE, map, new WebServiceUtils.WebServiceCallBack() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void callBack(final String result) {
                if (result != null) {
                    mLog.e("照片数据：" + result);
                    PLA.SevePhoto(index, result);
                    Bitmap bitmap = Utils.stringtoBitmap(result);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    DrawableList.add(new PhotoListAdapter.DrawableList(index, drawable));
                    PLA.UpDate(DrawableList);
                }
            }
        });
    }

    /**
     * 根据不同城市设置不同的界面
     */
    private void SetViewForCity() {
//        南宁 柳州 天津 许昌

        if (isScanCard.equals("1")) {
            btn_next.setText("扫码确认车牌");
        } else {
            btn_next.setText("确认车牌");
        }
        TV_buyTime.setHint("");
        carType = "1";

        LL_plateType.setVisibility(View.GONE);
        RL_vehicleColor2.setVisibility(View.GONE);
        LL_tagId.setVisibility(View.GONE);

        mLog.e(isScanLabel.equals("1") ? "启用扫标签：" + isScanLabel : "禁用扫标签：" + isScanLabel);

        isScanLabel = "0";
        if (isScanLabel.equals("1")) {
            RL_scanTheft.setVisibility(View.VISIBLE);
            if (ISDOUBLESIGN.equals("1")) {
                RL_scanTheft2.setVisibility(View.VISIBLE);
            } else {
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
            carType = "0";
        } else if (city.contains("南宁")) {
            LL_CarType.setVisibility(View.GONE);
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗号");
            TV_theftNo.setHint("请扫码确认防盗号");
            TV_theftNo2.setHint("请扫码确认防盗号");
            carType = "0";
        } else if (city.contains("柳州") || city.contains("防城港")) {
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗装置");
            TV_lable2.setText("防盗装置2");
            TV_theftNo.setHint("请扫描防盗装置");
            TV_theftNo2.setHint("请扫描防盗装置");
        } else if (city.contains("六盘水")) {
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗号");
            TV_lable2.setText("防盗号2");
        } else if (city.contains("丽水")) {
            isManualInputPlate = false;
        }
        if (isManualInputPlate) {
            RL_plateNumber.setVisibility(View.GONE);
            LL_plateNumber.setVisibility(View.VISIBLE);
        } else {
            RL_plateNumber.setVisibility(View.VISIBLE);
            LL_plateNumber.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_back, R.id.image_scan, R.id.tv_buyTime, R.id.rl_CarBrand, R.id.rl_vehicleColor, R.id
            .rl_vehicleColor2, R.id.iv_scanTheft, R.id.iv_scanTheft2, R.id.iv_scanPlate, R.id.btn_next, R.id
            .IV_ScanFrameNumber, R.id.IV_ScanMotorNumber})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.image_scan:
//                intent.setClass(this, QRCodeScanActivity.class);
//                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                registrPop.showPopupWindowDownOffset();
                break;
            case R.id.tv_buyTime:
                timePickerView.show();
                break;
            case R.id.rl_CarBrand:
                intent.setClass(ShangPaiCarActivity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.rl_vehicleColor:
                dialogShow(1, "");
                break;
            case R.id.rl_vehicleColor2:
                dialogShow(2, "");
                break;
            case R.id.iv_scanTheft:
                ScanType = "ScanTheft1";
                Scan(0, true, false, "请输入二维码");
                break;
            case R.id.iv_scanTheft2:
                ScanType = "ScanTheft2";
                Scan(0, true, false, "请输入二维码");
                break;
            case R.id.iv_scanPlate:
                ScanType = "ScanPlate";
                Scan(0, true, true, "请输入车牌号");
                break;
            case R.id.IV_ScanFrameNumber:
                ScanType = "ScanFrame";
                Scan(1, false, false, "请输入车架号");
                break;
            case R.id.IV_ScanMotorNumber:
                ScanType = "ScanMotor";
                Scan(1, false, false, "请输入电机号");
                break;

            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                if (isScanCard.equals("1")) {
                    ScanType = "ScanPlate";
                    CheckPlate("请输入车牌号");
                } else {
                    dialogShow(3, "确认车牌号");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 扫码
     *
     * @param ScanType   扫描类型
     * @param isshow     是否显示录入框
     * @param isPlate    是否扫描车牌
     * @param ButtonName 按钮文本
     */
    private void Scan(int ScanType, boolean isshow, boolean isPlate, String ButtonName) {
        Bundle bundle = new Bundle();
        bundle.putInt("ScanType", ScanType);
        bundle.putBoolean("isShow", isshow);
        bundle.putBoolean("isPlateNumber", isPlate);
        bundle.putString("ButtonName", ButtonName);
        ActivityUtil.goActivityForResultWithBundle(ShangPaiCarActivity.this, QRCodeScanActivity.class, bundle,
                SCANNIN_QR_CODE);
    }

    private void CheckPlate(String ButtonName) {
        Bundle bundle = new Bundle();
        bundle.putInt("ScanType", 0);
        bundle.putBoolean("isShow", true);
        bundle.putBoolean("isPlateNumber", true);
        bundle.putString("ButtonName", ButtonName);
        ActivityUtil.goActivityForResultWithBundle(ShangPaiCarActivity.this, QRCodeScanActivity.class, bundle,
                SCANNIN_GREQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        dialogShow(4, "信息编辑中，确认离开该页面？");
    }

    private void saveCarInfo() {
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, isConfirm);
        if (isManualInputPlate) {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, ET_plateNumber.getText().toString());
        } else {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, TV_Plate.getText().toString());
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, etShelvesNo.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, etEngineNo.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, TV_vehicleColor.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2NAME, TV_vehicleColor2.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, carType);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, TV_buyTime.getText().toString());
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing()) {

            return;
        }

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("確定")
                    .setCustomView(R.layout.custom_view, mContext).setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    SharedPreferencesUtils.put("preregisters", "");
                    SharedPreferencesUtils.put("preregistration", "");
                    SharedPreferencesUtils.put("PhotoListFile", "");
                    VehiclesStorageUtils.clearData();
                    ActivityUtil.goActivityAndFinish(ShangPaiCarActivity.this, HomeActivity.class);
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors1.setOnItemClickListener(this);
            colorsList.clear();
            colorsAdapter = new ColorAdapter(mContext, colorsList,
                    colorsMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(colorsAdapter);
//            colorList = db.findAllByWhere(BikeCode.class, " type='4'");
            try {
                colorList = db.selector(BikeCode.class).where("type", "=", "4").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (colorList == null) {
                colorList = new ArrayList<BikeCode>();
            }
//            Log.e("颜色数量：", "" + colorList.size());
            for (int i = 0; i < colorList.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(colorList.get(i).getCode());
                sortModel.setName(colorList.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(colorList.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母-+

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
                    TV_vehicleColor.setText(mColor1);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, mColorId1);
                    System.out.println("----------------选中颜色:"
                            + mColorId1);

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
                colorList = db.selector(BikeCode.class).where("type", "=", "4").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (colorList == null) {
                colorList = new ArrayList<BikeCode>();
            }
//            Log.e("颜色数量：", "" + colorList.size());
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
        } else if (flag == 3) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View identityView = mInflater.inflate(R.layout.layout_query_identity, null);
            final EditText editQueryIdentity = (EditText) identityView
                    .findViewById(R.id.edit_queryIdentity);
            final TextView textName = (TextView) identityView.findViewById(R.id.text_name);
            textName.setText("车牌号码");
            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(identityView, mContext);
            dialogBuilder.withTitle(msg).withTitleColor("#333333")
                    .withButton1Text("取消").withButton2Text("确认")
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
                    String plateNumber = editQueryIdentity.getText().toString().trim().toUpperCase();
                    checkPlateNumber(plateNumber);
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
                    SharedPreferencesUtils.put("PhotoListFile", "");
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    TransferUtil.remove("PhotoList");
                    finish();
                }
            }).show();
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
        } else if (flag == 10) {
            //预登记查询
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
        } else if (flag == 11) {
            //登记号查询
            showdialog(msg, "防盗号");
        } else if (flag == 12) {
            //车牌号预登记查询
            showdialog(msg, "车牌号");
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
                            Logger.d("查询成功，请继续操作");
                            mProgressHUD.dismiss();
                            preForKMModel = mGson.fromJson(data, new TypeToken<PreRegistrationModel>() {
                            }.getType());
                            for (PhotoModel photoModel : preForKMModel.getPhotoListFile()) {
                                photoModel.setPhotoFile("");
                            }
                            SharedPreferencesUtils.put("preregisters", data);
                            dealPreByPlateNumber(preForKMModel);
                        } else {
                            mProgressHUD.dismiss();
                            ToastUtil.showToast(data);
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        ToastUtil.showToast("JSON解析错误");
                    } catch (JsonSyntaxException e) {
                        mProgressHUD.dismiss();
                        ToastUtil.showToast("未查询到有效数据");
                    }
                } else {
                    mProgressHUD.dismiss();
                    ToastUtil.showToast("获取数据超时，请检查网络连接");
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
        initData();
    }

    //app内自主预登记的model
    private List<PreRegistrationModel> preRegistrationModels = new ArrayList<>();

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

                                ActivityUtil.goActivityForResultWithBundle(ShangPaiCarActivity.this,
                                        PreListActivity.class, bundle, PRE_SHOW_CODE);
                            } else if (preRegistrationModels.size() == 1) {
                                dealPreByPolice(preRegistrationModels.get(0));
                            }
                        } else {
                            mProgressHUD.dismiss();
                            ToastUtil.showToast(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        ToastUtil.showToast("JSON解析出错");
                    } catch (JsonSyntaxException e) {
                        mProgressHUD.dismiss();
                        ToastUtil.showToast("未查到有效数据");
                    }
                } else {
                    mProgressHUD.dismiss();
                    ToastUtil.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
    }

    private void dealPreByPolice(PreRegistrationModel preRegistrationModel) {
        String VEHICLETYPE = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE);
        String vehicleType = Utils.initNullStr(preRegistrationModel.getVEHICLETYPE());
        if (!vehicleType.equals(VEHICLETYPE)) {
            ToastUtil.showToast("预登记车辆类型与所选类型不符，请重新选择车辆类型登记");
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

    private void checkPlateNumber(String plateNumber) {
        String plate = "";
        if (isManualInputPlate) {
            plate = ET_plateNumber.getText().toString().trim().toUpperCase();
        } else {
            plate = TV_Plate.getText().toString().trim().toUpperCase();
        }
        mLog.e("isManualInputPlate:" + isManualInputPlate);
        mLog.e("plateNumber:" + plateNumber);
        mLog.e("plate:" + plate);
        if (plateNumber.equals(plate)) {
            //TODO
//            SendMSG();
            saveCarInfo();
            ActivityUtil.goActivity(this, ShangPaiPersonalActivity.class);//人员信息
        } else {
            ToastUtil.showToast("请确认车牌无误！");
        }
    }

    private void UpDatePhotoItem() {
        Bitmap bitmap = PhotoUtils.getPhotoBitmap();
        String Photoindex[] = PhotoUtils.mPicName.split(":");
        Drawable drawable = new BitmapDrawable(bitmap);
        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList
                .findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
        my.Photo.setBackgroundDrawable(drawable);
        my.PhotoName.setTextColor(Color.WHITE);
        PLA.SevePhoto(Photoindex[1]);


    }

    private void UpDatePhotoItem2(Intent data) {
        Bitmap bitmap = PhotoUtils.getPhotoBitmap(data);
        String Photoindex[] = PhotoUtils.mPicName.split(":");
        Drawable drawable = new BitmapDrawable(bitmap);

        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList
                .findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
        my.Photo.setBackgroundDrawable(drawable);
        my.PhotoName.setTextColor(Color.WHITE);
        PhotoUtils.sevephoto(bitmap);
        PLA.SevePhoto(Photoindex[1]);
    }


//    private void UpDatePhotoItem() {
//        mLog.e("PhotoUtils.imageFile.getAbsolutePath()=" + PhotoUtils.imageFile.getAbsolutePath());
//        int degree = PhotoUtils.readPictureDegree();
//        MobclickAgent.reportError(mActivity, "degree=" + degree);
//        Bitmap b = PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile, 480, 620);
//        Bitmap bitmap = PhotoUtils.rotaingImageView(degree, b);
//
//        String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
//        String name = Utils.getFileName(PhotoUtils.imageFile.getPath());
//        String Photoindex[] = name.split(":");
////        Log.e("Pan","Position="+Photoindex[2] + "  Index=" + Photoindex[1]);
//        Drawable drawable = new BitmapDrawable(bitmap);
//        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList
// .findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
//        my.Photo.setBackgroundDrawable(drawable);
//        my.PhotoName.setTextColor(Color.WHITE);
//        PLA.SevePhoto(Photoindex[1]);
//    }
//
//    private void UpDatePhotoItem2(Intent data) {
//        Bundle bundle = data.getExtras();
//        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//        String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
//        Drawable drawable = new BitmapDrawable(bitmap);
//        String Photoindex[] = PhotoUtils.mPicName.split(":");
//        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList
// .findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
//        my.Photo.setBackgroundDrawable(drawable);
//        my.PhotoName.setTextColor(Color.WHITE);
//        PhotoUtils.sevephoto(bitmap);
//        PLA.SevePhoto(Photoindex[1]);
//    }

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                mLog.e("系统API版本:" + PhotoUtils.CurrentapiVersion);
//                ToastUtil.showToast(mContext, "系统API版本"+PhotoUtils.CurrentapiVersion);
                try {
                    if (PhotoUtils.CurrentapiVersion > 20) {
                        UpDatePhotoItem();
                    } else {
                        UpDatePhotoItem2(data);
                    }
                } catch (Exception E) {
                    MobclickAgent.reportError(mActivity, "临时缓存图片AbsolutePath：" + PhotoUtils.imageFile.getAbsolutePath
                            ());
                    MobclickAgent.reportError(mActivity, "UpDatePhotoItem" + E.toString());
                }
            }
        } else if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                TV_vehicleBrand.setText(brandName);
                brandCode = data.getStringExtra("brandCode");
//                Log.e("Pan", "brandCode=" + brandCode);
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, brandName);
            }
        } else if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    ToastUtil.showToast("没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    String isPlateNumber = bundle.getString("isPlateNumber");
                    String plateNumberInput = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER);
                    //用户输入的车牌
                    if (isPlateNumber.equals("0")) {
                        if (scanResult.equals(plateNumberInput)) {
                            saveCarInfo();
                            ActivityUtil.goActivity(this, ShangPaiPersonalActivity.class);//人员信息
                        } else {
                            ToastUtil.showToast("输入的车牌有误，请重新确认");
                            return;
                        }
                    } else {
                        String plateNumberRead = mQR.plateNumber(scanResult);
                        if (plateNumberRead.equals(plateNumberInput)) {
                            saveCarInfo();
                            ActivityUtil.goActivity(this, ShangPaiPersonalActivity.class);//人员信息
                        } else if (plateNumberRead.equals("-1")) {
                            ToastUtil.showToast("校验不通过，请确认车牌合法正确性");

                            return;
                        } else {
                            ToastUtil.showToast("输入的车牌有误，请重新确认");
                            return;
                        }
                    }
                }
            }
        } else if (requestCode == SCANNIN_QR_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    ToastUtil.showToast("没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    String labelNumber = bundle.getString("result");
                    if (ScanType.equals("ScanPlate")) {
                        String num = "";
                        boolean isScan = bundle.getBoolean("isScan");
                        if (isScan) {
                            num = labelNumber;
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
                            ToastUtil.showToast("二维码不属于车牌");
                        }
                    } else if (ScanType.equals("ScanTheft1")) {
                        mLog.e("labelNumber" + labelNumber);
                        if (checkTheft(1, labelNumber)) {
                            TV_theftNo.setText(labelNumber);
                            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO, labelNumber);
                        }
                    } else if (ScanType.equals("ScanTheft2")) {
                        if (checkTheft(2, labelNumber)) {
                            TV_theftNo2.setText(labelNumber);
                            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO2, labelNumber);
                        }
                    } else if (ScanType.equals("ScanFrame")) {
                        etShelvesNo.setText(labelNumber);
                    } else if (ScanType.equals("ScanMotor")) {
                        etEngineNo.setText(labelNumber);
                    }
                }
            }
        } else if (requestCode == CONFIRMATION_INSURANCE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String isChecked = bundle.getString("isChecked");
                if (isChecked.equals("1")) {
                    sendMsg();
                }
            }
        } else if (requestCode == SCANNIN_GREQUEST_CODE_CAR) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    ToastUtil.showToast("没有扫描到二维码");
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
        } else if (requestCode == PRE_SHOW_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    ToastUtil.showToast("没有选择预登记车辆");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    PreRegistrationModel preRegistrationModel = (PreRegistrationModel) bundle.getSerializable
                            ("preModels");
                    dealPreByPolice(preRegistrationModel);
                }
            }
        }
    }

    private PreModel preModel = new PreModel();

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
                                ToastUtil.showToast("该预登记车辆已被登记");
                            }
                        } else {
                            mProgressHUD.dismiss();
                            ToastUtil.showToast(resultText);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        ToastUtil.showToast("JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    ToastUtil.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
    }

    List<DX_PreRegistrationModel> PRList;

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
                    } catch (JsonSyntaxException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.showToast("未查到有效数据");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });

    }

    private void dealModel(DX_PreRegistrationModel preModel) {
//        TransferUtil.save("PhotoList", preModel.getPhotoListFile());

        List<PhotoModel> photoListFile = preModel.getPhotoListFile();

        Logger.d("照片存储前:" + new Gson().toJson(photoListFile));

        SharedPreferencesUtils.put("PhotoListFile", new Gson().toJson(photoListFile));

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
            mLog.e("REGULAR=" + regular + "     labelNumber=" + labelNumber);
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

    private void SendMSG() {
        if (IsConfirm.equals("1")) {
            ConfirmInsuranceList.setPlateNumber(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
            ConfirmInsuranceList.setName(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
            ConfirmInsuranceList.setCardType(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE));
            ConfirmInsuranceList.setCardID(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
            ConfirmInsuranceList.setPhone(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
            mLog.e("PlateNumber=" + ConfirmInsuranceList.getPlateNumber());
            mLog.e("Name=" + ConfirmInsuranceList.getName());
            mLog.e("CardType=" + ConfirmInsuranceList.getCardType());
            mLog.e("CardID=" + ConfirmInsuranceList.getCardID());
            mLog.e("Phone=" + ConfirmInsuranceList.getPhone());

            Bundle bundle = new Bundle();
            ArrayList list = new ArrayList();
            list.add(ConfirmInsuranceList);
            bundle.putParcelableArrayList("ConfirmInsurance", list);
            ActivityUtil.goActivityForResultWithBundle(ShangPaiCarActivity.this, ConfirmationInsuranceActivity.class,
                    bundle, CONFIRMATION_INSURANCE);
        } else {
            sendMsg();
        }
    }

    private void sendMsg() {

        JSONObject obj = new JSONObject();
        JSONArray JA = new JSONArray();
        JSONObject JB;
        String index;
        String PhotoFile;

        String vehicletype = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE);
        if (vehicletype == null || vehicletype.equals("")) {
            String userName = (String) SharedPreferencesUtils.get("userName", "");
            String city = (String) SharedPreferencesUtils.get("locCityName", "");
            MobclickAgent.reportError(mActivity, "车辆类型数据丢失" + userName + "_" + city);
            Utils.showToast("车辆类型数据被回收,请重新登记。");

        }
        try {
            for (int i = 0; i < PLI.size(); i++) {
                index = PLI.get(i).getINDEX();
                PhotoFile = (String) SharedPreferencesUtils.get("Photo:" + index, "");
                JB = new JSONObject();
                JB.put("INDEX", index);
                JB.put("Photo", "");
                JB.put("PhotoFile", PhotoFile);
                JA.put(JB);
//                Log.e("Pan", i + "  photo:" + index + "=\n" + PhotoFile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e("Pan", "JA=" + JA.length());

        try {
            obj.put("EcId", ecId);
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ECID, ecId);
            obj.put("HasRFID", "1");// 是否有有源标签,1有，0无
            obj.put("VehicleModels", "");
//            obj.put("VEHICLETYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));

            obj.put("VEHICLETYPE", vehicletype);

            obj.put("THEFTNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
            obj.put("THEFTNO2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO2));
            obj.put("Photo1File", "");
            obj.put("Photo2File", "");
            obj.put("Photo3File", "");
            obj.put("Photo4File", "");
            obj.put("REGISTERID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REGISTERID));
            obj.put("CARTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE));
            obj.put("ISCONFIRM", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ISCONFIRM));
            obj.put("VehicleBrand", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
            obj.put("PlateNumber", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
            String platetype = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATETYPE);
            if (platetype == null || platetype.equals("")) {
                platetype = "1";
            }
            obj.put("PlateType", platetype);
            obj.put("ShelvesNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
            obj.put("EngineNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
            obj.put("ColorId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID));
            obj.put("ColorId2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2ID));
            obj.put("BuyDate", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));
            obj.put("Price", "");
            obj.put("CARDTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE));
            obj.put("OwnerName", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
            obj.put("CardId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
            obj.put("Phone1", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
            obj.put("Phone2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE2));
            obj.put("ResidentAddress", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS));
            obj.put("Remark", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REMARK));
            obj.put("INSURERTYPE", "2");// 保险公司

            obj.put("PhotoListFile", JA);
            JSONArray jsonArray1 = new JSONArray();
            JSONObject jsonObject1 = null;

            for (UploadInsuranceModel uploadInsuranceModel : models) {
//                {"CID":"保险配置字段里的CID(意义：险种ID)","REMARKID":"保险配置字段里的REMARKID(意义：险种下面的 各种规格ID)"}
                jsonObject1 = new JSONObject();
                if (Version.equals("1")) {
                    jsonObject1.put("CID", uploadInsuranceModel.getPOLICYID());
                    jsonObject1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                } else {
                    jsonObject1.put("POLICYID", "");
                    jsonObject1.put("Type", uploadInsuranceModel.getType());
                    jsonObject1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                    jsonObject1.put("DeadLine", uploadInsuranceModel.getDeadLine());
                    jsonObject1.put("BUYDATE", uploadInsuranceModel.getBUYDATE());
                }
                jsonArray1.put(jsonObject1);

            }
            obj.put("POLICYS", jsonArray1);
//            mLog.e("POLICYS:"+jsonArray1.toString());
            //代办人信息
            //TODO
            obj.put("InvoiceOp", InvoiceType);
            obj.put("AGENTNAME", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONNAME));
            obj.put("AGENTCARDTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONCARDTYPE));
            obj.put("AGENTCARDID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONIDENTITY));
            obj.put("AGENTPHONE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONPHONE1));
            obj.put("AGENTADDR", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONADDRESS));
            obj.put("AGENTREMARK", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONREMARK));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", obj.toString());
        String functionName = "";
        if (listId.equals("")) {
            functionName = Constants.WEBSERVER_ADDELECTRICCAR;
        } else {
            map.put("DistrainCarListID", listId);
            functionName = Constants.WEBSERVER_ADDELECTRICCARFROMDISTRAIN;

        }
        mLog.e("apiUrl:" + (String) SharedPreferencesUtils.get("apiUrl", ""));
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), functionName,
                map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("result:" + result);
                        Utils.LOGE("Pan", result);
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    mProgressHUD.dismiss();
                                    List<PayInsurance> payInsurances = mGson.fromJson(data, new
                                            TypeToken<List<PayInsurance>>
                                                    () {
                                            }.getType());

                                    if (payInsurances == null) {
                                        return;
                                    }
                                    if (payInsurances.size() == 1) {
                                        PayInsurance payInsurance = payInsurances.get(0);
                                        if (payInsurance.getPaymentWay() == 2) {
                                            //二维码支付
                                            PayQcodeActivity.goActivity(mActivity, payInsurance.getContent(),
                                                    payInsurance.getTotal_Amount(), payInsurance.getPlateNumber(),
                                                    payInsurance.getPayNo(), PayQcodeActivity.FORM_REGISTER);
                                        } else {
                                            //直接支付
                                            Bundle bundle = new Bundle();
                                            bundle.putString("UnPaid", "0");
                                            bundle.putString("PayDate", data);
                                            ArrayList list = new ArrayList();
                                            list.add(payInsurances);
                                            bundle.putParcelableArrayList("PayDate", list);
                                            ActivityUtil.goActivityWithBundle(mActivity, PayActivity.class, bundle);
                                        }

                                    } else if (payInsurances.size() > 1) {
                                        List<PayInsurance> PI = mGson.fromJson(data, new
                                                TypeToken<List<PayInsurance>>() {
                                                }.getType());
                                        SharedPreferencesUtils.put("preregisters", "");
                                        SharedPreferencesUtils.put("preregistration", "");
                                        SharedPreferencesUtils.put("PhotoListFile", "");
                                        Utils.showToast("车牌号：" + VehiclesStorageUtils.getVehiclesAttr
                                                (VehiclesStorageUtils
                                                        .PLATENUMBER) + "  电动车信息上传成功！");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("UnPaid", "2");
                                        ArrayList list = new ArrayList();
                                        list.add(PI);
                                        bundle.putParcelableArrayList("PayDate", list);
                                        ActivityUtil.goActivityWithBundle(mActivity, UnpaidActivity.class, bundle);
                                        mActivity.finish();

                                    } else {
                                        dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils
                                                .PLATENUMBER) + "  电动车信息上传成功！");
                                    }
                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.showToast(data);
                                    SharedPreferencesUtils.put("token", "");
                                    ActivityUtil.goActivityAndFinish(mActivity, LoginActivity.class);
                                } else {
                                    Utils.showToast(data);
                                    mProgressHUD.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mProgressHUD.dismiss();
                                Utils.showToast("JSON解析出错");
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.showToast("获取数据超时，请检查网络连接");
                        }

                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                colorsMap.clear();
                colorsMap.put(position, 100);
                colorsAdapter.notifyDataSetChanged();
                mColor1 = colorsList.get(position).getName();
                mColorId1 = colorsList.get(position).getGuid();
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


    private int checkJson(String json) {
        try {
            List<PayInsurance> list = mGson.fromJson(json, new TypeToken<List<PayInsurance>>() {
            }.getType());
            if (list.size() > 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 2;
        }
    }


    private boolean checkData() {
        for (int i = 0; i < PLI.size(); i++) {
            if (!PLA.checkItemDate(i)) {
                ToastUtil.showToast("请拍摄" + PLI.get(i).getREMARK());
                return false;
            }
        }
        if (!city.contains("南宁") || !city.contains("昆明")) {
            if (carType.equals("")) {
                ToastUtil.showToast("请选择车辆类型");
                return false;
            }
        }
//        if (isConfirm.equals("")) {
//            ToastUtil.showToast(mContext, "请选择是否有来历承诺书");
//            return false;
//        }
        if (city.contains("天津")) {
            if (plateType.equals("")) {
                ToastUtil.showToast("请选择车牌类型");
                return false;
            }
        }
        String brand = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND);
        if (brand.equals("") || brand == null) {
            ToastUtil.showToast("请选择车辆品牌");
            return false;
        }
        String plateNum = "";
        if (isManualInputPlate) {
            plateNum = ET_plateNumber.getText().toString().toUpperCase().trim();
        } else {
            plateNum = TV_Plate.getText().toString().toUpperCase().trim();
        }

        if (plateNum.equals("") || plateNum == null) {
            ToastUtil.showToast("请输入车牌");
            return false;
        }

        Pattern pattern = Pattern.compile(CarRegular);
        Matcher matcher = pattern.matcher(plateNum + "");
        if (!matcher.matches()) {
            Logger.d("车牌正则不匹配:" + plateNum + "-" + plateNum);
            ToastUtil.showToast("输入的车牌有误，请重新确认");
            return false;
        }

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
        String shelvesNo = etShelvesNo.getText().toString().toUpperCase().trim();
        if (!RegularChecker.checkShelvesNoRegular(shelvesNo)) {
            return false;
        }
        String engineNo = etEngineNo.getText().toString().toUpperCase().trim();
        if (!RegularChecker.checkEngineNoRegular(engineNo)) {
            return false;
        }
        if (city.contains("昆明")) {
            if (!Utils.check_FrameOrMotor(shelvesNo) && !Utils.check_FrameOrMotor(engineNo)) {
                ToastUtil.showToast("电机号与车架号必须正确录入其中一个");
                return false;
            }
        }
        String colorId = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID);
        if (colorId.equals("") || colorId == null) {
            ToastUtil.showToast("请选择电动车颜色");
            return false;
        }
        String buyTime = TV_buyTime.getText().toString();
        if (buyTime.equals("") || buyTime == null) {
            ToastUtil.showToast("请选择车辆购买时间");
            return false;
        }
        if (!CheckTime) {
            if (Utils.ServerTime == null || Utils.ServerTime.equals("")) {
                ToastUtil.showToast("获取服务器时间异常。");
            } else {
                ToastUtil.showToast("您选择的时间已超过当前时间");
            }
            return false;
        }

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, isConfirm);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, plateNum);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, shelvesNo);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, engineNo);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, buyTime);

        return true;
    }


    private Gson mGson;

    private void getBaseData() {
        mLog.e("BaseData");
        mProgressHUD.setMessage("更新本地数据...");
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", DESCoder.encrypt("GETCITYLIST", Constants.DES_KEY));
        map.put("infoJsonStr", "" + "_" + "ANDROID");

        WebServiceUtils.callWebService(mActivity, Constants.WEBSERVER_URL, Constants.WEBSERVER_OPENAPI, map, new
                WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            mLog.e("更新数据：" + result);
//                    Log.e("Pan","getBaseData_result= "+result);
//                    Utils.LOGE("Pan", result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                List<BaseInfo> baseInfos = new ArrayList<BaseInfo>();
                                if (errorCode == 0) {
                                    baseInfos = mGson.fromJson(data, new TypeToken<List<BaseInfo>>() {
                                    }.getType());
                                    if (baseInfos.size() > 0 && baseInfos != null) {
                                        for (int i = 0; i < baseInfos.size(); i++) {
//                                    Log.e("Pan","getPhotoConfig= "+ baseInfos.get(i).getPhotoConfig());
                                            db.deleteById(BaseInfo.class, baseInfos.get(i).getListId());
                                            db.save(baseInfos.get(i));
                                        }
                                    }
                                    getPhotoConfig();
                                    mProgressHUD.dismiss();
                                } else {
                                    mProgressHUD.dismiss();
                                    ToastUtil.showToast(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mProgressHUD.dismiss();
                                ToastUtil.showToast("JSON解析出错");
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mProgressHUD.dismiss();
                            ToastUtil.showToast("获取数据超时，请检查网络连接");
                        }
                    }
                });
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
                        SCANNIN_GREQUEST_CODE_CAR);

//                intent.setClass(this, QRCodeScanActivity.class);
//                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case 1:
                dialogShow(10, "预登记查询");
                break;
            case 2:
                dialogShow(11, "登记号查询");
                break;
            case 3:
                dialogShow(12, "车牌号预登记查询");
                break;
            default:
                break;

        }
        registrPop.dismiss();
    }
}

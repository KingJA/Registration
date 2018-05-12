package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.activity.normal.RegisterCarActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.DESCoder;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * 民警现场预登记
 */

public class PreFirstActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_photoList)
    TextView textPhotoList;
    @BindView(R.id.rb_newVehicle)
    RadioButton rbNewVehicle;
    @BindView(R.id.rb_oldVehicle)
    RadioButton rbOldVehicle;
    @BindView(R.id.group_vehicletype)
    RadioGroup groupVehicletype;
    @BindView(R.id.linear_vehicleBrand)
    LinearLayout linearVehicleBrand;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.relative_vehicleBrand)
    RelativeLayout relativeVehicleBrand;
    @BindView(R.id.linear_plateNumber)
    LinearLayout linearPlateNumber;
    @BindView(R.id.text_plateNumber)
    TextView textPlateNumber;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.image_scanPlateNumber)
    ImageView imageScanPlateNumber;
    @BindView(R.id.edit_frame)
    EditText editFrame;
    @BindView(R.id.edit_motor)
    EditText editMotor;
    @BindView(R.id.text_vehicleColor)
    TextView textVehicleColor;
    @BindView(R.id.relative_vehicleColor)
    RelativeLayout relativeVehicleColor;
    @BindView(R.id.text_vehicleColor2)
    TextView textVehicleColor2;
    @BindView(R.id.relative_vehicleColor2)
    RelativeLayout relativeVehicleColor2;
    @BindView(R.id.text_buyTime)
    TextView textBuyTime;
    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.rv_PhotoList)
    RecyclerView RV_PhotoList;
    @BindView(R.id.IV_ScanFrameNumber)
    ImageView IV_ScanFrameNumber;
    @BindView(R.id.IV_ScanMotorNumber)
    ImageView IV_ScanMotorNumber;
    private LinearLayout layoutPhotoList;

    private final static int BRAND_CODE = 2016;//品牌回调

    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap1 = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> colorsMap2 = new HashMap<Integer, Integer>();
    private List<BikeCode> colorList = new ArrayList<BikeCode>();
    private String mColor1 = "";// 颜色1
    private String mColorId1 = "";// 颜色1的ID
    private String mColor2 = "";// 颜色2
    private String mColorId2 = "";// 颜色2的ID
    private String brandCode = "";//品牌code

    private DbManager db;
    private Gson mGson;
    private CharacterParser characterParser;

    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private String city = "";//当前帐号所在市
    private String CarType = "1";//新车旧车

    private String isConfirm = "0";//来历承诺书

    private Intent intent;
    private Context mContext;
    private String REGULAR = "";
    private TimePickerView timePickerView;
    private List<PhotoListInfo> PLI;
    private PhotoListAdapter PLA;
    private Activity mActivity;
    private int num = 0;
    private String in = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first_kunming);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        db = x.getDb(DBUtils.getDb());
        mQR = new ParsingQR();
        mActivity = this;
        mGson = new Gson();
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        intent = new Intent();

        if (getIntent().getExtras() == null) {
            in = "";
        } else {
            in = getIntent().getExtras().getString("in");
        }
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mContext = this;
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        if (in.equals("TJ")) {
            textTitle.setText("免费上牌");
//            relativeVehicleColor2.setVisibility(GONE);
//            textPlateNumber.setVisibility(GONE);
        } else {
            textTitle.setText("车辆预登记");
        }
        textPhotoList.setVisibility(GONE);
        layoutPhotoList = (LinearLayout) findViewById(R.id.layout_photoList);
        layoutPhotoList.setVisibility(GONE);
        rbNewVehicle.setText("电动车");
        rbOldVehicle.setText("摩托车");

//        imageScanPlateNumber.setVisibility(GONE);
        String isScanCard = (String) SharedPreferencesUtils.get("isScanCard", "");
        Logger.d("isScanCard=" + isScanCard);
        String IsScanDjh = (String) SharedPreferencesUtils.get("IsScanDjh", "");
        Logger.d("IsScanDjh=" + IsScanDjh);
        String IsScanCjh = (String) SharedPreferencesUtils.get("IsScanCjh", "");
        Logger.d("IsScanCjh=" + IsScanCjh);
        if (isScanCard.equals("1")) {
            imageScanPlateNumber.setVisibility(View.VISIBLE);
        } else {
            imageScanPlateNumber.setVisibility(View.GONE);
        }
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

        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        editFrame.setTransformationMethod(new AllCapTransformationMethod(true));
        editMotor.setTransformationMethod(new AllCapTransformationMethod(true));

        rbNewVehicle.setChecked(true);
//        textPlateNumber.setVisibility(GONE);
        groupVehicletype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbNewVehicle.getId()) {//新销售 0 ,已上路 1
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, "0");
                    CarType = "0";
                } else {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, "1");
                    CarType = "1";
                }
            }
        });

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, "1");

        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textBuyTime.setText(Utils.setDate(date));
            }
        });
        getPhotoConfig();
    }

    private void getPhotoConfig() {
        PLI = new ArrayList<PhotoListInfo>();
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
        try {
            JSONArray JA = new JSONArray(BI.getPrephotoConfig());
            JSONObject JB;
            PhotoListInfo pli;
            mLog.e("PrephotoConfig:" + BI.getPrephotoConfig());
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
            mLog.e("数据异常3");
            if (num < 3) {
                getBaseData();
            }
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
        for (int i = 0; i < PLI.size(); i++) {
            String plateNumStr = (String) SharedPreferencesUtils.get("Photo:" + PLI.get(i).getINDEX(), "");
            com.orhanobut.logger.Logger.d("读取 Photo:" + PLI.get(i).getINDEX()+"="+plateNumStr);
//            Log.e("Pan",plateNumStr.equals("")?"读取图片为空":"读取图片不为空");
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
    private String ScanType = "";//扫描类型
    @OnClick({R.id.image_back, R.id.relative_vehicleBrand, R.id.relative_vehicleColor, R.id.text_buyTime, R.id
            .relative_vehicleColor2, R.id.btn_next, R.id.IV_ScanFrameNumber, R.id.IV_ScanMotorNumber, R.id
            .image_scanPlateNumber})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.relative_vehicleBrand:
                intent.setClass(PreFirstActivity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.relative_vehicleColor:
                dialogShow(1, "");
                break;
            case R.id.relative_vehicleColor2:
                dialogShow(2, "");
                break;
            case R.id.text_buyTime:
                timePickerView.show();
                break;
            case R.id.btn_next:
                mLog.e("VEHICLETYPE=" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
                if (!checkData()) {
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString("in", in);
                ActivityUtil.goActivityWithBundle(PreFirstActivity.this, PreSecondActivity.class, bundle);
                break;
            case R.id.image_scanPlateNumber:
                Logger.d("车牌号");
                ScanType = "ScanPlate";
                Scan(0, true, true, "请输入车牌号");
                break;
            case R.id.IV_ScanFrameNumber:
                Logger.d("车架号");
                ScanType = "ScanFrame";
                Scan(1, false, false, "请输入车架号");
                break;
            case R.id.IV_ScanMotorNumber:
                Logger.d("电机号");
                ScanType = "ScanMotor";
                Scan(1, false, false, "请输入电机号");
                break;



            default:
                break;
        }
    }
    private final static int SCANNIN_QR_CODE = 0514;//二维码回调值*
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
        ActivityUtil.goActivityForResultWithBundle(PreFirstActivity.this, QRCodeScanActivity.class, bundle,
                SCANNIN_QR_CODE);
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

                    for (int i = 0; i < PLI.size(); i++) {
                        SharedPreferencesUtils.put("Photo:" + PLI.get(i).getINDEX(), "");
                        com.orhanobut.logger.Logger.d("清理 Photo:" + PLI.get(i).getINDEX());
                    }

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
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors1.setOnItemClickListener(this);
            colorsList.clear();
            colorsAdapter = new ColorAdapter(mContext, colorsList,
                    colorsMap1, R.layout.brand_item,
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
            mLog.e("颜色数量：", "" + colorList.size());
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
                    textVehicleColor.setText(mColor1);
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
                    colorsMap2, R.layout.brand_item,
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
            mLog.e("颜色数量：", "" + colorList.size());
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
    private ParsingQR mQR;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                textVehicleBrand.setText(brandName);
                brandCode = data.getStringExtra("brandCode");
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
                SharedPreferencesUtils.put("brandName", brandName);//存储显示的车辆品牌
            }
        } else if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                mLog.e("系统API版本:" + PhotoUtils.CurrentapiVersion);
//                Utils.myToast(mContext, "系统API版本"+PhotoUtils.CurrentapiVersion);
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
        }else if (requestCode == SCANNIN_QR_CODE) {
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
                            num = labelNumber;
                        } else {
                            num = mQR.plateNumber(labelNumber);
                        }
                        if (!num.equals("-1")) {
                            editPlateNumber.setText(num);
                        } else {
                            Utils.myToast(mContext, "二维码不属于车牌");
                        }
                    }else if (ScanType.equals("ScanFrame")) {
                        //车架号
                        editFrame.setText(labelNumber);
                    } else if (ScanType.equals("ScanMotor")) {
                        //电机号
                        editMotor.setText(labelNumber);
                    }
                }
            }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                colorsMap1.clear();
                colorsMap1.put(position, 100);
                colorsAdapter.notifyDataSetChanged();
                mColor1 = colorsList.get(position).getName();
                mColorId1 = colorsList.get(position).getGuid();
                break;
            case R.id.list_colors2:
                colorsMap2.clear();
                colorsMap2.put(position, 100);
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
        String brand = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND);
        if (brand.equals("") || brand == null) {
            Utils.myToast(mContext, "请选择车辆品牌");
            return false;
        }
        String platenumber = textPlateNumber.getText().toString().trim().toUpperCase();
        if (textPlateNumber.getVisibility() != View.GONE) {
            if (platenumber.equals("") || platenumber == null) {
                Utils.myToast(mContext, "请输入车牌号");
                return false;
            }
        }
        String frame = editFrame.getText().toString().trim().toUpperCase();
        if (frame.equals("") || frame == null) {
            Utils.myToast(mContext, "请输入车架号");
            return false;
        }
        String motor = editMotor.getText().toString().trim().toUpperCase();
        if (motor.equals("") || motor == null) {
            Utils.myToast(mContext, "请输入电机号");
            return false;
        }
        String colorId = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID);
        if (colorId.equals("") || colorId == null) {
            Utils.myToast(mContext, "请选择电动车颜色");
            return false;
        }
        String buyTime = textBuyTime.getText().toString();
        if (buyTime.equals("") || buyTime == null) {
            Utils.myToast(mContext, "请选择车辆购买时间");
            return false;
        }
        if (!Utils.CheckBuyTime(buyTime)) {
            Utils.myToast(mContext, "您选择的时间已超过当前时间");
            return false;
        }

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, CarType);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, isConfirm);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, editPlateNumber.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, frame);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, motor);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, buyTime);

        return true;
    }

    private ZProgressHUD mProgressHUD;

    private void getBaseData() {
        num++;
        mLog.e("BaseData");
        mProgressHUD.setMessage("更新本地数据...");
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", DESCoder.encrypt("GETCITYLIST", Constants.DES_KEY));
        map.put("infoJsonStr", "_ANDROID");

        WebServiceUtils.callWebService(mActivity, Constants.WEBSERVER_URL, Constants.WEBSERVER_OPENAPI, map, new
                WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            mLog.e("更新数据：");
//                    Log.e("Pan","getBaseData_result= "+result);
                            Utils.LOGE("Pan", result);
                            try {
                                db.dropTable(BaseInfo.class);
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                List<BaseInfo> baseInfos = new ArrayList<BaseInfo>();
                                if (errorCode == 0) {
                                    baseInfos = mGson.fromJson(data, new TypeToken<List<BaseInfo>>() {
                                    }.getType());
                                    if (baseInfos.size() > 0 && baseInfos != null) {
                                        for (int i = 0; i < baseInfos.size(); i++) {
                                            db.save(baseInfos.get(i));
                                        }
                                    }
                                    getPhotoConfig();
                                    mProgressHUD.dismiss();
                                } else {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mProgressHUD.dismiss();
                                Utils.myToast(mContext, "JSON解析出错");
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, "获取数据超时，请检查网络连接");
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.getServerTime();
    }

}

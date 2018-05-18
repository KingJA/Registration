package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.RegularChecker;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 电信预登记车辆信息
 */
@ContentView(R.layout.activity_dx_pre_registration)
public class DX_PreRegistration_Car_Activity extends Activity implements View.OnClickListener, AdapterView
        .OnItemClickListener {
    private static final String TAG = "DX_PreRegistration_Car_Activity";
    @ViewInject(R.id.IV_back)
    private ImageView IV_back;

    @ViewInject(R.id.RL_DX_PhotoList)
    private RecyclerView RL_DX_PhotoList;

    @ViewInject(R.id.TV_CarBrand)
    private TextView TV_CarBrand;

    @ViewInject(R.id.ET_PlateNumber)
    private EditText ET_PlateNumber;

    @ViewInject(R.id.ET_FrameNumber)
    private EditText ET_FrameNumber;

    @ViewInject(R.id.ET_MotorNumber)
    private EditText ET_MotorNumber;

    @ViewInject(R.id.TV_Color)
    private TextView TV_Color;

    @ViewInject(R.id.TV_BuyTime)
    private TextView TV_BuyTime;

    @ViewInject(R.id.BT_Next)
    private Button BT_Next;

    @ViewInject(R.id.IV_ScanFrameNumber)
    ImageView IV_ScanFrameNumber;
    @ViewInject(R.id.IV_ScanMotorNumber)
    ImageView IV_ScanMotorNumber;
    @ViewInject(R.id.image_scanPlateNumber)
    ImageView imageScanPlateNumber;

    private Activity mActivity;

    private final static int BRAND_CODE = 2016;//品牌回调
    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;
    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap1 = new HashMap<Integer, Integer>();

    private String mColor1 = "";// 颜色1
    private String mColorId1 = "";// 颜色1的ID

    private DbManager db;
    private CharacterParser characterParser;
    private TimePickerView timePickerView;
    public boolean CheckTime;
    private String city = "";
    private List<PhotoListInfo> PLI;
    private PhotoListAdapter PLA;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "当前Actity: " );
        x.view().inject(this);
        mQR = new ParsingQR();
        initdate();
        setClick();
        getPhotoConfig();
    }

    /**
     * 加载数据
     */
    private void initdate() {
        mActivity = this;
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        db = x.getDb(DBUtils.getDb());
        characterParser = CharacterParser.getInstance();//初始化汉子转拼音控件

        //设置时间选择器
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                TV_BuyTime.setText(Utils.setDate(date));
                Utils.CheckBuyTime(TV_BuyTime.getText().toString(), new Utils.GetServerTime() {
                    @Override
                    public void ServerTime(String ST, boolean Check) {
                        CheckTime = Check;
                    }
                });
            }
        });
//        Utils.CheckBuyTime(TV_BuyTime.getText().toString(), new Utils.GetServerTime() {
//            @Override
//            public void ServerTime(String ST, boolean Check) {
//                CheckTime = Check;
//            }
//        });

        String isScanCard = (String) SharedPreferencesUtils.get("isScanCard", "");
        String IsScanDjh = (String) SharedPreferencesUtils.get("IsScanDjh", "");
        String IsScanCjh = (String) SharedPreferencesUtils.get("IsScanCjh", "");
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
    }

    /**
     * 读取照片列表
     */
    private void getPhotoConfig() {
        PLI = new ArrayList<PhotoListInfo>();
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
            if (JA.length() == 0) {
                RL_DX_PhotoList.setVisibility(View.GONE);
            } else {
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
            }
        } catch (Exception e) {
            mLog.e("数据异常");
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
        RL_DX_PhotoList.setLayoutManager(linearLayoutManager);

        RL_DX_PhotoList.addItemDecoration(new RecyclerViewItemDecoration());//设置Item间隔
        List<PhotoListAdapter.DrawableList> DrawableList = new ArrayList<PhotoListAdapter.DrawableList>();
        for (int i = 0; i < PLI.size(); i++) {
            String plateNumStr = (String) SharedPreferencesUtils.get("Photo:" + PLI.get(i).getINDEX(), "");
            if (!plateNumStr.equals("")) {
                Bitmap bitmap = Utils.stringtoBitmap(plateNumStr);
                DrawableList.add(new PhotoListAdapter.DrawableList(PLI.get(i).getINDEX(), new BitmapDrawable(bitmap)));
            }
        }
        PLA = new PhotoListAdapter(mActivity, PLI, DrawableList);
        RL_DX_PhotoList.smoothScrollToPosition(PLI.size());
        RL_DX_PhotoList.smoothScrollToPosition(0);
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
            }
        });
        RL_DX_PhotoList.setAdapter(PLA);
    }

    private void setClick() {
        IV_back.setOnClickListener(this);
        TV_CarBrand.setOnClickListener(this);
        TV_Color.setOnClickListener(this);
        TV_BuyTime.setOnClickListener(this);
        BT_Next.setOnClickListener(this);

        IV_ScanFrameNumber.setOnClickListener(this);
        IV_ScanMotorNumber.setOnClickListener(this);
        imageScanPlateNumber.setOnClickListener(this);
    }

    /**
     * 下一步
     */
    private void GoNext() {
        String CarBrand = TV_CarBrand.getText().toString().trim();
        String PlateNumber = ET_PlateNumber.getText().toString().trim();
        String FrameNumber = ET_FrameNumber.getText().toString().trim();
        String MotorNumber = ET_MotorNumber.getText().toString().trim();
        String Color = TV_Color.getText().toString().trim();
        String BuyTime = TV_BuyTime.getText().toString().trim();

        if (RL_DX_PhotoList.getVisibility() != View.GONE) {
            for (int i = 0; i < PLI.size(); i++) {
                if (!PLA.checkItemDate(i)) {
                    Utils.showToast("请拍摄" + PLI.get(i).getREMARK());
                    return;
                }
            }
        }
        String CarRegular = (String) SharedPreferencesUtils.get("PlatenumberRegular" + VehiclesStorageUtils
                .getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE), "");
        mLog.e("车牌正则：" + CarRegular);
        Pattern pattern = Pattern.compile(CarRegular);
        Matcher matcher = pattern.matcher(PlateNumber + "");
        if (!matcher.matches()) {
            Utils.showToast("输入的车牌有误，请重新确认");
            return;
        }
        if (CarBrand.equals("")) {
            Utils.showToast(TV_CarBrand.getHint().toString());
            return;
        }



        if (FrameNumber.equals("")) {
            Utils.showToast("请输入车架号");
            return;
        }
        if (MotorNumber.equals("")) {
            Utils.showToast("请输入电机号");
            return;
        }
        if (!RegularChecker.checkShelvesNoRegular(FrameNumber)) {
            return ;
        }
        if (!RegularChecker.checkEngineNoRegular(MotorNumber)) {
            return ;
        }

        if (Color.equals("")) {
            Utils.showToast(TV_Color.getHint().toString());
            return;
        }
        if (BuyTime.equals("")) {
            Utils.showToast(TV_BuyTime.getHint().toString());
            return;
        }
        if (!CheckTime) {
            if (Utils.ServerTime == null || Utils.ServerTime.equals("")) {
                Utils.showToast("获取服务器时间异常。");
            } else {
                Utils.showToast("您选择的时间已超过当前时间");
            }
            return;
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "1");

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, PlateNumber);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, FrameNumber);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, MotorNumber);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, mColorId1);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, BuyTime);
        ActivityUtil.goActivity(DX_PreRegistration_Car_Activity.this, DX_PreRegistration_Personnel_Activity.class);
    }

    /**
     * 颜色选择弹窗
     */
    private void ColorDialogShow(int flag) {
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
            colorsList.clear();
            colorsAdapter = new ColorAdapter(mActivity, colorsList,
                    colorsMap1, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(colorsAdapter);
            List<BikeCode> colorList = null;
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
            dialogBuilder.setCustomView(color_view, mActivity);
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
                    TV_Color.setText(mColor1);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, mColorId1);
                }
            }).show();
        } else if (flag == 4) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("信息编辑中，确认离开该页面？")
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, mActivity).withButton2Text("确认").setButton1Click(new View
                    .OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }
            }).show();
        }
    }

    @Override
    public void onBackPressed() {
        ColorDialogShow(4);
    }
    private String ScanType = "";//扫描类型
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.IV_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.TV_CarBrand:
                intent.setClass(DX_PreRegistration_Car_Activity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.TV_Color:
                ColorDialogShow(0);
                break;
            case R.id.TV_BuyTime:
                timePickerView.show();
                break;
            case R.id.BT_Next:
                GoNext();
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
        ActivityUtil.goActivityForResultWithBundle(DX_PreRegistration_Car_Activity.this, QRCodeScanActivity.class, bundle,
                SCANNIN_QR_CODE);
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
            default:
                break;
        }
    }
    private ParsingQR mQR;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                TV_CarBrand.setText(brandName);
                String brandCode = data.getStringExtra("brandCode");
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
            }
        } else if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                mLog.e("系统API版本:" + PhotoUtils.CurrentapiVersion);
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
                    Utils.myToast(this, "没有扫描到二维码");
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
                            ET_PlateNumber.setText(num);
                        } else {
                            Utils.myToast(this, "二维码不属于车牌");
                        }
                    }else if (ScanType.equals("ScanFrame")) {
                        //车架号
                        ET_FrameNumber.setText(labelNumber);
                    } else if (ScanType.equals("ScanMotor")) {
                        //电机号
                        ET_MotorNumber.setText(labelNumber);
                    }
                }
            }
        }
    }
    /**
     * Item刷新
     */
    private void UpDatePhotoItem() {
        Bitmap bitmap = PhotoUtils.getPhotoBitmap();
        String Photoindex[] = PhotoUtils.mPicName.split(":");
        Drawable drawable = new BitmapDrawable(bitmap);

        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RL_DX_PhotoList
                .findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
        my.Photo.setBackgroundDrawable(drawable);
        my.PhotoName.setTextColor(Color.WHITE);
        PLA.SevePhoto(Photoindex[1]);
    }

    /**
     * Item刷新
     */
    private void UpDatePhotoItem2(Intent data) {
        Bitmap bitmap = PhotoUtils.getPhotoBitmap(data);
        String Photoindex[] = PhotoUtils.mPicName.split(":");
        Drawable drawable = new BitmapDrawable(bitmap);

        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RL_DX_PhotoList
                .findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
        my.Photo.setBackgroundDrawable(drawable);
        my.PhotoName.setTextColor(Color.WHITE);
        PhotoUtils.sevephoto(bitmap);
        PLA.SevePhoto(Photoindex[1]);
    }

}
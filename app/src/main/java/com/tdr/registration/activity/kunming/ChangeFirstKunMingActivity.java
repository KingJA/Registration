package com.tdr.registration.activity.kunming;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 信息变更
 */
public class ChangeFirstKunMingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "ChangeFirstKunMingActivity";
    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码回调值

    private final static int BRAND_CODE = 2016;//品牌回调
    private Intent intent;
    private Activity mActivity;
    private Context mContext;
    @BindView(R.id.text_guleCar)
    TextView textGuleCar;

    @BindView(R.id.relative_guleBattery)
    ImageView relativeGuleBattery;//photo8：电瓶标签胶水
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.relative_guleCar)
    ImageView relativeGuleCar;//photo7，标签涂胶水照片
    @BindView(R.id.relative_labelA)
    ImageView relativeLabelA;//photo5，车辆标签安装位置
    @BindView(R.id.relative_labelB)
    ImageView relativeLabelB;//photo6，电池标签安装位置
    @BindView(R.id.relative_plateNum)
    ImageView relativePlateNum;//photo4，后方全车照
    @BindView(R.id.relative_applicationForm)
    ImageView relativeApplicationForm;//photo1，前方全车照
    @BindView(R.id.relative_invoice)
    ImageView relativeInvoice;//photo3，发票/来历证明承诺书
    @BindView(R.id.relative_identity)
    ImageView relativeIdentity;
    @BindView(R.id.text_guleBattery)
    TextView textGuleBattery;
    @BindView(R.id.text_labelA)
    TextView textLabelA;
    @BindView(R.id.text_labelB)
    TextView textLabelB;
    @BindView(R.id.text_plateNum)
    TextView textPlateNum;
    @BindView(R.id.text_applicationForm)
    TextView textApplicationForm;
    @BindView(R.id.text_invoice)
    TextView textInvoice;
    @BindView(R.id.text_identity)
    TextView textIdentity;
    @BindView(R.id.linear_vehicleBrand)
    LinearLayout linearVehicleBrand;
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
    @BindView(R.id.relative_vehicleBrand)
    RelativeLayout relativeVehicleBrand;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.image_scanPlateNumber)
    ImageView imageScanPlateNumber;
    @BindView(R.id.edit_frame)
    EditText editFrame;
    @BindView(R.id.edit_motor)
    EditText editMotor;
    @BindView(R.id.relative_vehicleColor)
    RelativeLayout relativeVehicleColor;
    @BindView(R.id.text_vehicleColor)
    TextView textVehicleColor;
    @BindView(R.id.text_vehicleColor2)
    TextView textVehicleColor2;
    @BindView(R.id.relative_vehicleColor2)
    RelativeLayout relativeVehicleColor2;
    @BindView(R.id.text_buyTime)
    TextView textBuyTime;
    @BindView(R.id.frame_identity)
    FrameLayout frameIdentity;
    @BindView(R.id.btn_next)
    Button btnNext;

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
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private String city = "";//当前帐号所在市

    private String carType = "";//车辆类型
    private String isConfirm = "";//来历承诺书

    private ElectricCarModel model;
    private List<PhotoModel> photoModels = new ArrayList<>();

    private String photo1 = "";
    private String photo2 = "";
    private String photo3 = "";
    private String photo4 = "";
    private String photoList = "";

    private TimePickerView timePickerView;
    private String PhotoConfig = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first_kunming);
        ButterKnife.bind(this);
        mActivity=this;
        initView();
        db = x.getDb(DBUtils.getDb());
        intent = new Intent();
        model = new ElectricCarModel();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mContext = this;
        city = (String) SharedPreferencesUtils.get("city", "");
        //clearImage();
        initData();
    }

    private void initView() {
        textTitle.setText("信息变更");
        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        rbOldVehicle.setChecked(true);
        imageScanPlateNumber.setVisibility(View.GONE);
        carType = "1";
        groupVehicletype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbNewVehicle.getId()) {//新销售 0 ,已上路 1
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "0");
                    carType = "0";
                } else {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "1");
                    carType = "1";
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
    }

    private void initData() {
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            model = (ElectricCarModel) bundle.getSerializable("model");
        }

        photo1 = model.getPhoto1();
        photo2 = model.getPhoto2();
        photo3 = model.getPhoto3();
        photo4 = model.getPhoto4();
        photoList = Utils.initNullStr(model.getPhotoList());
        if (!photoList.equals("")) {
            try {
                JSONArray jsonArray = new JSONArray(photoList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String index = json.getString("INDEX");
                    String photo = json.getString("Photo");
                    initImages(Integer.valueOf(index), photo);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initImages(1, photo1);
        initImages(2, photo2);
        initImages(3, photo3);
        initImages(4, photo4);

        if (model.getCARTYPE().equals("0")) {
            rbNewVehicle.setChecked(true);
        } else {
            rbOldVehicle.setChecked(true);
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, model.getCARTYPE());
        if (model.getISCONFIRM().equals("1")) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, model.getISCONFIRM());
        textVehicleBrand.setText(model.getVehicleBrandName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, model.getVehicleBrand());
        editPlateNumber.setText(model.getPlateNumber());
        editFrame.setText(model.getShelvesNo());
        editMotor.setText(model.getEngineNo());
        textVehicleColor.setText(model.getColorName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, model.getColorId());
        textVehicleColor2.setText(model.getColorName2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, model.getColorId2());
        textBuyTime.setText(Utils.dateWithoutTime(model.getBuyDate()));

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, model.getCARDTYPE());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, model.getVehicleType());

        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ECID, model.getEcId());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, model.getOwnerName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, model.getCardId());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, model.getPhone1());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, model.getPhone2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, model.getResidentAddress());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, model.getRemark());
    }

    /**
     * 获取图片
     */
    private void initImages(final int i, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pictureGUID", id);
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPICTURE, map, new WebServiceUtils.WebServiceCallBack() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Bitmap bitmap = Utils.stringtoBitmap(result);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
                    switch (i) {//6(labelB),5(labelA),1(applicationForm ),7(guleCar),2(identity),4(plateNum),3(invoice),8(guleBattery)
                        case 1:
                            relativeApplicationForm.setBackground(drawable);
                            textApplicationForm.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("applicationForm", photoStr);
                            break;
                        case 2:
                            relativeIdentity.setBackground(drawable);
                            textIdentity.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("identity", photoStr);
                            break;
                        case 3:
                            relativeInvoice.setBackground(drawable);
                            textInvoice.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("invoice", photoStr);
                            break;
                        case 4:
                            relativePlateNum.setBackground(drawable);
                            textPlateNum.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("plateNum", photoStr);
                            break;
                        case 5:
                            relativeLabelA.setBackground(drawable);
                            textLabelA.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("labelA", photoStr);//5
                            break;
                        case 6:
                            relativeLabelB.setBackground(drawable);
                            textLabelB.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("labelB", photoStr);
                            break;
                        case 7:
                            relativeGuleCar.setBackground(drawable);
                            textGuleCar.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("guleCar", photoStr);//7
                            break;
                        case 8:
                            relativeGuleBattery.setBackground(drawable);
                            textGuleBattery.setTextColor(getResources().getColor(R.color.white));
                            SharedPreferencesUtils.put("guleBattery", photoStr);
                            break;
                    }
                }
            }
        });
    }

    @OnClick({R.id.image_back, R.id.image_scan, R.id.text_buyTime, R.id.relative_guleCar, R.id.relative_guleBattery, R.id.relative_labelA, R.id.relative_labelB, R.id.relative_plateNum, R.id.relative_applicationForm, R.id.relative_invoice, R.id.relative_identity, R.id.relative_vehicleBrand, R.id.relative_vehicleColor, R.id.relative_vehicleColor2, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.text_buyTime:
                timePickerView.show();
                break;
            case R.id.image_scan:
                intent.setClass(this, QRCodeScanActivity.class);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.relative_guleCar:
                PhotoUtils.TakePicture(mActivity,"guleCar");
                break;
            case R.id.relative_guleBattery:
                PhotoUtils.TakePicture(mActivity,"guleBattery");
                break;
            case R.id.relative_labelA:
                PhotoUtils.TakePicture(mActivity,"labelA");
                break;
            case R.id.relative_labelB:
                PhotoUtils.TakePicture(mActivity,"labelB");
                break;
            case R.id.relative_plateNum:
                PhotoUtils.TakePicture(mActivity,"plateNum");
                break;
            case R.id.relative_applicationForm:
                PhotoUtils.TakePicture(mActivity,"applicationForm");
                break;
            case R.id.relative_invoice:
                PhotoUtils.TakePicture(mActivity,"invoice");
                break;
            case R.id.relative_identity:
                PhotoUtils.TakePicture(mActivity,"identity");
                break;
            case R.id.relative_vehicleBrand:
                intent.setClass(ChangeFirstKunMingActivity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.relative_vehicleColor:
                dialogShow(1, "");
                break;
            case R.id.relative_vehicleColor2:
                dialogShow(2, "");
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString("PhotoConfig", PhotoConfig);
                ActivityUtil.goActivity(ChangeFirstKunMingActivity.this, ChangeSecondActivity.class);
                break;
        }
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
                colorList= db.selector(BikeCode.class).where("type", "=", "4").findAll();
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
                    textVehicleColor.setText(mColor);
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
                colorList= db.selector(BikeCode.class).where("type", "=", "4").findAll();
            } catch (DbException e) {
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
                    case "guleCar":
                        relativeGuleCar.setBackground(bd);
                        SharedPreferencesUtils.put("guleCar", photoStr);
                        textGuleCar.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "guleBattery":
                        relativeGuleBattery.setBackground(bd);
                        SharedPreferencesUtils.put("guleBattery", photoStr);
                        textGuleBattery.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "labelA":
                        relativeLabelA.setBackground(bd);
                        SharedPreferencesUtils.put("labelA", photoStr);
                        textLabelA.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "labelB":
                        relativeLabelB.setBackground(bd);
                        SharedPreferencesUtils.put("labelB", photoStr);
                        textLabelB.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "plateNum":
                        relativePlateNum.setBackground(bd);
                        SharedPreferencesUtils.put("plateNum", photoStr);
                        textPlateNum.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "applicationForm":
                        relativeApplicationForm.setBackground(bd);
                        SharedPreferencesUtils.put("applicationForm", photoStr);
                        textApplicationForm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "invoice":
                        relativeInvoice.setBackground(bd);
                        SharedPreferencesUtils.put("invoice", photoStr);
                        textInvoice.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "identity":
                        relativeIdentity.setBackground(bd);
                        SharedPreferencesUtils.put("identity", photoStr);
                        textIdentity.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        } else if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                textVehicleBrand.setText(brandName);
                String brandCode = data.getStringExtra("brandCode");
                model.setVehicleBrand(brandCode);
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
                SharedPreferencesUtils.put("brandName", brandName);//存储显示的车辆品牌

            }
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
        String labelBStr = (String) SharedPreferencesUtils.get("applicationForm", "");
        if (labelBStr.equals("")) {
            Utils.myToast(mContext, "请拍摄前侧车身的照片");
            return false;
        }
        String plateNumStr = (String) SharedPreferencesUtils.get("plateNum", "");
        if (plateNumStr.equals("")) {
            Utils.myToast(mContext, "请拍摄后侧车身的照片");
            return false;
        }
        if (carType.equals("")) {
            Utils.myToast(mContext, "请选择车辆类型");
            return false;
        }
        if (isConfirm.equals("")) {
            Utils.myToast(mContext, "请选择是否有来历承诺书");
            return false;
        }
        String brand = model.getVehicleBrand();
        if (brand.equals("") || brand == null) {
            Utils.myToast(mContext, "请选择车辆品牌");
            return false;
        }
        String plateNum = editPlateNumber.getText().toString().toUpperCase().trim();
        if (plateNum.equals("") || plateNum == null) {
            Utils.myToast(mContext, "请输入车牌");
            return false;
        }
        Pattern pattern = Pattern.compile((String) SharedPreferencesUtils.get("regular1", ""));
        Matcher matcher = pattern.matcher(plateNum + "");
        if (!matcher.matches()) {
            Utils.myToast(mContext, "输入的车牌有误，请重新确认");
            return false;
        }
        String frame = editFrame.getText().toString().trim();
        if (frame.equals("") || frame == null) {
            Utils.myToast(mContext, "请输入车架号");
            return false;
        }
        String motor = editMotor.getText().toString().trim();
        if (motor.equals("") || motor == null) {
            Utils.myToast(mContext, "请输入电机号");
            return false;
        }
        String color = model.getColorId();
        if (color.equals("") || color == null) {
            Utils.myToast(mContext, "请选择电动车颜色");
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
    private void GetReady() {
        db = x.getDb(DBUtils.getDb());
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
        PhotoConfig = BI.getPhotoConfig();
//        Log.e("Pan", "PhotoConfig" + PhotoConfig);
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

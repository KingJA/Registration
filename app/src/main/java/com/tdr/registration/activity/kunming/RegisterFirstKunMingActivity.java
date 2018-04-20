package com.tdr.registration.activity.kunming;

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
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.PinyinComparator;
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

import static com.tdr.registration.R.id.text_vehicleBrand;

/**
 * 备案登记第一页
 */
public class RegisterFirstKunMingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "RegisterFirstKunMingActivity";

    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码扫描回调
    private final static int BRAND_CODE = 2016;//品牌回调

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.relative_guleCar)
    ImageView relativeGuleCar;
    @BindView(R.id.text_guleCar)
    TextView textGuleCar;
    @BindView(R.id.relative_guleBattery)
    ImageView relativeGuleBattery;
    @BindView(R.id.text_guleBattery)
    TextView textGuleBattery;
    @BindView(R.id.relative_labelA)
    ImageView relativeLabelA;
    @BindView(R.id.text_labelA)
    TextView textLabelA;
    @BindView(R.id.relative_labelB)
    ImageView relativeLabelB;
    @BindView(R.id.text_labelB)
    TextView textLabelB;
    @BindView(R.id.relative_applicationForm)
    ImageView relativeApplicationForm;
    @BindView(R.id.text_applicationForm)
    TextView textApplicationForm;
    @BindView(R.id.relative_plateNum)
    ImageView relativePlateNum;
    @BindView(R.id.text_plateNum)
    TextView textPlateNum;
    @BindView(R.id.relative_invoice)
    ImageView relativeInvoice;
    @BindView(R.id.text_invoice)
    TextView textInvoice;
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
    @BindView(text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.relative_vehicleBrand)
    RelativeLayout relativeVehicleBrand;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.image_scanPlateNumber)
    ImageView imageScanPlateNumber;
    @BindView(R.id.edit_frame)
    EditText editFrame;
    @BindView(R.id.edit_motor)
    EditText editMotor;
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
    @BindView(R.id.relative_identity)
    ImageView relativeIdentity;
    @BindView(R.id.text_identity)
    TextView textIdentity;

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

    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private String city = "";//当前帐号所在市
    private String carType = "1";//车辆类型

    private String isConfirm = "0";//来历承诺书

    private List<ElectricCarModel> electricCarModelList = new ArrayList<>();
    private ElectricCarModel model;
    private String activity = "";
    private String listId = "";
    private String insurance = "";
    private String ecId = "";

    private ParsingQR mQR;

    private TimePickerView timePickerView;

    private ZProgressHUD mProgressHUD;

    private List<UploadInsuranceModel> models = new ArrayList<>();
    private ArrayList list;
    private String PlatenumberRegular;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first_kunming);
        ButterKnife.bind(this);
        mActivity=this;
        mContext = this;
        db =  x.getDb(DBUtils.getDb());

        intent = new Intent();
        mQR = new ParsingQR();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        city = (String) SharedPreferencesUtils.get("city", "");
        Bundle bundle = (Bundle) getIntent().getExtras();
        activity = bundle.getString("activity");
        list = bundle.getParcelableArrayList("insurance");
        models = (List<UploadInsuranceModel>) list.get(0);
        PlatenumberRegular= (String) SharedPreferencesUtils.get("PlatenumberRegular"+"1", "");
        //insurance = bundle.getString("insurance");
        ecId = UUID.randomUUID().toString().toUpperCase();
        initView();
        initData();
    }

    private void initView() {
        if (activity.equals("")) {
            textTitle.setText("防盗登记");
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, "2015-01-01");
        } else if (activity.equals("seizure")) {
            textTitle.setText("扣押转正式");
        }
        btnNext.setText("提交");
        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        editPlateNumber.setFocusable(false);
        editFrame.setTransformationMethod(new AllCapTransformationMethod(true));
        editMotor.setTransformationMethod(new AllCapTransformationMethod(true));
        rbOldVehicle.setChecked(true);
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

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initData() {
        String plateNumStr = (String) SharedPreferencesUtils.get("plateNum", "");
        if (!plateNumStr.equals("")) {
            Bitmap bitmap = Utils.stringtoBitmap(plateNumStr);
            Drawable bd = new BitmapDrawable(bitmap);
            relativePlateNum.setBackground(bd);
            textPlateNum.setTextColor(getResources().getColor(R.color.white));
        }
        String applicationFormStr = (String) SharedPreferencesUtils.get("applicationForm", "");
        if (!applicationFormStr.equals("")) {
            Bitmap bitmap = Utils.stringtoBitmap(applicationFormStr);
            Drawable bd = new BitmapDrawable(bitmap);
            relativeApplicationForm.setBackground(bd);
            textApplicationForm.setTextColor(getResources().getColor(R.color.white));
        }
        String identityStr = (String) SharedPreferencesUtils.get("identity", "");
        if (!identityStr.equals("")) {
            Bitmap bitmap = Utils.stringtoBitmap(identityStr);
            Drawable bd = new BitmapDrawable(bitmap);
            relativeIdentity.setBackground(bd);
            textIdentity.setTextColor(getResources().getColor(R.color.white));
        }
        textVehicleBrand.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME));
        editPlateNumber.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
        editFrame.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
        editMotor.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
        textVehicleColor.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1NAME));
        textBuyTime.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));
    }

    @OnClick({R.id.image_back, R.id.image_scanPlateNumber, R.id.relative_guleCar, R.id.relative_guleBattery, R.id.relative_labelA, R.id.relative_labelB, R.id.relative_applicationForm, R.id.relative_identity, R.id.relative_plateNum, R.id.relative_invoice, R.id.relative_vehicleBrand, R.id.relative_vehicleColor, R.id.relative_vehicleColor2, R.id.text_buyTime, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
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
                intent.setClass(RegisterFirstKunMingActivity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.image_scanPlateNumber:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShow", true);
                bundle.putBoolean("isPlateNumber", true);
                bundle.putString("activity", "");
                ActivityUtil.goActivityForResultWithBundle(RegisterFirstKunMingActivity.this, QRCodeScanActivity.class, bundle, SCANNIN_GREQUEST_CODE);
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
                if (!checkData()) {
                    break;
                }
                sendMsg();
                break;
        }
    }



    @Override
    public void onBackPressed() {
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, isConfirm);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, editPlateNumber.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, editFrame.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, editMotor.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, carType);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, textBuyTime.getText().toString());
        finish();
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
                    //清空图片缓存
                    SharedPreferencesUtils.put("guleCar", "");
                    SharedPreferencesUtils.put("guleBattery", "");
                    SharedPreferencesUtils.put("labelA", "");
                    SharedPreferencesUtils.put("labelB", "");
                    SharedPreferencesUtils.put("plateNum", "");
                    SharedPreferencesUtils.put("identity", "");
                    SharedPreferencesUtils.put("applicationForm", "");
                    SharedPreferencesUtils.put("invoice", "");
                    SharedPreferencesUtils.put("install", "");
                    VehiclesStorageUtils.clearData();
                    ActivityUtil.goActivityAndFinish(RegisterFirstKunMingActivity.this, HomeActivity.class);
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
            if(colorList==null){colorList=new ArrayList<BikeCode>();}
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
                brandCode = data.getStringExtra("brandCode");
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
                //SharedPreferencesUtils.put("brandName", brandName);//存
                // 储显示的车辆品牌
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, brandName);
            }
        } else if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.myToast(mContext, "没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    String isPlateNumber = bundle.getString("isPlateNumber");
                    String plateNumberRead = mQR.plateNumber(scanResult);
                    if (isPlateNumber.equals("0")) {
                        editPlateNumber.setText(scanResult);
                    } else {
                        editPlateNumber.setText(plateNumberRead);
                    }
                    // String plateNumberInput = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER);//用户输入的车牌
                  /*  if (isPlateNumber.equals("0")) {
                        if (scanResult.equals(plateNumberInput)) {
                            sendMsg();
                        } else {
                            Utils.myToast(mContext, "输入的车牌有误，请重新确认");
                            return;
                        }
                    } else {
                        String plateNumberRead = mQR.plateNumber(scanResult);
                        if (plateNumberRead.equals(plateNumberInput)) {
                            //sendMsg();
                            editPlateNumber.setText(plateNumberRead);
                        } else if (plateNumberRead.equals("-1")) {
                            Utils.myToast(mContext, "校验不通过，请确认车牌合法正确性");
                            return;
                        } else {
                            Utils.myToast(mContext, "输入的车牌有误，请重新确认");
                            return;
                        }
                    }*/
                }
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

        String brand = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND);
        if (brand.equals("") || brand == null) {
            Utils.myToast(mContext, "请选择车辆品牌");
            return false;
        }
        String plateNum = editPlateNumber.getText().toString().toUpperCase().trim();
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
        if(!Utils.check_FrameOrMotor(frame)&&!Utils.check_FrameOrMotor(motor)){
                Utils.myToast(mContext, "电机号与车架号必须正确录入其中一个");
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
        if(!Utils.CheckBuyTime(textBuyTime.getText().toString())){
            Utils.myToast(mContext, "您选择的时间已超过当前时间");
            return false;
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, isConfirm);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, plateNum);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, frame);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, motor);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, carType);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, buyTime);

        return true;
    }

    private void sendMsg() {
        JSONObject obj = new JSONObject();
        String photo7 = (String) SharedPreferencesUtils.get("guleCar", "");
        String photo8 = (String) SharedPreferencesUtils.get("guleBattery", "");
        String photo5 = (String) SharedPreferencesUtils.get("labelA", "");
        String photo6 = (String) SharedPreferencesUtils.get("labelB", "");
        String photo4 = (String) SharedPreferencesUtils.get("plateNum", "");
        String photo2 = (String) SharedPreferencesUtils.get("identity", "");
        String photo1 = (String) SharedPreferencesUtils.get("applicationForm", "");
        String photo3 = (String) SharedPreferencesUtils.get("invoice", "");
        try {
            obj.put("EcId", ecId);
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ECID, ecId);
            obj.put("HasRFID", "1");// 是否有有源标签,1有，0无
            obj.put("VehicleModels", "");
            obj.put("VEHICLETYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
            obj.put("THEFTNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
            obj.put("Photo1File", photo1);
            obj.put("Photo2File", photo2);
            obj.put("Photo3File", photo3);
            obj.put("Photo4File", photo4);
            obj.put("REGISTERID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REGISTERID));
            obj.put("CARTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE));
            obj.put("ISCONFIRM", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ISCONFIRM));
            obj.put("VehicleBrand", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
            obj.put("PlateNumber", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
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
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject5 = new JSONObject();
            JSONObject jsonObject6 = new JSONObject();
            JSONObject jsonObject7 = new JSONObject();
            JSONObject jsonObject8 = new JSONObject();
            jsonObject5.put("INDEX", "5");
            jsonObject5.put("Photo", "");
            jsonObject5.put("PhotoFile", photo5);
            jsonObject6.put("INDEX", "6");
            jsonObject6.put("Photo", "");
            jsonObject6.put("PhotoFile", photo6);
            jsonObject7.put("INDEX", "7");
            jsonObject7.put("Photo", "");
            jsonObject7.put("PhotoFile", photo7);
            jsonObject8.put("INDEX", "8");
            jsonObject8.put("Photo", "");
            jsonObject8.put("PhotoFile", photo8);
            jsonArray.put(jsonObject5);
            jsonArray.put(jsonObject6);
            jsonArray.put(jsonObject7);
            jsonArray.put(jsonObject8);
            obj.put("PhotoListFile", jsonArray);
            JSONArray jsonArray1 = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            for (UploadInsuranceModel uploadInsuranceModel : models) {
                jsonObject1.put("POLICYID", "");
                jsonObject1.put("Type", uploadInsuranceModel.getType());
                jsonObject1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                jsonObject1.put("DeadLine", uploadInsuranceModel.getDeadLine());
                jsonObject1.put("BUYDATE", uploadInsuranceModel.getBUYDATE());
                jsonArray1.put(jsonObject1);
            }
            obj.put("POLICYS", jsonArray1);
            //代办人信息
            obj.put("AGENTNAME", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONNAME));
            obj.put("AGENTCARDTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONCARDTYPE));
            obj.put("AGENTCARDID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONIDENTITY));
            obj.put("AGENTPHONE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONPHONE1));
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
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), functionName, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                mLog.e("result"+result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            //if (locCityName.contains("昆明")) {
                            //   Utils.myToast(mContext, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER) + "  电动车信息上传成功！请打印凭条！");
                            //  goPrinter();
                            //} else {
                            dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER) + "  电动车信息上传成功！");
                            //}
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(RegisterFirstKunMingActivity.this, LoginActivity.class);
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

}

package com.tdr.registration.activity.kunming.motorcycle;

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
import com.tdr.registration.R;
import com.tdr.registration.activity.BrandActivity;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.PreListActivity;
import com.tdr.registration.activity.RegisterSecondActivity;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.PreModel;
import com.tdr.registration.model.SortModel;
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
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.RegistrPop;




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

import static com.tdr.registration.R.id.text_vehicleBrand;

/**
 * 摩托车备案登记第一页
 */
public class MotorcycleFirstKunMingActvity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, RegistrPop.OnRegistrPopClickListener {
    private static final String TAG = "RegisterFirstKunMingActivity";

    private final static int SCANNIN_GREQUEST_CODE = 1991;//二维码回调值
    private final static int BRAND_CODE = 2016;//品牌回调
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_scan)
    ImageView imageScan;
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
    @BindView(R.id.edit_frame)
    EditText editFrame;
    @BindView(R.id.text_motor)
    TextView textMotor;
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
    private Gson mGson;
    private CharacterParser characterParser;

    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private String city = "";//当前帐号所在市
    private String carType = "";//车辆类型

    private String isConfirm = "0";//来历承诺书

    private ZProgressHUD mProgressHUD;

    private PreModel preModel = new PreModel();
    private List<PreModel> preModels = new ArrayList<>();

    private List<ElectricCarModel> electricCarModelList = new ArrayList<>();
    private ElectricCarModel model = new ElectricCarModel();
    private String activity = "";
    private String listId = "";

    private RegistrPop registrPop;

    private TimePickerView timePickerView;
    private String PlatenumberRegular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first_kunming);
        ButterKnife.bind(this);
        mActivity=this;
        db = x.getDb(DBUtils.getDb());
        intent = new Intent();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mContext = this;
        mGson = new Gson();
        city = (String) SharedPreferencesUtils.get("city", "");
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            model = (ElectricCarModel) bundle.getSerializable("model");
            activity = bundle.getString("activity");
            listId = bundle.getString("distrainCarListID");
        }
        PlatenumberRegular= (String) SharedPreferencesUtils.get("PlatenumberRegular"+1, "");
        initView();
        initData();
    }

    private void initView() {
        imageScan.setVisibility(View.VISIBLE);
        imageScan.setBackgroundResource(R.mipmap.register_pop);
        if (activity.equals("")) {
            textTitle.setText("防盗登记");
        } else if (activity.equals("seizure")) {
            textTitle.setText("扣押转正式");
        }
        textMotor.setText("发动机");
        registrPop = new RegistrPop(imageScan, MotorcycleFirstKunMingActvity.this);
        registrPop.setOnRegistrPopClickListener(MotorcycleFirstKunMingActvity.this);

        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));

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

    private void initData() {
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
        if (model != null) {
            carType = Utils.initNullStr(model.getCARTYPE());
            if (carType.equals("0")) {
                rbNewVehicle.setChecked(true);
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "0");
            } else if (carType.equals("1")) {
                rbOldVehicle.setChecked(true);
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARTYPE, "1");
            }

            textVehicleBrand.setText(model.getVehicleBrandName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, model.getVehicleBrandName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, model.getVehicleBrand());
            textVehicleColor.setText(model.getColorName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, model.getColorName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, model.getColorId());
            textVehicleColor2.setText(model.getColorName2());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2ID, model.getColorId2());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR2NAME, model.getColorName2());
            editFrame.setText(model.getShelvesNo());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, model.getShelvesNo());
            editMotor.setText(model.getEngineNo());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, model.getEngineNo());
            textBuyTime.setText(model.getBuyDate());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, model.getBuyDate());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, model.getCARDTYPE());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, model.getOwnerName());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, model.getCardId());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, model.getPhone1());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, model.getPhone2());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, model.getAddress());
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, model.getRemark());
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, "3");

    }

    @OnClick({R.id.image_back, R.id.image_scan,R.id.text_buyTime, R.id.relative_guleCar, R.id.relative_guleBattery, R.id.relative_labelA, R.id.relative_labelB, R.id.relative_applicationForm, R.id.relative_plateNum, R.id.relative_invoice, R.id.relative_vehicleBrand, R.id.relative_vehicleColor, R.id.relative_vehicleColor2, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.image_scan:
                registrPop.showPopupWindowDownOffset();
                break;
            case R.id.text_buyTime:
                timePickerView.show();
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
            case R.id.relative_vehicleBrand:
                intent.setClass(MotorcycleFirstKunMingActvity.this, BrandActivity.class);
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
                bundle.putString("InType","Registration");
                bundle.putString("activity", activity);
                bundle.putString("listId", listId);
                ActivityUtil.goActivityWithBundle(MotorcycleFirstKunMingActvity.this, RegisterSecondActivity.class, bundle);
                break;
        }
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
                colorList=db.selector(BikeCode.class).where("type","=","4").findAll();
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
                colorList=db.selector(BikeCode.class).where("type","=","4").findAll();
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
                    String queryIdentity = editQueryIdentity.getText().toString().toUpperCase();
                    queryPreByIdentity(queryIdentity);
                }
            }).show();
        }
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
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPREREGISTERSBYCARDID, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            electricCarModelList = mGson.fromJson(data, new TypeToken<List<ElectricCarModel>>() {
                            }.getType());
                            if (electricCarModelList.size() > 1) {
                                Bundle bundle = new Bundle();
                                ArrayList list = new ArrayList();
                                list.add(electricCarModelList);
                                bundle.putParcelableArrayList("electricCarModelList", list);
                                ActivityUtil.goActivityWithBundle(MotorcycleFirstKunMingActvity.this, PreListActivity.class, bundle);
                            } else if (electricCarModelList.size() == 1) {
                                dealPreByPolice(electricCarModelList.get(0));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void dealPreByPolice(ElectricCarModel electricCarModel) {
        initData();
    }

    private void dealModel(PreModel preModel) {
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, preModel.getVehiclebrand());
//        List<BikeCode> bikeCodes = db.findAllByWhere(BikeCode.class, "code=\'" + preModel.getVehiclebrand() + "\'");
        List<BikeCode> bikeCodes = null;
        try {
            bikeCodes = db.selector(BikeCode.class).where("code","=", preModel.getVehiclebrand()).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(bikeCodes==null){
            bikeCodes=new ArrayList<BikeCode>();
        }
        textVehicleBrand.setText(bikeCodes.get(0).getName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, bikeCodes.get(0).getName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, preModel.getColorID());
//        List<BikeCode> bikeCodeList = db.findAllByWhere(BikeCode.class, "code=\'" + preModel.getColorID() + "\'");
        List<BikeCode>  bikeCodeList = null;
        try {
            bikeCodeList = db.selector(BikeCode.class).where("code","=", preModel.getColorID()).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(bikeCodeList==null){
            bikeCodeList=new ArrayList<BikeCode>();
        }
        textVehicleColor.setText(bikeCodeList.get(0).getName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1NAME, bikeCodeList.get(0).getName());
        editFrame.setText(preModel.getShelvesno());
        editMotor.setText(preModel.getEngineno());
        textBuyTime.setText(preModel.getBuyDate());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, preModel.getOwnerName());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, preModel.getCardid());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, preModel.getPhone1());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, preModel.getPhone2());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, preModel.getRemark());
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
                }
            }
        } else if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                textVehicleBrand.setText(brandName);
                brandCode = data.getStringExtra("brandCode");
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
                //SharedPreferencesUtils.put("brandName", brandName);//存储显示的车辆品牌
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, brandName);
            }
        } else if (requestCode == SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.myToast(mContext, "没有扫描到二维码");
                    return;
                } else {
                    mProgressHUD.show();
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    getPre(scanResult);
                }
            }
        }

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
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CARDHOLDER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
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
                            if (state.equals("1")) {
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
        String guleCarStr = (String) SharedPreferencesUtils.get("guleCar", "");
        if (guleCarStr.equals("")) {
            Utils.myToast(mContext, "请拍摄车辆标签涂抹胶水的照片");
            return false;
        }
        String guleBatteryStr = (String) SharedPreferencesUtils.get("guleBattery", "");
        if (guleBatteryStr.equals("")) {
            Utils.myToast(mContext, "请拍摄电池标签涂抹胶水的照片");
            return false;
        }
        String labelAStr = (String) SharedPreferencesUtils.get("labelA", "");
        if (labelAStr.equals("")) {
            Utils.myToast(mContext, "请拍摄车辆标签安装位置的照片");
            return false;
        }
        String labelBStr = (String) SharedPreferencesUtils.get("labelB", "");
        if (labelBStr.equals("")) {
            Utils.myToast(mContext, "请拍摄电池标签安装位置的照片");
            return false;
        }
        String plateNumStr = (String) SharedPreferencesUtils.get("plateNum", "");
        if (plateNumStr.equals("")) {
            Utils.myToast(mContext, "请拍摄带牌照的照片");
            return false;
        }
        String applicationFormStr = (String) SharedPreferencesUtils.get("applicationForm", "");
        if (applicationFormStr.equals("")) {
            Utils.myToast(mContext, "请拍摄申请表的照片");
            return false;
        }
        String invoiceStr = (String) SharedPreferencesUtils.get("invoice", "");
        if (invoiceStr.equals("")) {
            Utils.myToast(mContext, "请拍摄发票的照片");
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
        if (motor.equals("") || motor == null) {
            Utils.myToast(mContext, "请输入电机号");
            return false;
        }
        if(!Utils.check_FrameOrMotor(frame)&&!Utils.check_FrameOrMotor(motor)){
            Utils.myToast(mContext, "电机号于车架号必须正确录入其中一个");
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
        if(!Utils.CheckBuyTime(buyTime)){
            Utils.myToast(mContext, "您选择的时间已超过当前时间");
            return false;
        }
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ISCONFIRM, isConfirm);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PLATENUMBER, plateNum);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.SHELVESNO, frame);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ENGINENO, motor);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.BUYDATE, buyTime);

        return true;
    }

    @Override
    public void onRegistrPop(int position) {
        switch (position) {
            case 0:
                intent.setClass(this, QRCodeScanActivity.class);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;

            case 1:
                dialogShow(3, "预登记查询");
                break;
        }
    }
}

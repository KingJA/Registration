package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.gprinter.Util;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.model.TrafficModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;



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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆扣押
 */
public class VehiclesSeizeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @BindView(R.id.edit_concernedName)
    EditText editConcernedName;
    @BindView(R.id.edit_concernedPhone)
    EditText editConcernedPhone;
    @BindView(R.id.edit_concernedIdentity)
    EditText editConcernedIdentity;
    @BindView(R.id.linear_vehicleBrand)
    LinearLayout linearVehicleBrand;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.linear_vehicleColor)
    LinearLayout linearVehicleColor;
    @BindView(R.id.text_vehicleColor)
    TextView textVehicleColor;
    @BindView(R.id.edit_frame)
    EditText editFrame;
    @BindView(R.id.edit_motor)
    EditText editMotor;
    @BindView(R.id.rb_traffic)
    RadioButton rbTraffic;
    @BindView(R.id.rb_police)
    RadioButton rbPolice;
    @BindView(R.id.group_isTraffic)
    RadioGroup groupIsTraffic;
    @BindView(R.id.linear_seizeUnit)
    LinearLayout linearSeizeUnit;
    @BindView(R.id.text_seizeUnit)
    TextView textSeizeUnit;
    @BindView(R.id.linear_receivedUnit)
    LinearLayout linearReceivedUnit;
    @BindView(R.id.text_receivedUnit)
    TextView textReceivedUnit;
    @BindView(R.id.text_seizeTime)
    TextView textSeizeTime;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private final static int BRAND_CODE = 2016;//品牌回调

    private Context mContext;
    private Intent intent;

    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap = new HashMap<Integer, Integer>();
    private List<BikeCode> colorList = new ArrayList<BikeCode>();
    private String mColor;// 颜色
    private String mColorId;// 颜色的ID
    private String mBrandName;//品牌
    private String mBrandNameId;//品牌ID

    private ColorAdapter trafficsAdapter;
    private List<SortModel> trafficsList = new ArrayList<SortModel>();
    private HashMap<Integer, Integer> trafficsMap = new HashMap<Integer, Integer>();
    private String mTraffic = "";
    private String mTrafficId = "";
    private String selectedTraffic;

    private DbManager db;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private String isTraffic = "1";

    private List<TrafficModel> trafficModels = new ArrayList<>();
    //根据所在辖区选择出来的交警列表
    private List<TrafficModel> trafficModelList = new ArrayList<>();

    private ZProgressHUD mProgressHUD;
    private Gson mGson;

    private TimePickerView seizeTime;
    private Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_seize);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        intent = new Intent();
        mGson = new Gson();
        db = x.getDb(DBUtils.getDb());
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("车辆扣押");
        seizeTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        seizeTime.setTime(new Date());
        seizeTime.setCyclic(false);
        seizeTime.setCancelable(true);
        seizeTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textSeizeTime.setText(Utils.setDate(date));
            }
        });

        groupIsTraffic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbTraffic.getId()) {
                    isTraffic = "1";
                    textSeizeUnit.setClickable(true);
                    textSeizeUnit.setText("");
                } else {
                    isTraffic = "0";
                    textSeizeUnit.setClickable(false);
                    textSeizeUnit.setText((String) SharedPreferencesUtils.get("regionName", ""));
                }
            }
        });

        textReceivedUnit.setText((String) SharedPreferencesUtils.get("regionName", ""));

        editConcernedIdentity.setTransformationMethod(new AllCapTransformationMethod(true));

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
    }

    private void initData() {
        try {
            trafficModels = db.findAll(TrafficModel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(trafficModels==null){
            trafficModels=new ArrayList<TrafficModel>();
        }
        if (trafficModels.equals("") || trafficModels.size() == 0 || trafficModels == null) {
            mProgressHUD.show();
            HashMap<String, String> map = new HashMap<>();
            map.put("unitno", "");
            WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETTRAFFICLIST, map, new WebServiceUtils.WebServiceCallBack() {
                @Override
                public void callBack(String result) {
                    if (result != null) {
                        Utils.LOGE("Pan","交警列表"+result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                trafficModels = mGson.fromJson(data, new TypeToken<List<TrafficModel>>() {
                                }.getType());
                                if (trafficModels != null && trafficModels.size() > 0) {
                                    db.delete(TrafficModel.class);
                                    int max = trafficModels.size();
                                    for (int i = 0; i < max; i++) {
                                        db.save(trafficModels.get(i));
                                    }
                                }
                                mProgressHUD.dismiss();
                            } else if (errorCode == 1) {
                                mProgressHUD.dismiss();
                                SharedPreferencesUtils.put("token","");
                                ActivityUtil.goActivityAndFinish(VehiclesSeizeActivity.this, LoginActivity.class);
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
        } else {
//            trafficModelList = db.findAllByWhere(TrafficModel.class, " pValue like \'%" + SharedPreferencesUtils.get("regionNo", "") + "%\'");
            try {
                trafficModelList= db.selector(TrafficModel.class).where("pValue", "LIKE", "%"+SharedPreferencesUtils.get("regionNo", "")+"%").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }

            if(trafficModelList==null){
                trafficModelList=new ArrayList<TrafficModel>();
            }
        }

    }

    @OnClick({R.id.image_back, R.id.text_vehicleBrand, R.id.text_seizeTime, R.id.text_vehicleColor, R.id.text_seizeUnit, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_seizeTime:
                seizeTime.show();
                break;
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.text_vehicleBrand:
                intent.setClass(VehiclesSeizeActivity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.text_vehicleColor:
                dialogShow(1, "");
                break;
            case R.id.text_seizeUnit:
//                trafficModelList = db.findAllByWhere(TrafficModel.class, " pValue like \'%" + SharedPreferencesUtils.get("regionNo", "") + "%\'");
                try {
                    trafficModelList= db.selector(TrafficModel.class).where("pValue", "LIKE", SharedPreferencesUtils.get("regionNo", "")+"%").findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if(trafficModelList==null){
                    trafficModelList=new ArrayList<TrafficModel>();
                }
                if (trafficModelList.size() == 0) {
                    Utils.myToast(mContext, "该辖区无交警大队");
                } else {
                    dialogShow(2, "");
                }
                break;
            case R.id.btn_submit:
                if (!checkData()) {
                    break;
                }
                dialogShow(3, "确认对该车辆进行扣押？");
                break;
        }
    }

    private void sendMsg() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("LISTID", UUID.randomUUID().toString().toUpperCase());
            jsonObject.put("PHOTO1File", "");
            jsonObject.put("PHOTO2File", "");
            jsonObject.put("DISTRAINNO", "");
            jsonObject.put("OWNERNAME", editConcernedName.getText().toString().trim());
            jsonObject.put("PHONE", editConcernedPhone.getText().toString().trim());
            jsonObject.put("IDENTITYCARD", editConcernedIdentity.getText().toString().trim());
            jsonObject.put("DECKPLATENUMBER", "");
            jsonObject.put("VEHICLEBRAND", mBrandNameId);
            jsonObject.put("COLORID", mColorId);
            jsonObject.put("ENGINENO", editMotor.getText().toString().trim());
            jsonObject.put("SHELVESNO", editFrame.getText().toString().trim());
            jsonObject.put("ISTRAFFIC", isTraffic);
            jsonObject.put("DISTRAINUNIT", textSeizeUnit.getText().toString().trim());
            jsonObject.put("DISTRAINUNITID", mTrafficId);
            jsonObject.put("HANDLEUNITID", SharedPreferencesUtils.get("regionNo", ""));
            jsonObject.put("DISTRAINTIME", textSeizeTime.getText().toString().trim());
            jsonObject.put("Remark", editRemarks.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_ADDDISTRAINCAR, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, "车辆扣押成功");
                            dialogShow(0, "车辆扣押成功，扣押单号为：\n" + data);
                            //onBackPressed();
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            ActivityUtil.goActivityAndFinish(VehiclesSeizeActivity.this, LoginActivity.class);
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

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String msg) {
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
                colorList =db.selector(BikeCode.class).where("type","=","4").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(colorList==null){
                colorList=new ArrayList<BikeCode>();
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
                    textVehicleColor.setText(mColor);
                    System.out.println("----------------选中颜色:"
                            + mColorId);
                }
            }).show();
        } else if (flag == 2) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View trafficView = mInflater.inflate(R.layout.layout_traffic, null);
            ListView listTraffic = (ListView) trafficView.findViewById(R.id.list_traffic);
            listTraffic.setOnItemClickListener(this);
            trafficsList.clear();
            trafficsAdapter = new ColorAdapter(mContext, trafficsList,
                    trafficsMap, R.layout.brand_item,
                    new String[]{"traffics_name"},
                    new int[]{R.id.text_brandname});
            listTraffic.setAdapter(trafficsAdapter);

            for (int i = 0; i < trafficModelList.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(trafficModelList.get(i).getValue());
                sortModel.setName(trafficModelList.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(trafficModelList.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                trafficsList.add(sortModel);
            }
            trafficsAdapter.notifyDataSetChanged();
            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(trafficView, mContext);
            dialogBuilder.withTitle("辖区交警列表").withTitleColor("#333333").withMessage(null).
                    withEffect(effectstype)
                    .withButton1Text("取消").withButton2Text("选择")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    textSeizeUnit.setText(mTraffic);
                    selectedTraffic = mTrafficId;
                    System.out.println("----------------选中交警大队为:"
                            + selectedTraffic);
                }
            }).show();
        } else if (flag == 3) {
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
                    sendMsg();
                }
            }).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                textVehicleBrand.setText(brandName);
                String brandCode = data.getStringExtra("brandCode");
                mBrandNameId = brandCode;
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
            case R.id.list_traffic:
                trafficsMap.clear();
                trafficsMap.put(position, 100);
                trafficsAdapter.notifyDataSetChanged();
                mTraffic = trafficsList.get(position).getName();
                mTrafficId = trafficsList.get(position).getGuid();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        dialogShow(0, "信息编辑中，确认离开该页面？");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private boolean checkData() {
        String concernedName = editConcernedName.getText().toString().trim();
        if (concernedName.equals("")) {
            Utils.myToast(mContext, "请输入当事人姓名");
            return false;
        }
        String concernedPhone = editConcernedPhone.getText().toString().trim();
        if (concernedPhone.equals("")) {
            Utils.myToast(mContext, "请输入当事人联系方式");
            return false;
        }
        String vehicleBrand = textVehicleBrand.getText().toString().trim();
        if (vehicleBrand.equals("")) {
            Utils.myToast(mContext, "请选择车辆品牌");
            return false;
        }
        String vehicleColor = textVehicleColor.getText().toString().trim();
        if (vehicleColor.equals("")) {
            Utils.myToast(mContext, "请选择车辆颜色");
            return false;
        }
        String frame = editFrame.getText().toString().trim();
        if (frame.equals("")) {
            Utils.myToast(mContext, "请输入车架号");
            return false;
        }
        String motor = editMotor.getText().toString().trim();
        if (motor.equals("")) {
            Utils.myToast(mContext, "请输入电机号");
            return false;
        }
        String seizeUnit = textSeizeUnit.getText().toString().trim();
        if (seizeUnit.equals("")) {
            Utils.myToast(mContext, "请选择扣押单位");
            return false;
        }
        String seizeTime = textSeizeTime.getText().toString().trim();
        if (seizeTime.equals("")) {
            Utils.myToast(mContext, "请选择扣押时间");
            return false;
        }
        return true;
    }

}

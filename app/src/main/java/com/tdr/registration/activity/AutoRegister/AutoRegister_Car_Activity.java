package com.tdr.registration.activity.AutoRegister;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.adapter.PhotoListAdapter;

import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.RegisterUtil;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
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
 * 备案登记动态排序_车辆信息
 */
@ContentView(R.layout.activity_auto_register_car)
public class AutoRegister_Car_Activity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.image_back)
    ImageView image_back;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.image_scan)
    ImageView imageScan;
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
    @ViewInject(R.id.tv_lable)
    TextView TV_lable;
    @ViewInject(R.id.tv_theftNo)
    TextView TV_theftNo;
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
    EditText ET_frame;
    @ViewInject(R.id.IV_ScanFrameNumber)
    ImageView IV_ScanFrameNumber;

    /**
     * 电机号
     */
    @ViewInject(R.id.ll_MotorNumber)
    LinearLayout LL_MotorNumber;
    @ViewInject(R.id.et_engineNo)
    EditText ET_motor;
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


    private RegisterUtil RU;
    private Activity mActivity;

    private List<PhotoListInfo> PLI;
    private PhotoListAdapter PLA;
    private boolean isManualInputPlate = true;
    private TimePickerView timePickerView;
    private boolean CheckTime = false;
    private List<PhotoListAdapter.DrawableList> DrawableList;

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap = new HashMap<Integer, Integer>();
    private List<BikeCode> colorList = new ArrayList<BikeCode>();
    private String mColor1 = "";// 颜色1
    private String mColorId1 = "";// 颜色1的ID
    private String mColor2 = "";// 颜色2
    private String mColorId2 = "";// 颜色2的ID
    private String ScanType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("【当前Activity】", "AutoRegister_Car_Activity" );
        x.view().inject(this);
        RU = new RegisterUtil(this);
        RU.BA.getACList().add(this);
        initview();
        initData();
    }

    private void initview() {
        imageScan.setVisibility(View.VISIBLE);
        imageScan.setBackgroundResource(R.mipmap.register_pop);
        image_back.setOnClickListener(this);
        imageScan.setOnClickListener(this);
        TV_buyTime.setOnClickListener(this);
        RL_CarBrand.setOnClickListener(this);
        RL_vehicleColor.setOnClickListener(this);
        RL_vehicleColor2.setOnClickListener(this);
        IV_scanTheft.setOnClickListener(this);
        IV_scanTheft2.setOnClickListener(this);
        IV_scanPlate.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        IV_ScanFrameNumber.setOnClickListener(this);
        IV_ScanMotorNumber.setOnClickListener(this);
        mActivity = this;
        SetPhotoList();
        if (RU.BA.getTitleType().equals("")) {
            if (!RU.REGISTRATION.equals("")) {
                textTitle.setText(RU.REGISTRATION);
            } else {
                if (RU.city.contains("温州")) {
                    textTitle.setText("登记备案");
                } else {
                    if (!RU.appName.equals("") && RU.appName.contains("备案登记")) {
                        textTitle.setText("备案登记");
                    } else if (!RU.appName.equals("") && RU.appName.contains("防盗登记")) {
                        textTitle.setText("防盗登记");
                    } else {
                        textTitle.setText("防盗登记");
                    }
                }
            }
        } else if (RU.BA.getTitleType().equals("seizure")) {
            textTitle.setText("扣押转正式");
        }
        SetViewForCity();
        imageScan.setVisibility(View.GONE);
        imageScan.setBackgroundResource(R.mipmap.qr_scan);

        ET_plateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        if (RU.IsScanCjh.equals("1")) {
            IV_ScanFrameNumber.setVisibility(View.VISIBLE);
        } else {
            IV_ScanFrameNumber.setVisibility(View.GONE);
        }
        if (RU.IsScanDjh.equals("1")) {
            IV_ScanMotorNumber.setVisibility(View.VISIBLE);
        } else {
            IV_ScanMotorNumber.setVisibility(View.GONE);
        }
        ET_frame.setTransformationMethod(new AllCapTransformationMethod(true));
        ET_motor.setTransformationMethod(new AllCapTransformationMethod(true));

        RG_car_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == RB_new_car.getId()) {//新销售 0 ,已上路 1
                    RU.BA.getRD().setCARTYPE("0");
                } else {
                    RU.BA.getRD().setCARTYPE("1");
                }
            }
        });


        RB_formalPlate.setChecked(true);

        RG_plateType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == RB_formalPlate.getId()) {
                    RU.BA.getRD().setPlateType("1");
                } else {
                    RU.BA.getRD().setPlateType("0");
                }
            }
        });
        if (!RU.BA.getRD().getBuyDate().equals("")) {
            TV_buyTime.setText(RU.BA.getRD().getBuyDate());
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

    /**
     * 根据不同城市设置不同的界面
     */
    private void SetViewForCity() {
//        南宁 柳州 天津 许昌

        if (RU.isScanCard.equals("1")) {
            btn_next.setText("扫码确认车牌");
        } else {
            btn_next.setText("确认车牌");
        }
        TV_buyTime.setHint("");
        TV_buyTime.setText("2015-01-01");

        RU.BA.getRD().setCARTYPE("1");
        LL_plateType.setVisibility(View.GONE);
        RL_vehicleColor2.setVisibility(View.GONE);
        LL_tagId.setVisibility(View.GONE);

        mLog.e(RU.isScanLabel.equals("1") ? "启用扫标签：" + RU.isScanLabel : "禁用扫标签：" + RU.isScanLabel);
        if (RU.isScanLabel.equals("1")) {
            RL_scanTheft.setVisibility(View.VISIBLE);
            if (RU.ISDOUBLESIGN.equals("1")) {
                RL_scanTheft2.setVisibility(View.VISIBLE);
            } else {
                RL_scanTheft2.setVisibility(View.GONE);
            }
        } else if (RU.isScanLabel.equals("0")) {
            RL_scanTheft.setVisibility(View.GONE);
            RL_scanTheft2.setVisibility(View.GONE);
        }

        if (RU.city.contains("天津")) {
            isManualInputPlate = false;
            textTitle.setText("防盗登记");
            LL_plateType.setVisibility(View.VISIBLE);
            ET_plateNumber.setHint("请输入电动自行车车牌");
        } else if (RU.city.contains("昆明")) {
            isManualInputPlate = false;
            LL_CarType.setVisibility(View.GONE);
            TV_lable.setText("防盗号");
            TV_lable2.setText("防盗号2");
            RU.BA.getRD().setCARTYPE("0");
        } else if (RU.city.contains("南宁")) {
            LL_CarType.setVisibility(View.GONE);
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗号");
            TV_theftNo.setHint("请扫码确认防盗号");
            TV_theftNo2.setHint("请扫码确认防盗号");
            RU.BA.getRD().setCARTYPE("0");
        } else if (RU.city.contains("柳州") || RU.city.contains("防城港")) {
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗装置");
            TV_lable2.setText("防盗装置2");
            TV_theftNo.setHint("请扫描防盗装置");
            TV_theftNo2.setHint("请扫描防盗装置");
        } else if (RU.city.contains("六盘水")) {
            RL_vehicleColor2.setVisibility(View.VISIBLE);
            TV_lable.setText("防盗号");
            TV_lable2.setText("防盗号2");
        } else if (RU.city.contains("丽水")) {
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

    /**
     * 加载图片列表
     */
    private void SetPhotoList() {
        try {
            PLI = RU.getPhotoList();
        } catch (DbException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void initData() {


        if (RU.BA.getRD().getCARTYPE().equals("0")) {
            RB_new_car.setChecked(true);
        } else if (RU.BA.getRD().getCARTYPE().equals("1")) {
            RB_old_car.setChecked(true);
        }


        if (RU.BA.getRD().getPlateType().equals("1")) {
            RB_formalPlate.setChecked(true);
        } else if (RU.BA.getRD().getPlateType().equals("0")) {
            RB_temporaryPlate.setChecked(true);
        }

        TV_vehicleBrand.setText(RU.BA.getRD().getVehiclebrandName());
        TV_vehicleColor.setText(RU.BA.getRD().getColor1Name());
        TV_vehicleColor2.setText(RU.BA.getRD().getColor2Name());
        if (isManualInputPlate) {
            ET_plateNumber.setText(RU.BA.getRD().getPlateNumber());
        } else {
            TV_Plate.setText(RU.BA.getRD().getPlateNumber());
        }
        TV_theftNo.setText(RU.BA.getRD().getTHEFTNO());
        TV_theftNo2.setText(RU.BA.getRD().getTHEFTNO2());

        TV_tagID.setText(RU.BA.getRD().getTHEFTNO());
        ET_frame.setText(RU.BA.getRD().getShelvesNo());
        ET_motor.setText(RU.BA.getRD().getEngineNo());

        if (!RU.BA.getRD().getBuyDate().equals("")) {
            TV_buyTime.setText(RU.BA.getRD().getBuyDate());
        }
        Utils.CheckBuyTime(TV_buyTime.getText().toString(), new Utils.GetServerTime() {
            @Override
            public void ServerTime(String st, boolean Check) {
                CheckTime = Check;
            }
        });
        repairList();
    }

    private void repairList() {
        DrawableList = new ArrayList<PhotoListAdapter.DrawableList>();
        List<PhotoModel> photolist = new ArrayList<PhotoModel>();
        for (int i = 0; i < PLI.size(); i++) {
            for (int j = 0; j < RU.getPhotoFile().size(); j++) {
                if (PLI.get(i).getINDEX().equals(RU.getPhotoFile().get(j).getINDEX())) {
                    photolist.add(RU.getPhotoFile().get(j));
                }
            }
        }
        mLog.e("Pan", "photolist.size=" + photolist.size());
        for (PhotoModel PM : photolist) {
            mLog.e("Pan", "getINDEX=" + PM.getINDEX());
            initImages(PM.getINDEX(), PM.getPhoto());
        }
    }

    private void initImages(final String index, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pictureGUID", id);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPICTURE, map, new WebServiceUtils.WebServiceCallBack() {
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


    private void dialogShow(int flag, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors1.setOnItemClickListener(this);
            colorsList.clear();
            colorsAdapter = new ColorAdapter(mActivity, colorsList,
                    colorsMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(colorsAdapter);
            try {
                colorList = RU.db.selector(BikeCode.class).where("type", "=", "4").findAll();
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
                String pinyin = RU.characterParser.getSelling(colorList.get(i)
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
                    TV_vehicleColor.setText(mColor1);
                    RU.BA.getRD().setColorId(mColorId1);


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
            colorsAdapter = new ColorAdapter(mActivity, colorsList,
                    colorsMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors2.setAdapter(colorsAdapter);
//            colorList = db.findAllByWhere(BikeCode.class, " type='4'");
            try {
                colorList = RU.db.selector(BikeCode.class).where("type", "=", "4").findAll();
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
                String pinyin = RU.characterParser.getSelling(colorList.get(i)
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
                    TV_vehicleColor2.setText(mColor2);
                    RU.BA.getRD().setColorId2(mColorId2);
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
            dialogBuilder.setCustomView(identityView, mActivity);
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

            RU.ActivityFinish_Car();
        } else {
            Utils.myToast(mActivity, "请确认车牌无误！");
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
            regular = RU.REGULAR;
        } else {
            msg1 = "请输入" + TV_lable2.getText() + "号";
            msg2 = "输入的" + TV_lable2.getText() + "号格式错误";
            regular = RU.REGULAR2;
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

    private boolean checkData() {
        for (int i = 0; i < PLI.size(); i++) {
            if (!PLA.checkItemDate(i)) {
                Utils.showToast("请拍摄" + PLI.get(i).getREMARK());
                return false;
            }
        }
        if (!RU.city.contains("南宁") || !RU.city.contains("昆明")) {
            if (RU.BA.getRD().getCARTYPE().equals("")) {
                Utils.showToast("请选择车辆类型");
                return false;
            }
        }
//        if (isConfirm.equals("")) {
//            Utils.myToast(mContext, "请选择是否有来历承诺书");
//            return false;
//        }
        if (RU.city.contains("天津")) {
            if (RU.BA.getRD().getPlateType().equals("")) {
                Utils.showToast("请选择车牌类型");
                return false;
            }
        }
        if (RU.BA.getRD().getVehicleBrand().equals("") || RU.BA.getRD().getVehicleBrand() == null) {
            Utils.showToast("请选择车辆品牌");
            return false;
        }
        String plateNum = "";
        if (isManualInputPlate) {
            plateNum = ET_plateNumber.getText().toString().toUpperCase().trim();
        } else {
            plateNum = TV_Plate.getText().toString().toUpperCase().trim();
        }

        if (plateNum.equals("") || plateNum == null) {
            Utils.showToast("请输入车牌");
            return false;
        }

        mLog.e("RU.CarRegular="+RU.CarRegular);
        Pattern pattern = Pattern.compile(RU.CarRegular);
        Matcher matcher = pattern.matcher(plateNum + "");
        if (!matcher.matches()) {
            Utils.showToast("输入的车牌有误，请重新确认");
            return false;
        }

        if (RU.isScanLabel.equals("1")) {
            String theftNo = TV_theftNo.getText().toString().trim();
            if (theftNo.equals("")) {
                Utils.showToast("请输入" + TV_lable.getText().toString().trim());
                return false;
            }
            if (RU.ISDOUBLESIGN.equals("1")) {
                String theftNo2 = TV_theftNo2.getText().toString().trim();
                if (theftNo2.equals("")) {
                    Utils.showToast("请输入" + TV_lable2.getText().toString().trim());
                    return false;
                }
            }
        }

        String frame = ET_frame.getText().toString().toUpperCase().trim();
        if (frame.equals("") || frame == null) {
            Utils.showToast("请输入车架号");
            return false;
        }
        String motor = ET_motor.getText().toString().toUpperCase().trim();
        if (motor.equals("") || motor == null) {
            Utils.showToast("请输入电机号");
            return false;
        }
        if (RU.city.contains("昆明")) {
            if (!Utils.check_FrameOrMotor(frame) && !Utils.check_FrameOrMotor(motor)) {
                Utils.showToast("电机号与车架号必须正确录入其中一个");
                return false;
            }
        }
        if (RU.BA.getRD().getColorId().equals("") || RU.BA.getRD().getColorId() == null) {
            Utils.showToast("请选择电动车颜色");
            return false;
        }
        String buyTime = TV_buyTime.getText().toString();
        if (buyTime.equals("") || buyTime == null) {
            Utils.showToast("请选择车辆购买时间");
            return false;
        }
        if (!CheckTime) {
            if (Utils.ServerTime == null || Utils.ServerTime.equals("")) {
                Utils.showToast("获取服务器时间异常。");
            } else {
                Utils.showToast("您选择的时间已超过当前时间");
            }
            return false;
        }


        RU.BA.getRD().setPlateNumber(plateNum);
        RU.BA.getRD().setISCONFIRM(RU.IsConfirm);
        RU.BA.getRD().setEngineNo(frame);
        RU.BA.getRD().setShelvesNo(frame);
        RU.BA.getRD().setEngineNo(motor);
        RU.BA.getRD().setBuyDate(buyTime);

        return true;
    }

    private void UpDatePhotoItem() {
        Bitmap bitmap = PhotoUtils.getPhotoBitmap();

        String Photoindex[] = PhotoUtils.mPicName.split(":");
        Drawable drawable = new BitmapDrawable(bitmap);

        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList.findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
        my.Photo.setBackgroundDrawable(drawable);
        my.PhotoName.setTextColor(Color.WHITE);
        PLA.SevePhoto(Photoindex[1]);


    }

    private void UpDatePhotoItem2(Intent data) {
        Bitmap bitmap = PhotoUtils.getPhotoBitmap(data);
        String Photoindex[] = PhotoUtils.mPicName.split(":");
        Drawable drawable = new BitmapDrawable(bitmap);

        PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList.findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
        my.Photo.setBackgroundDrawable(drawable);
        my.PhotoName.setTextColor(Color.WHITE);
        PhotoUtils.sevephoto(bitmap);
        PLA.SevePhoto(Photoindex[1]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                RU.BackPressed();
                break;
            case R.id.image_scan:
                startActivityForResult(new Intent(AutoRegister_Car_Activity.this, QRCodeScanActivity.class), RU.SCANNIN_GREQUEST_CODE);
                break;
            case R.id.tv_buyTime:
                timePickerView.show();
                break;
            case R.id.rl_CarBrand:
                startActivityForResult(new Intent(AutoRegister_Car_Activity.this, BrandActivity.class), RU.BRAND_CODE);
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
                RU.Scan(0, true, false, "请输入二维码");
                break;
            case R.id.iv_scanTheft2:
                ScanType = "ScanTheft2";
                RU.Scan(0, true, false, "请输入二维码");
                break;
            case R.id.iv_scanPlate:
                ScanType = "ScanPlate";
                RU.Scan(0, true, true, "请输入车牌号");
                break;
            case R.id.IV_ScanFrameNumber:
                ScanType = "ScanFrame";
                RU.Scan(1, false, false, "请输入车架号");
                break;
            case R.id.IV_ScanMotorNumber:
                ScanType = "ScanMotor";
                RU.Scan(1, false, false, "请输入电机号");
                break;

            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                if (RU.isScanCard.equals("1")) {
                    ScanType = "ScanPlate";
                    RU.CheckPlate("请输入车牌号");
                } else {
                    dialogShow(3, "确认车牌号");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
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
                    MobclickAgent.reportError(mActivity, "临时缓存图片AbsolutePath：" + PhotoUtils.imageFile.getAbsolutePath());
                    MobclickAgent.reportError(mActivity, "UpDatePhotoItem" + E.toString());
                }
            }
        } else if (requestCode == RU.BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandCode = data.getStringExtra("brandCode");
                String brandName = data.getStringExtra("brandName");
                TV_vehicleBrand.setText(brandName);
                RU.BA.getRD().setVehicleBrand(brandCode);
                RU.BA.getRD().setVehiclebrandName(brandName);
            }
        } else if (requestCode == RU.SCANNIN_GREQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.showToast("没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    String isPlateNumber = bundle.getString("isPlateNumber");

                    if (isPlateNumber.equals("0")) {
                        if (scanResult.equals(RU.BA.getRD().getPlateNumber())) {
//                            RU.SendMSG();
                            RU.ActivityFinish_Car();
                        } else {
                            Utils.showToast("输入的车牌有误，请重新确认");
                            return;
                        }
                    } else {
                        String plateNumberRead = RU.mQR.plateNumber(scanResult);
                        if (plateNumberRead.equals(RU.BA.getRD().getPlateNumber())) {
//                            RU.SendMSG();
                            RU.ActivityFinish_Car();
                        } else if (plateNumberRead.equals("-1")) {
                            Utils.showToast("校验不通过，请确认车牌合法正确性");

                            return;
                        } else {
                            Utils.showToast("输入的车牌有误，请重新确认");
                            return;
                        }
                    }
                }
            }
        } else if (requestCode == RU.SCANNIN_QR_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.showToast("没有扫描到二维码");
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
                            num = RU.mQR.plateNumber(labelNumber);
                        }
                        if (!num.equals("-1")) {
                            if (isManualInputPlate) {
                                ET_plateNumber.setText(num);
                            } else {
                                TV_Plate.setText(num);
                            }
                        } else {
                            Utils.showToast("二维码不属于车牌");
                        }
                    } else if (ScanType.equals("ScanTheft1")) {
                        mLog.e("labelNumber" + labelNumber);
                        if (checkTheft(1, labelNumber)) {
                            TV_theftNo.setText(labelNumber);
                            RU.BA.getRD().setTHEFTNO(labelNumber);
                        }
                    } else if (ScanType.equals("ScanTheft2")) {
                        if (checkTheft(2, labelNumber)) {
                            TV_theftNo2.setText(labelNumber);
                            RU.BA.getRD().setTHEFTNO2(labelNumber);
                        }
                    } else if (ScanType.equals("ScanFrame")) {
                        ET_frame.setText(labelNumber);
                    } else if (ScanType.equals("ScanMotor")) {
                        ET_motor.setText(labelNumber);
                    }
                }
            }
        } else if (requestCode == RU.CONFIRMATION_INSURANCE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String isChecked = bundle.getString("isChecked");
                if (isChecked.equals("1")) {
//                    RU.sendMsg();
                    RU.ActivityFinish_Car();
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

    @Override
    public void onBackPressed() {
        RU.BackPressed();
        return;
    }
}

package com.tdr.registration.activity.AutoRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.PreListActivity;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.activity.RegisterSecondActivity;
import com.tdr.registration.activity.RegisterThirdActivity;
import com.tdr.registration.activity.SelectBleActivity;
import com.tdr.registration.activity.normal.RegisterFirstNormalActivity2;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.DX_PreRegistrationModel;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.PreModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RegisterUtil;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.RegistrPop;

import org.apache.commons.lang.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 备案登记动态排序_人员信息
 */
@ContentView(R.layout.activity_register_second)
public class AutoRegister_Personnel_Activity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, RegistrPop.OnRegistrPopClickListener {


    @ViewInject(R.id.image_back)
    ImageView imageBack;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.image_scan)
    ImageView imageScan;
    @ViewInject(R.id.linear_identity)
    LinearLayout linearIdentity;
    @ViewInject(R.id.relative_identity)
    ImageView relativeIdentity;
    @ViewInject(R.id.text_identity)
    TextView textIdentity;
    @ViewInject(R.id.text_currentAddressStart)
    TextView textCurrentAddressStart;
    @ViewInject(R.id.edit_ownerName)
    EditText editOwnerName;
    @ViewInject(R.id.edit_ownerIdentity)
    EditText editOwnerIdentity;
    @ViewInject(R.id.edit_ownerPhone1)
    EditText editOwnerPhone1;
    @ViewInject(R.id.edit_ownerPhone2)
    EditText editOwnerPhone2;
    @ViewInject(R.id.edit_ownerCurrentAddress)
    EditText editOwnerCurrentAddress;
    @ViewInject(R.id.edit_remarks)
    EditText editRemarks;
    @ViewInject(R.id.btn_next)
    Button btnNext;
    @ViewInject(R.id.text_cardType)
    TextView textCardType;
    @ViewInject(R.id.relative_commission)
    RelativeLayout relativeCommission;
    @ViewInject(R.id.check_commission)
    CheckBox checkCommission;
    @ViewInject(R.id.text_commissionCardType)
    TextView textCommissionCardType;
    @ViewInject(R.id.edit_commissionName)
    EditText editCommissionName;
    @ViewInject(R.id.edit_commissionIdentity)
    EditText editCommissionIdentity;
    @ViewInject(R.id.edit_commissionPhone1)
    EditText editCommissionPhone1;
    @ViewInject(R.id.linear_commission)
    LinearLayout linearCommission;
    @ViewInject(R.id.LL_Agent)
    LinearLayout LL_Agent;

    @ViewInject(R.id.edit_AGENTADDR)
    EditText edit_AGENTADDR;
    @ViewInject(R.id.edit_agentremark)
    EditText edit_agentremark;


    @ViewInject(R.id.text_showOwnerPhone1)
    TextView textShowOwnerPhone1;

    private RegistrPop registrPop;
    private Activity mActivity;
    private RegisterUtil RU;
    private List<BikeCode> cardList;
    private int cardType = 0;
    private int cardType2 = 0;
    private String mCardType = "";
    private String mCardTypeId = "";
    private String mCardType2 = "";
    private String mCardTypeId2 = "";
    private ColorAdapter cardTypeAdapter;
    private ColorAdapter cardTypeAdapter2;
    private List<SortModel> cardTypeList = new ArrayList<SortModel>();
    private List<SortModel> cardTypeList2 = new ArrayList<SortModel>();
    private List<BikeCode> cardList2 = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> cardTypeMap2 = new HashMap<Integer, Integer>();
    private List<PreRegistrationModel> preRegistrationModels;
    private PreRegistrationModel preForKMModel = new PreRegistrationModel();
    private List<DX_PreRegistrationModel> PRList;
    private PreModel preModel = new PreModel();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mActivity = this;
        RU = new RegisterUtil(this);
        RU.BA.getACList().add(this);
        initView();
        initData();
    }

    private void initView() {
        imageBack.setOnClickListener(this);
        imageScan.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        textCardType.setOnClickListener(this);
        textCommissionCardType.setOnClickListener(this);
        relativeIdentity.setOnClickListener(this);
        if(RU.BA.getACList().size()==3){
            btnNext.setText("提交");
        }

        imageScan.setVisibility(View.VISIBLE);
        imageScan.setBackgroundResource(R.mipmap.register_pop);

        registrPop = new RegistrPop(imageScan, mActivity);
        registrPop.setOnRegistrPopClickListener(this);

        editOwnerIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
        if (RU.BA.getTitleType().equals("")) {
            if (!RU.REGISTRATION.equals("")) {
                textTitle.setText(RU.REGISTRATION);
            } else {
                if (RU.city.contains("温州")) {
                    textTitle.setText("登记备案");
                } else {
                    if (RU.city.contains("昆明") || RU.city.contains("天津")) {
                        textTitle.setText("防盗登记");
                    } else {
                        textTitle.setText("备案登记");
                    }
                }
            }
        } else {
            textTitle.setText("扣押转正式");
        }
        textCardType.setText("身份证");


        checkCommission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RU.BA.getRD().setIsommission("0");
                    linearCommission.setVisibility(View.VISIBLE);
                } else {
                    RU.BA.getRD().setIsommission("1");
                    linearCommission.setVisibility(View.GONE);
                }
            }
        });
        if (RU.city.contains("昆明")) {
            textIdentity.setText("持车证和证件");
            textCurrentAddressStart.setVisibility(View.GONE);

        }
        linearIdentity.setVisibility(View.GONE);
        if (RU.HasAgent.equals("1")) {
            LL_Agent.setVisibility(View.VISIBLE);
        } else {
            LL_Agent.setVisibility(View.GONE);
        }
        linearCommission.setVisibility(View.GONE);

    }

    private void initData() {
        mCardTypeId = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE);
        try {
            cardList = RU.db.selector(BikeCode.class).where("type", "=", "6").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (cardList == null) {
            cardList = new ArrayList<BikeCode>();
        }

        for (BikeCode bikeCode : cardList) {
            if (mCardTypeId.equals(bikeCode.getCode())) {
                textCardType.setText(bikeCode.getName());
            }
        }

        cardType = Integer.parseInt(RU.BA.getRD().getCARDTYPE()) - 1;
        cardTypeMap.clear();
        cardTypeMap.put(cardType, 100);
        editOwnerName.setText(RU.BA.getRD().getOwnerName());
        editOwnerIdentity.setText(RU.BA.getRD().getIdentity());
        editOwnerPhone1.setText(RU.BA.getRD().getPhone1());
        editOwnerPhone2.setText(RU.BA.getRD().getPhone2());
        editOwnerCurrentAddress.setText(RU.BA.getRD().getCurrentAddress());
        editRemarks.setText(RU.BA.getRD().getRemark());
        if (!RU.identity.equals("")) {
            Bitmap bitmap = Utils.stringtoBitmap(RU.identity);
            Drawable bd = new BitmapDrawable(bitmap);
            relativeIdentity.setBackground(bd);
            textIdentity.setTextColor(getResources().getColor(R.color.white));
        }

        if (RU.BA.getRD().getIsommission().equals("0")) {
            linearCommission.setVisibility(View.VISIBLE);
            mCardTypeId2 = RU.BA.getRD().getCommissionCardType();
            for (BikeCode bikeCode : cardList) {
                if (mCardTypeId2.equals(bikeCode.getCode())) {
                    textCommissionCardType.setText(bikeCode.getName());
                }
            }

            cardType2 = Integer.parseInt(mCardTypeId2) - 1;
            cardTypeMap2.clear();
            cardTypeMap2.put(cardType2, 100);
            editCommissionName.setText(RU.BA.getRD().getCommissionName());
            editCommissionIdentity.setText(RU.BA.getRD().getCommissionIdentity());
            editCommissionPhone1.setText(RU.BA.getRD().getCommissionPhone1());
            edit_AGENTADDR.setText(RU.BA.getRD().getCommissionAddress());
            edit_agentremark.setText(RU.BA.getRD().getCommissionRemark());
        }

    }
    private boolean checkData() {
        if (mCardTypeId.equals("")) {
            Utils.showToast( "请选择证件类型");
            return false;
        }
        String ownerName = editOwnerName.getText().toString().trim();
        if (ownerName.equals("")) {
            Utils.showToast(  "请输入车主姓名");
            return false;
        }
        String ownerIdentity = editOwnerIdentity.getText().toString().toUpperCase().trim();
        if (ownerIdentity.equals("")) {
            Utils.showToast(  "请输入车主证件号码");
            return false;
        }
        if (mCardTypeId.equals("1")) {//如果车主证件类型为身份证（1）
            if (!Utils.isIDCard18(ownerIdentity)) {
                Utils.showToast( "输入的身份证号码格式有误");
                return false;
            }
        }

        String phone1 = editOwnerPhone1.getText().toString().trim();
        if (phone1.equals("")) {
            Utils.showToast(  "请输入车主联系手机");
            return false;
        } else {
            if (!phone1.substring(0, 1).equals("1")) {
                Utils.showToast(  "请输入正确的手机号码");
                return false;
            }
            if (phone1.length() != 11) {
                Utils.showToast(  "输入的手机号码长度不符");
                return false;
            }
        }

        if (!RU.city.contains("昆明")) {
            String address = editOwnerCurrentAddress.getText().toString().trim();
            if (address.equals("")) {
                Utils.showToast( "请输入车主现住址");
                return false;
            }
        }

        if (RU.BA.getRD().getIsommission().equals("0")) {
            String commissionName = editCommissionName.getText().toString().trim();
            if (commissionName.equals("")) {
                Utils.showToast(  "请输入代办人姓名");
                return false;
            }
            String commissionCardType = textCommissionCardType.getText().toString();
            if (commissionCardType.equals("")) {
                Utils.showToast(  "请选择代办人证件类型");
                return false;
            }
            String commissionIdentity = editCommissionIdentity.getText().toString().toUpperCase().trim();
            if (commissionIdentity.equals("")) {
                Utils.showToast( "请输入代办人证件号码");
                return false;
            }
            if (this.mCardTypeId2.equals("1")) {
                if (!Utils.isIDCard18(commissionIdentity)) {
                    Utils.showToast( "输入的代办人身份证号码格式有误");
                    return false;
                }
            }
            String commissionPhone1 = editCommissionPhone1.getText().toString().trim();
            if (commissionPhone1.equals("")) {
                Utils.showToast(  "请输入代办人联系方式");
                return false;
            }
            String AGENTADDR = edit_AGENTADDR.getText().toString().trim();
            if (AGENTADDR.equals("")) {
                Utils.showToast(  "请输入代办人现住址");
                return false;
            }

        }
        RU.BA.getRD().setOwnerName(ownerName);
        RU.BA.getRD().setIdentity(ownerIdentity);
        RU.BA.getRD().setPhone1(phone1);
        RU.BA.getRD().setPhone2(editOwnerPhone2.getText().toString().trim());
        RU.BA.getRD().setCurrentAddress(editOwnerCurrentAddress.getText().toString());
        RU.BA.getRD().setRemark(editRemarks.getText().toString().trim());

        //=======代办人=======
        RU.BA.getRD().setCommissionCardType(mCardTypeId2);
        RU.BA.getRD().setCommissionName(editCommissionName.getText().toString().trim());
        RU.BA.getRD().setCommissionIdentity(editCommissionIdentity.getText().toString().trim());
        RU.BA.getRD().setCommissionPhone1(editCommissionPhone1.getText().toString().trim());
        RU.BA.getRD().setCommissionAddress(edit_AGENTADDR.getText().toString().trim());
        RU.BA.getRD().setCommissionRemark(edit_agentremark.getText().toString().trim());

        return true;
    }


    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, final String msg) {
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
            cardTypeList.clear();
            cardTypeAdapter = new ColorAdapter(mActivity, cardTypeList,
                    cardTypeMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(cardTypeAdapter);

            for (int i = 0; i < cardList.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(cardList.get(i).getCode());
                sortModel.setName(cardList.get(i).getName());
                // 汉字转换成拼音
                String pinyin = RU.characterParser.getSelling(cardList.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                cardTypeList.add(sortModel);
            }

            cardTypeAdapter.notifyDataSetChanged();

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mActivity);
            dialogBuilder.withTitle("证件类型").withTitleColor("#333333")
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
                    cardTypeAdapter.notifyDataSetChanged();
                    mCardType = cardTypeList.get(cardType).getName();
                    mCardTypeId = cardTypeList.get(cardType).getGuid();

                    dialogBuilder.dismiss();
                    textCardType.setText(mCardType);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, mCardTypeId);
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color2, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors2);
            list_colors1.setOnItemClickListener(this);
            cardTypeList2.clear();
            cardTypeAdapter2 = new ColorAdapter(mActivity, cardTypeList2,
                    cardTypeMap2, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(cardTypeAdapter2);
//            cardList2 = db.findAllByWhere(BikeCode.class, " type='6'");
            try {
                cardList2 = RU.db.selector(BikeCode.class).where("type", "=", "6").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (cardList2 == null) {
                cardList2 = new ArrayList<BikeCode>();
            }
            for (int i = 0; i < cardList2.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(cardList2.get(i).getCode());
                sortModel.setName(cardList2.get(i).getName());
                // 汉字转换成拼音
                String pinyin = RU.characterParser.getSelling(cardList2.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                cardTypeList2.add(sortModel);
            }

            cardTypeAdapter2.notifyDataSetChanged();

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mActivity);
            dialogBuilder.withTitle("证件类型").withTitleColor("#333333")
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
                    textCommissionCardType.setText(mCardType2);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COMMISSIONCARDTYPE, mCardTypeId2);
                }
            }).show();
        } else if (flag == 3) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View identityView = mInflater.inflate(R.layout.layout_query_identity, null);
            final EditText editQueryIdentity = (EditText) identityView
                    .findViewById(R.id.edit_queryIdentity);

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(identityView, mActivity);
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
        } else if (flag == 4) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("信息编辑中，确认离开该页面？")
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, mActivity).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
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
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    TransferUtil.remove("PhotoList");
                    if (RU.BA.getInType().equals("PreRegistration")) {
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(mActivity, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }).show();
        } else if (flag == 5) {
            showdialog(msg, "防盗号");
        } else if (flag == 6) {
            showdialog(msg, "车牌号");
        } else if (flag == 7) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("确定")
                    .setCustomView(R.layout.custom_view, mActivity).setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).show();
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
        dialogBuilder.setCustomView(identityView, mActivity);
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

    private void dealPreByPolice(PreRegistrationModel preRegistrationModel) {
        String vehicleType = Utils.initNullStr(preRegistrationModel.getVEHICLETYPE());
        if (!vehicleType.equals(RU.BA.getRD().getVEHICLETYPE())) {
            Utils.showToast( "预登记车辆类型与所选类型不符，请重新选择车辆类型登记");
            return;
        } else {
            SharedPreferencesUtils.put("preregistration", RU.mGson.toJson(preRegistrationModel.getPhotoListFile()));
            RU.BA.getRD().setREGISTERID(preRegistrationModel.getREGISTERID());
            RU.BA.getRD().setPlateNumber(preRegistrationModel.getPLATENUMBER());
            RU.BA.getRD().setVehiclebrandName(preRegistrationModel.getVEHICLEBRANDNAME());
            RU.BA.getRD().setVehicleBrand(preRegistrationModel.getVEHICLEBRAND());
            RU.BA.getRD().setColor1Name(preRegistrationModel.getColorName());
            RU.BA.getRD().setColorId(preRegistrationModel.getCOLORID());
            RU.BA.getRD().setColor2Name( preRegistrationModel.getCOLORNAME2());
            RU.BA.getRD().setColorId2(preRegistrationModel.getCOLORID2());
            RU.BA.getRD().setShelvesNo(preRegistrationModel.getSHELVESNO());
            RU.BA.getRD().setEngineNo(preRegistrationModel.getENGINENO());
            RU.BA.getRD().setBuyDate(Utils.dateWithoutTime(preRegistrationModel.getBUYDATE()));
            RU.BA.getRD().setCARDTYPE(preRegistrationModel.getCARDTYPE());
            RU.BA.getRD().setOwnerName(preRegistrationModel.getOWNERNAME());
            RU.BA.getRD().setIdentity(preRegistrationModel.getCARDID());
            RU.BA.getRD().setPhone1(preRegistrationModel.getPHONE1());
            RU.BA.getRD().setPhone2(preRegistrationModel.getPHONE2());
            RU.BA.getRD().setCurrentAddress(preRegistrationModel.getADDRESS());
            RU.BA.getRD().setRemark(preRegistrationModel.getREMARK());
            RU.BA.getRD().setVEHICLETYPE(vehicleType);
            initData();
        }
    }
    private void dealPreByPlateNumber(PreRegistrationModel preForKMModel) {
        String vehicleType = Utils.initNullStr(preForKMModel.getVEHICLETYPE());
        RU.BA.getRD().setREGISTERID(preForKMModel.getREGISTERID());
        RU.BA.getRD().setPlateNumber(preForKMModel.getPLATENUMBER());
        RU.BA.getRD().setVehiclebrandName(preForKMModel.getVEHICLEBRANDNAME());
        RU.BA.getRD().setVehicleBrand(preForKMModel.getVEHICLEBRAND());
        RU.BA.getRD().setColor1Name(preForKMModel.getColorName());
        RU.BA.getRD().setColorId(preForKMModel.getCOLORID());
        RU.BA.getRD().setColor2Name( preForKMModel.getCOLORNAME2());
        RU.BA.getRD().setColorId2(preForKMModel.getCOLORID2());
        RU.BA.getRD().setShelvesNo(preForKMModel.getSHELVESNO());
        RU.BA.getRD().setEngineNo(preForKMModel.getENGINENO());
        RU.BA.getRD().setBuyDate(Utils.dateWithoutTime(preForKMModel.getBUYDATE()));
        if (Utils.initNullStr(preForKMModel.getCARDTYPE()).equals("")) {
            RU.BA.getRD().setCARDTYPE("1");
        } else {
            RU.BA.getRD().setCARDTYPE(preForKMModel.getCARDTYPE());
        }
        RU.BA.getRD().setOwnerName(preForKMModel.getOWNERNAME());
        RU.BA.getRD().setIdentity(preForKMModel.getCARDID());
        RU.BA.getRD().setPhone1(preForKMModel.getPHONE1());
        RU.BA.getRD().setPhone2(preForKMModel.getPHONE2());
        RU.BA.getRD().setCurrentAddress(preForKMModel.getADDRESS());
        RU.BA.getRD().setRemark(preForKMModel.getREMARK());
        RU.BA.getRD().setVEHICLETYPE(vehicleType);

        mLog.e("照片列表：" + preForKMModel.getPhotoListFile().toString());

        initData();
    }
    private void dealModel(DX_PreRegistrationModel preModel) {
        TransferUtil.save("PhotoList", preModel.getPhotoListFile());
        RU.BA.getRD().setPlateNumber(preModel.getPLATENUMBER());
        RU.BA.getRD().setVEHICLETYPE(preModel.getVEHICLETYPE());
        RU.BA.getRD().setREGISTERID(preModel.getREGISTERID());
        RU.BA.getRD().setVehicleBrand( preModel.getVEHICLEBRAND());

        List<BikeCode> bikeCodes=null;
        try {
            bikeCodes = RU.db.selector(BikeCode.class).where("code", "=", preModel.getVEHICLEBRAND()).and("type", "=", "1").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodes == null) {
            bikeCodes = new ArrayList<BikeCode>();
        }
        RU.BA.getRD().setVehiclebrandName(bikeCodes.get(0).getName());
        RU.BA.getRD().setColorId(preModel.getCOLORID());
        List<BikeCode> bikeCodeList = null;
        try {
            bikeCodeList = RU.db.selector(BikeCode.class).where("code", "=", preModel.getCOLORID()).and("type", "=", "4").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodeList == null) {
            bikeCodeList = new ArrayList<BikeCode>();
        }
        RU.BA.getRD().setColor1Name(bikeCodeList.get(0).getName());
        RU.BA.getRD().setShelvesNo(preModel.getSHELVESNO());
        RU.BA.getRD().setEngineNo(preModel.getENGINENO());
        RU.BA.getRD().setCARDTYPE(preModel.getCARDTYPE());
        RU.BA.getRD().setOwnerName(preModel.getOWNERNAME());
        RU.BA.getRD().setIdentity(preModel.getCARDID());
        RU.BA.getRD().setPhone1(preModel.getPHONE1());
        RU.BA.getRD().setPhone2(preModel.getPHONE2());
        RU.BA.getRD().setCurrentAddress(preModel.getADDRESS());
        RU.BA.getRD().setBuyDate(preModel.getBUYDATE());
        RU.BA.getRD().setRemark(preModel.getREMARK());
        initData();
    }

    private void dealModel(PreModel preModel) {
        RU.BA.getRD().setREGISTERID(preModel.getPrerateID());
        RU.BA.getRD().setVehicleBrand(preModel.getVehiclebrand());
        List<BikeCode> bikeCodes = null;
        try {
            bikeCodes = RU.db.selector(BikeCode.class).where("code", "=", preModel.getVehiclebrand()).and("type", "=", "1").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodes == null) {
            bikeCodes = new ArrayList<BikeCode>();
        }
        RU.BA.getRD().setVehiclebrandName(bikeCodes.get(0).getName());
        RU.BA.getRD().setColorId(preModel.getColorID());
        List<BikeCode> bikeCodeList = null;
        try {
            bikeCodeList = RU.db.selector(BikeCode.class).where("code", "=", preModel.getColorID()).and("type", "=", "4").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (bikeCodeList == null) {
            bikeCodeList = new ArrayList<BikeCode>();
        }
        RU.BA.getRD().setColor1Name(bikeCodeList.get(0).getName());
        RU.BA.getRD().setShelvesNo(preModel.getShelvesno());
        RU.BA.getRD().setEngineNo(preModel.getEngineno());
        RU.BA.getRD().setCARDTYPE(preModel.getCardType());
        RU.BA.getRD().setOwnerName(preModel.getOwnerName());
        RU.BA.getRD().setIdentity(preModel.getCardid());
        RU.BA.getRD().setPhone1(preModel.getPhone1());
        RU.BA.getRD().setPhone2(preModel.getPhone2());
        RU.BA.getRD().setRemark(preModel.getRemark());
        initData();
    }

    /**
     * 预登记查询通过身份证
     *
     * @param queryIdentity
     */
    private void queryPreByIdentity(String queryIdentity) {
        RU.mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("cardid", queryIdentity);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPREREGISTERSBYCARDID, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e("证件号查询：" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            RU.mProgressHUD.dismiss();
                            preRegistrationModels = RU.mGson.fromJson(data, new TypeToken<List<PreRegistrationModel>>() {
                            }.getType());
                            if (preRegistrationModels.size() > 1) {
                                Bundle bundle = new Bundle();
                                ArrayList list = new ArrayList();
                                list.add(preRegistrationModels);
                                bundle.putParcelableArrayList("electricCarModelList", list);

                                ActivityUtil.goActivityForResultWithBundle(AutoRegister_Personnel_Activity.this, PreListActivity.class, bundle, RU.PRE_SHOW_CODE);
                            } else if (preRegistrationModels.size() == 1) {
                                dealPreByPolice(preRegistrationModels.get(0));
                            }
                        } else {
                            RU.mProgressHUD.dismiss();
                            Utils.showToast( data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        RU.mProgressHUD.dismiss();
                        Utils.showToast( "JSON解析出错");
                    }
                } else {
                    RU.mProgressHUD.dismiss();
                    Utils.showToast( "获取数据超时，请检查网络连接");
                }
            }
        });
    }
    private void queryPreByPlateNumber(String plateNumber) {
        RU.mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("plateNumber", plateNumber);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPREREGISTERS, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e("车牌号预登记" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            RU.mProgressHUD.dismiss();
                            Utils.showToast("查询成功，请继续操作");
                            preForKMModel = RU.mGson.fromJson(data, new TypeToken<PreRegistrationModel>() {
                            }.getType());
                            for (PhotoModel photoModel : preForKMModel.getPhotoListFile()) {
                                photoModel.setPhotoFile("");
                            }
                            dealPreByPlateNumber(preForKMModel);
                            SharedPreferencesUtils.put("preregisters", data);
                        } else {
                            RU.mProgressHUD.dismiss();
                            Utils.showToast( data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        RU.mProgressHUD.dismiss();
                        Utils.showToast( "JSON解析出错");
                    }
                } else {
                    RU.mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
    }
    /**
     * 获取电信预登记
     */
    private void query(String registerId) {
        RU.mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("plateNumber", "");
        map.put("cardid", "");
        map.put("phone", "");
        map.put("registerId", registerId.substring(7));
        mLog.e("Pan", "map=" + map);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPREREGISTERLIST, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Utils.LOGE("Pan", result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            RU.mProgressHUD.dismiss();
                            try {
                                PRList = RU.mGson.fromJson(data, new TypeToken<List<DX_PreRegistrationModel>>() {
                                }.getType());
                                if (PRList.get(0) != null) {
                                    if (VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE).equals(PRList.get(0).getVEHICLETYPE())) {
                                        dealModel(PRList.get(0));
                                    } else {
                                        dialogShow(7, "扫码获得的车辆类型与您选择的车辆类型不符，请退出登记重新选择车辆类型或者扫描相对应的车辆类型的二维码。");
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                Utils.showToast(data);
                            }

                        } else {
                            RU.mProgressHUD.dismiss();
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        RU.mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    RU.mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });

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
        RU.mProgressHUD.show();
        final HashMap<String, String> map = new HashMap<>();
        map.put("token", (String) SharedPreferencesUtils.get("token", ""));
        map.put("taskId", "");
        map.put("encryption", "");
        map.put("DataTypeCode", "GetPreRateOne");
        map.put("content", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CARDHOLDER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e(result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int resultCode = json.getInt("ResultCode");
                        String resultText = json.getString("ResultText");
                        if (resultCode == 0) {
                            RU.mProgressHUD.dismiss();
                            String content = json.getString("Content");
                            preModel = RU.mGson.fromJson(content, new TypeToken<PreModel>() {
                            }.getType());
                            String state = preModel.getState();
                            if (state.equals("0")) {
                                dealModel(preModel);
                            } else {
                                Utils.showToast( "该预登记车辆已被登记");
                            }
                        } else {
                            RU.mProgressHUD.dismiss();
                            Utils.showToast( resultText);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        RU.mProgressHUD.dismiss();
                        Utils.showToast( "JSON解析出错");
                    }
                } else {
                    RU.mProgressHUD.dismiss();
                    Utils.showToast( "获取数据超时，请检查网络连接");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                RU.BackPressed();
                break;
            case R.id.image_scan:
                registrPop.showPopupWindowDownOffset();
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                RU.ActivityFinish_Personnel();
                break;

            case R.id.text_cardType:
                dialogShow(0, "");
                break;

            case R.id.text_commissionCardType:
                dialogShow(1, "");
                break;

            case R.id.relative_identity:
                PhotoUtils.TakePicture(mActivity, "identity");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.CAMERA_REQESTCODE:
                if (resultCode == RESULT_OK) {
                    int degree = PhotoUtils.readPictureDegree();
                    Bitmap bitmap = PhotoUtils.rotaingImageView(degree,
                            PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile));
                    RU.identity = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
                    String path = PhotoUtils.imageFile.getPath();
                    String name = Utils.getFileName(path);
                    Drawable db = new BitmapDrawable(bitmap);
                    switch (name) {
                        case "identity":
                            relativeIdentity.setBackground(db);
                            SharedPreferencesUtils.put("identity", RU.identity);
                            textIdentity.setTextColor(getResources().getColor(R.color.white));
                            break;
                    }
                }
                break;
            case RegisterUtil.SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Utils.showToast( "没有扫描到二维码");
                        return;
                    } else {
                        RU.mProgressHUD.show();
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
                break;
            case RegisterUtil.PRE_SHOW_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Utils.showToast( "没有选择预登记车辆");
                        return;
                    } else {
                        Bundle bundle = data.getExtras();
                        PreRegistrationModel preRegistrationModel = (PreRegistrationModel) bundle.getSerializable("preModels");
                        dealPreByPolice(preRegistrationModel);
                    }
                }
                break;
            case RegisterUtil.SCANNIN_QR_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Utils.showToast( "没有扫描到二维码");
                        return;
                    } else {
                        Bundle bundle = data.getExtras();
                        String labelNumber = bundle.getString("result");
                        mLog.e("预登记SCANNIN_QR_CODE: " + labelNumber);
                        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.THEFTNO, labelNumber);
                    }
                }
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                cardType = position;
                cardTypeMap.clear();
                cardTypeMap.put(cardType, 100);
                cardTypeAdapter.notifyDataSetChanged();
                mCardType = cardTypeList.get(cardType).getName();
                mCardTypeId = cardTypeList.get(cardType).getGuid();
                break;
            case R.id.list_colors2:
                cardType2 = position;
                cardTypeMap2.clear();
                cardTypeMap2.put(cardType2, 100);
                cardTypeAdapter2.notifyDataSetChanged();
                mCardType2 = cardTypeList2.get(cardType2).getName();
                mCardTypeId2 = cardTypeList2.get(cardType2).getGuid();
                break;
            default:
                break;
        }
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
                ActivityUtil.goActivityForResultWithBundle(this, QRCodeScanActivity.class, bundle, RU.SCANNIN_GREQUEST_CODE);
                break;
            case 1:
                dialogShow(3, "预登记查询");
                break;
            case 2:
                dialogShow(5, "登记号查询");
                break;
            case 3:
                dialogShow(6, "车牌号预登记查询");
                break;

        }
        registrPop.dismiss();
    }
    @Override
    public void onBackPressed() {
        RU.BackPressed();
        return;
    }
}

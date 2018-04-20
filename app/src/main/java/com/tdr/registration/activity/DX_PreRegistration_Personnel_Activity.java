package com.tdr.registration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tdr.registration.util.VehiclesStorageUtils.clearData;

/**
 * 电信预登记人员信息
 */
@ContentView(R.layout.activity_dx_pre_registration_personnel)
public class DX_PreRegistration_Personnel_Activity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.IV_back)
    private ImageView IV_back;

    @ViewInject(R.id.ET_Name)
    private EditText ET_Name;

    @ViewInject(R.id.TV_CardType)
    private TextView TV_CardType;

    @ViewInject(R.id.ET_CardID)
    private EditText ET_CardID;

    @ViewInject(R.id.ET_Phone)
    private EditText ET_Phone;

    @ViewInject(R.id.ET_Phone2)
    private EditText ET_Phone2;

    @ViewInject(R.id.ET_Address)
    private EditText ET_Address;

    @ViewInject(R.id.ET_Remark)
    private EditText ET_Remark;

    @ViewInject(R.id.BT_Submit)
    private Button BT_Submit;


    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;
    private DbManager db;
    private ColorAdapter cardTypeAdapter;
    private List<SortModel> cardTypeList = new ArrayList<SortModel>();
    private List<BikeCode> cardList = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap = new HashMap<Integer, Integer>();
    private String mCardType = "";
    private String mCardTypeId = "";
    private CharacterParser characterParser;
    private Activity mActivity;
    private ZProgressHUD mProgressHUD;
    private ArrayList<PhotoListInfo> PLI;
    private int cardType = 0;
    private String ShowQR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initdate();
        setClick();
        getPrephotoConfig();
        setdate();
    }


    private void initdate() {
        mActivity = this;
        db = x.getDb(DBUtils.getDb());
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
        ShowQR = (String) SharedPreferencesUtils.get("ShowQR", "1");
        characterParser = CharacterParser.getInstance();//初始化汉子转拼音控件
        String CARDTYPE= VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE);
        if(CARDTYPE==null||CARDTYPE.equals("")){
            mCardTypeId ="1";
        }else{
            mCardTypeId =CARDTYPE;
        }

        try {
            cardList = db.selector(BikeCode.class).where("type", "=", "6").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (cardList == null) {
            cardList = new ArrayList<BikeCode>();
        }

        Log.e("Pan","cardType:"+CARDTYPE);
        for (BikeCode bikeCode : cardList) {
            if(CARDTYPE.equals(bikeCode.getCode())){
                TV_CardType.setText(bikeCode.getName());
            }
        }
        cardType = Integer.parseInt(mCardTypeId) - 1;
        cardTypeMap.clear();
        cardTypeMap.put(cardType, 100);
    }

    private void setdate(){
        ET_Name.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
        ET_CardID.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
        ET_Phone.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
        ET_Phone2.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE2));
        ET_Address.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS));
        ET_Remark.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REMARK));
    }
    private void setClick() {
        IV_back.setOnClickListener(this);
        TV_CardType.setOnClickListener(this);
        BT_Submit.setOnClickListener(this);
    }
    private void CardTypeDialogShow(int flag,String msg) {
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
                String pinyin = characterParser.getSelling(cardList.get(i)
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
                    dialogBuilder.dismiss();
                    mCardType = cardTypeList.get(cardType).getName();
                    mCardTypeId = cardTypeList.get(cardType).getGuid();
                    TV_CardType.setText(mCardType);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, mCardTypeId);
                }
            }).show();
        }else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
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
                    //清空图片缓存
                    clearData();
                    ActivityUtil.goActivityAndFinish(DX_PreRegistration_Personnel_Activity.this, HomeActivity.class);
                }
            }).show();
        }
    }
    private void getPrephotoConfig(){
        String city = (String) SharedPreferencesUtils.get("locCityName", "");
        List<BaseInfo> ResultList = null;
        PLI=new ArrayList<PhotoListInfo>();
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
            mLog.e("数据异常");
            e.printStackTrace();
        }
    }
    /**
     * 下一步
     */
    private void Submit() {
        String Name = ET_Name.getText().toString().trim();
        String CardID = ET_CardID.getText().toString().trim();
        String Phone = ET_Phone.getText().toString().trim();
        String Phone2 = ET_Phone2.getText().toString().trim();
        String Address = ET_Address.getText().toString().trim();
        String Remark = ET_Remark.getText().toString().trim();

        if (Name.equals("")) {
            Utils.showToast(ET_Name.getHint().toString());
            return;
        }
        if (mCardTypeId.equals("")) {
            Utils.showToast("请选择证件类型");
            return ;
        }
        if (CardID.equals("")) {
            Utils.showToast(ET_CardID.getHint().toString());
            return;
        }
        if (mCardTypeId.equals("1")) {//如果车主证件类型为身份证（1）
            if (!Utils.isIDCard18(CardID)) {
                Utils.showToast("输入的身份证号码格式有误");
                return ;
            }
        }
        if (Phone.equals("")) {
            Utils.showToast(ET_Phone.getHint().toString());
            return;
        }
        if (Phone.equals("")) {
            Utils.showToast( "请输入车主联系手机");
            return ;
        } else {
            if (!Phone.substring(0, 1).equals("1")) {
                Utils.showToast( "请输入正确的手机号码");
                return ;
            }
            if (Phone.length() != 11) {
                Utils.showToast("输入的手机号码长度不符");
                return ;
            }
        }

        if (!Phone2.equals("")) {
            if (!Phone2.substring(0, 1).equals("1")) {
                Utils.showToast("请输入正确的手机号码");
                return ;
            }
            if (Phone2.length() != 11) {
                Utils.showToast("输入的手机号码长度不符");
                return ;
            }
        }

        if (Address.equals("")) {
            Utils.showToast(ET_Address.getHint().toString());
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {

            JSONArray JA = new JSONArray();
            JSONObject JB;
            String index;
            String PhotoFile;
            for (int i = 0; i < PLI.size(); i++) {
                index = PLI.get(i).getINDEX();
                PhotoFile = (String) SharedPreferencesUtils.get("Photo:" + index, "");
                JB = new JSONObject();
                JB.put("INDEX", index);
                JB.put("Photo", "");
                JB.put("PhotoFile", PhotoFile);
                JA.put(JB);
            }

            jsonObject.put("VEHICLETYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
            jsonObject.put("CARTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE));
            jsonObject.put("VEHICLEBRAND", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
            jsonObject.put("PlateNumber", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
            jsonObject.put("SHELVESNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
            jsonObject.put("ENGINENO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
            jsonObject.put("COLORID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID));
            jsonObject.put("COLORID2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2ID));
            jsonObject.put("BUYDATE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));

            jsonObject.put("CARDTYPE", mCardTypeId);
            jsonObject.put("OWNERNAME", ET_Name.getText().toString().trim());
            jsonObject.put("CARDID", ET_CardID.getText().toString().trim());
            jsonObject.put("PHONE1",  ET_Phone.getText().toString().trim());
            jsonObject.put("PHONE2",  ET_Phone2.getText().toString().trim());
            jsonObject.put("ADDRESS", ET_Address.getText().toString().trim());
            jsonObject.put("REMARK", ET_Remark.getText().toString().trim());
            jsonObject.put("PhotoListFile", JA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        mLog.e("预登记："+map.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_ADDPREREGISTER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                mLog.e("预登记result："+result);
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        String ID = json.getString("Id");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            if(ShowQR.equals("0")){
                                CardTypeDialogShow(1, data);
                            }else{
                                if(ID!=null&&!ID.equals("")){
                                    ID="TDR_APP"+ID;
                                    Bundle bundle=new Bundle();
                                    bundle.putBoolean("IsCanBack", false);
                                    bundle.putString("QRCodeID", ID);
                                    ActivityUtil.goActivityWithBundle(mActivity, QRCodeCreateActivity.class, bundle);
                                    finish();
                                }else{
                                    CardTypeDialogShow(1, data);
                                }
                            }
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.showToast( data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(DX_PreRegistration_Personnel_Activity.this, LoginActivity.class);
                        } else {
                            Utils.showToast(  data);
                            mProgressHUD.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        Utils.showToast( "JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast( "获取数据超时，请检查网络连接");
                }
            }
        });
    }
    private void sevedate(){
        String Name = ET_Name.getText().toString().trim();
        String CardID = ET_CardID.getText().toString().trim();
        String Phone = ET_Phone.getText().toString().trim();
        String Phone2 = ET_Phone2.getText().toString().trim();
        String Address = ET_Address.getText().toString().trim();
        String Remark = ET_Remark.getText().toString().trim();
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, Name);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, CardID);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, Phone);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, Phone2);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, Address);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, Remark);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_back:
                onBackPressed();
                break;
            case R.id.TV_CardType:
                CardTypeDialogShow(0,"");
                break;
            case R.id.BT_Submit:
                Submit();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                mLog.e("position="+position);
                cardType = position;
                cardTypeMap.clear();
                cardTypeMap.put(position, 100);
                cardTypeAdapter.notifyDataSetChanged();
                mCardType = cardTypeList.get(position).getName();
                mCardTypeId = cardTypeList.get(position).getGuid();
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        sevedate();
        finish();
    }
}

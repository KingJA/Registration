package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.model.CarLabel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.OrderModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;


/**
 * 更换标签查询
 */
@ContentView(R.layout.activity_label_binding_query)
public class LabelBindingCarQueryActivity extends  Activity{

    @ViewInject(R.id.IV_Back)
    ImageView IV_Back;
    @ViewInject(R.id.ET_PlateNumber)
    EditText ET_PlateNumber;
    @ViewInject(R.id.BT_Search)
    Button BT_Search;

    private Context mContext;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private Activity mActivity;
    private boolean isRuning=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        initView();
    }

    private void initView() {
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        IV_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();
            }
        });
        ET_PlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        BT_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plateNumber = ET_PlateNumber.getText().toString().toUpperCase().trim();
                if (plateNumber.equals("")) {
                    Utils.myToast(mContext, "查询条件不可为空");
                }else{
                    QueryCar(plateNumber);
                }
            }
        });

    }
    private void QueryCar(String plateNumber){
        if(isRuning){
            return;
        }
        isRuning=true;
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PlateNumber",plateNumber);
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants.HTTP_GetCarInfo);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        mLog.e("GetCarInfo:" + JB.toString());
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        isRuning=false;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                CarLabel CL = mGson.fromJson(data, new TypeToken<CarLabel>() {
                                }.getType());
                                dialogShow(CL);
                            } else if (errorCode==1){
                                Utils.myToast(mContext, data);
                                SharedPreferencesUtils.put("token","");
                                ActivityUtil.goActivityAndFinish(LabelBindingCarQueryActivity.this, LoginActivity.class);
                            }else{
                                Utils.showToast(data);
                            }
                            mProgressHUD.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressHUD.dismiss();
                            Utils.showToast("JSON解析出错");
                        }
                    } else {
                        mProgressHUD.dismiss();
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mProgressHUD.dismiss();
                    mLog.e("Http访问结果：" + Finish);
                }
            }
        });
    }


    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;
    private void dialogShow(final CarLabel CL) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(this);
        View layoutShow = mInflater.inflate(R.layout.layout_showcar, null);

        ((TextView) layoutShow.findViewById(R.id.text_plateNum)).setText(CL.getPlateNumber());
        ((TextView) layoutShow.findViewById(R.id.text_ownerIdentity)).setText(CL.getCardId());
        ((TextView) layoutShow.findViewById(R.id.text_ownerName)).setText(CL.getOwnerName());
        ((TextView) layoutShow.findViewById(R.id.text_plateBrand)).setText(CL.getVehicleBrandName());

        dialogBuilder.isCancelable(false);
        dialogBuilder.setCustomView(layoutShow, mContext);
        dialogBuilder.withTitle("车辆信息").withTitleColor("#333333")
                .withButton1Text("取消").withButton2Text("下一步")
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
                Bundle bundle=new Bundle();
                bundle.putSerializable("CarInfo", CL);
                ActivityUtil.goActivityWithBundle(LabelBindingCarQueryActivity.this, LabelBindingListActivity.class,bundle);
            }
        }).show();
    }
}

package com.tdr.registration.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.normal.RegisterCarActivity;
import com.tdr.registration.model.DX_PreRegistrationModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.tdr.registration.util.VehiclesStorageUtils.clearData;

/**
 * 二维码生成界面
 */
@ContentView(R.layout.activity_qrcode_create)
public class QRCodeCreateActivity extends Activity {

    @ViewInject(R.id.IV_back)
    ImageView imageBack;

    @ViewInject(R.id.TV_Register)
    TextView TV_Register;

    @ViewInject(R.id.IV_QRCode)
    ImageView IV_QRCode;
    private boolean IsCanBack=false;

    private Activity mActivity;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private List<DX_PreRegistrationModel> PRList;
    private String QRCodeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdate();
    }

    private void initview() {
        mActivity=this;
        mGson=new Gson();
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TV_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
    }

    private void initdate(){
        Bundle bundle = getIntent().getExtras();
        IsCanBack=bundle.getBoolean("IsCanBack", false);
        QRCodeID = bundle.getString("QRCodeID");
        String UserType = ((String) SharedPreferencesUtils.get("UserType", ""));
        mLog.e("UserType="+UserType);
        if(IsCanBack){
            TV_Register.setVisibility(View.VISIBLE);
        }else{
            TV_Register.setVisibility(View.GONE);
        }
        if(!UserType.equals("1")){
            mLog.e("-------------UserType");
            TV_Register.setVisibility(View.GONE);
        }
        mLog.e("QRCodeID="+QRCodeID);
        CreateQRCode createqrcode = new CreateQRCode();
        createqrcode.execute(QRCodeID);
    }
    private class CreateQRCode extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return QRCodeEncoder.syncEncodeQRCode(params[0], BGAQRCodeUtil.dp2px(QRCodeCreateActivity.this, 150));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                IV_QRCode.setImageBitmap(bitmap);
            } else {
                Utils.showToast("生成二维码失败");
            }
        }
    }
    @Override
    public void onBackPressed() {
        if(IsCanBack){
            finish();
        }else{
            clearData();
            ActivityUtil.goActivityAndFinish(QRCodeCreateActivity.this, HomeActivity.class);
        }
    }

    /**
     * 获取电信预登记
     */
    private void query(){
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken",(String)SharedPreferencesUtils.get("token",""));
        map.put("plateNumber","");
        map.put("cardid","");
        map.put("phone","");
        map.put("registerId",QRCodeID.substring(7));
        mLog.e("Pan","map="+map);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPREREGISTERLIST, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Utils.LOGE("Pan",result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            try{
                                PRList = mGson.fromJson(data, new TypeToken<List<DX_PreRegistrationModel>>() {
                                }.getType());
                                if(PRList.get(0)!=null){
                                    Bundle bundle=new Bundle();
                                    bundle.putString("InType","PreRegistration");
                                    bundle.putSerializable("PreRegistrationModel", PRList.get(0));
                                    TransferUtil.save("PreRegistrationModel",PRList.get(0));
                                    ActivityUtil.goActivityWithBundle(QRCodeCreateActivity.this, RegisterCarActivity.class, bundle);
                                }
                            }catch (JsonSyntaxException e){
                                Utils.showToast(data);
                            }
                        }else{
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast( "获取数据超时，请检查网络连接");
                }
            }
        });

    }


}

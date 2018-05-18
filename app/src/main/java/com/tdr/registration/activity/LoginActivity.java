package com.tdr.registration.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.BlackCarList;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.FieldSettingModel;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.LoginModel;
import com.tdr.registration.update.UpdateManager;
import com.tdr.registration.update.strategy.WebServiceStrategy;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppInfoUtil;
import com.tdr.registration.util.CheckUpdate;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.DESCoder;
import com.tdr.registration.util.PermissionUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ClearEditTextView;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 登陆
 */
public class LoginActivity extends AppCompatActivity {


    private Gson mGson;
    private String cityName = "";
    private final static int CITY = 1991;
    @BindView(R.id.linear_city)
    RelativeLayout linearCity;
    @BindView(R.id.text_cityName)
    TextView textCityName;
    @BindView(R.id.image_appName)
    ImageView imageAppName;
    @BindView(R.id.text_appName)
    TextView textAppName;
    @BindView(R.id.clear_userName)
    ClearEditTextView clearUserName;
    @BindView(R.id.clear_userPwd)
    ClearEditTextView clearUserPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.LOGO)
    ImageView LOGO;
    @BindView(R.id.RL_Loding)
    RelativeLayout RL_Loding;
    @BindView(R.id.TV_min)
    TextView TV_min;
    private String[] permissionArray = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Intent intent;
    private DbManager db;
    private boolean getBaseDataing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = (String) SharedPreferencesUtils.get("token", "");
        String LoginSuccess = (String) SharedPreferencesUtils.get("LoginSuccess", "");
        if (!"".equals(token) && !"".equals(LoginSuccess) && "Success".equals(LoginSuccess)) {
            ActivityUtil.goActivityAndFinish(LoginActivity.this, HomeActivity.class);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mGson = new Gson();
        intent = new Intent();
        db = x.getDb(DBUtils.getDb());
        PermissionUtils.checkPermissionArray(LoginActivity.this, permissionArray, PermissionUtils
                .PERMISSION_REQUEST_CODE);
        TV_min.setText("初始化数据...");
        textCityName.setText((String) SharedPreferencesUtils.get("locCityName", ""));
        getBaseData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }


    @OnClick({R.id.linear_city, R.id.btn_login, R.id.RL_Loding})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.linear_city:
                intent.setClass(LoginActivity.this, CityPickerActivity.class);
                startActivityForResult(intent, CITY);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.btn_login:
                Login();
                break;
            case R.id.RL_Loding:
                break;
            default:
                break;

        }
    }


    private void Login() {
        String userName = clearUserName.getText().toString().trim();
        if (userName.equals("")) {
            Utils.myToast(this, "请输入用户名");
            return;
        }
        String userPwd = clearUserPwd.getText().toString();
        if (userPwd.equals("")) {
            Utils.myToast(this, "请输入密码");
            return;
        }
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager
                .PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        String imei = tm.getDeviceId();
        TV_min.setText("正在登陆...");
        RL_Loding.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
            jsonObject.put("password", userPwd);
            jsonObject.put("imei", imei);
            if (cityName.contains("经销商")) {
                jsonObject.put("version", AppInfoUtil.getVersionCode() + "_ANDROID_SELLER");
            } else {
                jsonObject.put("version", AppInfoUtil.getVersionCode() + "_ANDROID");
            }
            jsonObject.put("ChannelID", JPushInterface.getRegistrationID(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("userInfo", jsonObject.toString());

        WebServiceUtils.callWebService(this, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_LOGIN_V2, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Logger.json(result);
                if (result != null) {
                    LoginModel loginModel = mGson.fromJson(result, new TypeToken<LoginModel>() {
                    }.getType());
                    if (loginModel.getErrorCode().equals("0")) {
                        RL_Loding.setVisibility(View.GONE);
                        SharedPreferencesUtils.put("userId", loginModel.getUserID());
                        SharedPreferencesUtils.put("userName", loginModel.getUserName());
                        SharedPreferencesUtils.put("rolePowers", loginModel.getRolePowers());
                        SharedPreferencesUtils.put("token", loginModel.getAccessToken());
                        SharedPreferencesUtils.put("codeVersion", loginModel.getCodeVersion());
                        SharedPreferencesUtils.put("regionNo", Utils.initNullStr(loginModel.getRegionNo()));
                        SharedPreferencesUtils.put("regionName", Utils.initNullStr(loginModel.getRegionName()));
                        SharedPreferencesUtils.put("city", Utils.initNullStr(loginModel.getCity()));
                        SharedPreferencesUtils.put("regionId", Utils.initNullStr(loginModel.getRegionID()));
                        SharedPreferencesUtils.put("UserType", Utils.initNullStr(loginModel.getUserType()));
                        if (loginModel.getRegionNo() == null) {
                            loginModel.setRegionNo("3");
                        }
                        if (loginModel.getRegionNo() != null && loginModel.getRegionNo().length() == 4) {//市级帐号
                            SharedPreferencesUtils.put("roleLevel", "1");
                        } else if (loginModel.getRegionNo().length() == 6) {//区、县帐号
                            SharedPreferencesUtils.put("roleLevel", "2");
                        } else {
                            SharedPreferencesUtils.put("roleLevel", "3");
                        }
                        //获取保险
                        String userName = clearUserName.getText().toString().trim();
                        String userPwd = clearUserPwd.getText().toString();
                        SharedPreferencesUtils.put("UP", loginModel.getCity() + ":" + userName + ":" + userPwd);
                        getInsuranceConfigure();
                    } else if (loginModel.getErrorCode().equals("")) {
                        RL_Loding.setVisibility(View.GONE);
                    } else {
                        RL_Loding.setVisibility(View.GONE);
                        Utils.myToast(LoginActivity.this, loginModel.getData());
                    }
                } else {
                    RL_Loding.setVisibility(View.GONE);
                    Utils.myToast(LoginActivity.this, "获取数据超时，请检查网络连接");
                }
            }
        });

    }

    /**
     * 获取保险数据
     */
    private void getInsuranceConfigure() {
        TV_min.setText("获取保险数据...");
        RL_Loding.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        WebServiceUtils.callWebService(this, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_GETINSURANCECONFIGURE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Logger.json(result);
                DownloadInsurance DI = new DownloadInsurance(result);
                DI.execute();
            }
        });
    }

    /**
     * 下载编码表
     */
    private void downLoadData() {
        TV_min.setText("获取配置字典表...");
        RL_Loding.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("lastUpdateTime", (String) SharedPreferencesUtils.get("codeUpdateTime", ""));
        WebServiceUtils.callWebService(this, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_GETCODETABLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    DownloadData download3 = new DownloadData(result);
                    download3.execute();
                }
            }
        });
    }

    private class DownloadInsurance extends AsyncTask<String, Integer, String> {
        private String result;
        private int MAX = 0;

        public DownloadInsurance(String result) {
            this.result = result;
        }

        @Override
        protected String doInBackground(String... params) {
            if (result != null) {
                Logger.json(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    String data = jsonObject.getString("Data");
                    List<InsuranceModel> listInsurance = new ArrayList<InsuranceModel>();
                    List<DetailBean> listDetailBean = new ArrayList<DetailBean>();
                    if (errorCode == 0) {
                        listInsurance = mGson.fromJson(data, new TypeToken<List<InsuranceModel>>() {
                        }.getType());
                        MAX = listInsurance.size();
                        db.dropTable(InsuranceModel.class);
                        db.dropTable(DetailBean.class);
                        if (listInsurance.size() > 0 && listInsurance != null) {
                            for (int i = 0; i < listInsurance.size(); i++) {
                                if (!listInsurance.get(i).getIsValid().equals("0")) {
                                    publishProgress(i);
                                    db.save(listInsurance.get(i));
                                    listDetailBean = listInsurance.get(i).getDetail();
                                    for (int j = 0; j < listDetailBean.size(); j++) {
                                        DetailBean meanB = listDetailBean.get(j);
                                        db.save(meanB);
                                    }
                                }
                            }
                        }
                        return "完成";
                    } else {
                        Utils.myToast(LoginActivity.this, data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.myToast(LoginActivity.this, "JSON解析出错");
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
            return "失败";
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);
            TV_min.setText("正在更新保险数据：" + (value[0] + 1) + "/" + MAX);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            RL_Loding.setVisibility(View.GONE);
            if (s.equals("失败")) {
                Utils.myToast(LoginActivity.this, "获取保险数据超时，请检查网络连接");
            }
            downLoadData();
        }
    }

    private class DownloadData extends AsyncTask<String, Integer, String> {
        private String result;
        private int MAX = 0;

        public DownloadData(String result) {
            this.result = result;
        }

        @Override
        protected String doInBackground(String... params) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    String data = jsonObject.getString("Data");
                    if (errorCode == 0) {
                        List<BikeCode> bikeCodes = mGson.fromJson(data, new TypeToken<List<BikeCode>>() {
                        }.getType());
                        String codeUpdateTime = (String) SharedPreferencesUtils.get("codeUpdateTime", "");
                        if (bikeCodes != null && bikeCodes.size() > 0) {
                            MAX = bikeCodes.size();
                            if (codeUpdateTime.equals("")) {//重新下载
                                mLog.e("重新下载字典表");
                                db.dropTable(BikeCode.class);
                                for (int i = 0; i < MAX; i++) {
                                    db.save(bikeCodes.get(i));
                                    publishProgress(i);
                                }
                            } else {//更新
                                mLog.e("更新");
                                updateCode(bikeCodes);
                            }
                            String time = Utils.getNowTime();
                            mLog.e("UpdateTime=" + time);
                            SharedPreferencesUtils.put("codeUpdateTime", time);
                        }
                        return "完成";
                    } else {
                        Utils.myToast(LoginActivity.this, data);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.myToast(LoginActivity.this, "JSON解析出错");
                } catch (DbException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.myToast(LoginActivity.this, "获取数据超时，请检查网络连接");
            }
            return "失败";
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);
            TV_min.setText("正在更新字典表：" + (value[0] + 1) + "/" + MAX);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            RL_Loding.setVisibility(View.GONE);
            if (s.equals("完成")) {
                SharedPreferencesUtils.put("LoginSuccess", "Success");
                try {
                    db.dropTable(BlackCarList.class);
                    mLog.e("清除本地黑车库");
                } catch (DbException e) {
                    e.printStackTrace();
                }
                ActivityUtil.goActivityAndFinish(LoginActivity.this, HomeActivity.class);
            }


        }

        private void updateCode(List<BikeCode> list) throws DbException {
            List<BikeCode> BC = db.findAll(BikeCode.class);
            HashMap<String, BikeCode> hm = new HashMap<String, BikeCode>();
            MAX = list.size();
            if (BC != null && BC.size() > 0) {
                mLog.e(BC.size() + "开始更新" + BC.size());
                for (BikeCode bikeCode : BC) {
                    hm.put(bikeCode.getCodeId(), bikeCode);
                }
                mLog.e("Map=" + hm.size());
                int index = 0;
                for (BikeCode bikeCode : list) {
                    if (hm.containsKey(bikeCode.getCodeId())) {
                        mLog.e("本地有该条数据" + bikeCode.getName());
                        if (bikeCode.getIsdelete().equals("1")) {
                            mLog.e("删除该数据" + bikeCode.getName());
                            db.deleteById(BikeCode.class, bikeCode.getCodeId());
                        } else {
                            mLog.e("更新该数据" + bikeCode.getName());
                            db.saveOrUpdate(bikeCode);
                        }
                    } else {
                        mLog.e("新增数据" + bikeCode.getName());
                        db.save(bikeCode);
                    }
                    publishProgress(index);
                    index++;
                }
            } else {
                mLog.e("本地无数据，直接写入" + list.size());
                for (int i = 0; i < list.size(); i++) {
                    db.save(list.get(i));
                    publishProgress(i);
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.PERMISSION_REQUEST_CODE:
                if (PermissionUtils.verifyPermissions(grantResults)) {
                } else {
                    Toast.makeText(this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("帮助");
                    builder.setMessage("当前应用缺少必要权限。点击设置打开权限设置页,否则无法使用该app");

                    // 拒绝, 退出应用
                    builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });

                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    });

                    builder.setCancelable(false);

                    builder.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static final String PACKAGE_URL_SCHEME = "package:";

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionUtils.PERMISSION_SETTING_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {

                } else {
                    Toast.makeText(this, "not has setting permission", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else if (requestCode == CITY) {
            if (resultCode == RESULT_OK) {
                BaseInfo mInfo = (BaseInfo) data.getSerializableExtra("city");
                cityName = mInfo.getCityName();
                mLog.e("cityName=" + mInfo.getCityCode() + "    CityName=" + mInfo.getCityName());
                String locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
                if (!locCityName.equals(cityName)) {
                    SharedPreferencesUtils.put("codeUpdateTime", "");
                }
                SharedPreferencesUtils.put("locCityName", cityName);
                textCityName.setText(cityName);
                initData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //相关参数设置
    private void initData() {
        List<BaseInfo> resultList = new ArrayList<BaseInfo>();
        try {
            resultList = db.selector(BaseInfo.class).where("cityName", "=", cityName).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (resultList == null) {
            resultList = new ArrayList<BaseInfo>();
        }
        if (resultList.size() != 0) {
            String plateNumberRegular = resultList.get(0).getPlatenumberRegular();
            try {
                JSONArray array = new JSONArray(plateNumberRegular);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String carType = json.getString("CarType");
//                    SharedPreferencesUtils.put("carType", carType);//备案登记车种
                    String regular = json.getString("Regular");
                    SharedPreferencesUtils.put("PlatenumberRegular" + carType, regular);//车牌正则
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String bluetoothRegular = resultList.get(0).getBluetooth_Regular();
            try {
                JSONArray jsonArray = new JSONArray(bluetoothRegular);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String key = jsonObject.getString("KEY");
                    SharedPreferencesUtils.put("key", key);
                    String deviceType = jsonObject.getString("DEVICETYPE");
                    SharedPreferencesUtils.put("deviceType", deviceType);
                    String content = jsonObject.getString("CONTENT");
                    SharedPreferencesUtils.put("content", content);
                    String pcCode = jsonObject.getString("PCCODE");
                    SharedPreferencesUtils.put("pcCode", pcCode);
                    String provinceAbbr = jsonObject.getString("PROVINCEABBR");
                    SharedPreferencesUtils.put("provinceAbbr", provinceAbbr);
                    String cityAbbr = jsonObject.getString("CITYABBR");
                    SharedPreferencesUtils.put("cityAbbr", cityAbbr);
                    String XQCode = jsonObject.getString("XQCode");
                    SharedPreferencesUtils.put("XQCode", XQCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String appConfig = resultList.get(0).getAppConfig();
            SharedPreferencesUtils.put("isChecked", "");
            SharedPreferencesUtils.put("whiteListUrl", "");
            SharedPreferencesUtils.put("hasPreregister", "");
            SharedPreferencesUtils.put("isScanLabel", "");
            SharedPreferencesUtils.put("isScanCard", "");
            SharedPreferencesUtils.put("INSURANCEREMARK", "");
            SharedPreferencesUtils.put("ACCEPTCONTRACT", "");
            SharedPreferencesUtils.put("REGULAR", "");
            SharedPreferencesUtils.put("REGULAR2", "");
            SharedPreferencesUtils.put("IsDoubleSign", "");
            SharedPreferencesUtils.put("EnableInvoice", "");
            SharedPreferencesUtils.put("IsShowPay", "");
            SharedPreferencesUtils.put("ShowWallet", "");
            SharedPreferencesUtils.put("ChangeType", "");
            SharedPreferencesUtils.put("HasAgent", "");
            SharedPreferencesUtils.put("IsConfirm", "");
            SharedPreferencesUtils.put("IsScanDjh", "");
            SharedPreferencesUtils.put("IsScanCjh", "");
            try {
                JSONArray jsonArray = new JSONArray(appConfig);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String key = jsonObject.getString("key");
                    switch (key) {
                        case "WhiteListApp":
                            String isChecked = jsonObject.getString("value");
                            SharedPreferencesUtils.put("isChecked", isChecked);
                            break;
                        case "WhiteListUrl":
                            String whiteListUrl = jsonObject.getString("value");
                            SharedPreferencesUtils.put("whiteListUrl", whiteListUrl);
                            break;
                        case "HasPreregister":
                            String hasPreregister = jsonObject.getString("value");
                            SharedPreferencesUtils.put("hasPreregister", hasPreregister);
                            break;
                        case "IsScanLabel":
                            String isScanLabel = jsonObject.getString("value");
                            SharedPreferencesUtils.put("isScanLabel", isScanLabel);
                            break;
                        case "IsScanCard":
                            String isScanCard = jsonObject.getString("value");
                            SharedPreferencesUtils.put("isScanCard", isScanCard);
                            break;
                        case "THEFTNO1_REGULAR":
                            String REGULAR = jsonObject.getString("value");
                            SharedPreferencesUtils.put("REGULAR", REGULAR);
                            break;
                        case "THEFTNO2_REGULAR":
                            String REGULAR2 = jsonObject.getString("value");
                            SharedPreferencesUtils.put("REGULAR2", REGULAR2);
                            break;
                        case "IsDoubleSign":
                            String ISDOUBLESIGN = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsDoubleSign", ISDOUBLESIGN);
                            break;
                        case "EnableInvoice":
                            String EnableInvoice = jsonObject.getString("value");
                            SharedPreferencesUtils.put("EnableInvoice", EnableInvoice);
                            break;
                        case "IsShowPay":
                            String IsShowPay = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsShowPay", IsShowPay);
                            break;
                        case "ShowWallet":
                            String ShowWallet = jsonObject.getString("value");
                            SharedPreferencesUtils.put("ShowWallet", ShowWallet);
                            break;

                        case "ChangeType":
                            String ChangeType = jsonObject.getString("value");
                            SharedPreferencesUtils.put("ChangeType", ChangeType);
                            break;
                        case "HasAgent":
                            String HasAgent = jsonObject.getString("value");
                            SharedPreferencesUtils.put("HasAgent", HasAgent);
                            break;
                        case "IsConfirm":
                            String IsConfirm = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsConfirm", IsConfirm);
                            break;
                        case "IsScanDjh":
                            String IsScanDjh = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsScanDjh", IsScanDjh);
                            break;
                        case "IsScanCjh":
                            String IsScanCjh = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsScanCjh", IsScanCjh);
                            break;
                        case "ShowQR":
                            String ShowQR = jsonObject.getString("value");
                            SharedPreferencesUtils.put("ShowQR", ShowQR);
                            break;
                        case "IsTransferReserve":
                            String IsTransferReserve = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsTransferReserve", IsTransferReserve);
                            break;
                        default:
                            break;

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String FieldSetting = resultList.get(0).getFieldSetting();
            mLog.e("FieldSetting" + FieldSetting);
            if (FieldSetting != null && !FieldSetting.equals("")) {
                List<FieldSettingModel> fieldSettingModels = new ArrayList<>();
                fieldSettingModels = mGson.fromJson(FieldSetting, new TypeToken<List<FieldSettingModel>>() {
                }.getType());
                if (fieldSettingModels != null && fieldSettingModels.size() > 0) {
                    for (FieldSettingModel fieldSettingModel : fieldSettingModels) {
                        if (fieldSettingModel.getFIELDKEY().equals("INSURANCEREMARK")) {
                            SharedPreferencesUtils.put("INSURANCEREMARK", fieldSettingModel.getDISPLAYNAME());
                        } else if (fieldSettingModel.getFIELDKEY().equals("ACCEPTCONTRACT")) {
                            SharedPreferencesUtils.put("ACCEPTCONTRACT", fieldSettingModel.getDISPLAYNAME());
                        } else if (fieldSettingModel.getFIELDKEY().equals("REGISTRATION")) {
                            SharedPreferencesUtils.put("REGISTRATION", fieldSettingModel.getDISPLAYNAME());
                        }
                    }
                }
            }

            Utils.CompleteConfig();//补齐缺失字段
            SharedPreferencesUtils.put("appName", resultList.get(0).getAppName());
            SharedPreferencesUtils.put("CarTypesList", resultList.get(0).getCardType());
            SharedPreferencesUtils.put("fullSpell", resultList.get(0).getFullSpell());
            SharedPreferencesUtils.put("cityCode", resultList.get(0).getCityCode());
            SharedPreferencesUtils.put("apiUrl", resultList.get(0).getApiUrl());

            String httpUrl = resultList.get(0).getApiUrl().substring(0, xxx(3, "/", resultList.get(0).getApiUrl()) + 1);
            SharedPreferencesUtils.put("httpUrl", httpUrl);

            String v = resultList.get(0).getVersion();
            if (v == null) {
                v = "";
            }
            SharedPreferencesUtils.put("Version", v);
            String appName = (String) SharedPreferencesUtils.get("appName", "");
            if (appName.equals("")) {
                imageAppName.setVisibility(View.VISIBLE);
                textAppName.setVisibility(View.GONE);
            } else {
                imageAppName.setVisibility(View.GONE);
                textAppName.setText(appName);
                textAppName.setVisibility(View.VISIBLE);
            }
            new CheckUpdate(this).UpdateAPK();
        } else {
            if (!cityName.equals("")) {
                Utils.myToast(LoginActivity.this, cityName + "即将开放");
            }
        }

    }

    public static int xxx(int paramInt, String paramString1, String paramString2) {
        int j = 0;
        int i = 1;
        while (i <= paramInt) {
            j = paramString2.indexOf(paramString1, j + 1);
            i += 1;
        }
        return j;
    }

    private void getBaseData() {
        if (getBaseDataing) {
            getBaseDataing = false;
        } else {
            return;
        }
        TV_min.setText("初始化中...");
        RL_Loding.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", DESCoder.encrypt("GETCITYLIST", Constants.DES_KEY));
        map.put("infoJsonStr", "_" + "ANDROID");
        WebServiceUtils.callWebService(this, Constants.WEBSERVER_URL, Constants.WEBSERVER_OPENAPI, map, new
                WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        Logger.json(result);
                        if (result != null) {
                            getBaseDataing = true;
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                List<BaseInfo> baseInfos = new ArrayList<BaseInfo>();
                                if (errorCode == 0) {
                                    baseInfos = mGson.fromJson(data, new TypeToken<List<BaseInfo>>() {
                                    }.getType());
                                    if (baseInfos != null && baseInfos.size() > 0) {
                                        db.dropTable(BaseInfo.class);
                                        for (int i = 0; i < baseInfos.size(); i++) {
                                            if (!baseInfos.get(i).getIsValid().equals("0")) {
                                                db.save(baseInfos.get(i));
                                            }
                                        }
                                        SharedPreferencesUtils.put("cityUpdateTime", Utils.getNowTime());
                                    }
                                    if (cityName != null && !cityName.equals("")) {
                                        initData();
                                    }
                                    RL_Loding.setVisibility(View.GONE);
                                } else {
                                    RL_Loding.setVisibility(View.GONE);
                                    Utils.myToast(LoginActivity.this, data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                RL_Loding.setVisibility(View.GONE);
                                Utils.myToast(LoginActivity.this, "JSON解析出错");
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        } else {
                            RL_Loding.setVisibility(View.GONE);
                            Utils.myToast(LoginActivity.this, "获取城市列表数据超时，请检查网络连接。");
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (RL_Loding.getVisibility() == View.VISIBLE) {
            Utils.showToast("系统正在加载数据!请勿关闭程序！");
        }
    }

}

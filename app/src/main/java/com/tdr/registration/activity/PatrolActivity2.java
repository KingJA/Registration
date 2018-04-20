package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.tdr.registration.R;
import com.tdr.registration.adapter.AuditingAdapter;
import com.tdr.registration.adapter.BlackCarAdapter;
import com.tdr.registration.base.MyApplication;
import com.tdr.registration.data.PacketData;
import com.tdr.registration.model.BlackCarList;
import com.tdr.registration.model.BlackCarModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.BleInterface;
import com.tdr.registration.util.BLE_Util;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PoolManager;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.util.onBleStateListener;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.BlePop;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 巡逻模式
 */
@ContentView(R.layout.activity_patrol)
public class PatrolActivity2 extends Activity implements
        Handler.Callback, View.OnClickListener, BlePop.OnBlePopClickListener,
        BleInterface.ConnectBleListener, BleInterface.BleNotifyListener, onBleStateListener, BleInterface.BleWriteListener {

    @ViewInject(R.id.image_back)
    private ImageView imageBack;
    @ViewInject(R.id.text_title)
    private TextView textTitle;
    @ViewInject(R.id.image_scan)
    private ImageView imageScan;
    @ViewInject(R.id.relative_title)
    private RelativeLayout relativeTitle;
    @ViewInject(R.id.text_bleState)
    private TextView textBleState;
    @ViewInject(R.id.text_bleName)
    private TextView textBleName;
    @ViewInject(R.id.linear_bleState)
    private LinearLayout linearBleState;
    @ViewInject(R.id.btn_selectble)
    private Button btnSelectble;
    @ViewInject(R.id.relative_stateBg)
    private RelativeLayout relativeStateBg;
    @ViewInject(R.id.text_blacklist)
    private TextView textBlacklist;
    @ViewInject(R.id.text_updateTime)
    private TextView textUpdateTime;
    @ViewInject(R.id.image_update)
    private ImageView imageUpdate;
    @ViewInject(R.id.list_blackCar)
    private ListView listBlackCar;
    @ViewInject(R.id.linear_updateBlack)
    private LinearLayout linearUpdateBlack;

    private Context mContext;
    private Handler mHandler;
    private BlePop blePop;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private DbManager db;

    private BLE_Util BLE = BLE_Util.instance;
    private PacketData mData = BLE_Util.instance.getmData();


    private List<ElectricCarModel> models = new ArrayList<>();
    private AuditingAdapter mAuditingAdapter;
    private BlackCarAdapter mBlackCarAdapter;

    private List<BlackCarModel> modelList = new ArrayList<>();//查询过来解析的黑车列表
    private List<BlackCarModel> selectedBlack = new ArrayList<>();//查询出来需要展示的黑车


    // 静止锁屏
    private PowerManager power = null;
    private PowerManager.WakeLock lock = null;
    private String bluetoothReglar;
    private String BleName;
    private String BleMAC;

    private MyApplication BA;
    private byte[] SendBlackCarMSG;
    private boolean BleState = false;
    private boolean isFirst = true;
    private boolean isSendKey = false;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initView();
        initData();
        BA = ((MyApplication) mContext.getApplicationContext());
        isBle();

        mLog.e("蓝牙缓存：" + BLE.isConnection());
        BleName = BLE.getName();
        BleMAC = BLE.getMAC();
//        if (BLE.isConnection()) {
//            BleName = BLE.getName();
//            BleMAC = BLE.getMAC();
//            BLE.ConnectBle(this);
//        }
        BA.setBSL(this);
    }

    private void initView() {
        mContext = this;
        mHandler = new Handler(this);
        mGson = new Gson();
        db = x.getDb(DBUtils.getDb());
        bluetoothReglar = (String) SharedPreferencesUtils.get("key", "");
        textTitle.setText("车辆查缉");
        imageScan.setVisibility(View.VISIBLE);
        imageScan.setBackgroundResource(R.mipmap.ble_menu);
        blePop = new BlePop(imageScan, PatrolActivity2.this);
        textUpdateTime.setText((String) SharedPreferencesUtils.get("blackCarUpdateTime", ""));
        relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));

        try {
            selectedBlack = L2M(db.findAll(BlackCarList.class));
            mLog.e("本地黑车库：" + selectedBlack.size());
        } catch (DbException e) {
            e.printStackTrace();
        }

        mAuditingAdapter = new AuditingAdapter(mContext, selectedBlack, 0);
        listBlackCar.setAdapter(mAuditingAdapter);
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        imageScan.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        blePop.setOnBlePopClickListener(this);
        btnSelectble.setOnClickListener(this);
        linearUpdateBlack.setOnClickListener(this);
        mAuditingAdapter.setClickListener(new AuditingAdapter.AuditingClickListener() {
            @Override
            public void onItemclick(BlackCarModel blackCarModel) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("BlackCar", blackCarModel);
                bundle.putBoolean("BleState", BleState);
                ActivityUtil.goActivityWithBundle(PatrolActivity2.this, BlackCarInfoActivity.class, bundle);
            }

            @Override
            public void onItemBottonclick(BlackCarModel blackCarModel) {
                if (BleState) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BlackCar", blackCarModel);
                    ActivityUtil.goActivityWithBundle(PatrolActivity2.this, SeekActivity2.class, bundle);
                } else {
                    Utils.showToast("请先连接稽查设备");
                }
            }
        });

        this.power = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        this.lock = this.power.newWakeLock(PowerManager.FULL_WAKE_LOCK, "ble");
    }

    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("no", (String) SharedPreferencesUtils.get("synId", ""));
        mProgressHUD.show();
        WebServiceUtils.callWebService(this, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETBLACKLIST, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Utils.LOGE("Pan", "黑车库:" + result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            modelList = mGson.fromJson(data, new TypeToken<List<BlackCarModel>>() {
                            }.getType());
                            mLog.e("黑车总数:" + modelList.size());
                            if (modelList.size() > 0) {
                                SharedPreferencesUtils.put("blackCarUpdateTime", Utils.getNowDate());
                                textUpdateTime.setText(Utils.getNowDate());

                                try {
                                    db.dropTable(BlackCarModel.class);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                PoolManager.getInstance().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (BlackCarModel model : modelList) {
                                            try {
                                                db.save(model);
                                            } catch (DbException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }
                            //扫描到的黑车数据库对比网络黑车数据库
                            List<BlackCarModel> bcm = new ArrayList<>();
                            int a = 0, b = 0;
                            for (BlackCarModel blackCarModel : selectedBlack) {
                                mLog.e(blackCarModel.getTHEFTNO() == null ? a + "1THEFTNO空" : a + "1THEFTNO非空");
                                mLog.e(blackCarModel.getTHEFTNO2() == null ? a + "1THEFTNO2空" : a + "1THEFTNO2非空");
                                for (BlackCarModel carModel : modelList) {
                                    mLog.e(carModel.getTHEFTNO() == null ? b + "2THEFTNO空" : b + "2THEFTNO非空");
                                    mLog.e(carModel.getTHEFTNO2() == null ? b + "2THEFTNO2空" : b + "2THEFTNO2非空");
                                    //如果该车载网络的黑车库里没有，则表示该车已被撤控
                                    if (blackCarModel.getTHEFTNO() != null && carModel.getTHEFTNO() != null) {
                                        if (blackCarModel.getTHEFTNO().equals(carModel.getTHEFTNO())) {
                                            bcm.add(blackCarModel);
                                        }
                                    } else {
                                        if (blackCarModel.getTHEFTNO2() != null && carModel.getTHEFTNO2() != null) {
                                            if (blackCarModel.getTHEFTNO2().equals(carModel.getTHEFTNO2())) {
                                                bcm.add(blackCarModel);
                                            }
                                        }
                                    }
                                    b++;
                                }
                                a++;
                            }
                            for (BlackCarModel blackCarModel : bcm) {
                                if (!selectedBlack.contains(blackCarModel)) {
                                    selectedBlack.remove(blackCarModel);
                                    db.delete(db.selector(BlackCarList.class).where("THEFTNO", "=", blackCarModel.getTHEFTNO()).or("THEFTNO2", "=", blackCarModel.getTHEFTNO2()).findFirst());
                                }
                            }
                            mAuditingAdapter.notifyDataSetChanged();
                        } else if (errorCode == 1) {
                            Utils.showToast(data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(PatrolActivity2.this, LoginActivity.class);
                        } else {
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.showToast("JSON解析出错");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    mProgressHUD.dismiss();
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
//        blackCarModels = db.findAll(BlackCarModel.class);
    }

    private void isBle() {
        //手机硬件支持蓝牙
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.showToast("该设备不支持BLE，即将离开改页面");
            try {
                Thread.sleep(2000);
                onBackPressed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUI(boolean b) {
        BleState = b;
        if (b) {
            mProgressHUD.dismiss();
            Utils.showToast("连接成功");
            textBleState.setText("已连接");
            textBleName.setText(BLE.getName());
            relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorStatus));
            relativeStateBg.setBackgroundColor(getResources().getColor(R.color.colorStatus));
        } else {
            Utils.showToast("连接断开");
            relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));
            relativeStateBg.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));
            textBleState.setText("未连接");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(BA.isBleConnectStatus());
        if (BleMAC != null && !BleName.equals("")) {
            BLE.ConnectBle(BleMAC, this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_selectble:
                Intent intent = new Intent();
                intent.setClass(PatrolActivity2.this, BleListActivity.class);
                startActivityForResult(intent, BLE.BLECODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.linear_updateBlack:
                initData();
                break;
            case R.id.image_scan:
                blePop.showPopupWindowDownOffset();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLE.BLECODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                //bluetoothDevices = bundle.getParcelable("bleDevice");
                BleName = bundle.getString("BleName");
                BleMAC = bundle.getString("BleMAC");
                mLog.e("BleName=" + BleName);
                mLog.e("BleMAC=" + BleMAC);
                SharedPreferencesUtils.put("bleName", BleName);
                SharedPreferencesUtils.put("bleMacAddress", BleMAC);
                BLE.ConnectBle(BleMAC, this);

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BLE.unRegisterConnectStatus(BLE.getMAC());
        BLE.unRegisterStateBLE();
        BLE.unNotify(BLE.getMAC(), BLE.getBGP());
    }

    @Override
    public void onBlePop(int position) {
        mLog.e("position=" + position);
        switch (position) {
            case 0:

                break;
            case 1:
                dialogShow(0, "单车查缉");
//                BlackCarModel BC = new BlackCarModel();
//                BC.setPLATENUMBER("  LR045040");
//                BC.setTHEFTNO("140750574628848");
//                BC.setTHEFTNO2(null);
//                SendBlackCarMSG=new byte[BLE.SendBlackCar(BC).length];
//                SendBlackCarMSG=BLE.SendBlackCar(BC);
//                BLE.Write(mData.sendPackageBegin(51, SendBlackCarMSG));
//                LOG.BLE("1单车数据："+Utils.bytesToHexString(BLE.SendBlackCar(BC)));
//                mLog.BLE("单车数据：" + Utils.bytesToHexString(mData.sendPackageBegin(51, BLE.SendBlackCar(BC))));
                break;
            case 2:

                ActivityUtil.goActivity(PatrolActivity2.this, AerialSignalActivity.class);
                break;
        }
        blePop.dismiss();
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View identityView = mInflater.inflate(R.layout.layout_query_identity, null);
            TextView textName = (TextView) identityView.findViewById(R.id.text_name);
            textName.setText("车牌号：");
            final EditText editQueryIdentity = (EditText) identityView
                    .findViewById(R.id.edit_queryIdentity);
            editQueryIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(identityView, mContext);
            dialogBuilder.withTitle(message).withTitleColor("#333333")
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
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String queryIdentity = editQueryIdentity.getText().toString().toUpperCase();
                    queryPlateNumber(queryIdentity);

                }
            }).show();
        }

    }

    private void queryPlateNumber(String queryIdentity) {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("W_CPH", queryIdentity);
        map.put("W_FDJH", "");
        map.put("W_CJH", "");
        WebServiceUtils.callWebService(this, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKSTOLENVEHICLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Utils.LOGE("Pan", "单车下发：" + result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            int type = jsonObject.getInt("Type");
                            String ElectricCar = jsonObject.getString("ElectricCar");
                            ElectricCarModel model = mGson.fromJson(ElectricCar, new TypeToken<ElectricCarModel>() {
                            }.getType());

                            if (type != 1) {
                                Utils.showToast("该车辆非布控车辆，不可查缉");
                            } else {
                                BlackCarModel BC = new BlackCarModel();
                                BC.setPLATENUMBER(model.getPlateNumber());
                                BC.setTHEFTNO(model.getTHEFTNO());
                                BC.setTHEFTNO2(model.getTHEFTNO2());
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("BlackCar", BC);
                                ActivityUtil.goActivityWithBundle(PatrolActivity2.this, SeekActivity2.class, bundle);
                            }
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(PatrolActivity2.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        BLE.DisConnectBle(BleMAC);
        BLE.unRegisterConnectStatus(BleMAC);
        BLE.unRegisterStateBLE();
        if (BLE.getBGP() != null) {
            BLE.unNotify(BleMAC, BLE.getBGP());
        }
        BA.setBSL(null);
        super.onDestroy();
    }

    @Override
    public void onNotify(byte[] value) {
        Notify(value);
        if (isFirst) {
            mLog.BLE("检查KEY" + Utils.bytesToHexString(mData.queryKey()));
            BLE.Write(mData.queryKey());
            mLog.BLE("下发配置:" + Utils.bytesToHexString(mData.sendPackageBegin(51, mData.packageSendContent())));
            isFirst = false;
        }
    }

    private void Notify(byte[] value) {
        byte[] Type = Utils.GetByteArrayByLength(value, 1, 1);
        if (!Utils.checkByte(Type, mData.uploadHeartBeat) && !Utils.checkByte(Type, mData.uploadState)) {
//            mLog.BLE("蓝牙数据=" + Utils.bytesToHexString(value));
        }
        if (Utils.checkByte(Type, mData.queryKeyCommand)) {//检查key
            isFirst = false;
            byte[] content = Utils.GetByteArrayByLength(value, 2, 19);
//            String key = mData.parsingKey(Utils.deleteByte(value, 2));
            String key = mData.parsingKey(content);
//            if (!bluetoothReglar.equals(key)) {//如果key不一致，则先下发配置
                isSendKey = true;
                BLE.Write(mData.sendPackageBegin(51, mData.packageSendContent()));
//            }
        } else if (Utils.checkByte(Type, mData.uploadHeartBeat)) {//获取标签心跳数据 对比数据库黑车
            mLog.BLE("标签心跳=" + Utils.bytesToHexString(value));
            String theftno = String.valueOf(mData.heartBeat(Utils.deleteByte(value, 2)));
            try {
                dealTheftNo(theftno);
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else if (Utils.checkByte(Type, mData.packagingStart)
                || Utils.checkByte(Type, mData.packagingComplete)
                || Utils.checkByte(Type, mData.sendKeyCommand)) {//打包下发
//            String result = Utils.bytesToHexString(Utils.GetByteArrayByLength(Utils.deleteByte(value, 2), 0, 1));
            String result = Utils.bytesToHexString(Utils.GetByteArrayByLength(Utils.deleteByte(value, 2), 0, 1));
            mLog.BLE(result.equals("01") ? "指令下发失败" : "指令下发成功");
            if (isSendKey) {//是否下发Key
                if (Utils.checkByte(Type, mData.packagingStart)) {
                    if (!result.equals("01")) {//51包下发成功
                        byte[] sendPackage = mData.sendPackaging(mData.packageSendContent());
                        mLog.BLE("开始下发配置");
                        int length = (int) Math.ceil((short) sendPackage.length / 20);//总共需要发送的包数
                        for (int i = 0; i < length; i++) {
                            BLE.Write(Utils.GetByteArrayByLength(sendPackage, i * 20, 20));
                            mLog.BLE("第" + i + "个包：" + Utils.bytesToHexString(Utils.GetByteArrayByLength(sendPackage, i * 20, 20)));
                            SystemClock.sleep(60);//下发间隔60毫秒
                        }
                        BLE.Write(mData.sendPackageBegin(52, mData.packageSendContent()));
                        mLog.BLE("配置下发完成");
                    } else {//下发失败 重新下发51 城市配置
                        mLog.BLE("配置下发失败，重新发送");
                        BLE.Write(mData.sendPackageBegin(51, mData.packageSendContent()));
                    }
                } else if (Utils.checkByte(Type, mData.packagingComplete)) {
                    mLog.BLE("配置下发成功");
                    isSendKey = false;
                }
            }
            if (SendBlackCarMSG != null) {//下发黑车
                if (Utils.checkByte(Type, mData.packagingStart)) {
                    if (!result.equals("01")) {//51包下发成功
                        byte[] sendPackage = mData.sendPackaging(SendBlackCarMSG);
                        mLog.BLE("要下发的数据：" + Utils.bytesToHexString(mData.sendPackaging(SendBlackCarMSG)));
                        int length = (int) Math.ceil((short) sendPackage.length / 20);//总共需要发送的包数
                        for (int i = 0; i < length; i++) {
                            BLE.Write(Utils.GetByteArrayByLength(sendPackage, i * 20, 20));
                            mLog.BLE("第" + i + "个包：" + Utils.bytesToHexString(Utils.GetByteArrayByLength(sendPackage, i * 20, 20)));
                            SystemClock.sleep(60);//下发间隔60毫秒
                        }
                        SystemClock.sleep(60);
                        BLE.Write(mData.sendPackageBegin(52, SendBlackCarMSG));

                    } else {
                        BLE.Write(mData.sendPackageBegin(51, SendBlackCarMSG));
                    }
                } else if (Utils.checkByte(Type, mData.packagingComplete)) {
                    mLog.BLE("黑车下发完成");
                    SendBlackCarMSG = null;
                }
            }
        }
    }


    private void dealTheftNo(String theftno) throws DbException {
        List<BlackCarModel> tempBlack = new ArrayList<>();
        //匹配黑车库是否有蓝牙上传回的车辆
        try {
            tempBlack = db.selector(BlackCarModel.class).where("THEFTNO", "=", theftno).or("THEFTNO2", "=", theftno).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (tempBlack == null) {
            tempBlack = new ArrayList<BlackCarModel>();
        }

//        mLog.BLE(theftno + "    tempBlack=" + tempBlack.size());
        if (tempBlack.size() > 0) {//list有数据说明该车属于黑车
            if (tempBlack.get(0).getSTATE().equals("0")) {
                mLog.BLE("稽查枪上传的标签：" + theftno);
                for (int i = 0; i < selectedBlack.size(); i++) {//更新黑车展示列表
                    try {
                        if ((selectedBlack.get(i).getTHEFTNO() != null && selectedBlack.get(i).getTHEFTNO().equals(theftno))
                                || (selectedBlack.get(i).getTHEFTNO2() != null && selectedBlack.get(i).getTHEFTNO2().equals(theftno))) {
                            selectedBlack.remove(i);//移除该车在界面车辆列表中早期的数据
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                selectedBlack.add(tempBlack.get(0));//添加最新的数据
                Collections.reverse(selectedBlack);//反序排列 近期的在上
                mAuditingAdapter.notifyDataSetChanged();

                try {
                    db.delete(BlackCarList.class);
                    //存储最新的数据到数据库
                    db.save(M2L(selectedBlack));
                } catch (DbException e) {
                    e.printStackTrace();
                }
                //下发该车辆给稽查枪
                SendBlackCarMSG = BLE.SendBlackCar(tempBlack.get(0));
                BLE.Write(mData.sendPackageBegin(51, SendBlackCarMSG));
            }
        }
    }

    @Override
    public void onResponse(int code, BleGattProfile data) {
        if (code == 0) {
            BLE.setName(BleName);
            BLE.setMAC(BleMAC);
            BLE.Notify(BleMAC, data, this);
            BLE.Write(mData.sendKey(), data);
            updateUI(true);
            BLE.setWriteListener(this);
        }
    }


    @Override
    public void BleState(boolean State) {
        BleState = State;
        updateUI(State);
        if (State && !BA.isBleConnectStatus()) {
            BLE.ConnectBle(this);
            Utils.showToast("蓝牙重新连接");
        }
    }

    @Override
    public void BleConnectState(boolean State) {

        updateUI(State);
        if (!State && BA.isBleStatus()) {
            BLE.ConnectBle(this);
            Utils.showToast("蓝牙重新连接");
        }
    }


    private List<BlackCarModel> L2M(List<BlackCarList> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<BlackCarModel> BCList = new ArrayList<>();
        for (BlackCarList blackCarList : list) {
            BCList.add(L2M(blackCarList));
        }
        return BCList;
    }

    private BlackCarModel L2M(BlackCarList bc) {
        BlackCarModel BC = new BlackCarModel();
        BC.setId(bc.getId());
        BC.setOPERATORTIME(bc.getOPERATORTIME());
        BC.setPLATENUMBER(bc.getPLATENUMBER());
        BC.setSTATE(bc.getSTATE());
        BC.setSYNCID(bc.getSYNCID());
        BC.setTHEFTNO(bc.getTHEFTNO());
        BC.setTHEFTNO2(bc.getTHEFTNO2());
        return BC;
    }

    private List<BlackCarList> M2L(List<BlackCarModel> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<BlackCarList> BCList = new ArrayList<>();
        for (BlackCarModel blackCarList : list) {
            BCList.add(M2L(blackCarList));
        }
        return BCList;
    }

    private BlackCarList M2L(BlackCarModel bc) {
        BlackCarList BC = new BlackCarList();
        BC.setId(bc.getId());
        BC.setOPERATORTIME(bc.getOPERATORTIME());
        BC.setPLATENUMBER(bc.getPLATENUMBER());
        BC.setSTATE(bc.getSTATE());
        BC.setSYNCID(bc.getSYNCID());
        BC.setTHEFTNO(bc.getTHEFTNO());
        BC.setTHEFTNO2(bc.getTHEFTNO2());
        return BC;
    }

    @Override
    public void onWriteListener(int code) {

    }
}

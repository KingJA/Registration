package com.tdr.registration.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.AuditingAdapter;
import com.tdr.registration.adapter.BlackCarAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.ble.BluetoothLeService;
import com.tdr.registration.data.PacketData;
import com.tdr.registration.gprinter.Util;
import com.tdr.registration.model.BlackCarModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PoolManager;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.BlePop;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 巡逻模式
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class PatrolActivity extends BaseActivity implements Handler.Callback {
    private static final String TAG = "AuditingActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_deal)
    TextView textDeal;
    @BindView(R.id.relative_title)
    RelativeLayout relativeTitle;
    @BindView(R.id.text_bleState)
    TextView textBleState;
    @BindView(R.id.text_bleName)
    TextView textBleName;
    @BindView(R.id.linear_bleState)
    LinearLayout linearBleState;
    @BindView(R.id.btn_selectble)
    Button btnSelectble;
    @BindView(R.id.relative_stateBg)
    RelativeLayout relativeStateBg;
    @BindView(R.id.text_blacklist)
    TextView textBlacklist;
    @BindView(R.id.text_updateTime)
    TextView textUpdateTime;
    @BindView(R.id.image_update)
    ImageView imageUpdate;
    @BindView(R.id.list_blackCar)
    ListView listBlackCar;
    @BindView(R.id.linear_updateBlack)
    LinearLayout linearUpdateBlack;

    private Context mContext;
    private Handler mHandler;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private DbManager db;
    private List<ElectricCarModel> models = new ArrayList<>();
    private AuditingAdapter mAuditingAdapter;
    private BlackCarAdapter mBlackCarAdapter;

    private List<BlackCarModel> modelList = new ArrayList<>();//查询过来解析的黑车列表
    private List<BlackCarModel> blackCarModels = new ArrayList<>();//数据库查询出来的黑车
    private List<BlackCarModel> selectedBlack = new ArrayList<>();//查询出来需要展示的黑车

    private List<BlackCarModel> sendBlackModels = new ArrayList<>();//需要下发的全部黑车

    private BlackCarModel blackCarModel = new BlackCarModel();

    private static final int BLECODE = 1991;
    private static final int TEMPBLACKCAR = 0316;

    private static final long TIME_OUT = 10000;

    private boolean isFirst = true;//每次进来，如果首次连接则下发KEY
    private boolean isBlack = false;//是否开始下发黑车
    private boolean allBlack = false;//是否全部下发全部黑车

    private PacketData mData;

    String bluetoothReglar = "";//蓝牙规则
    private BluetoothDevice bluetoothDevices;
    //蓝牙连接状态
    private boolean mConnected = false;
    private String status = "disconnected";
    private String mDeviceName = "";
    private String mDeviceAddress = "";
    //蓝牙service，负责后台蓝牙服务
    private static BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private static BluetoothGattCharacteristic target_chara = null;

    // 静止锁屏
    private PowerManager power = null;
    private PowerManager.WakeLock lock = null;

    private Timer timer;
    private Activity mActivity;
    public static final int SHOW_PROGRESS = 10;
    private Handler UI_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESS:
//                    Log.e(TAG,"显示Progress");
                    mProgressHUD.show();
                    break;

            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
        mHandler = new Handler(this);
        mGson = new Gson();
        db = x.getDb(DBUtils.getDb());
        bluetoothReglar = (String) SharedPreferencesUtils.get("key", "");
        mData = new PacketData();
        timer = new Timer();
        initView();

        initData();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                initData();
//            }
//        }, 300000, 1000);
        isBle();
        connectDevice();
    }

    private void initView() {
        textTitle.setText("车辆稽查");
        textDeal.setText("单车稽查");
        textDeal.setVisibility(View.VISIBLE);
        textUpdateTime.setText((String) SharedPreferencesUtils.get("blackCarUpdateTime", ""));

        relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));

        mAuditingAdapter = new AuditingAdapter(mContext, selectedBlack, 0);

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        this.power = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        this.lock = this.power.newWakeLock(PowerManager.FULL_WAKE_LOCK, "ble");
    }


    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("no", (String) SharedPreferencesUtils.get("synId", ""));
        UI_Handler.sendEmptyMessage(SHOW_PROGRESS);
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETBLACKLIST, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            modelList = mGson.fromJson(data, new TypeToken<List<BlackCarModel>>() {
                            }.getType());
                            for (int i = 0; i < modelList.size(); i++) {
//                                Log.e(TAG,"modelList="+modelList.get(i).getPLATENUMBER());
                            }
                            if (modelList.size() > 0) {
                                db.delete(BlackCarModel.class);
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
                            mProgressHUD.dismiss();
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(PatrolActivity.this, LoginActivity.class);
                        } else {
                            Utils.myToast(mContext, data);
                            mProgressHUD.dismiss();
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
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
        try {
            blackCarModels = db.findAll(BlackCarModel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (blackCarModels == null) {
            blackCarModels = new ArrayList<BlackCarModel>();
        }
    }

    private void isBle() {
        //手机硬件支持蓝牙
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.myToast(mContext, "该设备不支持BLE，即将离开改页面");
            try {
                Thread.sleep(2000);
                onBackPressed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectDevice() {
        mDeviceAddress = (String) SharedPreferencesUtils.get("bleMacAddress", "");
        if (!mDeviceAddress.equals("")) {
            /* 启动蓝牙service */
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        } else {
            Utils.myToast(mContext, "请选择手持设备");
            return;
        }
    }

    /* BluetoothLeService绑定的回调函数 */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // 根据蓝牙地址，连接设备
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))// Gatt连接成功
            {
                mConnected = true;
                status = "connected";
                // 更新连接状态
                updateUI(mConnected);
//                Log.e(TAG,"BroadcastReceiver :" + "device connected");
                System.out.println("BroadcastReceiver :" + "device connected");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED// Gatt连接失败
                    .equals(action)) {
                mConnected = false;
                status = "disconnected";
                // 更新连接状态
                updateUI(mConnected);
//                Log.e(TAG,"BroadcastReceiver :" + "device disconnected");
                System.out.println("BroadcastReceiver :" + "device disconnected");

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED// 发现GATT服务器
                    .equals(action)) {
                // 获取设备的所有蓝牙服务
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
//                Log.e(TAG,"BroadcastReceiver :" + "device SERVICES_DISCOVERED");
                System.out.println("BroadcastReceiver :" + "device SERVICES_DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))// 有效数据
            {
                // 处理发送过来的数据
                displayData(intent.getExtras().getByteArray(BluetoothLeService.EXTRA_DATA));
                String date = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
//                if(date!=null&&!date.equals("")){
//                    Log.e(TAG,"BroadcastReceiver onData:" + date);
//                }
//                System.out.println("BroadcastReceiver onData:" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void updateUI(boolean b) {
        if (b) {
            mProgressHUD.dismiss();
            Utils.myToast(mContext, "连接成功");
            textBleState.setText("已连接");
            textBleName.setText((String) SharedPreferencesUtils.get("bleName", ""));
            relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorStatus));
            relativeStateBg.setBackgroundColor(getResources().getColor(R.color.colorStatus));

        } else {
            Utils.myToast(mContext, "连接失败，请重新选择设备");
            relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));
            relativeStateBg.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));
            textBleState.setText("未连接");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 绑定广播接收器
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            // 根据蓝牙地址，建立连接
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @OnClick({R.id.image_back, R.id.image_scan, R.id.btn_selectble, R.id.image_update, R.id.linear_updateBlack, R.id.text_deal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.btn_selectble:
                Intent intent = new Intent();
                intent.setClass(PatrolActivity.this, SelectBleActivity.class);
                startActivityForResult(intent, BLECODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.linear_updateBlack:
                initData();
                break;
            case R.id.text_deal:
                dialogShow(0, "单车稽查");


                break;

        }
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

    private void showmsg() {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        Log.e("Pan", "showmsg");
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("稽查进行中，确定要退出吗？")
                .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                .setCustomView(R.layout.custom_view, mContext).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(mGattUpdateReceiver);
                mBluetoothLeService = null;
                dialogBuilder.dismiss();
                finish();
            }
        }).show();
    }

    private void queryPlateNumber(String queryIdentity) {

        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("W_CPH", queryIdentity);
        map.put("W_FDJH", "");
        map.put("W_CJH", "");
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKSTOLENVEHICLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Utils.LOGE("Pan", "单车稽查" + result);
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
                           /* if (models.size() == 0) {
                                models.add(model);
                                listTempCar.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                for (ElectricCarModel electricCarModel : models) {
                                    if (!electricCarModel.getPlateNumber().equals(model.getPlateNumber())) {
                                        models.add(model);
                                        listTempCar.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }*/
                            if (target_chara == null) {
                                Utils.myToast(mContext, "蓝牙设备未连接，请先连接设备");
                            } else {
                                if (model != null && type == 1) {
                                    blackCarModel.setPLATENUMBER(model.getPlateNumber());
                                    blackCarModel.setTHEFTNO(model.getTHEFTNO());
                                    blackCarModel.setTHEFTNO2(model.getTHEFTNO2());
                                    target_chara.setValue(mData.sendPackageBegin(51, black(blackCarModel)));
                                    mLog.e("黑车下发开始: " + Utils.bytesToHexString(mData.sendPackageBegin(51, black(blackCarModel))));
                                    mBluetoothLeService.writeCharacteristic(target_chara);
                                    isBlack = true;
                                } else {
                                    Utils.myToast(mContext, "该车辆非黑车，不可稽查");
                                }
                            }

                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(PatrolActivity.this, LoginActivity.class);
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

    @Override
    public void onBackPressed() {
        Log.e("Pan", "onBackPressed");
        showmsg();
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLECODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                bluetoothDevices = bundle.getParcelable("bleDevice");
                SharedPreferencesUtils.put("bleName", bluetoothDevices.getName());
                SharedPreferencesUtils.put("bleMacAddress", bluetoothDevices.getAddress());
                connectDevice();
            }
        }
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";

        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();

            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。

            gattServiceData.add(currentServiceData);

            System.out.println("Service uuid:" + uuid);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();

                if (gattCharacteristic.getUuid().toString().equals(Constants.UUID_CHAR_WRITE)) {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // 设置数据内容
                    // 往蓝牙模块写入数据
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                    if (isFirst) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        Log.e(TAG, "displayGattServices: " + "queryKey:" + Utils.bytesToHexString(mData.queryKey()));
                        target_chara.setValue(mData.queryKey());
                        mBluetoothLeService.writeCharacteristic(target_chara);
                    }
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }

                gattCharacteristicGroupData.add(currentCharaData);
            }
            // 按先后顺序，分层次放入特征值集合中，只有特征值
            mGattCharacteristics.add(charas);
            // 构件第二级扩展列表（服务下面的特征值）
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    private void displayData(byte[] rev_string) {
        byte[] commandType = Utils.GetByteArrayByLength(rev_string, 1, 1);
        byte[] content = Utils.GetByteArrayByLength(rev_string, 2, 19);
        if (!Utils.checkByte(commandType, mData.uploadHeartBeat)) {
            Log.e("Bluetooth", "收到数据类型：" + Utils.bytesToHexString(commandType));
            Log.e("Bluetooth", "数据内容：" + Utils.bytesToHexString(rev_string));
        }
        if (Utils.checkByte(commandType, mData.queryKeyCommand)) {
            isFirst = false;
//            Log.e(TAG, "displayData: " + "bluetoothReglar:" + bluetoothReglar);
            String key = mData.parsingKey(content);
//            Log.e(TAG,"设备的key"+bluetoothReglar+"    key="+key);
            if (!bluetoothReglar.equals(key)) {//如果key不一致，则先下发配置
                target_chara.setValue(mData.sendPackageBegin(51, mData.packageSendContent()));
//                Log.e(TAG, "51下发打包开始命令: " + Utils.bytesToHexString(mData.sendPackageBegin(51, mData.packageSendContent())));
                mBluetoothLeService.writeCharacteristic(target_chara);

            } else {//如果一致下发临时布控
//                target_chara.setValue(mData.sendPackageBegin(51, allBlack()));
//                Log.e(TAG, "黑车下发开始: " + Utils.bytesToHexString(mData.sendPackageBegin(51, black())));
//                mBluetoothLeService.writeCharacteristic(target_chara);
//                allBlack = true;
            }


//            aa40 11 03 1d 08 39 1f 00 00 00 00 00 00 00 00 00 00 f424
        } else if (Utils.checkByte(commandType, mData.uploadState)) {
          mData.searchState(content);

        } else if (Utils.checkByte(commandType, mData.uploadHeartBeat)) {
            String theftno = String.valueOf(mData.heartBeat(content));
            dealTheftNo(theftno);
        } else if (Utils.checkByte(commandType, mData.GatewayDate)) {
//            Log.e("Pan","--------------GatewayDate----------------");
//            mData.newDevicePackage(45);
            mData.AnalysisDate(content);
        } else if (Utils.checkByte(commandType, mData.GatewayLowPower)) {
//            Log.e("Pan","--------------GatewayLowPower----------------");

        } else if (Utils.checkByte(commandType, mData.getDeviceTime)) {
//            Log.e("Pan","--------------getDeviceTime----------------");
//            target_chara.setValue(mData.setDeviceTime());
            mBluetoothLeService.writeCharacteristic(target_chara);
        } else if (Utils.checkByte(commandType, mData.setDeviceTime)) {
//            Log.e("Pan","--------------setDeviceTime----------------");
//            target_chara.setValue(mData.queryCar(123456,8021,97));
            mBluetoothLeService.writeCharacteristic(target_chara);
        } else if (Utils.checkByte(commandType, mData.queryCar)) {
//            Log.e("Pan","--------------queryCar----------------");
//            target_chara.setValue(mData.getGatewayDate(123456));
            mBluetoothLeService.writeCharacteristic(target_chara);
        } else if (Utils.checkByte(commandType, mData.getGatewayDate)) {
//            Log.e("Pan","--------------getGatewayDate----------------");
//            target_chara.setValue(mData.setGatewayTime(123456));
            mBluetoothLeService.writeCharacteristic(target_chara);
        } else if (Utils.checkByte(commandType, mData.setGatewayTime)) {
//            Log.e("Pan","--------------setGatewayTime----------------");

        } else if (Utils.checkByte(commandType, mData.packagingStart)
                || Utils.checkByte(commandType, mData.sendKeyCommand)
                || Utils.checkByte(commandType, mData.deleteOnePlateNumberCommand)
                || Utils.checkByte(commandType, mData.packagingComplete)) {
            String result = Utils.bytesToHexString(Utils.GetByteArrayByLength(content, 0, 1));
            if (result.equals("01")) {//发送不成功，重新发送
                if (Utils.checkByte(commandType, mData.packagingStart)) {
                    target_chara.setValue(mData.sendPackageBegin(51, mData.sendPackaging(mData.packageSendContent())));
//                    Log.e(TAG, mData.packageSendContent()+" 下发打包开始命令: " + Utils.bytesToHexString(mData.sendPackageBegin(51, mData.sendPackaging(mData.packageSendContent()))));
                    mBluetoothLeService.writeCharacteristic(target_chara);
                } else if (Utils.checkByte(commandType, mData.sendKeyCommand)) {
                    target_chara.setValue(mData.sendKey());
                    mBluetoothLeService.writeCharacteristic(target_chara);
                } else if (Utils.checkByte(commandType, mData.deleteOnePlateNumberCommand)) {//暂时没有
                }
            } else {
                if (Utils.checkByte(commandType, mData.packagingStart)) {
                    if (isBlack && !allBlack) {
                        byte[] sendBlack = black(blackCarModel);
                        byte[] sendPackage = mData.sendPackaging(sendBlack);
                        int length = (int) Math.ceil((short) sendPackage.length / 20);//总共需要发送的包数
                        for (int i = 0; i < length; i++) {
                            target_chara.setValue(Utils.GetByteArrayByLength(sendPackage, i * 20, 20));
//                            Log.e(TAG, "黑车下发打包内容: " + Utils.bytesToHexString(Utils.GetByteArrayByLength(sendPackage, i * 20, 20)));
                            mBluetoothLeService.writeCharacteristic(target_chara);
                            try {
                                Thread.sleep(60);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        target_chara.setValue(mData.sendPackageBegin(52, sendBlack));
                        mBluetoothLeService.writeCharacteristic(target_chara);
                    } else {

                        byte[] sendPackage = mData.sendPackaging(mData.packageSendContent());

//                        for (int i = 0; i < sendPackage.length; i++) {
//                            Log.e(TAG,"sendPackaging="+sendPackage[i]);
//                        }
                        int length = (int) Math.ceil((short) sendPackage.length / 20);//总共需要发送的包数
                        for (int i = 0; i < length; i++) {
                            target_chara.setValue(Utils.GetByteArrayByLength(sendPackage, i * 20, 20));
//                            Log.e(TAG, "53下发打包内容: " + Utils.bytesToHexString(Utils.GetByteArrayByLength(sendPackage, i * 20, 20)));
                            mBluetoothLeService.writeCharacteristic(target_chara);
                            try {
                                Thread.sleep(60);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        target_chara.setValue(mData.sendPackageBegin(52, mData.packageSendContent()));
//                        Log.e(TAG, "52下发打包内容: " + Utils.bytesToHexString(mData.sendPackageBegin(52, mData.packageSendContent())));
                        mBluetoothLeService.writeCharacteristic(target_chara);

//                        target_chara.setValue(mData.getDeviceTime());
//                        mBluetoothLeService.writeCharacteristic(target_chara);

                    }
                } else if (Utils.checkByte(commandType, mData.packagingComplete)) {
                    target_chara.setValue(mData.sendKey());
//                    Log.e(TAG, "下发配置的key: " + Utils.bytesToHexString(mData.sendKey()));
                    mBluetoothLeService.writeCharacteristic(target_chara);
                } else if (Utils.checkByte(commandType, mData.sendKeyCommand)) {
                    /*target_chara.setValue(mData.sendPackageBegin(51, black()));
                    Log.e(TAG, "黑车下发开始: " + Utils.bytesToHexString(mData.sendPackageBegin(51, black())));
                    mBluetoothLeService.writeCharacteristic(target_chara);
                    isBlack = true;*/
                }
            }
        }
    }

    private void dealTheftNo(String theftno) {
        List<BlackCarModel> tempBlack = new ArrayList<>();
//        tempBlack = db.findAllByWhere(BlackCarModel.class, " THEFTNO = " + "\'" + theftno + "\'" + " or " + " THEFTNO2 = " + "\'" + theftno + "\'");
        try {
            tempBlack = db.selector(BlackCarModel.class).where("THEFTNO", "=", theftno).or("THEFTNO2", "=", theftno).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (tempBlack == null) {
            tempBlack = new ArrayList<BlackCarModel>();
        }
        if (tempBlack.size() > 0) {
            if (tempBlack.get(0).getSTATE().equals("0")) {
            /*if (selectedBlack.size() > 0) {
                for (int i = 0; i < selectedBlack.size(); i++) {
                    String plateNumber = selectedBlack.get(i).getPLATENUMBER();
                    String plateNumberTemp = tempBlack.get(0).getPLATENUMBER();
                    if (plateNumber.equals(plateNumberTemp)) {
                        selectedBlack.remove(tempBlack.get(0));
                        selectedBlack.add(tempBlack.get(0));
                        sendBlackModels.remove(tempBlack.get(0));
                        sendBlackModels.add(tempBlack.get(0));
                    } else {
                        selectedBlack.add(tempBlack.get(0));
                        sendBlackModels.add(tempBlack.get(0));
                    }
                }
            } else {
                selectedBlack.add(tempBlack.get(0));
                sendBlackModels.add(tempBlack.get(0));
            }*/
            mLog.e("selectedBlack="+selectedBlack.size());
                if (!selectedBlack.contains(tempBlack.get(0))) {
                    selectedBlack.add(tempBlack.get(0));
                } else {
                    selectedBlack.remove(tempBlack.get(0));
                    selectedBlack.add(tempBlack.get(0));
                }
                listBlackCar.setAdapter(mAuditingAdapter);
                mAuditingAdapter.notifyDataSetChanged();
                blackCarModel = tempBlack.get(0);
                target_chara.setValue(mData.sendPackageBegin(51, black(blackCarModel)));
//                Log.e(TAG, "黑车下发开始: " + Utils.bytesToHexString(mData.sendPackageBegin(51, black(blackCarModel))));
                mBluetoothLeService.writeCharacteristic(target_chara);
                isBlack = true;
            }
        }
    }

    /* private byte[] black(BlackCarModel model) {
         byte[] requestData = null;
         byte[] PN = null;
         String pcCode = (String) SharedPreferencesUtils.get("pcCode", "");//省市编码，云南昆明：0701
         // for (ElectricCarModel model : models) {
         byte[] childContent = null;
         byte[] content = null;
         byte[] plate = new byte[10];
         byte[] no1 = null;
         byte[] tag1 = null;
         byte[] tagId1 = null;
         byte[] no2 = null;
         byte[] tag2 = null;
         byte[] tagId2 = null;
         String plateNumber = model.getPLATENUMBER();
 //        String plateNumber = "新罗";
 //        String theftNo = Utils.initNullStr(model.getTHEFTNO());//840,920
         String theftNo = "0444442";//840,920
         String theftNo2 = Utils.initNullStr(model.getTHEFTNO2());//2.4G
         while (plateNumber.length() < 10) {
             plateNumber = " " + plateNumber;
         }
         try {
             plate = plateNumber.getBytes("ASCII");
             mLog.e(plate + "plateNumber=" + plateNumber);
             mLog.e("theftNo=" + theftNo);
             mLog.e("theftNo2=" + theftNo2);
 //            for (int i = 0; i < plateNumber.length(); i++) {
 //                String str = plateNumber.charAt(i) + "";
 //                PN = Utils.ByteArrayCopy(PN, str.getBytes("ASCII"));
 //                mLog.e(PN + "str=" + str);
 //            }
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
 //        mLog.e("PN=" + PN.length);
         if (!theftNo.equals("")) {
             no1 = Utils.longToByte(Long.valueOf(theftNo));
             tag1 = Utils.GetByteArrayByLength(no1, 4, 2);
             tagId1 = Utils.GetByteArrayByLength(no1, 0, 4);
         }
         if (!theftNo2.equals("")) {
             no2 = Utils.longToByte(Long.valueOf(theftNo2));
             tag2 = Utils.GetByteArrayByLength(no2, 4, 2);
             tagId2 = Utils.GetByteArrayByLength(no2, 0, 4);
         }
         childContent = Utils.ByteArrayCopy(childContent, Utils.hexStr2Bytes(pcCode));
         childContent = Utils.ByteArrayCopy(childContent, plate);
         childContent = Utils.ByteArrayCopy(childContent, tag1);
         childContent = Utils.ByteArrayCopy(childContent, tagId1);
         childContent = Utils.ByteArrayCopy(childContent, tag2);
         childContent = Utils.ByteArrayCopy(childContent, tagId2);
         byte[] size = Utils.shortToByte((short) childContent.length);
         content = Utils.ByteArrayCopy(content, mData.controlCommand);
         content = Utils.ByteArrayCopy(content, size);
         content = Utils.ByteArrayCopy(content, childContent);
         requestData = Utils.ByteArrayCopy(requestData, content);
         //}
         Utils.bytesToHexString(requestData);
         mLog.e("单车稽查：" + Utils.bytesToHexString(requestData));
         return requestData;
     }*/
    private byte[] black(BlackCarModel BCM) {
        byte[] requestData = null;
        String pcCode = (String) SharedPreferencesUtils.get("pcCode", "");//省市编码，云南昆明：0701
        byte[] childContent = null;
        byte[] content = null;
        byte[] plate = new byte[10];

        String plateNumber = BCM.getPLATENUMBER();

        String theftNo = Utils.initNullStr(BCM.getTHEFTNO());//840,920
        String theftNo2 = Utils.initNullStr(BCM.getTHEFTNO2());//2.4G
        while (plateNumber.length() < 10) {
            plateNumber = " " + plateNumber;
        }
        try {
            plate = plateNumber.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] tag1 = null;
        byte[] tagId1 = null;
        if (!theftNo.equals("")) {
            byte[] no1 = Utils.longToByte(Long.valueOf(theftNo));
            tag1 = Utils.GetByteArrayByLength(no1, 4, 2);
            tagId1 = Utils.GetByteArrayByLength(no1, 0, 4);
        }

        byte[] tag2 = null;
        byte[] tagId2 = null;
        if (!theftNo2.equals("")) {
            byte[] no2 = Utils.longToByte(Long.valueOf(theftNo2));
            tag2 = Utils.GetByteArrayByLength(no2, 4, 2);
            tagId2 = Utils.GetByteArrayByLength(no2, 0, 4);
        }

        childContent = Utils.ByteArrayCopy(childContent, Utils.hexStr2Bytes(pcCode));
        childContent = Utils.ByteArrayCopy(childContent, plate);
        childContent = Utils.ByteArrayCopy(childContent, tag1);
        childContent = Utils.ByteArrayCopy(childContent, tagId1);
        childContent = Utils.ByteArrayCopy(childContent, tag2);
        childContent = Utils.ByteArrayCopy(childContent, tagId2);
        byte[] size = Utils.shortToByte((short) childContent.length);
        content = Utils.ByteArrayCopy(content, mData.controlCommand);
        content = Utils.ByteArrayCopy(content, size);
        content = Utils.ByteArrayCopy(content, childContent);
        requestData = Utils.ByteArrayCopy(requestData, content);
        // }
        Utils.bytesToHexString(requestData);
        return requestData;
    }

}

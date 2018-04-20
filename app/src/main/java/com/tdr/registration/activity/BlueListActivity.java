package com.tdr.registration.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import com.tdr.registration.R;
import com.tdr.registration.adapter.BlueListViewAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.gprinter.Util;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdr.registration.util.VehiclesStorageUtils.clearData;

/**
 * 蓝牙打印机列表
 */
public class BlueListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final static String DEBUG_TAG = "SamleApp";
    public static final String ACTION_CONNECT_STATUS = "action.connect.status";
    private static final int INTENT_PORT_SETTINGS = 0;
    private BlueListViewAdapter mListViewAdapter = null;
    private List<Map<String, Object>> mList = null;
    private PortParameters mPortParam[] = new PortParameters[GpPrintService.MAX_PRINTER_CNT];
    private int mPrinterId = 0;
    private GpService mGpService;
    private PrinterServiceConnection conn = null;
    private Activity mActivity;
    private ZProgressHUD mProgressHUD;

    public class PrinterSeial {
        static final int GPIRNTER001 = 0;
        static final int GPIRNTER002 = 1;
        static final int GPIRNTER003 = 2;
        static final int GPIRNTER004 = 3;
        static final int GPIRNTER005 = 4;
        static final int GPIRNTER006 = 5;
    }

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.i(DEBUG_TAG, "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }

    ;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e(DEBUG_TAG, "onResume");
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Log.i(DEBUG_TAG, "connection");
        Intent intent = new Intent("com.gprinter.aidl.GpPrintService");
        intent.setPackage(BlueListActivity.this.getPackageName());
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_list);
        Log.e(DEBUG_TAG, "onCreate ");
        mActivity=this;
        initPortParam();
        initView();
        registerBroadcast();
        connection();
    }


    private void initPortParam() {
        Intent intent = getIntent();
        boolean[] state = intent.getBooleanArrayExtra(Constants.CONNECT_STATUS);
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            PortParamDataBase database = new PortParamDataBase(this);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            mPortParam[i].setPortOpenState(state[i]);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Log.e(DEBUG_TAG, "onDestroy ");
        super.onDestroy();
        this.unregisterReceiver(PrinterStatusBroadcastReceiver);
        if (conn != null) {
            unbindService(conn); // unBindService
        }
    }

    private TextView textDeal;

    private void initView() {
        ListView list = (ListView) findViewById(R.id.lvOperateList);
        mList = getOperateItemData();
        mListViewAdapter = new BlueListViewAdapter(this, mList, mHandler);
        list.setAdapter(mListViewAdapter);
        //list.setOnItemClickListener(new TitelItemOnClickLisener());
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(new TitelItemOnLongClickLisener());
        ImageView imageBack = (ImageView) findViewById(R.id.image_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final TextView textTitle = (TextView) findViewById(R.id.text_title);
        textTitle.setText("设备列表");
        textDeal = (TextView) findViewById(R.id.text_deal);
        textDeal.setText("打印");
        textDeal.setVisibility(View.VISIBLE);
        textDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textDeal.getText().toString();
                if (text.equals("打印")) {
                    sendReceipt();
                } else if (text.equals("上传凭单")) {
                    //ActivityUtil.goActivity(BlueListActivity.this,UploadCredentialsActivity.class);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    PhotoUtils.imageFile = new File(fileDir, "credentials.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(PhotoUtils.imageFile));
                    startActivityForResult(intent, PhotoUtils.CAMERA_REQESTCODE);
                }
            }
        });
        mProgressHUD = new ZProgressHUD(BlueListActivity.this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lvOperateList:
                mPrinterId = position;
                // Intent intent = new Intent(BlueListActivity.this, PortConfigurationActivity.class);
                //startActivityForResult(intent, INTENT_PORT_SETTINGS);
                getBluetoothDevice();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        dialogShow(0,"");
        ActivityUtil.goActivityAndFinish(BlueListActivity.this, HomeActivity.class);
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(message)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, BlueListActivity.this).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    //清空图片缓存
                    SharedPreferencesUtils.put("guleCar", "");
                    SharedPreferencesUtils.put("guleBattery", "");
                    SharedPreferencesUtils.put("labelA", "");
                    SharedPreferencesUtils.put("labelB", "");
                    SharedPreferencesUtils.put("plateNum", "");
                    SharedPreferencesUtils.put("identity", "");
                    SharedPreferencesUtils.put("applicationForm", "");
                    SharedPreferencesUtils.put("invoice", "");
                    SharedPreferencesUtils.put("install", "");
                    clearData();
                    ActivityUtil.goActivityAndFinish(BlueListActivity.this, HomeActivity.class);
                }
            }).show();
        }
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECT_STATUS);
        this.registerReceiver(PrinterStatusBroadcastReceiver, filter);
    }

    private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d(DEBUG_TAG, "connect status " + type);
                if (type == GpDevice.STATE_CONNECTING) {
                    setProgressBarIndeterminateVisibility(true);
                    SetLinkButtonEnable(BlueListViewAdapter.DISABLE);
                    mPortParam[id].setPortOpenState(false);
                    Map<String, Object> map;
                    map = mList.get(id);
                    map.put(BlueListViewAdapter.STATUS, getString(R.string.connecting));
                    mList.set(id, map);
                    mListViewAdapter.notifyDataSetChanged();

                } else if (type == GpDevice.STATE_NONE) {
                    setProgressBarIndeterminateVisibility(false);
                    SetLinkButtonEnable(BlueListViewAdapter.ENABLE);
                    mPortParam[id].setPortOpenState(false);
                    Map<String, Object> map;
                    map = mList.get(id);
                    map.put(BlueListViewAdapter.STATUS, getString(R.string.connect));
                    mList.set(id, map);
                    mListViewAdapter.notifyDataSetChanged();
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    setProgressBarIndeterminateVisibility(false);
                    SetLinkButtonEnable(BlueListViewAdapter.ENABLE);
                    mPortParam[id].setPortOpenState(true);
                    Map<String, Object> map;
                    map = mList.get(id);
                    map.put(BlueListViewAdapter.STATUS, getString(R.string.cut));
                    mList.set(id, map);
                    mListViewAdapter.notifyDataSetChanged();
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    setProgressBarIndeterminateVisibility(false);
                    SetLinkButtonEnable(BlueListViewAdapter.ENABLE);
                    messageBox("Please use Gprinter!");
                }
            }
        }
    };

    private String getPortParamInfoString(PortParameters Param) {
        String info = new String();
        //info = getString(R.string.port);
        int type = Param.getPortType();
        Log.d(DEBUG_TAG, "Param.getPortType() " + type);
        if (type == PortParameters.BLUETOOTH) {
            //info += getString(R.string.bluetooth);
            //info += "  " + getString(R.string.address);
            info += Param.getBluetoothAddr();
        } else if (type == PortParameters.USB) {
            // info += getString(R.string.usb);
            //info += "  " + getString(R.string.address);
            info += Param.getUsbDeviceName();
        } else if (type == PortParameters.ETHERNET) {
            // info += getString(R.string.ethernet);
            //info += "  " + getString(R.string.ip_address);
            info += Param.getIpAddr();
            // info += "  " + getString(R.string.port_number);
            info += Param.getPortNumber();
        } else {
            //info = getString(R.string.init_port_info);
        }

        return info;
    }

    void SetPortParamToView(PortParameters Param) {
        Map<String, Object> map;
        map = mList.get(mPrinterId);
        String info = getPortParamInfoString(Param);
        map.put(BlueListViewAdapter.INFO, info);
        mList.set(mPrinterId, map);
        mListViewAdapter.notifyDataSetChanged();
    }

    void SetLinkButtonEnable(String s) {
        Map<String, Object> map;
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            map = mList.get(i);
            map.put(BlueListViewAdapter.BT_ENABLE, s);
            mList.set(i, map);
        }
        mListViewAdapter.notifyDataSetChanged();
    }

    private List<Map<String, Object>> getOperateItemData() {
        int[] PrinterID = new int[]{R.string.gprinter001, R.string.gprinter002, R.string.gprinter003,
                R.string.gprinter004, R.string.gprinter005};
        int[] PrinterImage = new int[]{R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer,
                R.drawable.ic_printer, R.drawable.ic_printer};
        Map<String, Object> map;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            map = new HashMap<String, Object>();
            map.put(BlueListViewAdapter.IMG, PrinterImage[i]);
            map.put(BlueListViewAdapter.TITEL, getString(PrinterID[i]));
            if (mPortParam[i].getPortOpenState() == false)
                map.put(BlueListViewAdapter.STATUS, getString(R.string.connect));
            else
                map.put(BlueListViewAdapter.STATUS, getString(R.string.cut));
            String str = getPortParamInfoString(mPortParam[i]);
            map.put(BlueListViewAdapter.INFO, str);
            map.put(BlueListViewAdapter.BT_ENABLE, "enable");
            list.add(map);
        }
        return list;
    }

    class TitelItemOnLongClickLisener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            Log.d(DEBUG_TAG, "TitelItemOnLongClickLisener " + arg2);
            Intent intent = new Intent(GpPrintService.ACTION_PRINT_TESTPAGE);
            intent.putExtra(GpPrintService.PRINTER_ID, arg2);
            sendBroadcast(intent);
            return true;
        }
    }

    class TitelItemOnClickLisener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub]
            mPrinterId = arg2;
            // Intent intent = new Intent(BlueListActivity.this, PortConfigurationActivity.class);
            //startActivityForResult(intent, INTENT_PORT_SETTINGS);
            getBluetoothDevice();
        }
    }

    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 3;
    public static final int REQUEST_USB_DEVICE = 4;

    public void getBluetoothDevice() {
        // Get local Bluetooth adapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (bluetoothAdapter == null) {
            messageBox("Bluetooth is not supported by the device");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                Intent intent = new Intent(BlueListActivity.this, BluetoothDeviceActivity.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
            }
        }
    }

    void connectOrDisConnectToDevice(int PrinterId) {
        mPrinterId = PrinterId;
        int rel = 0;
        if (mPortParam[PrinterId].getPortOpenState() == false) {
            if (CheckPortParamters(mPortParam[PrinterId])) {
                switch (mPortParam[PrinterId].getPortType()) {
                    case PortParameters.USB:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(),
                                    mPortParam[PrinterId].getUsbDeviceName(), 0);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.ETHERNET:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(),
                                    mPortParam[PrinterId].getIpAddr(), mPortParam[PrinterId].getPortNumber());
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.BLUETOOTH:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(),
                                    mPortParam[PrinterId].getBluetoothAddr(), 0);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                        mPortParam[PrinterId].setPortOpenState(true);
                        Map<String, Object> map;
                        map = mList.get(PrinterId);
                        map.put(BlueListViewAdapter.STATUS, getString(R.string.cut));
                        mList.set(PrinterId, map);
                        mListViewAdapter.notifyDataSetChanged();
                    } else {
                        messageBox(GpCom.getErrorText(r));
                    }
                }
            } else {
                messageBox(getString(R.string.port_parameters_wrong));
            }
        } else {
            Log.d(DEBUG_TAG, "DisconnectToDevice ");
            setProgressBarIndeterminateVisibility(true);
            SetLinkButtonEnable(BlueListViewAdapter.DISABLE);
            Map<String, Object> map;
            map = mList.get(PrinterId);
            map.put(BlueListViewAdapter.STATUS, getString(R.string.cutting));
            mList.set(PrinterId, map);
            mListViewAdapter.notifyDataSetChanged();
            try {
                mGpService.closePort(PrinterId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BlueListViewAdapter.MESSAGE_CONNECT:
                    connectOrDisConnectToDevice(msg.arg1);
            }
            super.handleMessage(msg);
        }
    };

    Boolean CheckPortParamters(PortParameters param) {
        boolean rel = false;
        int type = param.getPortType();
        if (type == PortParameters.BLUETOOTH) {
            if (!param.getBluetoothAddr().equals("")) {
                rel = true;
            }
        } else if (type == PortParameters.ETHERNET) {
            if ((!param.getIpAddr().equals("")) && (param.getPortNumber() != 0)) {
                rel = true;
            }
        } else if (type == PortParameters.USB) {
            if (!param.getUsbDeviceName().equals("")) {
                rel = true;
            }
        }
        return rel;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.d(DEBUG_TAG, "requestCode" + requestCode + '\n' + "resultCode" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONNECT_DEVICE) {
            // getIP settings info from IP settings dialog
            if (resultCode == RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle = data.getExtras();
                Log.d(DEBUG_TAG, "PrinterId " + mPrinterId);
                int param = bundle.getInt(GpPrintService.PORT_TYPE);
                mPortParam[mPrinterId].setPortType(param);
                Log.d(DEBUG_TAG, "PortType " + param);
                String str = bundle.getString(GpPrintService.IP_ADDR);
                mPortParam[mPrinterId].setIpAddr(str);
                Log.d(DEBUG_TAG, "IP addr " + str);
                param = bundle.getInt(GpPrintService.PORT_NUMBER);
                mPortParam[mPrinterId].setPortNumber(param);
                Log.d(DEBUG_TAG, "PortNumber " + param);
                str = bundle.getString(GpPrintService.BLUETOOT_ADDR);
                mPortParam[mPrinterId].setBluetoothAddr(str);
                Log.d(DEBUG_TAG, "BluetoothAddr " + str);
                str = bundle.getString(GpPrintService.USB_DEVICE_NAME);
                mPortParam[mPrinterId].setUsbDeviceName(str);
                Log.d(DEBUG_TAG, "USBDeviceName " + str);
                SetPortParamToView(mPortParam[mPrinterId]);
                if (CheckPortParamters(mPortParam[mPrinterId])) {
                    PortParamDataBase database = new PortParamDataBase(this);
                    database.deleteDataBase("" + mPrinterId);
                    database.insertPortParam(mPrinterId, mPortParam[mPrinterId]);
                } else {
                    messageBox(getString(R.string.port_parameters_wrong));
                }

            } else {
                messageBox(getString(R.string.port_parameters_is_not_save));
            }
        } else if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                int degree = PhotoUtils.readPictureDegree();
                Bitmap bitmap = PhotoUtils.rotaingImageView(degree,
                        PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile));
                String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
                uploadVoucher(photoStr);
            }
        }
    }

    private void uploadVoucher(String photoStr) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ECID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ECID));
            jsonObject.put("VOUCHERFile", photoStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_UPLOADVOUCHER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int errorCode = object.getInt("ErrorCode");
                        String data = object.getString("Data");
                        if (errorCode == 0) {
                            Utils.myToast(BlueListActivity.this, data);
                            SharedPreferencesUtils.put("guleCar", "");
                            SharedPreferencesUtils.put("guleBattery", "");
                            SharedPreferencesUtils.put("labelA", "");
                            SharedPreferencesUtils.put("labelB", "");
                            SharedPreferencesUtils.put("plateNum", "");
                            SharedPreferencesUtils.put("identity", "");
                            SharedPreferencesUtils.put("applicationForm", "");
                            SharedPreferencesUtils.put("invoice", "");
                            clearData();
                            //dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER) + "  电动车信息上传成功！")
                            ActivityUtil.goActivityAndFinish(BlueListActivity.this, HomeActivity.class);
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(BlueListActivity.this, data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(BlueListActivity.this, LoginActivity.class);
                        } else {
                            Utils.myToast(BlueListActivity.this, data);
                            mProgressHUD.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        Utils.myToast(BlueListActivity.this, "JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.myToast(BlueListActivity.this, "获取数据超时，请检查网络连接");
                }
            }
        });
    }

    private void messageBox(String err) {
        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
    }

    void sendReceipt() {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("昆明市公安机关\n二轮电动车\n防盗装置安装凭单\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("证件号码：\n"); // 打印文字
        esc.addText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY) + "\n"); // 打印文字
        //esc.addText("330302198602010305\n");

        esc.addText("车主姓名：\n");
        esc.addText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME) + "\n");
        //esc.addText("张三\n");

        esc.addText("联系方式：\n");
        esc.addText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1) + "\n");
        //esc.addText("12345678902\n");

        esc.addText("车辆品牌：\n");
        esc.addText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME) + "\n");
        //esc.addText("爱玛\n");

        esc.addText("车牌：\n");
        esc.addText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER) + "\n");
        //esc.addText("0123456\n");

        esc.addText("车架号：\n");
        esc.addText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO) + "\n");
        //esc.addText("ssafdafsafwe\n");

        esc.addText("电机号：\n");
        //esc.addText("asdfasfsdaf\n");
        esc.addText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO) + "\n");

        esc.addText("----------------------------\n");
        esc.addText("车主签名：\n");
        esc.addText("\n");
        esc.addText("\n");
        esc.addText("----------------------------\n");

        esc.addText("声明：\n");
        esc.addText("1、此次防盗登记不涉及电动车在交通管理方面的车辆性质认定。\n2、请在离开防盗装置安装点前请仔细检查车辆状态，一旦离开出现车辆损坏的，防盗装置安装点概不负责。");
        esc.addText("----------------------------\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addText("昆明联安物联网科技有限公司\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addText("技术支持\n");
        esc.addPrintAndFeedLines((byte) 8);

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(0, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            } else {
                textDeal.setText("上传凭单");
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

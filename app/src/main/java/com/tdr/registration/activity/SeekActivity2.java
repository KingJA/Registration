package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ShowPhotoAdapter;
import com.tdr.registration.base.App;
import com.tdr.registration.data.PacketData;
import com.tdr.registration.model.BlackCarModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.BLE_Util;
import com.tdr.registration.util.BleInterface.BleNotifyListener;
import com.tdr.registration.util.BleInterface.ConnectBleListener;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.util.onBleStateListener;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 单辆车子稽查
 */
@ContentView(R.layout.activity_seek)
public class SeekActivity2 extends Activity implements OnClickListener, ConnectBleListener, BleNotifyListener, onBleStateListener {

    @ViewInject(R.id.image_back)
    ImageView imageBack;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.text_deal)
    TextView textDeal;

    @ViewInject(R.id.VP_photo)
    ViewPager VP_photo;

    @ViewInject(R.id.IV_vpbkg)
    ImageView IV_vpbkg;
    //    @ViewInject(R.id.image_circlelogo)
//    CircleImageView imageCirclelogo;
    @ViewInject(R.id.relative_imagecircle)
    RelativeLayout relativeImagecircle;
    @ViewInject(R.id.text_platemun)
    TextView textPlatemun;
    @ViewInject(R.id.LL_Signal1)
    LinearLayout LL_Signal1;
    @ViewInject(R.id.TV_Signal1)
    TextView TV_Signal1;
    @ViewInject(R.id.LL_Signal2)
    LinearLayout LL_Signal2;
    @ViewInject(R.id.TV_Signal2)
    TextView TV_Signal2;

    @ViewInject(R.id.text_brand)
    TextView textBrand;
    @ViewInject(R.id.relative_titlebg)
    RelativeLayout relativeTitlebg;

    @ViewInject(R.id.image_checked)
    ImageView imageChecked;
    @ViewInject(R.id.relative_checked)
    RelativeLayout relativeChecked;
    @ViewInject(R.id.layout_title)
    RelativeLayout relativeTitle;

    @ViewInject(R.id.TV_DeviceState)
    TextView TV_DeviceState;
    @ViewInject(R.id.LL_Voice)
    LinearLayout LL_Voice;

    @ViewInject(R.id.RL_ShowPhoto)
    RelativeLayout RL_ShowPhoto;
    @ViewInject(R.id.VP_ShowPhoto)
    ViewPager VP_ShowPhoto;

    @ViewInject(R.id.IV_back)
    ImageView IV_back;


    private Gson mGson;
    private ElectricCarModel electricCarModel;
    private String plateNumber = "";
    private Activity mActivity;
    private BlackCarModel blackCar;

    private BLE_Util BLE = BLE_Util.instance;
    private PacketData mData = BLE_Util.instance.getmData();
    private String BleName;
    private String BleMAC;
    private App BA;
    private String bluetoothReglar;
    private byte[] SendMSG;
    private int Circular_W = R.drawable.shape_circular2;
    private int Circular_G = R.drawable.shape_circular3;
    private int Circular_B = R.drawable.shape_circular4;
    private ZProgressHUD mProgressHUD;
    private final int signal = 200;
    private int Now_Signal = 0;
    private boolean OnLine = false;//是否连接设备
    private boolean isVoice = true;//声音开关
    private boolean isDisplay = true;//当前页面是否是最顶层
    private boolean isPlay = false;//是否播放
    private boolean isPrepare = false;//是否播放

    private ShowPhotoAdapter myadapter;
    private ShowPhotoAdapter myadapter2;

    private List<View> viewList = new ArrayList<View>();
    private List<View> viewList2 = new ArrayList<View>();
    private int THEFTNO = 0;
    private static final int BeepPlay = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case BeepPlay:
//                    if(MP.isPlaying()){
//                        MP.stop();
//                    }
                    mLog.e("-------------1-------------");
                    MP.start();

                    break;

                default:
                    break;
            }
        }
    };
    private MediaPlayer MP;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initView();
        queryPlateNumber(plateNumber);
        isBle();

    }

    private void initView() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
        textTitle.setText("稽查");
        imageBack.setOnClickListener(this);
        relativeChecked.setOnClickListener(this);
        LL_Voice.setOnClickListener(this);
        IV_back.setOnClickListener(this);
//        imageCirclelogo.setOnClickListener(this);
        blackCar = (BlackCarModel) getIntent().getExtras().getSerializable("BlackCar");
        mLog.e("blackCar=" + blackCar.getPLATENUMBER());
        plateNumber = blackCar.getPLATENUMBER();
        textPlatemun.setText(plateNumber);
        mActivity = this;
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

        BA = ((App) mActivity.getApplicationContext());
        bluetoothReglar = (String) SharedPreferencesUtils.get("key", "");
        setSignal(0, 0);
        setSignal(0, 1);
        mActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setMediaPlay();

    }

    private void setMediaPlay(){
        MP = MediaPlayer.create(this, R.raw.beep);

//        AssetFileDescriptor file = mActivity.getResources().openRawResourceFd(R.raw.beep);
//        try {
//            MP = new MediaPlayer();
//            MP.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            MP.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
//            file.close();
//            MP.prepare();
        MP.setVolume(1, 1);
        MP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
                mLog.e("------------isPlay--------------");
                if (isPlay&&isVoice&&OnLine&&isDisplay) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(MP!=null){
                                MP.start();
                            }
                        }
                    }, Now_Signal);
                }
            }
        });
        MP.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepare=true;
            }
        });
        MP.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });
//        } catch (IOException ioe) {
//            MP = null;
//        }

    }
    private void setSignal(int Signal, int Type) {
        if (Signal > 5) {
            Signal = 5;
        }
        LinearLayout LL;
        if (Type == 0) {
            LL = LL_Signal1;
        } else {
            LL = LL_Signal2;
        }
        if (LL.getChildCount() > 0) {
            //初始化信号强度
            for (int i = 0; i < LL.getChildCount(); i++) {
                ((ImageView) LL.getChildAt(i)).setBackgroundResource(Circular_W);
            }
            int bkg = Circular_W;
            //根据不同的标签类型改变颜色
            switch (Type) {
                case 0:
                    bkg = Circular_G;
                    break;
                case 1:
                    bkg = Circular_B;
                    break;
            }
            //设置信号强度
            for (int i = 0; i < Signal; i++) {
                try {
                    ((ImageView) LL.getChildAt(i)).setBackgroundResource(bkg);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //初始化信号强度
            for (int i = 0; i < 5; i++) {
                ImageView IV = new ImageView(this);
                LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(AppUtil.dp2px(7), AppUtil.dp2px(7));
                LP.setMargins(6, 0, 6, 0);
                IV.setLayoutParams(LP);
                IV.setBackgroundResource(Circular_W);
                LL.addView(IV);
            }
        }
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
        } else {
            BA.setBSL(this);
            if (BA.isBleStatus() && !BA.isBleConnectStatus()) {
                BLE.ConnectBle(this);
            }
            if (BA.isBleStatus() && BA.isBleConnectStatus()) {
                updateUI(true);
                BLE.Notify(BLE.getMAC(), BLE.getBGP(), this);
            }
            BleName = BLE.getName();
            BleMAC = BLE.getMAC();
        }
    }


    private void updateUI(boolean b) {
        OnLine = b;
        if (b) {
            mLog.e("updateUI: " + "连接成功");
            relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorTitle));
            relativeTitlebg.setBackgroundColor(getResources().getColor(R.color.colorTitle));
            IV_vpbkg.setBackgroundResource(R.mipmap.vp_show_photo_b);
        } else {
            Utils.showToast("连接失败，请重新选择设备");
            relativeTitle.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));
            relativeTitlebg.setBackgroundColor(getResources().getColor(R.color.colorUnConnected));
            IV_vpbkg.setBackgroundResource(R.mipmap.vp_show_photo_y);
            isPlay=false;
            setTHEFTNO(0,0, "");
            setTHEFTNO(1,0, "");
        }
    }

    private void queryPlateNumber(String platenumber) {
//        mHandler.removeCallbacks(RUN);
        TV_DeviceState.setText("黑车查询中");
        isPlay = false;
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("W_CPH", platenumber);
        map.put("W_FDJH", "");
        map.put("W_CJH", "");
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKSTOLENVEHICLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                Utils.LOGE("Pan", "车辆信息=" + result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            int type = jsonObject.getInt("Type");
                            if (type == 1) {
                                JSONObject json = jsonObject.getJSONObject("ElectricCar");
                                electricCarModel = mGson.fromJson(json.toString(), new TypeToken<ElectricCarModel>() {
                                }.getType());
                                setData();
                                if (BA.isBleConnectStatus()) {
                                    SendMSG = BLE.SendBlackCar(blackCar);
                                    BLE.Write(mData.sendPackageBegin(51, SendMSG));
                                    TV_DeviceState.setText("下发车辆数据");
                                }
                            } else {
                                Utils.showToast("该车辆非布控车辆，不可查缉");
                            }
                        } else {
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }

        });

    }

    private void setData() {
        plateNumber = electricCarModel.getPlateNumber();
        textPlatemun.setText(plateNumber);
        textBrand.setText(electricCarModel.getVehicleBrandName());
        blackCar.setPLATENUMBER(electricCarModel.getPlateNumber());
        blackCar.setTHEFTNO(electricCarModel.getTHEFTNO());
        blackCar.setTHEFTNO2(electricCarModel.getTHEFTNO2());
        setSignal(0, 0);
        setSignal(0, 1);
        viewList = new ArrayList<>();
        viewList2 = new ArrayList<>();

        myadapter = new ShowPhotoAdapter(viewList);
        myadapter.setOnItemclickListener(new ShowPhotoAdapter.onClickListener() {
            @Override
            public void onItemClickListener() {
                RL_ShowPhoto.setVisibility(View.VISIBLE);
            }
        });

        myadapter2 = new ShowPhotoAdapter(viewList2);
        VP_photo.setAdapter(myadapter);
        VP_ShowPhoto.setAdapter(myadapter2);



        for (PhotoModel photoModel : electricCarModel.getPhotoListFile()) {
            mLog.e(photoModel.getPhoto() == null ? "photoModel.getPhoto()==null" : "photoModel.getPhoto()!=null");
            if (photoModel.getPhoto() != null && !photoModel.getPhoto().equals("")) {
                initImages(photoModel.getPhoto());
            }
        }

    }

    private void initImages(String paramString) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("pictureGUID", paramString);
        mLog.e("initImages" + localHashMap.toString());
        mLog.e("apiUrl=" + (String) SharedPreferencesUtils.get("apiUrl", ""));
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPICTURE, localHashMap, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String result) {
                mLog.e("获取照片" + result);
                if (result != null) {
                    Bitmap bp = Utils.stringtoBitmap(result);
                    Bitmap bm = Bitmap.createScaledBitmap(bp, 100, 100, true);


                    Drawable drawable = new BitmapDrawable(bm);
                    ImageView IV = new ImageView(mActivity);
                    IV.setBackground(drawable);
                    viewList.add(IV);
                    myadapter.Update(viewList);
                    myadapter.notifyDataSetChanged();

//                    Bitmap BP= bp.copy(Bitmap.Config.ARGB_8888, true);
                    int Proportion = PhotoUtils.calculateInSampleSize(mActivity,bp);


                    Drawable drawable2 = new BitmapDrawable(bp);
                    RelativeLayout RL = new RelativeLayout(mActivity);
                    RL.setGravity(Gravity.CENTER);
                    RL.setBackgroundColor(Color.parseColor("#000000"));
                    ImageView IV2 = new ImageView(mActivity);
                    IV2.setBackground(drawable2);
                    IV2.setScaleType(ImageView.ScaleType.FIT_XY);
                    RelativeLayout.LayoutParams RLLP = new RelativeLayout.LayoutParams(bp.getWidth() * Proportion, bp.getHeight() * Proportion);
                    RL.addView(IV2, RLLP);
                    viewList2.add(RL);
                    myadapter2.Update(viewList2);
                    myadapter2.notifyDataSetChanged();
                }
            }
        });
    }



    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;
    private int theftno;
    private boolean Vioce;

    private void dialogShow(int type, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        if (type == 0) {
            View V = mActivity.getLayoutInflater().inflate(R.layout.dialog_voice, null);
            RadioGroup RG_SelectTHEFTNO = (RadioGroup) V.findViewById(R.id.RG_SelectTHEFTNO);
            CheckBox CB_Voice = (CheckBox) V.findViewById(R.id.CB_Voice);
            theftno = THEFTNO;
            Vioce = isVoice;
            if (THEFTNO == 0) {
                ((RadioButton) V.findViewById(R.id.RB_THEFTNO)).setChecked(true);
            } else {
                ((RadioButton) V.findViewById(R.id.RB_THEFTNO2)).setChecked(true);
            }
            if (isVoice) {
                CB_Voice.setChecked(true);
            } else {
                CB_Voice.setChecked(false);
            }

            RG_SelectTHEFTNO.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.RB_THEFTNO) {
                        mLog.e("THEFTNO");
                        theftno = 0;
                    } else {
                        mLog.e("THEFTNO2");
                        theftno = 1;
                    }
                }
            });
            CB_Voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Vioce = isChecked;
                }
            });
            dialogBuilder.setCustomView(V, mActivity)
                    .withTitle(message)
                    .withTitleColor("#333333")
                    .isCancelableOnTouchOutside(false)
                    .withEffect(effectstype)
                    .withButton1Text("取消")
                    .withButton2Text("确定")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(THEFTNO!=theftno){
                        THEFTNO = theftno;
                        isPlay=false;
                    }
                    isVoice = Vioce;
                    if(isPlay){
                        MP.start();
                    }
                    dialogBuilder.dismiss();
                }
            }).show();
        } else if (type == 1) {

            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View identityView = mInflater.inflate(R.layout.layout_query_identity, null);
            TextView textName = (TextView) identityView.findViewById(R.id.text_name);
            textName.setText("车牌号：");
            final EditText editQueryIdentity = (EditText) identityView
                    .findViewById(R.id.edit_queryIdentity);
            editQueryIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(identityView, mActivity);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.IV_back:
                onBackPressed();
                break;
            case R.id.relative_checked:
                dialogShow(1, "车辆稽查");
                break;
            case R.id.LL_Voice:
                dialogShow(0, "设置");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (RL_ShowPhoto.getVisibility() == View.VISIBLE) {
            RL_ShowPhoto.setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BLE.unNotify();
        BA.setBSL(null);
        MP.stop();
        MP.release();
        MP = null;//回收资源
    }

    @Override
    public void onResponse(int code, BleGattProfile data) {
        if (code == 0) {
            BLE.setName(BleName);
            BLE.setMAC(BleMAC);
            BLE.Notify(BLE.getMAC(), data, this);
            updateUI(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isDisplay = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDisplay = true;

    }

    @Override
    public void onNotify(byte[] value) {
        Notify(value);
    }

    private void Notify(byte[] value) {
        byte[] Type = Utils.GetByteArrayByLength(value, 1, 1);
        if (!Utils.checkByte(Type, mData.uploadHeartBeat)) {
            mLog.BLE("蓝牙数据=" + Utils.bytesToHexString(value));
        }
        if (Utils.checkByte(Type, mData.queryKeyCommand)) {//检查key
            String key = mData.parsingKey(Utils.deleteByte(value, 2));
            mLog.BLE("key=" + Utils.bytesToHexString(value));
            if (!bluetoothReglar.equals(key)) {//如果key不一致，则先下发配置
                SendMSG = mData.packageSendContent();
                mLog.BLE("配置内容=" + Utils.bytesToHexString(SendMSG));
                BLE.Write(mData.sendPackageBegin(51, SendMSG));
            }
        } else if (Utils.checkByte(Type, mData.packagingStart)
                || Utils.checkByte(Type, mData.packagingComplete)
                || Utils.checkByte(Type, mData.sendKeyCommand)) {//打包下发
            String result = Utils.bytesToHexString(Utils.GetByteArrayByLength(Utils.deleteByte(value, 2), 0, 1));
            mLog.BLE(result.equals("01") ? "指令下发失败" : "指令下发成功");
            if (result.equals("01")) {
                SystemClock.sleep(200);
                SendMSG = BLE.SendBlackCar(blackCar);
                BLE.Write(mData.sendPackageBegin(51, SendMSG));
            } else {
                if (SendMSG != null) {
                    byte[] sendPackage = mData.sendPackaging(SendMSG);
                    mLog.BLE("要下发的数据：" + Utils.bytesToHexString(mData.sendPackaging(SendMSG)));
                    int length = (int) Math.ceil((short) sendPackage.length / 20);//总共需要发送的包数
                    for (int i = 0; i < length; i++) {
                        BLE.Write(Utils.GetByteArrayByLength(sendPackage, i * 20, 20));
                        mLog.BLE("第" + i + "个包：" + Utils.bytesToHexString(Utils.GetByteArrayByLength(sendPackage, i * 20, 20)));
                        SystemClock.sleep(60);//下发间隔60毫秒
                    }
                    SystemClock.sleep(60);
                    BLE.Write(mData.sendPackageBegin(52, SendMSG));
                    mLog.BLE("完成下发：" + Utils.bytesToHexString(mData.sendPackageBegin(52, SendMSG)));
                    SendMSG = null;
                    TV_DeviceState.setText("开始搜索");
                }
            }
        } else if (Utils.checkByte(Type, mData.uploadState) || Utils.checkByte(Type, mData.uploadStateSecond)) {
            mLog.BLE("标签心跳=" + Utils.bytesToHexString(value));
            if(isPrepare){
                UpDataUI(mData.searchState(Utils.deleteByte(value, 2)));
            }
        }
    }

    private void UpDataUI(final List<String> data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int Sig1 = Integer.parseInt(data.get(3));
                int Sig2 = Integer.parseInt(data.get(6));
//                Sig1 = Utils.getRandom(0, 100);
//                Sig2 = Utils.getRandom(0, 100);
                setTHEFTNO(0, Sig1, data.get(4));
                setTHEFTNO(1, Sig2, data.get(4));
                int Signal;
                int S;
                int SIGNAL = 0;
                if (isVoice) {
                    if (THEFTNO == 0) {
//                        Signal =Integer.parseInt(data.get(3));
                        Signal = Sig1;

                    } else {
//                        Signal =Integer.parseInt(data.get(6));
                        Signal = Sig2;
                    }
                    S = Signal / 10;
                    if (Signal % 10 > 5) {
                        S++;
                    }
                    mLog.e(S + "Signal：" + Signal);
                    if (Signal > 0 && S <= 10) {
                        SIGNAL = (10 - S) * signal;
                        if (isDisplay && OnLine) {
                            if (Now_Signal != SIGNAL) {
                                Now_Signal = SIGNAL;
                                if (Now_Signal < 0) {
                                    Now_Signal = 0;
                                }
                                if(THEFTNO==0&&Sig1>0){
                                    if (!isPlay) {
                                        isPlay = true;
                                        mLog.e("BBBBBB","================标签1==================");
                                        MP.start();
                                    }
                                }else if(THEFTNO==1&&Sig2>0){
                                    if (!isPlay) {
                                        isPlay = true;
                                        mLog.e("BBBBBB","=================标签2=================");
                                        MP.start();
                                    }
                                }

                            }
                        }
                    }

                }
                mLog.e(SIGNAL + "标签：" + THEFTNO + "    信号间隔" + SIGNAL + "搜索状态：" + data.get(4));

            }
        });
    }

    private void setTHEFTNO(int THEFTNO_Type, int Signal, String State) {
        int S = Signal / 10;
        if (Signal % 10 > 5) {
            S++;
        }
        mLog.e(S + "标签" + THEFTNO_Type + "信号强度：" + Signal);
        if (THEFTNO_Type == 0) {
            setSignal(S / 2, THEFTNO_Type);
            TV_Signal1.setText(Signal+"");
        } else {
            setSignal(S / 2, THEFTNO_Type);
            TV_Signal2.setText(Signal+"");
        }

        if (State.equals("0")) {
            TV_DeviceState.setText("搜索中");
        } else if (State.equals("1")) {
            TV_DeviceState.setText("锁定中");
        } else if (State.equals("2")) {
            TV_DeviceState.setText("已锁定");
        }
    }

    @Override
    public void BleState(boolean State) {
        updateUI(State);
        if (State && !BA.isBleConnectStatus()) {
            BLE.ConnectBle(this);
            Utils.showToast("尝试重新连接蓝牙");
        }

    }

    @Override
    public void BleConnectState(boolean State) {
        updateUI(State);
        if (!State && BA.isBleStatus()) {
            BLE.ConnectBle(this);
            Utils.showToast("尝试重新连接蓝牙");
        }
    }


}
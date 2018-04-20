package com.tdr.registration.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.AreaModel;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.AppManager;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;



import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆布控
 */
public class VehicleMonitorActivity extends BaseActivity {
    private static final String TAG = "VehicleMonitorActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.rb_monitorStolen)
    RadioButton rbMonitorStolen;
    @BindView(R.id.rb_monitorOthers)
    RadioButton rbMonitorOthers;
    @BindView(R.id.group_monitorType)
    RadioGroup groupMonitorType;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.edit_dispatchedName)
    EditText editDispatchedName;
    @BindView(R.id.edit_dispatchedPhone)
    EditText editDispatchedPhone;
    @BindView(R.id.rb_noticeNone)
    RadioButton rbNoticeNone;
    @BindView(R.id.rb_noticeOne)
    RadioButton rbNoticeOne;
    @BindView(R.id.rb_noticeRepeat)
    RadioButton rbNoticeRepeat;
    @BindView(R.id.group_noticeTimes)
    RadioGroup groupNoticeTimes;
    @BindView(R.id.text_alarmTime)
    TextView textAlarmTime;
    @BindView(R.id.edit_noticePhone)
    EditText editNoticePhone;
    @BindView(R.id.edit_alarmName)
    EditText editAlarmName;
    @BindView(R.id.edit_alarmPhone)
    EditText editAlarmPhone;
    @BindView(R.id.text_alarmStartTime)
    TextView textAlarmStartTime;
    @BindView(R.id.text_alarmEndTime)
    TextView textAlarmEndTime;
    @BindView(R.id.edit_stolenAddress)
    EditText editStolenAddress;
    @BindView(R.id.text_responsibilityUnit)
    TextView textResponsibilityUnit;
    @BindView(R.id.edit_case)
    EditText editCase;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.linear_monitorStolen)
    LinearLayout linearMonitorStolen;
    @BindView(R.id.text_dispatchedTime)
    TextView textDispatchedTime;
    @BindView(R.id.edit_noticePhoneOthers)
    EditText editNoticePhoneOthers;
    @BindView(R.id.text_responsibilityUnitOthers)
    TextView textResponsibilityUnitOthers;
    @BindView(R.id.linear_monitorOthers)
    LinearLayout linearMonitorOthers;

    private Context mContext;
    private Gson mGson;
    private Intent intent;
    private DbManager db;
    private ZProgressHUD mProgressHUD;

    private List<AreaModel> areaModels = new ArrayList<>();

    private final static int AREA_CODE = 2016;//被盗，责任单位回调
    private final static int UNIT_CODE = 0316;//其他，责任单位回调

    private String monitorType = "1";//布控类型，1被盗，2其他
    private String noticeMode = "";//短信发送次数，0不发送，1发送一次，2重复发送
    private String dutyUnit = "";//市局级别选择后的单位ID
    private String roleLevel = "";//帐号所处级别

    private TimePickerView alarmTime;
    private TimePickerView dispatchedTime;
    private TimePickerView alarmStartTime;
    private TimePickerView alarmEndTime;
    private Activity mActivity;
    private boolean CheckStartTime=false;
    private boolean CheckendTime=false;
    private boolean CheckAlarmTime=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_monitor);
        ButterKnife.bind(this);
        db = x.getDb(DBUtils.getDb());
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        intent = new Intent();
        roleLevel = (String) SharedPreferencesUtils.get("roleLevel", "");
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("车辆布控");
        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));

        groupMonitorType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbMonitorStolen.getId()) {
                    monitorType = "1";
                    linearMonitorStolen.setVisibility(View.VISIBLE);
                    linearMonitorOthers.setVisibility(View.GONE);
                } else {
                    monitorType = "2";
                    linearMonitorStolen.setVisibility(View.GONE);
                    linearMonitorOthers.setVisibility(View.VISIBLE);
                }
            }
        });

        groupNoticeTimes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbNoticeNone.getId()) {
                    noticeMode = "0";
                } else if (checkedId == rbNoticeOne.getId()) {
                    noticeMode = "1";
                } else {
                    noticeMode = "2";
                }
            }
        });

        alarmStartTime = new TimePickerView(this, TimePickerView.Type.ALL);
        alarmStartTime.setTime(new Date());
        alarmStartTime.setCyclic(false);
        alarmStartTime.setCancelable(true);
        alarmStartTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textAlarmStartTime.setText(Utils.setTime(date));
                Utils.CheckBuyTime(textAlarmStartTime.getText().toString(), new Utils.GetServerTime() {
                    @Override
                    public void ServerTime(String ST, boolean Check) {
                        CheckStartTime = Check;
                    }
                });
            }
        });

        alarmEndTime = new TimePickerView(this, TimePickerView.Type.ALL);
        alarmEndTime.setTime(new Date());
        alarmEndTime.setCyclic(false);
        alarmEndTime.setCancelable(true);
        alarmEndTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textAlarmEndTime.setText(Utils.setTime(date));
                Utils.CheckBuyTime(textAlarmEndTime.getText().toString(), new Utils.GetServerTime() {
                    @Override
                    public void ServerTime(String ST, boolean Check) {
                        CheckendTime = Check;
                    }
                });
            }
        });

        dispatchedTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        dispatchedTime.setTime(new Date());
        dispatchedTime.setCyclic(false);
        dispatchedTime.setCancelable(true);
        dispatchedTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textDispatchedTime.setText(Utils.setDate(date));
            }
        });

        alarmTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        alarmTime.setTime(new Date());
        alarmTime.setCyclic(false);
        alarmTime.setCancelable(true);
        alarmTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textAlarmTime.setText(Utils.setDate(date));
                Utils.CheckBuyTime(textAlarmTime.getText().toString(), new Utils.GetServerTime() {
                    @Override
                    public void ServerTime(String ST, boolean Check) {
                        mLog.e("Check:"+Check);
                        CheckAlarmTime = Check;
                    }
                });
            }
        });

        if (roleLevel.equals("3")) {
            textResponsibilityUnit.setClickable(false);
            textResponsibilityUnit.setEnabled(false);
            textResponsibilityUnit.setText((String) SharedPreferencesUtils.get("regionName", ""));
            dutyUnit=(String) SharedPreferencesUtils.get("regionId", "");

        }

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
    }

    private void initData() {
        try {
            areaModels = db.findAll(AreaModel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(areaModels==null){
            areaModels=new ArrayList<AreaModel>();
        }
        if (areaModels.equals("") || areaModels.size() == 0 || areaModels == null) {
            mProgressHUD.show();
            WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETXQANDPCS, null, new WebServiceUtils.WebServiceCallBack() {
                @Override
                public void callBack(String result) {
                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            List<AreaModel> areaModels = new ArrayList<AreaModel>();
                            if (errorCode == 0) {
                                areaModels = mGson.fromJson(data, new TypeToken<List<AreaModel>>() {
                                }.getType());
                                if (areaModels.size() > 0 && areaModels != null) {
                                    for (int i = 0; i < areaModels.size(); i++) {
                                        db.deleteById(BaseInfo.class, areaModels.get(i).getValue());
                                        db.save(areaModels.get(i));
                                    }
                                    mProgressHUD.dismiss();
                                } else {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                }
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
        }
    }

    @OnClick({R.id.image_back, R.id.text_alarmTime, R.id.text_dispatchedTime, R.id.text_alarmStartTime, R.id.text_alarmEndTime, R.id.text_responsibilityUnit, R.id.text_responsibilityUnitOthers, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.text_alarmTime:
                alarmTime.show();
                break;
            case R.id.text_dispatchedTime:
                dispatchedTime.show();
                break;
            case R.id.text_alarmStartTime:
                alarmStartTime.show();
                break;
            case R.id.text_alarmEndTime:
                alarmEndTime.show();
                break;

            case R.id.text_responsibilityUnit:
                intent.setClass(VehicleMonitorActivity.this, AreaActivity.class);
                startActivityForResult(intent, AREA_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.text_responsibilityUnitOthers:
                intent.setClass(VehicleMonitorActivity.this, AreaActivity.class);
                startActivityForResult(intent, UNIT_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.btn_submit:
                if (!checkData()) {
                    break;
                }
                dialogShow(1, "确认对" + editPlateNumber.getText().toString().toUpperCase().trim() + "车辆进行布控？");

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AREA_CODE) {
            if (resultCode == RESULT_OK) {
                String areaName = data.getStringExtra("areaName");
                textResponsibilityUnit.setText(areaName);
//                deployUnit = data.getStringExtra("areaValue");
                dutyUnit= data.getStringExtra("areaValue");
            }
        } else if (requestCode == UNIT_CODE) {
            if (resultCode == RESULT_OK) {
                String areaName = data.getStringExtra("areaName");
                textResponsibilityUnitOthers.setText(areaName);
//                deployUnit = data.getStringExtra("areaValue");
                dutyUnit= data.getStringExtra("areaValue");
            }
        }

    }

    private void sendMsg() {
        mProgressHUD.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DEPLOY_USER", (String) SharedPreferencesUtils.get("userId", ""));
            jsonObject.put("DEPLOY_TYPE", monitorType);//布控类型(1:被盗,2:监控)
            jsonObject.put("PlateNumber", editPlateNumber.getText().toString().toUpperCase().trim());
            jsonObject.put("ALARM_USERNAME", editDispatchedName.getText().toString().trim());//布控人姓名
            jsonObject.put("ALARM_PHONE3", editDispatchedPhone.getText().toString().trim());//布控人电话
            jsonObject.put("SEND_MODE", noticeMode);//短信发送模式:不发送:0;发送一次1;重复发送2;发送一次完成3;
            jsonObject.put("ALARM_DATE", textAlarmTime.getText().toString().trim());//报警时间
            jsonObject.put("ALARM_PHONE", editNoticePhone.getText().toString().trim());//通知电话
            jsonObject.put("ALARM_USERNAME2", editAlarmName.getText().toString().trim());//报警人姓名
            jsonObject.put("ALARM_PHONE2", editAlarmPhone.getText().toString().trim());//报警人电话
            jsonObject.put("ALARM_DATE2", textAlarmStartTime.getText().toString().trim());
            jsonObject.put("ALARM_DATE3", textAlarmEndTime.getText().toString().trim());
            jsonObject.put("ALARM_ADDRESS", editStolenAddress.getText().toString().trim());//被盗地址
            jsonObject.put("DEPLOY_UNIT",     (String) SharedPreferencesUtils.get("regionId", ""));
            jsonObject.put("ALARM_REASON", editCase.getText().toString().trim());
            jsonObject.put("DEPLOY_TIME", Utils.getNowTime());
            jsonObject.put("DEPLOY_STATUS", "0");
            jsonObject.put("DUTY_UNIT",dutyUnit );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        mLog.e("map="+map.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_DEPLOY, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            dialogShow(0, "车牌号：" + editPlateNumber.getText().toString().toUpperCase().trim() + "  电动车布控成功！");
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(VehicleMonitorActivity.this, LoginActivity.class);
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
                    .setCustomView(R.layout.custom_view, mContext).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    //清空图片缓存
                    ActivityUtil.goActivityAndFinish(VehicleMonitorActivity.this, HomeActivity.class);
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(message)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, mContext).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMsg();
                    dialogBuilder.dismiss();
                }
            }).show();
        }
    }

    private boolean checkData() {
        if (monitorType.equals("")) {
            Utils.myToast(mContext, "请选择布控类型");
            return false;
        }
        String plateNumber = editPlateNumber.getText().toString().toUpperCase().trim();
        if (plateNumber.equals("")) {
            Utils.myToast(mContext, "请输入布控车辆车牌号");
            return false;
        }
        String dispatchedName = editDispatchedName.getText().toString().trim();
        if (dispatchedName.equals("")) {
            Utils.myToast(mContext, "请输入布控人姓名");
            return false;
        }
        if (noticeMode.equals("")) {
            Utils.myToast(mContext, "请选择短信预警方式");
            return false;
        }
        if (monitorType.equals("1")) {
            String alarmTime = textAlarmTime.getText().toString().trim();
            if (alarmTime.equals("")) {
                Utils.myToast(mContext, "请选择报警时间");
                return false;
            }
            if (!CheckAlarmTime) {
                if (Utils.ServerTime == null || Utils.ServerTime.equals("")) {
                    Utils.myToast(mContext, "获取服务器时间异常。");
                } else {
                    Utils.myToast(mContext, "您选择的报警时间已超过当前时间");
                }
                return false;
            }
            String alarmStartTime = textAlarmStartTime.getText().toString().trim();
            if (alarmStartTime.equals("")) {
                Utils.myToast(mContext, "请选择车辆被盗的起始时间");
                return false;
            }
            if (!CheckStartTime) {
                if (Utils.ServerTime == null || Utils.ServerTime.equals("")) {
                    Utils.myToast(mContext, "获取服务器时间异常。");
                } else {
                    Utils.myToast(mContext, "您选择的被盗起始时间已超过当前时间");
                }
                return false;
            }
            String alarmEndTime = textAlarmEndTime.getText().toString().trim();
            if (alarmEndTime.equals("")) {
                Utils.myToast(mContext, "请选择车辆被盗的终止时间");
                return false;
            }
            if (!CheckendTime) {
                if (Utils.ServerTime == null || Utils.ServerTime.equals("")) {
                    Utils.myToast(mContext, "获取服务器时间异常。");
                } else {
                    Utils.myToast(mContext, "您选择的被盗终止时间已超过当前时间");
                }
                return false;
            }
            if(Utils.ContrastTime(alarmEndTime,alarmStartTime)){
                Utils.myToast(mContext, "选择的被盗起始时间必须早于被盗终止时间。");
                return false;
            }


            String stolenAddress = editStolenAddress.getText().toString().trim();
            if (stolenAddress.equals("")) {
                Utils.myToast(mContext, "请输入车辆被盗地点");
                return false;
            }

            String responsibilityUnit = textResponsibilityUnit.getText().toString().trim();
            if (responsibilityUnit.equals("")) {
                Utils.myToast(mContext, "请选择责任单位");
                return false;
            }
        } else if (monitorType.equals("2")) {
            String DispatchedTime = textDispatchedTime.getText().toString().trim();
            if (DispatchedTime.equals("")) {
                Utils.myToast(mContext, "请选择布控时间");
                return false;
            }
        }

        return true;
    }

    private Dialog dateDialog;// 日期选择弹出框
    private Calendar c = null;

    @Override
    protected Dialog onCreateDialog(int id) {
        if (dateDialog != null && dateDialog.isShowing())
            return dateDialog;
        switch (id) {
            case R.id.text_alarmTime:
                c = Calendar.getInstance();
                dateDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                String m = (month + 1) < 10 ? "0" + (month + 1)
                                        : "" + (month + 1);
                                String d = dayOfMonth < 10 ? "0" + dayOfMonth : ""
                                        + dayOfMonth;
                                textAlarmTime.setText(year + "-" + m + "-" + d);
                            }
                        }, c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
            case R.id.text_dispatchedTime:
                c = Calendar.getInstance();
                dateDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                String m = (month + 1) < 10 ? "0" + (month + 1)
                                        : "" + (month + 1);
                                String d = dayOfMonth < 10 ? "0" + dayOfMonth : ""
                                        + dayOfMonth;
                                textDispatchedTime.setText(year + "-" + m + "-" + d);
                            }
                        }, c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
        }
        return dateDialog;
    }

    @Override
    public void onBackPressed() {
        //finish();
        dialogShow(0, "正在布控中，是否退出编辑？");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}

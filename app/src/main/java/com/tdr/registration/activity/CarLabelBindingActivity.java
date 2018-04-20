package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tdr.registration.R;
import com.tdr.registration.adapter.LabelPhotoListAdapter;
import com.tdr.registration.gprinter.Util;
import com.tdr.registration.model.CarLabel;
import com.tdr.registration.model.LabelType;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 车牌标签变更
 */
@ContentView(R.layout.activity_car_label_change)
public class CarLabelBindingActivity extends Activity {
    @ViewInject(R.id.IV_Back)
    ImageView IV_Back;
    @ViewInject(R.id.TV_Title)
    TextView TV_Title;

    @ViewInject(R.id.text_PlateNumber)
    TextView text_PlateNumber;
    @ViewInject(R.id.text_brand)
    TextView text_brand;
    @ViewInject(R.id.text_color)
    TextView text_color;
    @ViewInject(R.id.text_owner)
    TextView text_owner;
    @ViewInject(R.id.text_phone)
    TextView text_phone;
    @ViewInject(R.id.text_identityShow)
    TextView text_identityShow;

    @ViewInject(R.id.LL_Label_Change)
    LinearLayout LL_Label_Change;
    @ViewInject(R.id.TV_OldLabelNum)
    TextView TV_OldLabelNum;
    @ViewInject(R.id.ET_NewLabelNum)
    EditText ET_NewLabelNum;


    @ViewInject(R.id.LL_LabelNum)
    LinearLayout LL_LabelNum;
    @ViewInject(R.id.ET_LabelNum)
    EditText ET_LabelNum;

    @ViewInject(R.id.RL_LabelType)
    RelativeLayout RL_LabelType;
    @ViewInject(R.id.TV_Label_Type)
    TextView TV_Label_Type;


    @ViewInject(R.id.RL_PhotoList)
    RecyclerView RL_PhotoList;

    @ViewInject(R.id.BT_Search)
    Button BT_Search;

    private Activity mActivity;
    private ArrayList<LabelPhotoListAdapter.DrawableList> drawablelist;
    private LabelPhotoListAdapter PLA;
    private CarLabel CL;
    private String InType;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private MyAdapter mAdapter;
    private List<LabelType> LT;
    private String CarRegular;
    private int SIGNTYPE = -1;
    private int Position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mActivity = this;
        mGson = new Gson();
        initview();
        initdata();
    }

    private void initview() {
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        InType = getIntent().getExtras().getString("InType");
        CL = (CarLabel) getIntent().getExtras().getSerializable("CarInfo");
        if (InType.equals("Add")) {
            TV_Title.setText("绑定新标签");
            LL_Label_Change.setVisibility(View.GONE);
        } else if (InType.equals("Change")) {
            Position = getIntent().getExtras().getInt("Position");
            TV_Title.setText("更换标签");
            LL_LabelNum.setVisibility(View.GONE);
            TV_OldLabelNum.setText(CL.getEquipments().get(Position).getORI_THEFTNO());
            RL_LabelType.setEnabled(false);
            Picasso.with(mActivity)
                    .load(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants.HTTP_GetPhotoUrl+CL.getEquipments().get(Position).getExtraInfo().getPhoto())
                    .noFade()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            drawablelist.add(new LabelPhotoListAdapter.DrawableList("0", new BitmapDrawable(bitmap)));
                            PLA.UpDate(drawablelist);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
        }
        IV_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RL_LabelType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShow(0, "标签类型");
            }
        });
        BT_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckData()) {
                    Bind();
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RL_PhotoList.setLayoutManager(linearLayoutManager);
        drawablelist = new ArrayList<>();
        RL_PhotoList.addItemDecoration(new RecyclerViewItemDecoration());
        PLA = new LabelPhotoListAdapter(mActivity);
        PLA.setOnItemClickLitener(new LabelPhotoListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoUtils.TakePicture(mActivity, "CarCheckFeedBack:" + position);
            }

            @Override
            public void onItemClearClick(View view, int position) {
                drawablelist.remove(position);
                PLA.UpDate(drawablelist);
            }
        });
        RL_PhotoList.setAdapter(PLA);
    }

    private void initdata() {
        text_PlateNumber.setText(CL.getPlateNumber());
        text_brand.setText(CL.getVehicleBrandName());
        text_color.setText(CL.getColorName());
        text_owner.setText(CL.getOwnerName());
        text_phone.setText(CL.getPhone1());
        text_identityShow.setText(CL.getCardId());
        getSignType();
    }

    private void Bind() {
//        ECID	    是	string	车辆主键
//        THEFTNO	是	string	新标签值(十六进制)
//        SIGNTYPE	是	int	标签序号
//        ExtraInfo	否	Object	附带信息(详细请看输入示例说明)
//        LISTID        string	若是 更换标签 带上此参数
        mProgressHUD.show();
        JSONObject JB = new JSONObject();
        try {
            JSONObject JB1;
//            JSONArray  JA = new JSONArray();
            JB1 = new JSONObject();
            BitmapDrawable bd = (BitmapDrawable) drawablelist.get(0).getDrawable();
            String Photo = Utils.Byte2Str(Utils.Bitmap2Bytes(bd.getBitmap()));
            JB1.put("PhotoFile", Photo);
          /*  for (LabelPhotoListAdapter.DrawableList drawableList : drawablelist) {

                JA.put(JB1);
            }*/
            if (InType.equals("Add")) {
                JB.put("ECID", CL.getEcId());
                JB.put("THEFTNO", ET_LabelNum.getText().toString().trim());
                JB.put("SIGNTYPE", SIGNTYPE + "");
                JB.put("ExtraInfo", JB1);
            } else if (InType.equals("Change")) {
                JB.put("ECID", CL.getEcId());
                JB.put("THEFTNO", ET_NewLabelNum.getText().toString().trim());
                JB.put("SIGNTYPE", SIGNTYPE + "");
                JB.put("ExtraInfo", JB1);
                JB.put("LISTID", CL.getEquipments().get(Position).getLISTID());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants.HTTP_Bind);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        Utils.LOGE("Pan", "Bind:" + JB.toString());
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        mLog.e("绑定结果:" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                dialogShow(1, data);
                            } else if (errorCode == 1) {
                                Utils.showToast(data);
                                SharedPreferencesUtils.put("token", "");
                                ActivityUtil.goActivityAndFinish(CarLabelBindingActivity.this, LoginActivity.class);
                            } else {
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

    private boolean CheckData() {
        String label = "";
        if (InType.equals("Add")) {
            label = ET_LabelNum.getText().toString();
            if (label.equals("")) {
                Utils.showToast("请输入标签号");
                return false;
            }

        } else if (InType.equals("Change")) {
            label = ET_NewLabelNum.getText().toString();
            if (label.equals("")) {
                Utils.showToast("请输入新标签号");
                return false;
            }
        }

        if (TV_Label_Type.getText().toString().equals("")) {
            Utils.showToast("请选择标签类型");
            return false;
        }
        mLog.e("CarRegular="+CarRegular);
        if(CarRegular!=null&&!CarRegular.equals("")){
            Pattern pattern = Pattern.compile(CarRegular);
            Matcher matcher = pattern.matcher(label);
            if (!matcher.matches()) {
                Utils.showToast("标签号格式错误，请重新输入");
                return false;
            }
        }
        if (drawablelist.size() < 1) {
            Utils.showToast("请拍摄至少一张标签照片");
            return false;
        }
        return true;
    }

    private void getSignType() {
        mProgressHUD.show();

        mLog.e("Http=" + (String) SharedPreferencesUtils.get("httpUrl", "") + Constants.HTTP_signtype);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim()+ Constants.HTTP_signtype);
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        mLog.e("Http=" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                LT = mGson.fromJson(data, new TypeToken<List<LabelType>>() {
                                }.getType());
                                if (InType.equals("Add")) {
                                    mAdapter = new MyAdapter();
                                } else if (InType.equals("Change")) {
                                    for (LabelType labelType : LT) {
                                        if (labelType.getValue() == CL.getEquipments().get(Position).getSIGNTYPE()) {
                                            CarRegular = labelType.getRegular();
                                            TV_Label_Type.setText(labelType.getName());
                                            SIGNTYPE = labelType.getValue();
                                        }
                                    }
                                }
                            } else if (errorCode == 1) {
                                Utils.showToast(data);
                                SharedPreferencesUtils.put("token", "");
                                ActivityUtil.goActivityAndFinish(CarLabelBindingActivity.this, LoginActivity.class);
                            } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.CAMERA_REQESTCODE:
                if (resultCode == RESULT_OK) {
                    UpDatePhotoItem();
                }
                break;
        }

    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;

            ListView LV = new ListView(mActivity);

            LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CarRegular = LT.get(position).getRegular();
                    TV_Label_Type.setText(LT.get(position).getName());
                    SIGNTYPE = LT.get(position).getValue();
                    dialogBuilder.dismiss();
                }
            });
            LV.setAdapter(mAdapter);

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(LV, mActivity);
            dialogBuilder.withTitle(msg).withTitleColor("#333333")
                    .withButton1Text("取消")
                    .withMessage(null).withEffect(effectstype)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("确定")
                    .setCustomView(R.layout.custom_view, mActivity).setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    finish();
                }
            }).show();
        }

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return LT.size();
        }

        @Override
        public Object getItem(int position) {
            return LT.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView TV = new TextView(mActivity);
            TV.setTextColor(Color.parseColor("#333333"));
            TV.setTextSize(15);
            TV.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppUtil.dp2px(40)));
            TV.setGravity(Gravity.CENTER);
            TV.setText(LT.get(position).getName());
            return TV;
        }
    }

    private void UpDatePhotoItem() {
        Bitmap bitmap = PhotoUtils.getPhotoBitmap();
        String Photoindex[] = PhotoUtils.mPicName.split(":");
        Drawable drawable = new BitmapDrawable(bitmap);
        if (drawablelist.size() >= Integer.parseInt(Photoindex[1]) + 1) {
            drawablelist.get(Integer.parseInt(Photoindex[1])).setDrawable(drawable);
        } else {
            drawablelist.add(new LabelPhotoListAdapter.DrawableList(Photoindex[1], drawable));
        }
        PLA.UpDate(drawablelist);
    }
}

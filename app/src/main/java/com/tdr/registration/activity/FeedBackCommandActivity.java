package com.tdr.registration.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ImageAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.imagecompress.LGImgCompressor;
import com.tdr.registration.model.FeedBackModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PermissionUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.NoScrollGridView;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 指令反馈
 */

public class FeedBackCommandActivity extends BaseActivity implements LGImgCompressor.CompressListener {
    private static final String TAG = "FeedBackCommandActivity";
    private String[] permissionArray = new String[]{Manifest.permission.CAMERA};

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.rb_notFound)
    RadioButton rbNotFound;
    @BindView(R.id.rb_beenFound)
    RadioButton rbBeenFound;
    @BindView(R.id.rb_allGet)
    RadioButton rbAllGet;
    @BindView(R.id.group_feedbackStatus)
    RadioGroup groupFeedbackStatus;
    @BindView(R.id.edit_desc)
    EditText editDesc;
    @BindView(R.id.gridView_noScroll)
    NoScrollGridView gridViewNoScroll;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.rl_feedback)
    RelativeLayout RL_feedback;

    @BindView(R.id.tv_feedbackReason)
    TextView TV_feedbackReason;

    @BindView(R.id.ll_Arrested)
    LinearLayout LL_Arrested;

    @BindView(R.id.tv_ArrestedAll)
    TextView tv_ArrestedAll;
    @BindView(R.id.tv_ArrestedCar)
    TextView tv_ArrestedCar;
    @BindView(R.id.tv_ArrestedID)
    TextView tv_ArrestedID;
    @BindView(R.id.tv_ArrestedStop)
    TextView tv_ArrestedStop;


    private Context mContext;
    private Gson mGson;

    public static Bitmap bitmap;
    private List<Bitmap> bitmapList;
    private ImageAdapter mAdapter;

    private int number = 0;

    private String uploadStr = "";

    private String feedbackStatus = "";

    private ZProgressHUD mProgressHUD;

    String listId = "";
    private String plateNumber = "";
    private Activity mActivity;
    private boolean isshowing=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_command);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        listId = this.getIntent().getStringExtra("listId");
        initView();
        initData();
    }



    private void initView() {
        textTitle.setText("申请撤控");
        gridViewNoScroll.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridViewNoScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = position;
                //拍照
                PermissionUtils.checkPermissionArray(FeedBackCommandActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
//                takePhoto("command" + position);
                PhotoUtils.TakePicture(mActivity,"command" + position);
            }
        });

        groupFeedbackStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbNotFound.getId()) {
                    feedbackStatus = "0";
                } else if (checkedId == rbBeenFound.getId()) {
                    feedbackStatus = "1";
                } else {
                    feedbackStatus = "2";
                }
            }
        });

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");

    }

//    private void takePhoto(String picName) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        imageFile = new File(fileDir, picName + ".jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//        startActivityForResult(intent, CAMERA_REQESTCODE);
//    }

    private void initData() {
        bitmapList = new ArrayList<>();
        mAdapter = new ImageAdapter(mContext, bitmapList);
        gridViewNoScroll.setAdapter(mAdapter);
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            plateNumber = bundle.getString("plateNumber");
        }

    }

    @OnClick({R.id.image_back, R.id.btn_submit,R.id.rl_feedback,R.id.ll_Arrested,R.id.tv_ArrestedAll,R.id.tv_ArrestedCar,R.id.tv_ArrestedID,R.id.tv_ArrestedStop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_feedback:
                setArrested(true);
                break;
            case R.id.tv_ArrestedAll:
                feedbackStatus="3";
                TV_feedbackReason.setText(tv_ArrestedAll.getText());
                setArrested(false);
                break;
            case R.id.tv_ArrestedCar:
                feedbackStatus="4";
                TV_feedbackReason.setText(tv_ArrestedCar.getText());
                setArrested(false);
                break;
            case R.id.tv_ArrestedID:
                feedbackStatus="5";
                TV_feedbackReason.setText(tv_ArrestedID.getText());
                setArrested(false);
                break;
            case R.id.tv_ArrestedStop:
                feedbackStatus="6";
                TV_feedbackReason.setText(tv_ArrestedStop.getText());
                setArrested(false);
                break;
            case R.id.ll_Arrested:
                break;
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.btn_submit:
                if (feedbackStatus.equals("")) {
                    Utils.myToast(mContext, "请选择案件结果");
                    break;
                }
                String desc = editDesc.getText().toString().trim();
                if (desc.equals("")) {
                    Utils.myToast(mContext, "请输入简要案情");
                    break;
                }
                FeedBackModel model = new FeedBackModel();
                model.setLISTID(listId);
                model.setFEEDBACKSTATUS(feedbackStatus);
                model.setFEEDBACK(desc);
                model.setPHOTOLIST(uploadStr);
                model.setPLATENUMBER(plateNumber);
                String uploadFeedBack = mGson.toJson(model);
                mProgressHUD.show();
                HashMap<String, String> map = new HashMap<>();
                map.put("token", (String) SharedPreferencesUtils.get("token", ""));
                map.put("DataTypeCode", "Feedback");
                map.put("content", uploadFeedBack.toString());
                mLog.e("map="+map.toString());
                WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_INSSYS, map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("result="+result);
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                    ActivityUtil.goActivityAndFinish(FeedBackCommandActivity.this, HomeActivity.class);
                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                    //AppManager.getAppManager().finishActivity(HomeActivity.class);
                                    SharedPreferencesUtils.put("token","");
                                    ActivityUtil.goActivityAndFinish(FeedBackCommandActivity.this, LoginActivity.class);
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
                break;
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
        } else if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                LGImgCompressor.getInstance(this).withListener(this).
                        starCompress(Uri.fromFile(PhotoUtils.imageFile).toString(), 480, 600, 100);
            }
        }
    }

    @Override
    public void onCompressStart() {

    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult) {
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR)//压缩失败
            return;

        File file = new File(compressResult.getOutPath());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            Drawable drawable = new BitmapDrawable(bitmap);
            String path = file.getPath();
            String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            if (path.contains("command")) {
                if (bitmapList.size() > number) {

                } else {
                    bitmapList.add(bitmap);
                }
                mAdapter.notifyDataSetChanged();
                uploadStr = uploadStr + "," + photoStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Log.e("Pan","isshowing="+isshowing);
        if(isshowing){
            isshowing=false;
            setArrested(isshowing);
        }else{
            finish();
        }
    }
    private void setArrested(boolean on_off){
        isshowing=on_off;
        if(on_off){
            LL_Arrested.setVisibility(View.VISIBLE);
        }else{
            LL_Arrested.setVisibility(View.GONE);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

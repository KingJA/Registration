package com.tdr.registration.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.activity.VehicleMonitorActivity;
import com.tdr.registration.adapter.CommandCenterAdapter;
import com.tdr.registration.model.CommandModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppManager;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DateTimePickDialogUtil;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 指令
 * Created by Linus_Xie on 2016/10/18.
 */

public class CommandFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout layoutNoData;
    private TextView textNoData;
    private RecyclerView recyclerLoadMore;
    private SwipeRefreshLayout swipeCommand;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton fabQuery;

    private Gson mGson;
    private CommandCenterAdapter mAdapter;
    private List<CommandModel> commandModels = new ArrayList<>();

    public CommandFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGson = new Gson();
        mAdapter = new CommandCenterAdapter(commandModels, getActivity(),0);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_command, container, false);
        layoutNoData = (LinearLayout) view.findViewById(R.id.layout_noData);
        textNoData = (TextView) view.findViewById(R.id.text_noData);
        textNoData.setText("没有指令");
        recyclerLoadMore = (RecyclerView) view.findViewById(R.id.recycler_loadMore);
        swipeCommand = (SwipeRefreshLayout) view.findViewById(R.id.swipe_command);
        swipeCommand.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_green_light);
        swipeCommand.setProgressViewOffset(false, 0, AppUtil.dp2px(24));
        swipeCommand.setOnRefreshListener(this);

        fabQuery = (FloatingActionButton) view.findViewById(R.id.fab_query);
        fabQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShow("指令搜索");
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerLoadMore.setLayoutManager(layoutManager);
        recyclerLoadMore.setAdapter(mAdapter);

        addData("", "");

        return view;
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;
    private TextView textStartTime;
    private TextView textEndTime;

    private void dialogShow(String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View searchCommand = mInflater.inflate(R.layout.layout_command_search, null);
        textStartTime = (TextView) searchCommand.findViewById(R.id.text_startTime);
        textStartTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DateTimePickDialogUtil dateStartTime = new DateTimePickDialogUtil(getActivity(), "");
                    dateStartTime.dialogShow(textStartTime);
                }
                return true;
            }
        });

        textEndTime = (TextView) searchCommand.findViewById(R.id.text_endTime);
        textEndTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DateTimePickDialogUtil dateEndTime = new DateTimePickDialogUtil(getActivity(), "");
                    dateEndTime.dialogShow(textEndTime);
                }
                return true;
            }
        });

        dialogBuilder.withTitle(message).withTitleColor("#333333").withMessage(null)
                .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                .setCustomView(searchCommand, getActivity()).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                addData(textStartTime.getText().toString(), textEndTime.getText().toString());
            }
        }).show();
    }

    @Override
    public void onRefresh() {
        addData("", "");
    }

    protected void addData(String startTime, String endTime) {
        swipeCommand.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("STARTTIME", startTime);
            jsonObject.put("ENDTIME", endTime);
            jsonObject.put("ISFEEDBACK", "0");//0/1     未反馈/已反馈
            jsonObject.put("TYPE", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("token", (String) SharedPreferencesUtils.get("token", ""));
        map.put("DataTypeCode", "GetIns");
        map.put("content", jsonObject.toString());
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_INSSYS, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int errorCode = object.getInt("ErrorCode");
                        String data = object.getString("Data");
                        if (errorCode == 0) {
                            swipeCommand.setRefreshing(false);
                            commandModels = mGson.fromJson(data, new TypeToken<List<CommandModel>>() {
                            }.getType());
                            layoutNoData.setVisibility(View.GONE);
                            recyclerLoadMore.setVisibility(View.VISIBLE);
                            mAdapter.setData(commandModels);
                        } else if (errorCode == 1) {
                            swipeCommand.setRefreshing(false);
                            Utils.myToast(getActivity(), data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            ActivityUtil.goActivityAndFinish(getActivity(), LoginActivity.class);
                        } else {
                            swipeCommand.setRefreshing(false);
                            Utils.myToast(getActivity(), data);
                        }
                    } catch (JSONException e) {
                        swipeCommand.setRefreshing(false);
                        e.printStackTrace();
                        Utils.myToast(getActivity(), "JSON解析出错");
                    }
                } else {
                    swipeCommand.setRefreshing(false);
                    Utils.myToast(getActivity(), "获取数据超时，请检查网络连接");
                }
            }
        });

    }

}

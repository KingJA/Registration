package com.tdr.registration.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.activity.FeedBackCommandActivity;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.model.CommandModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Linus_Xie on 2016/10/18.
 */

public class CommandCenterAdapter extends RecyclerView.Adapter<CommandCenterAdapter.CommandViewHodler> {

    private List<CommandModel> list;
    private Context mContext;
    private int flag;

    public CommandCenterAdapter(List<CommandModel> list, Context mContext, int flag) {
        this.list = list;
        this.mContext = mContext;
        this.flag = flag;
    }


    @Override
    public CommandViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_item, parent, false);
        CommandViewHodler viewHodler = new CommandViewHodler(v);
        return viewHodler;
    }

    @Override
    public void onBindViewHolder(CommandViewHodler holder, int position) {
        String isReceived = list.get(position).getISRECEIVED();
        if (isReceived.equals("0")){
            holder.imageBlueCircle.setVisibility(View.VISIBLE);
        } else {
            holder.imageBlueCircle.setVisibility(View.GONE);
        }
        holder.textCommandTitle.setText(list.get(position).getCarDeploy().getPlateNumber());
        holder.textAlarmTime.setText(list.get(position).getORDERTIME());
    }

    public void setData(List<CommandModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommandViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public ImageView imageBlueCircle;
        public TextView textCommandTitle;
        public TextView textAlarmTime;
        public Button btnCheck;

        public CommandViewHodler(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            imageBlueCircle = (ImageView) view.findViewById(R.id.image_blueCircle);
            textCommandTitle = (TextView) view.findViewById(R.id.text_commandTitle);
            textAlarmTime = (TextView) view.findViewById(R.id.text_alarmTime);
            btnCheck = (Button) view.findViewById(R.id.btn_check);
//            if (flag == 0){
//                btnCheck.setText("签收");
//            } else {
                btnCheck.setText("查看");
//            }
            btnCheck.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_check:
                    dialogShow(list.get(getLayoutPosition()));
                    sendRead(list.get(getLayoutPosition()).getLISTID());
                    imageBlueCircle.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void sendRead(String listid) {
        HashMap<String,String> map = new HashMap<>();
        map.put("token", (String) SharedPreferencesUtils.get("token", ""));
        map.put("DataTypeCode", "Received");
        map.put("content", listid);
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_INSSYS, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {

            }
        });
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(final CommandModel commandModel) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(mContext);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View showCommand = mInflater.inflate(R.layout.layout_command_show, null);
        TextView textPlateNum = (TextView) showCommand.findViewById(R.id.text_plateNum);
        textPlateNum.setText(commandModel.getCarDeploy().getPlateNumber());
        TextView textTheftNo = (TextView) showCommand.findViewById(R.id.text_theftNo);
        textTheftNo.setText(commandModel.getCarDeploy().getRESERVE3());
        RelativeLayout rl_theftNo2=(RelativeLayout) showCommand.findViewById(R.id.rl_theftNo2);
        TextView textTheftNo2 = (TextView) showCommand.findViewById(R.id.text_theftNo2);
        String RESERVED4=commandModel.getCarDeploy().getRESERVED4();
        mLog.e("RESERVED4:"+RESERVED4);
        if(RESERVED4==null||RESERVED4.equals("null")||RESERVED4.equals("")){
            rl_theftNo2.setVisibility(View.GONE);
        }else{
            textTheftNo2.setText(RESERVED4);
        }
        TextView textLastPoint = (TextView) showCommand.findViewById(R.id.text_lastPoint);
        textLastPoint.setText(commandModel.getDEVICENAME());
        TextView textLastTime = (TextView) showCommand.findViewById(R.id.text_lastTime);
        textLastTime.setText(commandModel.getMONITORTIME());
        TextView textAfterTimes = (TextView) showCommand.findViewById(R.id.text_afterTimes);
        textAfterTimes.setText(commandModel.getTRACK_TIMES());

        dialogBuilder.withTitle("指令信息").withTitleColor("#333333").withMessage(null)
                .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                .setCustomView(showCommand, mContext).withButton2Text("申请撤控").setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                Intent intent = new Intent();
                intent.putExtra("listId",commandModel.getLISTID());
                intent.setClass(mContext, FeedBackCommandActivity.class);
                mContext.startActivity(intent);

            }
        }).show();
    }
}

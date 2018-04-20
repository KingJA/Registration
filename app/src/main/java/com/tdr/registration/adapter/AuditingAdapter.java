package com.tdr.registration.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.BlackCarModel;
import com.tdr.registration.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑车列表适配器
 * Created by Linus_Xie on 2016/11/30.
 */

public class AuditingAdapter extends BaseAdapter {

    private Context mContext;
    private List<BlackCarModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;
    private int flag = 0;
    private AuditingClickListener ACL;
    public interface AuditingClickListener{
        void onItemclick(BlackCarModel blackCarModel);
        void onItemBottonclick(BlackCarModel blackCarModel);
    }
    public void setClickListener(AuditingClickListener ACL){
        this.ACL= ACL;
    }
    public AuditingAdapter(Context mContext, List<BlackCarModel> models,int flag) {
        this.mContext = mContext;
        this.models = models;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final class AuditingViewHolder {
        public TextView textPlateNumber;
        public TextView tv;
        public TextView textCollectTime;
        public Button btnAuditing;
        public RelativeLayout RL_item;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AuditingViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new AuditingViewHolder();
            convertView = mInflater.inflate(R.layout.auditing_item, null);
            viewHolder.textPlateNumber = (TextView) convertView.findViewById(R.id.text_plateNumber);
            viewHolder.RL_item = (RelativeLayout) convertView.findViewById(R.id.RL_item);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_);
            viewHolder.textCollectTime = (TextView) convertView.findViewById(R.id.text_collectTime);
            viewHolder.btnAuditing = (Button) convertView.findViewById(R.id.btn_auditing);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AuditingViewHolder) convertView.getTag();
        }
        if (flag == 0){
            viewHolder.btnAuditing.setVisibility(View.VISIBLE);
        } else if(flag == 1){
            viewHolder.btnAuditing.setVisibility(View.GONE);
        }
        viewHolder.textPlateNumber.setText(models.get(position).getPLATENUMBER());
        viewHolder.textCollectTime.setText(Utils.getNowTime());
        viewHolder.btnAuditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ACL!=null){
                    ACL.onItemBottonclick(models.get(position));
                }
            }
        });
        viewHolder.RL_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ACL!=null){
                    ACL.onItemclick(models.get(position));
                }
            }
        });
        if(position==0){
            viewHolder.textPlateNumber.setTextColor(Color.parseColor("#333333"));
            viewHolder.tv.setTextColor(Color.parseColor("#333333"));
            viewHolder.textCollectTime.setTextColor(Color.parseColor("#333333"));
        }else{
            viewHolder.textPlateNumber.setTextColor(Color.parseColor("#999999"));
            viewHolder.tv.setTextColor(Color.parseColor("#999999"));
            viewHolder.textCollectTime.setTextColor(Color.parseColor("#999999"));
        }
        return convertView;
    }


}

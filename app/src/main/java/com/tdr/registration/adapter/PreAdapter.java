package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.util.DBUtils;


import org.xutils.DbManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 预登记信息展示
 * Created by Linus_Xie on 2016/11/1.
 */

public class PreAdapter extends BaseAdapter {

    private Context mContext;
    private List<PreRegistrationModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;

    private DbManager db;

    public PreAdapter(Context mContext, List<PreRegistrationModel> models) {
        this.mContext = mContext;
        this.models = models;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public final class ViewHolder {
        public TextView textPreIdentity;
        public TextView textBrandName;
        public TextView textMotor;
        public TextView textVehicleColor;
        public TextView textFrame;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.pre_item, null);
            viewHolder.textPreIdentity = (TextView) convertView.findViewById(R.id.text_preIdentity);
            viewHolder.textBrandName = (TextView) convertView.findViewById(R.id.text_brandName);
            viewHolder.textMotor = (TextView) convertView.findViewById(R.id.text_motor);
            viewHolder.textVehicleColor = (TextView) convertView.findViewById(R.id.text_vehicleColor);
            viewHolder.textFrame = (TextView) convertView.findViewById(R.id.text_frame);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textPreIdentity.setText(models.get(position).getOWNERNAME());
        viewHolder.textBrandName.setText(models.get(position).getVEHICLEBRANDNAME());
        viewHolder.textMotor.setText(models.get(position).getENGINENO());
        viewHolder.textVehicleColor.setText(models.get(position).getColorName());
        viewHolder.textFrame.setText(models.get(position).getSHELVESNO());

        return convertView;
    }
}

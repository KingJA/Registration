package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.PointModel;
import com.tdr.registration.model.RcPreRateModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约列表适配器
 * Created by Linus_Xie on 2017/1/11.
 */

public class AppointmentAdapter extends BaseAdapter{

    private Context mContext;
    private List<RcPreRateModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public AppointmentAdapter(Context mContext, List<RcPreRateModel> models) {
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
        public TextView textAppointmentNumber;
        public TextView textUserName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.appointment_item, null);
            viewHolder.textAppointmentNumber = (TextView) convertView.findViewById(R.id.text_appointmentNumber);
            viewHolder.textUserName = (TextView) convertView.findViewById(R.id.text_userName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textAppointmentNumber.setText(models.get(position).getPrerateName());
        viewHolder.textUserName.setText(models.get(position).getOwnerName());

        return convertView;
    }

}

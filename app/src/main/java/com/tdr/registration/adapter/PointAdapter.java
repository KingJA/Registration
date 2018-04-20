package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.RegistrationPointModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 点位适配器
 * Created by Linus_Xie on 2017/1/6.
 */

public class PointAdapter extends BaseAdapter {

    private Context mContext;
    private List<RegistrationPointModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public PointAdapter(Context mContext, List<RegistrationPointModel> models) {
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
        public TextView textPointName;
        public TextView textPointAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.point_item, null);
            viewHolder.textPointName = (TextView) convertView.findViewById(R.id.text_pointName);
            viewHolder.textPointAddress = (TextView) convertView.findViewById(R.id.text_pointAddress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textPointName.setText(models.get(position).getRegistersiteName());
        viewHolder.textPointAddress.setText(models.get(position).getAdress());

        return convertView;
    }
}

package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.MonitorModel;
import com.tdr.registration.util.mLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezu on 2016/10/15.
 */

public class RouteAdapter extends BaseAdapter {

    private Context mContext;
    private List<MonitorModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public RouteAdapter(Context mContext, List<MonitorModel> models) {
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

    public final class RouteViewHolder {
        public TextView textTime;
        public TextView textStationAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RouteViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new RouteViewHolder();
            convertView = mInflater.inflate(R.layout.route_item, null);
            viewHolder.textTime = (TextView) convertView.findViewById(R.id.text_plateNumber);
            viewHolder.textStationAddress = (TextView) convertView.findViewById(R.id.text_stationAddress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RouteViewHolder) convertView.getTag();
        }
        mLog.e("MONITORTIME2="+models.get(position).getMONITORTIME());
        viewHolder.textTime.setText(models.get(position).getMONITORTIME());
        viewHolder.textStationAddress.setText(models.get(position).getStationName());

        return convertView;
    }
}

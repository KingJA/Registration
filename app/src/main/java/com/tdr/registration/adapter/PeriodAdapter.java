package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.PointModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezu on 2017/1/10.
 */

public class PeriodAdapter extends BaseAdapter {

    private Context mContext;
    private List<PointModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public PeriodAdapter(Context mContext, List<PointModel> models) {
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
        public TextView textPeriodTime;
        public TextView textTotalNum;
        public TextView textUsableNum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.period_item, null);
            viewHolder.textPeriodTime = (TextView) convertView.findViewById(R.id.text_periodTime);
            viewHolder.textTotalNum = (TextView) convertView.findViewById(R.id.text_totalNum);
            viewHolder.textUsableNum = (TextView) convertView.findViewById(R.id.text_usableNum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textPeriodTime.setText(models.get(position).getOnTime() + "~" + models.get(position).getOffTime());
        viewHolder.textTotalNum.setText(models.get(position).getSurplusNum());
        viewHolder.textUsableNum.setText(models.get(position).getInstallCnt());

        return convertView;
    }
}

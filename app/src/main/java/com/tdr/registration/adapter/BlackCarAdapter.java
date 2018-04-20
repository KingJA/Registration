package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.DistrainModel;
import com.tdr.registration.model.ElectricCarModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 临时列表适配器
 * Created by Linus_Xie on 2016/10/15.
 */

public class BlackCarAdapter extends BaseAdapter {

    private Context mContext;
    private List<ElectricCarModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public BlackCarAdapter(Context mContext, List<ElectricCarModel> models) {
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

    public final class BlackCarViewHolder {
        public TextView textPlateNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BlackCarViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new BlackCarViewHolder();
            convertView = mInflater.inflate(R.layout.black_car_item, null);
            viewHolder.textPlateNumber = (TextView) convertView.findViewById(R.id.text_plateNumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BlackCarViewHolder) convertView.getTag();
        }

        viewHolder.textPlateNumber.setText(models.get(position).getPlateNumber());

        return convertView;
    }
}

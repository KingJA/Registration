package com.tdr.registration.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.SortModel;

import java.util.HashMap;
import java.util.List;

/**
 * 车辆品牌适配器
 * Created by Linus_Xie on 2016/9/23.
 */
public class ColorAdapter extends BaseAdapter {
    private int[] viewId;
    private int currentIndex = -1;
    private List<SortModel> list_brand;
    private HashMap<Integer, Integer> map;
    private LayoutInflater mInflater;
    private Resources resources;
    private Context mContext;

    public ColorAdapter(Context context, List<SortModel> data,
                        HashMap<Integer, Integer> map, int resource, String[] from, int[] to) {
        this.mInflater = LayoutInflater.from(context);
        this.list_brand = data;
        this.map = map;
        this.viewId = to;
    }

    public void updateListView(List<SortModel> list) {
        this.list_brand = list;
        notifyDataSetChanged();
    }

    public final class ViewHolder {
        public TextView text_brandname;
        public RadioButton item_radio;
    }

    @Override
    public int getCount() {
        return list_brand.size();
    }

    @Override
    public Object getItem(int position) {
        return list_brand.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.color_item, null);
            holder.text_brandname = (TextView) convertView
                    .findViewById(R.id.text_brandname);
            holder.item_radio = (RadioButton) convertView
                    .findViewById(R.id.item_radio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_brandname.setText(list_brand.get(position).getName());
        holder.item_radio.setChecked(map.get(position) == null ? false : true);
        return convertView;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }
}


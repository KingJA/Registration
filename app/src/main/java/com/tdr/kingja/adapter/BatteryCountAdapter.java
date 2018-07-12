package com.tdr.kingja.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tdr.registration.R;

import java.util.List;

/**
 * Description:TODO
 * Create Time:2018/6/7 14:08
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryCountAdapter extends BaseLvAdapter<String> {
    public BatteryCountAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View simpleGetView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_single_text, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_singleText.setText(list.get(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_singleText;
        public View root;

        public ViewHolder(View root) {
            this.root = root;
            tv_singleText = root.findViewById(R.id.tv_singleText);
        }
    }
}

package com.tdr.kingja.view.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tdr.kingja.adapter.BaseLvAdapter;
import com.tdr.registration.R;

import java.util.List;

/**
 * Description：TODO
 * Create Time：2016/8/18 10:32
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public abstract class DialogSelectAdapter<T> extends BaseLvAdapter<T> {

    public DialogSelectAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public View simpleGetView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View
                    .inflate(context, R.layout.item_checkbox, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       fillData(list,position,viewHolder.tv_item);
        return convertView;
    }

    protected abstract void fillData(List<T> list, int position, TextView tvroomnumber);


    public class ViewHolder {
        public final TextView tv_item;
        public final View root;

        public ViewHolder(View root) {
            tv_item = (TextView) root.findViewById(R.id.tv_item);
            this.root = root;
        }
    }
}

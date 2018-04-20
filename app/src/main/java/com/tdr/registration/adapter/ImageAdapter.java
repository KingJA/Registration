package com.tdr.registration.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tdr.registration.R;

import java.util.List;

/**
 * 图片展示适配器
 * Created by Linus_Xie on 2016/10/17.
 */

public class ImageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private int selectedPosition = -1;
    private List<Bitmap> bitmaps;
    private final int number = 5;
    private boolean shape;

    public ImageAdapter(Context mContext, List<Bitmap> bitmaps) {
        this.mContext = mContext;
        this.bitmaps = bitmaps;
        mInflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        public ImageView imageAdd;
    }

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    @Override
    public int getCount() {
        if (bitmaps.size() == number) {
            return number;
        } else {
            return bitmaps.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.image_item, null);
            holder = new ViewHolder();
            holder.imageAdd = (ImageView) convertView.findViewById(R.id.image_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == bitmaps.size()) {
            holder.imageAdd.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.image_add));
            if (position == number) {
                holder.imageAdd.setVisibility(View.GONE);
            }
        } else {
            holder.imageAdd.setImageBitmap(bitmaps.get(position));
        }
        return convertView;
    }
}

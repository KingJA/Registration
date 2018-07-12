package com.tdr.registration.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.mLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class LabelPhotoListAdapter extends  RecyclerView.Adapter<LabelPhotoListAdapter.MyViewHolder> {
public List<LabelPhotoListAdapter.DrawableList> getDrawableList() {
        return DrawableList;
        }

private List<DrawableList> DrawableList;
private Activity mActivity;
private OnItemClickLitener mOnItemClickLitener;
private final int number = 1;

/**
 * 订制点击事件
 */
public interface OnItemClickLitener {
    void onItemClick(View view, int position);

    void onItemClearClick(View view, int position);
}

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    /**
     * @param activity
     */
    public LabelPhotoListAdapter(Activity activity) {
        mActivity = activity;
        if (DrawableList == null) {
            DrawableList = new ArrayList<DrawableList>();
        }
    }

    /**
     * @param activity
     * @param drawableList
     */
    public LabelPhotoListAdapter(Activity activity, List<DrawableList> drawableList) {
        mActivity = activity;
        DrawableList = drawableList;
        if (DrawableList == null) {
            DrawableList = new ArrayList<DrawableList>();
        }
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void UpDate(List<DrawableList> drawableList) {
        DrawableList = drawableList;
        notifyDataSetChanged();
        mLog.e("UpDate");
    }

    @Override
    public int getItemCount() {
        if (DrawableList.size() == number) {
            return number;
        } else {
            return DrawableList.size() + 1;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_label_photo, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (position == DrawableList.size()) {
            holder.itemView.setTag("add");
            holder.clear.setVisibility(View.GONE);
            holder.name.setVisibility(View.GONE);
            holder.photo.setBackgroundResource(R.mipmap.ic_add_photo);
            if (position == number) {
                holder.itemView.setVisibility(View.GONE);
            }
        } else {
            holder.itemView.setTag("photo");
            holder.clear.setVisibility(View.VISIBLE);
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText("标签照片" + (position + 1));

            Drawable db = DrawableList.get(position).getDrawable();
            if (db != null) {
                mLog.e("---"+position);
                holder.photo.setBackground(db);
            } else {
                mLog.e("==="+position);
                holder.photo.setBackgroundResource(R.mipmap.image_camera);
            }


        }

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClearClick(holder.clear, pos);
                }
            });
        }
    }

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView photo;
    public TextView name;
    public ImageView clear;

    public MyViewHolder(View view) {
        super(view);
        view.setLayoutParams(new RelativeLayout.LayoutParams(AppUtil.dp2px(85), AppUtil.dp2px(85)));
        photo = (ImageView) view.findViewById(R.id.iv_photo);
        name = (TextView) view.findViewById(R.id.tv_name);
        clear = (ImageView) view.findViewById(R.id.iv_clear);
    }

}

public static class DrawableList {
    String index;
    Drawable drawable;

    public DrawableList(String i, Drawable d) {
        index = i;
        drawable = d;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String i) {
        this.index = i;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}

}

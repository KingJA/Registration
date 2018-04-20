package com.tdr.registration.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/17.
 */

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.MyViewHolder> {
    private List<DrawableList> DrawableList;
    private List<PhotoListInfo> PhotoDate;
    private Activity mActivity;
    private List<Boolean> PhotoList=new ArrayList<Boolean>();
    private OnItemClickLitener mOnItemClickLitener;
    /**
     * 订制点击事件
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * @param activity
     * @param photodate
     */
    public PhotoListAdapter(Activity activity, List<PhotoListInfo> photodate) {
        mActivity = activity;
        PhotoDate = photodate;
        DrawableList = new ArrayList<DrawableList>();
    }

    /**
     * @param activity
     * @param photodate
     * @param drawableList
     */
    public PhotoListAdapter(Activity activity, List<PhotoListInfo> photodate, List<DrawableList> drawableList) {
        mActivity = activity;
        PhotoDate = photodate;
        DrawableList = drawableList;
        if (DrawableList == null) {
            DrawableList = new ArrayList<DrawableList>();
        }
        PhotoList=new ArrayList<Boolean>();
//        Log.e("Pan","DrawableList="+DrawableList.size());
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }
    public void UpDate(List<DrawableList> drawableList){
        DrawableList = drawableList;
        notifyDataSetChanged();

    }

    /**
     * 检查相框内是否有照片
     *
     * @param index
     * @return
     */
    public boolean checkItemDate2(int index) {
        String plateNumStr = (String) SharedPreferencesUtils.get("Photo:" + PhotoDate.get(index).getINDEX(), "");
        if(!plateNumStr.equals("")){
            return true;
        }
        return false;
    }



    /**
     * 检查相框内是否有照片
     *
     * @param index
     * @return
     */
    public boolean checkItemDate(int index) {
        for (int i = 0; i <PhotoDate.size() ; i++) {
            Log.e("Pan","isRequire="+PhotoDate.get(i).isRequire());
        }
        if(!PhotoDate.get(index).isRequire()){
//            Log.e("Pan","checkItemDate1");
            return true;
        }
        String plateNumStr = (String) SharedPreferencesUtils.get("Photo:" + PhotoDate.get(index).getINDEX(), "");
        if(!plateNumStr.equals("")){
//            Log.e("Pan","checkItemDate2");
            return true;
        }
//        Log.e("Pan","checkItemDate3");
        return false;
    }

    /**
     * @param index
     */
    public void SevePhoto(String index) {
        for (int i = 0; i < PhotoDate.size(); i++) {
            if (PhotoDate.get(i).getINDEX().equals(index)) {
                Log.e("Pan","Position="+i + "  Photo:" + index);
                Log.e("Pan",PhotoUtils.photoStr.equals("")?"图片为空":"图片不为空");
                SharedPreferencesUtils.put("Photo:" + index, PhotoUtils.photoStr);
            }
        }
    }
    public void SevePhoto(String index,String photoStr) {
        for (int i = 0; i < PhotoDate.size(); i++) {
            if (PhotoDate.get(i).getINDEX().equals(index)) {
                SharedPreferencesUtils.put("Photo:" + index,photoStr);
//                Log.e("Pan","Position="+i + "  Photo:" + index);
//                Log.e("Pan",photoStr.equals("")?"图片为空":"图片不为空");

            }
        }
    }


    @Override
    public int getItemCount() {
        return PhotoDate.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e("PhotoListAdapter", "===========onCreateViewHolder==========");
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_photo, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        Log.e("Pan", "----------onBindViewHolder" + position + "---------");

        PhotoListInfo PLI = PhotoDate.get(position);
            for (int i = 0; i < DrawableList.size(); i++) {
                if (DrawableList.get(i).getIndex().equals(PLI.getINDEX())) {
                    Drawable db = DrawableList.get(i).getDrawable();
                    holder.Photo.setBackground(db);
                    holder.PhotoName.setTextColor(Color.WHITE);
//                    Log.e("Pan", position + "---加载照片");
                }
            }
        holder.PhotoName.setText(PLI.getREMARK());
        if (PLI.isRequire()) {
            holder.Mandatory.setVisibility(View.VISIBLE);
        } else {
            holder.Mandatory.setVisibility(View.GONE);
        }
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });

        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView Photo;
        public TextView Mandatory;
        public TextView PhotoName;

        public MyViewHolder(View view) {
            super(view);
            Photo = (ImageView) view.findViewById(R.id.iv_Photo);
            Mandatory = (TextView) view.findViewById(R.id.tv_Mandatory);
            PhotoName = (TextView) view.findViewById(R.id.tv_PhotoName);
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

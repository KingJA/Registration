package com.tdr.registration.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tdr.registration.R;
import com.tdr.registration.util.AppUtil;

import java.util.List;


/**
 * Created by Administrator on 2017/3/17.
 */

public class ShowPhotoListAdapter extends RecyclerView.Adapter<ShowPhotoListAdapter.MyViewHolder> {
    private List<String> URLs;
    private Activity mActivity;
    private OnItemClickLitener mOnItemClickLitener;

    /**
     * 订制点击事件
     */
    public interface OnItemClickLitener {
        void onItemClick(ImageView view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public ShowPhotoListAdapter(Activity A, List<String> list) {
        mActivity = A;
        URLs = list;
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void UpDate(List<String> drawableList) {
        URLs = drawableList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return URLs.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_photo, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Picasso.with(mActivity)
                .load(URLs.get(position))
                .placeholder(R.mipmap.image_camera)
                .error(R.mipmap.image_camera)
                .noFade()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.Photo.setImageBitmap(bitmap);
                        holder.Photo.setTag(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        holder.Mandatory.setVisibility(View.GONE);
        holder.PhotoName.setVisibility(View.GONE);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.Photo, pos);
                }
            });
        }
        holder.Photo.setMinimumHeight(AppUtil.dp2px(85));
        holder.Photo.setMinimumWidth(AppUtil.dp2px(85));
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
}

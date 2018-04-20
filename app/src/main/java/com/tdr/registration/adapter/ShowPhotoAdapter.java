package com.tdr.registration.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ShowPhotoAdapter extends PagerAdapter {
    List<View> mList;
    private onClickListener onClickListener;
    public interface onClickListener{
       void onItemClickListener();
    }
    public void setOnItemclickListener(onClickListener oil){
        onClickListener=oil;
    }
    public ShowPhotoAdapter(List<View> localList) {
        mList = localList;
    }

    public void Update(List<View> paramList) {
        mList = paramList;
        notifyDataSetChanged();
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
        paramViewGroup.removeView(mList.get(paramInt));
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    public Object instantiateItem(ViewGroup paramViewGroup, final int paramInt) {

            mList.get(paramInt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener!=null){
                        onClickListener.onItemClickListener();
                    }
                }
            });
        paramViewGroup.addView(mList.get(paramInt));
        return mList.get(paramInt);
    }

    @Override
    public boolean isViewFromObject(View paramView, Object paramObject) {
        return paramView == paramObject;
    }
}

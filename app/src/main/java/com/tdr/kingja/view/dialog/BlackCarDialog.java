package com.tdr.kingja.view.dialog;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.kingja.entity.BlackCar;
import com.tdr.kingja.utils.ImageUtil;
import com.tdr.registration.R;

import java.util.ArrayList;
import java.util.List;


public class BlackCarDialog extends DialogBaseAlert {
    private OnDoubleClickListener onDoubleClickListener;

    private ViewPager mVpBlackCheck;
    private RelativeLayout mRlLeft;
    private TextView mTvCancle;
    private RelativeLayout mRlRight;
    private TextView mTvChange;

    private List<View> blackCarsLayouts = new ArrayList<>();
    private List<BlackCar> blackCars = new ArrayList<>();
    private List<View> points = new ArrayList<>();
    private LinearLayout mLlPposition;


    public BlackCarDialog(Context context, List<BlackCar> blackCars) {
        super(context);
        this.blackCars = blackCars;
    }


    @Override
    public void initView() {
        setContentView(R.layout.dialog_black_check);
        mVpBlackCheck = findViewById(R.id.vp_blackCheck);
        mRlLeft = findViewById(R.id.rl_left);
        mTvCancle = findViewById(R.id.tv_cancle);
        mRlRight = findViewById(R.id.rl_right);
        mTvChange = findViewById(R.id.tv_change);
        mLlPposition = findViewById(R.id.ll_position);
    }

    @Override
    public void initNet() {

    }

    @Override
    public void initEvent() {
        mRlLeft.setOnClickListener(this);
        mRlRight.setOnClickListener(this);
        mVpBlackCheck.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (blackCars.size()<2) {
                    return;
                }
                for (int i = 0; i < points.size(); i++) {
                    if (i == position) {
                        points.get(i).setBackgroundResource(R.mipmap.ic_point_action);
                    } else {
                        points.get(i).setBackgroundResource(R.mipmap.ic_point_nor);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void initData() {
        initPoints();
        for (int i = 0; i < blackCars.size(); i++) {
            BlackCar blackCar = blackCars.get(i);
            View blackCarView = View.inflate(context, R.layout.layout_black_car, null);
            TextView mTvPlateNumber = blackCarView.findViewById(R.id.tv_plateNumber);
            TextView mTvName = blackCarView.findViewById(R.id.tv_name);
            TextView mTvColor = blackCarView.findViewById(R.id.tv_color);
            TextView mTvBranch = blackCarView.findViewById(R.id.tv_branch);
            TextView mTvShelvesNo = blackCarView.findViewById(R.id.tv_shelvesNo);
            TextView mTvEngineNo = blackCarView.findViewById(R.id.tv_engineNo);
            ImageView mIvFront = blackCarView.findViewById(R.id.iv_front);
            ImageView mIvEnd = blackCarView.findViewById(R.id.iv_end);
            mTvPlateNumber.setText(blackCar.getPlateNumber());
            mTvName.setText(blackCar.getOwnerName());
            mTvColor.setText(blackCar.getColorName());
            mTvBranch.setText(blackCar.getVehicleBrandName());
            mTvShelvesNo.setText(blackCar.getShelvesNo());
            mTvEngineNo.setText(blackCar.getEngineNo());
            List<BlackCar.PhotoListFileBean> photoListFile = blackCar.getPhotoListFile();
            ImageView[] photos={mIvFront,mIvEnd};
            if (photoListFile != null & photoListFile.size() > 0) {
                for (int i1 = 0; i1 < photos.length; i1++) {
                    photos[i1].setImageBitmap(ImageUtil.base64ToBitmap(photoListFile.get(i1).getPhotoFile()));
                }
            }
            blackCarsLayouts.add(blackCarView);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
        layoutParams.setMargins(0, 0, 30, 0);
        for (int i = 0; i < points.size(); i++) {
            mLlPposition.addView(points.get(i), layoutParams);
        }
        BlackCarAdapter blackCarAdapter = new BlackCarAdapter();
        mVpBlackCheck.setAdapter(blackCarAdapter);
    }

    private void initPoints() {
        if (blackCars.size()<2) {
            return;
        }
        for (int i = 0; i < blackCars.size(); i++) {
            View view = new View(context);
            if (i == 0) {
                view.setBackgroundResource(R.mipmap.ic_point_action);
            } else {
                view.setBackgroundResource(R.mipmap.ic_point_nor);
            }
            points.add(view);
        }
    }


    @Override
    public void childClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                dismiss();
                onDoubleClickListener.onCancle();
                break;
            case R.id.rl_right:
                dismiss();
                onDoubleClickListener.onConfirm();
                break;
            default:
                break;
        }
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    public interface OnDoubleClickListener {
        void onCancle();

        void onConfirm();
    }

    private class BlackCarAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return blackCars.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(blackCarsLayouts.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(blackCarsLayouts.get(position));
            return blackCarsLayouts.get(position);
        }
    }
}

package com.tdr.registration.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.ImageDBInfo;
import com.tdr.registration.model.ImageInfo;
import com.tdr.registration.util.ImageLoaders;
import com.tdr.registration.util.mLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Linus_Xie on 2016/10/15.
 */

public class PreviewImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    private int index = 0;
    private ViewPager viewPager;
    private ArrayList<ImageInfo> imageList;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private SamplePagerAdapter pagerAdapter;

    private float moveheight;
    private int type;

    private String photoStr = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        initView();
        listener();
        initData();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.hacky_viewPager);
    }

    private void listener() {
        viewPager.setOnPageChangeListener(this);
    }

    private void initData() {
        index = getIntent().getIntExtra("index",0);
        type = getIntent().getIntExtra("type",0);
        //imageList = (ArrayList<ImageInfo>) getIntent().getSerializableExtra("data");
        photoStr = getIntent().getStringExtra("data");
        mLog.e("11",imageList.size()+"数量");
        imageInfo = imageList.get(index);
        bdInfo = (ImageDBInfo) getIntent().getSerializableExtra("bdinfo");
        pagerAdapter = new SamplePagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(index);
        if (type == 1){
            moveheight = dip2px(70);
        } else if (type == 2){
            moveheight = (Width - 3 * dip2px(2)/3);
        } else if (type == 3){
            moveheight = (Width - dip2px(80) - dip2px(2)/3);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (showimg == null){
            return;
        }
        ImageInfo info = imageList.get(position);
        ImageLoaders.setsendimg(info.url,showimg);
        if (type == 1){
            int move_index = position - index;
            to_y = move_index * moveheight;
        }else if (type == 2){
            int	a = index / 3;
            int b = index % 3;
            int a1 = position / 3;
            int b1 = position % 3;
            to_y = (a1 - a) * moveheight + (a1 - a) * dip2px(2);
            to_x = (b1 - b) * moveheight + (b1 - b) * dip2px(2);
        }else if (type == 3){
            int	a = index / 3;
            int b = index % 3;
            int a1 = position / 3;
            int b1 = position % 3;
            to_y = (a1 - a) * moveheight + (a1 - a) * dip2px(1);
            to_x = (b1 - b) * moveheight + (b1 - b) * dip2px(1);
        }
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            String path = imageList.get(position).url;
            ImageLoader.getInstance().displayImage(path, photoView, options,
                    animateFirstListener);
            // Now just add PhotoView to ViewPager and return it
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    viewPager.setVisibility(View.GONE);
                    showimg.setVisibility(View.VISIBLE);
                    setShowimage();
//                    finish();
                }
            });
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class AnimateFirstDisplayListener extends SimpleImageLoadingListener{
        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewPager.setVisibility(View.GONE);
            showimg.setVisibility(View.VISIBLE);
            setShowimage();
        }
        return true;
    }

    @Override
    protected void EndSoring() {
        super.EndSoring();
        viewPager.setVisibility(View.VISIBLE);
        showimg.setVisibility(View.GONE);
    }

    @Override
    protected void EndMove() {
        super.EndMove();
        finish();
    }

}

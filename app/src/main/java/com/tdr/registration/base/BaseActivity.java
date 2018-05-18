package com.tdr.registration.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tdr.registration.model.ImageDBInfo;
import com.tdr.registration.model.ImageInfo;
import com.tdr.registration.util.ActivityCollection;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类
 * Created by Linus_Xie on 2016/9/9.
 */
public class BaseActivity extends AppCompatActivity {
    private  final String TAG = getClass().getSimpleName();
    private App mBaseApp = null;
    private boolean mIsAddedView;

    //屏幕宽度
    public float Width;
    //屏幕高度
    public float Height;

    protected ImageView showimg;

    protected boolean isSwipeToClose() {
        return false;
    }

    private final Spring mSpring = SpringSystem.create().createSpring().addListener(new ExampleSpringListener());

    private RelativeLayout mainView;

    protected ImageDBInfo bdInfo;
    protected ImageInfo imageInfo;
    private float size, size_h;

    private float img_w;
    private float img_h;

    //原图高
    private float y_img_h;
    protected float to_x = 0;
    protected float to_y = 0;
    private float tx;
    private float ty;
    private int statusBarHeight;
    private int titleBarHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "当前Activit ");
        ActivityCollection.addActivity(this);
        mBaseApp = (App) getApplication();

        mIsAddedView = false;
        bdInfo = new ImageDBInfo();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //statusBarHeight是上面所求的状态栏的高度
        titleBarHeight = contentTop - statusBarHeight;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsAddedView) {
            mBaseApp = null;
        }
        ActivityCollection.removeActivity(this);
    }

    public App getApp() {
        return mBaseApp;
    }

    protected void setShowimage() {
        if (mSpring.getEndValue() == 0) {
            mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(170, 5));
            tx = Width / 2 - (bdInfo.x + img_w / 2);
            ty = Height / 2 - (bdInfo.y + img_h / 2);
            MoveView();
            return;
        }
        mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(1, 5));
        mSpring.setEndValue(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                MoveBackView();
            }
        }, 300);
    }

    private class ExampleSpringListener implements SpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            double CurrentValue = spring.getCurrentValue();
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(CurrentValue, 0, 1, 1, size);
            float mapy = (float) SpringUtil.mapValueFromRangeToRange(CurrentValue, 0, 1, 1, size_h);
            showimg.setScaleX(mappedValue);
            showimg.setScaleY(mapy);
            if (CurrentValue == 1) {
                EndSoring();
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {

        }

        @Override
        public void onSpringActivate(Spring spring) {

        }

        @Override
        public void onSpringEndStateChange(Spring spring) {

        }
    }

    protected void EndSoring() {

    }

    protected void EndMove() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }


    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    private void MoveView() {

        ObjectAnimator.ofFloat(mainView, "alpha", 0.8f).setDuration(0).start();
        mainView.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(showimg, "translationX", tx).setDuration(200),
                ObjectAnimator.ofFloat(showimg, "translationY", ty).setDuration(200),
                ObjectAnimator.ofFloat(mainView, "alpha", 1).setDuration(200)

        );
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                showimg.setScaleType(ImageView.ScaleType.FIT_XY);
                mSpring.setEndValue(1);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();

    }

    private void MoveBackView() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(showimg, "translationX", to_x).setDuration(200),
                ObjectAnimator.ofFloat(showimg, "translationY", to_y).setDuration(200)
        );
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                showimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                EndMove();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        res.updateConfiguration(configuration,res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

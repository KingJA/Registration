package com.tdr.kingja.base;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tdr.registration.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description：TODO
 * Create Time：2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public abstract class BaseTitleActivity extends BaseActivity {
    protected String TAG=getClass().getSimpleName();

    protected View rootView;
    private TextView tvTitleTitle;
    private Unbinder bind;

    @Override
    public View getContentView() {
        rootView = View.inflate(this, R.layout.activity_base_title, null);
        FrameLayout flContent = rootView.findViewById(R.id.fl_content);
        tvTitleTitle = rootView.findViewById(R.id.tv_title_title);
        LinearLayout llTitleBack = rootView.findViewById(R.id.ll_title_back);

        if (showBack()) {
            llTitleBack.setVisibility(View.VISIBLE);
        }
        tvTitleTitle.setText(getContentTitle() == null ? "" : getContentTitle());
        llTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View content = View.inflate(this, getContentId(), null);
        if (content != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            flContent.addView(content, params);
            bind = ButterKnife.bind(this, rootView);
            // register after ButterKnife.bind()

        }
        return rootView;
    }

    protected  boolean showBack(){
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    public void setContentTitle(String title) {
        tvTitleTitle.setText(title);
    }

    public void setMenuRes(int resId, View.OnClickListener onClickListener) {
        LinearLayout llTitleMenu = rootView.findViewById(R.id.ll_title_menu);
        llTitleMenu.setVisibility(View.VISIBLE);
        llTitleMenu.setOnClickListener(onClickListener);
        ImageView ivMenu = rootView.findViewById(R.id.iv_menu);
        ivMenu.setBackgroundResource(resId);
    }

    public void setRightClick(String rightText, View.OnClickListener onClickListener) {
        TextView tv_right_text = rootView.findViewById(R.id.tv_right_text);
        tv_right_text.setText(rightText);
        tv_right_text.setVisibility(View.VISIBLE);
        tv_right_text.setOnClickListener(onClickListener);
    }

    @Override
    public abstract void initVariable();


    protected abstract String getContentTitle();

    protected abstract int getContentId();

    @Override
    protected abstract void initView();

    @Override
    protected abstract void initData();

    @Override
    protected abstract void initNet();


}

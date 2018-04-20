package com.tdr.registration.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tdr.registration.util.mLog;

/**
 * LinearLayout模拟listView
 * Created by Linus_Xie on 2016/9/23.
 */
public class LinearLayoutForShowInsurance extends LinearLayout {
    private static final String TAG = "LinearLayoutForListView";

    private android.widget.BaseAdapter adapter;
    private OnClickListener onClickListener = null;

    public void fillLinearLayout() {
        int count = adapter.getCount();
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            View v = adapter.getView(i, null, null);
            v.setOnClickListener(this.onClickListener);
            v.setLayoutParams(lp);
            addView(v, i);
        }
        mLog.v("countTAG", "" + count);
    }

    public LinearLayoutForShowInsurance(Context context) {
        super(context);
    }

    public LinearLayoutForShowInsurance(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public android.widget.BaseAdapter getAdpater() {
        return adapter;
    }

    public void setAdapter(android.widget.BaseAdapter adpater) {
        this.adapter = adpater;
        fillLinearLayout();
    }

    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

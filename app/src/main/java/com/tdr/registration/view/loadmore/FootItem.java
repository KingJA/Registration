package com.tdr.registration.view.loadmore;

import android.view.View;
import android.view.ViewGroup;

/**
 * Footer item
 * Created by Linus_Xie on 2016/10/6.
 */
public abstract class FootItem {
    public CharSequence loadingText;
    public CharSequence endText;
    public CharSequence pullToLoadText;

    public abstract View onCreateView(ViewGroup parent);

    public abstract void onBindData(View view, int state);
}

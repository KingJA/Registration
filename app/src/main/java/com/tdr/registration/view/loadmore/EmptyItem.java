package com.tdr.registration.view.loadmore;

import android.view.View;
import android.view.ViewGroup;

/**
 * 没数据时的默认VIew
 * Created by Linus_Xie on 2016/10/6.
 */
public abstract class EmptyItem {
    CharSequence mEmptyText;
    int mEmptyIconRes = -1;

    abstract View onCreateView(ViewGroup parent);

    abstract void onBindData(View view);
}

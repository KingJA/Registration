package com.tdr.registration.util;

import android.view.View;

/**
 * Created by Linus_Xie on 2016/9/17.
 */
public interface ItemClickListener {
    /**
     * Item 普通点击
     */

    public void onItemClick(View view, int postion);

    /**
     * Item 长按
     */

    public void onItemLongClick(View view, int postion);

    /**
     * Item 内部View点击
     */

    public void onItemSubViewClick(View view, int postion);
}

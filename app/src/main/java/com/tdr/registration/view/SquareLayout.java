package com.tdr.registration.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 设置GridView的item为正方形
 * 防止变形
 * Created by Linus_Xie on 2016/9/14.
 */
public class SquareLayout extends RelativeLayout {
    public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context) {
        super(context);
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();

        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

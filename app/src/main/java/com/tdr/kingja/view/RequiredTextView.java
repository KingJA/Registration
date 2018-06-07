package com.tdr.kingja.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

/**
 * Description：A smart switchable button,support multiple tabs.
 * Create Time：2016/8/19 14:57
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class RequiredTextView extends AppCompatTextView {

    public RequiredTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SpannableString spanString = new SpannableString("*"+this.getText());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        this.setText(spanString);
    }
}

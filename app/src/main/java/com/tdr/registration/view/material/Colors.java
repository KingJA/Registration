package com.tdr.registration.view.material;

import android.graphics.Color;

/**
 * Created by Linus_Xie on 2016/8/2.
 */
public class Colors {
    public static boolean isLight(int color) {
        return Math.sqrt(
                Color.red(color) * Color.red(color) * .241 +
                        Color.green(color) * Color.green(color) * .691 +
                        Color.blue(color) * Color.blue(color) * .068) > 130;
    }
}

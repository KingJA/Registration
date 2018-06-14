package com.tdr.kingja.utils;

import android.app.Activity;
import android.view.View;

import com.tdr.registration.R;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

/**
 * Description:TODO
 * Create Time:2018/6/13 14:02
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class DialogUtil {
    public static void showSuccess(final Activity activity, String message) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(activity);
        dialogBuilder.withTitle("提示")
                .withTitleColor("#333333")
                .withMessage(message)
                .isCancelableOnTouchOutside(false)
                .withEffect(NiftyDialogBuilder.Effectstype.Fadein)
                .withButton1Text("確定")
                .setCustomView(R.layout.custom_view, activity)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        GoUtil.goActivityAndFinish(activity, HomeActivity.class);
                    }
                });
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }
}

package com.tdr.registration.view.material.validation;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

/**
 * Created by Linus_Xie on 2016/8/2.
 */
public class RegexpValidator {
    private Pattern pattern;

    public RegexpValidator(@NonNull String errorMessage, @NonNull String regex) {
        pattern = Pattern.compile(regex);
    }

    public RegexpValidator(@NonNull String errorMessage, @NonNull Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return pattern.matcher(text).matches();
    }
}

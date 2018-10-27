package com.gauraw.wunder.custom.font;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomTextViewRegular extends AppCompatTextView {

    public CustomTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GlacialIndifference-Regular.ttf"));
    }
}

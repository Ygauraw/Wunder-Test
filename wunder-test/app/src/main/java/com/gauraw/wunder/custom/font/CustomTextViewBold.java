package com.gauraw.wunder.custom.font;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomTextViewBold extends AppCompatTextView {

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GlacialIndifference-Bold.ttf"));
    }
}

package com.gauraw.wunder.custom.font;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class CustomButtonRegular extends AppCompatButton {

    public CustomButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GlacialIndifference-Regular.ttf"));
    }
}

package com.gauraw.wunder.utils;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Utils {

    public static final String serverUrl = "https://s3-us-west-2.amazonaws.com/wunderbucket/locations.json" ;

    public static void changeTabsFont(ViewGroup viewGroup, Typeface typeface) {

        ViewGroup vg = (ViewGroup) viewGroup.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }
}

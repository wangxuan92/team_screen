package com.dariopellegrini.formbuilder;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

public class MApplication {
    public static Typeface typeface;

    public static void init(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "font/pingfangfine.ttf");
    }


    public static Typeface getTypeface() {
        return typeface;
    }

}

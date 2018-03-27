package com.dariopellegrini.formbuilder;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;


/**
 * Created by wangxuan on 17/11/23.
 */

public class FineEditText extends AppCompatEditText {

    public FineEditText(Context context) {
        super(context);
    }

    public FineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeTypeFace(context, attrs);
    }

    /**
     * 改变字体类型
     *
     * @param context
     * @param attrs
     */
    private void changeTypeFace(Context context, AttributeSet attrs) {
        if (attrs != null) {
//            Typeface mtf = Typeface.createFromAsset(context.getAssets(),
//                    "font/pingfangfine.ttf");
            super.setTypeface(MApplication.getTypeface());
        }
    }

}
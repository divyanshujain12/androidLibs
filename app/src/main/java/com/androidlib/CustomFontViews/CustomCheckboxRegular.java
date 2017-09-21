package com.androidlib.CustomFontViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * Created by divyanshu.jain on 8/8/2017.
 */

public class CustomCheckboxRegular extends AppCompatCheckBox {
    public CustomCheckboxRegular(Context context) {
        super(context);
        init();
    }

    public CustomCheckboxRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCheckboxRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Font.FONT_REGULAR);
        setTypeface(tf, 1);

    }
}

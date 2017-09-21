package com.androidlib.CustomFontViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


/**
 * Created by divyanshu.jain on 8/8/2017.
 */

public class CustomTextviewRegular extends android.support.v7.widget.AppCompatTextView {

    public CustomTextviewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextviewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextviewRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Font.FONT_REGULAR);
        setTypeface(tf ,1);

    }

}

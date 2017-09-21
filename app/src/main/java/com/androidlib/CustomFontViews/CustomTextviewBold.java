package com.androidlib.CustomFontViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


/**
 * Created by divyanshu.jain on 8/8/2017.
 */

public class CustomTextviewBold extends android.support.v7.widget.AppCompatTextView {

    public CustomTextviewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextviewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextviewBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Font.FONT_BOLD);
        setTypeface(tf, 1);

    }
}

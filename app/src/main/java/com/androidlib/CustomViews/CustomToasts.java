package com.androidlib.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Toast;

import com.androidlib.CustomFontViews.CustomTextviewRegular;
import com.locationlib.R;


/**
 * Created by divyanshu on 10/14/2016.
 */
public class CustomToasts {
    private static CustomToasts ourInstance = null;
    private static Toast errorToast = null, successToast = null;
    private static int toastTextID = 1001;

    public static CustomToasts getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new CustomToasts();
            createToast(context, Color.RED);
            createToast(context, context.getResources().getColor(R.color.colorPrimary));
        }
        return ourInstance;
    }

    private CustomToasts() {
    }

    public void showErrorToast(String message) {
        ((CustomTextviewRegular) errorToast.getView().findViewById(toastTextID)).setText(message);
        errorToast.show();
    }

    public void showSuccessToast(String message) {
        ((CustomTextviewRegular) successToast.getView().findViewById(toastTextID)).setText(message);
        successToast.show();
    }

    private static void createToast(Context context, int color) {
        CustomTextviewRegular tv = new CustomTextviewRegular(context);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER);
        tv.setText("My Custom Toast at Bottom of Screen");
        tv.setId(toastTextID);
        tv.setPadding(30, 10, 30, 10);
        tv.setBackgroundColor(color);
        if (color == Color.RED)
            createErrorToast(context, tv);
        else
            createSuccessToast(context, tv);

    }

    private static void createSuccessToast(Context context, CustomTextviewRegular tv) {
        successToast = new Toast(context);
        successToast.setView(tv);
        successToast.setGravity(Gravity.CENTER, 0, 50);
        successToast.setDuration(Toast.LENGTH_SHORT);
    }

    private static void createErrorToast(Context context, CustomTextviewRegular tv) {
        errorToast = new Toast(context);
        errorToast.setView(tv);
        errorToast.setGravity(Gravity.CENTER, 0, 50);
        errorToast.setDuration(Toast.LENGTH_SHORT);
    }
}

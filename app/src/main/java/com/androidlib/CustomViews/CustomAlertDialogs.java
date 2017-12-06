package com.androidlib.CustomViews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidlib.CustomFontViews.CustomButtonRegular;
import com.androidlib.CustomFontViews.CustomTextviewBold;
import com.androidlib.CustomFontViews.CustomTextviewRegular;
import com.androidlib.Interfaces.AlertDialogInterface;
import com.androidlib.Interfaces.ImagePickDialogInterface;
import com.androidlib.Interfaces.SnackBarCallback;
import com.androidlib.Utils.ImageLoading;
import com.locationlib.R;

import java.util.Locale;


public class CustomAlertDialogs {
    static AlertDialog alertDialog;
    private static ImageLoading imageLoading;

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snackBarCallback.doAction();
            }
        });
        alertDialog.show();
    }


    public static void showAlertDialogWithCallBack(final Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snackBarCallback.doAction();
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //((Activity) context).onBackPressed();
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    public static void showAlertDialogWithCallBacks(final Context context, String title, String message, final AlertDialogInterface alertDialogInterface) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogInterface.Yes();
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogInterface.No();
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    public static void showCategoryDescDialog(final Context context, String title, String message, final SnackBarCallback snackBarCallback) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        TextView textView = new TextView(context);
        textView.setText(Html.fromHtml(message));
        textView.setPadding(20, 10, 10, 10);
        alertDialog.setView(textView);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snackBarCallback.doAction();
                dialog.dismiss();
            }
        });


        alertDialog.show();
    }


    public static void showImageSelectDialog(Context context, final ImagePickDialogInterface imagePickDialogInterface) {
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.image_pick_dialog, null);
        setupFullWidthDialog();
        TextView selectCameraTV = (TextView) layout.findViewById(R.id.selectCameraTV);
        TextView selectGalleryTV = (TextView) layout.findViewById(R.id.selectGalleryTV);
        TextView cancelTV = (TextView) layout.findViewById(R.id.cancelTV);
        selectCameraTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialogInterface.Camera();
                dismissDialog();
                ;
            }
        });

        selectGalleryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialogInterface.Gallery();
                dismissDialog();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();

            }
        });
        alertDialog.setView(layout);
        alertDialog.show();
    }


    public static void showSingleBarcodeDialog(Context context, String qrCodePath, final AlertDialogInterface alertDialogInterface) {
        imageLoading = new ImageLoading(context);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.single_barcode_layout, null);
        setupFullInCenterWidthDialog();
        ImageView barcodeIV = (ImageView) layout.findViewById(R.id.barcodeIV);
        imageLoading.LoadImage(qrCodePath, barcodeIV, null);
        CustomButtonRegular printBT = (CustomButtonRegular) layout.findViewById(R.id.printBT);
        CustomButtonRegular cancelBT = (CustomButtonRegular) layout.findViewById(R.id.cancelBT);
        printBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                alertDialogInterface.Yes();

            }
        });

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                alertDialogInterface.No();
            }
        });
        alertDialog.setView(layout);
        alertDialog.show();
    }

    public static void showUniqueCodeDialog(final Context context, String uniqueCodeFor, String userName, final String uniqueCode, final AlertDialogInterface alertDialogInterface) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.unique_code_generate_layout, null);
        setupFullInCenterWidthDialog();
        CustomTextviewBold uniqueCodeForTV = (CustomTextviewBold) layout.findViewById(R.id.uniqueCodeForTV);
        uniqueCodeForTV.setText(String.format(Locale.getDefault(), context.getString(R.string.unique_code_generated), uniqueCodeFor));
        CustomTextviewRegular userNameTV = (CustomTextviewRegular) layout.findViewById(R.id.userNameTV);
        CustomTextviewBold uniqueCodeTV = (CustomTextviewBold) layout.findViewById(R.id.uniqueCodeTV);

        userNameTV.setText(userName);
        uniqueCodeTV.setText(uniqueCode);
        layout.findViewById(R.id.copyCodeBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(context.getString(R.string.unique_code), uniqueCode);
                clipboardManager.setPrimaryClip(clipData);

                CustomToasts.getInstance(context).showSuccessToast("Unique Code Has Been Copied");
                if (alertDialogInterface != null)
                alertDialogInterface.Yes();
            }
        });
        layout.findViewById(R.id.okBT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (alertDialogInterface != null)
                alertDialogInterface.No();
            }
        });

        alertDialog.setView(layout);
        alertDialog.show();
    }

    private static void showErrorToast(Context context, String errorMsg) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private static void dismissDialog() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }


    private static void setupFullWidthDialog() {
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    private static void setupFullInCenterWidthDialog() {
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }
}
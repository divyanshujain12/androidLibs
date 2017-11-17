package com.androidlib.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.androidlib.Fragments.RuntimePermissionHeadlessFragment;
import com.locationlib.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class CommonFunctions {
    private CommonFunctions() {
    }

    private static ProgressDialog progressDialog;
    private Snackbar customErrorSnackbar = null;
    private Snackbar customSuccessSnackbar = null;

    public static CommonFunctions getInstance() {


        return new CommonFunctions();
    }

    public static void showShortLengthSnackbar(String message, View view) {

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorSnackBar(Activity activity, String errorText) {
        getErrorSnackbar(activity).setText(errorText).setDuration(Snackbar.LENGTH_SHORT).show();
    }

    public void showSuccessSnackBar(Activity activity, String msg) {
        getSuccessSnackbar(activity).setText(msg).setDuration(Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorSnackBar(View view, String errorText) {
        getErrorSnackbar(view).setText(errorText).setDuration(Snackbar.LENGTH_SHORT).show();
    }

    public Snackbar getErrorSnackbar(View view) {
        if (customErrorSnackbar == null)
            customErrorSnackbar = createErrorCustomSnackBar(view);
        return customErrorSnackbar;
    }

    public Snackbar getErrorSnackbar(Activity activity) {
        if (customErrorSnackbar == null)
            customErrorSnackbar = createErrorCustomSnackBar(activity);
        return customErrorSnackbar;
    }

    public Snackbar getSuccessSnackbar(Activity activity) {
        if (customSuccessSnackbar == null)
            customSuccessSnackbar = createSuccessCustomSnackBar(activity);
        return customSuccessSnackbar;
    }

    private Snackbar createSuccessCustomSnackBar(Activity context) {
        Snackbar snackbar = Snackbar
                .make(context.findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#00FF00"));

        return snackbar;
    }

    public Snackbar createLoadingSnackBarWithActivity(Activity context) {
        Snackbar snackbar = Snackbar
                .make(context.findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, context.getResources().getColor(R.color.colorPrimary));

        return snackbar;
    }


    public Snackbar createLoadingSnackBarWithView(View view) {
        Snackbar snackbar = Snackbar
                .make(view, "", Snackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#ECC11C"));

        return snackbar;
    }

    private Snackbar createErrorCustomSnackBar(Activity context) {
        Snackbar snackbar = Snackbar
                .make(context.findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#FF0000"));

        return snackbar;
    }

    private Snackbar createErrorCustomSnackBar(View view) {
        Snackbar snackbar = Snackbar
                .make(view, "", Snackbar.LENGTH_LONG);
        setUpCustomSnackBar(snackbar, Color.parseColor("#FF0000"));

        return snackbar;
    }

    private void setUpCustomSnackBar(Snackbar snackbar, int color) {
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(color);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    public static void showContinuousSB(Snackbar continuousSB) {
        continuousSB.setText(R.string.loading_data).setDuration(Snackbar.LENGTH_INDEFINITE).show();
    }

    public static void hideContinuousSB(Snackbar continuousSB) {
        continuousSB.dismiss();
    }

    public void hideKeyBoard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public RuntimePermissionHeadlessFragment addRuntimePermissionFragment(AppCompatActivity activity, RuntimePermissionHeadlessFragment.PermissionCallback permissionCallback) {
        RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment = RuntimePermissionHeadlessFragment.newInstance(permissionCallback);
        activity.getFragmentManager().beginTransaction().add(runtimePermissionHeadlessFragment, runtimePermissionHeadlessFragment.getClass().getName()).commit();
        return runtimePermissionHeadlessFragment;
    }



    public Bitmap retrieveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public ArrayList<String> getListFiles(File parentDir) {
        ArrayList<String> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            } else if (file.getName().endsWith(".png")) {
                inFiles.add(file.getAbsolutePath());
            }
        }
        return inFiles;
    }

    public void showDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context, com.locationlib.R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}

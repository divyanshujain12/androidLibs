/**
 * ImagePrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package com.androidlib.brotherPrinter.printprocess;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.androidlib.CustomViews.CustomToasts;
import com.androidlib.brotherPrinter.common.MsgDialog;
import com.androidlib.brotherPrinter.common.MsgHandle;
import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;


import java.util.ArrayList;

public class ImagePrint extends BasePrint {
    private ProgressDialog progressDialog;
    private ArrayList<String> mImageFiles;
    private Context context;

    public ImagePrint(Context context, MsgHandle mHandle, MsgDialog mDialog) {
        super(context, mHandle, mDialog);
        this.context = context;
    }

    /**
     * set print data
     */

    public ArrayList<String> getFiles() {
        return mImageFiles;
    }

    /**
     * set print data
     */
    public void setFiles(ArrayList<String> files) {
        mImageFiles = files;
    }

    /**
     * do the particular print
     */

    public void doPrint() {

        PrintImages printImages = new PrintImages();
        printImages.execute();

    }

    private class PrintImages extends AsyncTask<String, Void, String> {
        private String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context, com.locationlib.R.style.MyAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... voids) {

            int count = mImageFiles.size();

            for (int i = 0; i < count; i++) {

                String strFile = mImageFiles.get(i);

                mPrintResult = mPrinter.printFile(strFile);

                // if error, stop print next files
                if (mPrintResult.errorCode != ErrorCode.ERROR_NONE) {
                    error = mPrintResult.errorCode.toString();
                    break;
                }
            }
            return error;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null)
                progressDialog.dismiss();
            if (s.equals("")) {
                CustomToasts.getInstance(context).showSuccessToast("Barcode Printed Successfully");
            } else {
                CustomToasts.getInstance(context).showErrorToast(s);
            }
        }
    }
}
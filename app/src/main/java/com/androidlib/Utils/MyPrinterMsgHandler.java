package com.androidlib.Utils;

import android.content.Context;
import android.os.Message;

import com.androidlib.Interfaces.PrinterStatusCallback;
import com.androidlib.brotherPrinter.common.MsgDialog;
import com.androidlib.brotherPrinter.common.MsgHandle;

/**
 * Created by divyanshuPC on 11/16/2017.
 */

public class MyPrinterMsgHandler extends MsgHandle {
    MsgDialog dialog;
    Context context;
    PrinterStatusCallback printerStatusCallback;
    String result = "";
    String battery;

    public MyPrinterMsgHandler(Context context, MsgDialog Dialog, PrinterStatusCallback printerStatusCallback) {
        super(context, Dialog);
        this.context = context;
        dialog = Dialog;
        this.printerStatusCallback = printerStatusCallback;
    }

    @Override
    public void setResult(String results) {
        super.setResult(results);
        result = results;
    }

    @Override
    public void setBattery(String battery) {
        super.setBattery(battery);
        this.battery = battery;
        dialog.close();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        printerStatusCallback.status(result, battery);
    }
}

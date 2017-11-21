/**
 * Base Activity for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.androidlib.brotherPrinter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.androidlib.CustomViews.CustomAlertDialogs;
import com.androidlib.Interfaces.PrintStatus;
import com.androidlib.Interfaces.PrinterStatusCallback;
import com.androidlib.Interfaces.SnackBarCallback;
import com.androidlib.Utils.LibSharedPreferences;
import com.androidlib.Utils.MyPrinterMsgHandler;
import com.androidlib.brotherPrinter.common.Common;
import com.androidlib.brotherPrinter.common.MsgDialog;
import com.androidlib.brotherPrinter.common.MsgHandle;
import com.androidlib.brotherPrinter.printprocess.BasePrint;
import com.androidlib.brotherPrinter.printprocess.ImagePrint;
import com.brother.ptouch.sdk.PrinterInfo;
import com.locationlib.R;


public abstract class BaseActivity extends com.androidlib.GlobalClasses.BaseActivity implements PrinterStatusCallback, PrintStatus {

    static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @TargetApi(12)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        Common.mUsbRequest = 1;
                    else
                        Common.mUsbRequest = 2;
                }
            }
        }
    };
    public BasePrint myPrint = null;
    public MsgHandle mHandle;
    public MsgDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = new MsgDialog(this);
        mHandle = new MyPrinterMsgHandler(this, mDialog, this);
        myPrint = new ImagePrint(this, mHandle, mDialog, this);
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        myPrint.setBluetoothAdapter(bluetoothAdapter);
    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    void printerSettingsButtonOnClick() {
        //startActivity(new Intent(this, Activity_Settings.class));
    }

    /**
     * show message when BACK key is clicked
     */
    /**
     * show the BACK message
     */
    private void showTips() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.end_title)
                .setMessage(R.string.end_message)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {

                                finish();
                            }
                        })
                .setNegativeButton(R.string.button_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {
                            }
                        }).create();
        alertDialog.show();
    }

    /**
     * get the BluetoothAdapter
     */
    public BluetoothAdapter getBluetoothAdapter() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            final Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBtIntent);
        }
        return bluetoothAdapter;
    }

    @TargetApi(12)
    UsbDevice getUsbDevice(UsbManager usbManager) {
        if (myPrint.getPrinterInfo().port != PrinterInfo.Port.USB) {
            return null;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            Message msg = mHandle.obtainMessage(Common.MSG_WRONG_OS);
            mHandle.sendMessage(msg);
            return null;
        }
        UsbDevice usbDevice = myPrint.getUsbDevice(usbManager);
        if (usbDevice == null) {
            Message msg = mHandle.obtainMessage(Common.MSG_NO_USB);
            mHandle.sendMessage(msg);
            return null;
        }

        return usbDevice;
    }

    @TargetApi(12)
    boolean checkUSB() {
        if (myPrint.getPrinterInfo().port != PrinterInfo.Port.USB) {
            return true;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            Message msg = mHandle.obtainMessage(Common.MSG_WRONG_OS);
            mHandle.sendMessage(msg);
            return false;
        }
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = myPrint.getUsbDevice(usbManager);
        if (usbDevice == null) {
            Message msg = mHandle.obtainMessage(Common.MSG_NO_USB);
            mHandle.sendMessage(msg);
            return false;
        }
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
        if (!usbManager.hasPermission(usbDevice)) {
            Common.mUsbRequest = 0;
            usbManager.requestPermission(usbDevice, permissionIntent);
        } else {
            Common.mUsbRequest = 1;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            LibSharedPreferences.getInstance().setString(this, BasePrint.IP_ADDRESS, data.getStringExtra(BasePrint.IP_ADDRESS));
            LibSharedPreferences.getInstance().setString(this, BasePrint.MAC_ADDRESS, data.getStringExtra(BasePrint.MAC_ADDRESS));
            LibSharedPreferences.getInstance().setString(this, BasePrint.MODEL_NUMBER, data.getStringExtra(BasePrint.MODEL_NUMBER));
            onCheckClick();
        }
    }

    public void onCheckClick() {
        myPrint.getPrinterStatus();
    }

    public void onFindClick() {
        Intent intent = new Intent(this, Activity_NetPrinterList.class);
        startActivityForResult(intent, 2);
    }

    @Override
    public void status(String s, String s1) {
        if (s.equals(PrinterInfo.ErrorCode.ERROR_NONE.toString())) {
            ((ImagePrint) myPrint).doPrint();
        } else {
            if (s.equals(PrinterInfo.ErrorCode.ERROR_BROTHER_PRINTER_NOT_FOUND.toString()) || s.equals(PrinterInfo.ErrorCode.ERROR_NO_ADDRESS.toString())) {
                CustomAlertDialogs.showAlertDialog(BaseActivity.this, getString(R.string.printer_error), getString(R.string.printer_not_found_error), new SnackBarCallback() {
                    @Override
                    public void doAction() {
                        onFindClick();
                    }
                });
            } else
                CustomAlertDialogs.showAlertDialog(BaseActivity.this, getString(R.string.printer_error), s, new SnackBarCallback() {
                    @Override
                    public void doAction() {
                        onFindClick();
                    }
                });

        }
    }

}
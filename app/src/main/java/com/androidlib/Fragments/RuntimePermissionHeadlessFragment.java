package com.androidlib.Fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.util.Log;

import com.androidlib.Utils.PermissionUtil;


/**
 * Created by divyanshu.jain on 9/5/2016.
 */
public class RuntimePermissionHeadlessFragment extends Fragment {
    private static final int READ_PERMISSION = 10;
    public static final String TAG = "READ_CALL_LOG";
    private String[] permissionRequired;
    private static int PERMISSION_TYPE = 0;
    private static PermissionCallback mCallback;
    private static boolean isCallLogPermissionDenied;

    public static RuntimePermissionHeadlessFragment newInstance(PermissionCallback callLogPermissionCallback) {

        mCallback = callLogPermissionCallback;
        return new RuntimePermissionHeadlessFragment();
    }

    public void setmCallback(PermissionCallback callLogPermissionCallback) {
        mCallback = callLogPermissionCallback;
    }

    public void addAndCheckPermission(String[] permissionRequired, int permissionType) {
        this.permissionRequired = permissionRequired;
        PERMISSION_TYPE = permissionType;
        checkPermissions();
    }

    public RuntimePermissionHeadlessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (PermissionUtil.hasSelfPermission(getActivity(), permissionRequired) && mCallback != null) {
            mCallback.onPermissionGranted(PERMISSION_TYPE);
        } else {
            // UNCOMMENT TO SUPPORT ANDROID M RUNTIME PERMISSIONS
            if (!isCallLogPermissionDenied) {
               requestPermissions(permissionRequired, READ_PERMISSION);
            }
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (mCallback != null) {
            if (requestCode == READ_PERMISSION) {
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    mCallback.onPermissionGranted(PERMISSION_TYPE);
                } else {
                    Log.i("BaseActivity", "permission was NOT granted.");
                    mCallback.onPermissionDenied(PERMISSION_TYPE);
                }

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

    }

    public interface PermissionCallback {
        void onPermissionGranted(int permissionType);

        void onPermissionDenied(int permissionType);
    }

}

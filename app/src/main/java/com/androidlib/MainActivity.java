package com.androidlib;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.androidlib.Fragments.RuntimePermissionHeadlessFragment;
import com.androidlib.Utils.CommonFunctions;
import com.locationlib.R;

public class MainActivity extends AppCompatActivity implements RuntimePermissionHeadlessFragment.PermissionCallback {
    TextView data;
    private RuntimePermissionHeadlessFragment runtimePermissionHeadlessFragment;
    protected String[] mRequiredPermissions = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = (TextView) findViewById(R.id.data);

        mRequiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        runtimePermissionHeadlessFragment = CommonFunctions.getInstance().addRuntimePermissionFragment(this, this);


    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof RuntimePermissionHeadlessFragment)
            runtimePermissionHeadlessFragment.addAndCheckPermission(mRequiredPermissions, 1);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
    @Override
    public void onPermissionGranted(int permissionType) {
        LocationTracker.getInstance().getCurrentLocation(this, new LocationTracker.GetLocationCallback() {
            @Override
            public void currentLocation(double latitude, double longitude) {
                data.setText("" + latitude + "," + longitude);
            }

            @Override
            public void onFailure(String message) {
                data.setText(message);
            }
        });
    }

    @Override
    public void onPermissionDenied(int permissionType) {

    }
}

package com.androidlib.GlobalClasses;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.androidlib.CustomViews.CustomToasts;
import com.androidlib.Interfaces.RecyclerViewClick;
import com.androidlib.Interfaces.SnackBarCallback;
import com.androidlib.Interfaces.UpdateUiCallback;
import com.androidlib.Utils.CallWebService;
import com.androidlib.Utils.CommonFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by divyanshu on 5/29/2016.
 */
public class BaseActivity extends AppCompatActivity implements CallWebService.ObjectResponseCallBack,CallWebService.ArrayResponseCallback, SnackBarCallback, UpdateUiCallback, RecyclerViewClick {

    @Override
    public void doAction() {

    }

    @Override
    public void updateUi(String string) {

    }

    public void showDialogFragment(DialogFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.show(fragmentManager, fragment.getClass().getName());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level > TRIM_MEMORY_MODERATE) {
            // Restart app so data is reloaded
            android.os.Process.killProcess(android.os.Process.myPid());

        }

    }

    @Override
    public void onClickItem(int position, View view) {

    }

    @Override
    public void onJsonArraySuccess(JSONArray array, int apiType) throws JSONException {

    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {

    }

    @Override
    public void onFailure(String str, int apiType) {
        CustomToasts.getInstance(this).showErrorToast(str);
        //CommonFunctions.getInstance().showErrorSnackBar(this, str);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

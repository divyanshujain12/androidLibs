package com.androidlib.GlobalClasses;


import android.support.v4.app.DialogFragment;
import android.view.View;


import com.androidlib.Interfaces.RecyclerViewClick;
import com.androidlib.Interfaces.SnackBarCallback;
import com.androidlib.Interfaces.UpdateUiCallback;
import com.androidlib.Utils.CallWebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by divyanshu on 9/3/2016.
 */
public class BaseDialogFragment extends DialogFragment implements CallWebService.ObjectResponseCallBack,CallWebService.ArrayResponseCallback, SnackBarCallback, UpdateUiCallback, RecyclerViewClick {

    @Override
    public void doAction() {

    }

    @Override
    public void updateUi(String string) {

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

    }
}

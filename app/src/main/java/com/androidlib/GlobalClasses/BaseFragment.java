package com.androidlib.GlobalClasses;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;


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
public class BaseFragment extends Fragment implements CallWebService.ObjectResponseCallBack,CallWebService.ArrayResponseCallback, SnackBarCallback, UpdateUiCallback, RecyclerViewClick{

    @Override
    public void doAction() {

    }

    @Override
    public void updateUi(String string) {

    }

    @Override
    public void onClickItem(int position, View view) {

    }

    public void showDialogFragment(DialogFragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        fragment.show(fragmentManager, fragment.getClass().getName());

    }


    @Override
    public void onJsonArraySuccess(JSONArray array, int apiType) throws JSONException {

    }

    @Override
    public void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException {

    }

    @Override
    public void onFailure(String str, int apiType) {
        CommonFunctions.getInstance().showErrorSnackBar(getActivity(), str);
    }
}

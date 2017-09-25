package com.androidlib.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.JsonRequest;
import com.android.volley.request.StringRequest;
import com.androidlib.CustomViews.CustomToasts;
import com.androidlib.GlobalClasses.CustomRequest;
import com.androidlib.GlobalClasses.LibInit;
import com.androidlib.Interfaces.Constants;
import com.locationlib.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Divyanshu jain on 30-10-2015.
 */
public class CallWebService implements Response.ErrorListener, Response.Listener {

    private Context context = null;

    public static int GET = Request.Method.GET;
    public static int POST = Request.Method.POST;
    public static int PUT = Request.Method.PUT;
    public static int DELETE = Request.Method.DELETE;
    private ResponseCallback responseCallback;

    private int apiCode = 0;
    private String url = "";
    private Snackbar continuousSB;


    public static CallWebService getInstance(Context context, boolean showProgressBar, int apiCode) {
        CallWebService instance = new CallWebService();
        instance.context = context;
        instance.apiCode = apiCode;
        instance.continuousSB = null;
        if (context != null && showProgressBar)
            instance.continuousSB = CommonFunctions.getInstance().createLoadingSnackBarWithActivity((Activity) context);
        else
            instance.continuousSB = null;
        return instance;
    }

    public void hitJsonObjectRequestAPI(int requestType, final String url, JSONObject json, final ResponseCallback callBackInterface) {
        if (InternetCheck.isInternetOn(context)) {
            responseCallback = callBackInterface;
            cancelRequest(url);
            this.url = url;
            if (continuousSB != null)
                CommonFunctions.showContinuousSB(continuousSB);

            JsonObjectRequest request = new JsonObjectRequest(requestType, url, json == null ? null : (json), this, this);
            addRequestToVolleyQueue(url, request);
        } else {
            CustomToasts.getInstance(context).showErrorToast(context.getString(R.string.no_internet_connection));
        }
    }

    public void hitJsonArrayRequestAPI(int requestType, final String url, JSONArray json, final ResponseCallback callBackinerface) {

        responseCallback = callBackinerface;
        cancelRequest(url);

        this.url = url;

        if (continuousSB != null)
            CommonFunctions.showContinuousSB(continuousSB);

        JsonArrayRequest request = new JsonArrayRequest(requestType, url, json == null ? null : (json), this, this);
        addRequestToVolleyQueue(url, request);
    }


    public void hitCustomRequestAPI(int requestType, final String url, JSONArray json, final ResponseCallback callBackInterface) {
        if (InternetCheck.isInternetOn(context)) {
        responseCallback = callBackInterface;
        cancelRequest(url);
        this.url = url;
        if (continuousSB != null)
            CommonFunctions.showContinuousSB(continuousSB);

        CustomRequest request = new CustomRequest(requestType, url, json == null ? null : (json), this, this);
        addRequestToVolleyQueue(url, request);
    } else {
        CustomToasts.getInstance(context).showErrorToast(context.getString(R.string.no_internet_connection));
    }
    }


    private void addRequestToVolleyQueue(String url, Request request) {
        RetryPolicy policy = new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        LibInit.getInstance().addToRequestQueue(request, url);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        LibInit.getInstance().getRequestQueue().getCache().invalidate(url, true);
        error = configureErrorMessage(error);
        onError(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        LibInit.getInstance().getRequestQueue().getCache().invalidate(url, true);
        if (continuousSB != null)
            CommonFunctions.hideContinuousSB(continuousSB);

        if (response instanceof JSONObject) {
            onJsonObjectResponse((JSONObject) response);
        } else if (response instanceof JSONArray) {
            onJsonArrayResponse((JSONArray) response);
        }

    }

    private void onJsonObjectResponse(JSONObject response) {
        try {
            if (response.getBoolean(Constants.STATUS_CODE)) {
                if (responseCallback != null)
                    responseCallback.onSuccess(response, apiCode);
            } else
                onError(response.getString(Constants.MESSAGE));
        } catch (final JSONException e) {
            onError(e.getMessage());
            e.printStackTrace();
        }
    }


    private void onJsonArrayResponse(JSONArray response) {
        try {
            responseCallback.onSuccess(response, apiCode);
        } catch (final JSONException e) {
            onError(e.getMessage());
            e.printStackTrace();
        }
    }

    public interface ResponseCallback {

        void onSuccess(Object data, int apiType) throws JSONException;

        void onFailure(String str, int apiType);

    }

    private void onError(String error) {

        if (continuousSB != null)
            CommonFunctions.hideContinuousSB(continuousSB);
        if (responseCallback != null)
            responseCallback.onFailure(error, apiCode);
        //  CustomToasts.getInstance(context).showErrorToast(error);
    }

    public static VolleyError configureErrorMessage(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }

    private void cancelRequest(String url) {
        if (this.url.equals(url))
            LibInit.getInstance().cancelPendingRequests(url);
    }

   /* private void sendErrorToView() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.postDelayed(runnable, 2000);
    }

    Handler handler = new Handler();*/
}
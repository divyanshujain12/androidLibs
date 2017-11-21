package com.androidlib.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.androidlib.CustomViews.CustomToasts;
import com.androidlib.GlobalClasses.LibInit;
import com.androidlib.Interfaces.LibConstants;
import com.locationlib.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Divyanshu jain on 30-10-2015.
 */
public class CallWebService implements Response.ErrorListener, Response.Listener {

    private Context context = null;

    public static int GET = Request.Method.GET;
    public static int POST = Request.Method.POST;
    public static int PUT = Request.Method.PUT;
    public static int DELETE = Request.Method.DELETE;
    private ObjectResponseCallBack objectCallBackInterface;
    private ArrayResponseCallback arrayCallBackInterface;
    private int apiCode = 0;
    private String url = "";
    //private Snackbar continuousSB;
    private static String username, password, authKey;
    private ProgressDialog progressDialog;
    public static CallWebService getInstance(Context context, boolean showProgressBar, int apiCode) {
        CallWebService instance = new CallWebService();
        instance.context = context;
        instance.apiCode = apiCode;
        //instance.continuousSB = null;
        if (context != null && showProgressBar){
            instance.progressDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
            instance.progressDialog.setCancelable(false);
            instance.progressDialog.setMessage("Loading Data..");
        }

        else{
            instance.progressDialog = null;
        }

        return instance;
    }

    public void setHeadersValue(String userid, String pass, String authKey) {
        username = userid;
        password = pass;
        authKey = authKey;
    }
    public void hitJsonObjectRequestAPI(int requestType, final String url, JSONObject json, final ObjectResponseCallBack callBackInterface) {
        if (InternetCheck.isInternetOn(context)) {
            objectCallBackInterface = callBackInterface;
            cancelRequest(url);
            this.url = url;
            if (progressDialog != null)
                progressDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(requestType, url, json == null ? null : (json), this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String credentials = username + ":" + password;
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", auth);
                    return params;
                }
            };
            addRequestToVolleyQueue(url, request);
        } else {
            CustomToasts.getInstance(context).showErrorToast(context.getString(R.string.no_internet_connection));
        }
    }

    public void hitJsonArrayRequestAPI(int requestType, final String url, JSONArray json, final ArrayResponseCallback callBackinerface) {
        arrayCallBackInterface = callBackinerface;
        cancelRequest(url);

        this.url = url;

        if (progressDialog != null)
            progressDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(requestType, url, json == null ? null : (json), this, this);
        addRequestToVolleyQueue(url, request);
    }

    private void addRequestToVolleyQueue(String url, Request request) {
        RetryPolicy policy = new DefaultRetryPolicy(LibConstants.REQUEST_TIMEOUT_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        LibInit.getInstance().addToRequestQueue(request, url);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        LibInit.getInstance().getRequestQueue().getCache().invalidate(url, true);
        onError(configureErrorMessage(error));
    }

    @Override
    public void onResponse(Object response) {
        LibInit.getInstance().getRequestQueue().getCache().invalidate(url, true);
        if (progressDialog != null)
            progressDialog.hide();

        if (response instanceof JSONObject) {
            onJsonObjectResponse((JSONObject) response);
        } else if (response instanceof JSONArray) {
            onJsonArrayResponse((JSONArray) response);
        }

    }

    private void onJsonObjectResponse(JSONObject response) {
        try {
            if (response.keys().hasNext()) {
                String key = response.keys().next();
                if (response.get(key) != null) {
                    if (objectCallBackInterface != null)
                        objectCallBackInterface.onJsonObjectSuccess(response, apiCode);
                } else {
                    objectCallBackInterface.onFailure("Data Not Available!", apiCode);
                }
            } else {
                objectCallBackInterface.onFailure("Data Not Available!", apiCode);
            }
//
        } catch (final JSONException e) {
            onError(e.getMessage());
            e.printStackTrace();
        }
    }


    private void onJsonArrayResponse(JSONArray response) {
        try {
            if (response.get(0) != null) {
                arrayCallBackInterface.onJsonArraySuccess(response, apiCode);
            } else {
                arrayCallBackInterface.onFailure("Data Not Available!", apiCode);
            }
        } catch (final JSONException e) {
            onError(e.getMessage());
            e.printStackTrace();
        }
    }


    public interface ObjectResponseCallBack {

        void onJsonObjectSuccess(JSONObject response, int apiType) throws JSONException;

        void onFailure(String str, int apiType);

    }

    public interface ArrayResponseCallback {

        void onJsonArraySuccess(JSONArray array, int apiType) throws JSONException;

        void onFailure(String str, int apiType);

    }

    private void onError(String error) {

        if (progressDialog != null)
            progressDialog.hide();
        if (objectCallBackInterface != null)
            objectCallBackInterface.onFailure(error, apiCode);
        //  CustomToasts.getInstance(context).showErrorToast(error);
    }

    public static String configureErrorMessage(VolleyError volleyError) {
        String message = "";
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        else{
            if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                message = error.getMessage();
            }
        }
        return message;
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
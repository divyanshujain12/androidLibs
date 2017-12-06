package com.androidlib.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
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
import com.androidlib.Utils.InternetCheck;
import com.locationlib.R.string;
import com.locationlib.R.style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CallWebService implements ErrorListener, Listener {
    private Context context = null;
    public static int GET = 0;
    public static int POST = 1;
    public static int PUT = 2;
    public static int DELETE = 3;
    private CallWebService.ObjectResponseCallBack objectCallBackInterface;
    private CallWebService.ArrayResponseCallback arrayCallBackInterface;
    private int apiCode = 0;
    private String url = "";
    private static String username;
    private static String password;
    private static String authKey;
    private ProgressDialog progressDialog;

    public CallWebService() {
    }

    public static CallWebService getInstance(Context context, boolean showProgressBar, int apiCode) {
        CallWebService instance = new CallWebService();
        instance.context = context;
        instance.apiCode = apiCode;
        if (context != null && showProgressBar) {
            instance.progressDialog = new ProgressDialog(context, style.MyAlertDialogStyle);
            instance.progressDialog.setCancelable(false);
            instance.progressDialog.setMessage("Loading Data..");
        } else {
            instance.progressDialog = null;
        }

        return instance;
    }

    public void setHeadersValue(String userid, String pass, String authKey) {
        username = userid;
        password = pass;
    }

    public void hitJsonObjectRequestAPI(final int requestType, final String url, JSONObject json, CallWebService.ObjectResponseCallBack callBackInterface) {
        if (InternetCheck.isInternetOn(this.context)) {
            this.objectCallBackInterface = callBackInterface;
            this.cancelRequest(url);
            this.url = url;
            if (this.progressDialog != null) {
                this.progressDialog.show();
            }

            JsonObjectRequest request = new JsonObjectRequest(requestType, url, json == null ? null : json, this, this) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap params = new HashMap();
                    String credentials = CallWebService.username + ":" + CallWebService.password;
                    String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), 2);
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", auth);
                    return params;
                }
            };
            this.addRequestToVolleyQueue(url, request);
        } else {
            CustomToasts.getInstance(this.context).showErrorToast(this.context.getString(string.no_internet_connection));
        }

    }

    public void hitJsonArrayRequestAPI(int requestType, String url, JSONArray json, CallWebService.ArrayResponseCallback callBackinerface) {
        this.arrayCallBackInterface = callBackinerface;
        this.cancelRequest(url);
        this.url = url;
        if (this.progressDialog != null) {
            this.progressDialog.show();
        }

        JsonArrayRequest request = new JsonArrayRequest(requestType, url, json == null ? null : json, this, this);
        this.addRequestToVolleyQueue(url, request);
    }

    private void addRequestToVolleyQueue(String url, Request request) {
        DefaultRetryPolicy policy = new DefaultRetryPolicy(30000, 0, 1.0F);
        request.setRetryPolicy(policy);
        LibInit.getInstance().addToRequestQueue(request, url);
    }

    public void onErrorResponse(VolleyError error) {
        LibInit.getInstance().getRequestQueue().getCache().invalidate(this.url, true);
        this.onError(configureErrorMessage(error));
    }

    public void onResponse(Object response) {
        LibInit.getInstance().getRequestQueue().getCache().invalidate(this.url, true);
        if (this.progressDialog != null) {
            this.progressDialog.hide();
        }

        if (response instanceof JSONObject) {
            this.onJsonObjectResponse((JSONObject) response);
        } else if (response instanceof JSONArray) {
            this.onJsonArrayResponse((JSONArray) response);
        }

    }

    private void onJsonObjectResponse(JSONObject response) {
        try {
            if (response.has("errors")) {
                this.objectCallBackInterface.onFailure("Some Error Occured", this.apiCode);
            } else
                checkNullValue(response);
        } catch (JSONException var3) {
            this.onError(var3.getMessage());
            var3.printStackTrace();
        }

    }

    private void checkNullValue(JSONObject response) throws JSONException {
        String e = "";
        if (response.keys().hasNext()) {
            e = (String) response.keys().next();
            if (!response.isNull(e)) {
                if (this.objectCallBackInterface != null) {
                    this.objectCallBackInterface.onJsonObjectSuccess(response, this.apiCode);
                }
            } else {
                this.objectCallBackInterface.onFailure("Data Not Available for" + e, this.apiCode);
            }
        } else {
            this.objectCallBackInterface.onFailure("Data Not Available for" + e, this.apiCode);
        }
    }

    private void onJsonArrayResponse(JSONArray response) {
        try {
            if (response.isNull(0)) {
                this.arrayCallBackInterface.onJsonArraySuccess(response, this.apiCode);
            } else {
                this.arrayCallBackInterface.onFailure("Data Not Available!", this.apiCode);
            }
        } catch (JSONException var3) {
            this.onError(var3.getMessage());
            var3.printStackTrace();
        }

    }

    private void onError(String error) {
        if (this.progressDialog != null) {
            this.progressDialog.hide();
        }

        if (this.objectCallBackInterface != null) {
            this.objectCallBackInterface.onFailure(error, this.apiCode);
        }

    }

    public static String configureErrorMessage(VolleyError volleyError) {
        String message = "";
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Invalid Credential";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        } else if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            message = error.getMessage();
        }

        return message;
    }

    private void cancelRequest(String url) {
        if (this.url.equals(url)) {
            LibInit.getInstance().cancelPendingRequests(url);
        }

    }

    public interface ArrayResponseCallback {
        void onJsonArraySuccess(JSONArray var1, int var2) throws JSONException;

        void onFailure(String var1, int var2);
    }

    public interface ObjectResponseCallBack {
        void onJsonObjectSuccess(JSONObject var1, int var2) throws JSONException;

        void onFailure(String var1, int var2);
    }
}
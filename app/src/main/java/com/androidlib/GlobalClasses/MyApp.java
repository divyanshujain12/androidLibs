package com.androidlib.GlobalClasses;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
//import com.example.divyanshu.smyt.Utils.LruBitmapCache;

/**
 * Created by divyanshu.jain on 9/15/2016.
 */
public class MyApp extends Application {
    private static final String TAG = MyApp.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static MyApp mInstance;


    @Override
    public void onCreate() {

        super.onCreate();
        mInstance = this;

    }

    public static synchronized MyApp getInstance() {
        if (mInstance == null)
            mInstance = new MyApp();
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
       /* if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }*/
        return this.mImageLoader;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
        Log.w(TAG, "Low Memory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

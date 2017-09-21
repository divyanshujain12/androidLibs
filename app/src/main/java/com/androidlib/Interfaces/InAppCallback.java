package com.androidlib.Interfaces;

/**
 * Created by divyanshu on 3/12/16.
 */

public interface InAppCallback {
    void onPurchasedSuccess(String productID, String transactionID);
    void onFailure();
}

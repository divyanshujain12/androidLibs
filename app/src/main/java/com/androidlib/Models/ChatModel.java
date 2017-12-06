package com.androidlib.Models;

import android.databinding.BaseObservable;

/**
 * Created by divyanshu.jain on 12/6/2017.
 */

public class ChatModel extends BaseObservable {
    String content;
    int sameSide;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSameSide() {
        return sameSide;
    }

    public void setSameSide(int sameSide) {
        this.sameSide = sameSide;
    }
}

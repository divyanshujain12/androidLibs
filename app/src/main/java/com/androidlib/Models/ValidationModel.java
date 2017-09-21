package com.androidlib.Models;

import android.view.View;

public class ValidationModel {
    public View view;
    public int validationType;
    public String errorMessage;


    public ValidationModel(View view, int validationType, String errorMessage) {
        this.view = view;
        this.validationType = validationType;
        this.errorMessage = errorMessage;
    }

    public View getView() {
        return view;
    }

    public void setView(View editText) {
        this.view = editText;
    }

    public int getValidationType() {
        return validationType;
    }

    public void setValidationType(int validationType) {
        this.validationType = validationType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        return view == ((ValidationModel) o).getView();
    }
}
package com.androidlib.Models;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by divyanshu.jain on 10/23/2017.
 */

public class BottleInfoModel extends BaseObservable implements Parcelable{
    int id;
    int leftAmount;
    int rightAmount;
    String leftStartTimings;
    String leftStopTimings;
    String rightStartTimings;
    String rightStopTimings;
    String notes;
    int priority;
    String status;
    String bottleQRGeneratorId;
    int motherId;
    String createdAt;
public BottleInfoModel(){}
    protected BottleInfoModel(Parcel in) {
        id = in.readInt();
        leftAmount = in.readInt();
        rightAmount = in.readInt();
        leftStartTimings = in.readString();
        leftStopTimings = in.readString();
        rightStartTimings = in.readString();
        rightStopTimings = in.readString();
        notes = in.readString();
        priority = in.readInt();
        status = in.readString();
        bottleQRGeneratorId = in.readString();
        motherId = in.readInt();
        createdAt = in.readString();
    }

    public static final Creator<BottleInfoModel> CREATOR = new Creator<BottleInfoModel>() {
        @Override
        public BottleInfoModel createFromParcel(Parcel in) {
            return new BottleInfoModel(in);
        }

        @Override
        public BottleInfoModel[] newArray(int size) {
            return new BottleInfoModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(int leftAmount) {
        this.leftAmount = leftAmount;
    }

    public int getRightAmount() {
        return rightAmount;
    }

    public void setRightAmount(int rightAmount) {
        this.rightAmount = rightAmount;
    }

    public String getLeftStartTimings() {
        return leftStartTimings;
    }

    public void setLeftStartTimings(String leftStartTimings) {
        this.leftStartTimings = leftStartTimings;
    }

    public String getLeftStopTimings() {
        return leftStopTimings;
    }

    public void setLeftStopTimings(String leftStopTimings) {
        this.leftStopTimings = leftStopTimings;
    }

    public String getRightStartTimings() {
        return rightStartTimings;
    }

    public void setRightStartTimings(String rightStartTimings) {
        this.rightStartTimings = rightStartTimings;
    }

    public String getRightStopTimings() {
        return rightStopTimings;
    }

    public void setRightStopTimings(String rightStopTimings) {
        this.rightStopTimings = rightStopTimings;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBottleQRGeneratorId() {
        return bottleQRGeneratorId;
    }

    public void setBottleQRGeneratorId(String bottleQRGeneratorId) {
        this.bottleQRGeneratorId = bottleQRGeneratorId;
    }

    public int getMotherId() {
        return motherId;
    }

    public void setMotherId(int motherId) {
        this.motherId = motherId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(leftAmount);
        dest.writeInt(rightAmount);
        dest.writeString(leftStartTimings);
        dest.writeString(leftStopTimings);
        dest.writeString(rightStartTimings);
        dest.writeString(rightStopTimings);
        dest.writeString(notes);
        dest.writeInt(priority);
        dest.writeString(status);
        dest.writeString(bottleQRGeneratorId);
        dest.writeInt(motherId);
        dest.writeString(createdAt);
    }
}

package com.androidlib.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidlib.CustomFontViews.CustomTextviewRegular;
import com.androidlib.Models.AllTypeUserModel;
import com.locationlib.R;
import com.locationlib.databinding.SingleTextviewRegularBinding;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 12/6/2017.
 */

public class AllTypeUserSPAdapter extends ArrayAdapter<AllTypeUserModel> {

    private ArrayList<AllTypeUserModel> allTypeUserModels = new ArrayList<>();
    private Context context;

    public AllTypeUserSPAdapter(Context context, int resource, ArrayList<AllTypeUserModel> objects) {
        super(context, resource, objects);
        this.allTypeUserModels = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_textview_regular, parent, false);

        AllTypeUserModel allTypeUserModel = allTypeUserModels.get(position);
        ((CustomTextviewRegular)row).setText(allTypeUserModel.getName());
//        singleTextviewRegularBinding = DataBindingUtil.bind(row);
//        singleTextviewRegularBinding.setData(allTypeUserModel);
//        singleTextviewRegularBinding.executePendingBindings();

        return row;
    }
}


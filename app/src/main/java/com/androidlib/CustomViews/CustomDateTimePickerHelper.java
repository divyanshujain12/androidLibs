package com.androidlib.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.androidlib.Utils.Utils;
import com.locationlib.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by divyanshu.jain on 9/20/2016.
 */
public class CustomDateTimePickerHelper implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    public static CustomDateTimePickerHelper getInstance() {
        return new CustomDateTimePickerHelper();
    }


    private TextView dateTimeTV;
    private static Calendar mcurrentDate = Calendar.getInstance();
    private String DateFormat = "";

 /*
       Custom Date Picker
     */

    public void showDateDialog(Activity context, final TextView textView, final String DateFormat) {
        this.DateFormat = DateFormat;
        dateTimeTV = textView;
        mcurrentDate = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                mcurrentDate.get(Calendar.YEAR),
                mcurrentDate.get(Calendar.MONTH),
                mcurrentDate.get(Calendar.DAY_OF_MONTH)
        );
        //dpd.setMinDate(mcurrentDate);
        dpd.show(context.getFragmentManager(), "DatePickerDialog");
    }

    /*
      Custom Time Picker
    */
    public void showTimeDialog(Activity context, final TextView textView, int pos) {
        dateTimeTV = textView;
        mcurrentDate = Calendar.getInstance();
       // if (pos > 0)
        mcurrentDate.setTime(Utils.getCurrentSelectedDate(textView.getText().toString(), Utils.TIME_FORMAT));
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                mcurrentDate.get(Calendar.HOUR_OF_DAY),
                mcurrentDate.get(Calendar.MINUTE),
                false
        );
        /*if (!isDateAheadToday(textView))
        tpd.setMinTime(mcurrentDate.get(Calendar.HOUR_OF_DAY), mcurrentDate.get(Calendar.MINUTE), mcurrentDate.get(Calendar.SECOND));*/
        tpd.show(context.getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mcurrentDate.set(Calendar.YEAR, year);
        mcurrentDate.set(Calendar.MONTH, monthOfYear);
        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateTimeTV.setText(Utils.formatDateAndTime(mcurrentDate.getTimeInMillis(), DateFormat));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);

        dateTimeTV.setText(Utils.formatDateAndTime(myCalendar.getTimeInMillis(), Utils.TIME_FORMAT));
    }


    public void showErrorMessage(Context context, int i, String errorMsg) {
        Toast.makeText(context, String.format(errorMsg, String.valueOf(i + 1)), Toast.LENGTH_SHORT).show();
    }



}
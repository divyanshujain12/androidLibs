package com.androidlib.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.locationlib.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by divyanshu.jain on 8/29/2016.
 */
public class Utils {
    static String DAYS = "days";
    static String HOURS = "hrs";
    static String MINUTES = "mins";
    static String SECONDS = "secs";
    static String AGO = "ago";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "hh:mm a";
    public static final String DEFAULT_DATE = "1940-01-01";
    public static final String POST_CHALLENGE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
    public static final String CURRENT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CURRENT_DATE_FORMAT_NON_STATIC = "EEEE, MMMMM d, yyyy";
    public static final String DATE_IN_DAY_FIRST = "EEEE, MMMMM d, yyyy";
    private SimpleDateFormat format;
    private static Utils utils;

    public static Utils getInstance() {
        if (utils == null)
            utils = new Utils();
        return utils;
    }

    public static Bitmap getRoundedCornerBitmap(Context context, int resource) {
        Bitmap mbitmap = ((BitmapDrawable) context.getResources().getDrawable(resource)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);

        return imageRounded;
    }

    public static void configureToolbarWithBackButton(final AppCompatActivity appCompatActivity, Toolbar toolbar, String name) {
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.onBackPressed();
            }
        });
    }

    public static void configureToolbarForHomeActivity(final AppCompatActivity appCompatActivity, Toolbar toolbar, String name) {
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(name);
        actionBar.setSubtitle(appCompatActivity.getString(R.string.click_for_desc));
       /* actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.onBackPressed();
            }
        });
    }

    public static void configureToolbarWithOutBackButton(final AppCompatActivity appCompatActivity, Toolbar toolbar, String name) {
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.onBackPressed();
            }
        });
    }

    public static String getChallengeTimeDifference(String time) {
        String timeDifference = "";
        Date currentDate = Calendar.getInstance().getTime();
        Date givenDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CURRENT_DATE_FORMAT);
        try {
            givenDate = simpleDateFormat.parse(time);
            long differenceInMillisecond = givenDate.getTime() - currentDate.getTime();
            timeDifference = getChallengeTimeDifference(differenceInMillisecond);
            return timeDifference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDifference;
    }

    public static String getUploadedTimeDifference(String time) {
        String timeDifference = "";
        Date currentDate = Calendar.getInstance().getTime();
        Date givenDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CURRENT_DATE_FORMAT);
        try {
            givenDate = simpleDateFormat.parse(time);
            long differenceInMillisecond = currentDate.getTime() - givenDate.getTime();
            timeDifference = getChallengeTimeDifference(differenceInMillisecond) + " " + AGO;

            return timeDifference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDifference;
    }

    public static String formatDateAndTime(long date, String format) {
        Date dt = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
        return sdf.format(dt);
    }

    public static Date getCurrentSelectedDate(String selectedDate, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            return format.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromDateTmeString(String dateValue, String timeValue) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(POST_CHALLENGE_TIME_FORMAT, Locale.getDefault());
        dateValue = dateValue + " " + timeValue;
        return simpleDateFormat.parse(dateValue);
    }

    public static String getDateInTwentyFourHoursFormat(String selectedDate, String time) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        try {
            Date date = parseFormat.parse(time);
            String formattedTime = displayFormat.format(date);
            return selectedDate + " " + formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentTime(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat displayFormat = new SimpleDateFormat(format, Locale.getDefault());
        return displayFormat.format(calendar.getTime());
    }


    public static String getChallengeTimeDifference(long difference) {
        String timePostString = " ago";
        System.out.println("different : " + difference);
        long currentTimeDifference = getCurrentTimeInMillisecond() - difference;
        if (currentTimeDifference < 0) {
            currentTimeDifference = difference - getCurrentTimeInMillisecond();
            timePostString = " left";
        }
        return getDifferenceInString(currentTimeDifference) + timePostString;
    }

    public static String getVideoUploadTimeDifference(long different) {
        long currentTimeDifference = different - getCurrentTimeInMillisecond();
        return getDifferenceInString(currentTimeDifference);
    }

    public static String getDifferenceInString(long time) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = time / daysInMilli;
        if (elapsedDays > 0) {
            return String.format(Locale.getDefault(), "%d days", elapsedDays);
        }
        time = time % daysInMilli;

        long elapsedHours = time / hoursInMilli;
        time = time % hoursInMilli;
        if (elapsedDays != 0) {
            return String.format(Locale.getDefault(), " %d hr", elapsedHours);
        }
        long elapsedMinutes = time / minutesInMilli;
        time = time % minutesInMilli;
        if (elapsedHours != 0) {
            return String.format(Locale.getDefault(), "%d min", elapsedMinutes);
        }
        long elapsedSeconds = time / secondsInMilli;
        return String.format(Locale.getDefault(), "%d sec", elapsedSeconds);
    }


    public static long getCurrentTimeInMillisecond() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long getNextSeventyTwoHoursInMS(long milliseconds) {
        milliseconds = milliseconds + (86400000 * 3);
        return milliseconds;
    }

    public static long getNextTenMinuteInMS(long milliseconds) {
        milliseconds = milliseconds + 600000;
        return milliseconds;
    }

    public static long getPreviousOneMinuteInMS(long milliseconds) {
        milliseconds = milliseconds - 60000;
        return milliseconds;
    }

    public static boolean isTimeGone(long milliseconds) {
        long difference = (milliseconds + 600000) - getCurrentTimeInMillisecond();
        if (difference > 0)
            return false;
        else
            return true;
    }

    public String getTimeFromTformat(String time) {

        format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date date = format.parse(time);
            return formatDateAndTime(date.getTime(), TIME_FORMAT);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTimeFromTFormatToGivenFormat(String time, String dateTimeFormat) {

        format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        format.setTimeZone(Calendar.getInstance().getTimeZone());
        try {
            Date date = format.parse(time);
            return formatDateAndTime(date.getTime(), dateTimeFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
    public long getTimeInMS(String selectedDate, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        format.setTimeZone(Calendar.getInstance().getTimeZone());
        try {
            return format.parse(selectedDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public String getTimeDifference(String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        format.setTimeZone(Calendar.getInstance().getTimeZone());
        try {
            Date startDate = format.parse(startTime);
            Date endDate = format.parse(endTime);

            return getDifferenceInString(endDate.getTime() - startDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getTimeDifferenceFromCurrent(long timeInMS) {
        return timeInMS - getCurrentTimeInMillisecond();
    }

    public static String getTimeInHMS(long millis) throws ParseException {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

    }

    public static boolean isDifferenceLowerThanTwentyFourHours(long milliseconds) {
        long diff = (getCurrentTimeInMillisecond() - milliseconds) / (60 * 60 * 1000);
        if (diff > 24)
            return false;
        else {
            return true;
        }
    }
}

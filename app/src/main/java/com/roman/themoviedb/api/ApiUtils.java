package com.roman.themoviedb.api;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ApiUtils {

    /**
     *
     * @return today's date for API requests.
     */
    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    /**
     *
     * @return current language and country set on device for API requests.
     */
    @NonNull
    public static String getLanguage(){
        String lang = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();
        return lang + '-' + country;
    }

}

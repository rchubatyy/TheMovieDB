package com.roman.themoviedb;

import android.app.Application;

import com.roman.themoviedb.api.RequestsData;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Roman on 06/04/2018.
 */

public class AppDelegate extends Application {

    public static Retrofit retrofitInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        // Loads Retrofit for HTTP API requests
        retrofitInstance = new Retrofit.Builder()
                .baseUrl(RequestsData.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();

    }


}

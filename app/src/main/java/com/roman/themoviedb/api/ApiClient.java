package com.roman.themoviedb.api;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.roman.themoviedb.AppDelegate;
import com.roman.themoviedb.models.Movie;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Roman on 06/04/2018.
 */

public class ApiClient {

    private static final String CREDITS = "credits";
    private static final String VIDEOS = "videos";

    public static void discoverMoviesRequest(ApiCallbacks callback, String sortBy,
                                      int page, int voteCount){

            final ApiCallbacks apiCallback = callback;
            final int requestCode = RequestsData.DISCOVER_REQUEST_CODE;

            ApiInterface service = AppDelegate.retrofitInstance.create(ApiInterface.class);

            Call<JsonObject> call = service.discoverMovies(RequestsData.API_KEY, sortBy, ApiUtils.getLanguage(), ApiUtils.getCurrentDate(), page, voteCount);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                    if (response.isSuccessful()) {
                        JsonObject responseObject = Objects.requireNonNull(response.body()).getAsJsonObject();
                        apiCallback.onApiClientSuccess(requestCode,
                                JsonParser.parseMovies(responseObject),
                                JsonParser.getNumPages(responseObject));
                    }
                    else
                        apiCallback.onApiClientError();

                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    apiCallback.onFailedResponse();
                }
            });
        }

    public static void searchMoviesRequest(ApiCallbacks callback, String query,
                                      int page){
            final ApiCallbacks apiCallback = callback;
            final int requestCode = RequestsData.SEARCH_REQUEST_CODE;

            ApiInterface service = AppDelegate.retrofitInstance.create(ApiInterface.class);

            Call<JsonObject> call = service.searchMovies(RequestsData.API_KEY, ApiUtils.getLanguage(), query, page);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        JsonObject responseObject = Objects.requireNonNull(response.body()).getAsJsonObject();
                        apiCallback.onApiClientSuccess(requestCode,
                                JsonParser.parseMovies(responseObject),
                                JsonParser.getNumPages(responseObject));
                    }
                    else
                        apiCallback.onApiClientError();

                }
                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    apiCallback.onFailedResponse();
                }
            });
    }

    public static void getMovieRequest(ApiCallbacks callback, int movieId){

            final ApiCallbacks apiCallback = callback;
            final int requestCode = RequestsData.DETAILS_REQUEST_CODE;

            ApiInterface service = AppDelegate.retrofitInstance.create(ApiInterface.class);

            Call<JsonObject> call = service.getMovie(movieId, RequestsData.API_KEY, ApiUtils.getLanguage(),
                    CREDITS + ',' + VIDEOS);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        apiCallback.onApiClientSuccess(requestCode,
                                JsonParser.parseMovieWithDetails(Objects.requireNonNull(response.body()).getAsJsonObject()),0);
                    }
                    else
                        apiCallback.onApiClientError();
                }
                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    apiCallback.onFailedResponse();
                }
            });
    }

    public interface ApiCallbacks {
        void onApiClientSuccess(int requestCode, ArrayList<Movie> objects, int numPages);
        void onApiClientError();
        void onFailedResponse();
    }
}

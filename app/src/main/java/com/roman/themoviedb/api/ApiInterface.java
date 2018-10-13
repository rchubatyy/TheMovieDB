package com.roman.themoviedb.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Roman on 06/04/2018.
 */

public interface ApiInterface {
    @GET("discover/movie")
    Call<JsonObject> discoverMovies(
            @Query("api_key") String apiKey,
            @Query("sort_by") String sortBy,
            @Query("language") String language,
            @Query("primary_release_date.lte") String primaryReleaseDate,
            @Query("page") int page,
            @Query("vote_count.gte") int voteCount
    );

    @GET("search/movie")
    Call<JsonObject> searchMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<JsonObject> getMovie(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("append_to_response") String appendToResponse
    );

}

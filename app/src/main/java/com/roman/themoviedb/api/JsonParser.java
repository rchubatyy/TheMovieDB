package com.roman.themoviedb.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.roman.themoviedb.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Roman on 06/04/2018.
 */

public class JsonParser {

    private static final String JSON_RESULTS = "results";
    private static final String JSON_TOTAL_PAGES = "total_pages";

    /**
     * Parses movies data from JSON result
     * @param object JSON with movie data
     * @return array with movie model
     */
    public static ArrayList<Movie> parseMovies(@NotNull JsonObject object) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (object.has(JSON_RESULTS)) {
            JsonArray results = object.get(JSON_RESULTS).getAsJsonArray();
            for (Object movie: results)
                if (movie instanceof JsonObject)
                    movies.add(new Movie( (JsonObject) movie ));
        }
        return movies;
    }

    /**
     * Parses movie with cast and trailer
     * @param object JSON with movie data
     * @return array with that movie
     */
    public static ArrayList<Movie> parseMovieWithDetails(@NotNull JsonObject object) {
        ArrayList<Movie> movie = new ArrayList<>();
        movie.add(new Movie(object));
        return movie;
    }

    /**
     * Gets total number of pages available
     * @param object JSON with movie data
     * @return number of pages
     */
    public static int getNumPages(@NotNull JsonObject object){
        return object.has(JSON_TOTAL_PAGES) ? object.get(JSON_TOTAL_PAGES).getAsInt() : 0;
    }
}

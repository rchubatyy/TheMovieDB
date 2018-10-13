package com.roman.themoviedb.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.roman.themoviedb.api.RequestsData;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcel;

import java.util.LinkedHashMap;

/**
 * Created by Roman on 07/04/2018.
 */

@Parcel
public class Movie {

    //JSON properties to be loaded from JSON object to Java object.
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_BACKDROP_PATH = "backdrop_path";
    private static final String JSON_VIDEOS = "videos";
    private static final String JSON_RESULTS = "results";
    private static final String JSON_SITE = "site";
    private static final String JSON_YOUTUBE = "YouTube";
    private static final String JSON_TYPE = "type";
    private static final String JSON_KEY = "key";
    private static final String JSON_TRAILER = "Trailer";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_RELEASE_DATE = "release_date";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_OVERVIEW = "overview";
    private static final String JSON_TAGLINE = "tagline";
    private static final String JSON_CREDITS = "credits";
    private static final String JSON_CAST = "cast";
    private static final String JSON_NAME = "name";
    private static final String JSON_PROFILE_PATH = "profile_path";

    //Movie properties, but only those that are needed to be loaded.
    int id;
    float rating;
    String title, backdropPath, trailer, posterPath, releaseDate, overview, tagline;
    // List of all actors ordered by their importance, with their photos.
    LinkedHashMap<String, String> cast;

    Movie(){}
    //Movie model is initialized from JSON object.
    public Movie(@NotNull JsonObject data){
        this.id = data.has(JSON_ID) ? data.get(JSON_ID).getAsInt() : 0;
        this.title = data.has(JSON_TITLE) ? data.get(JSON_TITLE).getAsString() : "";
        this.backdropPath = (data.has(JSON_BACKDROP_PATH) && !data.get(JSON_BACKDROP_PATH).isJsonNull()) ? data.get(JSON_BACKDROP_PATH).getAsString() : "";
        this.posterPath = (data.has(JSON_POSTER_PATH) && !data.get(JSON_POSTER_PATH).isJsonNull()) ? data.get(JSON_POSTER_PATH).getAsString() : "";
        this.overview = data.has(JSON_OVERVIEW) ? data.get(JSON_OVERVIEW).getAsString() : "";
        this.releaseDate = data.has(JSON_RELEASE_DATE) ? data.get(JSON_RELEASE_DATE).getAsString() : "";
        this.rating = data.has(JSON_VOTE_AVERAGE) ? data.get(JSON_VOTE_AVERAGE).getAsFloat() : 0;
        this.tagline = data.has(JSON_TAGLINE) ? data.get(JSON_TAGLINE).getAsString() : "";
        if (data.has(JSON_CREDITS)){
            JsonObject credits = data.get(JSON_CREDITS).getAsJsonObject();
            if (credits.has(JSON_CAST)) {
                cast=new LinkedHashMap<>();
                JsonArray castArray = credits.get(JSON_CAST).getAsJsonArray();
                for (Object actor: castArray)
                    if (actor instanceof JsonObject) {
                        JsonObject actorData = (JsonObject) actor;
                        if (actorData.has(JSON_NAME) && actorData.has(JSON_PROFILE_PATH))
                            cast.put(actorData.get(JSON_NAME).getAsString(),
                                    actorData.get(JSON_PROFILE_PATH).isJsonNull() ? "" : actorData.get(JSON_PROFILE_PATH).getAsString());
                    }
            }
        }
        if (data.has(JSON_VIDEOS)) {
            JsonObject videos = data.get(JSON_VIDEOS).getAsJsonObject();
            if (videos.has(JSON_RESULTS)){
                JsonArray results = videos.get(JSON_RESULTS).getAsJsonArray();
                for (Object video: results){
                    if (video instanceof JsonObject){
                        JsonObject videoData = (JsonObject) video;
                        if (videoData.has(JSON_SITE) && videoData.has(JSON_TYPE) && videoData.has(JSON_KEY))
                            if(videoData.get(JSON_SITE).getAsString().equals(JSON_YOUTUBE)
                                    && videoData.get(JSON_TYPE).getAsString().equals(JSON_TRAILER)){
                                trailer=videoData.get(JSON_KEY).getAsString();
                                break;
                            }
                        }
                }
                if (trailer==null) trailer="";
            }
        }
    }

    public String getPosterURL(){
        return RequestsData.POSTER_IMAGE_BASE_URL+this.posterPath;
    }

    public String getBackdropURL(){
        return RequestsData.BACKDROP_IMAGE_BASE_URL+this.backdropPath;
    }

    public int getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getTagline() {
        return tagline;
    }

    public LinkedHashMap<String, String> getCast() {
        return cast;
    }

    public boolean hasDetails(){
        return cast!=null && trailer!= null;
    }

}

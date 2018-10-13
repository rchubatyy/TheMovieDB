package com.roman.themoviedb.api;

/**
 * Created by Roman on 06/04/2018.
 */

public class RequestsData {

    private RequestsData(){}
    //MovieDB API data
    public final static String API_BASE_URL = "https://api.themoviedb.org/3/";
    public final static String API_KEY = "9c8a3badf8660ed8e756140ea8afda51";
    public final static String POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300";
    public final static String BACKDROP_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    public final static String CAST_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    //Youtube API key obtained through Google Developer Console by me
    public final static String YOUTUBE_API_KEY = "AIzaSyAjn07vpFM5R39B9IVNvWOH6RD9vsTtp9c";
    // API request codes
    public final static int DISCOVER_REQUEST_CODE = 1;
    public final static int SEARCH_REQUEST_CODE = 2;
    public final static int DETAILS_REQUEST_CODE = 3;
}

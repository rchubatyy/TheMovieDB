package com.roman.themoviedb.activities;


import android.content.Intent;
import android.os.Bundle;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.roman.themoviedb.R;
import com.roman.themoviedb.api.RequestsData;


public class YoutubeTrailerView extends YouTubeBaseActivity
implements YouTubePlayer.OnInitializedListener{

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_trailer_view);

        Intent intent = getIntent();
        link = intent.getStringExtra("link");

        YouTubePlayerView player = findViewById(R.id.player);
        player.initialize(RequestsData.YOUTUBE_API_KEY, this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        youTubePlayer.loadVideo(link);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}

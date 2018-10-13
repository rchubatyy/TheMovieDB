package com.roman.themoviedb.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roman.themoviedb.models.Movie;
import com.roman.themoviedb.R;
import com.roman.themoviedb.adapters.CastAdapter;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity
    implements View.OnClickListener, CastAdapter.OnActorClickedListener{

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(movie.getTitle());

        ImageView backdrop = findViewById(R.id.backdrop);
        FloatingActionButton playTrailerButton = findViewById(R.id.play_trailer);
        if (!movie.getTrailer().equals(""))
            playTrailerButton.setOnClickListener(this);
        else
            playTrailerButton.setVisibility(View.GONE);
        ImageView poster = findViewById(R.id.poster);
        TextView rating = findViewById(R.id.rating);
        TextView year = findViewById(R.id.year);
        TextView overview = findViewById(R.id.overview);
        TextView tagline = findViewById(R.id.tagline);
        RecyclerView castView = findViewById(R.id.cast_info);
        CastAdapter castAdapter=new CastAdapter(this,movie.getCast(), this);
        castView.setAdapter(castAdapter);
        castView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        try {
            Picasso.with(this)
                    .load(movie.getBackdropURL())
                    .into(backdrop);
            Picasso.with(this)
                    .load(movie.getPosterURL())
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .into(poster);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String ratingString = ""+movie.getRating();
        rating.setText(ratingString);
        String releaseDate = movie.getReleaseDate();
        year.setText(releaseDate.length()>=4 ?  releaseDate.substring(0,4) : "");
        overview.setText(movie.getOverview());
        overview.setMovementMethod(new ScrollingMovementMethod());
        tagline.setVisibility(movie.getTagline().equals("")?View.GONE : View.VISIBLE);
        tagline.setText(movie.getTagline());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            super.onBackPressed();
            supportFinishAfterTransition();
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.play_trailer){
            Intent intent = new Intent(this, YoutubeTrailerView.class);
            intent.putExtra("link",movie.getTrailer());
            startActivity(intent);
        }
    }

    @Override
    public void onActorClicked(int position, View v) {
        Toast.makeText(this, getResources().getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
    }
}

package com.roman.themoviedb.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.roman.themoviedb.models.Movie;
import com.roman.themoviedb.R;
import com.roman.themoviedb.adapters.MovieAdapter;
import com.roman.themoviedb.api.ApiClient;
import com.roman.themoviedb.api.RequestsData;

import org.parceler.Parcels;
import java.util.ArrayList;

/**
 * Home screen activity. Search screen is implement here, too.
 */
public class HomeScreenActivity extends AppCompatActivity
implements BottomNavigationView.OnNavigationItemSelectedListener,
        MenuItem.OnActionExpandListener,
        SearchView.OnQueryTextListener,
        ApiClient.ApiCallbacks,
        MovieAdapter.OnPosterClickedListener{

    private static final String DESC = ".desc";

    private TextView errorMessage;
    private RecyclerView movieInfo;
    private BottomNavigationView navigationBar;
    private GridLayoutManager gridLayoutManager;
    // Sorting criteria
    private enum SortCriteria{
        popularity,release_date,vote_average
    }
    private SortCriteria criteria = SortCriteria.popularity;

    private MovieAdapter movieAdapter;

    private ArrayList<Movie> movies = new ArrayList<>();
    // Control variables
    private int page = 1; // Number of last page loaded or being loaded.
    private int totalPages = 0; // Number of
    private String searchQuery = "";
    private boolean loadingNextPage = false; //
    private boolean search = false;
    private boolean canOpen = true;
    private boolean firstTime = true;
    private int movieIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // Make back button appear on search screen.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        errorMessage = findViewById(R.id.error);
        // Divide the posters RecyclerView into columns
        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                gridLayoutManager = new GridLayoutManager(this, 3);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                gridLayoutManager = new GridLayoutManager(this, 6);
                break;
        }
        movieInfo = findViewById(R.id.movie_info);
        movieAdapter = new MovieAdapter(this,movies, this);
        movieInfo.setAdapter(movieAdapter);
        movieInfo.setLayoutManager(gridLayoutManager);
        // Enable pagination
        movieInfo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!loadingNextPage) // Do not make a new request while still fetching
                    if (page<totalPages){ // Do not make a new request when all pages are loaded
                    int visible = gridLayoutManager.getChildCount();
                    int total = gridLayoutManager.getItemCount();
                    int pastVisible = gridLayoutManager.findFirstVisibleItemPosition();
                    if (pastVisible + visible >= total && pastVisible >= 0 && dy > 0)  // Fetch more when scrolling down to the end
                        loadNextPage();
                    }
                }
        });

        navigationBar = findViewById(R.id.navigation_bar);
        navigationBar.setVisibility(View.GONE);
        navigationBar.setOnNavigationItemSelectedListener(this);
        // Run the first request
        ApiClient.discoverMoviesRequest(this, criteria.name()+DESC, 1, 0);
    }

    @Override
    public void onResume(){
        super.onResume();
        canOpen=true; // Enables movie selecting when opening/returning to activity.
    }

    /**
     * Change number of columns when rotating phone
     * @param newConfig data with orientation
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch(newConfig.orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                gridLayoutManager = new GridLayoutManager(this, 3);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                gridLayoutManager = new GridLayoutManager(this, 6);
                break;
        }
        movieInfo.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search_button);
        item.setOnActionExpandListener(this);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.insert_a_keyword));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.most_popular:
                if (criteria!=SortCriteria.popularity || firstTime) {
                    criteria = SortCriteria.popularity; // Do not make a new request when item is already selected
                    break;
                }
                else return false;
            case R.id.most_recent:
                if (criteria!=SortCriteria.release_date || firstTime) {
                    criteria = SortCriteria.release_date;
                    break;
                }
                else return false;
            case R.id.best_rated:
                if (criteria!=SortCriteria.vote_average || firstTime) {
                    criteria = SortCriteria.vote_average;
                    break;
                }
                else return false;
            default:
                return false;
        }
        clear();
        // Disables switching navigation items until fetching is complete.
        setBottomNavigationViewEnabled(false);
        ApiClient.discoverMoviesRequest(this, criteria.name()+DESC, 1, 0);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchQuery=newText;
        if (!newText.equals(""))
            ApiClient.searchMoviesRequest(this, newText, 1);
        else clear();
        return true;
    }

    /**
     *
     * @param requestCode code of operation
     * @param objects objects fetched from JSON
     * @param numPages total number of pages available
     */
    @Override
    public void onApiClientSuccess(int requestCode, ArrayList<Movie> objects, int numPages) {
        int oldSize = movies.size();
        switch(requestCode){
            case RequestsData.DISCOVER_REQUEST_CODE:
                if (firstTime){
                    navigationBar.setSelectedItemId(navigationBar.getSelectedItemId());
                    firstTime=false;
                    return;
                }

                if (objects.size()==0 && page==1) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(getResources().getString(R.string.api_error));
                    setBottomNavigationViewEnabled(true);
                    navigationBar.setVisibility(View.VISIBLE);
                }
                else {
                    errorMessage.setVisibility(View.GONE);
                    movies.addAll(objects);
                    movieAdapter.notifyItemRangeInserted(oldSize, objects.size());
                    loadingNextPage = false;
                    if (totalPages==0) // Load total number of pages on first loading.
                        totalPages=numPages;
                    setBottomNavigationViewEnabled(true);
                    navigationBar.setVisibility(View.VISIBLE);
                    navigationBar.getMenu().getItem(criteria.ordinal()).setChecked(true);
                }
                break;
            case RequestsData.SEARCH_REQUEST_CODE:
                if (!loadingNextPage) clear();
                String noResults = getResources().getString(R.string.no_search_results)+" "+searchQuery;
                if (objects.size()==0 && page==1 && !searchQuery.equals("")) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(noResults);
                }
                else {
                    errorMessage.setVisibility(View.GONE);
                    if (searchQuery.equals(""))
                        clear();
                    else {
                        movies.addAll(objects);
                        movieAdapter.notifyItemRangeInserted(oldSize, objects.size());
                        loadingNextPage = false;
                        if (totalPages==0)
                            totalPages=numPages;
                    }
                }
                break;
            case RequestsData.DETAILS_REQUEST_CODE:
                movies.set(movieIndex, objects.get(0)); // Copy cast and trailer data to model so it is not needed to be loaded again.
                showDetails(movieIndex);
                break;
        }
    }

    /**
     * Runs on fetching error (e.g. too many requests)
     */
    @Override
    public void onApiClientError() {
        firstTime=false;
        if (movies.isEmpty()){
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText(getResources().getString(R.string.api_error));
            setBottomNavigationViewEnabled(true);
            if(!search)
                navigationBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Runs when missing Internet connection
     */
    @Override
    public void onFailedResponse() {
        firstTime=false;
        if (movies.isEmpty()){
            this.errorMessage.setVisibility(View.VISIBLE);
            this.errorMessage.setText(getResources().getString(R.string.no_connection_error));
            setBottomNavigationViewEnabled(true);
            if(!search)
                navigationBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        clear();
        search = true;
        navigationBar.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        clear();
        search = false;
        ApiClient.discoverMoviesRequest(this, criteria.name()+DESC,  1, 0); // Load movies again.
        navigationBar.setVisibility(View.VISIBLE);
        return true;
    }

    /**
     * Clear recycler view and reset the page control variables
     */
    private void clear(){
        page = 1;
        totalPages=0;
        movies.clear();
        errorMessage.setVisibility(View.GONE);
        movieAdapter.notifyDataSetChanged();
    }

    private void loadNextPage(){
        loadingNextPage = true;
        if (search)
            ApiClient.searchMoviesRequest(this, searchQuery,++page);
        else
            ApiClient.discoverMoviesRequest(this, criteria.name()+DESC, ++page, 0);
    }

    @Override
    public void onPosterClicked(int position) {
        movieIndex=position; // Copu movie position to load details into model when ready.
        if (!movies.get(position).hasDetails()) // If cast and trailers linked are already on model, open the screen immediately.
        ApiClient.getMovieRequest(this, movies.get(position).getId());
        else showDetails(position);
    }

    /**
     * Open the movie details screen.
     * @param position of the movie on the recycler view
     */
    private void showDetails(int position){
        if (canOpen) {
            canOpen = false; // Prevent movie details activity for starting more than once.
            Intent movieDetails = new Intent(this, MovieDetailActivity.class);
            final Bundle bundle = new Bundle();
            bundle.putParcelable("movie", Parcels.wrap(movies.get(position)));
            movieDetails.putExtras(bundle);
            startActivity(movieDetails);
        }
    }

    /**
     * Enable or disable the bottom navigation view
     * @param status true or false.
     */
    private void setBottomNavigationViewEnabled(boolean status){
        navigationBar.setEnabled(status);
        navigationBar.setFocusable(status);
        navigationBar.setFocusableInTouchMode(status);
        navigationBar.setClickable(status);
        navigationBar.setOnNavigationItemSelectedListener(status ? this : null);
    }

}

package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.android.popularmoviesstage1.ImageAdapter.ImageAdapterClickHandler;
import com.example.android.popularmoviesstage1.favouritesData.Contract;
import com.example.android.popularmoviesstage1.favouritesData.FavouritesProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

//TODO: implement onSaveInstanceState/onRestoreInstanceState

public class MainActivity extends AppCompatActivity
        implements ImageAdapterClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER = 5;
    private static final int CURSOR_LOADER = 10;
    private static final String SORT_BY_KEY = "SORT_BY_KEY";
    private static String mSortBy;
    public ImageAdapter mAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;
    private Bundle mBundle;
    private Context mContext;
    private MovieLoader mMovieLoader;
    private CursorLoader mCursorLoader;
    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of ImageAdapter to send the reference and data to Adapter
        mAdapter = new ImageAdapter(this, new ArrayList<MovieItem>(), this);
        recyclerView.setAdapter(mAdapter);

        setupSharedPreferences();

        //create Bundle for Loader
        mBundle = new Bundle();
        mBundle.putString(SORT_BY_KEY, mSortBy);

        mMovieLoader = new MovieLoader(this);
        mCursorLoader = new CursorLoader(this, mAdapter);
        mLoaderManager = getLoaderManager();

        checkConnectivityAndInitialiseLoader(mBundle);

    }

    private void checkConnectivityAndInitialiseLoader(Bundle loaderBundle) {
        //check connectivity and display "No Network Connection" if no connection
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            //initialise Loader
            Loader<ArrayList<MovieItem>> movieItemLoader = mLoaderManager.getLoader(MOVIE_LOADER);
            if (movieItemLoader == null) {
                mLoaderManager.initLoader(MOVIE_LOADER, loaderBundle, mMovieLoader);
            } else {
                mLoaderManager.restartLoader(MOVIE_LOADER, loaderBundle, mMovieLoader);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    //set up Settings option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_all_movies:
                mAdapter = new ImageAdapter(this, new ArrayList<MovieItem>(), this);
                recyclerView.setAdapter(mAdapter);
                getLoaderManager().restartLoader(MOVIE_LOADER, mBundle, mMovieLoader);
                break;
            case R.id.action_query_favourites:
                mCursorLoader = new CursorLoader(this, mAdapter);
                Loader<Cursor> cursorItemLoader = mLoaderManager.getLoader(CURSOR_LOADER);
                if (cursorItemLoader == null) {
                    mLoaderManager.initLoader(CURSOR_LOADER, null, mCursorLoader);
                } else {
                    mLoaderManager.restartLoader(CURSOR_LOADER, null, mCursorLoader);
                }
                break;
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //override the RecyclerView's onClickMethod, and define intent to open Detail Activity
    @Override
    public void onClickMethod(MovieItem movieItem) {
        Intent startDetailActivity = new Intent(this, DetailActivity.class);
        startDetailActivity.putExtra("parcelledMovieItem", movieItem);
        startActivity(startDetailActivity);
    }

    //set up SharedPreferences and it's listener
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSortBy = sharedPreferences.getString(getString(R.string.sort_by_key),
                getString(R.string.pref_pop_value));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sort_by_key))) {
            mSortBy = sharedPreferences.getString(getString(R.string.sort_by_key),
                    getString(R.string.pref_pop_value));
            //update mSortBy
            mBundle.putString(SORT_BY_KEY, mSortBy);
            //restart Loader with new Bundle
            getLoaderManager().restartLoader(MOVIE_LOADER, mBundle, mMovieLoader);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        //reload the Cursor Loader, in case the cursor has changed.
        mLoaderManager.restartLoader(CURSOR_LOADER, null, mCursorLoader);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    //Loader for acquiring Most Popular or To Rated movies

    class MovieLoader implements LoaderManager.LoaderCallbacks<ArrayList<MovieItem>> {
        //implement Loader callbacks
        public MovieLoader(Context context) {
            mContext = context;
        }

        @Override
        public Loader<ArrayList<MovieItem>> onCreateLoader(int i, final Bundle args) {
            return new AsyncTaskLoader<ArrayList<MovieItem>>(mContext) {

                ArrayList<MovieItem> mMovieItems;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                    ProgressBar progressBar = findViewById(R.id.progress_bar);
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }

                @Override
                public ArrayList<MovieItem> loadInBackground() {
                    //get mSortBy preference from Bundle
                    String sortByString = args.getString(SORT_BY_KEY);
                    //build Url from mSortBy string
                    URL url = NetworkUtils.buildUrl(sortByString, MainActivity.this);

                    try {
                        String queriedJsonResponse = NetworkUtils.makeHttpRequest(url);
                        ArrayList<MovieItem> queriedMovieItems = NetworkUtils.extractFeatureFromJson(queriedJsonResponse);
                        return queriedMovieItems;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.v(LOG_TAG, "Problem with network connection");
                        return null;
                    }
                }

                @Override
                public void deliverResult(ArrayList<MovieItem> data) {
                    mMovieItems = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> queriedMovieItems) {
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            if (queriedMovieItems == null) {
                emptyView.setText(R.string.no_results);
            } else {
                mAdapter.setMovieArrayData(queriedMovieItems);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {
        }
    }
}

package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class MainActivity extends AppCompatActivity
        implements ImageAdapterClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static ImageAdapter mAdapter;
    private static String mSortBy;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the reference of RecyclerView and EmptyView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of ImageAdapter to send the reference and data to Adapter
        mAdapter = new ImageAdapter(this, new ArrayList<MovieItem>(), this);
        recyclerView.setAdapter(mAdapter);

        setupSharedPreferences();

        //check connectivity and display "No Network Connection" if no connection
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            //call the Async method to make connection to given API in background
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute(NetworkUtils.buildUrl(mSortBy, this));
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
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
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
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute(NetworkUtils.buildUrl(mSortBy, this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
        .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    //TODO: change this to a Loader for greater lifecycle handling
    //TODO: cache Loader data
    public class MovieAsyncTask extends AsyncTask<URL, Void, ArrayList<MovieItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }

        //build Url and return a List of movies
        @Override
        protected ArrayList<MovieItem> doInBackground(URL... urls) {
            URL url = urls[0];
            String queriedJsonResponse = null;

            try {
                queriedJsonResponse = NetworkUtils.makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(LOG_TAG, "Problem with network connection");
            }

            ArrayList<MovieItem> queriedMovieItems = NetworkUtils.extractFeatureFromJson(queriedJsonResponse);
            return queriedMovieItems;
        }

        //set the list of movies to the adapter
        @Override
        protected void onPostExecute(ArrayList<MovieItem> queriedMovieItems) {
            super.onPostExecute(queriedMovieItems);
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            if (queriedMovieItems == null) {
                emptyView.setText(R.string.no_results);
            } else {
                mAdapter.setMovieData(queriedMovieItems);
            }
        }
    }
}

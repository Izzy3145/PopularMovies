package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ImageAdapter mAdapter;

    //example URL https://api.themoviedb.org/3/movie/popular?api_key=ccac7cd7a937bc204875001c4924f88a
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final String BASE_PATH = "3/movie";
    private static final String BASE_QUERY = "popular?api_key=ccac7cd7a937bc204875001c4924f88a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of ImageAdapter to send the reference and data to Adapter
        mAdapter = new ImageAdapter(this, new ArrayList<MovieItem>(), this);
        recyclerView.setAdapter(mAdapter);

        //check connectivity
        //ask Connectivity Manager to check the status of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //get status of default network connection
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //if there is a network connection, initialise loader
        if (networkInfo != null && networkInfo.isConnected()) {
            //call the Async method to make connection to given API in background
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute(buildUrl());
        } else {
            //otherwise set the EmptyView to display an appropriate message
            //TODO: polish
            // mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        //a call to the URL Builder in Network Utils
    }

    //a filter-dependent method returning the appropriate URL based on Spinner

    
    //method to build URL
    //TODO: build in sort order option
    public static URL buildUrl() {
        Uri sortOrderUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(BASE_PATH)
                .appendEncodedPath(BASE_QUERY)
                .build();
        try {
            URL sortOrderURL = new URL(sortOrderUri.toString());
            Log.v(LOG_TAG, "URL: " + sortOrderURL);
            return sortOrderURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class MovieAsyncTask extends AsyncTask<URL, Void, ArrayList<MovieItem>> {
        //TODO: polish, override onPreExecute

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

            //TODO: polish, if no results, set EmptyTextView
            mAdapter.setMovieData(queriedMovieItems);
        }
    }}

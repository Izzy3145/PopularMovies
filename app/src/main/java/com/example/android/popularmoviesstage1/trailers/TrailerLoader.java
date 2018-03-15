package com.example.android.popularmoviesstage1.trailers;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.example.android.popularmoviesstage1.DetailActivity;
import com.example.android.popularmoviesstage1.NetworkUtils;
import com.example.android.popularmoviesstage1.trailers.TrailerAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by izzystannett on 15/03/2018.
 */

public class TrailerLoader implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private String LOG_TAG = TrailerLoader.class.getSimpleName();
    private Context mContext;
    private int mId;
    private TrailerAdapter mTrailerAdapter;

    //TODO: add Id into constructor
    //public constructor
    public TrailerLoader(Context context, TrailerAdapter trailerAdapter) {
        this.mContext = context;
        //mId = id;
        mTrailerAdapter = trailerAdapter;
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<String>>(mContext) {

            ArrayList<String> mTrailers;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<String> loadInBackground() {
                //build Url to fetch Trailer Details
                URL url = NetworkUtils.buildUrlForTrailers(mContext);

                try {
                    String queriedJsonResponse = NetworkUtils.makeHttpRequest(url);
                    ArrayList<String> queriedTrailers = NetworkUtils.extractTrailerIdsFromJson
                            (queriedJsonResponse);
                    return queriedTrailers;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v(LOG_TAG, "Problem with network connection");
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<String> data) {
                mTrailers = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> trailerStrings) {
        mTrailerAdapter.setTrailerData(trailerStrings);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }
}

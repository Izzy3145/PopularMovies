package com.example.android.popularmoviesstage1.trailers;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.movies.MovieNetworkUtils;

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
    private TextView mEmptyTextView;

    //public constructor
    public TrailerLoader(Context context, TrailerAdapter trailerAdapter, TextView emptyTextView,
                         int id) {
        this.mContext = context;
        mTrailerAdapter = trailerAdapter;
        mEmptyTextView = emptyTextView;
        mId = id;
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<String>>(mContext) {

            ArrayList<String> mTrailers;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mTrailers != null) {
                    deliverResult(mTrailers);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<String> loadInBackground() {
                //build Url to fetch Trailer Details
                URL url = TrailerNetworkUtils.buildUrlForTrailers(mContext, mId);

                try {
                    String queriedJsonResponse = MovieNetworkUtils.makeHttpRequest(url);
                    ArrayList<String> queriedTrailers = TrailerNetworkUtils.extractTrailerIdsFromJson
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
        if (trailerStrings.size() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mTrailerAdapter.setTrailerData(trailerStrings);
            mEmptyTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }
}

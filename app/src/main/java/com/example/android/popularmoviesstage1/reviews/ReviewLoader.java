package com.example.android.popularmoviesstage1.reviews;

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

public class ReviewLoader implements LoaderManager.LoaderCallbacks<ArrayList<ReviewItem>> {

    private String LOG_TAG = ReviewLoader.class.getSimpleName();
    private Context mContext;
    private int mId;
    private ReviewAdapter mReviewAdapter;

    //TODO: add Id into constructor
    //public constructor
    public ReviewLoader(Context context, ReviewAdapter reviewAdapter) {
        this.mContext = context;
        //mId = id;
        mReviewAdapter = reviewAdapter;
    }

    @Override
    public Loader<ArrayList<ReviewItem>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<ReviewItem>>(mContext) {

            ArrayList<ReviewItem> mReviews;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<ReviewItem> loadInBackground() {
                //build Url to fetch Trailer Details
                URL url = NetworkUtils.buildUrlForReviews(mContext);

                try {
                    String queriedJsonResponse = NetworkUtils.makeHttpRequest(url);
                    ArrayList<ReviewItem> queriedReviews = NetworkUtils.extractReviewsFromJson
                            (queriedJsonResponse);
                    return queriedReviews;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v(LOG_TAG, "Problem with network connection");
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<ReviewItem> data) {
                mReviews = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ReviewItem>> loader, ArrayList<ReviewItem> reviews) {
        mReviewAdapter.setReviewData(reviews);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ReviewItem>> loader) {

    }
}

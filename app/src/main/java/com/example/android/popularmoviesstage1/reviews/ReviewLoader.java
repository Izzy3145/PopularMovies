package com.example.android.popularmoviesstage1.reviews;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.movies.MovieNetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by izzystannett on 15/03/2018.
 */

public class ReviewLoader implements LoaderManager.LoaderCallbacks<ArrayList<ReviewItem>> {

    private String LOG_TAG = ReviewLoader.class.getSimpleName();
    private Context mContext;
    private ReviewAdapter mReviewAdapter;
    private TextView mEmptyTextView;
    private int mId;

    //public constructor
    public ReviewLoader(Context context, ReviewAdapter reviewAdapter, TextView emptyTextView,
                        int id) {
        this.mContext = context;
        mReviewAdapter = reviewAdapter;
        mEmptyTextView = emptyTextView;
        mId = id;
    }

    @Override
    public Loader<ArrayList<ReviewItem>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<ReviewItem>>(mContext) {

            ArrayList<ReviewItem> mReviews;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mReviews != null) {
                    deliverResult(mReviews);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<ReviewItem> loadInBackground() {
                //build Url to fetch Trailer Details
                URL url = ReviewNetworkUtils.buildUrlForReviews(mContext, mId);

                try {
                    String queriedJsonResponse = MovieNetworkUtils.makeHttpRequest(url);
                    ArrayList<ReviewItem> queriedReviews = ReviewNetworkUtils.extractReviewsFromJson
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
        if (reviews.size() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mEmptyTextView.setVisibility(View.GONE);
            mReviewAdapter.setReviewData(reviews);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ReviewItem>> loader) {

    }
}

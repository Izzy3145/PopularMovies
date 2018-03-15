package com.example.android.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.favouritesData.Contract;
import com.example.android.popularmoviesstage1.reviews.ReviewAdapter;
import com.example.android.popularmoviesstage1.reviews.ReviewItem;
import com.example.android.popularmoviesstage1.reviews.ReviewLoader;
import com.example.android.popularmoviesstage1.trailers.TrailerAdapter;
import com.example.android.popularmoviesstage1.trailers.TrailerLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterClickHandler {
    private static final int TRAILER_LOADER = 60;
    private static final int REVIEW_LOADER = 65;
    @BindView(R.id.selectedImage)
    ImageView selectedImage;
    @BindView(R.id.selectedTitle)
    TextView selectedTitle;
    @BindView(R.id.selectedSynopsis)
    TextView selectedSynopsis;
    @BindView(R.id.selectedRating)
    TextView selectedRating;
    @BindView(R.id.selectedDate)
    TextView selectedDate;
    @BindView(R.id.favouritedButton)
    Button btn;
    @BindView(R.id.recyclerViewForTrailers)
    RecyclerView trailerRecyclerView;
    @BindView(R.id.emptyViewTrailers)
    TextView trailerEmptyView;
    @BindView(R.id.recyclerViewForReviews)
    RecyclerView reviewRecyclerView;
    @BindView(R.id.emptyViewReviews)
    TextView reviewEmptyView;
    private MovieItem mParcelledMovieItem;
    private int mMovieTag;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private LoaderManager mLoaderManager;
    private TrailerLoader mTrailerLoader;
    private ReviewLoader mReviewLoader;


    //TODO: https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=ccac7cd7a937bc204875001c4924f88a
    //TODO: implement sharing functionality to allow users to share YouTube videos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //set up ActionBar for Up button
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //get MovieItem from intent and set to views
        Intent intent = getIntent();
        mParcelledMovieItem = intent.getParcelableExtra("parcelledMovieItem");
        setItemToViews(mParcelledMovieItem);

        //get tag from movieItem and label the button appropriately
        mMovieTag = mParcelledMovieItem.getTag();
        setButtonText(mMovieTag);

        //set button functionality depending on the button tag
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mMovieTag) {
                    case ImageAdapter.CURSOR_TAG:
                        //if movie is in favourites and button is pressed, delete movie from db
                        Uri uri = Uri.parse(mParcelledMovieItem.getmUriString());
                        getContentResolver().delete(uri,
                                null, null);
                        finish();
                        break;
                    case ImageAdapter.ARRAY_TAG:
                        //if movie is not in favourites and button is pressed, add movie to db
                        ContentValues cv = new ContentValues();
                        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_TITLE, mParcelledMovieItem.getmOriginalTitle());
                        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_IMAGE, mParcelledMovieItem.getmImageUrl());
                        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_RATING, mParcelledMovieItem.getmUserRating());
                        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_RELEASE_DATE, mParcelledMovieItem.getmReleaseDate());
                        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_SYNOPSIS, mParcelledMovieItem.getmPlotSynopsis());

                        //add movieItem to the database via the content resolver
                        getContentResolver().insert(Contract.favouritesEntry.CONTENT_URI, cv);
                        break;
                }
            }
        });

        //set up recycler view for trailers
        LinearLayoutManager linearLayoutManagerForTrailers = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(linearLayoutManagerForTrailers);

        //set up recycler view for reviews
        LinearLayoutManager linearLayoutManagerForReviews = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(linearLayoutManagerForReviews);

        //set up trailer recycler view, adapter and loader
        mTrailerAdapter = new TrailerAdapter(this, new ArrayList<String>(), this);
        trailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerLoader = new TrailerLoader(this, mTrailerAdapter);

        //set up review recycler view, adapter and loader
        mReviewAdapter = new ReviewAdapter(this, new ArrayList<ReviewItem>());
        reviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewLoader = new ReviewLoader(this, mReviewAdapter);

        //get loader manager
        mLoaderManager = getLoaderManager();

        checkConnectivityAndInitialiseLoader(null);
    }

    private void checkConnectivityAndInitialiseLoader(Bundle loaderBundle) {
        //check connectivity and display "No Network Connection" if no connection
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //set up trailer views
            trailerRecyclerView.setVisibility(View.VISIBLE);
            trailerEmptyView.setVisibility(View.GONE);
            //set up reviews views
            reviewRecyclerView.setVisibility(View.VISIBLE);
            reviewEmptyView.setVisibility(View.GONE);
            //initialise Loaders
            Loader<ArrayList<String>> trailerLoader = mLoaderManager.getLoader(TRAILER_LOADER);
            Loader<ArrayList<ReviewItem>> reviewLoader = mLoaderManager.getLoader(REVIEW_LOADER);
            //if trailer loader doesn't exist yet, initialise it, otherwise restart it
            if (trailerLoader == null) {
                mLoaderManager.initLoader(TRAILER_LOADER, loaderBundle, mTrailerLoader);
            } else {
                mLoaderManager.restartLoader(TRAILER_LOADER, loaderBundle, mTrailerLoader);
            }
            //if review loader doesn't exist yet, initialise it, otherwise restart it
            if (reviewLoader == null) {
                mLoaderManager.initLoader(REVIEW_LOADER, loaderBundle, mReviewLoader);
            } else {
                mLoaderManager.restartLoader(REVIEW_LOADER, loaderBundle, mReviewLoader);
            }
        } else {
            //if no connection, make empty views visible
            trailerRecyclerView.setVisibility(View.GONE);
            trailerEmptyView.setVisibility(View.VISIBLE);
            reviewRecyclerView.setVisibility(View.GONE);
            reviewEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setItemToViews(MovieItem movieItem) {
        Picasso.with(this).load(movieItem.getmImageUrl())
                .into(selectedImage);
        selectedTitle.setText(movieItem.getmOriginalTitle());
        selectedSynopsis.setText(movieItem.getmPlotSynopsis());
        selectedRating.setText(Integer.toString(movieItem.getmUserRating()) + "/10");
        selectedDate.setText(movieItem.getmReleaseDate());
    }

    //switch button text between "remove from favourites" and "add to favourites" appropriately
    private void setButtonText(int movieTag) {
        switch (movieTag) {
            case ImageAdapter.CURSOR_TAG:
                btn.setText(R.string.remove_from_favourites);
                break;
            case ImageAdapter.ARRAY_TAG:
                btn.setText(R.string.mark_as_favourite);
                break;
        }
    }

    @Override
    public void onClickMethod(String trailerString) {
        Intent openYoutubeIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailerString));
        startActivity(openYoutubeIntent);
    }

}


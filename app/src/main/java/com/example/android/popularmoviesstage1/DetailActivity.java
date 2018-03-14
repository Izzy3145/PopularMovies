package com.example.android.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.favouritesData.Contract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
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

    private MovieItem mParcelledMovieItem;
    private int mMovieTag;


    //TODO: add Trailers videos(with Intent to YouTube) and user reviews
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mMovieTag) {
                    case ImageAdapter.CURSOR_TAG:
                        Uri uri = Uri.parse(mParcelledMovieItem.getmUriString());
                        getContentResolver().delete(uri,
                                null, null);
                        finish();
                        break;
                    case ImageAdapter.ARRAY_TAG:
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
}

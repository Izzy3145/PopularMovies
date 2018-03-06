package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private Context mContext;

    //TODO: add a button which adds movie to Favourites database
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
        MovieItem parcelledMovieItem = intent.getParcelableExtra("parcelledMovieItem");
        setItemToViews(parcelledMovieItem);
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
        Picasso.with(mContext).load(movieItem.getmImageUrl())
                .into(selectedImage);
        selectedTitle.setText(movieItem.getmOriginalTitle());
        selectedSynopsis.setText(movieItem.getmPlotSynopsis());
        selectedRating.setText(Integer.toString(movieItem.getmUserRating()) + "/10");
        selectedDate.setText(movieItem.getmReleaseDate());
    }
}

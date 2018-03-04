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

public class DetailActivity extends AppCompatActivity {

    Context mContext;
    private ImageView selectedImage;
    private TextView selectedTitle;
    private TextView selectedSynopsis;
    private TextView selectedRating;
    private TextView selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //set up ActionBar for Up button
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //find views in activity_detail.xml
        selectedImage = findViewById(R.id.selectedImage);
        selectedTitle = findViewById(R.id.selectedTitle);
        selectedSynopsis = findViewById(R.id.selectedSynopsis);
        selectedRating = findViewById(R.id.selectedRating);
        selectedDate = findViewById(R.id.selectedDate);

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
        selectedRating.setText(Integer.toString(movieItem.getmUserRating()));
        selectedDate.setText(movieItem.getmReleaseDate());
    }
}

package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.favouritesData.DbUtils;
import com.example.android.popularmoviesstage1.favouritesData.FavouritesDbHelper;
import com.squareup.picasso.Picasso;

import java.sql.SQLInput;

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
    Button favouriteButton;

    private MovieItem mParcelledMovieItem;

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

        final DbUtils dbUtils = new DbUtils(this);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUtils.addFavouriteMovie(mParcelledMovieItem);
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
}

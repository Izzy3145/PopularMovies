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

    private ImageView selectedImage;
    private TextView selectedTitle;
    private TextView selectedSynopsis;
    private TextView selectedRating;
    private TextView selectedDate;
    Context mContext;
    private MovieItem mParcelledMovieItem;
    private String mOriginalTitle;
    private String mImageUrl;
    private String mPlotSynopsis;
    private int mUserRating;
    private String mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //set up ActionBar for Up button
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        selectedTitle = (TextView) findViewById(R.id.selectedTitle);
        selectedSynopsis = (TextView) findViewById(R.id.selectedSynopsis);
        selectedRating = (TextView) findViewById(R.id.selectedRating);
        selectedDate = (TextView) findViewById(R.id.selectedDate);

        //get MovieItem from intent
        Intent intent = getIntent();
        mParcelledMovieItem = intent.getParcelableExtra("parcelledMovieItem");
        mOriginalTitle = mParcelledMovieItem.getmOriginalTitle();
        mImageUrl = mParcelledMovieItem.getmImageUrl();
        mPlotSynopsis = mParcelledMovieItem.getmPlotSynopsis();
        mUserRating = mParcelledMovieItem.getmUserRating();
        mReleaseDate = mParcelledMovieItem.getmReleaseDate();

        Picasso.with(mContext).load(mImageUrl)
                .into(selectedImage);
        selectedTitle.setText(mOriginalTitle);
        selectedSynopsis.setText(mPlotSynopsis);
        selectedRating.setText(Integer.toString(mUserRating));
        selectedDate.setText(mReleaseDate);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
